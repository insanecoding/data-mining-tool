import React, {PropTypes} from "react";
import TodoItem from "./ListItem";
import {List} from "material-ui";
import TodoTextInput from "./InputField";

const defaultStyle = {
    header: {
        marginLeft: 20
    },
    main: {
        width: "25%",
        marginLeft: 20
    }

};

const MainSection = ({title, placeholder, todos, actions}) => {

    const filteredTodos = todos.filter(() => true);

    const handleSave = (text) => {
        if (text.length !== 0) {
            actions.addTodo(text);
        }
    };

    return (
        <section>
            <header className="header">
                <h1 style={defaultStyle.header}>{title}</h1>
                <TodoTextInput newTodo
                               onSave={handleSave}
                               placeholder={placeholder}/>
            </header>

            <section className="main" style={defaultStyle.main}>
                <List className="todo-list">
                    {filteredTodos.map(todo =>
                        <TodoItem key={todo.id} todo={todo} {...actions} />
                    )}
                </List>
            </section>
        </section>
    );

};

MainSection.propTypes = {
    todos: PropTypes.array.isRequired,
    actions: PropTypes.object.isRequired,
    title: PropTypes.string.isRequired,
    placeholder: PropTypes.string.isRequired
};

export default MainSection;
