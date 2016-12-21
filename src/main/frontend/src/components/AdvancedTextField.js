import React, {Component, PropTypes} from "react";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";

const style = {
    inputField: {
        width: "80%"
    },
};
class AdvancedTextField extends Component {

    constructor(props) {
        super(props);

        this.state = {
            isValid: 0
        };
    }


    showValidationResult = (validationResult) => {

        if (validationResult === 1)
            return <FontIcon
                className="fa fa-check"
                color={"green"}
            />;
        else if (validationResult === -1)
            return <FontIcon
                className="fa fa-times"
                color={"red"}
            />;
    };

    changeState = (validationResult) => {
        if (validationResult === true)
            this.setState({
                isValid: 1
            });
        else
            this.setState({
                isValid: -1
            });
    };

    onBlurValidate = (pattern, e) => {
        const currentValue = e.target.value.trim();
        let result;
        if (pattern === "number") {
            result = (!isNaN(currentValue));
            console.log(result);
            this.changeState(result);
            console.log(this.state);
        } else if (pattern === "not_empty") {
            result = (currentValue !== "");
            console.log(result);
            this.changeState(result);
            console.log(this.state);
        } else if (pattern === "path") {
            const windowsFilePathPattern = /^(?:(?:[a-z]:|\\\\[a-z0-9_.$?-]+\\[a-z0-9_.$?-]+)\\|\\?[^\\/:*?"<>|\r\n]+\\?)(?:[^\\/:*?"<>|\r\n]+\\)*[^\\/:*?"<>|\r\n]*$/i;
            result = (windowsFilePathPattern.test(currentValue));
            console.log(result);
            this.changeState(result);
            console.log(this.state);
        }

    };

    render() {
        const { placeHolder, pattern, label, fieldName, value, onChangeEvent } = this.props;
        return (
            <div>
                <TextField
                    hintText={placeHolder}
                    onBlur={ (e) => this.onBlurValidate(pattern, e)}
                    floatingLabelText={label}
                    floatingLabelFixed={true}
                    name={fieldName}
                    value={value}
                    onChange={onChangeEvent}
                    style={style.inputField}
                />

                { this.showValidationResult(this.state.isValid)}

            </div>
        )
    }
}

AdvancedTextField.propTypes = {
    placeHolder: PropTypes.string.isRequired,
    pattern: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    fieldName: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
};

export default AdvancedTextField;

