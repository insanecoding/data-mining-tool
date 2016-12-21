import React, {Component, PropTypes} from "react";
import {Col, Row} from "react-grid-system";


const RightSideForm_Experiment = ({title, children}) => {

        return(
            <div>
                <h1>{title}</h1>
                {children}
            </div>
        )
};

RightSideForm_Experiment.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.node
};

export default RightSideForm_Experiment