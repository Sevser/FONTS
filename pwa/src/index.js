import './scss/style.css';
import './scss/style.scss';

const state = {
    page: ['start', 'new-clip'],
    textMouseDown: false,
    textEditMode: 'none',
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
    const regexp = /[A-Za-z]+$/;
    if (text.match(regexp)) {
        textElement.innerHTML = text;
    } else {
        text = text.split('').pop().join('');
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
            textElement.style.transform = `translate(${state.text.pos.x + dx}px,${state.text.pos.y + dy}px)`;
        }
    }
}

function dragTextPin(e) {
    const x = e.changedTouches[0].pageX;
    const y = e.changedTouches[0].pageY;
    const imageAreaPos = imageArea.getBoundingClientRect();
    if (e.changedTouches[0].pageX >= imageAreaPos.left
        && e.changedTouches[0].pageX <= imageAreaPos.right
        && e.changedTouches[0].pageY >= imageAreaPos.top
        && e.changedTouches[0].pageY <= imageAreaPos.bottom) {
        const width = Math.abs(x - state.text.pos.x);
        console.log(width);
        console.log(state.text.size.w);
        const scale = ((width * state.text.lastScale) / state.text.size.w);
        console.log(scale);
        if (scale) {
            textElement.style.transform = `scale(${scale})`;
            state.text.scale = scale;
            const newPinSize = Math.round(state.text.pinSize / scale);
            pinTextResize.style.width = `${newPinSize}px`;
            pinTextResize.style.height = `${newPinSize}px`;
            pinTextResize.style.right = `${-Math.round(newPinSize / 2)}px`;
            pinTextResize.style.bottom = `${-Math.round(newPinSize / 2)}px`;
        }
    }
}

function startTextResize() {
    state.textEditMode = 'resize';
    pinTextResize.classList.add('text-resize-active');
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

function completeDragText(e) {
    console.log(textElement.getBoundingClientRect());
    console.log(textElement.style.top);
    console.log(e.timeStamp);
    if ((e.timeStamp - touchTimeStamp) < 100) {
        startTextResize();
    } else {
        state.textEditMode = 'none';
        state.text.lastScale = state.text.scale;
    }
    imageArea.removeEventListener('touchmove', dragText);
}


inputFile.addEventListener('change', fileInputSelector);
inputText.addEventListener('input', textInputSelector);
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
