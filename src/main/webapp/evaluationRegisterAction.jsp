<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="evaluation.EvaluationDTO"%>
<%@ page import="evaluation.EvaluationDAO"%>
<%@ page import="java.io.PrintWriter"%>
<%
	request.setCharacterEncoding("UTF-8");
	String userID = null;
	if(session.getAttribute("userID") != null) {
		userID = (String) session.getAttribute("userID");
	}
	if(userID == null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인을 해주세요.');");
		script.println("location.href = 'userLogin.jsp'");
		script.println("</script>");
		script.close();
		return;
	}

	request.setCharacterEncoding("UTF-8");
	
	String lectureName = null;
	String professorName = null;
	int lectureYear = 0;
	String semesterDivide = null;
	String lectureDivide = null;
	String evaluationTitle = null;
	String evaluationContent = null;
	String totalScore = null;
	String creditScore = null;
	String comfortableScore = null;
	String lectureScore = null;
	
	if(request.getParameter("lectureName") != null) {
		lectureName = (String) request.getParameter("lectureName");
	}
	if(request.getParameter("professorName") != null) {
		professorName = (String) request.getParameter("professorName");
	}
	if(request.getParameter("lectureYear") != null) {
		try {
			lectureYear = Integer.parseInt(request.getParameter("lectureYear")); // 정수형이기 때문에 Integer.parseInt으로 정수로 바꿔줌
		} catch (Exception e) {
			System.out.println("강의 연도 데이터 오류");
		}
	}
	if(request.getParameter("semesterDivide") != null) {
		semesterDivide = (String) request.getParameter("semesterDivide");
	}
	if(request.getParameter("lectureDivide") != null) {
		lectureDivide = (String) request.getParameter("lectureDivide");
	}
	if(request.getParameter("evaluationTitle") != null) {
		evaluationTitle = (String) request.getParameter("evaluationTitle");
	}
	if(request.getParameter("evaluationContent") != null) {
		evaluationContent = (String) request.getParameter("evaluationContent");
	}
	if(request.getParameter("totalScore") != null) {
		totalScore = (String) request.getParameter("totalScore");
	}
	if(request.getParameter("creditScore") != null) {
		creditScore = (String) request.getParameter("creditScore");
	}
	if(request.getParameter("comfortableScore") != null) {
		comfortableScore = (String) request.getParameter("comfortableScore");
	}
	if(request.getParameter("lectureScore") != null) {
		lectureScore = (String) request.getParameter("lectureScore");
	}
	
	// 한개라도 못받으면 오류메세지 보냄
	if (lectureName == null || professorName == null || lectureYear == 0 || semesterDivide == null ||
			lectureDivide == null || evaluationTitle == null || evaluationContent == null || totalScore == null ||
			creditScore == null || comfortableScore == null || lectureScore == null ||
			evaluationTitle.equals("") || evaluationContent.equals("")) { //공백이 있으면 안되기때문에
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('입력이 안 된 사항이 있습니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();
		return;
	} else {
		//실제로 작성한 강의평가가 들어가도록 처리
		EvaluationDAO evaluationDAO = new EvaluationDAO();
		int result = evaluationDAO.write(new EvaluationDTO(0, userID, lectureName, professorName, lectureYear,
				semesterDivide, lectureDivide, evaluationTitle, evaluationContent,
				totalScore, creditScore, comfortableScore, lectureScore, 0));
		if (result == -1) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('평가 등록에 실패했습니다.');");
			script.println("history.back();");
			script.println("</script>");
			script.close();
			return;
		} else {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("location.href = './index.jsp';");
			script.println("</script>");
			script.close();
			return;
		}
	}
%>