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
            {
                listName: "Shalla Security Blacklist",
                folderName: "\\blacklists\\shalla\\",
                website: "http://www.shallalist.de/",
                key: 1
            },
            {
                listName: "URLBlacklist",
                folderName: "\\blacklists\\ubl\\",
                website: "http://urlblacklist.com/",
                key: 2
            }
        ]
    },
    download: {
        key: 2,
        route: "/download",
        displayName: "Download HTML",
        isOn: false,
        categories: [
            {
               name: "medical",
               key: 1
            },
            {
                name: "chat",
                key: 2
            },
            {
                name: "news",
                key: 3
            },
            {
                name: "pets",
                key: 4
            }
        ],
        downloadsPerCategory: 200,
        connectTimeout: 5000,
        readTimeout: 5000,
        threadsNumber: 50
    },
    extract: {
        key: 3,
        route: "/extract",
        displayName: "Extract features",
        isOn: false,
        isTextMain: true,
        isTextFromTags: true,
        isNgrams: false,
        isTagStat: false,
        categories: [
            {
                name: "medical",
                key: 1
            },
            {
                name: "chat",
                key: 2
            },
            {
                name: "news",
                key: 3
            },
            {
                name: "pets",
                key: 4
            }
        ],
        tagsWithText: [
            {
                name: "h1",
                key: 1
            },
            {
                name: "h2",
                key: 2
            },
            {
                name: "h3",
                key: 3
            },
            {
                name: "title",
                key: 4
            }
        ],
        maxNGramSize: 6,
        tagsToSkip: [
            {
                name: "html",
                key: 1
            },
            {
                name: "head",
                key: 2
            },
            {
                name: "title",
                key: 3
            },
            {
                name: "body",
                key: 4
            },
        ]
    },
    dataSplit: {
        key: 4,
        route: "/split",
        displayName: "Split data set",
        isOn: false,
        param: [
            {
                dataSetName: "set_1",
                description: "small data set",
                categories: [
                    {
                        name: "medical",
                        key: 1
                    },
                    {
                        name: "chat",
                        key: 2
                    }
                ],
                partitionLearn: 0.7,
                lang: "en",
                minTextLength: 500,
                maxTextLength: 5000,
                websitesPerCategory: 1000,
            },
            {
                dataSetName: "set_2",
                description: "large data set",
                categories: [
                    {
                        name: "medical",
                        key: 1
                    },
                    {
                        name: "chat",
                        key: 2
                    },
                    {
                        name: "news",
                        key: 3
                    },
                    {
                        name: "pets",
                        key: 4
                    }
                ],
                partitionLearn: 0.8,
                lang: "en",
                minTextLength: 500,
                maxTextLength: 5000,
                websitesPerCategory: 5000
            }
        ]
    },
    run: {
        key: 5,
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