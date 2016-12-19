import React, {Component} from "react";
import TextField from "material-ui/TextField";
import {Col, Row} from "react-grid-system";


export default class RightSideForm_Uncompress extends Component {
    render() {
        return(
            <div>
                <h1>Uncompress</h1>
                <Row>
                    <Col xs={12}>
                        <TextField
                            hintText="Hint Text"
                            floatingLabelText="Username"
                            name="userName"
                            floatingLabelFixed={true}
                            onChange={this.props.formHandler}
                        />
                        <TextField
                            hintText="Hint Text 2"
                            floatingLabelText="Password"
                            floatingLabelFixed={true}
                            type="password"
                            name="password"
                            onChange={this.props.formHandler}
                        />
                    </Col>
                </Row>
            </div>
        )
    }
}