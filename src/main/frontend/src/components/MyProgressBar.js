import React, {PropTypes} from "react";
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

const MyProgressBar = ({visible, percentsProgress, statusMsg}) => {

    return (
        <div>
            {
                (visible === true) ? <div style={style.progress}>
                        <LinearProgress mode="determinate" value={percentsProgress}/>
                    </div> : null
            }

            <div style={style.notificationBar}>
                {statusMsg}
            </div>
        </div>
    )
};

MyProgressBar.propTypes = {
    percentsProgress: PropTypes.number.isRequired,
    visible: PropTypes.bool.isRequired,
    statusMsg: PropTypes.string.isRequired,
};

export default MyProgressBar;

