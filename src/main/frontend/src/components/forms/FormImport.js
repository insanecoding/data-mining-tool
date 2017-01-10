import React, {Component} from "react";
import {Col, Row} from "react-grid-system";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";
import AlertDialog from "./../AlertDialog";
import FieldArray from "./../FieldArray";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../../actions/connectionActions";
import * as formActions from "./../../actions/formActions";

class FormImport extends Component {

    constructor(props) {
        super(props);

        this.state = FormImport.init()
    }

    static init() {
        return {
            isOpen: false,
            folderName: "\\blacklists\\name\\",
            listName: "blacklist name",
            website: "http://blacklist.com",
            editMode: false,
            modifiedId: -1
        };
    }

    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    };

    addBlacklist = () => {
        // open dialog
        this.setState({isOpen: true});
    };

    handleCloseOk = () => {
        const {addBlacklist, editBlacklist} = this.props.formActions;
        // close dialog window
        this.setState({isOpen: false});
        // call action with state's parameters
        if (!this.state.editMode) {
            addBlacklist(this.state.folderName, this.state.listName,
                this.state.website);
        } else {
            editBlacklist(this.state.modifiedId, this.state.folderName, this.state.listName,
                this.state.website)
        }

        // reset dialog form data to initial values
        this.setState(FormImport.init);
    };

    handleCloseCancel = () => {
        this.setState(FormImport.init);
    };

    changeEvent = (e) => {
        this.props.formActions.onInputChange(e.target.value, e.target.name, "import");
    };

    onDeleteAction = (blacklistId) => {
        console.log("deleting", blacklistId);
        this.props.formActions.onBlacklistDelete(blacklistId);
    };

    onEditAction = (blacklistId) => {
        const {formReducer} = this.props;
        let editedBlacklist = formReducer.getIn(['import', 'blacklists'])
            .filter(elem => elem.getIn(['key']) === blacklistId).first();
        // let relativePath = editedBlacklist.getIn(['folderName']).replace(cwd, "").replace(/\\\\/g, '\\');

        this.setState({
            folderName: editedBlacklist.getIn(['folderName']),
            listName: editedBlacklist.getIn(['listName']),
            website: editedBlacklist.getIn(['website']),
            isOpen: true,
            editMode: true,
            modifiedId: editedBlacklist.getIn(['key'])
        });
    };

    render() {
        const {displayName, userName, password, dbName, port, blacklists} =
            this.props.formReducer.getIn(['import']).toObject();

        const fieldArrayParam = {
            title: "Add blacklists for import:",
            elements: blacklists.toArray(),
            onEditAction: this.onEditAction,
            onDeleteAction: this.onDeleteAction,
            onAddAction: this.addBlacklist,
            editTooltip: "edit blacklist",
            removeTooltip: "remove blacklist",
            addTooltip: "add blacklist"
        };

        return (
            <GenericForm title={displayName}>
                <AlertDialog handleCloseOk={this.handleCloseOk} handleCloseCancel={this.handleCloseCancel}
                             title={"Add blacklist"} isOpen={this.state.isOpen} type={"dialog"}>
                    <Row>
                        <Col md={12} xs={12}>
                            <AdvancedTextField placeHolder="\list\sub\dir\"
                                               pattern={"path"}
                                               label={"blacklist subdirectory"}
                                               fieldName={"folderName"}
                                               value={this.state.folderName}
                                               onChangeEvent={this.onChange}
                                               style={{width: "90%"}}
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col md={6} xs={12}>
                            <AdvancedTextField placeHolder="blacklist name"
                                               pattern={"not_empty"}
                                               label={"Blacklist name"}
                                               fieldName={"listName"}
                                               value={this.state.listName}
                                               onChangeEvent={this.onChange}
                            />
                        </Col>
                        <Col md={6} xs={12}>
                            <AdvancedTextField placeHolder="http://blacklist.url"
                                               pattern={"not_empty"}
                                               label={"blacklist url"}
                                               fieldName={"website"}
                                               value={this.state.website}
                                               onChangeEvent={this.onChange}
                            />
                        </Col>
                    </Row>
                </AlertDialog>


                <Row>
                    <Col xs={12} md={6}>
                        <AdvancedTextField placeHolder="user name"
                                           pattern={"not_empty"}
                                           label={"user name"}
                                           fieldName={"userName"}
                                           value={userName}
                                           onChangeEvent={this.changeEvent}
                        />
                        <AdvancedTextField placeHolder="password"
                                           type={"password"}
                                           pattern={"not_empty"}
                                           label={"password"}
                                           fieldName={"password"}
                                           value={password}
                                           onChangeEvent={this.changeEvent}
                        />
                    </Col>
                    <Col xs={12} md={6}>
                        <AdvancedTextField placeHolder="database name"
                                           pattern={"not_empty"}
                                           label={"db name"}
                                           fieldName={"dbName"}
                                           value={dbName}
                                           onChangeEvent={this.changeEvent}
                        />
                        <AdvancedTextField placeHolder="port number"
                                           pattern={"number"}
                                           type={"number"}
                                           label={"port number"}
                                           fieldName={"port"}
                                           value={port}
                                           onChangeEvent={this.changeEvent}
                        />
                    </Col>
                </Row>

                <FieldArray {...fieldArrayParam} />
            </GenericForm>
        );
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

export default connect(mapStateToProps, mapDispatchToProps)(FormImport);