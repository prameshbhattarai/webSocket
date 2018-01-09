angular.module('tennisApp')
.factory('TournamentService', TournamentService);

TournamentService.$inject=['$http'];

function TournamentService($http){

	var service = {
		fetchScore : fetchScore
	};

	return service;

	function fetchScore(){
        var urlRest = 'http://localhost:8080/WebSocket-web/rest/tournament/lives';
		// $http returns a promise, which has a then function, which
        // also returns a promise
        var promise = $http.get(urlRest).then(function (response) {
            // The then function here is an opportunity to modify the
            // response
            console.log(response);
            // The return value gets picked up by the then in the
            // controller.
            return response.data;
        });
        // Return the promise to the controller
        return promise;
	};

}
