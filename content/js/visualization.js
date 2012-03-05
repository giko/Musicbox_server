/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 03.03.12
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
var visualization = new (function () {
    this.drawMainVisualzation = function (visualization, waveform, eqData, peakData) {
        canvas = document.getElementById("mainvisualization");
        canvas.height = 160;

        canvas.width = screen.width;
        ctx = canvas.getContext('2d');
        switch (visualization) {
            case 0:
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                ctx.beginPath();
                ctx.moveTo(0, 128);
                ctx.lineTo(canvas.width, 128);
                ctx.lineWidth = (peakData.left + peakData.right) / 2 * 20;
                ctx.strokeStyle = "rgb(" + Math.round((peakData.left + peakData.right) / 2 * 255) + ",0,0)";
                ctx.stroke();
                ctx.closePath();

                ctx.beginPath();
                ctx.moveTo(0, 128);
                ctx.lineWidth = 3;
                ctx.strokeStyle = "#000000";
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * canvas.width / 256, (-(eqData.right[i] + eqData.left[i]) - 1) * 32 + 160);
                }
                ctx.stroke();
                ctx.closePath();
                break;
            case 1:
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                ctx.beginPath();
                ctx.moveTo(0, 64);
                ctx.lineTo(canvas.width, 64);
                ctx.lineWidth = (peakData.left + peakData.right) / 2 * 20;
                ctx.strokeStyle = "rgb(" + Math.round((peakData.left + peakData.right) / 2 * 255) + ",0,0)";
                ctx.stroke();
                ctx.closePath();

                ctx.beginPath();
                ctx.moveTo(0, 64);
                ctx.lineWidth = 3;
                ctx.strokeStyle = "#000000";
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * canvas.width / 256, waveform.right[i] * 32 + 64);
                }
                ctx.moveTo(0, 64);
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * canvas.width / 256, waveform.left[i] * 32 + 64);
                }
                ctx.stroke();
                ctx.closePath();
                break;
            case 2:
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                ctx.beginPath();
                ctx.strokeStyle = "#ADFF2F";
                ctx.lineWidth = (peakData.left + peakData.right);
                ctx.moveTo(0, 100);
                for (var i = 0; i < 256; i++) {
                    ctx.moveTo(i * canvas.width / 256, waveform.left[i] * 32 + 10)
                    ctx.lineTo(i * canvas.width / 256, waveform.right[i] * 32 + 64);
                }

                ctx.strokeStyle = "#000000";
                ctx.lineWidth = (peakData.left + peakData.right) * 2;
                ctx.moveTo(0, 64);
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * canvas.width / 256, waveform.right[i] * 32 + 64);
                }

                ctx.moveTo(0, 100);
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * canvas.width / 256, waveform.right[i] * 32 + 100);
                }
                ctx.stroke();
                ctx.closePath();
                break;
        }
    };
    this.drawMainBass = function (waveform, eqData, peakData) {

        canvas = document.getElementById("bassvolume");
        canvas.height = 90;
        canvas.width = 190;
        var a = 100;
        var b = 90;

        ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        ctx.lineWidth = 40;
            ctx.moveTo(50, a)
            ctx.lineTo(50, b-peakData.left * b);

            ctx.moveTo(a, a)
            ctx.lineTo(a, b-peakData.right * b);
        ctx.strokeStyle = "rgb(" + Math.round((peakData.left + peakData.right) / 2 * 255) + ",0,0)";
        ctx.stroke();
        ctx.closePath();
    }
});
