function getShortestPath() {
    var destination = document.getElementById('planetname').value;
    var withTraffic = document.getElementById('withTraffic').checked;
    alert(withTraffic);

    if (withTraffic) {
        alert(withTraffic);
        $.get('/withTraffic/', function () {
        });
    }
    $.get('/shortestPath/' + destination, function () {
        window.location.href = "/getPlanets";
    });
}