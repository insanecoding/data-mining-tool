import React, {Component, PropTypes} from "react";
import InputField from "./InputField";
import {ListItem, IconButton, IconMenu, MenuItem} from "material-ui";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";

class MyListItem extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            editing: false
        };
    }

    handleEdit = () => {
        this.setState({editing: true});
    };

    handleDelete = (element, whereToSeek) => {
        this.props.onDelete(element.key, whereToSeek)
    };

    handleSave = (element, text, whereToSeek) => {
        if (text.length === 0) {
            this.props.onDelete(element.key, whereToSeek);
        } else {
            this.props.onEdit(element.key, text, whereToSeek);
        }
        this.setState({editing: false});
    };

    render() {
        const {element, whereToSeek} = this.props;

        const rightIconMenu = (
            <IconMenu iconButtonElement={
                <IconButton>
                    <MoreVertIcon/>
                </IconButton>
            }
            >
                <MenuItem primaryText="Edit" onTouchTap={this.handleEdit}/>
                <MenuItem primaryText="Delete" onTouchTap={ () => this.handleDelete(element, whereToSeek) }/>
            </IconMenu>
        );

        let nodeElement;
        if (this.state.editing) {
            nodeElement = (
                <InputField text={element.name}
                            editing={this.state.editing}
                            onSave={(text) => this.handleSave(element, text, whereToSeek)}/>
            );
        } else {
            nodeElement = (
                <ListItem primaryText={element.name} rightIconButton={rightIconMenu}/>
            );
        }

        return (
            <div>
                {nodeElement}
            </div>
        );
    }
}

MyListItem.propTypes = {
    element: PropTypes.object.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
    whereToSeek: PropTypes.array.isRequired
};

export default MyListItem;
