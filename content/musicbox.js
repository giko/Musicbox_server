// Socket reference.
var ws;

var audioElement = document.createElement('audio');
var textArea = document.getElementById('chatlog');

// Log text to main window.
function logText(msg) {
    var element = document.createElement('p');
    element.appendChild(document.createTextNode(msg));
    textArea.appendChild(element);
}

function login() {
    if (gup('s')) {
        window.localStorage.vktoken = '';
    }

    if (window.localStorage.vktoken) {
        send({action:'LOGINBYTOKEN', message:window.localStorage.vktoken});
    } else {
        if (gup('code')) {
            send({action:'LOGINBYCODE', message:gup('code')});
        } else {
            location.replace('http://api.vk.com/oauth/authorize?client_id=2810768&redirect_uri=' + document.URL + '&scope=audio,offline&display=page');
        }
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
        return results[1];
}

function onMessage(incoming) {
    switch (incoming.action) {
        case 'MESSAGE':
            logText(incoming.message);
            break
        case 'SEARCHRESULT':
            for (key in incoming.artists) {
                var li = $('<li>', {class:'span3'});
                var div = $('<div>', {class:'thumbnail'});
                div.append($('<img>', {src:incoming.artists[key].image[3]['#text']}));
                div.append($('<h5>', {text:incoming.artists[key].name}));
                li.append(div);
                $('#result').append(li);
            }
            send({action:'GETURLBYTRACK', message:incoming.artists[key].name[0]});
            break;
        case 'SONGURL':
            audioElement.setAttribute('src', incoming.message);
            audioElement.play();
            break;
        case 'JOIN':
            logText("* User '" + incoming.username + "' joined.");
            break;
        case 'TOKEN':
            window.localStorage.vktoken = incoming.message;
            send({action:'LOGINBYTOKEN', message:window.localStorage.vktoken});
            break;
        case 'SONGS':
            logText("Playing: " + incoming.songs[0].title);
            audioElement.setAttribute('src', incoming.songs[0].location);
            audioElement.play();
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
            var text = entry.value;
            if (text) {
                send({action:'SEARCH', message:text});
            }
            entry.value = '';
        }
    };
}

// Send message to server over socket.
function send(outgoing) {
    ws.send(JSON.stringify(outgoing));
}

// Connect on load.
window.onload = connect;
