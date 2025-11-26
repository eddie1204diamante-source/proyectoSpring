function createStars() {
  const container = document.getElementById('starsContainer');
  const starCount = 50;
  for (let i = 0; i < starCount; i++) {
    const star = document.createElement('div');
    const type = Math.floor(Math.random() * 3) + 1;
    star.className = `star type${type}`;
    star.style.left = Math.random() * 100 + '%';
    star.style.animationDelay = Math.random() * 8 + 's';
    star.style.animationDuration = (6 + Math.random() * 4) + 's';
    star.style.setProperty('--drift', (Math.random() - 0.5) * 100 + 'px');
    container.appendChild(star);
  }
}

let currentForm = 'login';
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');

// Toggle mostrar/ocultar contraseña
document.querySelectorAll('.password-toggle').forEach(btn => {
  btn.addEventListener('click', function () {
    const targetId = this.getAttribute('data-target');
    const input = document.getElementById(targetId);
    if (input.type === 'password') {
      input.type = 'text';
      this.textContent = 'Ocultar';
    } else {
      input.type = 'password';
      this.textContent = 'Mostrar';
    }
  });
});

function switchForm(targetForm) {
  const currentFormElement = currentForm === 'login' ? loginForm : registerForm;
  const targetFormElement = targetForm === 'login' ? loginForm : registerForm;
  currentFormElement.classList.add(targetForm === 'login' ? 'slide-out-right' : 'slide-out-left');
  setTimeout(() => {
    currentFormElement.classList.remove('active', 'slide-out-right', 'slide-out-left');
    targetFormElement.classList.add('active');
    currentForm = targetForm;
    const title = document.querySelector('.auth-title');
    const subtitle = document.querySelector('.auth-subtitle');
    if (targetForm === 'login') {
      title.textContent = 'Bienvenido de vuelta';
      subtitle.textContent = 'Inicia sesión en tu cuenta';
    } else {
      title.textContent = 'Crear cuenta';
      subtitle.textContent = 'Únete a nuestra comunidad';
    }
  }, 300);
}

function validatePassword(password) {
  return {
    length: password.length >= 8,
    uppercase: /[A-Z]/.test(password),
    lowercase: /[a-z]/.test(password),
    number: /\d/.test(password),
    special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
  };
}

function updatePasswordStrength(password) {
  const strengthFill = document.getElementById('strengthFill');
  const strengthText = document.getElementById('strengthText');
  if (!password) {
    strengthFill.style.width = '0%';
    strengthText.textContent = 'Ingresa una contraseña';
    return;
  }
  const checks = validatePassword(password);
  const score = Object.values(checks).filter(Boolean).length;
  let width = (score / 5) * 100;
  let color = '#ff6b6b';
  let text = 'Muy débil';
  if (score >= 2) { color = '#ffa726'; text = 'Débil'; }
  if (score >= 3) { color = '#ffee58'; text = 'Regular'; }
  if (score >= 4) { color = '#66bb6a'; text = 'Fuerte'; }
  if (score === 5) { color = '#4caf50'; text = 'Muy fuerte'; }
  strengthFill.style.width = width + '%';
  strengthFill.style.background = color;
  strengthText.textContent = text;
}

function showLoginOverlay() {
  const overlay = document.getElementById('loginOverlay');
  overlay.classList.remove('hidden');
  setTimeout(() => overlay.classList.add('visible'), 10);
}

function showError(inputId, errorId, message) {
  const input = document.getElementById(inputId);
  const error = document.getElementById(errorId);
  const errorText = error.querySelector('.error-text');
  errorText.textContent = message;
  error.classList.add('show');
  input.classList.add('error');
  input.classList.remove('success');
}

function hideError(inputId, errorId) {
  const input = document.getElementById(inputId);
  const error = document.getElementById(errorId);
  error.classList.remove('show');
  input.classList.remove('error');
  input.classList.add('success');
}

