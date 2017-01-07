import React, {Component} from "react";

const style = {
    title: {
        textAlign: "center",
        marginTop: "20px",
        marginLeft: "20px",
        marginRight: "20px",
        marginBottom: "1px"
    },
};

const Welcome = () => {
    return(
      <h1 style={style.title}>Welcome to website classification utility!</h1>
    );
};

export default Welcome;