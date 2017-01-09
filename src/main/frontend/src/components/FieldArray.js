import React, {PropTypes} from "react";
import {Col, Row} from "react-grid-system";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import ListRenderer from "./../components/ListRenderer";
import {Toolbar, ToolbarGroup} from "material-ui/Toolbar";

const style = {
    leftToRight: {
        justifyContent: "flex-start",
        backgroundColor: "white",
    },
    alignCenter: {
        textAlign: "center"
    }
};

const FieldArray = ({title, elements, onAddAction, onEditAction,
onDeleteAction, addTooltip, removeTooltip, editTooltip}) => {

    return (
        <article>
            <Toolbar style={style.leftToRight}>
                <ToolbarGroup>
                    <IconButton onClick={onAddAction} tooltip={addTooltip}>
                        <FontIcon className="fa fa-plus"/>
                    </IconButton>
                </ToolbarGroup>

                <ToolbarGroup>
                        <span style={style.alignCenter}>
                            {title}
                        </span>
                </ToolbarGroup>

            </Toolbar>

            <Row>
                <Col md={12} xs={12}>
                    <ListRenderer removeTooltip={removeTooltip} editTooltip={editTooltip}
                                  elements={elements} onDeleteAction={ onDeleteAction}
                                  onEditAction={onEditAction}
                    />
                </Col>
            </Row>
        </article>
    )
};


FieldArray.propTypes = {
    title: PropTypes.string.isRequired,
    elements: PropTypes.array.isRequired,
    onEditAction: PropTypes.func.isRequired,
    onDeleteAction: PropTypes.func.isRequired,
    onAddAction: PropTypes.func.isRequired,
    addTooltip: PropTypes.string,
    removeTooltip: PropTypes.string,
    editTooltip: PropTypes.string
};

export default FieldArray;