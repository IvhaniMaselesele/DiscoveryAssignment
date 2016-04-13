function getShortestPath() {
    var destination = document.getElementById('planetname').value;
    $.get('/shortestPath/' + destination, function () {
        window.location.href = "/getPlanets";
    });

}