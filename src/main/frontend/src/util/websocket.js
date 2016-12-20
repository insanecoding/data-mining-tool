import SockJS from "sockjs-client";
import Stomp from "stompjs";

export class WebsocketClient {
    constructor() {
        this.stompClient = null;
    }

    connect = (callback) => {
        // create socket with the following client identifier
        const socket = new SockJS('/web-app-client');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, () => {
            // connect to message broker and subscribe to it
            // callback action will be performed when each message is received
            this.stompClient.subscribe('/topic/broker', callback);
        });
    };

    disconnect = () => {
        if (this.stompClient != null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    };

    send = (obj) => {
        // send json object to the application
        this.stompClient.send("/socket/progress", {}, obj);
    };
}

