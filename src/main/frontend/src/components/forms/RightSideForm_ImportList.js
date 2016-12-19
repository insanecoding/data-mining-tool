import React, {Component} from "react";
import Subheader from "material-ui/Subheader";
import {List, ListItem} from "material-ui/List";
import TextField from "material-ui/TextField";
import {Col, Row} from "react-grid-system";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";


export default class RightSideForm_ImportList extends Component {
    render() {
        return(
            <div>
                <h1>Import blacklist</h1>
                <Row>
                    <Col xs={12}>
                        <TextField
                            hintText="Hint Text"
                            floatingLabelText="Username"
                            name="userName"
                            floatingLabelFixed={true}
                            onChange={this.props.formHandler}
                        />
                        <TextField
                            hintText="Hint Text 2"
                            floatingLabelText="Password"
                            floatingLabelFixed={true}
                            type="password"
                            name="password"
                            onChange={this.props.formHandler}
                        />
                        <TextField
                            hintText="Hint Text 2"
                            floatingLabelText="Database name"
                            floatingLabelFixed={true}
                            name="dbName"
                            onChange={this.props.formHandler}
                        />
                        <TextField
                            hintText="Hint Text 2"
                            floatingLabelText="Port"
                            floatingLabelFixed={true}
                            name="port"
                            onChange={this.props.formHandler}
                        />
                    </Col>
                </Row>

                <List>
                    <Subheader inset={true}>Blacklists</Subheader>
                    <Row>
                    <Col md={2} xs={6}>
                        <IconButton tooltip="Add item">
                            <FontIcon className="fa fa-plus"/>
                        </IconButton>
                    </Col>
                    <Col md={10} xs={12}>
                        <ListItem primaryText={"Some blacklist #1"}
                                  rightIconButton={<IconButton tooltip="Remove item">
                                          <FontIcon className="fa fa-minus"/>
                                      </IconButton>
                                  }

                        />
                        <ListItem primaryText={"Some blacklist #2"}
                                  rightIconButton={
                                          <IconButton tooltip="Remove item">
                                                <FontIcon className="fa fa-minus"/>
                                          </IconButton>
                                      }
                        />
                    </Col>
                    </Row>
                </List>
            </div>
        )
    }
}