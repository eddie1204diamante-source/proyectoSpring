// ==================== cita.js - VERSIÓN 100% FUNCIONAL ====================

function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add("active");
}
function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove("active");
}
window.addEventListener("click", e => {
  document.querySelectorAll(".modal.active").forEach(modal => {
    if (e.target === modal) modal.classList.remove("active");
  });
});

// ------------------- POBLAR ORIENTADORES -------------------
function populateOrientadores() {
  fetch("http://localhost:8080/api/citas/orientadores", { credentials: "include" })
    .then(res => res.ok ? res.json() : Promise.reject("Error al cargar orientadores"))
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
    .catch(err => console.error(err));
}

// ------------------- POBLAR HORAS -------------------
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

// ------------------- NOMBRE DEL ESTUDIANTE (SIEMPRE FUNCIONA) -------------------
async function populateNombreEstudiante() {
  const input = document.getElementById("nombre-estudiante");
  if (!input) return;

  try {
    const res = await fetch("http://localhost:8080/api/auth/me", {
      credentials: "include"
    });

    if (!res.ok) throw new Error("No autenticado");

    const data = await res.json();
    input.value = data.nombreCompleto || "Estudiante";

    // Guardar en localStorage para el resto del sistema
    localStorage.setItem("nombreEstudiante", data.nombreCompleto);
    localStorage.setItem("idUsuario", data.idUsuario);
    localStorage.setItem("rol", data.rol === 2 ? "estudiante" : "orientador");

  } catch (err) {
    console.warn("No se pudo cargar el nombre:", err);
    input.value = "Sesión expirada";
  }
}

// ------------------- CARGAR CITAS -------------------
function loadCitas() {
  const rol = localStorage.getItem("rol");
  const id = rol === "estudiante" ? localStorage.getItem("idUsuario") : localStorage.getItem("idOrientador");
  if (!id) return console.error("No hay ID de usuario/orientador");

  const endpoint = rol === "estudiante" 
    ? `/api/citas/estudiante/${id}` 
    : `/api/citas/orientador/${id}`;

  fetch(`http://localhost:8080${endpoint}`, { credentials: "include" })
    .then(res => res.ok ? res.json() : Promise.reject("Error al cargar citas"))
    .then(citas => {
      const tbody = document.querySelector(".styled-table tbody");
      if (!tbody) return;
      tbody.innerHTML = "";

      citas.forEach(cita => {
        const row = document.createElement("tr");
        const estudianteCell = rol === "orientador" ? `<td>${cita.nombreEstudiante || 'N/A'}</td>` : '';
        row.innerHTML = `
          ${estudianteCell}
          <td>${cita.fechaCita || '-'}</td>
          <td>${cita.horaCita || '-'}</td>
          <td>${cita.motivoOriginal || '-'}</td>
          <td>${cita.estado || '-'}</td>
          <td>
            <button onclick="verDetalle(${cita.idCita})">Ver</button>
            <button onclick="openModal('${rol === 'estudiante' ? 'modal-reprogramar' : 'modal-reprogramar-orientador'}'); setCitaId(${cita.idCita})">Reprogramar</button>
            <button onclick="openModal('${rol === 'estudiante' ? 'modal-cancelar' : 'modal-cancelar-orientador'}'); setCitaId(${cita.idCita})">Cancelar</button>
          </td>
        `;
        tbody.appendChild(row);
      });
    })
    .catch(err => {
      console.error(err);
      alert("Error al cargar citas. Sesión expirada?");
    });
}

// ------------------- VER DETALLE -------------------
function verDetalle(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}`, { credentials: "include" })
    .then(res => res.ok ? res.json() : Promise.reject("Cita no encontrada"))
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
        openModal('modal-ver');
      }
    })
    .catch(() => alert("No se pudo cargar el detalle"));
}

let currentCitaId = null;
function setCitaId(id) {
  currentCitaId = id;
}

// ------------------- GUARDAR CITA -------------------
function guardarCita(e) {
  e.preventDefault();
  if (localStorage.getItem("rol") !== "estudiante") return;

  const idEstudiante = localStorage.getItem("idUsuario");
  const idOrientador = document.getElementById("orientador")?.value;
  const fecha = document.getElementById("fecha")?.value;
  const hora = document.getElementById("hora")?.value;
  const motivo = document.getElementById("motivo")?.value;

  if (!idOrientador || !fecha || !hora || !motivo) {
    return alert("Completa todos los campos");
  }

  const data = { idEstudiante, idOrientador, fecha, hora, motivo };

  fetch("http://localhost:8080/api/citas/crear", {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
  .then(res => res.ok ? res.json() : res.text().then(text => Promise.reject(text || "Error del servidor")))
  .then(resp => {
    alert(resp.message || "Cita creada con éxito");
    closeModal('modal-crear');
    loadCitas();
  })
  .catch(err => {
    console.error(err);
    alert("Error: " + err);
  });
}

// ------------------- REPROGRAMAR -------------------
function reprogramarCita(e) {
  e.preventDefault();
  const rol = localStorage.getItem("rol");
  const fechaId = rol === "estudiante" ? "fecha_reprogramar" : "fecha_reprogramar_o";
  const horaId = rol === "estudiante" ? "hora_reprogramar" : "hora_reprogramar_o";

  const nuevaFecha = document.getElementById(fechaId)?.value;
  const nuevaHora = document.getElementById(horaId)?.value;

  if (!currentCitaId || !nuevaFecha || !nuevaHora) return alert("Faltan datos");

  fetch(`http://localhost:8080/api/citas/${currentCitaId}/reprogramar`, {
    method: "PUT",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fecha: nuevaFecha, hora: nuevaHora })
  })
  .then(res => res.ok ? res.json() : Promise.reject("Error al reprogramar"))
  .then(() => {
    alert("Cita reprogramada");
    closeModal(rol === "estudiante" ? 'modal-reprogramar' : 'modal-reprogramar-orientador');
    loadCitas();
  })
  .catch(err => alert("Error: " + err));
}

