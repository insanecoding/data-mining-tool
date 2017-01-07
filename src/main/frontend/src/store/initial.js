import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    import: {
        key: 1,
        route: "/import",
        displayName: "Import Blacklist",
        isOn: false,
        userName: "postgres",
        password: "postgresql",
        dbName: "Website_Classification",
        port: 5432,
        cwd: "C:\\DataMining\\experiment_data\\",
        blacklists: [
            // element example
            // {
            //     listName: ...
            //     folderName: ...
            //     website: ...
            //     key: ...
            // }
        ]
    },
    add: {
        key: 2,
        route: "/add",
        displayName: "Add features",
        isOn: false,
    },
    run: {
        key: 3,
        route: "/run",
        displayName: "Run experiments",
        isOn: false,
    },
});

export const connectionInitialState = Immutable.fromJS({
        started: false,
        status: "Ready",
        percentsProgress: 0,
        error: null,
        activeTab: 0,
        formActive: 0,
    }
);