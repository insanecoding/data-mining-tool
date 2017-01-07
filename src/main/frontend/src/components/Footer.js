import React, {Component} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";
import {browserHistory} from "react-router";

const homeIcon = <FontIcon className="fa fa-home"/>;
const settingsIcon = <FontIcon className="fa fa-cog"/>;
const resultsIcon = <FontIcon className="fa fa-bar-chart"/>;

export default class Footer extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            selectedIndex: 0
        };
    }

    select = (index) => {
        this.setState({selectedIndex: index});

        let url;
        switch (index) {
            case 0:
                url = "/";
                break;
            case 1:
                url = "/settings";
                break;

            case 2:
                url = "/results";
                break;

            default:
                url = "/";
                break;
        }

        browserHistory.push(url);
    };

    render() {
        return (
            <div>
                <Paper zDepth={0}>
                    <BottomNavigation selectedIndex={this.state.selectedIndex}>
                        <BottomNavigationItem
                            label="Home page"
                            icon={homeIcon}
                            onTouchTap={() => this.select(0)}
                        />
                        <BottomNavigationItem
                            label="Settings"
                            icon={settingsIcon}
                            onTouchTap={() => this.select(1)}
                        />
                        <BottomNavigationItem
                            label="Results"
                            icon={resultsIcon}
                            onTouchTap={() => this.select(2)}
                        />
                    </BottomNavigation>
                </Paper>
            </div>
        )
    }
}