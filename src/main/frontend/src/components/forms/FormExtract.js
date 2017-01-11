import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import ListOfElements from "./../list-of-elements/ListOfElements";
import {Col, Row} from "react-grid-system";
import AdvancedTextField from "./../AdvancedTextField";
import {onValueChange, createElements} from "./../../util/misc";

const style = {
    listElementWidth: {
        width: "80%"
    }
};

class FormExtract extends Component {

    changeEvent = (e) => {
        onValueChange (e, "extract", this.props.formActions.onInputChange);
    };

    render() {

        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;

        const listOfElementsParam = {
            elements: createElements(formReducer, ['extract', 'tagsWithText']),
            title: "Tags",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'tagsWithText'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const listOfElementsParam2 = {
            elements: createElements(formReducer, ['extract', 'categories']),
            title: "Categories",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'categories'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const {maxNGramSize} = formReducer.getIn(['extract']).toObject();

        return (
            <GenericForm title={formReducer.getIn(['extract', 'displayName'])}>

                <Row>
                    <Col xs={12} md={12}>
                        <AdvancedTextField placeHolder={"port number"}
                                           pattern={"number"}
                                           type="number"
                                           label={"maximum NGram size"}
                                           fieldName={"maxNGramSize"}
                                           value={maxNGramSize}
                                           onChangeEvent={this.changeEvent}
                        />
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...listOfElementsParam}/>
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...listOfElementsParam2}/>
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

export default connect(mapStateToProps, mapDispatchToProps)(FormExtract);