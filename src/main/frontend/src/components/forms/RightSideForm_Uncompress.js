import React, {Component} from "react";
import AdvancedTextField from "./../AdvancedTextField";
import {Col, Row} from "react-grid-system";


export default class RightSideForm_Uncompress extends Component {
    render() {
        return(
            <div>
                <h1>Uncompress</h1>
                <Row>
                    <Col xs={12}>

                        <AdvancedTextField onBlur={this.props.onBlur}
                                           hint="\sub\folder\with\archives"
                                           label={"Sub-directory with archives"}
                                           name={"archives"}
                                           validationSuccess={this.props.validationSuccess}
                                           onChangeEvent={this.props.onChangeEvent} value={this.props.value}/>

                    </Col>
                </Row>
            </div>
        )
    }
}