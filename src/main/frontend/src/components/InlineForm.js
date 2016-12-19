import React, {Component} from "react";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";

const style = {
    inputField: {
        width: "80%"
    },
};


export default class InlineForm extends Component {

    render() {
        return (
            <div>
                <TextField
                    hintText="C:\path\to\my\dir\"
                    onBlur={this.props.onBlur}
                    floatingLabelText={"Please, specify working directory"}
                    floatingLabelFixed={true}
                    name="cwd"
                    value={this.props.value}
                    onChange={this.props.onChangeEvent}
                    style={style.inputField}
                />

                { (this.props.isSuccess > 0) ?
                    <FontIcon
                        className="fa fa-check"
                        color={"green"}
                    /> : null
                }


                { (this.props.isSuccess < 0) ?
                    <FontIcon
                        className="fa fa-times"
                        color={"red"}
                    /> : null
                }

            </div>
        )
    }
}

