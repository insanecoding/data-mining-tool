import React, {PropTypes} from "react";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import {BottomNavigation, BottomNavigationItem} from "material-ui/BottomNavigation";
import {browserHistory} from "react-router";

const homeIcon = <FontIcon className="fa fa-home"/>;
const settingsIcon = <FontIcon className="fa fa-cog"/>;
const resultsIcon = <FontIcon className="fa fa-bar-chart"/>;

const Footer = ({activeTab, tabChanged}) => {

    const select = (index) => {
        tabChanged(index);

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

    return (

        <div>
            <Paper zDepth={0}>
                <BottomNavigation selectedIndex={activeTab}>
                    <BottomNavigationItem
                        label="Home page"
                        icon={homeIcon}
                        onTouchTap={() => select(0)}
                    />
                    <BottomNavigationItem
                        label="Settings"
                        icon={settingsIcon}
                        onTouchTap={() => select(1)}
                    />
                    <BottomNavigationItem
                        label="Results"
                        icon={resultsIcon}
                        onTouchTap={() => select(2)}
                    />
                </BottomNavigation>
            </Paper>
        </div>
    );
};

Footer.propTypes = {
    activeTab: PropTypes.number.isRequired,
    tabChanged: PropTypes.func.isRequired,
};

export default Footer;