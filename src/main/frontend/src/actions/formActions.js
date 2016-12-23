import {ACTIVE_FORM_CHANGED, COMPONENT_TOGGLED, FIELD_CHANGED} from "../constants/constants";
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

export function onInputChange(value, fieldName, formName, rootObject = {}) {
    return {
        type: FIELD_CHANGED,
        payload: Immutable.Map({
            formName: formName,
            fieldName: fieldName,
            value: value,
            rootObject: rootObject,
        })
    }
}