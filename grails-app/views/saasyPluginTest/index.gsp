<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"></script>
<style>
.pln{color:#000}@media screen{.str{color:#080}.kwd{color:#008}.com{color:#800}.typ{color:#606}.lit{color:#066}.pun,.opn,.clo{color:#660}.tag{color:#008}.atn{color:#606}.atv{color:#080}.dec,.var{color:#606}.fun{color:red}}@media print,projection{.str{color:#060}.kwd{color:#006;font-weight:bold}.com{color:#600;font-style:italic}.typ{color:#404;font-weight:bold}.lit{color:#044}.pun,.opn,.clo{color:#440}.tag{color:#006;font-weight:bold}.atn{color:#404}.atv{color:#060}}pre.prettyprint{padding:2px;border:1px solid #888}ol.linenums{margin-top:0;margin-bottom:0}li.L0,li.L1,li.L2,li.L3,li.L5,li.L6,li.L7,li.L8{list-style-type:none}li.L1,li.L3,li.L5,li.L7,li.L9{background:#eee}

#log {
	margin:20px;
}

</style>
</head>
<body>
	<div class="container">
		<h2>Saasy API Testing</h2>
		<h5><g:link uri="${grailsApplication.config.saasy.baseUrl}/apiDoc" target="_new">Saasy API Docs</g:link></h5>
		<form>
		  <div class="form-group">
		    <label for="service-name">Service</label>
		    <div id="service-name" class="form-control-static">
		    	saasyServicePlan
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="method">Method</label>
			<select class="form-control" name="method">
			<optgroup label="saasyServicePlan"/>
			  <option data-method="GET">get</option>
			  <option selected="true" data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServicePlan"/>
			  <option data-method="GET">get</option>
			  <option selected="true" data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServicePlanProductType"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServiceSubscriber"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyPaymentSource"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServiceSubscription"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServiceSubscriptionProductType"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServiceOrder"/>
			  <option data-method="GET">get</option>
			  <option data-method="GET">list</option>
			  <option data-method="POST">create</option>
			  <option data-method="PUT">update</option>
			  <option data-method="DELETE">delete</option>
			  <option data-method="POST">bulkUpdateAttributes</option>
			<optgroup label="saasyServiceLimit"/>
			  <option data-method="GET">check</option>
			  <option data-method="GET">count</option>
			  <option data-method="GET">ttl</option>
			  <option data-method="GET">increment</option>
			  <option data-method="GET">clear</option>
			  <option data-method="GET">reset</option>
			<optgroup label="saasyComposite"/>
			  <option data-method="POST">saveSubscriberInformation</option>
			<optgroup label="aasSaasy"/>
			  <option data-method="POST">syncAppWithSaasyServicePlans</option>
			  <option data-method="POST">syncSaasyWithAppServicePlans</option>
			  <option data-method="POST">syncSaasyWithAppServiceOrders</option>
			</select>
		  </div>
		  <div class="form-group">
		    <label for="method">Query String (include &amp;)</label>
			<input class="form-control" name="query-string" value=""/>
		  </div>
		  <div class="form-group hide" id="request-form-group">
		    <label for="request-body">Request Body</label>
		    <textarea name="request-body" class="form-control" rows="8">
{
  "name": "value"
}
		    </textarea>
		  </div>
		</form>
		<div class="text-center">
		<button id="submit-btn" class="btn btn-success">Submit</button>
		</div>
		<pre id="log" class="prettyprint" style="height:400px;overflow-y:auto"></pre>
	</div>
	<script>
	$(document).ready(function() {
		$( "#log" ).hide();
		$('select[name=method]').on('change', function() {
			$( "#log" ).addClass('hide');
			var option = $('option:selected');
			var httpMethod = option.data('method') || "POST";
			var api = option.closest('optgroup').attr('label');
			$('#service-name').text(api);
			if (httpMethod != 'GET') {
				$('#request-form-group').removeClass('hide');
			} else {
				$('#request-form-group').addClass('hide');
			}
		});

		$("#submit-btn").on('click', function() {
			var option = $('option:selected');
			var api = option.closest('optgroup').attr('label');
			var call = $('select[name=method]').val();
			var httpMethod = option.data('method') || "POST";
			var request = $.ajax({
			  url: "/admin/aasPlusSaasyTest/command?typeName="+api+'&command='+call+$('input[name=query-string]').val(),
			  method: httpMethod,
			  processData: false,
			  data: httpMethod != 'GET' ? $('textarea[name=request-body]').val() : null,
			  contentType: "application/json"
			}).done(function( data, textStatus, textStatus ) {
			  var str = JSON.stringify(data, null, 4);
			  $( "#log" ).removeClass('hide');
			  $( "#log" ).show();
//			  $( "#log" ).text( textStatus.responseText );
			  $( "#log" ).text( str );
			  //prettyPrint();
			});			
		});
	});
	</script>
</body>
</html>