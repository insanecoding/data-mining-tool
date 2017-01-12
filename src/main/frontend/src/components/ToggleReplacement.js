import React, {PropTypes} from "react";
import Toggle from "material-ui/Toggle";

const ToggleReplacement = ({value, ...otherParams}) => {

    const elem = (value) ? <Toggle toggled={true} {...otherParams}/> :
        <Toggle toggled={false} {...otherParams}/>;
    return (
        elem
    )
};

ToggleReplacement.propTypes = {
    value: PropTypes.bool.isRequired,
    otherParams: PropTypes.object
};

export default ToggleReplacement;