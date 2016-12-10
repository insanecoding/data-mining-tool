import React, {Component} from 'react';
import fetch from './ReactFetch';

class MyApp extends Component {

    constructor(props) {
        super(props);
        this.myGet = this.myGet.bind(this);
    }

    getInfo() {
        fetch('http://localhost:4000/athletes').then(function (response) {
            return response.json()
        }).then(function (response) {
            console.log('content type = ' + response.headers.get('Content-Type'));
            console.log('date = ' + response.headers.get('Date'));
            console.log('status = ' + response.status);
            console.log('status text = ' + response.statusText);
            console.log('resp data = ' + response.data)
        })
    }

    myGet() {
        fetch('http://localhost:4000/athletes').then(function (response) {
            return response.json()
        }).then(function (json) {
            console.log('parsed json', json)
        }).catch(function (ex) {
            console.log('parsing failed', ex)
        });
    }

    myPost() {
        fetch('http://localhost:4000/athletes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: 'Hubot',
                login: 'hubot',
            })
        })
    }
}
export default MyApp;
