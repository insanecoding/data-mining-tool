import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import AdvancedTextField from "./../AdvancedTextField";
import RaisedButton from "material-ui/RaisedButton";
import ExperimentRenderer from "./../ExperimentRenderer";

const style = {
    wideInputField: {
        width: "95%"
    },
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

class FormPrepare extends Component {

    valueChanged = (e) => {
        this.props.formActions.onInputFieldChange(e.target.value, ['prepare', 'stopWordsPath']);
    };

    amIDisabled = () => {
        const arr = this.props.formReducer.getIn(['prepare', 'experiments']).toArray();
        return arr.length === 0;
    };

    handleClick = (buttonName) => {
        if (buttonName === "remove") {
            const {removeLast} = this.props.formActions;
            removeLast(['prepare', 'experiments']);
        } else if (buttonName === "create") {
            const {addNew} = this.props.formActions;
            const newElem = {
                name: "exp_1",
                description: "text experiment 1",
                dataSetName: "set_1",
                mode: "text_main",
                type: "binomial",
                IDF_Treshold: 0.001,
                IDF_Type: "M",
                TF_Type: "S",
                tagName: "",
                nGramSize: "",
                featuresByCategory: 50,
                roundToDecimalPlaces: "",
                normalizeRatio: "",
                experiments: []
            };
            addNew(newElem, ['prepare', 'experiments']);
        }
    };

    render() {
        const {formReducer} = this.props;

        const {
            onListElementAdd, onListElementDelete, onListElementEdit, onInputFieldChange
        } = this.props.formActions;

        const param = {
            elements: formReducer.getIn(['prepare', 'experiments']).toArray(),
            onListElementAdd: onListElementAdd,
            onListElementDelete: onListElementDelete,
            onListElementEdit: onListElementEdit,
            onInputFieldChange: onInputFieldChange,
            style: style
        };

        const {stopWordsPath} =
            formReducer.getIn(['prepare']).toObject();

        return (
            <GenericForm title={formReducer.getIn(['prepare', 'displayName'])}>
                <AdvancedTextField placeHolder={"\\stop\\words\\file.dat"}
                                   pattern={"path"}
                                   label={"stopwords path"}
                                   fieldName={"stopwords"}
                                   value={stopWordsPath}
                                   onChangeEvent={this.valueChanged}
                                   style={style.wideInputField}
                />
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Create" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("create") }/>
                    <RaisedButton className={"button"} label="Remove" secondary={true}
                                  style={style.buttons} onClick={ () => this.handleClick("remove") }
                                  disabled={this.amIDisabled()}/>
                </div>
                
                <ExperimentRenderer {...param} />

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

export default connect(mapStateToProps, mapDispatchToProps)(FormPrepare);