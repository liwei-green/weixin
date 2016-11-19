package cn.jiudao.eap.weixin;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import cn.jiudao.json.JsonUtils;

public class WeixinUtilsQY {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(WeixinUtils.class);

	private final static XmlMapper MAPPER = new XmlMapper();

	public static Map<String, Object> getAccessToken(String corpid,
			String corpsecret) throws Exception {
		String url = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
						corpid, corpsecret);
		return get(url);
	}

	public static String getProviderToken(String corpid, String provider_secret)
			throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_provider_token");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("corpid", corpid);
		map.put("provider_secret", provider_secret);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("provider_access_token").toString();
	}

	public static String getLoginTticket(String access_token, String auth_code)
			throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_login_info?access_token=%s",
						access_token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("auth_code", auth_code);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("redirect_login_info").toString();
	}

	public static String getLoginUrl(String access_token, String login_ticket,
			String target, int agentid) throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_login_url?access_token=%s",
						access_token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login_ticket", login_ticket);
		map.put("target", target);
		map.put("agentid", agentid);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("login_url").toString();
	}

	public static String suiteAccessToken(String suite_id, String suite_secret,
			String suite_ticket) throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suite_id", suite_id);
		map.put("suite_secret", suite_secret);
		map.put("suite_ticket", suite_ticket);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("suite_access_token").toString();
	}

	public static String get_pre_auth_code(String suite_access_token,
			String suite_id) throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_pre_auth_code?suite_access_token=%s",
						suite_access_token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suite_id", suite_id);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("pre_auth_code").toString();
	}

	public static String get_permanent_code(String suite_access_token,
			String suite_id, String auth_code) throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_permanent_code?suite_access_token=%s",
						suite_access_token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suite_id", suite_id);
		map.put("auth_code", auth_code);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("permanent_code").toString();
	}

	public static String get_corp_token(String suite_access_token,
			String suite_id, String auth_corpid, String permanent_code)
			throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/service/get_corp_token?suite_access_token=%s",
						suite_access_token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suite_id", suite_id);
		map.put("auth_corpid", auth_corpid);
		map.put("permanent_code", permanent_code);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("access_token").toString();
	}

	public static String sendMessage(String token, String touser,
			String toparty, String totag, String msgtype, int agentid,
			Object data, int safe) throws Exception {
		String requestUrl = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s",
						token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("touser", touser);
		map.put("toparty", toparty);
		map.put("totag", totag);
		map.put("msgtype", msgtype);
		map.put("agentid", agentid);
		map.put("data", data);

		Map<String, Object> response = post(requestUrl, map);

		return response.get("msgid").toString();
	}

	public static Map<String, String> fromXml(String xml) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> map = MAPPER.readValue(xml, Map.class);
		return map;
	}

	public static String toXml(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("<xml>\n");
		for (String name : map.keySet()) {
			Object value = map.get(name);
			if (value == null)
				value = "";
			String item = MessageFormat.format("<{0}>{1}</{0}>\n", name,
					StringEscapeUtils.escapeXml10(value.toString()));
			builder.append(item);
		}
		builder.append("</xml>");
		return builder.toString();
	}

	public static Map<String, Object> get(String url) throws Exception {
		String content = Request.Get(url).execute().returnContent().asString();
		LOGGER.debug("Server response: {}", content);
		return convert(content);
	}

	public static Map<String, Object> post(String url, Object data)
			throws Exception {
		Request request = Request.Post(url);

		if (data != null) {
			request = request.bodyString(JsonUtils.toJson(data),
					ContentType.APPLICATION_JSON);
		}
		String content = request.execute().returnContent().asString();
		LOGGER.debug("Server response: {}", content);
		return convert(content);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> convert(String content) throws Exception {
		Map<String, Object> map = JsonUtils.fromJson(content, Map.class);
		Integer code = (Integer) map.get("errcode");
		if (code != null && code.intValue() != 0) {
			Object msg = map.get("errmsg");
			String err = String.format("errcode: %d, errmsg: %s", code, msg);
			LOGGER.error(err);
			throw new Exception(err);
		}
		return map;
	}
}
