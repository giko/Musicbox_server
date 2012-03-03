/***************************/
//@Author: Adrian "yEnS" Mato Gondelle & Ivan Guardado Castro
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!
/***************************/
var canvas;
var ctx;
Array.prototype.avg = function () {
    var av = 0;
    var cnt = 0;
    var len = this.length;
    for (var i = 0; i < len; i++) {
        var e = +this[i];
        if (!e && this[i] !== 0 && this[i] !== '0') e--;
        if (this[i] == e) {
            av += e;
            cnt++;
        }
    }
    return av / cnt;
}
//This is the class that interact with the interface
var player = new (function () {
    soundManager.url = '/bootstrap/swf';
    soundManager.flashVersion = 9; // optional: shiny features (default = 8)
    soundManager.useFlashBlock = false; // optionally, enable when you're ready to dive in
    soundManager.flash9Options = {
        isMovieStar:null, // "MovieStar" MPEG4 audio mode. Null (default) = auto detect MP4, AAC etc. based on URL. true = force on, ignore URL
        usePeakData:true, // enable left/right channel peak (level) data
        useWaveformData:true, // enable sound spectrum (raw waveform data) - WARNING: May set CPUs on fire.
        useEQData:true, // enable sound EQ (frequency spectrum data) - WARNING: Also CPU-intensive.
        onbufferchange:null, // callback for "isBuffering" property change
        ondataerror:null      // callback for waveform/eq data access error (flash playing audio in other tabs/domains)
    }
    var sound;
    //The song position
    this.position = -1;
    //The current volume
    this.volume = 3;
    //Status: 0:pause, 1:play
    this.status = 1;
    this.timeout = 30;

    this.nextSong = function () {
        if (this.position + 1 == this.playList.length)
            this.position = 0;
        else
            this.position++;
        //So, the reference to this class is not lost
        var me = this;
        $(".title").fadeOut(200,
            function () {
                $(this).text(me.playList[me.position]);
            }).fadeIn();
    }
    this.play = function (audio) {
        canvas = document.getElementById("example");
        canvas.height = 160;

        canvas.width = 640;
        ctx = canvas.getContext('2d');
        ctx.beginPath();
        for (var i = 0; i < 256; i++) {
            ctx.lineTo(i * 10, i * Math.cos(i) + 50);
        }
        ctx.stroke();
        this.status = 0;
        $("#play").css("backgroundImage", "url('../images/pause.jpg')");
        if (typeof(sound) != 'undefined') {
            sound.destruct();
        }
        sound = soundManager.createSound({
            id:'mySound', // required
            url:audio.url, // required
            autoPlay:true,
            whileplaying:function () {
                $(".position").text(Math.round(sound.position / 1000 / 60) + '/' + Math.round(sound.duration / 1000 / 60));
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                ctx.beginPath();
                ctx.moveTo(0, 128);
                ctx.lineTo(640, 128);
                ctx.lineWidth = (this.peakData.left + this.peakData.right) / 2 * 20;
                ctx.strokeStyle = "rgb(" + Math.round((this.peakData.left + this.peakData.right) / 2 * 255) + ",0,0)";
                ctx.stroke();
                ctx.closePath();

                ctx.beginPath();
                ctx.moveTo(0, 128);
                ctx.lineWidth = 3;
                ctx.strokeStyle = "#000000";
                for (var i = 0; i < 256; i++) {
                    ctx.lineTo(i * 2.5, (-(this.eqData.right[i]+this.eqData.left[i]) - 1) * 32 + 160);
                }
                ctx.stroke();
                ctx.closePath();
            }
        });

        this.setTitle(audio.artist + ' - ' + audio.title);
    }
    //Jump to he previous or last song if it is in the first position
    this.prevSong = function () {
        if (this.position - 1 < 0)
            this.position = this.playList.length - 1;
        else
            this.position--;

        var me = this;
        $(".title").fadeOut(200,
            function () {
                $(this).text(me.playList[me.position]);
            }).fadeIn();
    }
    this.setTitle = function (text) {
        var me = this;
        $(".title").fadeOut(200,
            function () {
                $(this).text(text);
            }).fadeIn();
    }
    //Increase the volume in one point
    this.volumeInc = function () {
        if (this.volume + 1 <= 3) {
            this.volume++;
            sound.setVolume(33 * this.volume);
            var me = this;
            $("#volume").fadeOut(200,
                function () {
                    $(this).css("backgroundImage", "url('../images/vol" + me.volume + ".jpg')");
                }).fadeIn(200);
        }
    }
    //Decrease the volume in one point
    this.volumeDec = function () {
        if (this.volume - 1 > 0) {
            this.volume--;
            sound.setVolume(33 * this.volume);
            var me = this;
            $("#volume").fadeOut(200,
                function () {
                    $(this).css("backgroundImage", "url('../images/vol" + me.volume + ".jpg')");
                }).fadeIn(200);
        }
    }
    //Toggle play & pause
    this.playPause = function () {
        if (typeof(sound) != 'undefined') {
            this.status = !this.status;
            var me = this;
            $("#play").fadeOut(200,
                function () {
                    if (me.status == 0) {
                        sound.resume();
                        $(this).css("backgroundImage", "url('../images/pause.jpg')");
                    }

                    else {
                        $(this).css("backgroundImage", "url('../images/play.jpg')");
                        sound.pause();
                    }
                }).fadeIn(200);
        }
    }
});

//Captures the key press events
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