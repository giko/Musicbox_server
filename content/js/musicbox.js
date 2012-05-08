WEB_SOCKET_SWF_LOCATION = "bootstrap/js/WebSocketMain.swf";


var musicboxclient = new function () {
    this.init = function () {
        $('#artist-info').hide();
        visualization.init();
        musicboxclient.connect();


        $("#slider").slider({
            range:"min",
            value:37,
            min:1,
            max:1000,
            slide:function (event, ui) {
                sound = player.getSound();
                sound.setPosition(ui.value / 1000 * sound.duration);
            }
        });

        $('#brand').click(function () {
            send({action:'SEARCH', message:''});
        });

        // wire up text input event
        var searchentry = document.getElementById('searchfield');
        searchentry.onkeypress = function (e) {
            if (e.keyCode == 13) { // enter key pressed
                send({action:'SEARCH', message:searchentry.value});
                searchentry.value = '';
            }
        };

        // Chat text form
        var chatentry = document.getElementById('chat-text');
        chatentry.onkeypress = function (e) {
            if (e.keyCode == 13) { // enter key pressed
                var text = chatentry.value;
                if (text) {
                    send({action:'CHATMESSAGE', message:text});
                }
                chatentry.value = '';
            }
        };

        $('#artist-info-find-similar').click(function () {
            send({action:'SEARCHSIMILARARTISTSBYNAME', message:$('#artist-info-name').text()});
        });
    }

    // Socket reference.
    var ws;

    function login() {
        if (gup('c')) {
            window.localStorage.token = '';
        }

        if (window.localStorage.token) {
            send({action:'LOGIN', message:window.localStorage.token});
        } else {
            send({action:'LOGINBYCODE', message:gup('code')});
        }
    }

    function gup(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null)
            return "";
        else
            return decodeURIComponent(results[1]);
    }

    function secondsToTime(secs) {
        var hours = Math.floor(secs / (60 * 60));

        var divisor_for_minutes = secs % (60 * 60);
        var minutes = Math.floor(divisor_for_minutes / 60);

        var divisor_for_seconds = divisor_for_minutes % 60;
        var seconds = Math.ceil(divisor_for_seconds);

        var obj = {
            "h":hours,
            "m":minutes,
            "s":seconds
        };
        return obj;
    }

    function onMessage(incoming) {
        switch (incoming.action) {
            case 'MESSAGE':
                console.log(incoming.message);
                break;
            case 'SEARCHRESULT':
                $("#content").fadeOut(200,
                    function () {

                        $('#tracks').empty();
                        $('#artists').empty();
                        $('#tags').empty();

                        if (typeof(incoming.artists) != 'undefined' && incoming.artists.length > 1) {
                            $('#artistsunit').show();
                        } else {
                            $('#artistsunit').hide();
                        }
                        if (typeof(incoming.songs) != 'undefined') {
                            $('#tracksunit').show();
                        } else {
                            $('#tracksunit').hide();
                        }
                        if (typeof(incoming.tags) != 'undefined') {
                            $('#tagsunit').show();
                        } else {
                            $('#tagsunit').hide();
                        }

                        var li;
                        var div;
                        var btn;
                        for (key in incoming.tags) {
                            li = $('<li>', {class:'span4', id:incoming.tags[key].name});
                            li.click(function () {
                                send({action:'SEARCHBYTAG', message:this.getAttribute("id")});
                            });
                            div = $('<div>', {class:'thumbnail'});
                            div.append($('<h5>', {text:incoming.tags[key].name}));
                            li.append(div);
                            $('#tags').append(li);
                        }

                        if (incoming.artists.length == 1 && typeof(incoming.artists[0].bio) != 'undefined') {
                            $('#artist-info').show();
                            $('#artist-info-name').text(incoming.artists[0].name);
                            $('#artist-info-img').attr('src', incoming.artists[0].image[3]['#text']);
                            $('#artist-info-text').html(incoming.artists[0].bio.summary);
                        }
                        else {
                            $('#artist-info').hide();
                            for (key in incoming.artists) {
                                if (incoming.artists[key].name != "") {
                                    if (incoming.artists[key].mbid) {
                                        li = $('<li>', {class:'span4', id:incoming.artists[key].mbid});
                                        li.click(function () {
                                            send({action:'GETTOPSONGSBYARTISTID', message:this.getAttribute("id")});
                                        });
                                    }
                                    else {
                                        li = $('<li>', {class:'span4', id:incoming.artists[key].name});
                                        li.click(function () {
                                            send({action:'GETTOPSONGSBYARTISTNAME', message:this.getAttribute("id")});
                                        });
                                    }
                                    div = $('<div>', {class:'thumbnail'});
                                    if (typeof(incoming.artists[key].image) != "undefined") {
                                        div.append($('<img>', {src:incoming.artists[key].image[3]['#text']}));
                                    }
                                    div.append($('<h5>', {text:incoming.artists[key].name}));
                                    li.append(div);
                                    $('#artists').append(li);
                                }
                            }
                        }

                        for (key in incoming.songs) {
                            var trackname = typeof(incoming.songs[key].artist) == 'undefined' ? incoming.songs[key].name : (incoming.songs[key].artist.name) + ' ' + incoming.songs[key].name;

                            li = $('<tr>', {id:trackname});
                            btn = $('<button>', {class:"btn", href:"#"});
                            btn.click(function () {
                                send({action:'ADDTOLIBRARY', message:this.parentElement.parentElement.getAttribute("id")});
                            });
                            btn.append('<i class="icon-plus"></i> add');
                            actionrow = $('<td>');
                            actionrow.append(btn);

                            artistlink = $('<a>', {text:incoming.songs[key].artist.name, id:incoming.songs[key].artist.name});
                            artistlink.click(function () {
                                send({action:'SEARCH', message:this.getAttribute("id")});
                            });

                            songname = $('<td>', {text:incoming.songs[key].name});
                            songname.click(function () {
                                send({action:'GETAUDIOBYTRACK', message:this.parentElement.getAttribute("id")});
                            });

                            li.append($('<td>', {text:parseInt(key) + 1}));
                            li.append($('<td>').append(artistlink));
                            li.append(songname);
                            li.append(actionrow);
                            $('#tracks').append(li);
                        }
                    }).fadeIn();
                //send({action:'GETURLBYTRACK', message:incoming.artists[0].name});
                break;
            case 'REDIRECTTOVK':
                window.localStorage.token = '';
                location.replace('http://api.vk.com/oauth/authorize?client_id=' + incoming.message + '&redirect_uri=' + document.domain + '&scope=audio,offline&display=page&response_type = code');
                break;
            case 'EXECUTEREQUEST':
                $.ajax({
                    type:"GET",
                    dataType:"jsonp",
                    url:incoming.request.url,
                    data:incoming.request.data,
                    success:function (msg) {
                        send({action:'EXECUTEREQUESTRESULT', message:JSON.stringify({action:incoming.request.action, result:JSON.stringify({query:incoming.message, data:msg})})});
                    }
                });
                break;
            case 'SONGURL':
                player.play(incoming.message);
                break;
            case 'AUDIO':
                player.play(incoming.audio);
                break;
            case 'JOIN':
                console.log("* User '" + incoming.username + "' joined.");
                break;
            case 'LOGINSUCCESS':
                if (gup('track')) {
                    send({action:'GETAUDIOBYTRACK', message:gup('track')});
                }
                if (gup('search')) {
                    send({action:'SEARCH', message:gup('search')});
                } else
                    send({action:'SEARCH', message:''});
                break;
            case 'TOKEN':
                window.localStorage.token = incoming.message;
                send({action:'LOGIN', message:window.localStorage.token});
                break;
        }
    }

    // Connect to socket and setup events.
    this.connect = function () {
        // clear out any cached content
        document.getElementById('chatlog').value = '';

        // connect to socket
        console.log('* Connecting...');
        ws = new WebSocket('ws://' + document.location.host + '/musicbox');
        ws.onopen = function (e) {
            console.log('* Connected!');
            login();
        };

        ws.onclose = function (e) {
            console.log('* Disconnected');
            setTimeout(function () {
                musicboxclient.connect();
            }, 3000);
        };

        ws.onerror = function (e) {
            console.log('* Unexpected error');
            setTimeout(function () {
                musicboxclient.connect();
            }, 3000);
        };

        ws.onmessage = function (e) {
            onMessage(JSON.parse(e.data));
        };
    }

    // Send message to server over socket.
    function send(outgoing) {
        ws.send(JSON.stringify(outgoing));
    }
}

// Connect on load.
window.onload = musicboxclient.init;