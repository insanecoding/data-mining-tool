import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import LinearProgress from 'material-ui/LinearProgress';
import ListItem from "./ListItem";

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

    showHelp() {
        console.log("button pressed");
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

                <div id="buttons">
                    <RaisedButton className={"button"} label="Start" secondary={true} onTouchTap={this.showHelp} />
                    <RaisedButton className={"button"} label="Cancel" secondary={true} onTouchTap={this.showHelp}/>
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