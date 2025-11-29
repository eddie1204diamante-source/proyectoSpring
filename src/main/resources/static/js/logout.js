document.getElementById("btn-sesion").addEventListener("click", () => {

    // Confirmación clásica
    const confirmar = confirm("¿Seguro que deseas cerrar sesión?");

    if (!confirmar) {
        return; // No hacer nada si cancela
    }

    // Logout
    fetch("/logout", {
        method: "POST",
        credentials: "include"
    })
    .then(() => {
        alert("Sesión cerrada correctamente");

        // Redirige después del alert
        window.location.href = "/"; // o /index.html
    })
    .catch(err => {
        console.error("Error al cerrar sesión:", err);
        alert("Error al cerrar sesión.");
    });
});