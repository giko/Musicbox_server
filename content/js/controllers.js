/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 23.09.12
 * Time: 21:04
 */

function MainCtrl($scope, $location, $http, socket) {
    var messages = [];
    $scope.messages = messages;

/*
    socket.on("MESSAGE", function (data) {
        $scope.msg = data.message;
    });
*/

    socket.on("CRITICALERROR", function (data) {
        $scope.error = true;
        $scope.error_msg = data.message;
    });

    socket.on("MESSAGE", function (data) {
        messages.push(data.message);
    });


    socket.on("REDIRECTTOVK", function (data) {
        window.localStorage.token = "";
        location.replace('https://oauth.vk.com/authorize?client_id=' + data.vkappid + '&scope=audio,offline&redirect_uri=http://' + document.domain + '/&response_type=code&display=page');
    });

    socket.on("TOKEN", function (data) {
        window.localStorage.token = data.token;
        socket.send({action: "LOGIN", message: data.token});
    });

    socket.on("LOGINSUCCESS", function () {
        socket.send({action: "SEARCH", message: ""});
    });

    socket.on("EXECUTEREQUEST", function (data) {
        $http.jsonp(data.request.url, {params: data.request.data}).success(function (msg, status) {
            socket.send({action: 'EXECUTEREQUESTRESULT', message: JSON.stringify({action: data.request.action, result: JSON.stringify({query: data.message, data: msg})})})
        }).error(function () {
                console.log("EXECUTEREQUEST FAILED!");
            });
    })

    $scope.searchArtist = function (artist) {
        if (angular.isDefined(artist.mbid) && artist.mbid != "") {
            $location.path("/artist/id/" + artist.mbid);
        } else {
            $location.path("/artist/" + artist.name);
        }
    }

    $scope.sendMessage = function(text){
        socket.send({action:"CHATMESSAGE", message:text});
    }

    $scope.left_bar = "/partials/left-bar.html";
}

function ArtistCtrl($scope, $location, $routeParams, socket, player, ArtistCache) {
    $scope.player = player;
    $scope.loading = true;
    var cache;

    $scope.findSimilar = function (artist) {
        $location.path("/artist/" + artist.name + "/similar");
    }

    $scope.addToFavorites = function(artist){
        socket.send({action:"ADDARTISTTOFAVORITES", message: artist})
    }

    if (angular.isDefined($routeParams.query)) {
        cache = ArtistCache.get($routeParams.query);
        if (angular.isUndefined(cache)) {
            socket.send({action: "GETTOPSONGSBYARTISTNAME", message: $routeParams.query});
        }
    } else {
        cache = ArtistCache.get($routeParams.id);
        if (angular.isUndefined(cache)) {
            socket.send({action: "GETTOPSONGSBYARTISTID", message: $routeParams.id});
        }
    }

    if (angular.isUndefined(cache)) {
        socket.on("ARTISTSONGS", function (data) {
            ArtistCache.put($routeParams.query || $routeParams.id, data);
            $scope.loading = false;
            $scope.artist = data.artist;
            $scope.playlist = {name: data.artist.name, songs: data.songs};
        });
    } else {
        $scope.loading = false;
        $scope.artist = cache.artist;
        $scope.playlist = {name: cache.artist.name, songs: cache.songs};
    }
}

function UserCtrl($scope, $location, $routeParams, socket, player, ArtistCache) {
    $scope.loading = true;

    if (angular.isDefined($routeParams.id)) {
            socket.send({action: "GETUSER", message: $routeParams.id});
    }

    socket.on("USER", function (data) {
        $scope.loading = false;
        $scope.user = data.user;
    });
}

function HomeCtrl($scope, $location, socket, GlobalCache) {
    var cache = GlobalCache.get("TopArtists");
    if (angular.isDefined(cache)) {
        $scope.topArtists = cache.artists;
    } else {
        socket.on("SEARCHRESULT", function (data) {
            GlobalCache.put("TopArtists", data);
            $scope.topArtists = data.artists;
        });
    }
}

function PlayListCtrl($scope, $location, player, socket) {
    $scope.player = player;
    $scope.isCurrent = function (playlist, song) {
        return player.current.playlist == playlist && player.current.song == song;
    };

    $scope.search = function (query) {
        if (angular.isUndefined(query)) return;

        $location.path("/search/" + query);
    }

    $scope.searchuser = function (query) {
        if (angular.isUndefined(query)) return;

        $location.path("/user/" + query);
    }
}

function SearchResultCtrl($scope, player, socket, $routeParams, SearchCache) {
    $scope.player = player;
    $scope.playlist;
    $scope.loading = true;


    var query = $routeParams.query;

    if (angular.isDefined(query)) {
        var cache = SearchCache.get(query);

        if (angular.isDefined(cache)) {
            $scope.loading = false;
            $scope.artists = cache.artists;
            $scope.playlist = {name: query, songs: cache.songs};
        } else {
            socket.send({action: "SEARCH", message: query });
            socket.on("SEARCHRESULT", function (data) {
                SearchCache.put(query, data);
                $scope.loading = false;
                $scope.artists = data.artists;
                $scope.playlist = {name: query, songs: data.songs};
            });
        }
    } else {
        socket.send({action: "SEARCHSIMILARARTISTSBYNAME", message: $routeParams.id});
        socket.on("SEARCHRESULT", function (data) {
            $scope.loading = false;
            $scope.artists = data.artists;
        });
    }
}