/**
 * transforms array of maps <string, Map> into array of simple objects,
 * notice new attribute "name" !
 * e.g:
 *  [
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
 *  ]
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
    deeplyNestedObject.map( (key, value) => key.toObject() )
        .map( (k,v) => {
            k.name = v;
            return k;
        }).map( k => plainArray.push(k));

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
