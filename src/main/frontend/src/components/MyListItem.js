import React, {Component} from "react";
import IconButton from "material-ui/IconButton";
import FontIcon from "material-ui/FontIcon";
import {ListItem} from "material-ui/List";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";
import Toggle from "material-ui/Toggle";

class MyListItem extends Component {
    render() {
        return (
            <ListItem leftCheckbox={<Toggle/>} primaryText={this.props.text} rightIconButton={
                <IconButton>
                    <FontIcon className="fa fa-bars fa-fw"/>
                </IconButton>
            }/>
        )
    }
}
export default MyListItem;
