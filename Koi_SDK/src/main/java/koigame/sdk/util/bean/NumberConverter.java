package koigame.sdk.util.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class NumberConverter extends AbstractConverter {

	private static final Integer ZERO = new Integer(0);
	private static final Integer ONE = new Integer(1);

	public NumberConverter(Object defaultValue) {
		super(defaultValue);
	}

	/**
	 * Convert the input object into a Number object of the specified type.
	 * 
	 * @param targetType
	 *            Data type to which this value should be converted.
	 * @param value
	 *            The input value to be converted.
	 * @return The converted value.
	 * @throws Throwable
	 *             if an error occurs converting to the specified type
	 */
	public Object convert(Class targetType, Object value) {
		// if (value == null)
		// return null;

		Class sourceType = value.getClass();
		// Handle Number
		if (value instanceof Number) {
			return toNumber(sourceType, targetType, (Number) value);
		}

		// Handle Boolean
		if (value instanceof Boolean) {
			return toNumber(sourceType, targetType, ((Boolean) value).booleanValue() ? ONE : ZERO);
		}

		// Handle Date --> Long
		if (value instanceof Date && Long.class.equals(targetType)) {
			return new Long(((Date) value).getTime());
		}

		// Handle Calendar --> Long
		if (value instanceof Calendar && Long.class.equals(targetType)) {
			return new Long(((Calendar) value).getTime().getTime());
		}

		// Convert all other types to String & handle
		String stringValue = value.toString().trim();
		// if (stringValue == null || StringUtils.equalsIgnoreCase(stringValue,
		// "null"))
		// return null;
		// Convert/Parse a String
		Number number = toNumber(sourceType, targetType, stringValue);

		// Ensure the correct number type is returned
		return toNumber(sourceType, targetType, number);

	}

	/**
	 * Convert any Number object to the specified type for this
	 * <i>Converter</i>.
	 * <p/>
	 * This method handles conversion to the following types:
	 * <ul>
	 * <li><code>java.lang.Byte</code></li>
	 * <li><code>java.lang.Short</code></li>
	 * <li><code>java.lang.Integer</code></li>
	 * <li><code>java.lang.Long</code></li>
	 * <li><code>java.lang.Float</code></li>
	 * <li><code>java.lang.Double</code></li>
	 * <li><code>java.math.BigDecimal</code></li>
	 * <li><code>java.math.BigInteger</code></li>
	 * </ul>
	 * 
	 * @param sourceType
	 *            The type being converted from
	 * @param targetType
	 *            The Number type to convert to
	 * @param value
	 *            The Number to convert.
	 * @return The converted value.
	 */
	private Number toNumber(Class sourceType, Class targetType, Number value) {

		// Correct Number type already
		if (targetType.equals(value.getClass())) {
			return value;
		}

		// Byte
		if (targetType.equals(Byte.class)) {
			long longValue = value.longValue();
			if (longValue > Byte.MAX_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too large for " + targetType);
			}
			if (longValue < Byte.MIN_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too small " + targetType);
			}
			return new Byte(value.byteValue());
		}
		if ("byte".equalsIgnoreCase(targetType.getName())) {
			return new Byte(value.byteValue());
		}
		// Short
		if (targetType.equals(Short.class)) {
			long longValue = value.longValue();
			if (longValue > Short.MAX_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too large for " + targetType);
			}
			if (longValue < Short.MIN_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too small " + targetType);
			}
			return new Short(value.shortValue());
		}
		if ("short".equalsIgnoreCase(targetType.getName())) {
			return new Short(value.shortValue());
		}
		// Integer
		if (targetType.equals(Integer.class)) {
			long longValue = value.longValue();
			if (longValue > Integer.MAX_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too large for " + targetType);
			}
			if (longValue < Integer.MIN_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too small " + targetType);
			}
			return new Integer(value.intValue());
		}

		if ("int".equalsIgnoreCase(targetType.getName())) {
			return new Integer(value.intValue());
		}
		// Long
		if (targetType.equals(Long.class)) {
			return new Long(value.longValue());
		}

		if ("long".equalsIgnoreCase(targetType.getName())) {
			return new Long(value.longValue());
		}

		// Float
		if (targetType.equals(Float.class)) {
			if (value.doubleValue() > Float.MAX_VALUE) {
				throw new ConversionException(sourceType + " value '" + value + "' is too large for " + targetType);
			}
			return new Float(value.floatValue());
		}
		if ("float".equalsIgnoreCase(targetType.getName())) {
			return new Float(value.floatValue());
		}
		// Double
		if (targetType.equals(Double.class)) {
			return new Double(value.doubleValue());
		}
		if ("double".equalsIgnoreCase(targetType.getName())) {
			return new Double(value.doubleValue());
		}
		// BigDecimal
		if (targetType.equals(BigDecimal.class)) {
			if (value instanceof Float || value instanceof Double) {
				return new BigDecimal(value.toString());
			} else if (value instanceof BigInteger) {
				return new BigDecimal((BigInteger) value);
			} else {
				return BigDecimal.valueOf(value.longValue());
			}
		}

		// BigInteger
		if (targetType.equals(BigInteger.class)) {
			if (value instanceof BigDecimal) {
				return ((BigDecimal) value).toBigInteger();
			} else {
				return BigInteger.valueOf(value.longValue());
			}
		}

		String msg = getClass() + " cannot handle conversion from '" + sourceType + "' to '" + targetType + "'";
		throw new ConversionException(msg);

	}

	/**
	 * Default String to Number conversion.
	 * <p/>
	 * This method handles conversion from a String to the following types:
	 * <ul>
	 * <li><code>java.lang.Byte</code></li>
	 * <li><code>java.lang.Short</code></li>
	 * <li><code>java.lang.Integer</code></li>
	 * <li><code>java.lang.Long</code></li>
	 * <li><code>java.lang.Float</code></li>
	 * <li><code>java.lang.Double</code></li>
	 * <li><code>java.math.BigDecimal</code></li>
	 * <li><code>java.math.BigInteger</code></li>
	 * </ul>
	 * 
	 * @param sourceType
	 *            The type being converted from
	 * @param targetType
	 *            The Number type to convert to
	 * @param value
	 *            The String value to convert.
	 * @return The converted Number value.
	 */
	private Number toNumber(Class sourceType, Class targetType, String value) {

		// Byte
		if (targetType.equals(Byte.class)) {
			return new Byte(value);
		}

		// Short
		if (targetType.equals(Short.class)) {
			return new Short(value);
		}

		// Integer
		if (targetType.equals(Integer.class)) {
			return new Integer(value);
		}

		// Long
		if (targetType.equals(Long.class)) {
			return new Long(value);
		}

		if ("long".equalsIgnoreCase(targetType.getName())) {
			return new Long(value);
		}

		// Float
		if (targetType.equals(Float.class)) {
			return new Float(value);
		}

		// Double
		if (targetType.equals(Double.class)) {
			return new Double(value);
		}

		// BigDecimal
		if (targetType.equals(BigDecimal.class)) {
			return new BigDecimal(value);
		}

		// BigInteger
		if (targetType.equals(BigInteger.class)) {
			return new BigInteger(value);
		}

		// sourceType is String
		if (sourceType.equals(String.class)) {
			return Long.parseLong(value);
		}

		String msg = getClass() + " cannot handle conversion from '" + sourceType + "' to '" + targetType + "'";

		throw new ConversionException(msg);
	}
}
