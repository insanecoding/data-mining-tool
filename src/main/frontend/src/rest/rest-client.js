export function getQuery(url) {
    genericFetch(url, 'GET').then(function (res) {
        console.log(res);
    }).catch(function (ex) {
        console.log('parsing failed', ex)
    });
}

export function postQuery(url, myObj) {
    genericFetch(url, 'POST', myObj).then(function (res) {
        console.log(res);
    }).catch(function (ex) {
        console.log('parsing failed', ex)
    });
}

function genericFetch(url, method, myObj) {
    let {myRequest, myInit} = prepareFetch(url, method, myObj);

    return fetch(myRequest, myInit).then(function (response) {
        if (response.status === 200)
            return response.json();
        else
            throw new Error('Something went wrong on api server!');
    })
}

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
