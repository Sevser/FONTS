import * as d3 from 'd3';
import urlLetters from './urlLetters';
import * as paper from 'paper';
import * as saveSvgAsPng from 'save-svg-as-png';

var GIF = require('./gif');

var frames = 60,
    countLetters = 0,
    duration = 50;

export default class handlerInput {
    constructor(state) {
        this.state = state;
        this.input = d3.select('.text-input');
        this.getLetters = d3.select('.button-done');
        this.makeGif = d3.select('#makeGif');
        this.outer = d3.select('#svgContainer');
        this.downloadFile = d3.select('#downloadFile');
        this.currentLetter = null;
        this.currentLetters = [];
        this.arrayLetters = urlLetters.letters;
        this.initSubs();
    }

    initSubs() {
        this.input.on('change', this.getValue.bind(this));
        this.getLetters.on('click', this.downloadLetters.bind(this));
        this.makeGif.on('click', this.createGifInstance.bind(this));
    }

    createGifInstance() {
        this.gif = new GIF.GIF({
            workers: 12,
            quality: 0,
            repeat: 0,
            height: 400,
            width: 400,
            background: this.state.color.background,
            transparent: this.state.color.background
        });

        this.gif.on('finished', function (blob) {
            d3.select('.image')
                .attr('class', 'image')
                .attr('src', URL.createObjectURL(blob));
            this.downloadFile.node().href = URL.createObjectURL(blob);
            this.downloadFile.node().dataset.downloadurl = blob;
            this.downloadFile.node().click()
            download(blob, this.input.node().value, "image/gif");

            window.open(URL.createObjectURL(blob));
        }.bind(this));
        var q = queue(1);

        d3.range(frames)
            .forEach(function (f) {
                q.defer(this.addFrame.bind(this), f * duration / (frames - 1));
            }.bind(this));

        q.awaitAll(function () {

            // Show SVG as progress bar
            this.outer.style('display', 'block');

            // Start web workers
            this.gif.render();

        }.bind(this));
    }

    getValue() {
        console.log(event.target.value);
    }

    htmlToElement(html) {
        var template = document.createElement('div');
        html = html.trim(); // Never return a text node of whitespace as the result
        template.innerHTML = html;
        return template.firstChild;
    }

    downloadLetters() {
        const string = this.input.node().value;
        duration = string.length * 10;
        frames = string.length * 10;
        countLetters = string.length;
        const stringsArray = string.split(' ');
        let currentLine = 0;
        let scale = 0.03;
        let idsCOUNT = 0;
        let currentOffsetLeft = 0;
        stringsArray.forEach(stringArray => {
            const arrayLetters = stringArray.split('');
            arrayLetters.push(' ');
            arrayLetters.forEach((letter, index) => {
                const matrix = new paper.Matrix(scale, 0, 0, scale, currentOffsetLeft, currentLine * 1000 * scale);
                const template = this.arrayLetters.find(urlLetter => {
                    return letter === urlLetter.name;
                });
                if (template) {
                    currentOffsetLeft = currentOffsetLeft + template.horizAdvX * scale;
                    let letterTemplate = this.htmlToElement('<div>' + template.template + '</div>');
                    const path = letterTemplate.querySelector('path[style]');
                    const clippath = letterTemplate.querySelector('clippath');
                    const path2 = clippath.firstElementChild;
                    path.contentEditable = true;
                    clippath.contentEditable = true;
                    path2.contentEditable = true;
                    var arrayFirstPath = path.getAttribute('d')
                        .split(' ');
                    arrayFirstPath = arrayFirstPath.map(element => element.substring(1)
                        .split(','));
                    var arraySecondPath = path2.getAttribute('d')
                        .split(' ')
                        .filter(element => element);
                    arraySecondPath = arraySecondPath.map((element, index, array) => {
                        if (element.substring(0, 1) === 'M') {
                            return [element.substring(1), array[index + 1]];
                        } else if (element.substring(0, 1) === 'Q') {
                            return [element.substring(1), array[index + 1], array[index + 2], array[index + 3]];
                        }
                        return null;
                    })
                        .filter(element => element);

                    arrayFirstPath = arrayFirstPath.map(element => {
                        var point = new paper.Point(parseFloat(element[0]), parseFloat(element[1]));
                        point = matrix.transform(point);
                        return [point.x, point.y];
                    });
                    arrayFirstPath = arrayFirstPath.map((element, index) => {
                        if (index) {
                            return 'L' + element.join(',');
                        }
                        return 'M' + element.join(',');
                    })
                        .join(' ');
                    path.setAttribute('d', arrayFirstPath);
                    arraySecondPath = arraySecondPath.map(element => {
                        if (element.length === 2) {
                            var point = new paper.Point(parseFloat(element[0]), parseFloat(element[1]));
                            point = matrix.transform(point);
                            return [point.x, point.y];
                        }
                        var point = new paper.Point(parseFloat(element[0]), parseFloat(element[1]));
                        var point1 = new paper.Point(parseFloat(element[2]), parseFloat(element[3]));
                        point = matrix.transform(point);
                        point1 = matrix.transform(point1);
                        return [point.x, point.y, point1.x, point1.y];
                    });
                    arraySecondPath = arraySecondPath.map((element, index) => {
                        if (index) {
                            if (element.length === 4) {
                                return 'Q' + element.join(' ');
                            } else {
                                return 'M' + element.join(' ');
                            }
                        }
                        return 'M' + element.join(' ');
                    })
                        .join(' ');
                    path2.setAttribute('d', arraySecondPath + ' Z');
                    path.style.strokeWidth = parseFloat(path.style.strokeWidth.slice(0, -2)) * scale + 'px';
                    path.style.stroke = this.state.color.font ? this.state.color.font : '#000';
                    const id = 'a' + idsCOUNT;
                    idsCOUNT += 1;
                    const id2 = 'a' + idsCOUNT;
                    idsCOUNT += 1;
                    path.style.clipPath = 'url(#' + id2 + ')';
                    clippath.setAttribute('id', id2);
                    path.setAttribute('id', id);
                    path.setAttribute('stroke-dashoffset', 10000);

                    this.currentLetters.push({
                        id,
                        speedDrawing: template.speedDrawing,
                    });
                    document.querySelector('#svgContainer').innerHTML += letterTemplate.innerHTML;
                }
                if (currentOffsetLeft > 140) {
                    currentLine += 1;
                    currentOffsetLeft = 0;
                }
            });
            // currentLine += 1;
        });
        this.createGifInstance();
    }

    addFrame(t, cb) {
        this.drawFrame(t);
        saveSvgAsPng.svgAsPngUri(this.outer.node())
            .then(function (svg) {
                var img = new Image();
                img.onload = function () {
                    this.gif.addFrame(img, {
                        delay: 0.001,
                        copy: true,
                    });
                    cb(null, t);
                }.bind(this);
                img.src = svg;
            }.bind(this));
    }

    drawFrame(t) {
        console.log(t / 10);
        console.log(Math.floor(t / 10));
        if (Math.floor(t / 10) < this.currentLetters.length) {
            const node = document.querySelector('#' + this.currentLetters[Math.floor(t / 10)].id);
            const strokeDashOffset = parseInt(node.getAttribute('stroke-dashoffset'));
            node.setAttribute('stroke-dashoffset', strokeDashOffset - this.currentLetters[Math.floor(t / 10)].speedDrawing);
        }
    }
}

