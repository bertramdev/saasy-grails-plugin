<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script src="https://google-code-prettify.googlecode.com/svn/loader/prettify.js"></script>
<style>
.pln{color:#000}@media screen{.str{color:#080}.kwd{color:#008}.com{color:#800}.typ{color:#606}.lit{color:#066}.pun,.opn,.clo{color:#660}.tag{color:#008}.atn{color:#606}.atv{color:#080}.dec,.var{color:#606}.fun{color:red}}@media print,projection{.str{color:#060}.kwd{color:#006;font-weight:bold}.com{color:#600;font-style:italic}.typ{color:#404;font-weight:bold}.lit{color:#044}.pun,.opn,.clo{color:#440}.tag{color:#006;font-weight:bold}.atn{color:#404}.atv{color:#060}}pre.prettyprint{padding:2px;border:1px solid #888}ol.linenums{margin-top:0;margin-bottom:0}li.L0,li.L1,li.L2,li.L3,li.L5,li.L6,li.L7,li.L8{list-style-type:none}li.L1,li.L3,li.L5,li.L7,li.L9{background:#eee}

#log {
	margin:10px;
}

</style>
</head>
<body>
	<div class="container">
	<h2>Test</h2>
	<form>
	  <div  id="payload" class="form-group">
	    <label for="api">API</label>
		<select class="form-control" name="api">
		  <option>serviceSubscriber</option>
		  <option>servicePlan</option>
		  <option>serviceOrder</option>
		  <option>servicePlanProductType</option>
		  <option>serviceSubscription</option>
		  <option>serviceInvoice</option>
		  <option>serviceLimit</option>
		  <option>composite</option>
		  <option>paymentSource</option>
		</select>
	  </div>
	  <div class="form-group">
	    <label for="method">Method</label>
		<select class="form-control" name="method">
		  <option>create</option>
		  <option>update</option>
		  <option>bulkUpdateAttributes</option>
		</select>
	  </div>
	  <div class="form-group">
	    <label for="request-body">Request Body</label>
	    <textarea name="request-body" class="form-control" rows="8">
{
  "name": "value"
}
	    </textarea>
	  </div>
	</form>
  <button id="submit-btn" class="btn btn-default">Submit</button>
	<pre id="log" class="prettyprint"></pre>
	</div>
	<script>
	$(document).ready(function() {
		$( "#log" ).hide();

		$("#submit-btn").on('click', function() {
			var request = $.ajax({
			  url: "/api/"+$('select[name=api]').val()+'/'+$('select[name=method]').val(),
			  method: "POST",
			  processData: false,
			  data: $('textarea[name=request-body]').val(),
			  contentType: "application/json"
			}).done(function( data, textStatus, textStatus ) {
			  var str = JSON.stringify(data, null, 4);
			  $( "#log" ).show();
//			  $( "#log" ).text( textStatus.responseText );
			  $( "#log" ).text( str );
			  prettyPrint();
			});			
		});
	});
	</script>
</body>
</html>