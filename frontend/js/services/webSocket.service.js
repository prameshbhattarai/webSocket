angular.module('tennisApp')
.factory('WebSocketService',WebSocketService);

WebSocketService.$inject=[];

function WebSocketService(){

    var wsUrl = "ws://localhost:8080/WebSocket-web/matches/";

    var service = {
        connect : connect,
        sendBetMatchWinner : sendBetMatchWinner,
        disconnect : disconnect,
        status : status,
        statusAsText : statusAsText,
        subscribe : subscribe

    };
    service.ws = new Object();
    return service;

    function connect(matchId) {
        if (service.ws[matchId] && service.ws[matchId].readyState == WebSocket.OPEN) {
            return;
        }

        var websocket = new WebSocket(wsUrl + matchId);

        websocket.onopen = function () {
            service.callback(matchId, "CONNECTED");
        };

        websocket.onerror = function () {
            service.callback(matchId, "Failed to open a connection");
        };

        websocket.onclose = function () {
            service.callback(matchId, "DISCONNECTED");
        };

        websocket.onmessage = function (message) {
            service.callback(matchId, message.data);
        };

        service.ws[matchId] = websocket;
    };

    function sendBetMatchWinner(matchId, player) {
        var jsonObj = {"type": "betMatchWinner", "name": player};
        service.ws[matchId].send(JSON.stringify(jsonObj));
    };

    function disconnect(matchId) {
        service.ws[matchId].close();
    };

    function status(matchId) {
        if (service.ws == null || angular.isUndefined(service.ws[matchId])) {
            return WebSocket.CLOSED;
        }
        return service.ws[matchId].readyState;
    };

    function statusAsText(matchId) {
        var readyState = service.status(matchId);
        if (readyState == WebSocket.CONNECTING) {
            return "CONNECTING";
        } else if (readyState == WebSocket.OPEN) {
            return "OPEN";
        } else if (readyState == WebSocket.CLOSING) {
            return "CLOSING";
        } else if (readyState == WebSocket.CLOSED) {
            return "CLOSED";
        } else {
            return "UNKNOW";
        }
    };

     // handle callback
    function subscribe(callback) {
        console.log(" subscribe >>>> "); 
        service.callback = callback;
    };

}
