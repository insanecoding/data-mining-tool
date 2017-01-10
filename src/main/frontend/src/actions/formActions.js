import {
    COMPONENT_TOGGLED,
    FIELD_CHANGED,
    DIALOG_SUBMIT,
    DIALOG_EDIT,
    ON_BLACKLIST_DELETE,
    ON_LIST_ELEMENT_ADD,
    ON_LIST_ELEMENT_DELETE,
    ON_LIST_ELEMENT_EDIT
} from "../constants/constants";
import Immutable from "immutable";

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

// where to save is an array with Immutable path like: ['download', 'categories']
export function onListElementAdd(element, whereToSave) {
    return {
        type: ON_LIST_ELEMENT_ADD,
        payload: Immutable.Map({
            element: element,
            whereToSave: whereToSave
        })
    }
}

export function onListElementDelete(elementId, whereToSeek) {
    return {
        type: ON_LIST_ELEMENT_DELETE,
        payload: Immutable.Map({
            elementId: elementId,
            whereToSeek: whereToSeek
        })
    }
}

export function onListElementEdit(elementId, element, whereToSeek) {
    return {
        type: ON_LIST_ELEMENT_EDIT,
        payload: Immutable.Map({
            elementId: elementId,
            element: element,
            whereToSeek: whereToSeek
        })
    };
}