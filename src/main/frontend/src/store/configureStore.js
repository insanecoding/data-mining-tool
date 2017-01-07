import {createStore, applyMiddleware} from "redux";
import thunk from "redux-thunk";
import createLogger from "redux-logger";
import {routerMiddleware} from "react-router-redux";
import {browserHistory} from "react-router";

export default function configureStore(initialState, rootReducer) {
    const logger = createLogger();
    const routerMiddleWare = routerMiddleware(browserHistory);
    return createStore(
        rootReducer,
        initialState,
        applyMiddleware(routerMiddleWare, thunk, logger));
}