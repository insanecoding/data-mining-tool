import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import InlineForm from "./InlineForm";
import RightSideForm from "./RightSideForm";
import MyListItem from "./MyListItem";
import MyProgressBar from "./MyProgressBar";
import {getQuery, postQuery} from "../connection/rest";
import {Row, Col} from "react-grid-system";
import {WebsocketClient} from "./../connection/websocket";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";

const style = {
    title: {
        textAlign: "center",
        marginTop: "20px",
        marginLeft: "20px",
        marginRight: "20px",
        marginBottom: "1px"
    },
    buttons: {
        padding: "5px",
        margin: "auto auto",
        boxShadow: "none"
    },
    buttonContainer: {
        textAlign: "center",
        padding: "20px",
    },
    fileChooser: {
        cursor: 'pointer',
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        width: '100%',
        opacity: 0,
    },
    inlineForm: {
        textAlign: "center"
    },
};

export default class Body extends Component {

    constructor(props) {
        super(props);
        this.state = {
            completed: 0,
            isDialogOpen: false,
            dialogText: "",
            dialogHeader: "",
            userName: "",
            password: "",
            dbName: "",
            port: "",
            wsMsg: "Ready"
        };
    };

    formHandler = (e) => {
        this.setState({[e.target.name]: e.target.value});
    };

    componentDidMount = () => {
        this.websocket = new WebsocketClient();
        this.websocket.connect(this.outputWebSocket);
    };

    componentWillUnmount = () => {
        this.websocket.disconnect();
        this.websocket = null;
    };

    // getRest = (e) => {
    //     e.preventDefault();
    //     getQuery('api/result').then((res) => {
    //         this.updateDialogText(res, 'GET');
    //     });
    // };

    startService = () => {
        this.setState({wsMsg: "", completed: 0});
        let myObj = {
            userName: this.state.userName,
            password: this.state.password,
            dbName: this.state.dbName,
            port: this.state.port
        };
        console.log(myObj);

        this.websocket.send();

        postQuery('api/invoke', myObj).then((res) => {
            console.log(res);
            this.setState(
                {
                    // 'finished' or 'cancelled'
                    wsMsg: res.status,
                    completed: 0
                }
            );
        });
    };

    cancelService = () => {
        getQuery('api/cancel');
    };

    outputWebSocket = (resp) => {
        const response = JSON.parse(resp.body);
        console.log("response is: ", response);
        this.setState(
            {
                wsMsg: response.info + " (" + response.progress + "%)",
                completed: response.progress
            }
        );
    };

    render() {
        return (
            <div {...this.props}>
                <h1 style={style.title}>Welcome to website classification utility</h1>

                <InlineForm style={style.inlineForm}/>

                
                <Row>
                    <Col xs={12} md={6}>
                        <MyListItem text="Download Blacklist"/>
                        <MyListItem text="Uncompress Blacklist"/>
                        <MyListItem text="Import Blacklist"/>
                        <MyListItem text="Add features"/>
                        <MyListItem text="Run experiments"/>
                    </Col>
                    <Col xs={12} md={6}>
                        <RightSideForm formHandler={this.formHandler}/>
                    </Col>
                </Row>

                <MyProgressBar completed={this.state.completed} status={this.state.wsMsg}/>

                <div style={style.buttonContainer}>
                        <RaisedButton className={"button"} label="Start" secondary={true}
                                      onTouchTap={this.startService} style={style.buttons}/>
                        <RaisedButton className={"button"} label="Pause" secondary={true}
                                      onTouchTap={this.cancelService} style={style.buttons}/>
                </div>
            </div>
        )
    }
}