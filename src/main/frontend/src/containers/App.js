import React, {Component} from "react";
import injectTapEventPlugin from "react-tap-event-plugin";
import {deepOrange500} from "material-ui/styles/colors";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Header from "./../components/Header";
import Footer from "./../components/Footer";
import {Container} from "react-grid-system";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as connectionActions from "./../actions/connectionActions";
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
        const { connectionReducer } = this.props;
        const { tabChanged } = this.props.connectionActions;

        const footerParam = {
            activeTab: connectionReducer.getIn(['activeTab']),
            tabChanged: tabChanged
        };

        return (
            <MuiThemeProvider muiTheme={muiTheme}>
                <Container style={styles.container}>
                    <Header />
                    <main style={styles.main}>
                        {this.props.children}
                    </main>
                    <Footer {...footerParam}/>
                </Container>
            </MuiThemeProvider>
        );
    }
}

function mapStateToProps(state) {
    return {
        connectionReducer: state.connectionReducer,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        connectionActions: bindActionCreators(connectionActions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)