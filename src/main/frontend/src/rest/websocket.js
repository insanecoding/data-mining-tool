import SockJS from "sockjs-client";
import Stomp from "stompjs";

export class WebsocketClient {
    constructor() {
        this.stompClient = null;
    }

    connect = () => {
        const socket = new SockJS('/gs-guide-websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            this.stompClient.subscribe('/topic/greetings', (greeting) => {
                const resp = JSON.parse(greeting.body);
                console.log("resonse: ", resp);
            });
            this.stompClient.send("/app/hello", {}, JSON.stringify({'name': "foo"}));
        });
    };

    disconnect = () => {
        if (this.stompClient != null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    };

    sendName = () => {
        this.stompClient.send("/app/hello", {}, JSON.stringify({'name': "foo"}));
    }
}

