package com.athena.peacock.controller.web.common.json;
import java.io.StringWriter;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * JSON Î¨∏Ïûê??Ï≤òÎ¶¨ ?†Ìã∏.
 *
 * @author Bong-Jin Kwon
 * @version
 * @see
 */
public class JSONUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/*static { // if you need to change default configuration:
		mapper.configure(SerializationConfig.Feature.USE_STATIC_TYPING, true); // faster this way, not default
	}*/

	/**
	 * <pre>
	 * json string???πÏ†ï class Object Î°?	Î≥?ôò.
	 * </pre>
	 *
	 * @param <T>
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T jsonToObj(String json, Class<T> valueType) {

		/*
		JSONObject jsonObject = JSONObject.fromObject( json );

		return (T)JSONObject.toBean( jsonObject, valueType.getClass() );
		*/


		//ObjectMapper mapper = new ObjectMapper();

		try {
			return MAPPER.readValue(json, valueType);
		} catch (Exception ex) {
			throw new RuntimeException(ex);  
		}

	}
	
	public static <T> List<T> jsonToList(String json, Class<T> valueType){
		
		try {
			return MAPPER.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, valueType));
		} catch (Exception ex) {
			throw new RuntimeException(ex);  
		}
	}

	/**
	 * <pre>
	 * Object Î•?json String?ºÎ°ú Î≥?ôò.
	 * </pre>
	 *
	 * @param obj
	 * @return
	 */
	public static String objToJson(Object obj) {

		//ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		try {
			JsonGenerator jgen = MAPPER.getJsonFactory().createJsonGenerator(sw);

			MAPPER.writeValue(jgen, obj);

			return sw.toString();

			//return mapper.writeValueAsString(obj);
		} catch (Exception ex) {
			throw new RuntimeException(ex);  
		}
	}

}
