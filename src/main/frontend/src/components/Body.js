import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import FlatButton from "material-ui/FlatButton";
import LinearProgress from "material-ui/LinearProgress";
import Subheader from "material-ui/Subheader";
import {List, ListItem} from "material-ui/List";
import MyListItem from "./MyListItem";
import {getQuery, postQuery} from "./../rest/rest-client";
import MyDialog from "./MyDialog";
import TextField from "material-ui/TextField";
import {Row, Col} from "react-grid-system";
import {WebsocketClient} from "./../rest/websocket";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";

const style = {
    title: {
        textAlign: "center"
    },
    buttons: {
        padding: "5px",
        margin: "auto auto",
        boxShadow: "none"
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
    imageInput: {
        cursor: 'pointer',
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        width: '100%',
        opacity: 0,
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
                <div style={{display: 'flex', flexDirection: 'row', padding: 0,}}>
                    <label>Current working directory</label>
                    <TextField
                        hintText="Specify working directory..."
                    />
                    <FlatButton label="Choose" labelPosition="before">
                        <input type="file" style={style.imageInput}/>
                    </FlatButton>
                </div>



                <Row>
                    <Col xs={6} md={6}>
                        <MyListItem text="Download Blacklist"/>
                        <MyListItem text="Uncompress Blacklist"/>
                        <MyListItem text="Import Blacklist"/>
                        <MyListItem text="Add features"/>
                        <MyListItem text="Run experiments"/>
                    </Col>
                    <Col xs={6} md={6}>
                        <div>
                            <TextField
                                hintText="Hint Text"
                                floatingLabelText="Username"
                                floatingLabelFixed={true}
                            />
                            <TextField
                                hintText="Hint Text 2"
                                floatingLabelText="Password"
                                floatingLabelFixed={true}
                                type="password"
                            />
                            <TextField
                                hintText="Hint Text 2"
                                floatingLabelText="Database name"
                                floatingLabelFixed={true}
                            />
                            <TextField
                                hintText="Hint Text 2"
                                floatingLabelText="Port"
                                floatingLabelFixed={true}
                            />

                            <List>
                                <Subheader inset={true}>Blacklists</Subheader>
                                <Col xs={2}>
                                    <FlatButton primary={false} icon={<FontIcon className="fa fa-plus"/>}>
                                        <input type="file" style={style.imageInput}/>
                                    </FlatButton>
                                    <IconButton tooltip="Add item">
                                        <FontIcon className="fa fa-plus"/>
                                    </IconButton>
                                </Col>
                                <Col xs={10}>
                                    <ListItem primaryText={"Some blacklist #1"} rightIconButton={
                                        <IconButton tooltip="Remove item">
                                            <FontIcon className="fa fa-minus"/>
                                        </IconButton>}
                                    />
                                    <ListItem primaryText={"Some blacklist #2"} rightIconButton={
                                        <IconButton tooltip="Remove item">
                                            <FontIcon className="fa fa-minus"/>
                                        </IconButton>}
                                    />
                                </Col>









                            </List>
                        </div>
                    </Col>
                </Row>


                <div style={style.buttonContainer}>
                    <Row>
                        <Col xs={6}>
                            <RaisedButton className={"button"} label="Start" secondary={true}
                                          onTouchTap={this.getRest} style={style.buttons}/>
                        </Col>
                        <Col xs={6}>
                            <RaisedButton className={"button"} label="Pause" secondary={true}
                                          onTouchTap={this.getPost} style={style.buttons}/>
                        </Col>
                    </Row>

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