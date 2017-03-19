import Immutable from "immutable";

export const initialState = Immutable.fromJS({
    import: {
        route: "/import",
        displayName: "Import Blacklist",
        isOn: false,
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
        categoriesRadio: {
            isAll: true,
            isCustom: false
        },
        categories: ["medical", "religion", "alcohol", "shopping"],
        downloadsPerCategory: 200,
        connectTimeout: 5000,
        readTimeout: 5000,
        threadsNumber: 50
    },
    extract: {
        route: "/extract",
        displayName: "Extract features",
        isOn: false,
        categoriesRadio: {
            isAll: true,
            isCustom: false
        },
        isTextMain: true,
        isTextFromTags: true,
        isNgrams: true,
        isTagStat: true,
        defaultSkipTags: true,
        defaultTextTags: true,
        categories: ["medical", "religion", "alcohol", "shopping"],
        tagsWithText: ["h1", "h2", "h3", "title", "a", "b", "img", "meta:description", "meta:keywords"],
        maxNGramSize: 6,
        tagsToSkip: ["html", "head", "title", "body"]
    },
    dataSplit: {
        route: "/split",
        displayName: "Create data set",
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
                websitesPerCategory: 2000
            }
        ]
    },
    prepare: {
        route: "/prepare",
        displayName: "Create and run experiments",
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
                tagName: "",
                nGramSize: "",
                featuresByCategory: 50,
                roundToDecimalPlaces: "",
                normalizeRatio: "",
                experiments: []
            }
        ]
    },
    schemes: {
        route: "/schemes",
        displayName: "Generate schemes",
        isOn: false,
        templatesFolder: "\\templates\\",
        experiments: [
            "exp_1",
            "exp_2",
            "exp_3",
            "united_1"
        ],
        rapidMinerPath: "C:\\My Programs\\RapidMiner\\RapidMiner5\\scripts\\rapidminer.bat"
    }
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