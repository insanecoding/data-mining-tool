import React, {Component, PropTypes} from "react";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";

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
            const regex = /^\d+$/;
            result = (regex.test(currentValue));
        } else if (pattern === "not_empty") {
            result = (currentValue !== "");
        } else if (pattern === "path") {
            const windowsFilePathPattern = /^(?:(?:[a-z]:|\\\\[a-z0-9_.$?-]+\\[a-z0-9_.$?-]+)\\|\\?[^\\/:*?"<>|\r\n]+\\?)(?:[^\\/:*?"<>|\r\n]+\\)*[^\\/:*?"<>|\r\n]*$/i;
            result = (windowsFilePathPattern.test(currentValue));
        }
        this.changeState(result);
    };

    render() {
        const { placeHolder, pattern, label, type, fieldName, value, onChangeEvent, style } = this.props;
        return (
            <div>
                <TextField
                    hintText={placeHolder}
                    onBlur={ (e) => this.onBlurValidate(pattern, e)}
                    floatingLabelText={label}
                    floatingLabelFixed={true}
                    name={fieldName}
                    value={value}
                    type={type}
                    onChange={onChangeEvent}
                    style={style || {width: "80%"}}
                />

                { this.showValidationResult(this.state.isValid)}

            </div>
        )
    }
}

AdvancedTextField.propTypes = {
    pattern: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    fieldName: PropTypes.string.isRequired,
    type: PropTypes.string,
    value: PropTypes.any.isRequired,
    onChangeEvent: PropTypes.func.isRequired,
    style: PropTypes.object,
    placeHolder: PropTypes.string
};

export default AdvancedTextField;