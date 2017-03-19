import React, {Component} from "react";
import AppBar from "material-ui/AppBar";
import IconButton from "material-ui/IconButton";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import Build from "material-ui/svg-icons/action/build";
import MyDialog from "./MyDialog";

const styles = {
    title: {
        textAlign: "center"
    }
};

export default class Header extends Component {

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

    helpText = "This is control panel to prepare and run experiments with website classification. " +
        "For further details, please, consult manual";
    aboutText = "(c) Novozhilov Dmitry, ComSec Laboratory, SPIIRAS, 2017";

    iconMenu =
        <IconMenu iconButtonElement={<IconButton><MoreVertIcon /></IconButton>}
                  targetOrigin={{horizontal: 'right', vertical: 'top'}}
                  anchorOrigin={{horizontal: 'right', vertical: 'top'}}>
            <MenuItem primaryText="Help" name="help" onTouchTap={this.showHelp}/>
            <MenuItem primaryText="About" name="about" onTouchTap={this.showAbout}/>
        </IconMenu>;

    render() {
        return (
            <div>
                <AppBar
                    title="ComSec laboratory at SPIIRAS"
                    titleStyle={styles.title}
                    iconElementLeft={<IconButton><Build /></IconButton>}
                    iconElementRight={this.iconMenu}
                />

                <MyDialog amIOpen={this.state.isHelpOpen} title="Help" handleRequestClose={this.handleRequestClose}>
                    {this.helpText}
                </MyDialog>

                <MyDialog amIOpen={this.state.isAboutOpen} title="About" handleRequestClose={this.handleRequestClose}>
                    {this.aboutText}
                </MyDialog>
            </div>
        );
    }
}