import {connectionInitialState} from "./../store/initial";
import {CONNECTION_FAILED, CONNECTION_SUCCESS, WEBSOCKET_MESSAGE} from "../constants/constants";

export default function connection(state = connectionInitialState, action) {
    switch (action.type) {

        case CONNECTION_SUCCESS:
            return state.setIn(['status'], action.payload);

        case CONNECTION_FAILED:
            return state.setIn(['error'], action.payload);

        case WEBSOCKET_MESSAGE:
            const status = action.payload.getIn(['status']);
            const percentsProgress = action.payload.getIn(['percentsProgress']);
            return state.setIn(['status'], status).setIn(['percentsProgress'], percentsProgress);

        default:
            return state;
    }
}