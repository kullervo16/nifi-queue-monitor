function loadData() {
    //console.log("Loading data");
    $.getJSON( "../servers/", function( data ) {

        for(var serverIndex=0;serverIndex<data.length;serverIndex++)  {
            var val = data[serverIndex];
            var updateTime = val.collectionTime;
            if(collectionTimes[val.name] !== undefined) {
                var lastCollectionTime = collectionTimes[val.name];
                if(updateTime <= lastCollectionTime) {
                    return;
                }
            }
            collectionTimes[val.name] = updateTime;
            var pattern = /[^0-9a-zA-Z]+/g;
            var stateId = val.name.replace(pattern,"_")+'_state';
            $("#"+stateId).remove();

            var serverStateClass = 'default';
            if(val.state === 'BLOCKED') {
                serverStateClass = 'danger';
            } else if(val.state === 'UNREACHABLE') {
                serverStateClass = 'default';
            } else {
                serverStateClass = 'success';
            }
            $("#serverStatus").append('<button type="button" class="btn btn-'+serverStateClass+' btn-round" id="'+stateId+'" onclick="selectServer('+serverIndex+')">' + val.name + '</button>');
            if( serverIndex != selectedIndex) {
                // only provide the queue details for the selected server
                continue;
            }
            $("#selectedServer").text(val.name);
            $(".queueInfo").remove();

            // display the queues of the selected one (per default, the first)
            for(var i=0;i<val.queues.length;i++) {

                var q = val.queues[i];
                //console.log(q.displayName);
                var innerContent = '<div class="row queueInfo">';
                var clazz = 'success';
                innerContent += '<div class="col-4 info-title">\n' + q.displayName +'</div>';
                if(q.blocked) {
                    innerContent += '<div class="col info-title">\<i class="fa fa-stop" style="color:#f5593d"></i></div>';
                    clazz = 'danger';
                } else {
                    innerContent += '<div class="col info-title">\<i class="fa fa-play success" style="color: #6bd098"></i></div>';
                }
                innerContent += '<div class="col-6"><div class="progress">\n' +
                    '                        <div class="progress-bar progress-bar-'+clazz+'" role="progressbar" style="width: '+q.filledPercentage+'%" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>\n' +
                    '                    </div><br/></div>';

                innerContent += '<div class="col info-title"><a href="'+q.url+'" target="_blank"><i class="fa fa-question-circle"></i></a></div>';
                innerContent += "</div><br class='queueInfo'/>";


                $("#queues").append(innerContent);
            }
            $("#idle").text(data[0].queuesIdle);
            $("#busy").text(data[0].queuesBusy);
            $("#blocked").text(data[0].queuesBlocked);
            $("#ignored").text(data[0].queuesIgnored);
        }


    });

}

function selectServer(index) {
    selectedIndex = index;
    collectionTimes = {};
    loadData();
}

var collectionTimes = {};
var selectedIndex = 0;

$(document).ready(function(){
    window.setInterval(loadData, 1000);

});
