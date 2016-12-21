import React, {PropTypes} from "react";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";


const RightSideForm_Uncompress = ({title}) => {

    return(
        <GenericForm title={title}>
            <AdvancedTextField hint="\sub\folder\with\archives"
                               label={"Sub-directory with archives"}
                               name={"archives"}
                               pattern={"path"}
            />
        </GenericForm>
    );
};

RightSideForm_Uncompress.propTypes = {
    title: PropTypes.string.isRequired,
};


export default RightSideForm_Uncompress;