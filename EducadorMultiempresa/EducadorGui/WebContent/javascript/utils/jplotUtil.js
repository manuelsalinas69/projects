$(document).ready(function(){document.documentElement.setAttribute('lang', 'es');});



function plotBarReport(selector, titleText, data){
	try {
		checkData(data);
	} catch (e) {
		addMessageToContainer(selector,e );
		return;
	}
	$.jqplot.config.enablePlugins = true;
	$(selector).children().remove();
	$(selector).jqplot( [data], {
		// Only animate if we're not using excanvas (not in IE 7 or IE 8)..
		title:titleText,
		animate: !$.jqplot.use_excanvas,
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			pointLabels: { show: true },
			rendererOptions: {
				// Set the varyBarColor option to true to use different colors for each bar.
				// The default series colors are used.
				varyBarColor: true
			}
		},
		axes: {
			xaxis: {
				renderer: $.jqplot.CategoryAxisRenderer,

			},
			tickOptions:{ 
				fontSize:'10pt', 
				angle:-40
			}
		},
		highlighter: { show: false },
		cursor:{
			show:true,
			zoom:true,
			showTooltip:false

		}
	});


}


function plotBarLegendReport(selector, titleText, data){
	try {
		checkData(data);
	} catch (e) {
		addMessageToContainer(selector,e );
		return;
	}
	$.jqplot.config.enablePlugins = true;
	$(selector).children().remove();
	var leg=[];
//	var ticks=['a'];
//	
	var transData=[];
//	var s1=[2];
//	var s2=[8];
//	var s3=[90];
	for ( var int = 0; int < data.length; int++) {
		leg[int] = data[int][0];
		transData[int]=data[int][1];
	}
	$(selector).jqplot( [transData], {
		// Only animate if we're not using excanvas (not in IE 7 or IE 8)..
		title:titleText,
		animate: !$.jqplot.use_excanvas,
		
		
		seriesDefaults:{
			
			renderer:$.jqplot.BarRenderer,
			pointLabels: { show: true },
			rendererOptions: {
				// Set the varyBarColor option to true to use different colors for each bar.
				// The default series colors are used.
				varyBarColor: true,
				 //barDirection: 'horizontal'
			},
			//ticks:ticks,
		},
		axes: {
			xaxis: {
				
				renderer: $.jqplot.CategoryAxisRenderer,
				tickRenderer: $.jqplot.AxisTickRenderer,
		        //ticks:ticks,
				tickOptions: {
		        	showLabel:true,
		        	angle:-40,
		        	//textColor:'white',
		        	//formatter: function(format, value) { return "-"; } 
		        		
		        		
		        }
				
				
			}
			
		},
		highlighter: { show: false },
		legend: {
		    show: false,
		    location: 'e',
		    placement: 'outside',
		    labels:leg,
		    
		    
		},
		cursor:{
			show:true,
			zoom:true,
			showTooltip:false

		}

	});

	

}

function plotDateReport(selector,titleText, data){
	try {
		checkData(data);
	} catch (e) {
		addMessageToContainer(selector,e );
		return;
	}
	$.jqplot.config.enablePlugins = true;
	$(selector).children().remove();

	$(selector).jqplot([data], {
		title:titleText,
		animate: !$.jqplot.use_excanvas,
		axes:{
			xaxis:{
				renderer:$.jqplot.DateAxisRenderer, 
				rendererOptions:{
					tickRenderer:$.jqplot.CanvasAxisTickRenderer
				},
				tickOptions:{ 
					fontSize:'10pt', 
					fontFamily:'Tahoma', 
					angle:-40
				},
				tickInterval:'3 hours'
			},
			yaxis:{
				rendererOptions:{
					tickRenderer:$.jqplot.CanvasAxisTickRenderer},
					tickOptions:{
						fontSize:'10pt', 
						fontFamily:'Tahoma', 
						angle:30
					}
			}
		},
		series:[{ lineWidth:4, markerOptions:{ style:'square' } }],
		cursor:{
			show:true,
			zoom:true,
			showTooltip:false

		}
	});
}



function plotPieReport(selector, titleText, data) {
	try {
		checkData(data);
	} catch (e) {
		addMessageToContainer(selector,e );
		return;
	}
	$.jqplot.config.enablePlugins = true;
	$(selector).children().remove();
	$(selector).jqplot( [data], {
			title:titleText,
			animate: !$.jqplot.use_excanvas,
			gridPadding: {top:0, bottom:38, left:0, right:0},
	        seriesDefaults:{
	            renderer:$.jqplot.PieRenderer, 
	            trendline:{ show:false }, 
	            rendererOptions: { padding: 8, showDataLabels: true },
	            
	        },
	        legend:{
	            show:true, 
	            placement: 'outside', 
	            rendererOptions: {
	                numberRows: 1
	            }, 
	            location:'s',
	            marginTop: '15px'
	        },
	        seriesColors: [ "#BCED91", "#FFA07A" ]
	       
	    });
}

function checkData(data){
	if (typeof data == 'undefined') {
		throw ('Dato no recibido');
		return;
	}
	if (!data) {
		throw ('Dato no recibido. Verifique sus parametros.');
		
	}
	if (data.length==0) {
		throw ('No se encontraron resultados');	
	}
}

function addMessageToContainer(selector, message){
	$(selector).children().remove();
	$(selector).append("<p>"+message+"</p>");
}