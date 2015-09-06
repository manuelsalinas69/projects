$(document).ready(function() {
   
    $(".ph_minutes").attr('placeholder', 'minutos');
    $(".ph_hours").attr('placeholder', 'horas');
});

function toggle(id) {
	var r=jQuery("div[id*='"+id+"']").toggle();
	
	//jQuery("#"+id).toggle();
}