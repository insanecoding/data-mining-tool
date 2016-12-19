import React, {Component} from "react";
import LinearProgress from "material-ui/LinearProgress";

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

export default class MyProgressBar extends Component {
    render() {
        return (
            <div>
                {
                    (this.props.visible === true) ? <div style={style.progress}>
                            <LinearProgress mode="determinate" value={this.props.completed}/>
                        </div> : null
                }

                <div style={style.notificationBar}>
                    {this.props.status}
                </div>
            </div>
        )
    }
}