import iro from '@jaames/iro';

function ColorPalette() {
    this.colorPage = document.querySelector('.color-page');
    this.colorSample = document.querySelector('.color-page-text');
    this.buttonOk = document.querySelector('.button-color-ok');
    this.color = null;
    this.type = '';

    this.getColor = type => new Promise((resolve) => {
        this.type = type;
        this.colorPage.classList.remove('hide');
        const okHandler = () => {
            this.colorPage.classList.add('hide');
            this.buttonOk.removeEventListener('click', okHandler);
            resolve(this.color);
        };
        this.buttonOk.addEventListener('click', okHandler);
    });

    this.onColorChange = function onColorChange(e) {
        this.color = e;
        if (this.type === 'font') {
            this.colorSample.style.color = e.hexString;
        } else {
            this.colorPage.style.backgroundColor = e.hexString;
        }
    };

    this.picker = new iro.ColorPicker('#color-picker-container');
    this.picker.on('color:change', this.onColorChange.bind(this));
}

export default ColorPalette;
