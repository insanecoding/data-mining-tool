import {
    ACTIVE_FORM_CHANGED,
    COMPONENT_TOGGLED,
    FIELD_CHANGED,
    DIALOG_SUBMIT,
    DIALOG_EDIT,
    ON_BLACKLIST_DELETE
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

export function addBlacklist (subDir, blacklistName, blacklistUrl) {
    return {
        type: DIALOG_SUBMIT,
        payload: Immutable.Map({
            listName: blacklistName,
            folderName: subDir,
            website: blacklistUrl
        })
    }
}

export function editBlacklist (key, subDir, blacklistName, blacklistUrl) {
    return {
        type: DIALOG_EDIT,
        payload: Immutable.Map({
            key: key,
            listName: blacklistName,
            folderName: subDir,
            website: blacklistUrl
        })
    }
}

export function onBlacklistDelete (key) {
    return {
        type: ON_BLACKLIST_DELETE,
        payload: key
    }
}