angular.module('tennisApp')
.directive('Match',Match); // defining Directive Controller

Match.$inject=['BetService']; // injecting Cobntroller dependencies

// controller function for directve
function Match(BetService){
	return {
        restrict: 'AE',
        templateUrl: "../../templates/match.html",
        replace: true,
        scope: false,
        link: function (scope, element, attrs, ctrl) {
            scope.theMatch = [];
            scope.theMatchId = "";
            scope.$watch(attrs.match, function (newVal, oldVal) {
                scope.theMatch = newVal;
            });

            scope.$watch(attrs.key, function (newVal, oldVal) {
                scope.theMatchId = newVal;
            });

            scope.betOn = function (idMatch, player) {
                BetsService.betOnWinner(idMatch, player);
            };

            scope.cssStyleForSet = function (player, idSet) {
                var cssClass = "label label-default";
                angular.forEach(scope.theMatch.players, function (p, key) {
                    if (angular.equals(p.name, player.name) == false)
                        if (idSet == 1 && player.set1 > p.set1) {
                            cssClass = 'label label-success';
                        } else if (idSet == 2 && player.set2 > p.set2) {
                            cssClass = 'label label-success';
                        } else if (idSet == 3 && player.set3 > p.set3) {
                            cssClass = 'label label-success';
                        }
                }, cssClass);
                return cssClass;
            };
        }
    };
};// end of Match function