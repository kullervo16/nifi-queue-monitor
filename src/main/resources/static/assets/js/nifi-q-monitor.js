

$(document).ready(function(){
    console.log("Loading data");
    $.getJSON( "../servers/", function( data ) {

        $.each( data, function( key, val ) {
            // add a
            if(val.state === 'BLOCKED') {
                $("#serverStatus").append('<button type="button" class="btn btn-danger btn-round">' + val.name + '</button>');
            } else {
                $("#serverStatus").append('<button type="button" class="btn btn-success btn-round">' + val.name + '</button>');
            }
        });

        // TODO : persist selection in URL
        // TODO : reload automatically
        // display the queues of the selected one (per default, the first)
        for(var i=0;i<data[0].queues.length;i++) {
            var q = data[0].queues[i];
            console.log(q.displayName);
            var innerContent = '<div class="row">';
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

            innerContent += '<div class="col info-title"><a href="'+q.url+'"><i class="fa fa-question-circle"></i></a></div>';
            innerContent += "</div><br/>";

            $("#queues").append(innerContent);
        }

        
    });
});
