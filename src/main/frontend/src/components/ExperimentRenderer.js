import React, {PropTypes} from "react";
import {List} from "material-ui/List";
import SingleExperimentAdder from "./SingleExperimentAdder";

const ExperimentRenderer = ({
                             elements, onListElementAdd, onListElementEdit,
                             onListElementDelete, onInputFieldChange, style
                         }) => {

    const changeMe = (key, e) => {
        const path = ['prepare', 'experiments'];
        path.push(key, e.target.name);
        console.log(path);

        onInputFieldChange(e.target.value, path);
    };

    const changeSelectField = (key, name, value) => {
        const path = ['prepare', 'experiments'];
        path.push(key, name);
        console.log(path, value);

        onInputFieldChange(value, path);
    };


    const assignExperiments = (elem) => {
        return elem.has('experiments')
            ?
            elem.getIn(['experiments']).toArray()
            :
            null;
    };

    const listGenerator = (elements) => {

        return (
            elements.map(elem => {

                const key = elements.indexOf(elem);

                const params = {
                    experiments: {
                        elements: assignExperiments(elem),
                        title: "Choose experiments",
                        placeholder: "input and press Enter to submit",
                        whereToSave: ['prepare', 'experiments', key, 'experiments'],
                        onAdd: onListElementAdd,
                        onEdit: onListElementEdit,
                        onDelete: onListElementDelete,
                        listElementStyle: style.listElementWidth
                    },
                    inputFields: {
                        name: elem.getIn(['name']),
                        description: elem.getIn(['description']),
                        dataSetName: elem.getIn(['dataSetName']),
                        mode: elem.getIn(['mode']),
                        type: elem.getIn(['type']),
                        IDF_Treshold: elem.getIn(['IDF_Treshold']),
                        IDF_Type: elem.getIn(['IDF_Type']),
                        TF_Type: elem.getIn(['TF_Type']),
                        featuresByCategory: elem.getIn(['featuresByCategory']),
                        tagName: elem.getIn(['tagName']),
                        nGramSize: elem.getIn(['nGramSize']),
                        roundToDecimalPlaces: elem.getIn(['roundToDecimalPlaces']),
                        normalizeRatio: elem.getIn(['normalizeRatio']),
                        changeEvent: (e) => changeMe(key, e),
                        changeSelectField: (name, value) => changeSelectField(key, name, value)
                    },
                    orderNum: key + 1
                };

                return <SingleExperimentAdder {...params} key={key}/>
            })
        );
    };

    return (
        <List style={style.noPadding}>
            {listGenerator(elements)}
        </List>
    );
};

ExperimentRenderer.propTypes = {
    elements: PropTypes.array.isRequired,
    onListElementAdd: PropTypes.func.isRequired,
    onListElementEdit: PropTypes.func.isRequired,
    onListElementDelete: PropTypes.func.isRequired,
    onInputFieldChange: PropTypes.func.isRequired,
    style: PropTypes.object.isRequired,
};

export default ExperimentRenderer;