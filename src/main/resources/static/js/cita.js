// ======================== MODALES ========================
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
    if (e.target === modal) modal.classList.remove("active");
  });
});

// ======================== CARGA DE DATOS DEL USUARIO ========================
async function cargarDatosUsuario() {
  try {
    const res = await fetch("http://localhost:8080/api/auth/me", {
      credentials: "include"
    });
    if (!res.ok) throw new Error("No autenticado");

    const user = await res.json();

    // Guardamos todo lo necesario
    const rol = user.rol === 2 ? "estudiante" : "orientador";
    localStorage.setItem("rol", rol);
    localStorage.setItem("idUsuario", user.idUsuario);
    localStorage.setItem("nombreCompleto", user.nombreCompleto || "Usuario");

    // Solo estudiante necesita nombre en el input
    const inputNombre = document.getElementById("nombre-estudiante");
    if (inputNombre) {
      inputNombre.value = user.nombreCompleto;
    }
  } catch (err) {
    console.warn("No se pudo cargar usuario (puede ser normal en login):", err);
  }
}

// ======================== POBLAR ORIENTADORES Y HORAS ========================
function populateOrientadores() {
  fetch("http://localhost:8080/api/citas/orientadores")
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById("orientador");
      if (select) {
        data.forEach(o => {
          const opt = document.createElement("option");
          opt.value = o.id;
          opt.textContent = o.nombreCompleto;
          select.appendChild(opt);
        });
      }
    })
    .catch(err => console.error("Error cargando orientadores:", err));
}

function populateHoras(selectId) {
  const select = document.getElementById(selectId);
  if (!select) return;
  select.innerHTML = '<option value="">Hora</option>';
  for (let h = 6; h <= 18; h++) {
    for (let m = 0; m < 60; m += 30) {
      const time = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
      const opt = document.createElement("option");
      opt.value = time;
      opt.textContent = time;
      select.appendChild(opt);
    }
  }
}

// ======================== CARGAR CITAS (ESTUDIANTE Y ORIENTADOR) ========================
let currentCitaId = null;
function setCitaId(id) {
  currentCitaId = id;
}

function loadCitas() {
  const rol = localStorage.getItem("rol");
  const idUsuario = localStorage.getItem("idUsuario");

  if (!rol || !idUsuario) {
    console.error("Falta rol o idUsuario en localStorage");
    return;
  }

  const endpoint = rol === "estudiante"
    ? `/api/citas/estudiante/${idUsuario}`
    : `/api/citas/orientador/${idUsuario}`;

  fetch(`http://localhost:8080${endpoint}`, { credentials: "include" })
    .then(res => {
      if (!res.ok) throw new Error("Error en endpoint de citas");
      return res.json();
    })
    .then(citas => {
      const tbody = document.querySelector(".styled-table tbody");
      if (!tbody) return;
      tbody.innerHTML = "";

      citas.forEach(cita => {
        const row = document.createElement("tr");

        const estudianteCell = rol === "orientador"
          ? `<td>${cita.nombreEstudiante || "Sin nombre"}</td>`
          : "";

        const acciones = `
          <button class="btn-ver" onclick="verDetalle(${cita.idCita})">Ver</button>
          ${cita.estado !== "CANCELADA" && cita.estado !== "FINALIZADA" ? `
            <button class="btn-reprogramar" onclick="abrirReprogramar(${cita.idCita})">Reprogramar</button>
            <button class="btn-cancelar" onclick="setCitaId(${cita.idCita}); openModal('${rol === 'estudiante' ? 'modal-cancelar' : 'modal-cancelar-orientador'}')">Cancelar</button>
          ` : ""}
        `;

        row.innerHTML = `
          ${estudianteCell}
          <td>${cita.fechaCita}</td>
          <td>${cita.horaCita}</td>
          <td>${cita.motivoOriginal}</td>
          <td><span class="estado estado-${cita.estado.toLowerCase()}">${cita.estado}</span></td>
          <td class="acciones">${acciones}</td>
        `;
        tbody.appendChild(row);
      });
    })
    .catch(err => {
      console.error("Error cargando citas:", err);
      alert("Error al cargar citas. Revisa la consola.");
    });
}

