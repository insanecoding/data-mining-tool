import React, {Component} from "react";
import Toggle from "material-ui/Toggle";
import AppBar from "material-ui/AppBar";
import IconButton from "material-ui/IconButton";
import FontIcon from "material-ui/FontIcon";
import {white, fullBlack} from "material-ui/styles/colors";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

const muiTheme = getMuiTheme({
    appBar: {
        color: white,
        textColor: fullBlack,
    },
});

const style = {
    appBar: {
        minWidth: "390px",
    },
    toggleStyle: {
        padding: "11px"
    }
};

class ListItem extends Component {
    render() {
        return (
            <MuiThemeProvider muiTheme={muiTheme}>
                <AppBar
                    style={style.appBar}
                    title={this.props.name}
                    titleStyle={style.titleStyle}
                    iconElementLeft={ <Toggle style={style.toggleStyle} labelPosition="right"/>}
                    iconElementRight={
                        <IconButton iconStyle={style.iconStyle} style={style.icon}>
                            <FontIcon className="fa fa-bars fa-fw"/>
                        </IconButton>
                    }
                />
            </MuiThemeProvider>
        )
    }
}
export default ListItem;
