angular.module('tennisApp')
.factory('MatchService', MatchService);

MatchService.$inject=['WebSocketService'];

function MatchService(WebSocketService){

	var service = {
		whoIsTheWinner : whoIsTheWinner
	};

	return service;

	function whoIsTheWinner(match) {
		if (angular.isUndefined(match.players)) {
            return null;
        }
        if (parseInt(match.players[0].sets) > parseInt(match.players[1].sets)) {
            return match.players[0].name;
        } else {
            return match.players[1].name;
        }
	}

}
