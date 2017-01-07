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
    fileChooser: {
        cursor: 'pointer',
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        width: '100%',
        opacity: 0,
    },
    inlineForm: {
        textAlign: "center"
    },
};

class Settings extends Component {
    render() {

        const { formReducer, connectionReducer } = this.props;
        const { componentToggled, onInputChange} = this.props.formActions;
        const { activeFormAndRouteChanged, onWebsocketMessage,
            executePostQuery, executeGetQuery } = this.props.connectionActions;

        const pathChooserParam = {
            formName: "import",
            cwd: formReducer.getIn(['import', 'cwd']),
            onInputChange: onInputChange,
        };

        const leftFormParam = {
            components: mapToArray(formReducer),
            activeFormChanged: activeFormAndRouteChanged,
            componentToggled: componentToggled,
        };

        const buttonsAndProgressParam = {
            onWebsocketMessage: onWebsocketMessage,
            executePostQuery: executePostQuery,
            executeGetQuery: executeGetQuery,
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