import MyLeftListItem from "./../components/MyListItem";
import React, {PropTypes} from "react";

const LeftSideItem = ({components, activeFormChanged, componentToggled}) => {

    const handleClick = (elementId, newRoute) => {
        activeFormChanged(elementId, newRoute);
    };

    const onToggle = (elementName) => {
        let params = [elementName];
        params.push("isOn");
        componentToggled(params);
    };


    const listGenerator = (elements) => {
        return(
            elements.map( elem => <MyLeftListItem text={elem.displayName} key={elem.route} value={elem.isOn}
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