import {CONNECTION_FAILED, CONNECTION_SUCCESS, WEBSOCKET_MESSAGE} from "../constants/constants";
import {postQuery, getQuery} from "./../util/rest";
import Immutable from "immutable";

// Simple pure action creator
export function connectionFailure(error) {
    return {
        type: CONNECTION_FAILED,
        payload: error
    };
}

// Another simple pure action creator
export function connectionSuccess(status) {
    return {
        type: CONNECTION_SUCCESS,
        payload: status
    };
}

export function executePostQuery(api, object = {}) {
    return dispatch => {
        postQuery(api, object).then(
            json => dispatch(connectionSuccess(json.status)),
            error => dispatch(connectionFailure(error))
        )
    };
}

export function executeGetQuery(api) {
    return dispatch => {
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
