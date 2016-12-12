import React, {Component} from "react";
import injectTapEventPlugin from "react-tap-event-plugin";
import {deepOrange500} from "material-ui/styles/colors";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Header from "./components/Header";
import Body from "./components/Body";
import Footer from "./components/Footer";
import {Container} from "react-grid-system";

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
    }
};

class App extends Component {

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
                <Container>
                    <Header />
                    <Body/>
                    <Footer/>
                </Container>
            </MuiThemeProvider>
        );
    }
}

export default App;