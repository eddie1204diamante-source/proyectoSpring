  function openModal(id) {
    document.getElementById(id).classList.add('active');
  }

  function closeModal(id) {
    document.getElementById(id).classList.remove('active');
  }

  // Cerrar modal haciendo clic afuera
  window.onclick = function(e) {
    if (e.target.classList.contains('modal')) {
      e.target.classList.remove('active');
    }
  }