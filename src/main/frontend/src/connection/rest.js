/**
 * @param url to get result from
 * @returns Promise with get-query result
 */
export function getQuery(url) {
    return genericFetch(url, 'GET');
}

/**
 * @param url to get result from
 * @param myObj (post parameter)
 * @returns Promise with post-query result
 */
export function postQuery(url, myObj) {
    return genericFetch(url, 'POST', myObj);
}

/**
 * do actual fetch job
 */
function genericFetch(url, method, myObj) {
    let {myRequest, myInit} = prepareFetch(url, method, myObj);

    return fetch(myRequest, myInit).then(function (response) {
        if (response.status === 200)
            return response.json();
        else
            throw new Error('Something went wrong on api server!');
    }).catch(function (ex) {
        console.log('parsing failed', ex)
    });
}

/**
 * set necessary headers
 */
function prepareFetch(url, method, myObj) {
    let myRequest = new Request(url);
    let myInit = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    };
    if (method === 'POST') {
        myInit.body = JSON.stringify(myObj);
    }
    return {myRequest, myInit};
}
