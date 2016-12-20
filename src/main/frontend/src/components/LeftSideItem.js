import MyListItem from "./MyListItem";
import React, {PropTypes} from "react";
import Immutable from "immutable";

const LeftSideItem = ({names}) => {

    const listGenerator = (elements) => {

        return(
            elements.map( x => x.toObject())
                .map( elem => <MyListItem text={elem.name} key={elem.key}/>)
        );

    };

    return (
        <div>
            { listGenerator(names) }
        </div>
    )
};

LeftSideItem.propTypes = {
    names: PropTypes.instanceOf(Immutable.Set).isRequired,
};

export default LeftSideItem;