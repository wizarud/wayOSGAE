<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
<title>WAYOS</title>
<%@ include file="fragment/env-css.jspf"%>
</head>
<body>
	<%@ include file="fragment/env-param.jspf"%>
	<div class="wrapper">
		<%@ include file="fragment/sidebar.jspf"%>
		<div class="main-panel">
			<%@ include file="fragment/navbar.jspf"%>			
			<div class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12">
							<div class="card">
							
								<div class="content showcase-btt"
									style="width: 100%; display: inline-block;">
									
									<a href="report.jsp" class="col-md-2" style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i class="pe-7s-box1"></i>
											<br>
											<br>
											Report
											<br>
											<br>
										</button>
									</a>
																		
									<a href="showcase.jsp" class="col-md-2" style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i class="pe-7s-albums"></i>
											<br>
											<br>
											<fmt:message key="sidbar.showcase" />
											<br>
											<br>
										</button>
									</a>
									 
									<a href="catalog_spreadSheet.jsp" class="col-md-2" style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i class="pe-7s-magic-wand"></i>
											<br>
											<br>
											Catalog Wizard
											<br>
											<br>
										</button>
									</a>
									
									<a href="spreadSheet.jsp" class="col-md-2" style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i class="pe-7s-note"></i>
											<br>
											<br>
											Spread Sheet
											<br>
											<br>											
										</button>
									</a>
									
									<a href="diagram.jsp" target="_blank" class="col-md-2" style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i class="pe-7s-shuffle"></i>
											<br>
											<br>
											<fmt:message key="limz.designer" />
											<br>
											<br>											
										</button>
									</a>
								</div>

							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	<textarea rows="4" cols="4" id="testChatText" style="display: none;"></textarea>
	<textarea rows="4" cols="4" id="chatLogTextArea" style="display: none;"></textarea>
	<input type="checkbox" id="onOff" style="display: none;">
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
</body>

<%@ include file="fragment/env-js.jspf"%>
<script type="text/javascript">
function onBotListLoaded(changed) {
	
	console.log("dashboard onBotListLoaded:" + changed);
	
	if (changed) {
		location.reload();
		return;
	}

}
</script>
</html>
