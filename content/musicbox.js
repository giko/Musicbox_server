WEB_SOCKET_SWF_LOCATION = "bootstrap/js/WebSocketMain.swf";

// Socket reference.
var ws;

var audioElement = document.getElementById('audio');
var textArea = document.getElementById('chatlog');

// Log text to main window.
function logText(msg) {
    var element = document.createElement('p');
    element.appendChild(document.createTextNode(msg));
    textArea.appendChild(element);
}

function login() {
    if (gup('s')) {
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
            logText(incoming.message);
            break;
        case 'SEARCHRESULT':
            $('#tracks').empty();
            $('#artists').empty();

            if (typeof(incoming.artists) != 'undefined') {
                $('#artistsunit').show();
            } else {
                $('#artistsunit').hide();
            }
            if (typeof(incoming.songs) != 'undefined') {
                $('#tracksunit').show();
            } else {
                $('#tracksunit').hide();
            }

            var li;
            var div;
            for (key in incoming.artists) {
                if (incoming.artists[key].mbid) {
                    li = $('<li>', {class:'span3', id:incoming.artists[key].mbid});
                    li.click(function () {
                        send({action:'GETTOPSONGSBYARTISTID', message:this.getAttribute("id")});
                    });
                }
                else {
                    li = $('<li>', {class:'span3', id:incoming.artists[key].name});
                    li.click(function () {
                        send({action:'GETTOPSONGSBYARTISTNAME', message:this.getAttribute("id")});
                    });
                }
                div = $('<div>', {class:'thumbnail'});
                div.append($('<img>', {src:incoming.artists[key].image[3]['#text']}));
                div.append($('<h5>', {text:incoming.artists[key].name}));
                li.append(div);
                $('#artists').append(li);
            }
            for (key in incoming.songs) {
                li = $('<li>', {class:'span3', id:incoming.songs[key].artist.name + ' ' + incoming.songs[key].name});
                li.click(function () {
                    send({action:'GETURLBYTRACK', message:this.getAttribute("id")});
                });
                div = $('<div>', {class:'thumbnail'});
                div.append($('<h5>', {text:incoming.songs[key].artist.name + ' - ' + incoming.songs[key].name}));
                li.append(div);
                $('#tracks').append(li);
            }
            //send({action:'GETURLBYTRACK', message:incoming.artists[0].name});
            break;
        case 'REDIRECTTOVK':
            window.localStorage.token = '';
            location.replace('http://api.vk.com/oauth/authorize?client_id=2810768&redirect_uri=' + document.domain + '&scope=audio,offline&display=page');
            break;
        case 'SONGURL':
            audioElement.setAttribute('src', incoming.message);
            audioElement.play();
            break;
        case 'JOIN':
            logText("* User '" + incoming.username + "' joined.");
            break;
        case 'LOGINSUCCESS':
            if (gup('track')) {
                send({action:'GETURLBYTRACK', message:gup('track')});
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
function connect() {
    // clear out any cached content
    document.getElementById('chatlog').value = '';

    // connect to socket
    logText('* Connecting...');
    ws = new WebSocket('ws://' + document.location.host + '/musicbox');
    ws.onopen = function (e) {
        logText('* Connected!');
        login();
    };
    ws.onclose = function (e) {
        logText('* Disconnected');
    };
    ws.onerror = function (e) {
        logText('* Unexpected error');
    };
    ws.onmessage = function (e) {
        onMessage(JSON.parse(e.data));
    };

    // wire up text input event
    var entry = document.getElementById('searchfield');
    entry.onkeypress = function (e) {
        if (e.keyCode == 13) { // enter key pressed
            send({action:'SEARCH', message:entry.value});
            entry.value = '';
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
}

// Send message to server over socket.
function send(outgoing) {
    ws.send(JSON.stringify(outgoing));
}

// Connect on load.
window.onload = connect;