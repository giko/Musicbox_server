/***************************/
//@Author: Adrian "yEnS" Mato Gondelle & Ivan Guardado Castro
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!
/***************************/

//This is the class that interact with the interface
var player = new (function () {
    soundManager.url = '/bootstrap/swf';
    soundManager.flashVersion = 9; // optional: shiny features (default = 8)
    soundManager.useFlashBlock = false; // optionally, enable when you're ready to dive in
    var sound;
    //The song position
    this.position = -1;
    //The current volume
    this.volume = 3;
    //Status: 0:pause, 1:play
    this.status = 1;

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
        this.status = 0;
        $("#play").css("backgroundImage", "url('../images/pause.jpg')");
        if (typeof(sound) != 'undefined') {
            sound.destruct();
        }
        sound = soundManager.createSound({
            id:'mySound', // required
            url:audio.url, // required
            autoPlay:true
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