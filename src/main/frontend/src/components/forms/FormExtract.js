import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import ListOfElements from "./../list-of-elements/ListOfElements";
import {Col, Row} from "react-grid-system";
import AdvancedTextField from "./../AdvancedTextField";
import {onValueChange} from "./../../util/misc";
import CheckBoxReplacement from "./../CheckBoxReplacement";
import {RadioButton, RadioButtonGroup} from "material-ui/RadioButton";

const style = {
    listElementWidth: {
        width: "80%"
    },
    hasMargin: {
        margin: "10px"
    },
    inputField: {
        marginLeft: "13px",
        marginRight: "13px",
        width: "65%"
    },
    h2withoutMargin: {
        marginTop: "20px",
        marginBottom: "0px"
    }
};

class FormExtract extends Component {

    changeEvent = (e) => {
        onValueChange(e, "extract", this.props.formActions.onInputChange);
    };

    onCheck = (e) => {
        const {onCheck} = this.props.formActions;
        let path = ['extract'];
        path.push(e.target.name);
        onCheck(path);
    };


    onRadioChange = (e) => {
        const target = e.target.value;
        console.log(target);
        this.props.formActions.onRadioChange(target, ['extract', 'categoriesRadio']);
    };

    whoIsSelected = () => {
        const me = this.props.formReducer.getIn(['extract', 'categoriesRadio']).toObject();
        // the only one key has true value - return it
        const foo = Object.keys(me).filter(k => me[k] === true);
        return foo.toString();
    };

    render() {

        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;

        const tagsWithText = {
            elements: formReducer.getIn(['extract', 'tagsWithText']).toArray(),
            title: "Tags",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'tagsWithText'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

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

        const tagsToSkip = {
            elements: formReducer.getIn(['extract', 'tagsToSkip']).toArray(),
            title: "Tags to skip",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'tagsToSkip'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const {isTextMain, isTextFromTags, isNgrams, isTagStat, maxNGramSize, defaultSkipTags, defaultTextTags} =
            formReducer.getIn(['extract']).toObject();

        const hideCustomCategories =
            formReducer.getIn(['extract', 'categoriesRadio', 'isAll']);

        return (
            <GenericForm title={formReducer.getIn(['extract', 'displayName'])}>

                <Row>
                    <Col xs={12} md={6}>
                        <h2>
                            Available modes
                        </h2>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"text main"} name={"isTextMain"}
                                                 value={isTextMain} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"text from tags"} name={"isTextFromTags"}
                                                 value={isTextFromTags} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"tag statistics"} value={isTagStat}
                                                 name={"isTagStat"} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"Ngrams"} name={"isNgrams"}
                                                 value={isNgrams} onCheck={this.onCheck}/>
                        </div>
                        <h2>
                            Categories
                        </h2>
                        <RadioButtonGroup name="categories" defaultSelected={this.whoIsSelected()}
                                          onChange={this.onRadioChange}>
                            <RadioButton
                                value="isAll"
                                label="All with >1000 texts"
                                style={style.hasMargin}
                            />
                            <RadioButton
                                value="isCustom"
                                label="Custom"
                                style={style.hasMargin}
                            />
                        </RadioButtonGroup>

                        { hideCustomCategories ? null : <ListOfElements {...categories}/> }
                        { defaultSkipTags ? null : <ListOfElements {...tagsToSkip} /> }
                    </Col>
                    <Col xs={12} md={6}>

                        <h2 style={style.h2withoutMargin}>
                            Extra parameters
                        </h2>
                        <AdvancedTextField placeHolder={"max nGram size"}
                                           pattern={"number"}
                                           type="number"
                                           label={"maximum NGram size"}
                                           fieldName={"maxNGramSize"}
                                           value={maxNGramSize}
                                           onChangeEvent={this.changeEvent}
                                           style={style.inputField}
                        />
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"default tags with text"} name={"defaultTextTags"}
                                                 value={defaultTextTags} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"default tags to skip"} name={"defaultSkipTags"}
                                                 value={defaultSkipTags} onCheck={this.onCheck}/>
                        </div>
                        { defaultTextTags ? null : <ListOfElements {...tagsWithText} /> }
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
