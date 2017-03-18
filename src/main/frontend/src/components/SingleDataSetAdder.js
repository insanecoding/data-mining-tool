import React, {PropTypes} from "react";
import ListOfElements from "./list-of-elements/ListOfElements";
import AdvancedTextField from "./AdvancedTextField";
import {Col, Row} from "react-grid-system";
import Divider from "material-ui/Divider";

const style = {
    wideInputField: {
        width: "82%"
    },
    mediumInputField: {
        width: "70%"
    },
    smallInputField: {
        width: "68%"
    },
    divider: {
        margin: "20px"
    }
};

const SingleDataSetAdder = ({categories, inputFields}) => {
    return (
        <div>
            <Row>
                <Col xs={12} md={7}>
                    <AdvancedTextField label={"dataset name"}
                                       placeHolder={"name"}
                                       pattern={"not_empty"}
                                       type="not_empty"
                                       fieldName={"dataSetName"}
                                       value={inputFields.dataSetName}
                                       onChangeEvent={inputFields.changeEvent}
                                       style={style.wideInputField}
                    />
                    {/* (inputFields.key) => inputFields.changeEvent */}
                    <AdvancedTextField label={"description"}
                                       placeHolder={"description"}
                                       pattern={"not_empty"}
                                       type="not_empty"
                                       fieldName={"description"}
                                       value={inputFields.description}
                                       onChangeEvent={inputFields.changeEvent}
                                       style={style.wideInputField}
                    />
                    <AdvancedTextField label={"sites per category"}
                                       placeHolder={"1000"}
                                       pattern={"not_empty"}
                                       type="not_empty"
                                       fieldName={"websitesPerCategory"}
                                       style={style.wideInputField}
                                       value={inputFields.websitesPerCategory}
                                       onChangeEvent={inputFields.changeEvent}
                    />

                    <Row>
                        <Col xs={12} md={5}>
                            <AdvancedTextField placeHolder={"en"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               label={"lang"}
                                               fieldName={"lang"}
                                               value={inputFields.lang}
                                               onChangeEvent={inputFields.changeEvent}
                                               style={style.mediumInputField}
                            />
                            <AdvancedTextField placeHolder={"0.8"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               label={"learn ratio"}
                                               fieldName={"partitionLearn"}
                                               value={inputFields.partitionLearn}
                                               onChangeEvent={inputFields.changeEvent}
                                               style={style.mediumInputField}
                            />
                        </Col>

                        <Col xs={12} md={7}>
                            <AdvancedTextField placeHolder={"200"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               label={"min text length"}
                                               fieldName={"minTextLength"}
                                               style={style.smallInputField}
                                               value={inputFields.minTextLength}
                                               onChangeEvent={inputFields.changeEvent}
                            />
                            <AdvancedTextField placeHolder={"2000"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               label={"max text length"}
                                               fieldName={"maxTextLength"}
                                               style={style.smallInputField}
                                               value={inputFields.maxTextLength}
                                               onChangeEvent={inputFields.changeEvent}
                            />
                        </Col>
                    </Row>
                </Col>

                <Col xs={12} md={5}>
                    <ListOfElements {...categories}/>
                </Col>
            </Row>
            <Row>
                <Col xs={12} md={12}>
                    <Divider style={style.divider}/>
                </Col>
            </Row>

        </div>
    );
};

SingleDataSetAdder.propTypes = {
    categories: PropTypes.object.isRequired,
    inputFields: PropTypes.object.isRequired,
};

export default SingleDataSetAdder;
