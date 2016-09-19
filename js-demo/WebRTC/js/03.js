var video = document.querySelector('video');
var canvas = window.canvas = document.querySelector('canvas');

canvas.width = 480;
canvas.height = 360;

var button = document.querySelector('button');

button.onclick = function () {
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    canvas.getContext('2d')
        .drawImage(video, 0, 0, canvas.width, canvas.height);
};

var constraints = {
    audio: true,
    video: true
};

function handleSuccess(stream) {
    window.stream = stream;
    video.srcObject = stream;
}

function handleError(err) {
    console.error('navigator.getUserMedia error: ', err);
}

navigator.mediaDevices.getUserMedia(constraints)
    .then(handleSuccess).catch(handleError);