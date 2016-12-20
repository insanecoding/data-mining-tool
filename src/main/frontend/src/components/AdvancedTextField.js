import React, {Component} from "react";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";

const style = {
    inputField: {
        width: "80%"
    },
};


export default class AdvancedTextField extends Component {

    render() {
        return (
            <div>
                <TextField
                    hintText={this.props.hint}
                    onBlur={this.props.onBlur}
                    floatingLabelText={this.props.label}
                    floatingLabelFixed={true}
                    name={this.props.name}
                    value={this.props.value}
                    onChange={this.props.onChangeEvent}
                    style={style.inputField}
                />

                { (this.props.validationSuccess > 0) ?
                    <FontIcon
                        className="fa fa-check"
                        color={"green"}
                    /> : null
                }


                { (this.props.validationSuccess < 0) ?
                    <FontIcon
                        className="fa fa-times"
                        color={"red"}
                    /> : null
                }

            </div>
        )
    }
}

