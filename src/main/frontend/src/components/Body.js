import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import InlineForm from "./InlineForm";
import RightSideForm from "./RightSideForm";
import LinearProgress from "material-ui/LinearProgress";
import MyListItem from "./MyListItem";
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
    progress: {
        margin: "auto",
        width: "50%",
        padding: "10px",
        backgroundColor: "white"
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
    notificationBar: {
        textAlign: "center",
        marginTop: "13px"
    }
};

class Body extends Component {

    constructor(props) {
        super(props);
        this.state = {
            completed: 0,
            isDialogOpen: false,
            dialogText: "",
            dialogHeader: "",
        };
    };

    componentDidMount = () => {
        this.timer = setTimeout(() => this.progress(5), 1000);
        this.client = new WebsocketClient();
    };

    componentWillUnmount = () => {
        clearTimeout(this.timer);
        this.client.disconnect();
        this.client = null;
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
        getQuery('api/retrieve').then((res) => {
            this.updateDialogText(res, 'GET');
        });
    };

    getPost = (e) => {
        e.preventDefault();
        let myObj = {
            firstName: "My",
            lastName: "New Object",
        };
        postQuery('api/add', myObj).then((res) => {
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
                        <RightSideForm/>
                    </Col>
                </Row>

                <Row>
                    <div style={style.progress}>
                        <LinearProgress mode="determinate" value={this.state.completed}/>
                    </div>
                </Row>

                <div style={style.notificationBar}>
                    Bla
                </div>


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