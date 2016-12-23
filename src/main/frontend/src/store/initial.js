import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    forms: {
        uncompress: {
            key: 1,
            displayName: "Uncompress blacklist",
            isOn: false,
            archives: "\\some\\sub\\folder\\"
        },
        import: {
            key: 2,
            displayName: "Import Blacklist",
            isOn: false,
            userName: "postgres",
            password: "postgresql",
            dbName: "Website_Classification",
            port: 5432,
        },
        add: {
            key: 3,
            displayName: "Add features",
            isOn: false,
        },
        run: {
            key: 4,
            displayName: "Run experiments",
            isOn: false,
        },
    },
    pathChooser: {
        cwd: "C:\\DataMining\\experiments\\blacklists2\\",
    },
    formActive: 0,
    hasValidationErrors: false,
});

export const connectionInitialState = Immutable.fromJS({
        started: true,
        status: "Ready",
        percentsProgress: 0,
        isLoggingIn: false,
        isLoggedIn: false,
        error: null
    }
);