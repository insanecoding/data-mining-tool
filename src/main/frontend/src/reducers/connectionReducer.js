import {connectionInitialState} from "./../store/initial";
import {CONNECTION_FAILED, CONNECTION_SUCCESS} from "../constants/constants";

export default function connection(state = connectionInitialState, action) {
    switch (action.type) {

        case CONNECTION_SUCCESS:
            console.log('inside reducer2', action);
            return state.setIn(['status'], action.payload);

        case CONNECTION_FAILED:
            return state.setIn(['error'], action.payload);

        default:
            return state;
    }
}