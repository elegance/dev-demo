var btnSnapshot = document.querySelector('#snapshot');
var selFilter = document.querySelector('#filter');

var video = document.querySelector('video');
var canvas = document.querySelector('canvas');
canvas.width = 480;
canvas.height = 360;

btnSnapshot.onclick = function() {
    canvas.className = selFilter.value;
    canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
};

selFilter.onchange = function() {
    video.className = selFilter.value;
};

var constraints = {
    video: true,
    audio: true
};

function handleSuccess(stream) {
    video.srcObject = stream;
}

function handleError(err) {
    console.error('navigator.getUserMedia error:' , err);
}

navigator.mediaDevices.getUserMedia(constraints)
    .then(handleSuccess).catch(handleError);