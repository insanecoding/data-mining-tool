import {CONNECTION_FAILED, CONNECTION_SUCCESS} from "../constants/constants";
import postQuery from "./../util/rest";

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

// Another simple pure action creator
// function disconnect() {
//     return { type: DISCONNECT };
// }

// Side effect: uses thunk middleware
export function myConnect(api, object) {
    return dispatch => {
        postQuery(api, object).then(
            json => dispatch(connectionSuccess(json.status)),
            error => dispatch(connectionFailure(error))
        )
    };
}
