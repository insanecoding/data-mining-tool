import React, {PropTypes} from "react";
import {Col, Row} from "react-grid-system";

const style = {
    textAlign: "center"
};

const RightSideForm_Experiment = ({title, children}) => {

        return(
            <div>
                <Row>
                    <Col xs={12} md={12}>
                        <h1 style={style}>{title}</h1>
                    </Col>
                    <Col xs={12} md={12}>
                    {children}
                    </Col>
                </Row>
            </div>
        )
};

RightSideForm_Experiment.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.node
};

export default RightSideForm_Experiment