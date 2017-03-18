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

class FormExtract extends Component {

    amIDisabled = () => {
        const arr = this.props.formReducer.getIn(['dataSplit', 'param']).toArray();
        return arr.length === 0;
    };

    handleClick = (buttonName) => {
        if (buttonName === "remove") {
            const {removeLast} = this.props.formActions;
            removeLast(['dataSplit', 'param']);
        } else if (buttonName === "create") {
            const {addNew} = this.props.formActions;
            const newElem = {
                dataSetName: "set_0",
                description: "some data set",
                categories: ["adult", "alcohol"],
                partitionLearn: 0.8,
                lang: "en",
                minTextLength: 500,
                maxTextLength: 5000,
                websitesPerCategory: 1000
            };
            addNew(newElem, ['dataSplit', 'param']);
        }
    };

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

        return (
            <GenericForm title={formReducer.getIn(['dataSplit', 'displayName'])}>
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Create" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("create") }/>
                    <RaisedButton className={"button"} label="Remove" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("remove") }
                                  disabled={this.amIDisabled()}/>
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
