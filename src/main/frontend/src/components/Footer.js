import React, {Component} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";

const recentsIcon = <FontIcon className="fa fa-undo"/>;
const favoritesIcon = <FontIcon className="fa fa-heart"/>;
const nearbyIcon = <FontIcon className="fa fa-location-arrow"/>;

const style = {
    footer: {
        position: "fixed",
        bottom: "0",
        left: "0",
        width: "100%",
        boxShadow: "none",
    }
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
        return (
            <Paper zDepth={1} style={style.footer}>
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
        )
    }
}

export default Footer;