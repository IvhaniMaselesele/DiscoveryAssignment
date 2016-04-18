function getShortestPath() {
    var destination = document.getElementById('planetname').value;
    $.get('/shortestPath/' + destination, function () {
        window.location.href = "/getPlanets";
    });

}

function getUpdateType() {
    var updateType = document.getElementById('updateType').value;
    if (updateType == 'add') {
        document.getElementById('updateTypeAdd').className = '';
        $.get('/getPlanets', function (data) {
            //alert(data);
        });
    }
    else if (updateType == 'delete') {

    }
    else if (updateType == 'update') {

    }

}


function updateTypeAdd() {
    var updateType = document.getElementById('updateTypeAddDropdown').value;
    if (updateType == 'addPlanet') {
        document.getElementById('updateTypeAddPlanet').className = '';
        document.getElementById('updateTypeAddRoute').className = 'hidden';
        $.get('/getPlanets', function (data) {
            //alert(data);
        });
    }
    else if (updateType == 'addRoute') {
        document.getElementById('updateTypeAddRoute').className = '';
        document.getElementById('updateTypeAddPlanet').className = 'hidden';
        $.get('/getPlanets', function (data) {
            //alert(data);
        });
    }
}


function addNewPlanet() {
    var planetName = document.getElementById('planetName').value;
    $.get('/addNewPlanet/' + planetName, function () {
        alert("d");
        //window.location.href = "/getPlanets";
    });

}


function submitEditedPlanet(v, vName) {
    var newPlanetName = document.getElementById('planetName').value;
    var sendToController = v + ',' + newPlanetName;
    $.put('/editPlanetPageSubmit/' + sendToController, function (data, status) {

        alert('You have changed the name of planet ' + vName + ' to planet ' + data.name);
        alert(data);
    });

}
