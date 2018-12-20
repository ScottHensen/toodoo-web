$.support.cors = true

$(document).ready(function() {
	
	//events
	$(document).on('click', '.deleteSymbolButton', function() {
		var $this  = $(this)
		var params = $this.attr("param").split(",")
		var clientId    = params[0]
		var portfolioId = params[1]
		var symbol      = params[2]
		
		if (params) {
			var postParams = {
							  "clientId"   : clientId,
							  "portfolioId": portfolioId,
							  "symbol"     : symbol      
							  }
		}
		
		$.ajax({
			 type:		"POST"
			,url:		"/deleteSymbolFromPortfolio"
			,headers: 	{
							'Accept': 		'application/json',
							'Content-Type': 'application/json'
						}
			,data:		JSON.stringify(postParams)
			,cache:		false
			,success:	function(json) {
							if(!json.error) {	
								location.replace("/clients/" + clientId)
							}
							console.log("json error!")
						}
			,error:		function(json) {
							alert("boo!")
						}
		})
	})
})