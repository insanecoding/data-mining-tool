import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import InlineForm from "./InlineForm";
import RightSideForm from "./RightSideForm";
import MyListItem from "./MyListItem";
import MyProgressBar from "./MyProgressBar";
import {getQuery, postQuery} from "./../rest/rest-client";
import MyDialog from "./MyDialog";
import {Row, Col} from "react-grid-system";
import {WebsocketClient} from "./../rest/websocket";
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

class Body extends Component {

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
        };
    };

    formHandler = (elem, event) => {
        this.setState(
            {
                elem: event.target.value
            }
        )
    };

    componentDidMount = () => {
        this.timer = setTimeout(() => this.progress(5), 1000);
        this.client = new WebsocketClient();
        this.client.connect();
    };

    componentWillUnmount = () => {
        clearTimeout(this.timer);
        // this.client.disconnect();
        // this.client = null;
    };

    progress(completed) {
        if (completed > 100) {
            this.setState({completed: 100});
        } else {
            this.setState({completed});
            const diff = Math.random() * 10;
            this.timer = setTimeout(() => this.progress(completed + diff), 1000);
        }
    }

    updateDialogText = (res, type) => {
        this.setState({
            isDialogOpen: true,
            dialogText: res.firstName + ' ' + res.lastName,
            dialogHeader: type + " response"
        });
    };

    getRest = (e) => {
        e.preventDefault();
        console.log("current state", this.state);
        getQuery('api/result').then((res) => {
            this.updateDialogText(res, 'GET');
        });
    };

    getPost = (e) => {
        e.preventDefault();
        let myObj = {
            firstName: "My",
            lastName: "New Object",
        };

        this.client.send(JSON.stringify(myObj));

        postQuery('api/invoke', myObj).then((res) => {
            this.updateDialogText(res, 'POST');
        });
    };

    handleClose = (e) => {
        e.preventDefault();
        this.setState({
            isDialogOpen: false,
            dialogText: "",
            dialogHeader: "",
        });
    };

    render() {
        return (
            <div>
                <h1 style={style.title}>Welcome to website classification utility</h1>

                <Row>
                    <Col xs={12}>
                        <InlineForm/>
                    </Col>
                </Row>

                <Row>
                    <Col xs={6}>
                        <MyListItem text="Download Blacklist"/>
                        <MyListItem text="Uncompress Blacklist"/>
                        <MyListItem text="Import Blacklist"/>
                        <MyListItem text="Add features"/>
                        <MyListItem text="Run experiments"/>
                    </Col>
                    <Col xs={6}>
                        <RightSideForm userName={this.state.userName}
                                       password={this.state.password}
                                       dbName={this.state.dbName}
                                       port={this.state.port}
                                       formHandler={this.formHandler}
                        />
                    </Col>
                </Row>

                <MyProgressBar completed={this.state.completed}/>

                <div style={style.buttonContainer}>
                    <Row>
                        <RaisedButton className={"button"} label="Start" secondary={true}
                                      onTouchTap={this.getRest} style={style.buttons}/>
                        <RaisedButton className={"button"} label="Pause" secondary={true}
                                      onTouchTap={this.getPost} style={style.buttons}/>
                    </Row>
                </div>

                <MyDialog amIOpen={this.state.isDialogOpen}
                          title={this.state.dialogHeader} textMain={this.state.dialogText}
                          handleRequestClose={this.handleClose}/>
            </div>
        )
    }
}

export default Body;