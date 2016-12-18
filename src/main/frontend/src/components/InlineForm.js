import React, {Component} from "react";
import FlatButton from "material-ui/FlatButton";
import TextField from "material-ui/TextField";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";
import FontIcon from "material-ui/FontIcon";

const style = {
    fileChooser: {
        cursor: 'pointer',
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        width: '100%',
        opacity: 0,
    }
};

export default class InlineForm extends Component {
    render() {
        return(
            <div {...this.props}>
                <TextField
                    hintText="Press 'Choose' to set directory..."
                    floatingLabelText={"Working directory"}
                    floatingLabelFixed={true}
                />
                <FlatButton label="Choose" labelPosition="before" icon={<FontIcon className="fa fa-search"/>}>
                    <input type="file" style={style.fileChooser}/>
                </FlatButton>
            </div>
        )
    }
}
