// CAMBIO: Agregué detección de rol (estudiante u orientador) basado en localStorage.getItem('rol').
// Asume que en login guardas 'rol' como 'estudiante' o 'orientador', y 'idUsuario' o 'idOrientador'.
// Si no, ajusta según tu login.

// Funciones de modales (sin cambios)
function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add("active");
}
function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove("active");
}
window.addEventListener("click", function (e) {
  const modals = document.querySelectorAll(".modal.active");
  modals.forEach(modal => {
    if (e.target === modal) {
      modal.classList.remove("active");
    }
  });
});

// CAMBIO: Nueva función para poblar select de orientadores desde backend.
function populateOrientadores() {
  fetch("http://localhost:8080/api/citas/orientadores")
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById("orientador");
      if (select) { // Solo si existe (para estudiante)
        data.forEach(orientador => {
          const option = document.createElement("option");
          option.value = orientador.id;
          option.textContent = orientador.nombreCompleto;
          select.appendChild(option);
        });
      }
    })
    .catch(err => console.error("Error al cargar orientadores:", err));
}

// CAMBIO: Nueva función para poblar horas (estáticas: 06:00-18:00 cada 30 min).
// Puedes mejorar con fetch a /api/citas/disponibilidad basado en fecha/orientador.
function populateHoras(selectId) {
  const select = document.getElementById(selectId);
  if (select) {
    for (let h = 6; h <= 18; h++) {
      for (let m = 0; m < 60; m += 30) {
        const hourStr = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
        const option = document.createElement("option");
        option.value = hourStr;
        option.textContent = hourStr;
        select.appendChild(option);
      }
    }
  }
}

// CAMBIO: Nueva función para poblar nombre estudiante desde localStorage.
function populateNombreEstudiante() {
  const rol = localStorage.getItem("rol");
  if (rol === "estudiante") {
    const nombre = localStorage.getItem("nombreEstudiante"); // Guarda esto en login.
    const input = document.getElementById("nombre-estudiante");
    if (input && nombre) {
      input.value = nombre;
    } else {
      // Opcional: Fetch desde backend si no está en localStorage.
      console.warn("Nombre de estudiante no encontrado en localStorage.");
    }
  }
}

// CAMBIO: Nueva función para cargar citas en la tabla (diferente endpoint por rol).
function loadCitas() {
  const rol = localStorage.getItem("rol");
  let id = rol === "estudiante" ? localStorage.getItem("idUsuario") : localStorage.getItem("idOrientador");
  id = parseInt(id, 10);
  const endpoint = rol === "estudiante" ? `/api/citas/estudiante/${id}` : `/api/citas/orientador/${id}`;

  fetch(`http://localhost:8080${endpoint}`)
    .then(res => res.json())
    .then(citas => {
      const tbody = document.querySelector(".styled-table tbody");
      if (tbody) {
        tbody.innerHTML = "";
        citas.forEach(cita => {
          const row = document.createElement("tr");
          let estudianteCell = '';
          if (rol === "orientador") {
            estudianteCell = `<td>${cita.nombreEstudiante || 'N/A'}</td>`;
          }
          row.innerHTML = `
            ${estudianteCell}
            <td>${cita.fechaCita}</td>
            <td>${cita.horaCita}</td>
            <td>${cita.motivoOriginal}</td>
            <td>${cita.estado}</td>
            <td>
              <button onclick="verDetalle(${cita.idCita})">Ver</button>
              <button onclick="openModal('${rol === "estudiante" ? 'modal-reprogramar' : 'modal-reprogramar-orientador'}'); setCitaId(${cita.idCita})">Reprogramar</button>
              <button onclick="openModal('${rol === "estudiante" ? 'modal-cancelar' : 'modal-cancelar-orientador'}'); setCitaId(${cita.idCita})">Cancelar</button>
            </td>
          `;
          tbody.appendChild(row);
        });
      }
    })
    .catch(err => console.error("Error al cargar citas:", err));
}

