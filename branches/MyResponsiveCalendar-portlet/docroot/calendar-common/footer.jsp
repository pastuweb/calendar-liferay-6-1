</div>
<div style="text-align:center;">
	<!--  Copyright Telecom Italia LAB - Via Reiss Romoli Torino -->
</div>
<br>

<script type="text/javascript" >

var icons = {
   	header: "ui-icon-circle-arrow-e",
   	activeHeader: "ui-icon-circle-arrow-s"
};	

var dialog;
var form;
var today = new Date();
	
	
$(document).ready(function(){		 		         
	
		$("#calendar").dp_calendar({
			date_selected: today,
			events_array: events_array
		}); 
		
		$("#datepickerFrom").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#datepickerTo").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#allday").button();
		$("#allday").click(function() {
			if($(this).is(':checked')){
				$("#slider-durata").slider('value',24);
				$("#amountOre").text(24);
				$("#allday").val("true");
			}else{
				$("#slider-durata").slider('value',2);
				$("#amountOre").text(2);
				$("#allday").val("false");
			}
		});
		$("#reminds").buttonset();
		$("#repeat").buttonset();
		$("#giornoSettimana").buttonset();

		
		$("#repeat input:radio").click(function() {
			  var id = $(this).attr("id");
			  $('input[name=giornoSettimana]').attr('checked',false);
			  $('.divRepeat').hide();
			  $('#'+id+'_form').show();
			  if(id != 'repeat_never'){
				  $('#divDataFine').show();
			  }else{
				  $('#divDataFine').hide();
			  }
	      });
		
		$("#accordion").accordion({
	    	icons: icons,
	    	collapsible: true
	   	});
		$("#slider-durata").slider({
		      range: "max",
		      min: 1,
		      max: 24,
		      value: 2,
		      slide: function( event, ui ) {
		        $( "#amountOre" ).text( ui.value );
		        $( "#inputOre" ).val( ui.value );
		      }
		    });
		$("#amountOre").text($("#slider-durata" ).slider( "value" ));
		$("#inputOre").val($("#slider-durata" ).slider( "value" ));
		
		$("#slider-ora-inizio").slider({
		      range: "max",
		      min: 0,
		      max: 23,
		      value: 12,
		      slide: function( event, ui ) {
		        $( "#amountOraInizio" ).text( ui.value );
		        $( "#inputOraInizio" ).val( ui.value );
		      }
		    });
		$("#amountOraInizio").text($("#slider-ora-inizio" ).slider( "value" ));
		$("#inputOraInizio").val($("#slider-ora-inizio" ).slider( "value" ));
		
		$("#slider-minuti-inizio").slider({
		      range: "max",
		      min: 0,
		      max: 59,
		      value: 0,
		      slide: function( event, ui ) {
		        $( "#amountMinutiInizio" ).text( ui.value );
		        $( "#inputMinutiInizio" ).val( ui.value );
		      }
		    });
		$("#amountMinutiInizio").text($("#slider-minuti-inizio" ).slider( "value" ));
		$("#inputMinutiInizio").val($("#slider-minuti-inizio" ).slider( "value" ));
				
    	dialog = $( "#dialog-formInserisciEvento" ).dialog({
    	      autoOpen: false,
    	      resizable: true,
    	      modal: true,
    	      width: 400,
    	      buttons: [
    	                {
    	                    text: "Crea Evento",
    	                    click: function() {
    	                    	//alert("Evento in Creazione");
    	                    	form[0].submit();
    	                        $(this).dialog( "close" );
    	                    },
    	                    type: "submit"
    	                },
    	                {
    	                    text: "Close",
    	                    click: function() {
    	                    	form[0].reset();
    	                        $(this).dialog( "close" );
    	                    }
    	                }
    	            ]
    	});
    	form = dialog.find("form");
    	
    	$( "#addEvent" ).button().on( "click", function() {
  	      	dialog.dialog( "open" );
  		}); 
    	
    	
});

</script>
