import React from "react";

const style = {
    title: {
        textAlign: "center",
        marginTop: "20px",
        marginLeft: "20px",
        marginRight: "20px",
        marginBottom: "1px"
    },
    article: {
        fontSize: "18px",
        margin: "20px",
    },
    list: {
        padding: "10px"
    }
};

const Welcome = () => {

    return (
        <div>
            <h1 style={style.title}>Welcome to website classification utility!</h1>
            <h2 style={style.title}>This software infrastructure makes experiments
                with Data Mining methods easier</h2>

            <article style={style.article}>
                <header>
                    It allows you to:
                </header>

                <ul>
                    <li style={style.list}>
                        import data from categorized websites
                    </li>
                    <li style={style.list}>
                        download HTML sources of web pages
                    </li>
                    <li style={style.list}>
                        extract various features for the experiments
                    </li>
                    <li style={style.list}>
                        organize saved websites into datasets with different
                        parameters (categories in use, feature types, etc)
                    </li>
                    <li style={style.list}>
                        generate schemes for RapidMiner software
                    </li>
                    <li style={style.list}>
                        create experiments from datasets and run them in batch
                    </li>
                </ul>
            </article>
        </div>
    );
};

export default Welcome;