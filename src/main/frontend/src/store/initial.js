import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    data: {
        uncompress: {
            key: 1,
            displayName: "Uncompress blacklist",
            isOn: false,
        },
        import: {
            key: 2,
            displayName: "Import Blacklist",
            isOn: false,
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
    active: 0
});