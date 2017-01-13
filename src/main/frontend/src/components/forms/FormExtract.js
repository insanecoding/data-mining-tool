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
import CheckBoxReplacement from "./../CheckBoxReplacement";

const style = {
    listElementWidth: {
        width: "80%"
    },
    hasMargin: {
        margin: "10px"
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
        // console.log("I was checked");
    };

    render() {

        const {onListElementAdd, onListElementDelete, onListElementEdit} = this.props.formActions;
        const {formReducer} = this.props;

        const tagsWithText = {
            elements: createElements(formReducer, ['extract', 'tagsWithText']),
            title: "Tags",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'tagsWithText'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const categories = {
            elements: createElements(formReducer, ['extract', 'categories']),
            title: "Categories",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'categories'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const tagsToSkip = {
            elements: createElements(formReducer, ['extract', 'tagsToSkip']),
            title: "Tags to skip",
            placeholder: "input and press Enter to submit",
            whereToSave: ['extract', 'tagsToSkip'],
            onAdd: onListElementAdd,
            onEdit: onListElementEdit,
            onDelete: onListElementDelete,
            listElementStyle: style.listElementWidth
        };

        const {isTextMain, isTextFromTags, isNgrams, isTagStat, maxNGramSize} =
            formReducer.getIn(['extract']).toObject();

        return (
            <GenericForm title={formReducer.getIn(['extract', 'displayName'])}>

                <Row>
                    <Col xs={12} md={6}>
                        <AdvancedTextField placeHolder={"max nGram size"}
                                           pattern={"number"}
                                           type="number"
                                           label={"maximum NGram size"}
                                           fieldName={"maxNGramSize"}
                                           value={maxNGramSize}
                                           onChangeEvent={this.changeEvent}
                        />
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"text main"} name={"isTextMain"}
                                                 value={isTextMain} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"text from tags"} name={"isTextFromTags"}
                                                 value={isTextFromTags} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"Ngrams"} name={"isNgrams"}
                                                 value={isNgrams} onCheck={this.onCheck}/>
                        </div>
                        <div style={style.hasMargin}>
                            <CheckBoxReplacement label={"tag statistics"} value={isTagStat}
                                                 name={"isTagStat"} onCheck={this.onCheck}/>
                        </div>
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...categories}/>
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...tagsWithText}/>
                    </Col>
                    <Col xs={12} md={6}>
                        <ListOfElements {...tagsToSkip}/>
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
