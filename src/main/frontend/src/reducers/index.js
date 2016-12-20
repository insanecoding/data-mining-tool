import {combineReducers} from "redux";
import forms from "./forms";
import user from "./user";

export default combineReducers({
    forms,
    user
})