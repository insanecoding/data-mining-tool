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

const style = {
    listElementWidth: {
        width: "80%"
    }
};

class FormFeatures extends Component {

    changeEvent = (e) => {
        onValueChange (e, "download", this.props.formActions.onInputChange);
    };

    render() {
        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;

        const displayName = formReducer.getIn(['download', 'displayName']);
        const {downloadsPerCategory, connectTimeout, readTimeout, threadsNumber} =
            formReducer.getIn(['download']).toObject();

        const categories = {
            elements: formReducer.getIn(['download', 'categories']).toArray(),
            title: "Categories",
            placeholder: "input and press Enter to submit",
            whereToSave: ['download', 'categories'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        return(
            <GenericForm title={displayName}>
                <Row>
                    <Col xs={12} md={6}>
                        <ListOfElements {...categories}/>
                    </Col>
                    <Col xs={12} md={6}>
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