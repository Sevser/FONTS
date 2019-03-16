import './scss/style.css';
import './scss/style.scss';

const state = {
    page: ['start', 'new-clip'],
};

const pageStart = document.querySelector('.start-page');
const pageMain = document.querySelector('.main-page');
const buttonStart = document.querySelector('.button-start');
const inputFile = document.querySelector('#file');
const imageArea = document.querySelector('.main-page_edit_area');
const imageElement = document.querySelector('.image');

const hide = function hide(el) {
    el.classList.add('hide');
};
const show = function show(el) {
    el.classList.remove('hide');
};

hide(pageMain);
hide(imageElement);
buttonStart.addEventListener('click', () => {
    hide(pageStart);
    show(pageMain);
});
const reader = new FileReader();
function fileInputSelector() {
    reader.onload = () => {
        imageElement.src = reader.result;
        show(imageElement);
    };
    reader.readAsDataURL(this.files[0]);
}

inputFile.addEventListener('change', fileInputSelector);
