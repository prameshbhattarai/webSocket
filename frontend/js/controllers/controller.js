(function() {
'use strict';

    angular.module('tennisApp')
    .controller('MatchController', MatchController);

    MatchController.$inject = ['TournamentService', 'MatchService', 'WebSocketService'];

    function MatchController(TournamentService, MatchService, WebSocketService) {
        
        var vm = this;

        // properties
        vm.lives = new Object();
        vm.error = '';
        vm.loaded = false;
        vm.search = ''; // navigation search string
        vm.enableSearchNavigation = false;
        vm.searchedNavigation = []; // holds filter navigation object

        // methods
        vm.connect = connect;
        vm.disconnect = disconnect;
        vm.splitForImage = splitForImage;

        vm.tournamentResources = tournamentResources;
        vm.webSocketResources = webSocketResources;

        // initialize methods
        activate();
        function activate(){
            console.log("controller initialize >>>");
            tournamentResources();
            webSocketResources();
        } // end of activate function

        // function to open connection
        function connect(matchId) {
            WebSocketService.connect(matchId);
        }

        // function to disconnect connection
        function disconnect(matchId) {
            vm.lives[matchId].match = {};
            vm.lives[matchId].winner = "";
            vm.lives[matchId].bet = {};
            WebSocketService.disconnect(matchId);
        }

        // function to render images
        function splitForImage(string, numberOfbets) {
            var array = string.toLowerCase().split(' ');
            return vm.result = array[numberOfbets];
        }

        // fetch from tournament services
        function tournamentResources() {
            TournamentService.fetchScore()
            .then(function(success) {
                setLiveScores(success.data);
            }, function(error) {
                console.log(error);
            });   
        }

        function setLiveScores(datas) {
            angular.forEach(datas, function(value, key) {
                vm.lives[value.key] = new Object();
                vm.lives[value.key].key = value.key;
                vm.lives[value.key].status = 'DISCONNECTED';
                vm.lives[value.key].title = value.title;
                vm.lives[value.key].p1 = value.playerOneName;
                vm.lives[value.key].p2 = value.playerTwoName;
                vm.lives[value.key].p1c = value.p1Country;
                vm.lives[value.key].p2c = value.p2Country;
            });
        }

        // listen to webSocket, Messages sent by peer server are handled here
        function webSocketResources() {
            console.log("listen to webSocketResources >>>> ");
            WebSocketService.subscribe(function (matchId, message) {
                try {
                    var obj = JSON.parse(message);

                    //Match Live message from server
                    if (obj.hasOwnProperty("match")) {
                        vm.lives[matchId].match = obj.match;
                        vm.lives[matchId].winner = "";
                        vm.lives[matchId].key = matchId;

                        if (obj.match.finished) {
                            vm.lives[matchId].winner = MatchService.whoIsTheWinner(obj.match);
                        }
                    }
                    //Bet Message from server
                    else if (obj.hasOwnProperty("winner")) {
                        vm.lives[matchId].bet = obj;
                    }

                } catch (exception) {
                    //Message WebSocket lifcycle
                    vm.lives[matchId].status = message;
                    console.log(message);
                }
            });
        }

    } // end of tournament controller    

})();
