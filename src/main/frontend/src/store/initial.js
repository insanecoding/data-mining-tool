import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    data: {
        uncompress: {
            key: 1,
            displayName: "Uncompress blacklist",
            isOn: false,
            archives: "\\some\\sub\\folder"
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
    active: 0,

});