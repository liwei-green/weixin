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

public class WeixinUtils {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(WeixinUtils.class);

	private final static XmlMapper MAPPER = new XmlMapper();

	public static Map<String, Object> getAccessToken(String appid, String secret)
			throws Exception {
		String url = String
				.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
						appid, secret);
		return get(url);
	}

	public static Map<String, Object> getWebToken(String appid, String secret,
			String code) throws Exception {
		String url = String
				.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
						appid, secret, code);
		return get(url);
	}

	public static String sendMessage(String token, String openid, String tpl,
			Object data, String url) throws Exception {
		String requestUrl = String
				.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",
						token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("touser", openid);
		map.put("template_id", tpl);
		map.put("data", data);
		if (StringUtils.isNotBlank(url))
			map.put("url", url);

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
