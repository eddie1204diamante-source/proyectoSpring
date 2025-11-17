// Crear estrellas animadas
function createStars() {
  const container = document.getElementById('starsContainer');
  const starCount = 40;
  
  for (let i = 0; i < starCount; i++) {
    const star = document.createElement('div');
    const type = Math.floor(Math.random() * 3) + 1;
    star.className = `star type${type}`;
    
    const leftPosition = Math.random() * 100;
    star.style.left = leftPosition + '%';
    
    const delay = Math.random() * 8;
    star.style.animationDelay = delay + 's';
    
    const duration = 6 + Math.random() * 4;
    star.style.animationDuration = duration + 's';
    
    const drift = (Math.random() - 0.5) * 100;
    star.style.setProperty('--drift', drift + 'px');
    
    container.appendChild(star);
  }
}

// Variables globales
let currentForm = 'login';
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');

// Toggle de visibilidad de contraseña
document.querySelectorAll('.password-toggle').forEach(btn => {
  btn.addEventListener('click', function() {
    const targetId = this.getAttribute('data-target');
    const input = document.getElementById(targetId);
    
    if (input.type === 'password') {
      input.type = 'text';
      this.textContent = '🙈';
    } else {
      input.type = 'password';
      this.textContent = '👁️';
    }
  });
});

// Función para cambiar entre formularios
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

// Validación de contraseña (ya existía, la mantengo)
function validatePassword(password) {
  return {
    length: password.length >= 8,
    uppercase: /[A-Z]/.test(password),
    lowercase: /[a-z]/.test(password),
    number: /\d/.test(password),
    special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
  };
}

// Actualizar fortaleza de contraseña (ya existía, la mantengo)
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

  if (score >= 2) {
    color = '#ffa726';
    text = 'Débil';
  }
  if (score >= 3) {
    color = '#ffee58';
    text = 'Regular';
  }
  if (score >= 4) {
    color = '#66bb6a';
    text = 'Fuerte';
  }
  if (score === 5) {
    color = '#4caf50';
    text = 'Muy fuerte';
  }

  strengthFill.style.width = width + '%';
  strengthFill.style.background = color;
  strengthText.textContent = text;
}

// Mostrar/ocultar overlay (ya existía)
function showLoginOverlay() {
  const overlay = document.getElementById('loginOverlay');
  overlay.classList.remove('hidden');
  setTimeout(() => overlay.classList.add('visible'), 10);
}

function hideLoginOverlay(callback) {
  const overlay = document.getElementById('loginOverlay');
  overlay.classList.remove('visible');
  setTimeout(() => {
    overlay.classList.add('hidden');
    if (callback) callback();
  }, 300);
}

// Simulación de carga (ya existía)
function simulateLoading(button, callback) {
  button.classList.add('loading');
  button.disabled = true;

  setTimeout(() => {
    button.classList.remove('loading');
    button.disabled = false;
    callback();
  }, 2000);
}

// NUEVA: Función para mostrar error en un campo
function showError(inputId, errorId, message) {
  const input = document.getElementById(inputId);
  const error = document.getElementById(errorId);
  const errorText = error.querySelector('.error-text');
  errorText.textContent = message;
  error.classList.add('show');
  input.classList.add('error');
  input.classList.remove('success');
}

// NUEVA: Función para ocultar error y marcar éxito
function hideError(inputId, errorId) {
  const input = document.getElementById(inputId);
  const error = document.getElementById(errorId);
  error.classList.remove('show');
  input.classList.remove('error');
  input.classList.add('success');
}

