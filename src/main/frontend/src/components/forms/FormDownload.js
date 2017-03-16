import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import {Col, Row} from "react-grid-system";
import AdvancedTextField from "./../AdvancedTextField";
import ListOfElements from "./../list-of-elements/ListOfElements";
import {onValueChange} from "./../../util/misc";
import {RadioButton, RadioButtonGroup} from "material-ui/RadioButton";

const style = {
    listElementWidth: {
        width: "80%"
    },
    hasMargin: {
        margin: "10px"
    },
    h2withoutMargin: {
        marginTop: "20px",
        marginBottom: "0px"
    }
};

class FormFeatures extends Component {

    onRadioChange = (e) => {
        const target = e.target.value;
        console.log(target);
        this.props.formActions.onRadioChange(target, ['download', 'categoriesRadio']);
    };

    whoIsSelected = () => {
        const me = this.props.formReducer.getIn(['download', 'categoriesRadio']).toObject();
        // the only one key has true value - return it
        const foo = Object.keys(me).filter(k => me[k] === true);
        return foo.toString();
    };

    changeEvent = (e) => {
        onValueChange(e, "download", this.props.formActions.onInputChange);
    };

    render() {
        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;

        const displayName = formReducer.getIn(['download', 'displayName']);
        const {downloadsPerCategory, connectTimeout, readTimeout, threadsNumber} =
            formReducer.getIn(['download']).toObject();

        const categories = {
            elements: formReducer.getIn(['download', 'categories']).toArray(),
            title: "Choose categories",
            placeholder: "input and press Enter to submit",
            whereToSave: ['download', 'categories'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const hideCustomCategories =
            formReducer.getIn(['download', 'categoriesRadio', 'isAll']);

        return (
            <GenericForm title={displayName}>
                <Row>
                    <Col xs={12} md={6}>
                        <h2>
                            Categories
                        </h2>
                        <RadioButtonGroup name="categories" defaultSelected={this.whoIsSelected()}
                                          onChange={this.onRadioChange}>
                            <RadioButton
                                value="isAll"
                                label="All with >1000 websites"
                                style={style.hasMargin}
                            />
                            <RadioButton
                                value="isCustom"
                                label="Custom"
                                style={style.hasMargin}
                            />
                        </RadioButtonGroup>
                        { hideCustomCategories ? null : <ListOfElements {...categories}/> }
                    </Col>
                    <Col xs={12} md={6}>
                        <h2 style={style.h2withoutMargin}>
                            Download settings
                        </h2>
                        <AdvancedTextField placeHolder="downloads per category"
                                           pattern={"number"}
                                           type={"number"}
                                           label={"downloads per category"}
                                           fieldName={"downloadsPerCategory"}
                                           value={downloadsPerCategory}
                                           onChangeEvent={this.changeEvent}
                        />
                        <AdvancedTextField placeHolder="connect timeout"
                                           type={"number"}
                                           pattern={"number"}
                                           label={"connect timeout"}
                                           fieldName={"connectTimeout"}
                                           value={connectTimeout}
                                           onChangeEvent={this.changeEvent}
                        />
                        <AdvancedTextField placeHolder="read timeout"
                                           pattern={"number"}
                                           type={"number"}
                                           label={"read timeout"}
                                           fieldName={"readTimeout"}
                                           value={readTimeout}
                                           onChangeEvent={this.changeEvent}
                        />
                        <AdvancedTextField placeHolder="threads number"
                                           pattern={"number"}
                                           type={"number"}
                                           label={"threads number"}
                                           fieldName={"threadsNumber"}
                                           value={threadsNumber}
                                           onChangeEvent={this.changeEvent}
                        />
                    </Col>
                </Row>
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

export default connect(mapStateToProps, mapDispatchToProps)(FormFeatures);