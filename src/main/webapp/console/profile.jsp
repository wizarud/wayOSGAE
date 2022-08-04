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
								<div class="header">
									<h4 class="title"><fmt:message key="profile.title" /></h4>
								</div>
								<div class="content">
									<form>
										<div class="row">
											<div class="col-md-3">
												<div class="form-group">
													<label><fmt:message key="profile.name" /></label> 
													<input id="name" type="text" class="form-control" placeholder="<fmt:message key="profile.name" />"
														readonly="readonly" value="">
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">
													<label><fmt:message key="profile.email" /></label> 
													<input id="email" type="text" class="form-control" placeholder="<fmt:message key="profile.email" />"
														readonly="readonly" value="">
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
</body>
<%@ include file="fragment/env-js.jspf"%>
<script src="js/profile.js"></script> 
<script type="text/javascript">
function onBotListLoaded() {
	getProfile();
}
</script>

</html>
