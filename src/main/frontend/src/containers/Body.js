import React, {Component} from "react";
import LeftSideItem from "./LeftSideItem";
import RightSideItem from "./RightSideItem";
import {Row, Col} from "react-grid-system";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as formActions from "../actions/formActions";
import PathChooser from "./../components/forms/PathChooser";
import {mapToArray} from "./../util/misc";

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

class Body extends Component {
    render() {

        const { formReducer } = this.props;
        const { activeFormChanged, componentToggled, onInputChange } = this.props.formActions;

        const pathChooserParam = {
            formName: "pathChooser",
            cwd: formReducer.getIn(['pathChooser', 'cwd']),
            onInputChange: onInputChange,
        };

        const leftFormParam = {
            components: mapToArray(formReducer.getIn(['forms'])),
            activeFormChanged: activeFormChanged,
            componentToggled: componentToggled,
        };

        const rightFormParam = {
            number: formReducer.getIn(['formActive']),
            archives: formReducer.getIn(['forms', 'uncompress', 'archives']),
            userName: formReducer.getIn(['forms', 'import', 'userName']),
            password: formReducer.getIn(['forms', 'import', 'password']),
            dbName:  formReducer.getIn(['forms', 'import', 'dbName']),
            port:  formReducer.getIn(['forms', 'import', 'port']),
            onInputChange: onInputChange,
        };

        return (
            <div style={this.props.style}>
                <h1 style={style.title}>Welcome to website classification utility</h1>

                <PathChooser {...pathChooserParam}/>

                <Row>
                    <Col xs={12} md={6}>
                        <LeftSideItem {...leftFormParam}/>
                    </Col>
                    <Col xs={12} md={6}>
                        <RightSideItem {...rightFormParam}/>
                    </Col>
                </Row>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        formReducer: state.formReducer,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        formActions: bindActionCreators(formActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Body)