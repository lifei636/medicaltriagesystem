import java.io.UnsupportedEncodingException;

import com.core.toolbox.kit.CharsetKit;

public class Base64Test {

	/**
	 * Shiro 记住密码采用的是AES加密，AES key length 需要是16位，该方法生成16位的key
	 */
	public static void main(String[] args) {
		String keyStr = "JFinalBlade";

		byte[] keys;
		try {
			keys = keyStr.getBytes(CharsetKit.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
