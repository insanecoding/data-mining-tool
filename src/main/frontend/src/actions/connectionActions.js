import {
    TAB_SWITCHED,
    CONNECTION_FAILED,
    CONNECTION_SUCCESS,
    WEBSOCKET_MESSAGE,
    IS_APP_STARTED
} from "../constants/constants";
import {postQuery, getQuery} from "./../util/rest";
import Immutable from "immutable";

export function tabChanged (newTabNumber) {
    return {
        type: TAB_SWITCHED,
        payload: newTabNumber
    }
}

export function connectionFailure(error) {
    return {
        type: CONNECTION_FAILED,
        payload: error
    };
}

export function connectionSuccess(status, percentsProgress) {
    return {
        type: CONNECTION_SUCCESS,
        payload:Immutable.Map({
            status: status,
            percentsProgress: percentsProgress,
        })
    };
}

// e.g: start button
export function executePostQuery(api, object = {}) {
    return dispatch => {
        dispatch(isAppStarted(true));
        postQuery(api, object).then(
            json => dispatch(connectionSuccess(json.status, json.percentsProgress)),
            error => dispatch(connectionFailure(error))
        ).then( () => dispatch(isAppStarted(false)));
    };
}

// e.g: cancel button
export function executeGetQuery(api) {
    return dispatch => {
        dispatch(isAppStarted(false));
        getQuery(api).then(
            json => {
                if (json !== undefined)
                    dispatch(connectionSuccess(json.status))
            },
            error => dispatch(connectionFailure(error))
        )
    };
}

export function onWebsocketMessage(status, percentsProgress) {
    return {
        type: WEBSOCKET_MESSAGE,
        payload: Immutable.Map({
            status: status,
            percentsProgress: percentsProgress,
        })
    }
}

export function isAppStarted(started) {
    return {
        type: IS_APP_STARTED,
        payload: started
    }
}