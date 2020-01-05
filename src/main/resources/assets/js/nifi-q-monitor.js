

$(document).ready(function(){
    console.log("Loading data");
    $.getJSON( "http://localhost:8080/nifi-api/flow/process-groups/root", function( data ) {
        var items = [];
        $.each( data, function( key, val ) {
            console.log(data);
        });

        
    });
});
