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
    download: {
        key: 2,
        route: "/download",
        displayName: "Download HTML",
        isOn: false,
        categories: [
            // element example
            // {
            //    categoryName: ...
            //    key: ...
            // }
        ],
        downloadsPerCategory: 200,
        connectTimeout: 5000,
        readTimeout: 5000,
        threadNumber: 50
    },
    extract: {
        key: 3,
        route: "/extract",
        displayName: "Extract features",
        isOn: false,
        categories: [
            // element example
            // {
            //    categoryName: ...
            //    key: ...
            // }
        ],
        tagsWithText: [
            // element example
            // {
            //    tagName: ...
            //    key: ...
            // }
        ],
        maxNGramSize: 6
    },
    run: {
        key: 4,
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