// 
// MODAL DE INFORMACIÓN
// 

function openModal(title, text) {
  const modalInfo = document.getElementById("modalInfo");
  document.getElementById("modalTitle").innerText = title;
  document.getElementById("modalText").innerText = text;
  modalInfo.classList.add("active");
}

function closeModal() {
  const modalInfo = document.getElementById("modalInfo");
  modalInfo.classList.remove("active");
}


// 
// MODAL CREAR ACTIVIDAD
// 

const modalCrear = document.getElementById("crearModal");
const btnAbrirCrear = document.getElementById("abrirModalCrearBtn");
const btnCerrarCrear = document.getElementById("cerrarModalCrearBtn");

// Abrir modal crear
if (btnAbrirCrear) {
  btnAbrirCrear.addEventListener("click", () => {
    modalCrear.classList.add("active");
  });
}

// Cerrar modal crear (botón X)
if (btnCerrarCrear) {
  btnCerrarCrear.addEventListener("click", () => {
    modalCrear.classList.remove("active");
  });
}

function closeCrearModal() {
  modalCrear.classList.remove("active");
}

// Cerrar modal crear al hacer clic fuera
window.addEventListener("click", function (e) {
  if (e.target === modalCrear) {
    modalCrear.classList.remove("active");
  }
});


// 
// MODAL DE CONFIRMACIÓN
// 

let confirmCallback = null;

function openConfirmModal(title, text) {
  const modalConfirm = document.getElementById("modalConfirm");
  document.getElementById("confirmTitle").innerText = title;
  document.getElementById("confirmText").innerText = text;

  modalConfirm.classList.add("active");

  // Acción por defecto si no se define otra
  confirmCallback = () => alert("Acción confirmada para: " + title);
}

function closeConfirmModal() {
  document.getElementById("modalConfirm").classList.remove("active");
}

function confirmAction() {
  if (confirmCallback) confirmCallback();
  closeConfirmModal();
}


// 
// CREAR ACTIVIDAD (POST Spring Boot)
// 

const formCrear = document.getElementById("formCrearRecurso");

if (formCrear) {
  formCrear.addEventListener("submit", async (e) => {
    e.preventDefault();

    const nuevaActividad = {
      titulo: document.getElementById("nombre_recurso").value,
      descripcion: document.getElementById("descripcion_recurso").value,
      urlActividad: document.getElementById("tipo_recurso").value,
      fechaCreacion: document.getElementById("fecha_creacion_recurso").value
    };

    try {
      const response = await fetch("/api/actividades", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevaActividad)
      });

      if (response.ok) {
        alert("Actividad creada exitosamente");

        formCrear.reset();
        modalCrear.classList.remove("active");

        cargarActividades();
      } else {
        alert("Error al crear actividad");
      }
    } catch (error) {
      console.error(error);
      alert("No se pudo conectar con el servidor");
    }
  });
}
function openEditModal(id, titulo, descripcion) {
  document.getElementById("editar_id").value = id;
  document.getElementById("editar_titulo").value = titulo;
  document.getElementById("editar_descripcion").value = descripcion;

  document.getElementById("modalEditar").style.display = "flex";
}

function closeEditModal() {
  document.getElementById("modalEditar").style.display = "none";
}
document.getElementById("formEditarActividad").addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("editar_id").value;
  const titulo = document.getElementById("editar_titulo").value;
  const descripcion = document.getElementById("editar_descripcion").value;

  const respuesta = await fetch(`/api/actividades/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ titulo, descripcion })
  });

  if (respuesta.ok) {
    alert("Actividad actualizada");
    closeEditModal();
    location.reload(); // recargar lista
  } else {
    alert("Error al actualizar");
  }
});
//editar
function editarActividad(id, titulo, descripcion) {
  document.getElementById("editar_id").value = id;
  document.getElementById("editar_titulo").value = titulo;
  document.getElementById("editar_descripcion").value = descripcion;

  document.getElementById("modalEditar").classList.add("active");
}

function closeEditModal() {
  document.getElementById("modalEditar").classList.remove("active");
}

//borrar


async function borrarActividad(id) {
  if (!confirm("¿Seguro que deseas eliminar esta actividad?")) return;

  try {
    const response = await fetch(`/api/actividades/${id}`, {
      method: "DELETE"
    });

    if (response.ok) {
      alert("Actividad eliminada");
      cargarActividades(); // recarga la lista después de borrar
    } else {
      alert("Error al eliminar la actividad");
    }
  } catch (error) {
    console.error(error);
    alert("No se pudo conectar con el servidor");
  }
}


// 
// CARGAR ACTIVIDADES OPCIONALES
// 

async function cargarActividades() {
  try {
    const response = await fetch("/api/actividades");
    const actividades = await response.json();

    const contenedor = document.querySelector(".column:nth-child(2)");

    if (!contenedor) return;

    contenedor.innerHTML = `<h2>🧘 Actividades de Autoayuda (Opcionales)</h2>`;
actividades.forEach(a => {
  contenedor.innerHTML += `
    <div class="activity-card">
      <h3>${a.titulo}</h3>
      <p>${a.descripcion}</p>

      <div class="activity-actions">

        <button class="btn-ver"
          onclick="openModal('${a.titulo}', '${a.descripcion}', '${a.urlActividad || ''}')">
          Ver
        </button>

        <button class="btn-edit"
          onclick="editarActividad(${a.idActividad}, '${a.titulo}', '${a.descripcion}')">
          Editar
        </button>

        <button class="btn-delete"
          onclick="borrarActividad(${a.idActividad})">
          Borrar
        </button>

      </div>
    </div>
  `;
});



  } catch (error) {
    console.error(error);
  }
}

document.addEventListener("DOMContentLoaded", cargarActividades);
