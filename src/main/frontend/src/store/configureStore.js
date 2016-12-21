import {createStore, applyMiddleware} from "redux";
import rootReducer from "../reducers/rootReducer";
import thunk from "redux-thunk";
import createLogger from "redux-logger";

export default function configureStore(initialState) {
    const logger = createLogger();
    return createStore(
        rootReducer,
        initialState,
        applyMiddleware(thunk, logger));
}