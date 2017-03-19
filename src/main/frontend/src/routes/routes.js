import {App, Body, Welcome} from "./../containers/index";
import {
    FormDescription,
    FormDownload,
    FormExtract,
    FormImport,
    FormPrepare,
    FormSchemes,
    FormSplit
} from "./../components/forms/index";
import {IndexRoute, Route} from "react-router";
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
            <Route path="/prepare" component={FormPrepare}/>
            <Route path="/schemes" component={FormSchemes}/>
        </Route>
    </Route>
);

