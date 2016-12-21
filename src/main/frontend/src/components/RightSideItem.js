import React, {PropTypes} from "react";
import {MyForms} from "./forms/index";

const RightSideItem = ({number}) => {

    const formChooser = (index) => {
        switch (index) {
            case 0:
                return <MyForms.FormWelcome title="Click components to see settings"/>;
            case 1:
                return <MyForms.FormUncompress title="Uncompress settings"/>;
            case 2:
                return <MyForms.FormImport title="Import blacklist"/>;
            case 3:
                return <MyForms.FormFeatures/>;
            case 4:
                return <MyForms.FormExperiment/>;
            default:
                return null;
        }
    };

    return (
        <div>
            { formChooser (number) }
        </div>
    )
};

RightSideItem.propTypes = {
    number: PropTypes.number.isRequired,
};

export default RightSideItem;