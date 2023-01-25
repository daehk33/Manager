<%@ page pageEncoding="utf-8" session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
	<tiles:insertAttribute name="include" />
</head>
<!-- header에서 body 태그 및 div 태그 선언 -->
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="side" />
		<div class="main-panel">
			<div class="content">
				<tiles:insertAttribute name="content" />
			</div>
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
</body>
</html>
