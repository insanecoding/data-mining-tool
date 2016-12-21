import React, {PropTypes} from "react";
import Subheader from "material-ui/Subheader";
import {List, ListItem} from "material-ui/List";
import {Col, Row} from "react-grid-system";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";


const RightSideForm_ImportList = ({formName, title, userName, password, dbName, port, onInputChange}) => {

    const changeEvent = (e) => {
        onInputChange(e.target.value, e.target.name, formName, "data");
    };

    return(
        <GenericForm title={title}>
            <Row>
                <Col xs={12} md={6}>
                    <AdvancedTextField placeHolder="user name"
                                       pattern={"not_empty"}
                                       label={"user name"}
                                       fieldName={"userName"}
                                       value={userName}
                                       onChangeEvent={changeEvent}
                    />
                    <AdvancedTextField placeHolder="password"
                                       type={"password"}
                                       pattern={"not_empty"}
                                       label={"password"}
                                       fieldName={"password"}
                                       value={password}
                                       onChangeEvent={changeEvent}
                    />
                </Col>
                <Col xs={12} md={6}>
                    <AdvancedTextField placeHolder="database name"
                                       pattern={"not_empty"}
                                       label={"db name"}
                                       fieldName={"dbName"}
                                       value={dbName}
                                       onChangeEvent={changeEvent}
                    />
                    <AdvancedTextField placeHolder="port number"
                                       pattern={"number"}
                                       label={"port number"}
                                       fieldName={"port"}
                                       value={port}
                                       onChangeEvent={changeEvent}
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
                        </IconButton>}/>

                        <ListItem primaryText={"Some blacklist #1"}
                                  rightIconButton={<IconButton tooltip="Remove item">
                                      <FontIcon className="fa fa-minus"/>
                                  </IconButton>}/>
                    </Col>
                </Row>
            </List>
        </GenericForm>
    );
};

RightSideForm_ImportList.propTypes = {
    title: PropTypes.string.isRequired,
    formName: PropTypes.string.isRequired,
    userName: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    dbName: PropTypes.string.isRequired,
    port: PropTypes.number.isRequired,
    onInputChange: PropTypes.func.isRequired,
};

export default RightSideForm_ImportList;