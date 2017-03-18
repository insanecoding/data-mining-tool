import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import RaisedButton from "material-ui/RaisedButton";
import Renderer from "./../Renderer";

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
    }
};

// todo: make separate component: dataSet form
// this class should keep an array of such forms, foreach array elem - render it
class FormExtract extends Component {

    render() {
        const {formReducer} = this.props;
        const {
            onListElementAdd, onListElementDelete, onListElementEdit, onInputFieldChange
        } = this.props.formActions;
        const param = {
            elements: formReducer.getIn(['dataSplit', 'param']).toArray(),
            onListElementAdd: onListElementAdd,
            onListElementDelete: onListElementDelete,
            onListElementEdit: onListElementEdit,
            onInputFieldChange: onInputFieldChange,
            style: style
        };

        // const arr = formReducer.getIn(['dataSplit', 'param']).toArray();
        // const first = arr[0].getIn(['categories']);
        // // console.log(first);
        //
        // const categories = {
        //     elements: first,
        //     title: "Choose categories",
        //     placeholder: "input and press Enter to submit",
        //     whereToSave: ['extract', 'categories'],
        //     onAdd: onListElementAdd,
        //     onEdit: onListElementEdit,
        //     onDelete: onListElementDelete,
        //     listElementStyle: style.listElementWidth
        // };
        // console.log(formReducer.getIn(['dataSplit', 'param', 1, 'categories']).toArray());

        return (
            <GenericForm title={formReducer.getIn(['dataSplit', 'displayName'])}>
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Create" secondary={true}
                                  style={style.buttons}/>
                    <RaisedButton className={"button"} label="Remove" secondary={true}
                                  style={style.buttons}/>
                </div>
                <div>
                    <Renderer {...param}/>
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
