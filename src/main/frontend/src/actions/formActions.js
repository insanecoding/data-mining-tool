import {
    ADD_NEW,
    CHECKBOX_CHECKED,
    COMPONENT_TOGGLED,
    DIALOG_EDIT,
    DIALOG_SUBMIT,
    FIELD_CHANGED,
    ON_BLACKLIST_DELETE,
    ON_INPUT_CHANGED,
    ON_LIST_ELEMENT_ADD,
    ON_LIST_ELEMENT_DELETE,
    ON_LIST_ELEMENT_EDIT,
    ON_RADIO_CHANGED,
    ON_REMOVE_LAST
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

export function onListElementDelete(element, whereToSeek) {
    return {
        type: ON_LIST_ELEMENT_DELETE,
        payload: Immutable.Map({
            element: element,
            whereToSeek: whereToSeek
        })
    }
}

export function onListElementEdit(oldElem, newElem, whereToSeek) {
    return {
        type: ON_LIST_ELEMENT_EDIT,
        payload: Immutable.Map({
            oldElem: oldElem,
            newElem: newElem,
            whereToSeek: whereToSeek
        })
    };
}

export function onCheck(path) {
    return {
        type: CHECKBOX_CHECKED,
        payload: path
    }
}

export function onRadioChange(target, whereToSeek) {
    return {
        type: ON_RADIO_CHANGED,
        payload: Immutable.Map({
            target: target,
            whereToSeek: whereToSeek
        })
    }
}

export function onInputFieldChange(target, whereToSeek) {
    return {
        type: ON_INPUT_CHANGED,
        payload: Immutable.Map({
            target: target,
            whereToSeek: whereToSeek
        })
    }
}

export function removeLast(whereToSeek) {
    return {
        type: ON_REMOVE_LAST,
        payload: whereToSeek
    }
}

export function addNew(target, whereToSeek) {
    return {
        type: ADD_NEW,
        payload: Immutable.Map({
            target: target,
            whereToSeek: whereToSeek
        })
    }
}