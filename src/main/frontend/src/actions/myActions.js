import {
    FORM_CHANGED,
    GET_PHOTOS_REQUEST,
    GET_PHOTOS_SUCCESS,
    ACTIVE_FORM_CHANGED,
    COMPONENT_TOGGLED
} from "../constants/constants";
import Immutable from "immutable";


export function activeFormChanged(formNumber) {
    return {
        type: ACTIVE_FORM_CHANGED,
        payload: formNumber
    }
}

export function componentToggled(componentName) {
    return {
        type: COMPONENT_TOGGLED,
        payload: componentName
    }
}

export function setFName(value, name, formName) {
    return {
        type: FORM_CHANGED,
        payload: Immutable.Map({
            value: value,
            name: name,
            formName: formName,
        })
    }
}

export function getPhotos(year) {
    return (dispatch) => {
        dispatch({
            type: GET_PHOTOS_REQUEST,
            payload: year
        });
        setTimeout(() => {
            dispatch({
                type: GET_PHOTOS_SUCCESS,
                payload: [1,2,3,4,5]
            })
        }, 1000)
    }
}