/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 05.10.12
 * Time: 11:23
 */

WEB_SOCKET_SWF_LOCATION = "ws/WebSocketMain.swf";

var mbApp = angular.module('mbApp', ['ui'], function ($locationProvider, $routeProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.
        when('/', {templateUrl:'/partials/main.html', controller:HomeCtrl}).
        when('/search/:query', {templateUrl:'/partials/search-result.html', controller:SearchResultCtrl}).
        when('/artist/:query', {templateUrl:'/partials/artist.html', controller:ArtistCtrl}).
        when('/artist/id/:id', {templateUrl:'/partials/artist.html', controller:ArtistCtrl}).
        otherwise({redirectTo:'/'});
});

mbApp.factory('GlobalCache', function ($cacheFactory) {
    return $cacheFactory('GlobalCache');
});

mbApp.factory('SearchCache', function ($cacheFactory) {
    return $cacheFactory('SearchCache');
});

mbApp.factory('AudioCache', function ($cacheFactory) {
    return $cacheFactory('AudioCache');
});

mbApp.factory('ArtistCache', function ($cacheFactory) {
    return $cacheFactory('ArtistCache');
});

mbApp.directive('contenteditable', function () {
    return {
        restrict:'A', // only activate on element attribute
        require:'?ngModel', // get a hold of NgModelController
        link:function (scope, element, attrs, ngModel) {
            if (!ngModel) return; // do nothing if no ng-model

            // Specify how UI should be updated
            ngModel.$render = function () {
                element.html(ngModel.$viewValue || '');
            };

            // Listen for change events to enable binding
            element.bind('blur keyup change', function () {
                scope.$apply(read);
            });

            // Write data to the model
            function read() {
                ngModel.$setViewValue(element.html());
            }
        }
    };
});

mbApp.factory('player', function (socket, audio, AudioCache, $rootScope) {
    var player,
        playlists = [],
        paused = false,
        current = {
            waiting:false,
            playlist:null,
            track:null
        };

    player = {
        playlists:playlists,

        current:current,

        playing:false,

        play:function (playlist, song) {
            if (current.waiting) return;

            if (angular.isDefined(playlist) && angular.isDefined(song)) {
                current.waiting = true;
                current.playlist = playlist;
                current.song = song;

                player.requestSong(song);
            } else {
                if (current.song != null && current.playlist != null) {
                    audio.play();
                    player.playing = true;
                }
            }
        },

        playURL:function (url) {
            current.waiting = false;
            player.playing = true;
            audio.src = url;
            audio.play();
        },

        pause:function () {
            player.playing = false;
            audio.pause();
        },

        reset:function () {
            audio.pause();
            player.playing = false;
            current.waiting = false;
            current.song = null;
            current.playlist = null;
        },

        next:function () {
            if (current.waiting) return;

            var playlists_index = playlists.indexOf(current.playlist);
            if (playlists_index == -1) {
                var song_index = current.playlist.songs.indexOf(current.song);
                if (current.playlist.songs.length > song_index + 1) {
                    player.play(current.playlist, current.playlist.songs[song_index + 1]);
                }
            } else {
                var song_index = playlists[playlists_index].songs.indexOf(current.song);
                if (playlists[playlists_index].songs.length > song_index + 1) {
                    player.play(current.playlist, current.playlist.songs[song_index + 1])
                } else {

                }
            }

        },

        previous:function () {
            if (current.waiting) return;

            var playlists_index = playlists.indexOf(current.playlist);
            if (playlists_index == -1) {
                var song_index = current.playlist.songs.indexOf(current.song);
                if (song_index - 1 >= 0) {
                    player.play(current.playlist, current.playlist.songs[song_index - 1]);
                }
            } else {
                var song_index = playlists[playlists_index].songs.indexOf(current.song);
                if (playlists[playlists_index].songs.length > song_index - 1) {
                    player.play(current.playlist, current.playlist.songs[song_index - 1]);
                } else {

                }
            }

        },

        requestSong:function (song) {
            current.waiting = true;
            var cache = AudioCache.get(song.artist.name + song.name);
            if (angular.isDefined(cache)) {
                player.playURL(cache.url);
            } else {
                socket.send({action:'GETAUDIOBYTRACK', message:song.artist.name + " " + song.name});
            }
        }
    };

    playlists.add = function (playlist) {
        if (playlists.indexOf(playlist) != -1) return;
        playlists.push(playlist);
    };

    playlists.remove = function (playlist) {
        var index = playlists.indexOf(playlist);
        if (current.playlist == playlist) player.reset();
        playlists.splice(index, 1);
    };

    audio.addEventListener('ended', function () {
        $rootScope.$apply(player.next);
    }, false);

    socket.on("AUDIO", function (data) {
        AudioCache.put(current.song.artist.name + current.song.name, data.audio);
        player.playURL(data.audio.url);
    });

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