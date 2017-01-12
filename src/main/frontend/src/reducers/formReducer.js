import {initialState} from "./../store/initial";
import {
    COMPONENT_TOGGLED,
    FIELD_CHANGED,
    DIALOG_SUBMIT,
    ON_BLACKLIST_DELETE,
    DIALOG_EDIT,
    ON_LIST_ELEMENT_ADD,
    ON_LIST_ELEMENT_DELETE,
    ON_LIST_ELEMENT_EDIT,
    CHECKBOX_CHECKED
} from "../constants/constants";
import {isEmptyObject} from "./../util/misc";
import Immutable from "immutable";

export default function processForm(state = initialState, action) {
    switch (action.type) {

        case COMPONENT_TOGGLED:
            const componentName = action.payload;
            const prevState = state.getIn([componentName, 'isOn']);
            return state.setIn([componentName, 'isOn'], !prevState);

        case FIELD_CHANGED:
            const formName = action.payload.getIn(['formName']);
            const fieldName = action.payload.getIn(['fieldName']);
            const value = action.payload.getIn(['value']);
            const rootObject = action.payload.getIn(['rootObject']);

            if (isEmptyObject(rootObject))
                return state.setIn([formName, fieldName], value);
            else
                return state.setIn([rootObject, formName, fieldName], value);

        case DIALOG_SUBMIT: {
            const payload = action.payload;
            // create absolute path from current dir + relative dir
            const blacklistSubDir = payload.getIn(['folderName']);
            // retrieve current array with blacklists
            const currBlacklistsArray = state.getIn(['import', 'blacklists']);
            let lastKey = (currBlacklistsArray.size !== 0) ? currBlacklistsArray.last().getIn(['key']) : 0;

            // prepare new list entry, which should be added
            // add absolute blacklist folder and generated key
            const blacklistData = payload.setIn(['folderName'], blacklistSubDir)
                .set('key', ++lastKey);
            // update list
            const newList = currBlacklistsArray.push(blacklistData);
            // merge changes
            return state.setIn(['import', 'blacklists'], newList);
        }

        case DIALOG_EDIT: {
            const payload = action.payload;
            const keyOfModified = payload.getIn(['key']);
            let blacklists = state.getIn(['import', 'blacklists']);
            // find blacklist that was edited and its index in the array
            const oldValue = blacklists.filter( elem => elem.getIn(['key']) === keyOfModified).first();
            const index = blacklists.indexOf(oldValue);
            // assign all updated values
            const newVal = oldValue.setIn(['listName'], payload.getIn(['listName']))
                .setIn(['folderName'], payload.getIn(['folderName']))
                .setIn(['website'], payload.getIn(['website']));
            // change old value at index into new value
            blacklists = blacklists.update(index, item => item.merge(newVal));
            return state.setIn(['import', 'blacklists'], blacklists);
        }

        case ON_BLACKLIST_DELETE: {
            const key = action.payload;
            const currentArray = state.getIn(['import', 'blacklists']);
            const newArray = currentArray.filterNot(x => x.getIn(['key']) === key);
            return state.setIn(['import', 'blacklists'], newArray);
        }

        case ON_LIST_ELEMENT_ADD: {
            const whereToSave = action.payload.getIn(['whereToSave']);

            let currentElements = state.getIn(whereToSave);
            let lastKey = (currentElements.size !== 0) ? currentElements.last().getIn(['key']) : 0;
            const newElement = Immutable.Map({
                key: ++lastKey,
                name: action.payload.getIn(['element'])
            });
            currentElements = currentElements.push(newElement);
            return state.setIn(whereToSave, currentElements);
        }

        case ON_LIST_ELEMENT_DELETE: {
            const {whereToSeek, elementId} = action.payload.toObject();

            const currentArray = state.getIn(whereToSeek);
            const newArray = currentArray.filterNot(x => x.getIn(['key']) === elementId);
            return state.setIn(whereToSeek, newArray);
        }

        case ON_LIST_ELEMENT_EDIT: {
            const {whereToSeek, element, elementId} = action.payload.toObject();

            let currentArray = state.getIn(whereToSeek);
            const newArray = currentArray = currentArray.map(elem =>
                elem.getIn(['key']) === elementId ?
                    elem.setIn(['name'], element) :
                    elem
            );

            return state.setIn(whereToSeek, newArray);
        }
        case CHECKBOX_CHECKED: {
            const path = action.payload;
            let oldState = state.getIn(path);
            return state.setIn(path, !oldState);
        }


        default:
            return state;
    }
}