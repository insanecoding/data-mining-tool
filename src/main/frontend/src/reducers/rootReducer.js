import {combineReducers} from "redux";
import myReducer from "./myReducer";
import user from "./user";

export default combineReducers({
    myReducer,
    user
})