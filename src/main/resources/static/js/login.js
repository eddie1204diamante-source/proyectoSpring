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

    // Validación de contraseña
    function validatePassword(password) {
      return {
        length: password.length >= 8,
        uppercase: /[A-Z]/.test(password),
        lowercase: /[a-z]/.test(password),
        number: /\d/.test(password),
        special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
      };
    }

    // Actualizar fortaleza de contraseña
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

    // Mostrar/ocultar overlay
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

    // Simulación de carga
    function simulateLoading(button, callback) {
      button.classList.add('loading');
      button.disabled = true;

      setTimeout(() => {
        button.classList.remove('loading');
        button.disabled = false;
        callback();
      }, 2000);
    }

    // Event listeners para cambio de formularios
    document.getElementById('showRegister').addEventListener('click', (e) => {
      e.preventDefault();
      switchForm('register');
    });

    document.getElementById('showLogin').addEventListener('click', (e) => {
      e.preventDefault();
      switchForm('login');
    });

    // Actualizar fortaleza de contraseña en tiempo real
    document.getElementById('registerContrasena').addEventListener('input', (e) => {
      updatePasswordStrength(e.target.value);
    });

    // Submit del formulario de login
    loginForm.addEventListener('submit', (e) => {
      e.preventDefault();
      
      const documento = document.getElementById('loginDocumento').value;
      const contrasena = document.getElementById('loginContrasena').value;
      
      if (documento && contrasena) {
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
            window.location.href = "/dashboard.html";
              alert('¡Login exitoso! Redirigiendo al dashboard...');
            });
          }, 1500);
        });
      }
    });

    // Submit del formulario de registro
    registerForm.addEventListener('submit', (e) => {
      e.preventDefault();
      
      const nombres = document.getElementById('registerNombres').value;
      const apellidos = document.getElementById('registerApellidos').value;
      const documento = document.getElementById('registerDocumento').value;
      const email = document.getElementById('registerEmail').value;
      const contrasena = document.getElementById('registerContrasena').value;
      const confirmarContrasena = document.getElementById('confirmContrasena').value;
      const acceptTerms = document.getElementById('acceptTerms').checked;
      
      if (!acceptTerms) {
        alert('Debes aceptar los términos y condiciones');
        return;
      }
      
      if (contrasena !== confirmarContrasena) {
        alert('Las contraseñas no coinciden');
        return;
      }
      
      if (nombres && apellidos && documento && email && contrasena) {
        const submitBtn = registerForm.querySelector('.btn-primary');
        
        simulateLoading(submitBtn, () => {
          console.log('Register data:', {
            nombres: nombres,
            apellidos: apellidos,
            documento: documento,
            email: email,
            contrasena: contrasena
          });
          
          alert('¡Registro exitoso! Bienvenido a MindWell 🎉');
          
          setTimeout(() => {
            switchForm('login');
            document.getElementById('loginDocumento').value = documento;
          }, 1000);
        });
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
        if (!input.value) {
          input.style.borderColor = '#e1e5e9';
        }
      });
    });

    // Prevenir paste en confirmación de contraseña
    document.getElementById('confirmContrasena').addEventListener('paste', (e) => {
      e.preventDefault();
      alert('Por favor, escribe tu contraseña nuevamente para confirmarla');
    });

    // Inicialización
    document.addEventListener('DOMContentLoaded', () => {
      createStars();
      
      setTimeout(() => {
        document.getElementById('loginDocumento').focus();
      }, 800);
    });