import React, {Component} from 'react';
import injectTapEventPlugin from 'react-tap-event-plugin';

import {deepOrange500} from 'material-ui/styles/colors';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import "./App.css";
import Header from "./components/Header";
import Body from "./components/Body";

import FontIcon from 'material-ui/FontIcon';
import {BottomNavigation, BottomNavigationItem} from 'material-ui/BottomNavigation';
import Paper from 'material-ui/Paper';
import IconLocationOn from 'material-ui/svg-icons/communication/location-on';

const recentsIcon = <FontIcon className="material-icons">restore</FontIcon>;
const favoritesIcon = <FontIcon className="material-icons">favorite</FontIcon>;
const nearbyIcon = <IconLocationOn />;

// prevent warnings with Material UI
injectTapEventPlugin();

const muiTheme = getMuiTheme({
    palette: {
        accent1Color: deepOrange500,
    },
});

class App extends Component {

    state = {
        selectedIndex: 0,
    };

    select = (index) => this.setState({selectedIndex: index});

    render() {
        return (
            <MuiThemeProvider muiTheme={muiTheme}>
                <div id="all">
                    <Header />
                    <Body/>

                    <div id="footer">
                        <Paper zDepth={1}>
                            <BottomNavigation selectedIndex={this.state.selectedIndex}>
                                <BottomNavigationItem
                                    label="Recents"
                                    icon={recentsIcon}
                                    onTouchTap={() => this.select(0)}
                                />
                                <BottomNavigationItem
                                    label="Favorites"
                                    icon={favoritesIcon}
                                    onTouchTap={() => this.select(1)}
                                />
                                <BottomNavigationItem
                                    label="Nearby"
                                    icon={nearbyIcon}
                                    onTouchTap={() => this.select(2)}
                                />
                            </BottomNavigation>
                        </Paper>
                    </div>
                </div>
            </MuiThemeProvider>
        );
    }
}

export default App;