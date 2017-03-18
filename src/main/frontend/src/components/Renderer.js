import React, {PropTypes} from "react";
import {List} from "material-ui/List";
import SingleDataSetAdder from "./SingleDataSetAdder";

const Renderer = ({
                      elements, onListElementAdd, onListElementEdit,
                      onListElementDelete, onInputFieldChange, style
                  }) => {

    const changeMe = (key, e) => {
        const path = ['dataSplit', 'param'];
        path.push(key, e.target.name);
        console.log(path);

        onInputFieldChange(e.target.value, path);
    };


    const listGenerator = (elements) => {

        return (
            elements.map(elem => {

                const key = elements.indexOf(elem);

                const params = {
                    categories: {
                        elements: elem.getIn(['categories']).toArray(),
                        title: "Choose categories",
                        placeholder: "input and press Enter to submit",
                        whereToSave: ['dataSplit', 'param', key, 'categories'],
                        onAdd: onListElementAdd,
                        onEdit: onListElementEdit,
                        onDelete: onListElementDelete,
                        listElementStyle: style
                    },
                    inputFields: {
                        dataSetName: elem.getIn(['dataSetName']),
                        description: elem.getIn(['description']),
                        partitionLearn: elem.getIn(['partitionLearn']),
                        lang: elem.getIn(['lang']),
                        minTextLength: elem.getIn(['minTextLength']),
                        maxTextLength: elem.getIn(['maxTextLength']),
                        websitesPerCategory: elem.getIn(['websitesPerCategory']),
                        changeEvent: (e) => changeMe(key, e)
                    },
                    orderNum: key + 1
                };

                return <SingleDataSetAdder {...params} key={key}/>
            })
        );
    };

    return (
        <List style={style.noPadding}>
            {listGenerator(elements)}
        </List>
    );
};

Renderer.propTypes = {
    elements: PropTypes.array.isRequired,
    onListElementAdd: PropTypes.func.isRequired,
    onListElementEdit: PropTypes.func.isRequired,
    onListElementDelete: PropTypes.func.isRequired,
    onInputFieldChange: PropTypes.func.isRequired,
    style: PropTypes.object.isRequired,
};

export default Renderer;