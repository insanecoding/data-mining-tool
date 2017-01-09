import React, {PropTypes} from "react";
import {List} from "material-ui/List";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import {Toolbar, ToolbarGroup} from "material-ui/Toolbar";

const style = {
    leftToRight: {
        justifyContent: "flex-start",
        backgroundColor: "rgba(0, 0, 0, 0.0)",
    },
    noPadding: {
        padding: "0px"
    }
};

const ListRenderer = ({elements, onDeleteAction, onEditAction, removeTooltip, editTooltip}) => {

    const listGenerator = (elements) => {

        return (
            elements.map(elem =>

                <Toolbar key={elem.getIn(['key'])} style={style.leftToRight}>
                    <ToolbarGroup>
                        <IconButton tooltip={removeTooltip}
                                    onClick={ () => onDeleteAction(elem.getIn(['key'])) }>
                            <FontIcon className="fa fa-minus"/>
                        </IconButton>
                    </ToolbarGroup>

                    <ToolbarGroup>
                        <IconButton tooltip={editTooltip}
                                    onClick={ () => onEditAction(elem.getIn(['key'])) }>
                            <FontIcon className="fa fa-pencil"/>
                        </IconButton>
                    </ToolbarGroup>

                    <ToolbarGroup>
                        {elem.getIn(['listName'])}
                    </ToolbarGroup>
                </Toolbar>
            )
        );
    };

    return (
        <List style={style.noPadding}>
            {listGenerator(elements)}
        </List>
    );
};

ListRenderer.propTypes = {
    elements: PropTypes.array.isRequired,
    onDeleteAction: PropTypes.func.isRequired,
    onEditAction: PropTypes.func.isRequired,
    editTooltip: PropTypes.string,
    removeTooltip: PropTypes.string
};

export default ListRenderer;