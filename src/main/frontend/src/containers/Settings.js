import React, {Component} from "react";
import LeftSideItem from "./LeftSideItem";
import {Row, Col} from "react-grid-system";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as formActions from "../actions/formActions";
import * as connectionActions from "./../actions/connectionActions";
import PathChooser from "./../components/forms/PathChooser";
import {mapToArray} from "./../util/misc";
import ButtonsAndProgress from "./../components/ButtonsAndProgress";

const style = {
    title: {
        textAlign: "center",
        marginTop: "20px",
        marginLeft: "20px",
        marginRight: "20px",
        marginBottom: "1px"
    },
    inlineForm: {
        textAlign: "center"
    },
};

class Settings extends Component {
    render() {

        const { formReducer, connectionReducer } = this.props;
        const { onCheck, onInputChange} = this.props.formActions;
        const { activeFormAndRouteChanged, updateStatusAndProgress,
            isAppStarted } = this.props.connectionActions;

        const pathChooserParam = {
            formName: "import",
            cwd: formReducer.getIn(['import', 'cwd']),
            onInputChange: onInputChange,
        };

        const leftFormParam = {
            components: mapToArray(formReducer),
            activeFormChanged: activeFormAndRouteChanged,
            componentToggled: onCheck,
        };

        const buttonsAndProgressParam = {
            updateStatusAndProgress: updateStatusAndProgress,
            isAppStarted: isAppStarted,
            formData: formReducer,
            connectionData: connectionReducer
        };

        return (
            <div style={this.props.style}>
                <h1 style={style.title}>Settings</h1>

                <PathChooser {...pathChooserParam}/>

                <Row>
                    <Col xs={12} md={6}>
                        <LeftSideItem {...leftFormParam}/>
                    </Col>
                    <Col xs={12} md={6}>
                        { this.props.children }
                    </Col>
                    <Col xs={12} md={12}>
                        <ButtonsAndProgress {...buttonsAndProgressParam}/>
                    </Col>
                </Row>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        formReducer: state.formReducer,
        connectionReducer: state.connectionReducer,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        formActions: bindActionCreators(formActions, dispatch),
        connectionActions: bindActionCreators(connectionActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Settings)