import React, {PropTypes} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";

/**
 * A modal dialog can only be closed by selecting one of the actions.
 */
const AlertDialog = ({title, type, handleClose, isOpen, children}) => {

    const assignActions = (type) => {
        let result;
        if (type === "alert")
            result = [
                <FlatButton
                    label="Ok"
                    primary={true}
                    disabled={false}
                    onTouchTap={handleClose}
                />
            ];
        return result;
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
    handleClose: PropTypes.func.isRequired,
    isOpen: PropTypes.bool.isRequired,
    children: PropTypes.node
};

export default AlertDialog;