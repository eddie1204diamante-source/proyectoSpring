// JS (carrusel.js)
const carrusel = document.getElementById("slider-container");
const totalSlides = carrusel.children.length;
let index = 0;
let interval;

function goToSlide(i) {
  index = i;
  carrusel.style.transform = `translateX(-${index * 100}%)`;
}

function nextSlide() {
  index = (index + 1) % totalSlides;
  goToSlide(index);
}

function prevSlide() {
  index = (index - 1 + totalSlides) % totalSlides;
  goToSlide(index);
}

function startInterval() {
  interval = setInterval(nextSlide, 5000);
}

function resetInterval() {
  clearInterval(interval);
  startInterval();
}

document.getElementById("next").addEventListener("click", () => {
  nextSlide();
  resetInterval();
});

document.getElementById("back").addEventListener("click", () => {
  prevSlide();
  resetInterval();
});

startInterval();

// Imagen interactiva que mira al mouse
const interactiveImg = document.getElementById("interactive-img");

const images = [
  "Assets/img/fondo 2.jpg",
  "Assets/img/fondo.png",
  "Assets/img/images.jfif"
];
let currentImg = 0;

// Efecto más brusco (más grados)
document.addEventListener("mousemove", (e) => {
  const { innerWidth, innerHeight } = window;
  const x = (e.clientX / innerWidth - 0.5) * 2;
  const y = (e.clientY / innerHeight - 0.5) * 2;

  const rotateX = -y * 40; // antes era 15
  const rotateY = x * 40;  // antes era 15

  interactiveImg.style.transform = `rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
});

// Cambiar imagen con clic
interactiveImg.addEventListener("click", () => {
  currentImg = (currentImg + 1) % images.length;
  interactiveImg.src = images[currentImg];
});

interactiveImg.addEventListener("click", () => {
  // Efecto boing
  interactiveImg.classList.add("boing");

  // Cuando termine la animación, cambiar imagen y quitar clase
  interactiveImg.addEventListener("animationend", function handler() {
    currentImg = (currentImg + 1) % images.length;
    interactiveImg.src = images[currentImg];
    interactiveImg.classList.remove("boing");
    interactiveImg.removeEventListener("animationend", handler);
  });
});
