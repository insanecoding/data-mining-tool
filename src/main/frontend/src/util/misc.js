/**
 * transforms array of maps <string, Map> into array of simple objects,
 * notice new attribute "name" !
 * e.g:
 *  {
 *    uncompress: {
 *        archives: "\some\sub\folder\",
 *        displayName: "Uncompress blacklist",
 *        ...
 *    },
 *    import: {
 *         key: 2,
 *         displayName: "Import Blacklist",
 *         ...
 *       },
 *     ...
 *  }
 *     into:
 *  [
 *      {
 *          name: "uncompress"
 *          archives: "\some\sub\folder\",
 *          displayName: "Uncompress blacklist",
 *          ...
 *      },
 *      {
 *          name: "import",
 *          key: 2,
 *          displayName: "Import Blacklist",
 *          ...
 *      },
 *      ...
 *  ]
 *
 * @param deeplyNestedObject {Immutable.Map}
 * @returns {Array}
 */
export const mapToArray = (deeplyNestedObject) => {
    let plainArray = [];
    // surprisingly, value is simple string and key is a map, not otherwise
    deeplyNestedObject.map( (value, key) => value.setIn(['name'], key))
         // each element is map now, but we wanna simple object
        .map( map => map.toObject())
        // save object to array
        .map( k => plainArray.push(k));

    return plainArray;
};

/**
 * checks whether object has properties or empty
 * @param object {Object} object to test
 * @returns {boolean} empty or no
 */
export const isEmptyObject = (object) => {
    return Object.keys(object).length === 0;
};

export const onValueChange = (e, formName, foo) => {
    let element = e.target.value;
    if (e.target.type === "number") {
        element = parseInt(element, 10);
    }
    foo(element, e.target.name, formName);
};


export const createElements = (formReducer, whereToSeek) => {
    let newArray = [];
    formReducer.getIn(whereToSeek)
        .toArray()
        .map(elem => elem.toObject())
        .map(elem => newArray.push(elem));
    return newArray;
};
