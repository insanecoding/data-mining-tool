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
        height: "50px",
    },
    titleStyle: {
        lineHeight: "2.2"
    },
    toggleStyle: {
        padding: "7px"
    },
    icon: {
        width: "40px",
        height: "40px",
        padding: "6px"
    },
    iconStyle: {
        width: "40px",
        height: "40px"
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
                    iconElementLeft={ <Toggle style={style.toggleStyle}/>}
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
