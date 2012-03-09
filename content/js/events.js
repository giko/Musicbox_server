/**
 * Created by IntelliJ IDEA.
 * User: AxiS
 * Date: 06.03.12
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
//Captures the key press events
var canvas;
var ctx;
document.onkeydown = function (e) {
    var ev = e;
    if (ev.charCode && ev.charCode == 32)
        player.playPause();
    else {
        switch (ev.keyCode) {
            case 32:
                player.playPause();
                break;
            case 39:
                player.nextSong();
                break;
            case 37:
                player.prevSong();
                break;
            case 38:
                player.volumeInc();
                break;
            case 40:
                player.volumeDec();
                break;
        }
    }
}

window.onload = function(){
    if ( canvas.addEventListener ) {
        canvas.addEventListener('mousemove', function(evt){
        var mousePos = getMousePos(canvas, evt);
        }, false);
        canvas.addEventListener("mousedown", control.writeLine, false);
        canvas.addEventListener("mouseup", CreateWave, false);

    } else if ( canvas.attachEvent ) {
        canvas.attachEvent('mousemove', function(evt){
        var mousePos = getMousePos(canvas, evt);
        });
        canvas.attachEvent("mousedown", control.writeLine);
        canvas.attachEvent("mouseup", CreateWave);
    }
}

var control = new (function () {
    this.writeLine = function() {
        canvas = document.getElementById("bassvolume");
        ctx = canvas.getContext('2d');
        canvas.height = 90;
        canvas.width = 190;

        ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        ctx.lineWidth = 1;
        ctx.strokeStyle = "#000000";
        mousePos = control.getMousePos(canvas, evt);
        ctx.moveTo(0, mousePos.y);
        ctx.lineTo(190, mousePos.y);
        ctx.stroke();
        ctx.closePath();
    }

    this.getMousePos = function (canvas, evt){
        // get canvas position
        obj = canvas;
        top = 0;
        left = 0;
        while (obj && obj.tagName != 'BODY') {
            top += obj.offsetTop;
            left += obj.offsetLeft;
            obj = obj.offsetParent;
        }

         // return relative mouse position
        mouseX = evt.clientX - left + window.pageXOffset;
        mouseY = evt.clientY - top + window.pageYOffset;
        return {
              x: mouseX,
              y: mouseY
        };
    }
});