// CAMBIO: Nueva función para ver detalles de cita.
function verDetalle(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}`)
    .then(res => res.json())
    .then(detalle => {
      const content = document.getElementById("detalle-cita-content");
      if (content) {
        content.innerHTML = `
          <p><strong>Fecha:</strong> ${detalle.fechaCita}</p>
          <p><strong>Hora:</strong> ${detalle.horaCita}</p>
          <p><strong>Motivo:</strong> ${detalle.motivoOriginal}</p>
          <p><strong>Estado:</strong> ${detalle.estado}</p>
          <p><strong>Creado:</strong> ${detalle.createdAt}</p>
          <!-- Agrega más si necesitas -->
        `;
        openModal('modal-ver');
      }
    })
    .catch(err => console.error("Error al ver detalle:", err));
}

// CAMBIO: Variable global para ID de cita actual (para reprogramar/cancelar).
let currentCitaId = null;
function setCitaId(id) {
  currentCitaId = id;
}

// CAMBIO: Función guardarCita corregida: Ahora envía claves correctas (idEstudiante, idOrientador, fecha, hora separadas).
// Agregado preventDefault para evitar recarga. Solo para estudiante.
function guardarCita(e) {
  e.preventDefault();
  const rol = localStorage.getItem("rol");
  if (rol !== "estudiante") return;

  const idEstudiante = parseInt(localStorage.getItem("idUsuario"), 10);
  const idOrientador = document.getElementById("orientador").value;
  const fecha = document.getElementById("fecha").value;
  const hora = document.getElementById("hora").value;
  const motivo = document.getElementById("motivo").value;

  if (!idOrientador || !fecha || !hora || !motivo) {
    alert("Completa todos los campos requeridos");
    return;
  }

  const data = { idEstudiante, idOrientador, fecha, hora, motivo };

  fetch("http://localhost:8080/api/citas/crear", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then(res => {
      if (!res.ok) {
        return res.text().then(text => { throw new Error(text); });
      }
      return res.json();
    })
    .then(response => {
      alert(response.message || "Cita creada con éxito");
      closeModal('modal-crear');
      loadCitas(); // Recarga tabla
    })
    .catch(err => {
      console.error("Error al crear cita:", err);
      alert("Error: " + err.message);
    });
}

// CAMBIO: Nueva función para reprogramar (PUT al backend). Adaptada por rol.
function reprogramarCita(e) {
  e.preventDefault();
  const rol = localStorage.getItem("rol");
  const fechaId = rol === "estudiante" ? "fecha_reprogramar" : "fecha_reprogramar_o";
  const horaId = rol === "estudiante" ? "hora_reprogramar" : "hora_reprogramar_o";
  const nuevaFecha = document.getElementById(fechaId).value;
  const nuevaHora = document.getElementById(horaId).value;

  if (!currentCitaId || !nuevaFecha || !nuevaHora) {
    alert("Selecciona una cita y completa los campos");
    return;
  }

  const data = { fecha: nuevaFecha, hora: nuevaHora };

  fetch(`http://localhost:8080/api/citas/${currentCitaId}/reprogramar`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then(res => {
      if (!res.ok) throw new Error("Error al reprogramar");
      return res.json();
    })
    .then(response => {
      alert(response.message || "Cita reprogramada");
      closeModal(rol === "estudiante" ? 'modal-reprogramar' : 'modal-reprogramar-orientador');
      loadCitas();
    })
    .catch(err => alert("Error: " + err.message));
}

// CAMBIO: Nueva función para cancelar (PUT al backend). Adaptada por rol.
function cancelarCita(e) {
  if (e) e.preventDefault();
  if (!currentCitaId) {
    alert("Selecciona una cita");
    return;
  }

  const rol = localStorage.getItem("rol");
  const motivo = rol === "orientador" ? document.getElementById("motivo-cancelacion").value : ""; // Opcional para orientador

  fetch(`http://localhost:8080/api/citas/${currentCitaId}/cancelar`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ motivo }), // Si backend lo soporta, sino quita.
  })
    .then(res => {
      if (!res.ok) throw new Error("Error al cancelar");
      return res.json();
    })
    .then(response => {
      alert(response.message || "Cita cancelada");
      closeModal(rol === "estudiante" ? 'modal-cancelar' : 'modal-cancelar-orientador');
      loadCitas();
    })
    .catch(err => alert("Error: " + err.message));
}

