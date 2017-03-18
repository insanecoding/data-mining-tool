import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";
import AdvancedTextField from "./../AdvancedTextField";
import {Col, Row} from "react-grid-system";
import {RadioButton, RadioButtonGroup} from "material-ui/RadioButton";
import RaisedButton from "material-ui/RaisedButton";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";

const style = {
    wideInputField: {
        width: "95%"
    },
    oneOfTwoInputFields: {
        width: "90%"
    },
    oneOfFourInputFields: {
        width: "78%"
    },
    hasMargin: {
        margin: "7px"
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
};

class FormPrepare extends Component {

    render() {
        const {formReducer} = this.props;

        return (
            <GenericForm title={formReducer.getIn(['prepare', 'displayName'])}>
                <AdvancedTextField placeHolder={"\\stop\\words\\file.dat"}
                                   pattern={"path"}
                                   label={"stopwords path"}
                                   fieldName={"stopwords"}
                    // value={maxNGramSize}
                    // onChangeEvent={this.changeEvent}
                                   style={style.wideInputField}
                />
                <div style={style.buttonContainer}>
                    <RaisedButton className={"button"} label="Create" secondary={true}
                                  style={style.buttons}/>
                    <RaisedButton className={"button"} label="Remove" secondary={true}
                                  style={style.buttons}/>
                </div>
                <h2>
                    New Experiment
                </h2>
                <Row>
                    <Col xs={12} md={6}>
                        <Row>
                            <Col xs={12} md={5}>
                                <AdvancedTextField placeHolder={""}
                                                   pattern={"path"}
                                                   label={"name"}
                                                   fieldName={"stopwords"}
                                    // value={maxNGramSize}
                                    // onChangeEvent={this.changeEvent}
                                                   style={{width: "71%"}}
                                />
                            </Col>
                            <Col xs={12} md={7}>
                                <SelectField
                                    floatingLabelText="Mode"
                                    value={"text_main"}
                                    onChange={this.handleChange}
                                    style={{width: "90%"}}
                                >
                                    <MenuItem value={"text_main"} primaryText="text_main" />
                                    <MenuItem value={"text_from_tags"} primaryText="text_tag" />
                                    <MenuItem value={"ngrams"} primaryText="ngram" />
                                    <MenuItem value={"tag_stat"} primaryText="tag_stat" />
                                    <MenuItem value={"join"} primaryText="join" />
                                </SelectField>
                            </Col>
                        </Row>

                        <AdvancedTextField placeHolder={""}
                                           pattern={"path"}
                                           label={"description"}
                                           fieldName={"stopwords"}
                            // value={maxNGramSize}
                            // onChangeEvent={this.changeEvent}
                                           style={style.oneOfTwoInputFields}
                        />

                        <Row>
                            <Col xs={12} md={6}>
                                <SelectField
                                    floatingLabelText="Type"
                                    value={"binomial"}
                                    onChange={this.handleChange}
                                    style={{width: "100%"}}
                                >
                                    <MenuItem value={"binomial"} primaryText="binomial" />
                                    <MenuItem value={"real"} primaryText="real" />
                                </SelectField>
                            </Col>
                            <Col xs={12} md={6}>
                                <AdvancedTextField placeHolder={""}
                                                   pattern={"path"}
                                                   label={"IDF_Threshold"}
                                                   fieldName={"stopwords"}
                                    // value={maxNGramSize}
                                    // onChangeEvent={this.changeEvent}
                                                   style={style.oneOfFourInputFields}
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col xs={12} md={6}>
                                <AdvancedTextField placeHolder={""}
                                                   pattern={"path"}
                                                   label={"IDF_Type"}
                                                   fieldName={"stopwords"}
                                    // value={maxNGramSize}
                                    // onChangeEvent={this.changeEvent}
                                                   style={style.oneOfFourInputFields}
                                />
                            </Col>
                            <Col xs={12} md={6}>
                                <AdvancedTextField placeHolder={""}
                                                   pattern={"path"}
                                                   label={"TF_Type"}
                                                   fieldName={"stopwords"}
                                    // value={maxNGramSize}
                                    // onChangeEvent={this.changeEvent}
                                                   style={style.oneOfFourInputFields}
                                />
                            </Col>
                        </Row>

                    </Col>
                    <Col xs={12} md={6}>
                        <AdvancedTextField placeHolder={""}
                                           pattern={"path"}
                                           label={"dataset"}
                                           fieldName={"stopwords"}
                            // value={maxNGramSize}
                            // onChangeEvent={this.changeEvent}
                                           style={style.oneOfTwoInputFields}
                        />
                        <AdvancedTextField placeHolder={""}
                                           pattern={"path"}
                                           label={"specific setting"}
                                           fieldName={"stopwords"}
                            // value={maxNGramSize}
                            // onChangeEvent={this.changeEvent}
                                           style={style.oneOfTwoInputFields}
                        />
                        <AdvancedTextField placeHolder={""}
                                           pattern={"path"}
                                           label={"features by category"}
                                           fieldName={"stopwords"}
                            // value={maxNGramSize}
                            // onChangeEvent={this.changeEvent}
                                           style={style.oneOfTwoInputFields}
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

export default connect(mapStateToProps, mapDispatchToProps)(FormPrepare);