import React, {PropTypes} from "react";
import {FormWelcome, FormUncompress, FormImport} from "./../components/forms/index";

const RightSideItem = ({number, archives, userName, password, dbName, port, onInputChange}) => {

    const formChooser = (index) => {

        switch (index) {
            case 0:
                return <FormWelcome title={"Click components to see settings"}/>;
            case 1:
                return <FormUncompress title="Uncompress settings" formName="uncompress"
                                       onInputChange={onInputChange} archives={archives}/>;
            case 2:
                return <FormImport title="Import blacklist" formName="import"
                                   userName={userName} password={password}
                                   dbName={dbName} port={port} onInputChange={onInputChange}/>;
            // case 3:
            //     return <FormFeatures/>;
            // case 4:
            //     return <FormExperiment/>;
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
    archives: PropTypes.string.isRequired,
    userName: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    dbName: PropTypes.string.isRequired,
    port: PropTypes.number.isRequired,
    onInputChange: PropTypes.func.isRequired,
};

export default RightSideItem;