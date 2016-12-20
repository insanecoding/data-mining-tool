import {SET_YEAR, FORM_CHANGED, GET_PHOTOS_REQUEST, GET_PHOTOS_SUCCESS} from "../constants/Forms";
import {initialState} from "./../store/initial";

export default function page(state = initialState, action) {
    switch (action.type) {
        case SET_YEAR:
            return state.setIn(['year'], action.payload);

        case FORM_CHANGED: {
            const value = action.payload.getIn(['value']);
            const name = action.payload.getIn(['name']);
            const formName = action.payload.getIn(['formName']);
            return state.setIn([formName, name], value);
        }
        case GET_PHOTOS_REQUEST:
            return state.setIn(['year'], action.payload).setIn(['fetching'], true);
        case GET_PHOTOS_SUCCESS:
            return state.setIn(['photos'], action.payload).setIn(['fetching'], false);
        default:
            return state;
    }
}