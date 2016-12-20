import {SET_YEAR, FORM_CHANGED, GET_PHOTOS_REQUEST, GET_PHOTOS_SUCCESS} from "./../constants/Forms";
import Immutable from "immutable";

export function setYear(year) {
    return {
        type: SET_YEAR,
        payload: year
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