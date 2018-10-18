<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글작성 양식</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	/* 유효성 검사 */
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
		<h3>글작성 양식</h3>
		<%-- 확인(submit) 클릭시 post 방식으로 form의 name과 해당 값을 write.jsp로 전달 --%>
		<form action="write.jsp" method="post">
			<table border="1">
				<tr>
					<td>이름</td>
					<td><input type="text" name="name" id="name"></td>
					<td>비밀번호</td>
					<td><input type="password" name="passwd" id="passwd"></td>
				</tr>
				<tr>
					<td>제목</td>
					<td colspan="3"><input type="text" name="subject" id="subject"
						size="58"></td>
				</tr>
				<tr>
					<td>내용</td>
					<td colspan="3"><textarea rows="15" cols="60" name="content"
							id="content"></textarea></td>
				</tr>
				<tr align="center">
					<td colspan="4"><input type="submit" value="확인"> <%-- 취소버튼 클릭 시 list.jsp로 이동 --%>
						<input type="button" value="취소" onclick="location.href='list.jsp'">
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>