// CAMBIO: Nueva función para aprobar (solo orientador, desde modal).
function aprobarCita(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}/aprobar`, {
    method: "PUT",
  })
    .then(res => {
      if (!res.ok) throw new Error("Error al aprobar");
      return res.json();
    })
    .then(response => {
      alert(response.message || "Cita aprobada");
      loadCitasPendientes(); // Recarga pendientes si abierto
      loadCitas();
    })
    .catch(err => alert("Error: " + err.message));
}

// CAMBIO: Nueva función para cargar pendientes en modal aprobar (solo orientador).
function loadCitasPendientes() {
  const idOrientador = parseInt(localStorage.getItem("idOrientador"), 10);
  fetch(`http://localhost:8080/api/citas/orientador/${idOrientador}/pendientes`)
    .then(res => res.json())
    .then(citas => {
      const tbody = document.getElementById("tabla-pendientes-body");
      if (tbody) {
        tbody.innerHTML = "";
        citas.forEach(cita => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td>${cita.nombreEstudiante}</td>
            <td>${cita.fechaCita}</td>
            <td>${cita.horaCita}</td>
            <td>${cita.motivoOriginal}</td>
            <td><button onclick="aprobarCita(${cita.idCita})">Aprobar</button></td>
          `;
          tbody.appendChild(row);
        });
      }
    })
    .catch(err => console.error("Error al cargar pendientes:", err));
}

// CAMBIO: Funciones para modales orientador.
function abrirModalAprobar() {
  loadCitasPendientes();
  openModal('modal-aprobar-orientador');
}
function abrirModalReprogramarOrientador() {
  // CAMBIO: Poblar select de citas para reprogramar.
  loadCitasIntoSelect('cita-reprogramar-select');
  populateHoras('hora_reprogramar_o');
  openModal('modal-reprogramar-orientador');
}
function abrirModalCancelarOrientador() {
  // CAMBIO: Poblar select de citas para cancelar.
  loadCitasIntoSelect('cita-cancelar-select');
  openModal('modal-cancelar-orientador');
}

// CAMBIO: Nueva función helper para poblar selects con citas (para orientador reprogramar/cancelar).
function loadCitasIntoSelect(selectId) {
  const idOrientador = parseInt(localStorage.getItem("idOrientador"), 10);
  fetch(`http://localhost:8080/api/citas/orientador/${idOrientador}`)
    .then(res => res.json())
    .then(citas => {
      const select = document.getElementById(selectId);
      if (select) {
        select.innerHTML = '<option value="">Selecciona una cita</option>';
        citas.forEach(cita => {
          if (cita.estado !== 'CANCELADA') { // Solo no canceladas
            const option = document.createElement("option");
            option.value = cita.idCita;
            option.textContent = `${cita.nombreEstudiante} - ${cita.fechaCita} ${cita.horaCita}`;
            select.appendChild(option);
          }
        });
      }
    })
    .catch(err => console.error("Error al cargar citas en select:", err));
}

// Filtros (stub: filtra client-side; puedes mejorar con params en fetch).
function aplicarFiltros() {
  // TODO: Implementa filtrado en tabla o agrega params a loadCitas.
  console.log("Filtros aplicados");
}
function limpiarFiltros() {
  // Limpia inputs y recarga.
  document.querySelectorAll('.filters-container input, .filters-container select').forEach(el => el.value = '');
  loadCitas();
}

// CAMBIO: EjecutarCancelar ahora llama a cancelarCita (para modal estudiante).
function ejecutarCancelar() {
  cancelarCita();
}

// On load
document.addEventListener("DOMContentLoaded", () => {
  const rol = localStorage.getItem("rol");
  populateOrientadores();
  populateHoras("hora"); // Para crear (estudiante)
  populateHoras("hora_reprogramar"); // Para reprogramar (estudiante)
  populateNombreEstudiante();
  loadCitas();

  // Listeners para forms
  const formCrear = document.getElementById("formCrearCita");
  if (formCrear) formCrear.addEventListener("submit", guardarCita);

  const formReprogramar = document.getElementById("formReprogramarCita");
  const formReprogramarO = document.getElementById("formReprogramarOrientador");
  if (formReprogramar) formReprogramar.addEventListener("submit", reprogramarCita);
  if (formReprogramarO) formReprogramarO.addEventListener("submit", reprogramarCita);

  const formCancelarO = document.getElementById("formCancelarOrientador");
  if (formCancelarO) formCancelarO.addEventListener("submit", cancelarCita);
});