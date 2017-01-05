import React, {PropTypes} from "react";
import {FormWelcome, FormImport, FormFeatures, FormExperiment} from "./../components/forms/index";

const RightSideItem = ({number, userName, password, dbName, port, blacklists, addBlacklist, onInputChange, onBlacklistDelete}) => {

    const formChooser = (index) => {

        switch (index) {
            case 0:
                return <FormWelcome title={"Click components to see settings"}/>;
            case 1:
                return <FormImport title="Import blacklist" formName="import"
                                   userName={userName} password={password} dbName={dbName}
                                   port={port} onInputChange={onInputChange}
                                   addBlacklist={addBlacklist} blacklists={blacklists} onBlacklistDelete={onBlacklistDelete}/>;
            case 2:
                return <FormFeatures/>;
            case 3:
                return <FormExperiment/>;
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
    userName: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    dbName: PropTypes.string.isRequired,
    port: PropTypes.number.isRequired,
    onInputChange: PropTypes.func.isRequired,
    addBlacklist: PropTypes.func.isRequired,
    blacklists: PropTypes.array.isRequired,
    onBlacklistDelete: PropTypes.func.isRequired
};

export default RightSideItem;