package koigame.sdk.util.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataTypeConverter {
	private static final Integer ZERO = new Integer(0);
	private static final Character SPACE = new Character(' ');
	private static DataTypeConverter instance = new DataTypeConverter();
	private Map<Class, Converter> map = new HashMap<Class, Converter>();

	private DataTypeConverter() {
		init();
	}

	public static DataTypeConverter instance() {
		return instance;
	}

	private void init() {
		register(Boolean.TYPE, new BooleanConverter(Boolean.FALSE));
		register(Byte.TYPE, new NumberConverter(ZERO));
		register(Character.TYPE, new CharacterConverter(SPACE));
		register(Double.TYPE, new NumberConverter(ZERO));
		register(Float.TYPE, new NumberConverter(ZERO));
		register(Integer.TYPE, new NumberConverter(ZERO));
		register(Long.TYPE, new NumberConverter(ZERO));
		register(Short.TYPE, new NumberConverter(ZERO));

		Number defaultNumber = ZERO;
		BigDecimal bigDecDeflt = new BigDecimal("0.0");
		BigInteger bigIntDeflt = new BigInteger("0");
		Boolean booleanDefault = Boolean.FALSE;
		Character charDefault = SPACE;
		String stringDefault = "";

		register(BigDecimal.class, new NumberConverter(bigDecDeflt));
		register(BigInteger.class, new NumberConverter(bigIntDeflt));
		register(Boolean.class, new BooleanConverter(booleanDefault));
		register(Byte.class, new NumberConverter(defaultNumber));
		register(Character.class, new CharacterConverter(charDefault));
		register(Double.class, new NumberConverter(defaultNumber));
		register(Float.class, new NumberConverter(defaultNumber));
		register(Integer.class, new NumberConverter(defaultNumber));
		register(Long.class, new NumberConverter(defaultNumber));
		register(Short.class, new NumberConverter(defaultNumber));
		register(String.class, new StringConverter(stringDefault));

		register(java.util.Date.class, new DateConverter(null));
		register(Calendar.class, new DateConverter(null));
		register(java.sql.Date.class, new DateConverter(null));
		register(java.sql.Time.class, new DateConverter(null));
		register(Timestamp.class, new DateConverter(null));
	}

	private Converter lookup(Class clazz) {
		return map.get(clazz);
	}

	public void register(Class clazz, Converter converter) {
		map.put(clazz, converter);
	}

	public Object convert(Class type, Object value) {
		if (type == null || value == null || type.equals(value.getClass())) {
			return value;
		}
		return lookup(type).convert(type, value);
	}

	public void clear() {
		map.clear();
	}
}
