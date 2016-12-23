import React, {Component} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";
import MyProgressBar from "./MyProgressBar";
import RaisedButton from "material-ui/RaisedButton";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "../actions/connectionActions";
import {mapToArray} from "./../util/misc";
import {WebsocketClient} from "./../util/websocket";

const recentsIcon = <FontIcon className="fa fa-undo"/>;
const favoritesIcon = <FontIcon className="fa fa-heart"/>;
const nearbyIcon = <FontIcon className="fa fa-location-arrow"/>;

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

class Footer extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            selectedIndex: 0,
        };
    }

    componentDidMount = () => {
        this.websocketClient = new WebsocketClient();
        this.websocketClient.connect(this.outputWebSocket);
    };

    componentWillUnmount = () => {
        this.websocketClient.disconnect();
        this.websocketClient = null;
    };

    outputWebSocket = (resp) => {
        const { onWebsocketMessage } = this.props.connectionActions;
        const response = JSON.parse(resp.body);
        console.log("response is: ", response);
        const status = response.info + " (" + response.progress + "%)";
        const percentsProgress = response.progress;
        onWebsocketMessage(status, percentsProgress);
    };

    select = (index) => this.setState({selectedIndex: index});

    handleClick = (buttonName) => {
        const { executePostQuery, executeGetQuery } = this.props.connectionActions;
        const { formReducer } = this.props;

        if (buttonName === "start") {
            const myObj = mapToArray(formReducer.getIn(['forms']));
            this.websocketClient.send();
            executePostQuery("api/invoke", myObj);
        } else if (buttonName === "cancel") {
            executeGetQuery("api/cancel");
        }
    };

    render() {

        const { connectionReducer} = this.props;

        const progressBarParam = {
            visible: connectionReducer.getIn(['started']),
            statusMsg: connectionReducer.getIn(['status']),
            percentsProgress: connectionReducer.getIn(['percentsProgress']),
        };

        return (
            <div>
                <MyProgressBar {...progressBarParam}/>

                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Start" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("start") } />
                    <RaisedButton className={"button"} label="Pause" secondary={true}
                                  style={style.buttons} onClick={() => this.handleClick("cancel")} />
                </div>
                <Paper zDepth={0}>
                    <BottomNavigation selectedIndex={this.state.selectedIndex}>
                        <BottomNavigationItem
                            label="Check website"
                            icon={recentsIcon}
                            onTouchTap={() => this.select(0)}
                        />
                        <BottomNavigationItem
                            label="Experiments"
                            icon={favoritesIcon}
                            onTouchTap={() => this.select(1)}
                        />
                        <BottomNavigationItem
                            label="Results"
                            icon={nearbyIcon}
                            onTouchTap={() => this.select(2)}
                        />
                    </BottomNavigation>
                </Paper>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        connectionReducer: state.connectionReducer,
        formReducer: state.formReducer,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        connectionActions: bindActionCreators(connectionActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Footer)