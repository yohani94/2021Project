package util;

import java.security.MessageDigest;

public class SHA256 {

	public static String getSHA256(String input) {
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256"); //입력한 값을 SHA-256으로 알고리즘 적용
			byte[] salt = "Hello! This is Salt.".getBytes(); //단순하게 SHA-256을 적용하면 해커에 의해 해킹을 당할 수 있기 때문에 솔트값을 적용
			digest.reset();
			digest.update(salt); // 솔트값 적용
			byte[] chars = digest.digest(input.getBytes("UTF-8"));// 배열값을 만들어서 UTF-8로 chars변수에다 넣어주고
			for(int i = 0; i < chars.length; i++) { // 이것을 문자열 형태로 변환
				String hex = Integer.toHexString(0xff & chars[i]); // oxff헥스 값과 현제 이 해쉬값을 적용한 chars의 해당 인덱스를 AND연산 해주고
				if(hex.length() == 1) result.append('0'); // 1자리수인 경우 0을 붙여서 총 2자리수가 나오는 16진수로 만들어 주고
				result.append(hex); // hex값을 뒤에 차근차근 넣어서 해당 해쉬값을 반환하도록 함
			}
		} catch (Exception e) {
			e.printStackTrace(); // 오류 발생하는 경우 오류출력
		}
		return result.toString(); // 결과를 반환
	}
}