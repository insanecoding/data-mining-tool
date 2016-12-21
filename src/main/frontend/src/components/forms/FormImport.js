import React, {PropTypes} from "react";
import Subheader from "material-ui/Subheader";
import {List, ListItem} from "material-ui/List";
import {Col, Row} from "react-grid-system";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";


const RightSideForm_ImportList = ({title}) => {

    return(
        <GenericForm title={title}>
            <Row>
                <Col xs={12} md={6}>
                    <AdvancedTextField hint="postgres"
                                       label={"user name"}
                                       name={"userName"}
                                       pattern={"not_empty"}
                    />
                    <AdvancedTextField hint="password"
                                       label={"password"}
                                       name={"password"}
                                       pattern={"not_empty"}
                    />
                </Col>
                <Col xs={12} md={6}>
                    <AdvancedTextField hint="database name"
                                       label={"db name"}
                                       name={"dbName"}
                                       pattern={"not_empty"}
                    />
                    <AdvancedTextField hint="5432"
                                       label={"port"}
                                       name={"port"}
                                       pattern={"number"}
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
};


export default RightSideForm_ImportList;