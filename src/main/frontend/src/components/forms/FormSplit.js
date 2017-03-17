import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import AdvancedTextField from "./../AdvancedTextField";
import ListOfElements from "./../list-of-elements/ListOfElements";

class FormExtract extends Component {

    render() {
        const {formReducer} = this.props;
        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;

        const categories = {
            elements: formReducer.getIn(['extract', 'categories']).toArray(),
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
                <AdvancedTextField placeHolder={"dataset name"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"dataset name"}
                                   fieldName={"dataSetName"}
                                   // value={dataSetName}
                                   // onChangeEvent={this.changeEvent}
                                   // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"description"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"description"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"partition learn"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"partition learn"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"lang"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"lang"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"min text length"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"min text length"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"max text length"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"max text length"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <AdvancedTextField placeHolder={"websites per category"}
                                   pattern={"not_empty"}
                                   type="not_empty"
                                   label={"websites per category"}
                                   fieldName={"description"}
                    // value={dataSetName}
                    // onChangeEvent={this.changeEvent}
                    // style={style.inputField}
                />
                <ListOfElements {...categories}/>


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
