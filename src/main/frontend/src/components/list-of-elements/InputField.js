import React, {Component, PropTypes} from "react";
import TextField from "material-ui/TextField";

class TodoTextInput extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            text: this.props.text || ''
        };
    }

    handleEnter(e) {
        if (e.keyCode === 13) {
            const text = e.target.value.trim();
            this.props.onSave(text);
            if (this.props.newElement) {
                this.setState({text: ''});
            }
        }
    }

    handleChange(e) {
        this.setState({text: e.target.value});
    }

    handleBlur(e) {
        if (!this.props.newElement) {
            this.props.onSave(e.target.value);
        }
    }

    render() {
        return (
            <div>
                <TextField onKeyDown={this.handleEnter.bind(this)}
                           id='new-todo-input'
                           style={this.props.style}
                           type="text"
                           hintText={this.props.placeholder}
                           value={this.state.text}
                           onBlur={this.handleBlur.bind(this)}
                           onChange={this.handleChange.bind(this)}
                />
            </div>
        );
    }
}

TodoTextInput.propTypes = {
    onSave: PropTypes.func.isRequired,
    text: PropTypes.string,
    placeholder: PropTypes.string,
    editing: PropTypes.bool,
    newElement: PropTypes.bool,
    style: PropTypes.object
};

export default TodoTextInput;
