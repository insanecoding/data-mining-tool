import React, {Component} from "react";
import AppBar from "material-ui/AppBar";
import IconButton from "material-ui/IconButton";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import Build from "material-ui/svg-icons/action/build";
import MyDialog from "./MyDialog";

class Header extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            isHelpOpen: false,
            isAboutOpen: false
        };
    }

    handleRequestClose = () => {
        this.setState({
            isHelpOpen: false,
            isAboutOpen: false
        });
    };

    showHelp = () => {
        this.setState({
            isHelpOpen: true,
        });
    };

    showAbout = () => {
        this.setState({
            isAboutOpen: true
        })
    };

    helpText = "Here you'll get help";
    aboutText = "We are great! Learn more about us!";

    iconMenu =
        <IconMenu iconButtonElement={<IconButton><MoreVertIcon /></IconButton>}
                  targetOrigin={{horizontal: 'right', vertical: 'top'}}
                  anchorOrigin={{horizontal: 'right', vertical: 'top'}}>
            <MenuItem primaryText="Help" onTouchTap={this.showHelp}/>
            <MenuItem primaryText="About" onTouchTap={this.showAbout}/>
        </IconMenu>;

    render() {
        return (
            <div>
                <AppBar
                    title="Website classification tool"
                    iconElementLeft={<IconButton><Build /></IconButton>}
                    iconElementRight={this.iconMenu}
                />

                <MyDialog amIOpen={this.state.isHelpOpen} title="I'm help!" textMain={this.helpText}
                          handleRequestClose={this.handleRequestClose}/>

                <MyDialog amIOpen={this.state.isAboutOpen} title="I'm about!" textMain={this.aboutText}
                          handleRequestClose={this.handleRequestClose}/>
            </div>
        );
    }
}
export default Header;