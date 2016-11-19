package cn.jiudao.eap.weixin;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WeixinUtilsTest {
	private String appid = "wx5fb2c436bfde0501";
	private String secret = "b7752f3104045171e0d1783fe28ed94c";
	private String code;
	private String token = "1sdadcadcacacas";
	private String openid = "Qacd23d";
	private String tpl = "<xml></xml>";
	private String url = "www.baidu.com";
	private String data = "{\"name\":\"olay\",\"age\":100}";

	@Test
	public void getAccessToken() {
		try {
			Assert.assertEquals(WeixinUtils.getAccessToken(appid, "333333")
					.containsKey("errcode"), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	@Test
	public void getWebToken() {
		try {
			Assert.assertEquals(WeixinUtils.getWebToken(appid, secret, code)
					.containsKey("errcode"), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	@Test
	public void sendMessage() {
		try {
			Assert.assertEquals(
					WeixinUtils.sendMessage(token, openid, tpl, data, url)
							.length(), 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}
}
