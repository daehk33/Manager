<%@ page pageEncoding="utf-8" session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
<tiles:insertAttribute name="include" />
</head>
<body>
	<div class="content">
		<tiles:insertAttribute name="content" />
	</div>
</body>
</html>