function validateDocumento(value, inputId, errorId) {
  const regex = /^\d{10}$/;
  if (!regex.test(value)) {
    showError(inputId, errorId, 'El documento debe tener exactamente 10 números.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

function validateNombreApellido(value, inputId, errorId) {
  const regex = /^[a-zA-Z\s]{3,30}$/;
  if (!regex.test(value)) {
    showError(inputId, errorId, 'Solo letras y espacios, entre 3 y 30 caracteres.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

function validateEmail(value, inputId, errorId) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!regex.test(value)) {
    showError(inputId, errorId, 'Correo inválido.');
    return false;
  }
  const allowedDomains = ['@gmail.com', '@soy.sena.edu.co', '@sena.edu.co', '@administradormindwell2025.com'];
  const domain = value.slice(value.lastIndexOf('@')).toLowerCase();
  if (!allowedDomains.includes(domain)) {
    showError(inputId, errorId, 'Dominio no permitido.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

function validateContrasena(value, inputId, errorId) {
  const checks = validatePassword(value);
  if (!checks.length || !checks.uppercase || !checks.lowercase || !checks.number || !checks.special) {
    showError(inputId, errorId, 'Mínimo 8 caracteres, mayúscula, minúscula, número y especial.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

// Validaciones en tiempo real
document.getElementById('loginDocumento').addEventListener('input', (e) => validateDocumento(e.target.value, 'loginDocumento', 'loginDocumentoError'));
document.getElementById('loginContrasena').addEventListener('input', (e) => validateContrasena(e.target.value, 'loginContrasena', 'loginContrasenaError'));
document.getElementById('registerNombres').addEventListener('input', (e) => validateNombreApellido(e.target.value, 'registerNombres', 'registerNombresError'));
document.getElementById('registerApellidos').addEventListener('input', (e) => validateNombreApellido(e.target.value, 'registerApellidos', 'registerApellidosError'));
document.getElementById('registerDocumento').addEventListener('input', (e) => validateDocumento(e.target.value, 'registerDocumento', 'registerDocumentoError'));
document.getElementById('registerEmail').addEventListener('input', (e) => validateEmail(e.target.value, 'registerEmail', 'registerEmailError'));
document.getElementById('registerContrasena').addEventListener('input', (e) => {
  validateContrasena(e.target.value, 'registerContrasena', 'registerContrasenaError');
  updatePasswordStrength(e.target.value);
});
document.getElementById('confirmContrasena').addEventListener('input', () => {
  const pass1 = document.getElementById('registerContrasena').value;
  const pass2 = document.getElementById('confirmContrasena').value;
  if (pass1 !== pass2) {
    showError('confirmContrasena', 'confirmContrasenaError', 'Las contraseñas no coinciden');
  } else {
    hideError('confirmContrasena', 'confirmContrasenaError');
  }
});

// Cambio entre formularios
document.getElementById('showRegister').addEventListener('click', (e) => { e.preventDefault(); switchForm('register'); });
document.getElementById('showLogin').addEventListener('click', (e) => { e.preventDefault(); switchForm('login'); });

// ==================== LOGIN 100% FUNCIONAL ====================
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const documento = document.getElementById('loginDocumento').value.trim();
  const contrasena = document.getElementById('loginContrasena').value;

  if (!validateDocumento(documento, 'loginDocumento', 'loginDocumentoError')) return;
  if (!validateContrasena(contrasena, 'loginContrasena', 'loginContrasenaError')) return;

  try {
    const res = await fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ documento, contrasena })
    });

    const data = await res.json();

    if (!res.ok || !data.success) {
      showError('loginContrasena', 'loginContrasenaError', data.message || 'Credenciales incorrectas');
      return;
    }

    // Guardar datos del usuario
    localStorage.setItem("idUsuario", data.idUsuario);
    localStorage.setItem("nombreEstudiante", data.nombreCompleto);
    localStorage.setItem("rol", data.rol === 2 ? "estudiante" : data.rol === 3 ? "orientador" : "administrador");

    const redirectUrl = 'http://localhost:8080' + data.redirect;

    showLoginOverlay();
    setTimeout(() => {
      window.location.href = redirectUrl; // ← LA CLAVE: redirección directa
    }, 800);

  } catch (err) {
    console.error(err);
    alert("Error de conexión con el servidor");
  }
});

// ==================== REGISTRO ====================
registerForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const nombres = document.getElementById('registerNombres').value.trim();
  const apellidos = document.getElementById('registerApellidos').value.trim();
  const documento = document.getElementById('registerDocumento').value.trim();
  const correo = document.getElementById('registerEmail').value.trim().toLowerCase();
  const contrasena = document.getElementById('registerContrasena').value;
  const confirmar = document.getElementById('confirmContrasena').value;

  let isValid = true;
  if (!validateNombreApellido(nombres, 'registerNombres', 'registerNombresError')) isValid = false;
  if (!validateNombreApellido(apellidos, 'registerApellidos', 'registerApellidosError')) isValid = false;
  if (!validateDocumento(documento, 'registerDocumento', 'registerDocumentoError')) isValid = false;
  if (!validateEmail(correo, 'registerEmail', 'registerEmailError')) isValid = false;
  if (!validateContrasena(contrasena, 'registerContrasena', 'registerContrasenaError')) isValid = false;
  if (contrasena !== confirmar) { showError('confirmContrasena', 'confirmContrasenaError', 'Las contraseñas no coinciden'); isValid = false; }
  if (!document.getElementById('acceptTerms').checked) { alert('Debes aceptar los términos'); isValid = false; }
  if (!isValid) return;

  const btn = registerForm.querySelector('.btn-primary');
  btn.classList.add('loading');
  btn.disabled = true;

  try {
    const res = await fetch('http://localhost:8080/api/v1/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nombres, apellidos, documento, correo, contrasena })
    });
    const data = await res.json();

    if (data.success) {
      alert('¡Cuenta creada con éxito! Ya puedes iniciar sesión');
      switchForm('login');
      document.getElementById('loginDocumento').value = documento;
      registerForm.reset();
    } else {
      alert(data.message || 'Error al registrar');
    }
  } catch (err) {
    alert('Error de conexión con el servidor');
    console.error(err);
  } finally {
    btn.classList.remove('loading');
    btn.disabled = false;
  }
});

// Animaciones de inputs
document.querySelectorAll('.form-input').forEach(input => {
  input.addEventListener('focus', () => {
    input.parentElement.style.transform = 'scale(1.02)';
    input.style.borderColor = '#cc96f9';
  });
  input.addEventListener('blur', () => {
    input.parentElement.style.transform = 'scale(1)';
    if (!input.value) input.style.borderColor = '#e1e5e9';
  });
});

document.getElementById('confirmContrasena').addEventListener('paste', (e) => e.preventDefault());

// ==================== VERIFICAR SI YA ESTÁ LOGUEADO AL CARGAR ====================
document.addEventListener("DOMContentLoaded", () => {
  createStars();

  fetch('http://localhost:8080/api/auth/me', { 
    credentials: 'include' 
  })
  .then(r => {
    if (r.ok) return r.json();
    throw new Error("No autenticado");
  })
  .then(user => {
    const rol = user.rol === 2 ? "estudiante" : user.rol === 3 ? "orientador" : "administrador";
    localStorage.setItem("rol", rol);
    localStorage.setItem("idUsuario", user.idUsuario);
    localStorage.setItem("nombreEstudiante", user.nombreCompleto);

    const redirectMap = {
      estudiante: "/estudiante/dashboard",
      orientador: "/orientador/dashboard",
      administrador: "/administrador/dashboard"
    };

    window.location.href = "http://localhost:8080" + (redirectMap[rol] || "/estudiante/dashboard");
  })
  .catch(() => {
    // No está logueado → se queda en login
    setTimeout(() => document.getElementById('loginDocumento').focus(), 800);
  });
});