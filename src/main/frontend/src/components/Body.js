import React, {Component} from "react";
import RaisedButton from "material-ui/RaisedButton";
import LinearProgress from "material-ui/LinearProgress";
import ListItem from "./ListItem";
import {getQuery, postQuery} from "./../rest/rest-client";

class Body extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: false,
            completed: 0,
        };
    }

    componentDidMount() {
        this.timer = setTimeout(() => this.progress(5), 1000);
    }

    componentWillUnmount() {
        clearTimeout(this.timer);
    }

    progress(completed) {
        if (completed > 100) {
            this.setState({completed: 100});
        } else {
            this.setState({completed});
            const diff = Math.random() * 10;
            this.timer = setTimeout(() => this.progress(completed + diff), 1000);
        }
    }


    handleExpandChange = (expanded) => {
        this.setState({expanded: expanded});
    };

    handleToggle = (event, toggle) => {
        this.setState({expanded: toggle});
    };

    handleExpand = () => {
        this.setState({expanded: true});
    };

    handleReduce = () => {
        this.setState({expanded: false});
    };

    getRest() {
        getQuery('api/retrieve');
    }

    getPost() {
        let myObj = {
            firstName: "My",
            lastName: "New Object",
        };
        postQuery('api/add', myObj);
    }



    render(){
        return (
            <div id="main">
                <div id="header">
                    <h1>Welcome to website classification utility</h1>
                </div>

                <ListItem name="Import Blacklist"/>
                <ListItem name="Uncompress Blacklist"/>
                <ListItem name="Add Features"/>
                <ListItem name="Something more"/>

                <div id="buttons">
                    <RaisedButton className={"button"} label="Get" secondary={true} onTouchTap={this.getRest} />
                    <RaisedButton className={"button"} label="Post" secondary={true} onTouchTap={this.getPost}/>
                </div>

                <LinearProgress mode="determinate" value={this.state.completed} style={{margin: "auto", width: "50%"}}/>


                {/*<Card expanded={this.state.expanded} onExpandChange={this.handleExpandChange}>*/}
                {/*<CardHeader showExpandableButton={true} title={"BlacklistImporter"}>*/}
                {/*<Toggle label={"bla"} labelPosition={"right"}/>*/}
                {/*</CardHeader>*/}
                {/*<CardText expandable={true}>*/}
                {/*<h3>Lorem ipsum dolor sit amet </h3>*/}
                {/*<DatePicker hintText="Date of import"/>*/}
                {/*</CardText>*/}
                {/*</Card>*/}
            </div>
        )
    }
}

export default Body;