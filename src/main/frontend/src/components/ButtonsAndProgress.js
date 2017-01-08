import React, {Component, PropTypes} from "react";
import MyProgressBar from "./MyProgressBar";
import RaisedButton from "material-ui/RaisedButton";
import {mapToArray} from "./../util/misc";
import {WebsocketClient} from "./../util/websocket";
import AlertDialog from "./AlertDialog";
import Immutable from "immutable";

const style = {
    buttons: {
        padding: "5px",
        margin: "auto auto",
        boxShadow: "none"
    },
    buttonContainer: {
        textAlign: "center",
        padding: "20px",
    },
};

class ButtonsAndProgress extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            dialogOpen: false,
        };
    }

    handleClose = () => {
        this.setState({dialogOpen: false});
    };

    componentDidMount = () => {
        this.websocketClient = new WebsocketClient();
        this.websocketClient.connect(this.outputWebSocket);
    };

    componentWillUnmount = () => {
        this.websocketClient.disconnect();
        this.websocketClient = null;
    };

    outputWebSocket = (resp) => {
        const {onWebsocketMessage} = this.props;
        const response = JSON.parse(resp.body);
        console.log("response is: ", response);
        const status = response.info;
        const percentsProgress = response.progress;
        onWebsocketMessage(status, percentsProgress);
    };

    validateExecutionOrder = (myObj) => {
        // transform array into string
        const asArray = mapToArray(myObj).map(object => object.isOn);
        // join by whitespaces
        const toStr = asArray.join(' ');
        // check whether contains some combinations of toggles that are prohibited
        return (toStr.includes("true false true") || toStr.includes("false false false false") || toStr.includes("true false false true"));
    };

    handleClick = (buttonName) => {
        const {executePostQuery, executeGetQuery, formData} = this.props;

        if (buttonName === "start") {
            if (this.validateExecutionOrder(formData)) {
                this.setState({dialogOpen: true});
            } else {
                // send empty message to start websocket communication
                this.websocketClient.send();
                executePostQuery("api/invoke", formData);
            }
        } else if (buttonName === "cancel") {
            executeGetQuery("api/cancel");
        }
    };

    render() {

        const {connectionData} = this.props;

        const progressBarParam = {
            visible: connectionData.getIn(['started']),
            statusMsg: connectionData.getIn(['status']),
            percentsProgress: connectionData.getIn(['percentsProgress']),
        };

        const disableStart = connectionData.getIn(['started']);
        const disableCancel = !disableStart;

        return (
            <div>
                <MyProgressBar {...progressBarParam}/>

                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Start" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("start") }
                                  disabled={disableStart}/>
                    <RaisedButton className={"button"} label="Cancel" secondary={true}
                                  style={style.buttons} onClick={() => this.handleClick("cancel")}
                                  disabled={disableCancel}/>
                </div>
                <AlertDialog title="Warning!" type="alert" handleCloseOk={this.handleClose}
                             isOpen={this.state.dialogOpen}>
                    Incorrect modules configuration! At least one should be selected or they don't go one-by-one!
                </AlertDialog>
            </div>
        )
    }
}

ButtonsAndProgress.propTypes = {
    onWebsocketMessage: PropTypes.func.isRequired,
    executePostQuery: PropTypes.func.isRequired,
    executeGetQuery: PropTypes.func.isRequired,
    formData: PropTypes.instanceOf(Immutable.Map).isRequired,
    connectionData: PropTypes.instanceOf(Immutable.Map).isRequired,
};

export default ButtonsAndProgress;