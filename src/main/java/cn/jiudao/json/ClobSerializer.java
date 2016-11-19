package cn.jiudao.json;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ClobSerializer extends JsonSerializer<Clob> {

	@Override
	public Class<Clob> handledType() {
		return Clob.class;
	}

	@Override
	public void serialize(Clob clob, JsonGenerator generator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		try {
			generator.writeString(IOUtils.toString(clob.getAsciiStream(),
					"UTF-8"));
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
