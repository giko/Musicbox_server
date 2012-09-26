/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 23.09.12
 * Time: 21:04
 */

var mbApp = angular.module('mbApp', []);

mbApp.factory('socket', function ($rootScope) {
    var socket = new WebSocket('ws://' + document.location.host + '/musicbox');
    var callbacks = new Array();

    socket.onmessage = function (event) {
        var data = JSON.parse(event.data);
        $rootScope.$apply(function () {
            callbacks[data.action](data);
        });
    };

    socket.onopen = function (e) {
        console.log('* Connected!');
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
        }
    };
});

function PlayListCtrl(socket, $scope) {
    socket.on("MESSAGE", function (data) {
        $scope.msg = data.message;
    });

    $scope.playlists = [
        {name:"Nexus S",
            songs:[
                {name:"lol"},
                {name:"bad"}
            ]},
        {name:"Motorola XOOM™ with Wi-Fi",
            songs:[
                {name:"lol"},
                {name:"bad"}
            ]},
        {name:"MOTOROLA XOOM™",
            songs:[
                {name:"lol"},
                {name:"bad"}
            ]}
    ];

    $scope.AddToPlaylists = function (name) {
        $scope.playlists.push({name:name, songs:[]})
    }

    $scope.DeleteSong = function (playlist, song) {
        var real_playlist = $scope.playlists[$scope.playlists.indexOf(playlist)];
        real_playlist.songs.splice(real_playlist.songs.indexOf(song), 1);
    }

    $scope.AddSong = function (song, playlist) {
        $scope.playlists[$scope.playlists.indexOf(playlist)].songs.push({name:song});
    }
}