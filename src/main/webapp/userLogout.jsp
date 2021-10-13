<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>

<%
	session.invalidate(); // invalidate 함수는 세션을 없애고 세션에 속해있는 값들을 모두 없앤다
%>

<script>
	location.href = 'index.jsp';
</script>