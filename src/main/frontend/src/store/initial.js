import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    data: {
        uncompress: {
            key: 1,
            name: "Uncompress blacklist"
        },
        import: {
            key: 2,
            name: "Import Blacklist"
        },
        add: {
            key: 3,
            name: "Add features"
        },
        run: {
            key: 4,
            name: "Run experiments"
        },
        result: {
            key: 5,
            name: "Result"
        },
    },
    active: 0
});