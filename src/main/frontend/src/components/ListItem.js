import React, {Component} from 'react';
import Toggle from 'material-ui/Toggle';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import FontIcon from 'material-ui/FontIcon';
import {white, fullBlack} from 'material-ui/styles/colors';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

const muiTheme = getMuiTheme({
    appBar: {
        color: white,
        textColor: fullBlack,
    },
});

class ListItem extends Component {
    render() {
        return(
            <MuiThemeProvider muiTheme={muiTheme}>
                <AppBar
                    style={{height: "50px", width: "30%", margin: "auto"}}
                    title={this.props.name}
                    titleStyle={{lineHeight: "2.2"}}
                    iconElementLeft={ <Toggle style={{padding: "7px"}}/>}
                    iconElementRight={
                        <IconButton iconStyle={{width: "40px", height: "40px"}}
                                    style={{width: "40px", height: "40px", padding: "6px"}}>
                            <FontIcon className="fa fa-bars fa-fw"></FontIcon>
                        </IconButton>
                    }
                />
            </MuiThemeProvider>
        )
    }
}
export default ListItem;
