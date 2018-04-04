package koigame.sdk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import koigame.sdk.util.bean.BeanUtils;


public class JSONUtils {
	private static final Object[] EMPTY_ARGS = new Object[0];
	private static final Class[] EMPTY_PARAM_TYPES = new Class[0];
	public final static String RETURN_STATUS = "status";
	public final static String RETURN_STATUS_OK = "0";
	public final static String RETURN_STATUS_ERROR = "-1";
	public final static String ERROR_CODE = "errorCode";
	public final static String ERROR_MSG = "errorMessage";

	public static boolean isOK(JSONObject props) {
		return JSONUtils.getString(props, JSONUtils.RETURN_STATUS).equals(JSONUtils.RETURN_STATUS_OK);
	}

	public static JSONObject build(String str) {
		try {
			return new JSONObject(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static JSONObject fromBean(Object object) throws JSONException {
		if (object == null) {
			return new JSONObject();
		} else if (object instanceof Enum) {
			throw new JSONException("'object' is an Enum. Use JSONArray instead");
		} else if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())) {
			throw new JSONException("'object' is an Annotation.");
		} else if (isNumber(object) || isBoolean(object) || isString(object)) {
			return new JSONObject();
		} else if (isArray(object)) {
			throw new JSONException("'object' is an array. Use JSONArray instead");
		} else {
			return _fromBean(object);
		}

	}

	public static JSONObject fromBeanNx(Object object) {
		if (object == null) {
			return new JSONObject();
		} else if (object instanceof Enum) {
		} else if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())) {
		} else if (isNumber(object) || isBoolean(object) || isString(object)) {
			return new JSONObject();
		} else if (isArray(object)) {
		} else {
			return _fromBean(object);
		}
		return null;
	}

	private static JSONObject _fromBean(Object object) {
		JSONObject json = new JSONObject();
		if (object == null)
			return json;
		List<Field> fields = BeanUtils.getFieldList(object.getClass());
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				String key = field.getName();
				Object temp = field.get(object);
				String value = temp == null ? "" : String.valueOf(temp);
				json.put(key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public static JSONArray fromArray(Object[] array) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		try {
			for (int i = 0; i < array.length; i++) {
				Object element = array[i];
				JSONObject jsonObject = _fromBean(element);
				jsonArray.put(jsonObject.toString());
			}
		} catch (RuntimeException e) {
			throw new JSONException(e.toString());
		}

		return jsonArray;
	}

	public static JSONArray fromProps(Properties props) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		try {
			props.toString();

			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey()).append("=").append(entry.getValue());
				jsonArray.put(sb.toString());
			}
		} catch (RuntimeException e) {
			throw new JSONException(e.toString());
		}
		return jsonArray;
	}

	public static Properties toProps(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) {
			throw new JSONException("JSONArray or Class is null : json=[" + jsonArray + "]");
		}
		if (jsonArray.length() == 0) {
			return new Properties();
		}

		Properties props = new Properties();
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			Object value = jsonArray.get(i);
			if (value != null) {
				String str = String.valueOf(value);
				String[] arr = str.split("=");
				if (arr.length == 2) {
					props.put(arr[0], arr[1]);
				} else if (arr.length == 1) {
					props.put(arr[0], "");
				}
			}
		}
		return props;

	}

	public static Properties toProps(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			throw new JSONException("JSONArray is null!");
		}
		if (jsonObject.length() == 0) {
			return new Properties();
		}
		Properties props = new Properties();
		for (Iterator itr = jsonObject.keys(); itr.hasNext();) {
			String key = (String) itr.next();
			String value = jsonObject.getString(key);
			props.put(key, value);
		}
		return props;

	}

	public static JSONArray fromList(List list) {
		JSONArray jsonArray = new JSONArray();
		if (list == null || list.isEmpty())
			return jsonArray;
		try {
			for (int i = 0; i < list.size(); i++) {
				Object element = list.get(i);
				JSONObject jsonObject = _fromBean(element);
				jsonArray.put(jsonObject);
			}
		} catch (RuntimeException e) {
			throw e;
		}

		return jsonArray;
	}

	public static List toList(JSONArray jsonArray, Class clazz) {
		if (clazz == null) {
			throw new RuntimeException("JSONArray or Class is null : json=[" + jsonArray + "], clazz=[" + clazz + "]");
		}
		if (jsonArray == null || jsonArray.length() == 0) {
			return new ArrayList();
		}
		try {
			List list = new ArrayList();
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				Object value = jsonArray.get(i);
				if (value == null) {
					list.add(null);
				} else {
					JSONObject jso = new JSONObject(value.toString());
					list.add(toBean(jso, clazz));
				}
			}
			return list;
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}

	}

	public static Object toBean(JSONObject json, Class clazz) throws JSONException {
		if (json == null || clazz == null) {
			throw new JSONException("JSONObject or Class is null : json=[" + json + "], clazz=[" + clazz + "]");
		}

		Object bean = null;
		try {
			bean = newInstance(clazz);
		} catch (Exception e) {
			throw new JSONException("Instance Class [" + clazz + "] failed!");
		}

		for (Iterator<String> itr = json.keys(); itr.hasNext();) {
			String key = itr.next();
			Object value = json.get(key);
			BeanUtils.setProperty(bean, key, value);
		}

		return bean;
	}

	public static boolean isNumber(Object obj) {
		if ((obj != null && obj.getClass() == Byte.TYPE) || (obj != null && obj.getClass() == Short.TYPE)
				|| (obj != null && obj.getClass() == Integer.TYPE) || (obj != null && obj.getClass() == Long.TYPE)
				|| (obj != null && obj.getClass() == Float.TYPE) || (obj != null && obj.getClass() == Double.TYPE)) {
			return true;
		}

		return obj instanceof Number;
	}

	public static boolean isBoolean(Object obj) {
		if ((obj instanceof Boolean) || (obj != null && obj.getClass() == Boolean.TYPE)) {
			return true;
		}
		return false;
	}

	public static boolean isString(Object obj) {
		if ((obj instanceof String) || (obj instanceof Character)
				|| (obj != null && (obj.getClass() == Character.TYPE || String.class.isAssignableFrom(obj.getClass())))) {
			return true;
		}
		return false;
	}

	public static boolean isArray(Object obj) {
		if ((obj != null && obj.getClass().isArray()) || (obj instanceof Collection) || (obj instanceof JSONArray)) {
			return true;
		}
		return false;
	}

	public static Object newInstance(Class target) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InvocationTargetException {
		if (target != null) {
			Constructor c = target.getDeclaredConstructor(EMPTY_PARAM_TYPES);
			c.setAccessible(true);
			try {
				return c.newInstance(EMPTY_ARGS);
			} catch (InstantiationException e) {
				String cause = "";
				try {
					cause = e.getCause() != null ? "\n" + e.getCause().getMessage() : "";
				} catch (Throwable t) { /* ignore */
				}
				throw new InstantiationException("Instantiation of \"" + target + "\" failed. "
						+ "It's probably because class is an interface, "
						+ "abstract class, array class, primitive type or void." + cause);
			}
		}
		return null;
	}

	/**
	 * Transforms a Number into a valid javascript number.<br>
	 * Float gets promoted to Double.<br>
	 * Byte and Short get promoted to Integer.<br>
	 * Long gets downgraded to Integer if possible.<br>
	 */
	public static Number transformNumber(Number input) {
		if (input instanceof Float) {
			return new Double(input.toString());
		} else if (input instanceof Short) {
			return new Integer(input.intValue());
		} else if (input instanceof Byte) {
			return new Integer(input.intValue());
		} else if (input instanceof Long) {
			Long max = new Long(Integer.MAX_VALUE);
			if (input.longValue() <= max.longValue() && input.longValue() >= Integer.MIN_VALUE) {
				return new Integer(input.intValue());
			}
		}

		return input;
	}

	public static void testValidity(Object o) throws JSONException {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
					throw new JSONException("JSON does not allow non-finite numbers");
				}
			} else if (o instanceof Float) {
				if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
					throw new JSONException("JSON does not allow non-finite numbers.");
				}
			} else if (o instanceof BigDecimal || o instanceof BigInteger) {
				// ok
				return;
			}
		}
	}

	public static JSONObject getJSONObject(JSONObject obj, String key) {
		try {
			return obj.getJSONObject(key);

		} catch (JSONException e) {
			return null;
		}
	}

	public static String getString(JSONArray obj, int idx) {
		try {
			return obj.getString(idx);
		} catch (JSONException e) {
			return "";
		}
	}

	public static double getDouble(JSONObject obj, String key) {
		try {
			return Double.parseDouble(obj.getString(key));
		} catch (JSONException e) {
			return 0.0;
		}
	}

	public static float getFloat(JSONObject obj, String key) {
		try {
			return Float.parseFloat(obj.getString(key));
		} catch (JSONException e) {
			return 0.0f;
		}
	}

	public static long getLong(JSONObject obj, String key) {
		try {
			return Long.parseLong(obj.getString(key));
		} catch (JSONException e) {
			return 0L;
		}
	}

	public static String getString(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	public static boolean getBoolean(JSONObject obj, String key) {
		try {
			return obj.getBoolean(key);
		} catch (JSONException e) {
			return false;
		}
	}

	public static Object get(JSONObject obj, String key) {
		try {
			return obj.get(key);
		} catch (JSONException e) {
			return "";
		}
	}

	public static String getString(JSONObject obj, String key, String defValue) {
		try {
			return obj.getString(key);
		} catch (JSONException e) {
			return defValue;
		}
	}

	public static int getInt(JSONObject obj, String key) {
		return getInt(obj, key, -1);
	}

	public static int getInt(JSONObject obj, String key, int defValue) {
		try {
			String str = obj.getString(key);
			if (StringUtils.isEmpty(str)) {
				return defValue;
			}

			return Integer.parseInt(str);
		} catch (JSONException e) {
			return defValue;
		}
	}

	public static JSONArray getArray(JSONObject obj, String key) {
		JSONArray ja = null;
		try {
			ja = obj.getJSONArray(key);
			return ja;
		} catch (JSONException e) {

			if (ja == null) {
				try {
					ja = new JSONArray(obj.getString(key));
				} catch (JSONException e1) {
					new JSONArray();
				}
			}
			return ja;
		}
	}
	
	 public static HashMap<String, String> toHashMap(JSONObject object) throws JSONException  
	   {  
	       HashMap<String, String> data = new HashMap<String, String>();  
	       Iterator it = object.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           String value = (String) object.get(key);  
	           data.put(key, value);  
	       }  
	       return data;  
	   }  

	public static void put(JSONObject obj, String key, String value) {
		try {
			if (!StringUtils.isEmpty(value))
				obj.put(key, value);
		} catch (JSONException e) {

		}
	}

	public static void put(JSONObject obj, String key, Object value) {
		try {
			obj.put(key, value);
		} catch (JSONException e) {

		}
	}

	public static void put(JSONObject obj, String key, JSONArray value) {
		try {
			obj.put(key, value);
		} catch (JSONException e) {

		}
	}

	public static void put(JSONObject obj, String key, JSONObject value) {
		try {
			obj.put(key, value);
		} catch (JSONException e) {

		}
	}

	public static void put(JSONObject src, JSONObject dest) {
		try {
			for (Iterator itr = src.keys(); itr.hasNext();) {
				String key = (String) itr.next();
				String value = src.getString(key);
				dest.put(key, value);
			}
		} catch (JSONException e) {

		}
	}
}
