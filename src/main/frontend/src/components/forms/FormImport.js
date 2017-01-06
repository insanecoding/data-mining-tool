import React, {PropTypes, Component} from "react";
import {List, ListItem} from "material-ui/List";
import {Col, Row} from "react-grid-system";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import AdvancedTextField from "./../AdvancedTextField";
import GenericForm from "./GenericForm";
import AlertDialog from "./../AlertDialog";
import ListRenderer from "./../ListRenderer";
import {Toolbar, ToolbarGroup} from "material-ui/Toolbar";

const style = {
    leftToRight: {
        justifyContent: "flex-start",
        backgroundColor: "white",
    }
};

class RightSideForm_ImportList extends Component {

    constructor(props) {
        super(props);

        this.state = RightSideForm_ImportList.init()
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
        // close dialog window
        this.setState({isOpen: false});
        // call action with state's parameters
        if (!this.state.editMode) {
            this.props.addBlacklist(this.state.folderName, this.state.listName,
                this.state.website);
        } else {
            this.props.editBlacklist(this.state.modifiedId, this.state.folderName, this.state.listName,
                this.state.website)
        }

        // reset dialog form data to initial values
        this.setState(RightSideForm_ImportList.init);
    };

    handleCloseCancel = () => {
        this.setState(RightSideForm_ImportList.init);
    };

    changeEvent = (e) => {
        this.props.onInputChange(e.target.value, e.target.name, this.props.formName, "forms");
    };

    onDeleteAction = (blacklistId) => {
        console.log("deleting", blacklistId);
        this.props.onBlacklistDelete(blacklistId);
    };

    onEditAction = (blacklistId) => {
        let foo = this.props.formStore.getIn(['forms', 'import', 'blacklists'])
            .filter( elem => elem.getIn(['key']) === blacklistId).first();
        const cwd = this.props.formStore.getIn(['pathChooser', 'cwd']);
        // get relative path from full path and replace unnecessary double backslashes with single ones
        let relativePath = foo.getIn(['folderName']).replace(cwd, "").replace(/\\\\/g, '\\');
        
        this.setState({
            folderName: relativePath,
            listName: foo.getIn(['listName']),
            website: foo.getIn(['website']),
            isOpen: true,
            editMode: true,
            modifiedId: foo.getIn(['key'])
        });
    };

    render() {
        const {title, userName, password, dbName, port, blacklists} = this.props;

        return (
            <GenericForm title={title}>
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
                                           label={"port number"}
                                           fieldName={"port"}
                                           value={port}
                                           onChangeEvent={this.changeEvent}
                        />
                    </Col>
                </Row>


                <Toolbar style={style.leftToRight}>
                    <ToolbarGroup>
                        <IconButton onClick={this.addBlacklist} tooltip={"Add blacklist"}>
                            <FontIcon className="fa fa-plus"/>
                        </IconButton>
                    </ToolbarGroup>

                    <ToolbarGroup>
                        <span style={{textAlign: "center"}}>
                            {"Add blacklists for import:"}
                        </span>
                    </ToolbarGroup>

                </Toolbar>

                <Row>
                    <Col md={12} xs={12}>
                        <ListRenderer elements={blacklists} onDeleteAction={ this.onDeleteAction}
                                      onEditAction={this.onEditAction}/>
                    </Col>
                </Row>
            </GenericForm>
        );
    }
}

RightSideForm_ImportList.propTypes = {
    title: PropTypes.string.isRequired,
    formName: PropTypes.string.isRequired,
    userName: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    dbName: PropTypes.string.isRequired,
    port: PropTypes.number.isRequired,
    onInputChange: PropTypes.func.isRequired,
    addBlacklist: PropTypes.func.isRequired,
    editBlacklist: PropTypes.func.isRequired,
    blacklists: PropTypes.array.isRequired,
    onBlacklistDelete: PropTypes.func.isRequired,
    formStore: PropTypes.object.isRequired,
};

export default RightSideForm_ImportList;