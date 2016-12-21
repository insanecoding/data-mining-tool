import React, {PropTypes} from "react";
import GenericForm from "./GenericForm";


const RightSideForm_Welcome = ({title}) => {
        return(
            <GenericForm title={title}/>
        )
};

RightSideForm_Welcome.propTypes = {
    title: PropTypes.string.isRequired,
};

export default RightSideForm_Welcome;