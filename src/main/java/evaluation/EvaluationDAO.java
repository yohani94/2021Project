package evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

// 강의평가와 관련된 데이터데이터스 접근 직접적으로 가능하게 하는 곳
public class EvaluationDAO {

	private Connection conn;
	private ResultSet rs;

	public EvaluationDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/LectureEvaluation";
			String dbID = "root";
			String dbPassword = "tkdjq0108??";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 강의평가 글쓰기 함수
	public int write(EvaluationDTO evaluationDTO) {
		PreparedStatement pstmt = null;
		try { // evaluationID값이 auto_increment로 적용되어있기 때문에 NULL값을 넣어주면 차례대로 1씩 증가되며 기입이됨, likeCount는 기본적으로(처음에) 좋아요 개수가 없기 때문에 0을 넣어줌
			String SQL = "INSERT INTO EVALUATION VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0);";
			pstmt = conn.prepareStatement(SQL);//.replaceAll("1", "2")는 아스키문자 1을 HTML 참조문자 2로 치환하여 브라우저에서는 일반 문자로 인식하여 스크립트로 해석되어 실행되지 않는다.
			pstmt.setString(1, evaluationDTO.getUserID().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 유저아이디
			pstmt.setString(2, evaluationDTO.getLectureName().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 강의명
			pstmt.setString(3, evaluationDTO.getProfessorName().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 교수명
			pstmt.setInt(4, evaluationDTO.getLectureYear());// 수강년도
			pstmt.setString(5, evaluationDTO.getSemesterDivide().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 수강학기
			pstmt.setString(6, evaluationDTO.getLectureDivide().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 강의구분
			pstmt.setString(7, evaluationDTO.getEvaluationTitle().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 게시글 제목
			pstmt.setString(8, evaluationDTO.getEvaluationContent().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 게시글 내용
			pstmt.setString(9, evaluationDTO.getTotalScore().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 종합 점수
			pstmt.setString(10, evaluationDTO.getCreditScore().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 성적 점수
			pstmt.setString(11, evaluationDTO.getComfortableScore().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>"));// 여유 점수
			pstmt.setString(12, evaluationDTO.getLectureScore().replaceAll("<", "&lt;").replaceAll(">", " &gt;").replaceAll("\r\n", "<br>")); // 강의 점수
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	
	
    // 강의 평가 글을 실질적으로 출력하고, 검색할 수 있는 기능 구현
	public ArrayList<EvaluationDTO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
		if(lectureDivide.equals("전체")) {
			lectureDivide = "";
		}
		// 강의 평가 글이 담기는 리스트 생성
		ArrayList<EvaluationDTO> evaluationList = null;
		PreparedStatement pstmt = null;
		String SQL = "";
		try {
			if(searchType.equals("최신순")) { // 여기의 LIKE 는 mysql 문법중 하나임(특정한 문자열 포함하는지)/ ORDER BY evaluationID DESC로 최신순으로 내림차순정렬/ 총 6개의 게시글을 출력하도록 한것, 페이지가 넘어갈때 마다 5개씩 넘어가게함
				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY evaluationID DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;
			} else if(searchType.equals("추천순")) { // ORDER BY likeCount DESC로 추천순으로 내림차순정렬
				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY likeCount DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;
			}
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, "%" + lectureDivide + "%");
			pstmt.setString(2, "%" + search + "%");
			rs = pstmt.executeQuery();
			evaluationList = new ArrayList<EvaluationDTO>();
			while(rs.next()) {
				EvaluationDTO evaluation = new EvaluationDTO(
					rs.getInt(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10),
					rs.getString(11),
					rs.getString(12),
					rs.getString(13),
					rs.getInt(14)
				);
				evaluationList.add(evaluation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return evaluationList;
	}
	
	
	public int like(String evaluationID) {
		PreparedStatement pstmt = null;
		try {
			String SQL = "UPDATE EVALUATION SET likeCount = likeCount + 1 WHERE evaluationID = ?";
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(evaluationID));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public int delete(String evaluationID) {
		PreparedStatement pstmt = null;
		try {
			String SQL = "DELETE FROM EVALUATION WHERE evaluationID = ?";
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(evaluationID));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	// 특정한 강의평가를 작성한 사용자 아이디를 구하는 함수
	public String getUserID(String evaluationID) {
		PreparedStatement pstmt = null;
		try {
			String SQL = "SELECT userID FROM EVALUATION WHERE evaluationID = ?";
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(evaluationID));
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}