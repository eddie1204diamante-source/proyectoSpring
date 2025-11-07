document.addEventListener('DOMContentLoaded', () => {
  // 🌙 Modo oscuro
  const body = document.body;
  const themeIcon = document.getElementById('theme-icon');
  const themeToggle = document.getElementById('theme-toggle');
  const sidebar = document.getElementById('sidebar');
  

  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'dark') {
    body.setAttribute('data-theme', 'dark');
    themeIcon.textContent = '☀';
  } else {
    themeIcon.textContent = '🌙';
  }

  themeToggle.addEventListener('click', () => {
    if (body.getAttribute('data-theme') === 'dark') {
      body.removeAttribute('data-theme');
      themeIcon.textContent = '🌙';
      localStorage.setItem('theme', 'light');
    } else {
      body.setAttribute('data-theme', 'dark');
      themeIcon.textContent = '☀';
      localStorage.setItem('theme', 'dark');
    }
  });

  // 📊 Gráfico circular (Chart.js)
  const pieCtx = document.getElementById('pieChart').getContext('2d');
  new Chart(pieCtx, {
    type: 'doughnut',
    data: {
      labels: ['Estrés Alto', 'Estrés Medio', 'Estrés Bajo'],
      datasets: [{
        data: [estresAlto, estresMedio, estresBajo],
        backgroundColor: ['#ef4444', '#f59e0b', '#10b981'],
        borderWidth: 0,
        hoverOffset: 10
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { position: 'bottom' }
      },
      cutout: '60%'
    }
  });

  // 📈 Gráfico de líneas
  const lineCtx = document.getElementById('lineChart').getContext('2d');
  const months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

  const getVolatileData = (base, variation) => {
    const values = [];
    for (let i = 0; i < 6; i++) {
      values.push(Math.max(0, Math.floor(base + (Math.random() * variation * 2 - variation))));
    }
    return values.concat(Array(6).fill(null));
  };

  new Chart(lineCtx, {
    type: 'line',
    data: {
      labels: months,
      datasets: [
        {
          label: 'Totalmente estresados',
          data: getVolatileData(6, 3),
          borderColor: '#ef4444',
          backgroundColor: 'rgba(239, 68, 68, 0.1)',
          tension: 0.4,
          fill: true
        },
        {
          label: 'Parcialmente estresados',
          data: getVolatileData(4, 2),
          borderColor: '#f59e0b',
          backgroundColor: 'rgba(245, 158, 11, 0.1)',
          tension: 0.4,
          fill: true
        },
        {
          label: 'No estresados',
          data: getVolatileData(4, 2),
          borderColor: '#10b981',
          backgroundColor: 'rgba(16, 185, 129, 0.1)',
          tension: 0.4,
          fill: true
        }
      ]
    },
    options: { responsive: true }
  });
});
