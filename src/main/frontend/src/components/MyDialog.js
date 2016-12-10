import React, {Component} from 'react';
import Dialog from 'material-ui/Dialog';

class MyDialog extends Component {

    render() {
        return(
            <Dialog
                open={this.props.amIOpen}
                title={this.props.title}
                actions={this.props.standardActions}
                onRequestClose={this.props.handleRequestClose}
            >
                {this.props.textMain}
            </Dialog>
        )
    }
}

export default MyDialog;