import React, {Component} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";
import MyProgressBar from "./MyProgressBar";
import RaisedButton from "material-ui/RaisedButton";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "../actions/connectionActions";

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

    select = (index) => this.setState({selectedIndex: index});

    render() {

        const { connectionReducer, formReducer} = this.props;
        const { myConnect } = this.props.connectionActions;

        const progressBarParam = {
            visible: connectionReducer.getIn(['started']),
            statusMsg: connectionReducer.getIn(['status']),
            percentsProgress: connectionReducer.getIn(['percentsProgress']),
        };

        const myObj = {
            cwd: formReducer.getIn(['pathChooser', 'cwd']),
        };

        return (
            <div>
                <MyProgressBar {...progressBarParam}/>

                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Start" secondary={true}
                                  style={style.buttons}
                                  //{/*onClick={ myConnect("api/invoke", myObj)}*/}
                    />
                    <RaisedButton className={"button"} label="Pause" secondary={true}
                                  style={style.buttons} onClick={ () => myConnect("api/invoke", myObj)}/>
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