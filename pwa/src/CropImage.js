import Croppier from 'cropperjs';
import './scss/crop.css';


function CropImage() {
    this.cropPage = document.querySelector('.crop-page');
    this.buttonOk = document.querySelector('.button-crop-ok');
    this.imageElement = document.querySelector('.image-to-crop');

    this.image = null;
    this.croppier = null;

    this.getCropImage = image => new Promise((resolve) => {
        this.cropPage.classList.remove('hide');
        this.imageElement.src = image;
        this.image = image;
        if (!this.croppier) {
            this.croppier = new Croppier(this.imageElement, {
                aspectRatio: 16 / 16,
                viewMode: 1,
                crop(event) {
                    console.log(event);
                },
            });
        } else {
            this.croppier.replace(image);
        }
        const okHandler = () => {
            this.cropPage.classList.add('hide');
            this.buttonOk.removeEventListener('click', okHandler);
            this.croppier.getCroppedCanvas().toBlob((blob) => {
                const fr = new FileReader();
                fr.onload = () => {
                    resolve(fr.result);
                };
                fr.readAsDataURL(blob);
            });
        };
        this.buttonOk.addEventListener('click', okHandler);
    });
}

export default CropImage;
