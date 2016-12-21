import React, {Component} from "react";
import IconButton from "material-ui/IconButton";
import FontIcon from "material-ui/FontIcon";
import {ListItem} from "material-ui/List";
import Toggle from "material-ui/Toggle";

export default class MyListItem extends Component {
    render() {
        return (
            <ListItem leftCheckbox={
                <Toggle onToggle={this.props.onToggle}/>  }
                      primaryText={this.props.text} rightIconButton={
                <IconButton onClick={this.props.handleClick}>
                    <FontIcon className="fa fa-bars fa-fw"/>
                </IconButton>
            }/>
        )
    }
}
