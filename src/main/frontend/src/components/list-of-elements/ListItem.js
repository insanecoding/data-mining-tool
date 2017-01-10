import React, {Component, PropTypes} from "react";
// import classnames from 'classnames';
import InputField from "./InputField";
import {ListItem, IconButton, IconMenu, MenuItem} from "material-ui";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";

class TodoItem extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            editing: false
        };
    }

    handleEdit = () => {
        this.setState({editing: true});
    };

    handleSave = (id, text) => {
        if (text.length === 0) {
            this.props.onDelete(id);
        } else {
            this.props.onEdit(id, text);
        }
        this.setState({editing: false});
    };

    render() {
        const {todo, completeTodo, onDelete} = this.props;

        const rightIconMenu = (
            <IconMenu iconButtonElement={
                <IconButton>
                    <MoreVertIcon/>
                </IconButton>
            }
            >
                <MenuItem primaryText="Edit" onTouchTap={this.handleEdit}/>
                <MenuItem primaryText="Delete" onTouchTap={() => onDelete(todo.id)}/>
            </IconMenu>
        );

        let element;
        if (this.state.editing) {
            element = (
                <InputField text={todo.text}
                            editing={this.state.editing}
                            onSave={(text) => this.handleSave(todo.id, text)}/>
            );
        } else {
            element = (
                <ListItem primaryText={todo.text}
                          onTouchTap={() => completeTodo(todo.id)}
                          rightIconButton={rightIconMenu}
                />
            );
        }

        return (
            <div
                {/*className={classnames({*/}
                {/*completed: todo.completed,*/}
                {/*editing: this.state.editing*/}
            {/*})}*/}
            >
                {element}
            </div>
        );
    }
}

TodoItem.propTypes = {
    todo: PropTypes.object.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
    completeTodo: PropTypes.func.isRequired
};

export default TodoItem;
