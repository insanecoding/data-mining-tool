import SockJS from "sockjs-client";
import Stomp from "stompjs";

export class WebsocketClient {
    constructor() {
        this.stompClient = null;
    }

    connect = () => {
        // create socket with the following client identifier
        const socket = new SockJS('/web-app-client');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, () => {
            // connect to message broker and subscribe to it
            this.stompClient.subscribe('/topic/broker', (response) => {
                const resp = JSON.parse(response.body);
                console.log("response: ", resp);
            });
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

    connectAndSend = (myObj) => {
        return new Promise(() => {
            this.connect()
        }).then( () => {
            this.send(myObj)
        });
    };


    promiseConnect = () => {
        return new Promise(() => {
            this.connect()
        })
    };
}

