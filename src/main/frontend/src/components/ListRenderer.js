import React, {PropTypes} from "react";
import {List, ListItem} from "material-ui/List";
import FontIcon from "material-ui/FontIcon";
import IconButton from "material-ui/IconButton";
import {Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle} from "material-ui/Toolbar";

const style = {
    leftToRight: {
        justifyContent: "flex-start",
        backgroundColor: "rgba(0, 0, 0, 0.0)",
    },
};

const ListRenderer = ({elements, onDeleteAction, onEditAction}) => {

    const listGenerator = (elements) => {

        return (
            elements.map(elem =>

                <Toolbar key={elem.getIn(['key'])} style={style.leftToRight}>
                    <ToolbarGroup>
                        <IconButton tooltip={"remove blacklist"}
                                    onClick={ () => onDeleteAction(elem.getIn(['key'])) }>
                            <FontIcon className="fa fa-minus"/>
                        </IconButton>
                    </ToolbarGroup>

                    <ToolbarGroup>
                        <IconButton tooltip={"edit blacklist"}
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
        <List style={{padding: "0px"}}>
            {listGenerator(elements)}
        </List>
    );
};

ListRenderer.propTypes = {
    elements: PropTypes.array.isRequired,
    onDeleteAction: PropTypes.func.isRequired,
    onEditAction: PropTypes.func.isRequired
};

export default ListRenderer;