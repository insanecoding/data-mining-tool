import React, {Component} from "react";
import TextField from "material-ui/TextField";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";
import GenericForm from "./GenericForm";


class FormFeatures extends Component {
    render() {

        const {formHandler} = this.props.formActions;

        return(
            <GenericForm title={"Features"}>
                <TextField
                    hintText="Hint Text"
                    floatingLabelText="Username"
                    name="userName"
                    floatingLabelFixed={true}
                    onChange={formHandler}
                />
                <TextField
                    hintText="Hint Text 2"
                    floatingLabelText="Password"
                    floatingLabelFixed={true}
                    type="password"
                    name="password"
                    onChange={formHandler}
                />
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