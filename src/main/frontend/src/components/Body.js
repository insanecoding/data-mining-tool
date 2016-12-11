import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import LinearProgress from "material-ui/LinearProgress";
import ListItem from "./ListItem";
import {getQuery, postQuery} from "./../rest/rest-client";
import MyDialog from "./MyDialog";
import {WebsocketClient} from "./../rest/websocket";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";

const style = {
    title: {
        textAlign: "center"
    },
    buttons: {
        position: "relative",
        padding: "5px",
        margin: "auto auto",
        // "width": "15%"
    },
    progress: {
        margin: "auto",
        width: "50%"
    },
    buttonContainer: {
        margin: "auto",
        width: "11.5%",
        padding: "20px",
    },
    Items: {
        margin: "auto",
        width: "30%"
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
                <div style={style.Items}>
                    <ListItem name="Import Blacklist"/>
                    <ListItem name="Uncompress Blacklist"/>
                    <ListItem name="Add Features"/>
                    <ListItem name="Something more"/>
                </div>
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Get" secondary={true}
                                  onTouchTap={this.getRest} style={style.buttons}/>
                    <RaisedButton className={"button"} label="Post" secondary={true}
                                  onTouchTap={this.getPost} style={style.buttons}/>
                </div>

                <LinearProgress mode="determinate" value={this.state.completed} style={style.progress}/>
                <MyDialog amIOpen={this.state.isDialogOpen}
                          title={this.state.dialogHeader} textMain={this.state.dialogText}
                          handleRequestClose={this.handleClose}/>
            </div>
        )
    }
}

export default Body;