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
            subDir: "\\blacklists\\name\\",
            blacklistName: "blacklist name",
            blacklistUrl: "http://blacklist.com"
        };
    }

    // listGenerator = (elements) => {
    //     return (
    //         elements.map(elem =>
    //             <p key={elements.indexOf(elem)}>
    //                 {elem.getIn(['key'])}
    //                 <br/>
    //                 {elem.getIn(['folderName'])}
    //                 <br/>
    //                 {elem.getIn(['listName'])}
    //                 <br/>
    //                 {elem.getIn(['website'])}
    //             </p>)
    //     );
    // };


    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    };

    addBlacklist = () => {
        this.setState({isOpen: true});
    };

    handleCloseOk = () => {
        this.setState({isOpen: false});
        this.props.addBlacklist(this.state.subDir, this.state.blacklistName,
            this.state.blacklistUrl);
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

    onEditAction = (e) => {
        console.log("editing", e);
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
                                               fieldName={"subDir"}
                                               value={this.state.subDir}
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
                                               fieldName={"blacklistName"}
                                               value={this.state.blacklistName}
                                               onChangeEvent={this.onChange}
                            />
                        </Col>
                        <Col md={6} xs={12}>
                            <AdvancedTextField placeHolder="http://blacklist.url"
                                               pattern={"not_empty"}
                                               label={"blacklist url"}
                                               fieldName={"blacklistUrl"}
                                               value={this.state.blacklistUrl}
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
    blacklists: PropTypes.array.isRequired,
    onBlacklistDelete: PropTypes.func.isRequired
};

export default RightSideForm_ImportList;