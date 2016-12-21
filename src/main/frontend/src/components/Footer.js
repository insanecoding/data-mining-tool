import React, {Component} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";
import MyProgressBar from "./MyProgressBar";
import RaisedButton from "material-ui/RaisedButton";

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

export default class Footer extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            selectedIndex: 0,
        };
    }

    select = (index) => this.setState({selectedIndex: index});

    render() {
        return (
            <div>
                <MyProgressBar visible={true}/>

                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Start" secondary={true}
                                  style={style.buttons}/>
                    <RaisedButton className={"button"} label="Pause" secondary={true}
                                  style={style.buttons}/>
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