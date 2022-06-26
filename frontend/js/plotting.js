function getEndpointURL() {
    return "http://ec2-18-133-26-253.eu-west-2.compute.amazonaws.com:8080";
}

function getPowerURL(siteId, from, to) {
    endpoint = getEndpointURL();
    return `${endpoint}/sites/${siteId}/power?from=${from}&to=${to}`;
}

function getSiteDetailsURL(endpoint, siteId) {
    endpoint = getEndpointURL();
    return `${endpoint}/sites/${siteId}/`;
}

async function updateChart(chart, siteId) {
    from = new Date(Date.now() - 5*24*3600*1000).toISOString().split('.')[0];
    to = new Date(Date.now() + 24*3600*1000).toISOString().split('.')[0];

    return fetch(getPowerURL(siteId, from, to))
        .then(x => x.json())
        .then(x => plot(x))
        .catch(e => console.log(e))
}

function plot(arr) {
    let time = [];
    let power = [];
    arr.forEach(element => {
        time.push(element['timestamp'].replace('T', ' '));
        power.push(element['powerOutput']);
    });

    const data =  {
        x : time,
        y : power,
        type: 'scatter'
    }

    const chart = document.getElementById('chart');
    Plotly.newPlot(chart, [data]);
}

updateChart(chart, '25cfc53d-e483-44cc-aff4-ebed7762505d');