// NUEVA: Validar documento (común para login y register)
function validateDocumento(value, inputId, errorId) {
  const regex = /^\d{10}$/;
  if (!regex.test(value)) {
    showError(inputId, errorId, 'El documento debe tener exactamente 10 números.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

// NUEVA: Validar nombre o apellido
function validateNombreApellido(value, inputId, errorId) {
  const regex = /^[a-zA-Z\s]{3,30}$/;
  if (!regex.test(value)) {
    showError(inputId, errorId, 'Solo letras y espacios, entre 3 y 30 caracteres.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

// NUEVA: Validar email y dominio
function validateEmail(value, inputId, errorId) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Email básico
  if (!regex.test(value)) {
    showError(inputId, errorId, 'Correo inválido.');
    return false;
  }
  
  const allowedDomains = ['@gmail.com', '@soy.sena.edu.co', '@sena.edu.co', '@administradorMindwall.mindwall.com'];
  const domain = value.slice(value.lastIndexOf('@'));
  if (!allowedDomains.includes(domain)) {
    showError(inputId, errorId, 'Dominio incorrecto.');
    return false;
  }
  
  hideError(inputId, errorId);
  return true;
}

// NUEVA: Obtener rol_id basado en dominio (simulación)
function getRolIdFromEmail(email) {
  const domain = email.slice(email.lastIndexOf('@'));
  if (domain === '@gmail.com' || domain === '@soy.sena.edu.co') return 1;
  if (domain === '@sena.edu.co') return 2;
  if (domain === '@administradorMindwall.mindwall.com') return 3;
  return null;
}

// NUEVA: Validar contraseña (para register y login formato)
function validateContrasena(value, inputId, errorId) {
  const checks = validatePassword(value);
  if (!checks.length || !checks.uppercase || !checks.lowercase || !checks.number || !checks.special) {
    showError(inputId, errorId, 'La contraseña debe tener al menos 8 caracteres, mayúsculas, minúsculas, números y especiales.');
    return false;
  }
  hideError(inputId, errorId);
  return true;
}

// Event listeners para cambio de formularios (ya existía)
document.getElementById('showRegister').addEventListener('click', (e) => {
  e.preventDefault();
  switchForm('register');
});

document.getElementById('showLogin').addEventListener('click', (e) => {
  e.preventDefault();
  switchForm('login');
});

// Actualizar fortaleza de contraseña en tiempo real (ya existía)
document.getElementById('registerContrasena').addEventListener('input', (e) => {
  updatePasswordStrength(e.target.value);
});

// NUEVO: Validaciones en tiempo real (input y blur) para todos los campos
// Para Login
const loginDocumento = document.getElementById('loginDocumento');
loginDocumento.addEventListener('input', () => validateDocumento(loginDocumento.value, 'loginDocumento', 'loginDocumentoError'));
loginDocumento.addEventListener('blur', () => validateDocumento(loginDocumento.value, 'loginDocumento', 'loginDocumentoError'));

const loginContrasena = document.getElementById('loginContrasena');
loginContrasena.addEventListener('input', () => validateContrasena(loginContrasena.value, 'loginContrasena', 'loginContrasenaError'));
loginContrasena.addEventListener('blur', () => validateContrasena(loginContrasena.value, 'loginContrasena', 'loginContrasenaError'));

// Para Register
const registerNombres = document.getElementById('registerNombres');
registerNombres.addEventListener('input', () => validateNombreApellido(registerNombres.value, 'registerNombres', 'registerNombresError'));
registerNombres.addEventListener('blur', () => validateNombreApellido(registerNombres.value, 'registerNombres', 'registerNombresError'));

const registerApellidos = document.getElementById('registerApellidos');
registerApellidos.addEventListener('input', () => validateNombreApellido(registerApellidos.value, 'registerApellidos', 'registerApellidosError'));
registerApellidos.addEventListener('blur', () => validateNombreApellido(registerApellidos.value, 'registerApellidos', 'registerApellidosError'));

const registerDocumento = document.getElementById('registerDocumento');
registerDocumento.addEventListener('input', () => validateDocumento(registerDocumento.value, 'registerDocumento', 'registerDocumentoError'));
registerDocumento.addEventListener('blur', () => validateDocumento(registerDocumento.value, 'registerDocumento', 'registerDocumentoError'));

const registerEmail = document.getElementById('registerEmail');
registerEmail.addEventListener('input', () => validateEmail(registerEmail.value, 'registerEmail', 'registerEmailError'));
registerEmail.addEventListener('blur', () => validateEmail(registerEmail.value, 'registerEmail', 'registerEmailError'));

const registerContrasena = document.getElementById('registerContrasena');
registerContrasena.addEventListener('input', () => validateContrasena(registerContrasena.value, 'registerContrasena', 'registerContrasenaError'));
registerContrasena.addEventListener('blur', () => validateContrasena(registerContrasena.value, 'registerContrasena', 'registerContrasenaError'));

const confirmContrasena = document.getElementById('confirmContrasena');
confirmContrasena.addEventListener('input', () => {
  if (confirmContrasena.value !== registerContrasena.value) {
    showError('confirmContrasena', 'confirmContrasenaError', 'Las contraseñas no coinciden.');
  } else {
    hideError('confirmContrasena', 'confirmContrasenaError');
  }
});
confirmContrasena.addEventListener('blur', () => {
  if (confirmContrasena.value !== registerContrasena.value) {
    showError('confirmContrasena', 'confirmContrasenaError', 'Las contraseñas no coinciden.');
  } else {
    hideError('confirmContrasena', 'confirmContrasenaError');
  }
});

// Submit del formulario de login (modificado con validaciones)
loginForm.addEventListener('submit', (e) => {
  e.preventDefault();
  
  const documento = document.getElementById('loginDocumento').value;
  const contrasena = document.getElementById('loginContrasena').value;
  
  let isValid = true;
  
  if (!validateDocumento(documento, 'loginDocumento', 'loginDocumentoError')) isValid = false;
  if (!validateContrasena(contrasena, 'loginContrasena', 'loginContrasenaError')) isValid = false;
  
  // Simulación de verificación de contraseña (propuesta Opción 1: usuario fijo)
  const testDocumento = '1234567890';
  const testContrasena = 'Password1!';
  if (documento === testDocumento && contrasena === testContrasena) {
    hideError('loginContrasena', 'loginContrasenaError');
  } else {
    showError('loginContrasena', 'loginContrasenaError', 'Contraseña incorrecta.');
    isValid = false;
  }
  
  if (isValid) {
    const submitBtn = loginForm.querySelector('.btn-primary');
    
    simulateLoading(submitBtn, () => {
      showLoginOverlay();
      
      console.log('Login data:', {
        documento: documento,
        contrasena: contrasena,
        remember: document.getElementById('rememberMe').checked
      });
      
      setTimeout(() => {
        hideLoginOverlay(() => {
          alert('¡Login exitoso! Redirigiendo al dashboard...');
          window.location.href = "/dashboard.html"; // Descomentar si se quiere redirigir
        });
      }, 1500);
    });
  }
});

// Submit del formulario de registro (modificado con validaciones)
registerForm.addEventListener('submit', (e) => {
  e.preventDefault();
  
  const nombres = document.getElementById('registerNombres').value;
  const apellidos = document.getElementById('registerApellidos').value;
  const documento = document.getElementById('registerDocumento').value;
  const email = document.getElementById('registerEmail').value;
  const contrasena = document.getElementById('registerContrasena').value;
  const confirmarContrasena = document.getElementById('confirmContrasena').value;
  const acceptTerms = document.getElementById('acceptTerms').checked;
  
  let isValid = true;
  
  if (!validateNombreApellido(nombres, 'registerNombres', 'registerNombresError')) isValid = false;
  if (!validateNombreApellido(apellidos, 'registerApellidos', 'registerApellidosError')) isValid = false;
  if (!validateDocumento(documento, 'registerDocumento', 'registerDocumentoError')) isValid = false;
  if (!validateEmail(email, 'registerEmail', 'registerEmailError')) isValid = false;
  if (!validateContrasena(contrasena, 'registerContrasena', 'registerContrasenaError')) isValid = false;
  
  if (contrasena !== confirmarContrasena) {
    showError('confirmContrasena', 'confirmContrasenaError', 'Las contraseñas no coinciden.');
    isValid = false;
  } else {
    hideError('confirmContrasena', 'confirmContrasenaError');
  }
  
  if (!acceptTerms) {
    alert('Debes aceptar los términos y condiciones');
    isValid = false;
  }
  
  if (isValid) {
    const rol_id = getRolIdFromEmail(email);
    const submitBtn = registerForm.querySelector('.btn-primary');
    
    simulateLoading(submitBtn, () => {
      console.log('Register data:', {
        nombres: nombres,
        apellidos: apellidos,
        documento: documento,
        email: email,
        contrasena: contrasena,
        rol_id: rol_id // Agregado para simulación
      });
      
      alert('¡Registro exitoso! Bienvenido a MindWell 🎉');
      
      setTimeout(() => {
        switchForm('login');
        document.getElementById('loginDocumento').value = documento;
      }, 1000);
    });
  }
});

// Animaciones de inputs (ya existía, lo mantengo)
document.querySelectorAll('.form-input').forEach(input => {
  input.addEventListener('focus', () => {
    input.parentElement.style.transform = 'scale(1.02)';
    input.style.borderColor = '#cc96f9';
  });
  
  input.addEventListener('blur', () => {
    input.parentElement.style.transform = 'scale(1)';
    if (!input.value) {
      input.style.borderColor = '#e1e5e9';
    }
  });
});

// Prevenir paste en confirmación de contraseña (ya existía)
document.getElementById('confirmContrasena').addEventListener('paste', (e) => {
  e.preventDefault();
  alert('Por favor, escribe tu contraseña nuevamente para confirmarla');
});

// Inicialización (ya existía)
document.addEventListener('DOMContentLoaded', () => {
  createStars();
  
  setTimeout(() => {
    document.getElementById('loginDocumento').focus();
  }, 800);
});