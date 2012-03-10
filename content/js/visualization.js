/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 03.03.12
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
var visualization = new (function () {
    var maincanvas, basscanvas;
    var mainctx, bassctx;

    this.init = function () {
        maincanvas = document.getElementById("mainvisualization");
        maincanvas.height = 160;

        maincanvas.width = window.innerWidth - 17;
        mainctx = maincanvas.getContext('2d');

        basscanvas = document.getElementById("bassvolume");
        basscanvas.height = 90;
        basscanvas.width = 190;

        bassctx = basscanvas.getContext('2d');
    }

    this.drawMainVisualzation = function (visualization, waveform, eqData, peakData) {
        switch (visualization) {
            case 0:
                mainctx.clearRect(0, 0, maincanvas.width, maincanvas.height);
                mainctx.beginPath();
                mainctx.moveTo(0, 128);
                mainctx.lineTo(maincanvas.width, 128);
                mainctx.lineWidth = (peakData.left + peakData.right) / 2 * 20;
                mainctx.strokeStyle = "rgb(" + Math.round((peakData.left + peakData.right) / 2 * 255) + ",0,0)";
                mainctx.stroke();
                mainctx.closePath();

                mainctx.beginPath();
                mainctx.moveTo(0, 128);
                mainctx.lineWidth = 3;
                mainctx.strokeStyle = "#000000";
                for (var i = 0; i < 256; i++) {
                    mainctx.lineTo(i * maincanvas.width / 256, (-(eqData.right[i] + eqData.left[i]) - 1) * 32 + 160);
                }
                mainctx.stroke();
                mainctx.closePath();
                break;
            case 1:
                mainctx.clearRect(0, 0, maincanvas.width, maincanvas.height);
                mainctx.beginPath();
                mainctx.moveTo(0, 64);
                mainctx.lineTo(maincanvas.width, 64);
                mainctx.lineWidth = (peakData.left + peakData.right) / 2 * 20;
                mainctx.strokeStyle = "rgb(" + Math.round((peakData.left + peakData.right) / 2 * 255) + ",0,0)";
                mainctx.stroke();
                mainctx.closePath();

                mainctx.beginPath();
                mainctx.moveTo(0, 64);
                mainctx.lineWidth = 3;
                mainctx.strokeStyle = "#000000";
                for (var i = 0; i < 256; i++) {
                    mainctx.lineTo(i * maincanvas.width / 256, waveform.right[i] * 32 + 64);
                }
                mainctx.moveTo(0, 64);
                for (var i = 0; i < 256; i++) {
                    mainctx.lineTo(i * maincanvas.width / 256, waveform.left[i] * 32 + 64);
                }
                mainctx.stroke();
                mainctx.closePath();
                break;
            case 2:
                mainctx.clearRect(0, 0, maincanvas.width, maincanvas.height);
                mainctx.beginPath();
                mainctx.strokeStyle = "#000000";
                mainctx.lineWidth = (peakData.left + peakData.right) * 2;
                mainctx.moveTo(0, 100);
                for (var i = 0; i < 256; i++) {
                    mainctx.moveTo(i * maincanvas.width / 256, waveform.left[i] * 32 + 10);
                    mainctx.lineTo(i * maincanvas.width / 256, waveform.right[i] * 32 + 64);
                }
                mainctx.stroke();
                mainctx.closePath();

                mainctx.beginPath();
                mainctx.moveTo(0, 64);
                for (var i = 0; i < 256; i++) {
                    mainctx.lineTo(i * maincanvas.width / 256, waveform.right[i] * 32 + 64);
                }

                mainctx.moveTo(0, 96);
                for (var i = 0; i < 256; i++) {
                    mainctx.lineTo(i * maincanvas.width / 256, waveform.right[i] * 32 + 96);
                }
                mainctx.stroke();
                mainctx.closePath();
                break;
        }
    };
    this.drawMainBass = function (peakData) {
        max_y = 100;
        bassctx.clearRect(0, 0, basscanvas.width, basscanvas.height);
        bassctx.beginPath();
        bassctx.lineWidth = 40;
        bassctx.strokeStyle = "rgb(0,0," + Math.round((1 + peakData.left) * 127) + ")";
        bassctx.moveTo(50, max_y);
        bassctx.lineTo(50, basscanvas.height - peakData.left * basscanvas.height);
        bassctx.stroke();
        bassctx.closePath();

        bassctx.beginPath();
        bassctx.strokeStyle = "rgb(" + Math.round((1 + peakData.right) * 127) + ",0,0)";
        bassctx.moveTo(100, max_y);
        bassctx.lineTo(100, basscanvas.height - peakData.right * basscanvas.height);
        bassctx.stroke();
        bassctx.closePath();
    };
});
