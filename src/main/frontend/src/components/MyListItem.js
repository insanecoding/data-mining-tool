import React, {Component} from "react";
import IconButton from "material-ui/IconButton";
import FontIcon from "material-ui/FontIcon";
import {ListItem} from "material-ui/List";
import ToggleReplacement from "./ToggleReplacement";

export default class MyLeftListItem extends Component {
    render() {
        return (
            <ListItem leftCheckbox={
                <ToggleReplacement onToggle={this.props.onToggle} value={this.props.value}/>  }
                      primaryText={this.props.text} rightIconButton={
                <IconButton onClick={this.props.handleClick}>
                    <FontIcon className="fa fa-bars fa-fw"/>
                </IconButton>
            }/>
        )
    }
}
