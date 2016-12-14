import React, {Component} from "react";
import Subheader from "material-ui/Subheader";
import {List, ListItem} from "material-ui/List";
import TextField from "material-ui/TextField";
import {Col} from "react-grid-system";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";


class RightSideForm extends Component {
    render() {
        return(
            <div>
                <TextField
                    hintText="Hint Text"
                    floatingLabelText="Username"
                    floatingLabelFixed={true}
                    value={this.props.userName}
                    onChange={ () => this.props.formHandler("userName")}
                />
                <TextField
                    hintText="Hint Text 2"
                    floatingLabelText="Password"
                    floatingLabelFixed={true}
                    type="password"
                    value={this.props.password}
                    onChange={() => this.props.formHandler("password")}
                />
                <TextField
                    hintText="Hint Text 2"
                    floatingLabelText="Database name"
                    floatingLabelFixed={true}
                    value={this.props.dbName}
                    onChange={() => this.props.formHandler("dbName")}
                />
                <TextField
                    hintText="Hint Text 2"
                    floatingLabelText="Port"
                    floatingLabelFixed={true}
                    value={this.props.port}
                    onChange={ () => this.props.formHandler("port")}
                />

                <List>
                    <Subheader inset={true}>Blacklists</Subheader>
                    <Col xs={2}>
                        <IconButton tooltip="Add item">
                            <FontIcon className="fa fa-plus"/>
                        </IconButton>
                    </Col>
                    <Col xs={10}>
                        <ListItem primaryText={"Some blacklist #1"} rightIconButton={
                            <IconButton tooltip="Remove item">
                                <FontIcon className="fa fa-minus"/>
                            </IconButton>}
                        />
                        <ListItem primaryText={"Some blacklist #2"} rightIconButton={
                            <IconButton tooltip="Remove item">
                                <FontIcon className="fa fa-minus"/>
                            </IconButton>}
                        />
                    </Col>
                </List>
            </div>
        )
    }
}

export default RightSideForm;