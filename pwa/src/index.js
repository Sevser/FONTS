import './scss/style.css';
import './scss/style.scss';
import ColorPalette from './ColorPalette';

const state = {
    page: ['start', 'new-clip'],
    textMouseDown: false,
    textEditMode: 'move',
    text: {
        size: {
            w: 0,
            h: 0,
        },
        pos: {
            x: 0,
            y: 0,
        },
        scale: 1,
        lastScale: 1,
        pinSize: 16,
        translate: {
            x: 0,
            y: 0,
        },
        angle: 0,
    },
    color: {
        font: '',
        background: '',
    },
};


const pageStart = document.querySelector('.start-page');
const pageMain = document.querySelector('.main-page');
const buttonStart = document.querySelector('.button-start');
const inputFile = document.querySelector('#file');
const imageArea = document.querySelector('.main-page_edit_area');
const imageElement = document.querySelector('.image');
const textElement = document.querySelector('.text');
const inputText = document.querySelector('.text-input');
const pinTextResize = document.querySelector('.text-resize');
const buttonFontColor = document.querySelector('.button-color-font');
const buttonBackgroundColor = document.querySelector('.button-color-background');

const colorPalette = new ColorPalette();

const mousePos = { x: 0, y: 0 };
const textPos = { x: 0, y: 0 };
const textSize = { w: 0, h: 0 };
let touchTimeStamp = 0;


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
function textInputSelector(e) {
    let text = e.target.value;
    pinTextResize.classList.remove('text-rotate-active');
    pinTextResize.classList.remove('text-resize-active');
    state.textEditMode = 'move';
    const regexp = /[A-Za-z]+$/;
    if (text.match(regexp) || text === '') {
        const span = document.querySelector('.span-text');
        span.innerHTML = text;
    } else {
        text = text.split('');
        text.pop();
        text = text.join('');
        inputText.value = text;
    }
}
function dragText(e) {
    if (state.textEditMode === 'move') {
        const dx = e.changedTouches[0].pageX - mousePos.x;
        const dy = e.changedTouches[0].pageY - mousePos.y;
        const imageAreaPos = imageArea.getBoundingClientRect();
        if (e.changedTouches[0].pageX >= imageAreaPos.left
            && e.changedTouches[0].pageX <= imageAreaPos.right
            && e.changedTouches[0].pageY >= imageAreaPos.top
            && e.changedTouches[0].pageY <= imageAreaPos.bottom) {
            state.text.translate.x = state.text.pos.x + dx;
            state.text.translate.y = state.text.pos.y + dy;
            textElement.style.transform = `translate(${state.text.translate.x}px,${state.text.translate.y}px) scale(${state.text.scale})  rotate(${state.text.angle}deg)`;
        }
    }
}

function dragTextPin(e) {
    if (state.textEditMode === 'resize') {
        e.preventDefault();
        e.stopPropagation();
        const x = e.changedTouches[0].pageX;
        const y = e.changedTouches[0].pageY;
        const imageAreaPos = imageArea.getBoundingClientRect();
        if (e.changedTouches[0].pageX >= imageAreaPos.left
            && e.changedTouches[0].pageX <= imageAreaPos.right
            && e.changedTouches[0].pageY >= imageAreaPos.top
            && e.changedTouches[0].pageY <= imageAreaPos.bottom) {
            const width = Math.abs(x - state.text.pos.x);
            const scale = ((width * state.text.lastScale) / state.text.size.w);
            if (scale) {
                textElement.style.transform = `translate(${state.text.translate.x}px,${state.text.translate.y}px) scale(${scale}) rotate(${state.text.angle}deg)`;
                state.text.scale = scale;
                pinTextResize.style.transform = `scale(${1 / scale})`;
            }
        }
    } else if (state.textEditMode === 'rotate') {
        e.preventDefault();
        e.stopPropagation();
        const x = e.changedTouches[0].pageX;
        const y = e.changedTouches[0].pageY;
        state.text.angle = 180 + Math.atan2(state.text.pos.y - y, state.text.pos.x - x) * 180 / Math.PI;
        const imageAreaPos = imageArea.getBoundingClientRect();
        if (e.changedTouches[0].pageX >= imageAreaPos.left
            && e.changedTouches[0].pageX <= imageAreaPos.right
            && e.changedTouches[0].pageY >= imageAreaPos.top
            && e.changedTouches[0].pageY <= imageAreaPos.bottom) {
            textElement.style.transform = `translate(${state.text.translate.x}px,${state.text.translate.y}px) scale(${state.text.scale}) rotate(${state.text.angle}deg)`;
        }
    }
}

function startTextResize() {
    pinTextResize.addEventListener('touchstart', (e) => {
        mousePos.x = e.changedTouches[0].pageX;
        mousePos.y = e.changedTouches[0].pageY;
        textSize.w = textElement.getBoundingClientRect.right - textElement.getBoundingClientRect.left;
        textSize.h = textElement.getBoundingClientRect.bottom - textElement.getBoundingClientRect.top;
        state.text.pos.x = pinTextResize.getBoundingClientRect().left;
        state.text.pos.y = pinTextResize.getBoundingClientRect().top;
        imageArea.addEventListener('touchmove', dragTextPin);
    });
}
function toggleTextEditMode() {
    switch (state.textEditMode) {
    case 'move':
        state.textEditMode = 'resize';
        pinTextResize.classList.add('text-resize-active');
        break;
    case 'resize':
        state.textEditMode = 'rotate';
        pinTextResize.classList.remove('text-resize-active');
        pinTextResize.classList.add('text-rotate-active');
        break;
    case 'rotate':
        state.textEditMode = 'move';
        pinTextResize.classList.remove('text-rotate-active');
        break;
    default: break;
    }
}
function completeDragText(e) {
    if ((e.timeStamp - touchTimeStamp) < 100) {
        startTextResize();
        toggleTextEditMode();
    } else {
        state.text.lastScale = state.text.scale;
    }
    imageArea.removeEventListener('touchmove', dragText);
    imageArea.removeEventListener('touchmove', dragTextPin);
}

function getFontColor() {
    colorPalette.getColor('font').then((color) => {
        state.color.font = color.hexString;
        textElement.style.color = state.color.font;
    });
}

function getBackgroundColor() {
    colorPalette.getColor('background').then((color) => {
        state.color.background = color.hexString;
        imageArea.style.backgroundColor = color.hexString;
    });
}

inputFile.addEventListener('change', fileInputSelector);
inputText.addEventListener('input', textInputSelector);
buttonFontColor.addEventListener('click', getFontColor);
buttonBackgroundColor.addEventListener('click', getBackgroundColor);
imageArea.addEventListener('touchend', completeDragText);
textElement.addEventListener('touchstart', (e) => {
    touchTimeStamp = e.timeStamp;
    mousePos.x = e.changedTouches[0].pageX;
    mousePos.y = e.changedTouches[0].pageY;
    state.text.pos.x = textElement.getBoundingClientRect().left;
    state.text.pos.y = textElement.getBoundingClientRect().top;
    state.text.size.w = textElement.getBoundingClientRect().width;
    state.text.size.h = textElement.getBoundingClientRect().height;
    imageArea.addEventListener('touchmove', dragText);
});
