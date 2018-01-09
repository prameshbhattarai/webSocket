angular.module('tennisApp')
.controller('Message',Message); // defining Directive Controller

Message.$inject=['BetService']; // injecting Cobntroller dependencies

// controller function for directve
function Message(BetService){
	return {
        restrict: 'AE',
        templateUrl: "../../templates/msg.html",
        replace: true,
        link: function (scope, element, attrs, ctrl) {
            scope.typeAlert = "";
            scope.finalMessage = "";
            scope.theWinner = "";

            scope.$watch(attrs.winner, function (newVal, oldVal) {
                if (angular.isUndefined(newVal) == false) {
                    if (angular.equals(newVal, "") == false) {
                        scope.theWinner = newVal;
                    }
                }
            });

            scope.$watch(attrs.message, function (newVal, oldVal) {
                if (angular.isUndefined(newVal) == false) {
                    scope.finalMessage = scope.theWinner.concat(" WINS the match. ");

                    if (angular.equals(newVal.winner, "") == false) {
                        if (angular.equals(newVal.result, "OK")) {
                            scope.finalMessage = scope.finalMessage.concat("CONGRATS !! You won your bet !");
                            scope.typeAlert = "success";
                        } else if (angular.equals(newVal.result, "KO")) {
                            scope.finalMessage = scope.finalMessage.concat("SORRY, you've lost your bet, try again :) ");
                            scope.typeAlert = "danger";
                        }
                    }
                } else {
                    //scope.finalMessage = MatchesService.whoIsTheWinner(scope.theMatchId) + scope.finalMessage;
                    scope.finalMessage = ("Next time, bet on a player to win :)");
                    scope.typeAlert = "info";
                }
            });
        }
    };
};// end of Message function