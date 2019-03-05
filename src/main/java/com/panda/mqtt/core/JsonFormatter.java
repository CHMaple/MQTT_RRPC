package com.panda.mqtt.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author huangyongfa
 * 
 * fastJson工具类
 *
 */
public class JsonFormatter {
	
	
	private static final ThreadLocal<ObjectMapper> INCLUDE_NULL_MAPPER = new ThreadLocal<ObjectMapper>();

	private static final ThreadLocal<ObjectMapper> NOT_INCLUDE_NULL_MAPPER = new ThreadLocal<ObjectMapper>();

	private static ObjectMapper getMapper(boolean serializeNull) {
		ThreadLocal<ObjectMapper> tl = (serializeNull) ? INCLUDE_NULL_MAPPER : NOT_INCLUDE_NULL_MAPPER;
		if (null == tl.get()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
			if (!(serializeNull)) {
				mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES );
			}
			tl.set(mapper);
		}

		ObjectMapper resultMapper = (ObjectMapper) tl.get();
		INCLUDE_NULL_MAPPER.remove();
		NOT_INCLUDE_NULL_MAPPER.remove();
		return resultMapper;
	}

	public static String toJsonString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return toJsonString(obj, true);
	}

	public static String toJsonAsString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return toJsonAsString(obj, true);
	}

	public static byte[] toJsonAsBytes(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return toJsonAsBytes(obj, true);
	}

	public static void toJsonToFile(File file, Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		toJsonToFile(file, obj, true);
	}

	public static void toJsonToOutputStream(OutputStream out, Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		toJsonToOutputStream(out, obj, true);
	}

	public static void toJsonToWriter(Writer writer, Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		toJsonToWriter(writer, obj, true);
	}

	public static <T> T toObject(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return toObject(json, clazz, true);
	}

	public static <T> T toObject(byte[] src, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return toObject(src, clazz, true);
	}

	public static <T> T toObject(File file, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return toObject(file, clazz, true);
	}

	public static <T> T toObject(InputStream input, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return toObject(input, clazz, true);
	}

	public static <T> T toObject(Reader reader, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return toObject(reader, clazz, true);
	}

	public static <T> T toObject(URL url, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return toObject(url, clazz, true);
	}

	public static String toJsonString(Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		return getMapper(serializeNull).writeValueAsString(obj);
	}

	public static String toJsonAsString(Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		return getMapper(serializeNull).writeValueAsString(obj);
	}

	public static byte[] toJsonAsBytes(Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		return getMapper(serializeNull).writeValueAsBytes(obj);
	}

	public static void toJsonToFile(File file, Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		getMapper(serializeNull).writeValue(file, obj);
	}

	public static void toJsonToOutputStream(OutputStream out, Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		getMapper(serializeNull).writeValue(out, obj);
	}

	public static void toJsonToWriter(Writer writer, Object obj, boolean serializeNull)
			throws JsonGenerationException, JsonMappingException, IOException {
		getMapper(serializeNull).writeValue(writer, obj);
	}

	public static <T> T toObject(String json, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(json, clazz);
	}

	public static <T> T toObject(byte[] src, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(src, clazz);
	}

	public static <T> T toObject(File file, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(file, clazz);
	}

	public static <T> T toObject(InputStream input, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(input, clazz);
	}

	public static <T> T toObject(Reader reader, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(reader, clazz);
	}

	public static <T> T toObject(URL url, Class<T> clazz, boolean serializeNull)
			throws JsonParseException, JsonMappingException, IOException {
		return getMapper(serializeNull).readValue(url, clazz);
	}

	public static void setDateFormat(DateFormat dateFormat) {
		getMapper(true).setDateFormat(dateFormat);
		getMapper(false).setDateFormat(dateFormat);
	}
}
