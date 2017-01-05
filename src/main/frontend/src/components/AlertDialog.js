import React, {PropTypes} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";

const style = {
    buttons: {
        textAlign: "center"
    }
};


/**
 * A modal dialog can only be closed by selecting one of the actions.
 */
const AlertDialog = ({title, type, handleCloseOk, handleCloseCancel, isOpen, children, buttonsStyle}) => {

    const assignActions = (type) => {
        if (type === "alert") {

            return (
                <div style={buttonsStyle || style.buttons}>
                    <FlatButton
                        label="Ok"
                        primary={true}
                        disabled={false}
                        onTouchTap={handleCloseOk}
                    />
                </div>
            )
        } else if (type === "dialog") {
            return (
                <div style={buttonsStyle || style.buttons}>
                    <FlatButton
                        label="Ok"
                        primary={true}
                        disabled={false}
                        onTouchTap={handleCloseOk}/>

                    <FlatButton
                        label="Cancel"
                        primary={true}
                        disabled={false}
                        onTouchTap={handleCloseCancel}/>
                </div>
            );
        }
    };


    return (
        <Dialog
            title={title}
            actions={ assignActions(type)}
            modal={true}
            open={isOpen}
        >
            {children}
        </Dialog>
    );
};

AlertDialog.propTypes = {
    title: PropTypes.string.isRequired,
    type: PropTypes.string.isRequired,
    handleCloseOk: PropTypes.func.isRequired,
    handleCloseCancel: PropTypes.func,
    isOpen: PropTypes.bool.isRequired,
    children: PropTypes.node
};

export default AlertDialog;