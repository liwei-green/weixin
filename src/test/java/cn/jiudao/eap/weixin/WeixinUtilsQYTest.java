package cn.jiudao.eap.weixin;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WeixinUtilsQYTest {
	private String corpid = "admin";
	private String corpsecret = "password";
	private String access_token = "1sdadcadcacacas";
	private String auth_code = "acessToken";
	private String login_ticket = "loginticket";
	private String target = "target";
	private int agentid = 1;
	private String provider_secret = "providersecret";
	private String suite_access_token = "suite_access_token";
	private String suite_id = "suite_id";
	private String auth_corpid = "auth_corpid";
	private String permanent_code = "permanent_code";
	private String suite_secret = "suite_secret";
	private String suite_ticket = "suite_ticket";
	private String touser = "touser";
	private String toparty = "toparty";
	private String totag = "totag";
	private String msgtype = "news";
	private int safe = 1;
	private String data = "{\"name\":\"olay\",\"age\":100}";

	@Test
	public void getAccessToken() {
		try {
			Assert.assertEquals(WeixinUtilsQY
					.getAccessToken(corpid, corpsecret).containsKey("errcode"),
					true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	@Test
	public void getLoginTticket() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.getLoginTticket(access_token, auth_code)
							.contains(login_ticket), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void getLoginUrl() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.getLoginUrl(access_token, login_ticket,
							target, agentid).contains("login_code"), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void getProviderToken() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.getProviderToken(corpid, provider_secret)
							.isEmpty(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void get_corp_token() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.get_corp_token(suite_access_token, suite_id,
							auth_corpid, permanent_code).isEmpty(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void get_permanent_code() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.get_permanent_code(suite_access_token,
							suite_id, auth_code).isEmpty(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void get_pre_auth_code() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.get_pre_auth_code(suite_access_token,
							suite_id).isEmpty(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void sendMessage() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.sendMessage(access_token, touser, toparty,
							totag, msgtype, agentid, data, safe).length(), 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void suiteAccessToken() {
		try {
			Assert.assertEquals(
					WeixinUtilsQY.suiteAccessToken(suite_id, suite_secret,
							suite_ticket).isEmpty(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
