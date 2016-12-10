import React, {Component} from 'react';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import Build from 'material-ui/svg-icons/action/build';
import FlatButton from 'material-ui/FlatButton';
import MyDialog from "./MyDialog";

class Header extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            isHelpOpen: false,
            isAboutOpen: false
        };
    }

    handleRequestClose() {
        this.setState({
            isHelpOpen: false,
            isAboutOpen: false
        });
    }

    showHelp() {
        this.setState({
            isHelpOpen: true,
        });
    }

    showAbout() {
        this.setState({
            isAboutOpen: true
        })
    }

    helpText = "Here you'll get help";
    aboutText = "We are great! Learn more about us!";

    iconMenu =
        <IconMenu iconButtonElement={<IconButton><MoreVertIcon /></IconButton>}
                  targetOrigin={{horizontal: 'right', vertical: 'top'}}
                  anchorOrigin={{horizontal: 'right', vertical: 'top'}}>
            <MenuItem primaryText="Help" onTouchTap={this.showHelp.bind(this)}/>
            <MenuItem primaryText="About" onTouchTap={this.showAbout.bind(this)}/>
        </IconMenu>;

    standardActions =
        <FlatButton label="Ok" primary={true} onTouchTap={this.handleRequestClose.bind(this)}/>;

    render() {
        return (
            <div>
                <AppBar
                    title="Website classification tool"
                    iconElementLeft={<IconButton><Build /></IconButton>}
                    iconElementRight={this.iconMenu}
                />

                <MyDialog amIOpen={this.state.isHelpOpen}
                          title="I'm help!" standardActions={this.standardActions}
                          handleRequestClose={this.handleRequestClose.bind(this)} textMain={this.helpText}/>

                <MyDialog amIOpen={this.state.isAboutOpen}
                          title="I'm about!" standardActions={this.standardActions}
                          handleRequestClose={this.handleRequestClose.bind(this)} textMain={this.aboutText}/>
            </div>
        );
    }
}
export default Header;