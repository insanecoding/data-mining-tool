import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import {Col, Row} from "react-grid-system";
import AdvancedTextField from "./../AdvancedTextField";
import {onValueChange} from "./../../util/misc";
import ListOfElements from "./../list-of-elements/ListOfElements";

const style = {
    listElementWidth: {
        width: "80%"
    }
};

class FormSchemes extends Component {


    changeEvent = (e) => {
        onValueChange(e, "schemes", this.props.formActions.onInputChange);
    };

    render() {
        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;
        const {displayName, rapidMinerPath, templatesFolder} =
            formReducer.getIn(['schemes']).toObject();

        const experiments = {
            elements: formReducer.getIn(['schemes', 'experiments']).toArray(),
            title: "Add experiments",
            placeholder: "input and press Enter to submit",
            whereToSave: ['schemes', 'experiments'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        return (
            <GenericForm title={displayName}>
                <Row>
                    <Col xs={12} md={6}>
                        <AdvancedTextField label={"RapidMiner path"}
                                           placeHolder={" "}
                                           pattern={"path"}
                                           type="not_empty"
                                           fieldName={"rapidMinerPath"}
                                           value={rapidMinerPath}
                                           onChangeEvent={this.changeEvent}
                                           style={{width: "82%"}}
                        />
                        <AdvancedTextField label={"templates subfolder"}
                                           placeHolder={" "}
                                           pattern={"path"}
                                           type="not_empty"
                                           fieldName={"templatesFolder"}
                                           value={templatesFolder}
                                           onChangeEvent={this.changeEvent}
                                           style={{width: "82%"}}
                        />
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...experiments}/>
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

export default connect(mapStateToProps, mapDispatchToProps)(FormSchemes);