// ======================== DETALLE DE CITA ========================
function verDetalle(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}`, { credentials: "include" })
    .then(res => res.json())
    .then(detalle => {
      const content = document.getElementById("detalle-cita-content");
      if (content) {
        content.innerHTML = `
          <p><strong>Fecha:</strong> ${detalle.fechaCita}</p>
          <p><strong>Hora:</strong> ${detalle.horaCita}</p>
          <p><strong>Motivo:</strong> ${detalle.motivoOriginal}</p>
          <p><strong>Estado:</strong> ${detalle.estado}</p>
          <p><strong>Creada:</strong> ${new Date(detalle.createdAt).toLocaleString()}</p>
        `;
        openModal("modal-ver");
      }
    });
}

// ======================== CREAR CITA (ESTUDIANTE) ========================
function guardarCita(e) {
  e.preventDefault();
  if (localStorage.getItem("rol") !== "estudiante") return;

  const idEstudiante = localStorage.getItem("idUsuario");
  const idOrientador = document.getElementById("orientador").value;
  const fecha = document.getElementById("fecha").value;
  const hora = document.getElementById("hora").value;
  const motivo = document.getElementById("motivo").value;

  if (!idOrientador || !fecha || !hora || !motivo) {
    alert("Completa todos los campos");
    return;
  }

  fetch("http://localhost:8080/api/citas/crear", {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ idEstudiante, idOrientador, fecha, hora, motivo })
  })
    .then(res => res.ok ? res.json() : Promise.reject("Error al crear"))
    .then(() => {
      alert("Cita solicitada con éxito");
      closeModal("modal-crear");
      loadCitas();
    })
    .catch(err => alert("Error: " + err.message || err));
}

// ======================== REPROGRAMAR (AMBOS ROLES) ========================
function abrirReprogramar(idCita) {
  setCitaId(idCita);
  const rol = localStorage.getItem("rol");
  populateHoras(rol === "estudiante" ? "hora_reprogramar" : "hora_reprogramar_o");
  openModal(rol === "estudiante" ? "modal-reprogramar" : "modal-reprogramar-orientador");
}

function reprogramarCita(e) {
  e.preventDefault();
  if (!currentCitaId) return alert("No hay cita seleccionada");

  const rol = localStorage.getItem("rol");
  const fechaInput = rol === "estudiante" ? "fecha_reprogramar" : "fecha_reprogramar_o";
  const horaInput = rol === "estudiante" ? "hora_reprogramar" : "hora_reprogramar_o";

  const fecha = document.getElementById(fechaInput).value;
  const hora = document.getElementById(horaInput).value;

  if (!fecha || !hora) return alert("Selecciona fecha y hora");

  fetch(`http://localhost:8080/api/citas/${currentCitaId}/reprogramar`, {
    method: "PUT",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fecha, hora })
  })
    .then(res => res.ok ? res.json() : Promise.reject("Error"))
    .then(() => {
      alert("Cita reprogramada");
      closeModal(rol === "estudiante" ? "modal-reprogramar" : "modal-reprogramar-orientador");
      loadCitas();
    })
    .catch(() => alert("Error al reprogramar"));
}

// ======================== CANCELAR (AMBOS ROLES) ========================
function cancelarCita(e) {
  if (e) e.preventDefault();
  if (!currentCitaId) return alert("Selecciona una cita");

  const rol = localStorage.getItem("rol");
  const motivo = rol === "orientador" ? document.getElementById("motivo-cancelacion")?.value : "";

  fetch(`http://localhost:8080/api/citas/${currentCitaId}/cancelar`, {
    method: "PUT",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ motivo })
  })
    .then(res => res.ok ? res.json() : Promise.reject())
    .then(() => {
      alert("Cita cancelada");
      closeModal(rol === "estudiante" ? "modal-cancelar" : "modal-cancelar-orientador");
      loadCitas();
    })
    .catch(() => alert("Error al cancelar"));
}

