package com.a3fun.core.common;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 用来使jackson2支持{@link IntEnum}的序列化
 *
 * @author zheng.sun
 */
public class IntEnumSerializer extends StdScalarSerializer<IntEnum> {

	/**  */
	private static final long serialVersionUID = -2571982673299899055L;

	public IntEnumSerializer(Class<?> intEnumClass) {
		super(intEnumClass, false);
	}

	@Override
	public void serialize(IntEnum intEn, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
		jgen.writeNumber(intEn.getId());
	}

	@Override
	public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
		return createSchemaNode("integer", true);
	}

	@Override
	public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
		JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
		if (v2 != null) { // typically serialized as a small number (byte or int)
			v2.numberType(JsonParser.NumberType.INT);
		}
	}

}
