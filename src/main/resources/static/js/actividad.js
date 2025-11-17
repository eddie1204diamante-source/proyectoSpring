// --- Modal de información 
function openModal(title, text) {
  document.getElementById("modalTitle").innerText = title;
  document.getElementById("modalText").innerText = text;
  document.getElementById("modalInfo").classList.add("active");
}

function closeModal() {
  document.getElementById("modalInfo").classList.remove("active");
}


const openBtn = document.getElementById("abrirModalCrearBtn");
const closeBtn = document.getElementById("cerrarModalCrearBtn"); 
const modal = document.getElementById("crearModal");

// Abrir modal
openBtn.addEventListener("click", () => {
  modal.style.display = "block";
});

// Cerrar modal
closeBtn.addEventListener("click", () => {
  modal.style.display = "none";
});

// Cerrar al hacer click fuera
window.addEventListener("click", (e) => {
  if (e.target === modal) {
    modal.style.display = "none";
  }
});



// --- Modal de confirmación ---
let confirmCallback = null;

function openConfirmModal(title, text) {
  document.getElementById("confirmTitle").innerText = title;
  document.getElementById("confirmText").innerText = text;

  document.getElementById("modalConfirm").classList.add("active");

  // Acción por defecto 
  confirmCallback = () => alert("Acción confirmada para: " + title);
}

function closeConfirmModal() {
  document.getElementById("modalConfirm").classList.remove("active");
}

function confirmAction() {
  if (confirmCallback) confirmCallback();
  closeConfirmModal();
}
