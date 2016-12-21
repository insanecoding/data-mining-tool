import React, {Component} from "react";
import {Col, Row} from "react-grid-system";


export default class RightSideForm_Welcome extends Component {
    render() {
        return(
            <div>
                <Row>
                    <Col xs={12} md={12}>
                        <h1 style={{textAlign: "center"}}>Click component to see settings</h1>
                    </Col>
                </Row>
            </div>
        )
    }
}