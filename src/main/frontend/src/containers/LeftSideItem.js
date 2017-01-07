import MyListItem from "./../components/MyListItem";
import React, {PropTypes} from "react";

const LeftSideItem = ({components, activeFormChanged, componentToggled}) => {

    const handleClick = (elementId, newRoute) => {
        activeFormChanged(elementId, newRoute);
    };

    const onToggle = (elementName) => {
        componentToggled(elementName);
    };


    const listGenerator = (elements) => {
        return(
            elements.map( elem => <MyListItem text={elem.displayName} key={elem.key}
                                          handleClick={ () => handleClick(elem.key, elem.route)}
                                          onToggle={ () => onToggle(elem.name)}
                    />
                )
        );
    };

    return (
        <div>
            { listGenerator(components) }
        </div>
    )
};

LeftSideItem.propTypes = {
    components: PropTypes.array.isRequired,
    activeFormChanged: PropTypes.func.isRequired,
    componentToggled: PropTypes.func.isRequired,
};

export default LeftSideItem;