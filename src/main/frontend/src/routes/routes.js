import {App, Body, Welcome, Results} from "./../containers/index";
import {
    FormDescription,
    FormImport,
    FormDownload,
    FormExtract,
    FormExperiment,
    FormSplit
} from "./../components/forms/index";
import {Route, IndexRoute} from "react-router";
import React from "react";

module.exports = (
    <Route path="/" component={App}>
        <IndexRoute component={Welcome}/>
        <Route path="/settings" component={Body}>
            <IndexRoute component={FormDescription}/>
            <Route path="/import" component={FormImport}/>
            <Route path="/download" component={FormDownload}/>
            <Route path="/extract" component={FormExtract}/>
            <Route path="/split" component={FormSplit}/>
            <Route path="/run" component={FormExperiment}/>
        </Route>
        <Route path="/results" component={Results}/>
    </Route>
);

