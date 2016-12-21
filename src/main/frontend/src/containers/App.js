import React, {Component} from "react";
import injectTapEventPlugin from "react-tap-event-plugin";
import {deepOrange500} from "material-ui/styles/colors";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Header from "./../components/Header";
import Body from "./Body";
import Footer from "./../components/Footer";
import {Container} from "react-grid-system";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";


// prevent warnings with Material UI
injectTapEventPlugin();

const muiTheme = getMuiTheme({
    palette: {
        accent1Color: deepOrange500,
    },
});

const styles = {
    body: {
        margin: "0",
        padding: "0",
        fontFamily: "sans-serif",
    },
    container: {
        display: "flex",
        minHeight: "100vh",
        flexDirection: "column",
    },
    main: {
        flex: "1"
    },
};

export default class App extends Component {

    componentDidMount = () => {
        for(let entry in styles.body){
            if (styles.body.hasOwnProperty(entry)) {
                document.body.style[entry] = styles.body[entry];
            }
        }
    };

    componentWillUnmount = () => {
        for(let entry in styles.body){
            if (styles.body.hasOwnProperty(entry)) {
                document.body.style[entry] = null;
            }
        }
    };

    render() {
        return (
            <MuiThemeProvider muiTheme={muiTheme}>
                <Container style={styles.container}>
                    <Header />
                        <Body style={styles.main}/>
                    <Footer/>
                </Container>
            </MuiThemeProvider>
        );
    }
}