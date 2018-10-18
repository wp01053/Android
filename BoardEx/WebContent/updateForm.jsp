<%@page import="blog.exboard.ExBoardDTO"%>
<%@page import="blog.exboard.ExBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	// get 방식으로 전달된  no값을 할당
	int no = Integer.parseInt(request.getParameter("no"));
	ExBoardDAO manager = ExBoardDAO.getInstance();
	// getBoard 메서드로 해당 no의 데이터를 반환 받음
	ExBoardDTO board = manager.getBoard(no);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 수정</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<%-- 유효성 검사 --%>
<script>
	$(function() {
		$("form").submit(function() {
			if ($("#name").val() === "") {
				alert("이름 입력");
				$("#name").focus();
				return false;
			}
			if ($("#passwd").val() === "") {
				alert("비밀번호 입력");
				$("#passwd").focus();
				return false;
			}
			if ($("#subject").val() === "") {
				alert("제목 입력");
				$("#subject").focus();
				return false;
			}
			if ($("#content").val() === "") {
				alert("내용 입력");
				$("#content").focus();
				return false;
			}
		})
	})
</script>
</head>
<body>
	<center>
		<h3>글 수정 양식</h3>
		<%-- 확인 시 post 방식으로 해당 값을 갖고 update.jsp로 이동 --%>
		>
		<form action="update.jsp" method="post">
			<%-- hidden 값으로 no를 갖고 넘어감 / primary key --%>
			<input type="hidden" name="no" value="<%=no%>">
			<table border="1">
				<tr>
					<td>이름</td>
					<td><input type="text" name="name" id="name"
						value="<%=board.getName()%>"></td>
					<td>비밀번호</td>
					<td><input type="password" name="passwd" id="passwd"></td>
				</tr>
				<tr>
					<td>제목</td>
					<td colspan="3"><input type="text" name="subject" id="subject"
						size="58" value="<%=board.getSubject()%>"></td>
				</tr>
				<tr>
					<td>내용</td>
					<td colspan="3"><pre>
							<textarea rows="15" cols="60" name="content" id="content"><%=board.getContent()%></textarea>
						</pre></td>
				</tr>
				<tr align="center">
					<td colspan="4"><input type="submit" value="확인"> <input
						type="button" value="취소" onclick="history.go(-1)"></td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
