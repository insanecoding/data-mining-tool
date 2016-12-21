import React, {PropTypes} from "react";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";


const RightSideForm_Uncompress = ({formName, title, archives, onInputChange}) => {

    const changeEvent = (e) => {
        onInputChange(e.target.value, e.target.name, formName, "forms");
    };

    return(
        <GenericForm title={title}>
            <AdvancedTextField placeHolder={"\\sub\\folder\\with\\archives\\"}
                               pattern={"path"}
                               label={"Sub-directory with archives"}
                               fieldName={"archives"}
                               value={archives}
                               onChangeEvent={changeEvent}
            />
        </GenericForm>
    );
};

RightSideForm_Uncompress.propTypes = {
    title: PropTypes.string.isRequired,
    formName: PropTypes.string.isRequired,
    archives: PropTypes.string.isRequired,
    onInputChange: PropTypes.func.isRequired,
};


export default RightSideForm_Uncompress;