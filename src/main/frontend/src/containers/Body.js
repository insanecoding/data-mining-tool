import React, {Component} from "react";
import LeftSideItem from "./LeftSideItem";
import RightSideItem from "./RightSideItem";
import {Row, Col} from "react-grid-system";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as myActions from "./../actions/myActions";
import PathChooser from "./../components/forms/PathChooser";

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
    // constructor(props) {
    //     super(props);
    //     this.state = {
    //         cwd: "C:\\DataMining\\experiments\\blacklists2\\uncompressed\\",
    //         archives: "\\sub\\folder\\with\\archives\\",
    //         completed: 0,
    //         userName: "",
    //         password: "",
    //         dbName: "",
    //         port: "",
    //         wsMsg: "Ready",
    //
    //         validationSuccess: 0,
    //         isStarted: false,
    //         asideElem: <FormWelcome/>,
    //         toggled0: false,
    //         toggled1: false,
    //         toggled2: false,
    //         toggled3: false,
    //     };
    // };
    //
    // formHandler = (e) => {
    //     this.setState({[e.target.name]: e.target.value});
    // };
    //
    // componentDidMount = () => {
    //     this.websocket = new WebsocketClient();
    //     this.websocket.connect(this.outputWebSocket);
    // };
    //
    // componentWillUnmount = () => {
    //     this.websocket.disconnect();
    //     this.websocket = null;
    // };
    //
    // getRest = (e) => {
    //     e.preventDefault();
    //     getQuery('api/result').then((res) => {
    //         this.updateDialogText(res, 'GET');
    //     });
    // };
    //
    // startService = () => {
    //     this.setState(
    //         {
    //             wsMsg: "",
    //             completed: 0,
    //             isStarted: true
    //         }
    //     );
    //     let myObj = {
    //         cwd: this.state.cwd,
    // toggle0: this.state.toggled0,
    // toggle1: this.state.toggled1,
    // toggle2: this.state.toggled2,
    // toggle3: this.state.toggled3,
    // archives: this.state.uncompressed.archives
    // };
    // console.log(myObj);
    //
    // this.websocket.send();
    //
    // postQuery('api/invoke', myObj).then((res) => {
    //     console.log(res);
    //     this.setState(
    //         {
    //             'finished' or 'cancelled'
    // wsMsg: res.status,
    // completed: 0,
    // isStarted: false
    // }
    // );
    // });
    // };
    //
    // cancelService = () => {
    //     getQuery('api/cancel');
    // };
    //
    // outputWebSocket = (resp) => {
    //     const response = JSON.parse(resp.body);
    //     console.log("response is: ", response);
    //     this.setState(
    //         {
    //             wsMsg: response.info + " (" + response.progress + "%)",
    //             completed: response.progress
    //         }
    //     );
    // };
    //
    // onBlur = (e) => {
    //     const input = e.target.value.trim();
    //
    //     const windowsFilePathPattern = /^(?:(?:[a-z]:|\\\\[a-z0-9_.$?-]+\\[a-z0-9_.$?-]+)\\|\\?[^\\/:*?"<>|\r\n]+\\?)(?:[^\\/:*?"<>|\r\n]+\\)*[^\\/:*?"<>|\r\n]*$/i;
    //
    //     if (!windowsFilePathPattern.test(input)) {
    //         console.log("error");
    //         this.setState(
    //             {
    //                 validationSuccess: -1,
    //             }
    //         );
    //     } else {
    //         console.log("ok");
    //         this.setState(
    //             {
    //                 validationSuccess: 1,
    //             }
    //         );
    //         console.log(this.state.cwd);
    //     }
    // };
    //
    // onChangeEvent = (e) => {
    //     this.setState(
    //         {[e.target.name]: e.target.value}
    //     );
    // };
    // iconButtonClickEvent = (index) => {
    //     let newActiveForm = null;
    //
    //     switch (index) {
    //         case 0:
    //             newActiveForm = <FormUncompress value={this.state.archives} onChangeEvent={this.onChangeEvent}
    //             onBlur={this.onBlur}/>;
    //             break;
    //         case 1:
    //             newActiveForm = <FormImport formHandler={this.formHandler}/>;
    //             break;
    //         case 2:
    //             newActiveForm = <FormFeatures formHandler={this.formHandler}/>;
    //             break;
    //         case 3:
    //             newActiveForm = <FormExperiment formHandler={this.formHandler}/>;
    //             break;
    //         default:
    //             newActiveForm = <FormWelcome/>;
    //     }
    //
    //     this.setState(
    //         {asideElem: newActiveForm}
    //     );
    // };
    // toggleEvent = (index) => {
    //     this.setState({[index]: !this.state[index]});
    // };

    extracted(deeplyNestedObject) {
        let plainArray = [];
        deeplyNestedObject.getIn(['forms'])
            .map( (k,v) => k.toObject())
            .map( (k,v) => {
                k.name = v;
                return k;
            }).map( k => plainArray.push(k));

        return plainArray;
    }

    render() {

        const { myReducer } = this.props;
        const { activeFormChanged, componentToggled, onInputChange } = this.props.myActions;

        const pathChooserParam = {
            formName: "pathChooser",
            cwd: myReducer.getIn(['pathChooser', 'cwd']),
            onInputChange: onInputChange,
        };

        const leftFormParam = {
            components: this.extracted(myReducer),
            activeFormChanged: activeFormChanged,
            componentToggled: componentToggled,
        };

        const rightFormParam = {
            number: myReducer.getIn(['formActive']),
            archives: myReducer.getIn(['forms', 'uncompress', 'archives']),
            userName: myReducer.getIn(['forms', 'import', 'userName']),
            password: myReducer.getIn(['forms', 'import', 'password']),
            dbName:  myReducer.getIn(['forms', 'import', 'dbName']),
            port:  myReducer.getIn(['forms', 'import', 'port']),
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
        myReducer: state.myReducer,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        myActions: bindActionCreators(myActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Body)