/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 23.09.12
 * Time: 21:04
 */
WEB_SOCKET_SWF_LOCATION = "ws/WebSocketMain.swf";

var mbApp = angular.module('mbApp', [], function ($locationProvider, $routeProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.
        when('/', {templateUrl:'/partials/main.html', controller:HomeCtrl}).
        when('/search/:query', {templateUrl:'/partials/search-result.html', controller:SearchResultCtrl}).
        otherwise({redirectTo:'/'});
});

mbApp.factory('player', function (socket, audio, $rootScope) {
    var player,
        playlists = [],
        paused = false,
        current = {
            playlist:0,
            track:0
        };

    player = {
        playlists:playlists,

        current:current,

        playing:false,

        play:function (track, album) {

        },

        pause:function () {

        },

        reset:function () {

        },

        next:function () {

        },

        previous:function () {

        }
    };

    playlists.add = function (playlist) {
        if (playlists.indexOf(playlist) != -1) return;
        playlists.push(playlist);
    };

    playlists.remove = function (playlist) {
        var index = playlists.indexOf(playlist);
        if (index == current.playlist) player.reset();
        playlists.splice(index, 1);
    };

    audio.addEventListener('ended', function () {
        $rootScope.$apply(player.next);
    }, false);

    return player;
});

mbApp.factory('audio', function ($document) {
    var audio = $document[0].createElement('audio');
    return audio;
});

mbApp.factory('socket', function ($rootScope, $location) {
    var socket = new WebSocket('ws://' + document.location.host + '/musicbox');
    var callbacks = new Array();

    socket.onmessage = function (event) {
        var data = JSON.parse(event.data);
        console.log(data.action);
        $rootScope.$apply(function () {
            callbacks[data.action](data);
        });
    };

    socket.onopen = function (e) {
        console.log('* Connected!');
        socket.send(JSON.stringify({action:window.localStorage.token ? "LOGIN" : "LOGINBYCODE",
            message:window.localStorage.token || ($location.search().code || "")}));
    };

    socket.onclose = function (e) {
        console.log('* Disconnected');
    };

    socket.onerror = function (e) {
        console.log('* Unexpected error');
    };

    return {
        on:function (eventName, callback) {
            callbacks[eventName] = callback;
        },

        send:function (data) {
            socket.send(JSON.stringify(data));
        },
        state:socket.readyState
    };
});

function MainCtrl($scope, $location, socket) {
    socket.on("MESSAGE", function (data) {
        $scope.msg = data.message;
    });
    socket.on("REDIRECTTOVK", function (data) {
        window.localStorage.token = "";
        location.replace('https://oauth.vk.com/authorize?client_id=' + data.message + '&redirect_uri=http://' + document.domain + '/&scope=audio,offline&response_type=code');
    });

    socket.on("TOKEN", function (data) {
        window.localStorage.token = data.message;
        socket.send({action:"SEARCH", message:""});
    });

    socket.on("LOGINSUCCESS", function () {
        socket.send({action:"SEARCH", message:""});
    });
}

function HomeCtrl($scope, $location, socket) {
    socket.on("SEARCHRESULT", function (data) {
        $scope.topArtists = data.artists;
    });

    $scope.search = function (query) {
        $location.path("/search/" + query);
    }
}

function PlayListCtrl($scope, player, socket) {
    $scope.player = player;
}

function SearchResultCtrl($scope, player, socket, $routeParams) {
    $scope.player = player;
    $scope.playlist;
    var query = $routeParams.query;

    socket.send({action:"SEARCH", message:query });
    socket.on("SEARCHRESULT", function (data) {
        $scope.playlist = {name:query, songs:data.songs};
    });
}