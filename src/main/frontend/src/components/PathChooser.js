import React, {PropTypes} from "react";
import AdvancedTextField from "./AdvancedTextField";


const PathChooser = ({formName, cwd, onInputChange}) => {

    const changeEvent = (e) => {
        onInputChange(formName, e.target.name, e.target.value);
    };

    return (
        <div>
            <AdvancedTextField placeHolder="C:\path\to\my\dir\"
                               pattern={"path"}
                               label={"Please, specify working directory"}
                               fieldName={"cwd"}
                               value={cwd}
                               onChangeEvent={changeEvent}
            />
        </div>
    )
};

PathChooser.propTypes = {
    formName: PropTypes.string.isRequired,
    cwd: PropTypes.string.isRequired,
    onInputChange: PropTypes.func.isRequired,
};

export default PathChooser;