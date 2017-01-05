import {initialState} from "./../store/initial";
import {
    ACTIVE_FORM_CHANGED,
    COMPONENT_TOGGLED,
    FIELD_CHANGED,
    DIALOG_SUBMIT,
    ON_BLACKLIST_DELETE
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

        case DIALOG_SUBMIT:
            const payload = action.payload;
            // create absolute path from current dir + relative dir
            const cwd = state.getIn(['pathChooser', 'cwd']);
            const blacklistSubDir = payload.getIn(['folderName']);
            const absolutePath = `${cwd}\\${blacklistSubDir}`;
            // retrieve current array with blacklists
            const currBlacklistsArray = state.getIn(['forms', 'import', 'blacklists']);
            let lastKey = (currBlacklistsArray.size !== 0) ? currBlacklistsArray.last().getIn(['key']) : 0;
            // prepare new list entry, which should be added
            // add absolute blacklist folder and generated key
            const blacklistData =
                payload.setIn(['folderName'], absolutePath)
                .set('key', ++lastKey);
            // update list
            const newList = currBlacklistsArray.push(blacklistData);
            // merge changes
            return state.setIn(['forms', 'import', 'blacklists'], newList);

        case ON_BLACKLIST_DELETE:
            const key = action.payload;
            const currentArray = state.getIn(['forms', 'import', 'blacklists']);
            const newArray = currentArray.filterNot( x => x.getIn(['key']) === key);
            return state.setIn(['forms', 'import', 'blacklists'], newArray);

        default:
            return state;
    }
}