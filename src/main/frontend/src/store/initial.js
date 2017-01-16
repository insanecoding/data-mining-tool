import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    import: {
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
        route: "/download",
        displayName: "Download HTML",
        isOn: false,
        categories: ["medical", "chat", "news", "pets"],
        downloadsPerCategory: 200,
        connectTimeout: 5000,
        readTimeout: 5000,
        threadsNumber: 50
    },
    extract: {
        route: "/extract",
        displayName: "Extract features",
        isOn: false,
        isTextMain: true,
        isTextFromTags: true,
        isNgrams: false,
        isTagStat: false,
        categories: ["medical", "chat", "news", "pets"],
        tagsWithText: ["h1", "h2", "h3", "title"],
        maxNGramSize: 6,
        tagsToSkip: ["html", "head", "title", "body"]
    },
    dataSplit: {
        route: "/split",
        displayName: "Split data set",
        isOn: false,
        param: [
            {
                dataSetName: "set_1",
                description: "large data set",
                categories: ["medical", "religion", "alcohol", "shopping"],
                partitionLearn: 0.8,
                lang: "en",
                minTextLength: 500,
                maxTextLength: 5000,
                websitesPerCategory: 2000,
            },
            {
                dataSetName: "set_2",
                description: "small data set",
                categories: ["religion", "alcohol", "adult"],
                partitionLearn: 0.7,
                lang: "en",
                minTextLength: 500,
                maxTextLength: 5000,
                websitesPerCategory: 1000
            }
        ]
    },
    run: {
        route: "/run",
        displayName: "Run experiments",
        isOn: false,
        stopWordsPath: "\\stopwords.dat",
        experiments: [
            {
                name: "exp_1",
                description: "text experiment 1",
                dataSetName: "set_1",
                mode: "text_main",
                type: "binomial",
                IDF_Treshold: 0.001,
                IDF_Type: "M",
                TF_Type: "S",
                featuresByCategory: 50
            },
            {
                name: "exp_2",
                description: "text experiment 2",
                dataSetName: "set_2",
                mode: "text_main",
                type: "binomial",
                IDF_Treshold: 0.001,
                IDF_Type: "M",
                TF_Type: "S",
                featuresByCategory: 50
            }
        ]
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