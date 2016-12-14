import React, {Component} from "react";
import LinearProgress from "material-ui/LinearProgress";
import {Row} from "react-grid-system";
import "./../../node_modules/font-awesome/css/font-awesome.min.css";

const style = {
    progress: {
        margin: "auto",
        width: "50%",
        padding: "10px",
        backgroundColor: "white"
    },
    notificationBar: {
        textAlign: "center",
        marginTop: "13px"
    }
};

class MyProgressBar extends Component {
    render(){
        return(
            <div>
                <Row>
                    <div style={style.progress}>
                        <LinearProgress mode="determinate" value={this.props.completed}/>
                    </div>
                </Row>

                <div style={style.notificationBar}>
                    {this.props.status}
                </div>
            </div>
        )
    }
}
export default MyProgressBar;