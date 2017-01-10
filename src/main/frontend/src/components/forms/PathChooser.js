import React, {PropTypes} from "react";
import AdvancedTextField from "./../AdvancedTextField";

const style = {
    inputField: {
        width: "90%"
    }
};

const PathChooser = ({formName, cwd, onInputChange}) => {

    const changeEvent = (e) => {
        onInputChange(e.target.value, e.target.name, formName);
    };

    return (
        <div>
            <AdvancedTextField placeHolder="C:\path\to\my\dir\"
                               style={style.inputField}
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