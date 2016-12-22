import {combineReducers} from "redux";
import formReducer from "./formReducer";
import connectionReducer from "./connectionReducer";

export default combineReducers({
    formReducer,
    connectionReducer
})