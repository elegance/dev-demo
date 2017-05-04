var dimensions = document.querySelector('#dimensions');
var video = document.querySelector('video');
var stream;

var vgaButton = document.querySelector('#vga');
var qvgaButton = document.querySelector('#qvga');
var hdButton = document.querySelector('#hd');
var fullHdButton = document.querySelector('#full-hd');

vgaButton.onclick = function () {
    getMedia(vgaConstraints);
};

qvgaButton.onclick = function () {
    getMedia(qvgaConstraints);
};

hdButton.onclick = function () {
    getMedia(hdConstraints);
};

fullHdButton.onclick = function () {
    getMedia(fullHdConstraints);
};

var qvgaConstraints = {
    video: { width: { exact: 320 }, height: { exact: 240 } }
};

var vgaConstraints = {
    video: { width: { exact: 640 }, height: { exact: 480 } }
};

var hdConstraints = {
    video: { width: { exact: 1280 }, height: { exact: 720 } }
};

var fullHdConstraints = {
    video: { width: { exact: 1920 }, height: { exact: 1080 } }
};

function getMedia(constraints) {
    if (stream) {
        stream.getTracks().forEach(function(track) {
            track.stop();
        });
    }
    navigator.mediaDevices.getUserMedia(constraints)
        .then(goStream)
        .catch((e) => {
            alert(`getUserMedia error: ${e.name}`);
            console.error(e);
        });
}

function goStream(mediaStream) {
    stream = mediaStream;
    video.srcObject = mediaStream;
}

function displayVideoDimesions() {
    if (!video.videoWidth) {
        setTimeout(displayVideoDimesions, 500);
    }
    dimensions.innerHTML = `Actual video dimesions: ${video.videoWidth} X ${video.videoHeight} px.`;
}

video.onloadedmetadata = displayVideoDimesions;