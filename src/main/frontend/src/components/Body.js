import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import LinearProgress from 'material-ui/LinearProgress';
import ListItem from "./ListItem";
import fetch from './../rest/ReactFetch';

class Body extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: false,
            completed: 0,
        };
    }

    // getInfo() {
    //     fetch('http://localhost:8080/api').then(function (response) {
    //         return response.json()
    //     }).then(function (response) {
    //         console.log('content type = ' + response.headers.get('Content-Type'));
    //         console.log('date = ' + response.headers.get('Date'));
    //         console.log('status = ' + response.status);
    //         console.log('status text = ' + response.statusText);
    //         console.log('resp data = ' + response.data)
    //     })
    // }
    //
    // myGet() {
    //     fetch('http://localhost:8080/api').then(function (response) {
    //         return response.json()
    //     }).then(function (json) {
    //         console.log('parsed json', json)
    //     }).catch(function (ex) {
    //         console.log('parsing failed', ex)
    //     });
    // }
    //
    // myPost() {
    //     fetch('http://localhost:8080/api/add', {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         mode: 'no-cors',
    //         body: JSON.stringify({
    //             name: 'Hubot',
    //             login: 'hubot',
    //         })
    //     })
    // }

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
        let myRequest = new Request('http://localhost:8080/api');
        let myInit = {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        };

        fetch(myRequest, myInit).then(function(response) {
            if(response.status === 200)
                return response.json();
            else
                throw new Error('Something went wrong on api server!');
        }).then(function (body) {
            console.log(body);
        }).catch(function (ex) {
            console.log('parsing failed', ex)
        });
    }

    getPost() {
        let myObj = {
            firstName: "My",
            lastName: "New Object",
        };


        let myRequest = new Request('http://localhost:8080/api/add');
        let myInit = {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(myObj)
        };

        fetch(myRequest, myInit)
            .then(function(response) {
                if(response.status === 200) return response.json();
                else throw new Error('Something went wrong on api server!');
            })
            .then(function(response) {
                console.debug(response);
            })
            .catch(function(error) {
                console.error(error);
            });


        // fetch(myRequest, myInit);
        //
        // fetch(myRequest, {
        //         method: 'get',
        //         mode: 'no-cors'
        //     })
        //     .then(function (response) {
        //         return response.text()
        //     })
        //     .then(function(text) {
        //         return text ? JSON.parse(text) : {"me": "me"}
        //     })
        //     .then(function (json) {
        //     console.log('parsed json', json)
        //     })
        //     .catch(function (ex) {
        //     console.log('parsing failed', ex)
        // });
        // const text = '{"firstName":"John","lastName":"Johnson"}';
        // const obj = JSON.parse(text);
        //
        // fetch('http://localhost:8080/api/add', {
        //     method: 'POST',
        //     mode: 'no-cors',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify( {firstName: "Hubot",lastName: "hubot"} )
        // })

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