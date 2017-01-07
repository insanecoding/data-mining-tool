import {App, Body, Welcome, Results} from "./../containers/index";
import {Route, IndexRoute} from "react-router";
import React from "react";

module.exports = (
    <Route path="/" component={App}>
        <IndexRoute component={Welcome}/>
        <Route path="/settings" component={Body}>
            {/*<Route path="/form1" component={Form1}/>*/}
            {/*<Route path="/form2" component={Form2}/>*/}
        </Route>
        <Route path="/results" component={Results}/>
    </Route>
);

