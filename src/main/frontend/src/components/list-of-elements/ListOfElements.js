import React, {PropTypes} from "react";
import MyListItem from "./MyListItem";
import {List} from "material-ui";
import InputField from "./InputField";

const defaultStyle = {
    header: {
        // textAlign: "center",
        marginBottom: "0px"
    },
    flex: {
        display: "flex",
        justifyContent: "center"
    }
};

const MainSection = ({title, placeholder, listElementStyle, elements, whereToSave, onAdd, onEdit, onDelete}) => {

    const handleSave = (text) => {
        if (text.length !== 0) {
            onAdd(text, whereToSave);
        }
    };

    return (
        <section>
            <header>
                <h1 style={defaultStyle.header}>{title}</h1>
                <div style={defaultStyle.flex}>
                    <InputField newElement
                            onSave={handleSave} placeholder={placeholder}/>
                </div>
            </header>

            <section style={defaultStyle.flex}>
                <List style={listElementStyle}>
                    {elements.map(element =>
                        <MyListItem key={element} element={element}
                                    onDelete={onDelete} onEdit={onEdit} whereToSeek={whereToSave}/>
                    )}
                </List>
            </section>
        </section>
    );

};

MainSection.propTypes = {
    elements: PropTypes.array.isRequired,
    whereToSave: PropTypes.array.isRequired,
    title: PropTypes.string.isRequired,
    placeholder: PropTypes.string.isRequired,
    onAdd: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
    listElementStyle: PropTypes.object
};

export default MainSection;
