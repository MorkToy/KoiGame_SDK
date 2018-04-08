package koigame.sdk.util.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import koigame.sdk.util.CollectionUtils;
import koigame.sdk.util.StringUtils;


public class BeanUtils {

	public static String getProperty(Object obj, String propertyName) {
		if (obj == null || StringUtils.isBlank(propertyName))
			return "";

		Object retObj = null;
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			retObj = field.get(obj);
		} catch (Exception e) {
			// Log.e("ReflectUtils", e.getMessage());
		}
		if (field == null) {
			try {
				field = obj.getClass().getSuperclass().getDeclaredField(propertyName);
				field.setAccessible(true);
				retObj = field.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retObj == null ? "" : String.valueOf(retObj);
	}

	public static void setProperty(Object obj, String propertyName, Object value) {
		if (obj == null || StringUtils.isBlank(propertyName) || value == null)
			return;
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(propertyName);

			Object newValue = DataTypeConverter.instance().convert(field.getType(), value);

			field.setAccessible(true);
			field.set(obj, newValue);
		} catch (Exception e) {
			// Log.e("ReflectUtils", e.getMessage());
			e.printStackTrace();
		}
		if (field == null) {
			try {
				field = obj.getClass().getSuperclass().getDeclaredField(propertyName);

				Object newValue = DataTypeConverter.instance().convert(field.getType(), value);

				field.setAccessible(true);
				field.set(obj, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getClassName(Class cls) {
		return cls == null ? null : cls.getSimpleName().toLowerCase();
	}

	public static String getClassName(Object obj) {
		return obj == null ? null : obj.getClass().getSimpleName().toLowerCase();
	}

	public static String getFieldNameWithValue(Class cls, int value) {
		if (cls == null)
			return StringUtils.EMPTY;
		Field[] fields = cls.getDeclaredFields();
		try {
			for (Field field : fields) {
				if (field.getInt(cls) == value) {
					return field.getName();
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Field> getFieldListWithPrefix(Class cls, String prefix) {
		if (cls == null || StringUtils.isBlank(prefix))
			return Collections.EMPTY_LIST;

		List<Field> list = new ArrayList<Field>();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (field != null && field.getName().contains(prefix))
				list.add(field);
		}
		return list;
	}

	public static List<Field> getFieldList(Class cls) {
		if (cls == null)
			return Collections.EMPTY_LIST;

		List<Field> list = new ArrayList<Field>();
		Field[] fields = cls.getDeclaredFields();
		CollectionUtils.addToList(fields, list);

		Class superClass = cls.getSuperclass();
		if (superClass != null) {
			Field[] sfields = superClass.getDeclaredFields();
			CollectionUtils.addToList(sfields, list);
		}
		return list;
	}

}