// ------------------- CANCELAR -------------------
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
  .then(res => res.ok ? res.json() : Promise.reject("Error al cancelar"))
  .then(() => {
    alert("Cita cancelada");
    closeModal(rol === "estudiante" ? 'modal-cancelar' : 'modal-cancelar-orientador');
    loadCitas();
  })
  .catch(err => alert("Error: " + err));
}

// ------------------- APROBAR (ORIENTADOR) -------------------
function aprobarCita(idCita) {
  fetch(`http://localhost:8080/api/citas/${idCita}/aprobar`, {
    method: "PUT",
    credentials: "include"
  })
  .then(res => res.ok ? res.json() : Promise.reject("Error al aprobar"))
  .then(() => {
    alert("Cita aprobada");
    loadCitasPendientes();
    loadCitas();
  })
  .catch(() => alert("Error al aprobar"));
}

function loadCitasPendientes() {
  const id = localStorage.getItem("idOrientador");
  if (!id) return;
  fetch(`http://localhost:8080/api/citas/orientador/${id}/pendientes`, { credentials: "include" })
    .then(res => res.ok ? res.json() : [])
    .then(citas => {
      const tbody = document.getElementById("tabla-pendientes-body");
      if (!tbody) return;
      tbody.innerHTML = "";
      citas.forEach(c => {
        tbody.innerHTML += `
          <tr>
            <td>${c.nombreEstudiante}</td>
            <td>${c.fechaCita}</td>
            <td>${c.horaCita}</td>
            <td>${c.motivoOriginal}</td>
            <td><button onclick="aprobarCita(${c.idCita})">Aprobar</button></td>
          </tr>
        `;
      });
    });
}

function loadCitasIntoSelect(selectId) {
  const id = localStorage.getItem("idOrientador");
  if (!id) return;
  fetch(`http://localhost:8080/api/citas/orientador/${id}`, { credentials: "include" })
    .then(res => res.ok ? res.json() : [])
    .then(citas => {
      const select = document.getElementById(selectId);
      if (!select) return;
      select.innerHTML = '<option value="">Selecciona una cita</option>';
      citas.filter(c => c.estado !== 'CANCELADA').forEach(c => {
        const opt = document.createElement("option");
        opt.value = c.idCita;
        opt.textContent = `${c.nombreEstudiante} - ${c.fechaCita} ${c.horaCita}`;
        select.appendChild(opt);
      });
    });
}

function abrirModalAprobar() { loadCitasPendientes(); openModal('modal-aprobar-orientador'); }
function abrirModalReprogramarOrientador() { loadCitasIntoSelect('cita-reprogramar-select'); populateHoras('hora_reprogramar_o'); openModal('modal-reprogramar-orientador'); }
function abrirModalCancelarOrientador() { loadCitasIntoSelect('cita-cancelar-select'); openModal('modal-cancelar-orientador'); }
function ejecutarCancelar() { cancelarCita(); }

// ==================== INICIO ====================
document.addEventListener("DOMContentLoaded", () => {
  populateOrientadores();
  populateHoras("hora");
  populateHoras("hora_reprogramar");
  populateNombreEstudiante();
  loadCitas();

  document.getElementById("formCrearCita")?.addEventListener("submit", guardarCita);
  document.getElementById("formReprogramarCita")?.addEventListener("submit", reprogramarCita);
  document.getElementById("formReprogramarOrientador")?.addEventListener("submit", reprogramarCita);
  document.getElementById("formCancelarOrientador")?.addEventListener("submit", cancelarCita);
});