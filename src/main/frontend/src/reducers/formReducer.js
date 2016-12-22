import {initialState} from "./../store/initial";
import {
    FORM_CHANGED,
    GET_PHOTOS_REQUEST,
    GET_PHOTOS_SUCCESS,
    ACTIVE_FORM_CHANGED,
    COMPONENT_TOGGLED,
    FIELD_CHANGED
} from "../constants/constants";
import {isEmptyObject} from "./../util/misc";


export default function processForm(state = initialState, action) {
    switch (action.type) {

        case ACTIVE_FORM_CHANGED:
            return state.setIn(['formActive'], action.payload);

        case COMPONENT_TOGGLED:
            const componentName = action.payload;
            const prevState = state.getIn(['forms', componentName, 'isOn']);
            return state.setIn(['forms', componentName, 'isOn'], !prevState);

        case FIELD_CHANGED:
            const formName = action.payload.getIn(['formName']);
            const fieldName = action.payload.getIn(['fieldName']);
            const value = action.payload.getIn(['value']);
            const rootObject = action.payload.getIn(['rootObject']);

            if (isEmptyObject(rootObject))
                return state.setIn([formName, fieldName], value);
            else
                return state.setIn([rootObject, formName, fieldName], value);

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