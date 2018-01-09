angular.module('tennisApp')
.directive('Bet',Bet); // defining Directive

Bet.$inject=['BetService']; // injecting Cobntroller dependencies

// controller function for directve
function Bet(BetService){
	return{
        restrict: 'EA',
        templateUrl: "../../templates/bet.html",
        replace: true,
        scope: false,
        link: function (scope, element, attrs, ctrl) {

            scope.betOnWinnerName = "";
            scope.nbBets = 0;
            scope.betOnTheMatch = false;

            scope.$watch(attrs.bet, function (newVal, oldVal) {
                if (angular.isUndefined(newVal) == false && newVal.hasOwnProperty('nbBets')) {
                    scope.nbBets = newVal.nbBets;
                    if (angular.equals(newVal.winner, "") == false) {
                        scope.betOnTheMatch = true;
                        scope.betOnWinnerName = newVal.winner;
                    }
                } else {
                    scope.betOnWinnerName = "";
                    scope.nbBets = 0;
                    scope.betOnTheMatch = false;
                }
            });
        }
    };
};// end of Bet function