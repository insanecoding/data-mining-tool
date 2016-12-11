import React, {Component} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";

class MyDialog extends Component {

    standardActions = <FlatButton label="Ok" primary={true} onTouchTap={this.props.handleRequestClose}/>;
    render() {
        return(
            <Dialog
                open={this.props.amIOpen}
                title={this.props.title}
                actions={this.standardActions}
                onRequestClose={this.props.handleRequestClose}
            >
                {this.props.textMain}
            </Dialog>
        )
    }
}

export default MyDialog;