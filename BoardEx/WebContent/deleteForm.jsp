<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int no = Integer.parseInt(request.getParameter("no"));
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 삭제 폼</title>
</head>
<body>
	<center>
		<%-- 삭제 클릭시 delete.jsp로 post 방식으로 이동 / 값 전달(no, passwd) --%>
		<form action="delete.jsp" method="post">
			<input type="hidden" name="no" value="<%=no%>">
			<table border="1">
				<tr>
					<td>비밀번호</td>
					<td><input type="password" name="passwd"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="삭제">
						<input type="button" value="취소" onclick="history.go(-1)">
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
