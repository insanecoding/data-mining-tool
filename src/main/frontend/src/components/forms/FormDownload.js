import React, {Component} from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";


class FormFeatures extends Component {
    render() {

        const {formHandler} = this.props.formActions;
        const displayName = this.props.formReducer.getIn(['download', 'displayName']);

        return(
            <GenericForm title={displayName}>
                {/*<Row>*/}
                    {/*<Col xs={12} md={6}>*/}
                        {/*<AdvancedTextField placeHolder="downloads per category"*/}
                                           {/*pattern={"number"}*/}
                                           {/*label={"downloads per category"}*/}
                                           {/*fieldName={"downloadsPerCategory"}*/}
                                           {/*value={downloadsPerCategory}*/}
                                           {/*onChangeEvent={this.changeEvent}*/}
                        {/*/>*/}
                        {/*<AdvancedTextField placeHolder="connect timeout"*/}
                                           {/*type={"number"}*/}
                                           {/*pattern={"number"}*/}
                                           {/*label={"connect timeout"}*/}
                                           {/*fieldName={"connectTimeout"}*/}
                                           {/*value={connectTimeout}*/}
                                           {/*onChangeEvent={this.changeEvent}*/}
                        {/*/>*/}
                    {/*</Col>*/}
                    {/*<Col xs={12} md={6}>*/}
                        {/*<AdvancedTextField placeHolder="read timeout"*/}
                                           {/*pattern={"number"}*/}
                                           {/*label={"read timeout"}*/}
                                           {/*fieldName={"readTimeout"}*/}
                                           {/*value={readTimeout}*/}
                                           {/*onChangeEvent={this.changeEvent}*/}
                        {/*/>*/}
                        {/*<AdvancedTextField placeHolder="threads number"*/}
                                           {/*pattern={"number"}*/}
                                           {/*label={"threads number"}*/}
                                           {/*fieldName={"threadsNumber"}*/}
                                           {/*value={threadsNumber}*/}
                                           {/*onChangeEvent={this.changeEvent}*/}
                        {/*/>*/}
                    {/*</Col>*/}
                {/*</Row>*/}
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