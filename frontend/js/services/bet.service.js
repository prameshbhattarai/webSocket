angular.module('tennisApp')
.factory('BetService', BetService);

BetService.$inject=['WebSocketService'];

function BetService(WebSocketService){

	var service = {
		betOnWinner : betOnWinner
	};

	return service;

	function betOnWinner(matchId, player) {
		WebSocketService.sendBetMatchWinner(matchId, player);
	}
	
}
