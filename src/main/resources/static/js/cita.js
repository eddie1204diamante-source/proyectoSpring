function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add("active");
}

function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove("active");
}

// Cerrar click afuera
window.addEventListener("click", function (e) {
  const modals = document.querySelectorAll(".modal.active");

  modals.forEach(modal => {
    if (e.target === modal) {
      modal.classList.remove("active");
    }
  });
});
function guardarCita() {
    const fecha = document.getElementById("fecha").value;
    const hora = document.getElementById("hora").value;
    const motivo = document.getElementById("motivo").value;

    // 1. CORRECCIÓN CLAVE: Asegurar que idAprendiz sea un número (Integer)
    const idAprendiz = parseInt(localStorage.getItem("idAprendiz"), 10);
    // Necesitas enviar la hora y la fecha juntas si tu DTO espera un solo LocalDateTime
    const fechaHora = `${fecha}T${hora}:00`;

    const data = { 
        idAprendiz, 
        fecha: fechaHora, 
        motivo 
        // Nota: Asegúrate de incluir idPsicologica si es requerido en tu DTO
    };

    fetch("http://localhost:8080/api/citas/crear", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    })
    .then(async res => {
        // 2. CORRECCIÓN CLAVE: Verificar si la respuesta es exitosa (código 200 o 201)
        if (!res.ok) {
            // Si el status es 400 (Bad Request), lanzamos un error que será atrapado por .catch()
            const errorText = await res.text();
            console.error("Respuesta de Error RAW del servidor:", errorText);
            throw new Error(errorText || "Error desconocido al crear la cita.");
        }
        
        // Si es exitoso, devolvemos el texto/JSON para el siguiente .then()
        return res.text();
    })
    .then(text => {
        // Este bloque solo se ejecuta si la respuesta fue exitosa (res.ok es true)
        console.log("Respuesta Exitosa RAW del servidor:", text);
        alert("Cita creada con éxito");
        closeModal('modal-crear');
        setTimeout(() => location.reload(), 300);
    })
    .catch(err => {
        // Este bloque se ejecuta si hay un error de red O si lanzamos un error en el bloque .then(async res => ...)
        console.error("Error al enviar la cita:", err);
        alert("ERROR: La cita no pudo ser creada. Revisa la consola para más detalles.");
    });
}