import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import AdvancedTextField from "./../AdvancedTextField";
import RaisedButton from "material-ui/RaisedButton";
import ListOfElements from "./../list-of-elements/ListOfElements";
import {Col, Row} from "react-grid-system";

const style = {
    buttonContainer: {
        textAlign: "center",
        paddingLeft: "20px",
        paddingRight: "20px",
    },
    buttons: {
        padding: "5px",
        margin: "auto auto",
        boxShadow: "none"
    },
    listElementWidth: {
        width: "100%"
    },
    wideInputField: {
        width: "82%"
    },
    mediumInputField: {
        width: "70%"
    },
    smallInputField: {
        width: "68%"
    }
};

// todo: make separate component: dataSet form
// this class should keep an array of such forms, foreach array elem - render it
class FormExtract extends Component {

    render() {
        const {formReducer} = this.props;
        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const arr = formReducer.getIn(['dataSplit', 'param']).toArray();
        const first = arr[0].getIn(['categories']);
        console.log(first);

        const categories = {
            elements: first,
            title: "Choose categories",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'categories'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        return (
            <GenericForm title={formReducer.getIn(['dataSplit', 'displayName'])}>
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Create" secondary={true}
                                  style={style.buttons} />
                    <RaisedButton className={"button"} label="Remove" secondary={true}
                                  style={style.buttons} />
                </div>
                <div>
                    <Row>
                        <Col xs={12} md={7}>
                            <AdvancedTextField label={"dataset name"}
                                               placeHolder={"name"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               fieldName={"dataSetName"}
                                // value={dataSetName}
                                // onChangeEvent={this.changeEvent}
                                               style={style.wideInputField}
                            />
                            <AdvancedTextField label={"description"}
                                               placeHolder={"description"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               fieldName={"description"}
                                // value={dataSetName}
                                // onChangeEvent={this.changeEvent}
                                // style={style.inputField}
                                               style={style.wideInputField}
                            />
                            <AdvancedTextField label={"sites per category"}
                                               placeHolder={"1000"}
                                               pattern={"not_empty"}
                                               type="not_empty"
                                               fieldName={"webSitesPerCategory"}
                                               style={style.wideInputField}
                                // value={dataSetName}
                                // onChangeEvent={this.changeEvent}
                                // style={style.inputField}
                            />

                            <Row>
                                <Col xs={12} md={5}>
                                    <AdvancedTextField placeHolder={"en"}
                                                       pattern={"not_empty"}
                                                       type="not_empty"
                                                       label={"lang"}
                                                       fieldName={"description"}
                                        // value={dataSetName}
                                        // onChangeEvent={this.changeEvent}
                                                       style={style.mediumInputField}
                                    />
                                    <AdvancedTextField placeHolder={"0.8"}
                                                       pattern={"not_empty"}
                                                       type="not_empty"
                                                       label={"learn ratio"}
                                                       fieldName={"description"}
                                        // value={dataSetName}
                                        // onChangeEvent={this.changeEvent}
                                        // style={style.inputField}
                                                       style={style.mediumInputField}
                                    />
                                </Col>

                                <Col xs={12} md={7}>
                                    <AdvancedTextField placeHolder={"200"}
                                                       pattern={"not_empty"}
                                                       type="not_empty"
                                                       label={"min text length"}
                                                       fieldName={"description"}
                                                       style={style.smallInputField}
                                        // value={dataSetName}
                                        // onChangeEvent={this.changeEvent}
                                        //               style={style.inputField}
                                    />
                                    <AdvancedTextField placeHolder={"2000"}
                                                       pattern={"not_empty"}
                                                       type="not_empty"
                                                       label={"max text length"}
                                                       fieldName={"description"}
                                                       style={style.smallInputField}
                                        // value={dataSetName}
                                        // onChangeEvent={this.changeEvent}
                                        //               style={style.inputField}
                                    />
                                </Col>
                            </Row>
                        </Col>

                        <Col xs={12} md={5}>
                            <ListOfElements {...categories}/>
                        </Col>
                    </Row>
                </div>
            </GenericForm>
        )
    }
}

function mapStateToProps(state) {
    return {
        connectionReducer: state.connectionReducer,
        formReducer: state.formReducer
    }
}

function mapDispatchToProps(dispatch) {
    return {
        connectionActions: bindActionCreators(connectionActions, dispatch),
        formActions: bindActionCreators(formActions, dispatch),
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(FormExtract);
