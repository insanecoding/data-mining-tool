import React from "react";
import {render} from "react-dom";
import {Provider} from "react-redux";
import configureStore from "./store/configureStore";
import routes from "./routes/routes";
import * as reducers from "./reducers/index";
import {Router, browserHistory} from "react-router";
import {syncHistoryWithStore, routerReducer} from "react-router-redux";
import {combineReducers} from "redux";

const reducer = combineReducers({
    ...reducers,
    routing: routerReducer
});

const store = configureStore({}, reducer);
const history = syncHistoryWithStore(browserHistory, store);

render(
    <Provider store={store}>
        <Router routes={routes} history={history}/>
    </Provider>,
    document.getElementById('root')
);