function ejecutarCancelar() {
  cancelarCita();
}

// ======================== APROBAR CITAS (ORIENTADOR) ========================
function aprobarCita(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}/aprobar`, {
    method: "PUT",
    credentials: "include"
  })
    .then(res => res.ok ? res.json() : Promise.reject())
    .then(() => {
      alert("Cita aprobada");
      loadCitasPendientes();
      loadCitas();
    })
    .catch(() => alert("Error al aprobar"));
}

function loadCitasPendientes() {
  const idUsuario = localStorage.getItem("idUsuario");
  fetch(`http://localhost:8080/api/citas/orientador/${idUsuario}/pendientes`, { credentials: "include" })
    .then(res => res.json())
    .then(citas => {
      const tbody = document.getElementById("tabla-pendientes-body");
      if (!tbody) return;
      tbody.innerHTML = "";
      citas.forEach(cita => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${cita.nombreEstudiante}</td>
          <td>${cita.fechaCita}</td>
          <td>${cita.horaCita}</td>
          <td>${cita.motivoOriginal}</td>
          <td><button class="btn-aprobar" onclick="aprobarCita(${cita.idCita})">Aprobar</button></td>
        `;
        tbody.appendChild(row);
      });
    });
}

function abrirModalAprobar() {
  loadCitasPendientes();
  openModal("modal-aprobar-orientador");
}

// ======================== REPROGRAMAR/CANCELAR ORIENTADOR CON SELECT ========================
function loadCitasIntoSelect(selectId) {
  const idUsuario = localStorage.getItem("idUsuario");
  fetch(`http://localhost:8080/api/citas/orientador/${idUsuario}`, { credentials: "include" })
    .then(res => res.json())
    .then(citas => {
      const select = document.getElementById(selectId);
      if (!select) return;
      select.innerHTML = '<option value="">Selecciona una cita</option>';
      citas.forEach(cita => {
        if (cita.estado !== "CANCELADA") {
          const opt = document.createElement("option");
          opt.value = cita.idCita;
          opt.textContent = `${cita.nombreEstudiante} - ${cita.fechaCita} ${cita.horaCita}`;
          select.appendChild(opt);
        }
      });
    });
}

function abrirModalReprogramarOrientador() {
  loadCitasIntoSelect("cita-reprogramar-select");
  populateHoras("hora_reprogramar_o");
  openModal("modal-reprogramar-orientador");
}

function abrirModalCancelarOrientador() {
  loadCitasIntoSelect("cita-cancelar-select");
  openModal("modal-cancelar-orientador");
}

// Escuchar cambio en selects del orientador
document.getElementById("cita-reprogramar-select")?.addEventListener("change", e => setCitaId(e.target.value));
document.getElementById("cita-cancelar-select")?.addEventListener("change", e => setCitaId(e.target.value));

// ======================== FILTROS ========================
function aplicarFiltros() { console.log("Filtros (implementar)"); }
function limpiarFiltros() {
  document.querySelectorAll('.filters-container input, .filters-container select').forEach(el => el.value = '');
  loadCitas();
}

// ======================== ON LOAD ========================
document.addEventListener("DOMContentLoaded", () => {
  cargarDatosUsuario();
  populateOrientadores();
  populateHoras("hora");
  populateHoras("hora_reprogramar");
  populateHoras("hora_reprogramar_o");
  loadCitas();

  // Forms
  document.getElementById("formCrearCita")?.addEventListener("submit", guardarCita);
  document.getElementById("formReprogramarCita")?.addEventListener("submit", reprogramarCita);
  document.getElementById("formReprogramarOrientador")?.addEventListener("submit", reprogramarCita);
  document.getElementById("formCancelarOrientador")?.addEventListener("submit", cancelarCita);
});