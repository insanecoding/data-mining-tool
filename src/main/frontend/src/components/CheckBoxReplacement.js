import React, {PropTypes} from "react";
import Checkbox from "material-ui/Checkbox";

const CheckBoxReplacement = ({value, ...otherParams}) => {

    const elem = (value) ? <Checkbox checked={true} {...otherParams}/> :
        <Checkbox checked={false} {...otherParams}/>;
    return (
        elem
    )
};

CheckBoxReplacement.propTypes = {
    value: PropTypes.bool.isRequired,
    otherParams: PropTypes.object
};

export default CheckBoxReplacement;