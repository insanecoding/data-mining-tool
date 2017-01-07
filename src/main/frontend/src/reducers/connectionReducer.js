import {connectionInitialState} from "./../store/initial";
import {
    CONNECTION_FAILED,
    CONNECTION_SUCCESS,
    WEBSOCKET_MESSAGE,
    IS_APP_STARTED,
    TAB_SWITCHED
} from "../constants/constants";

export default function connection(state = connectionInitialState, action) {
    switch (action.type) {

        case TAB_SWITCHED:
            return state.setIn(['activeTab'], action.payload);

        case CONNECTION_SUCCESS:
            return state.setIn(['status'], action.payload.getIn(['status']))
                .setIn(['percentsProgress'], action.payload.getIn(['percentsProgress']));

        case CONNECTION_FAILED:
            return state.setIn(['error'], action.payload);

        case WEBSOCKET_MESSAGE:
            const status = action.payload.getIn(['status']);
            const percentsProgress = action.payload.getIn(['percentsProgress']);
            return state.setIn(['status'], status).setIn(['percentsProgress'], percentsProgress);

        case IS_APP_STARTED:
            return state.setIn(['started'], action.payload);

        default:
            return state;
    }
}