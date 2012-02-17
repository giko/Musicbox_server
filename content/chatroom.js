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
    var defaultUsername = (window.localStorage && window.localStorage.username) || 'yourname';
    var username = prompt('Choose a username', defaultUsername);
    if (username) {
        if (window.localStorage) { // store in browser localStorage, so we remember next next
            window.localStorage.username = username;
        }
        send({action:'LOGIN', loginUsername:username});
        document.getElementById('searchfield').focus();
    } else {
        ws.close();
    }
}


function onMessage(incoming) {
    switch (incoming.action) {
        case 'SEARCHRESULT':
		for (key in incoming.result.artist){
			var li = $('<li>',{class:'span3'});
			var div = $('<div>',{class:'thumbnail'});
			div.append($('<img>',{src:incoming.result.artist[key].image}));
			div.append($('<h5>',{text:incoming.result.artist[key].title}));
			li.append(div);
			$('#result').append(li);
		}
			send({action:'GETSONGBYID', message:incoming.result.song[0].identifier});
            break;
         case 'JOIN':
            logText("* User '" + incoming.username + "' joined.");
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
    ws.onopen = function(e) {
        logText('* Connected!');
        login();
    };
    ws.onclose = function(e) {
        logText('* Disconnected');
    };
    ws.onerror = function(e) {
        logText('* Unexpected error');
    };
    ws.onmessage = function(e) {
        onMessage(JSON.parse(e.data));
    };

    // wire up text input event
    var entry = document.getElementById('searchfield');
    entry.onkeypress = function(e) {
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
