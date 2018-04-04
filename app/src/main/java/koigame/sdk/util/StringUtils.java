package koigame.sdk.util;

import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities.
 */

public class StringUtils {

	public static final String EMPTY = "";

	/**
	 * Default set of bytes that can used to generate
	 * <p/>
	 * a random string. The set below represent digits 0-9 and
	 * <p/>
	 * lowercase letters.
	 */

	public static final byte[] RANDOM_DEFAULT_BYTES = {

	48, 49, 50, 51, 52, 53, 54, 55, 56, 57,

	97, 98, 99, 100, 101, 102, 103, 104, 105, 106,

	107, 108, 109, 110, 111, 112, 113, 114, 115, 116,

	117, 118, 119, 120, 121, 122

	};

	/**
	 * Default newline charactar for system
	 */

	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Default encoding used when generating a random string.
	 */

	public static final String RANDOM_DEFAULT_ENCODING = "US-ASCII";

	/**
	 * Replaces every occurrence of "oldString" with "newString" in string s.
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if oldString or newString
	 *             <p/>
	 *             is null. Note that if 's' is null, you'll just get back s w/o
	 *             <p/>
	 *             an exception being thrown.
	 */

	public static String replace(String s, String oldString, String newString) {

		if (s == null || s.length() == 0)

			return s;

		if (oldString == null)

			throw new IllegalArgumentException("oldString is null!");

		if (newString == null)

			throw new IllegalArgumentException("oldString is null!");

		int x = s.indexOf(oldString);

		if (x < 0) {

			return (s);

		}

		int oldLen = oldString.length();

		int prev = 0;

		StringBuffer sb = new StringBuffer(s.length());

		while (x >= 0) {

			sb.append(s.substring(prev, x));

			sb.append(newString);

			prev = x + oldLen;

			x = s.indexOf(oldString, prev);

		}

		sb.append(s.substring(prev));

		return (sb.toString());

	}

	public static String escape(String s, Set<Character> chars, char escapeChar) {

		if (isEmpty(s)) {

			return s;

		}

		if (chars == null) {

			throw new IllegalArgumentException("Escape character set cannot be null");

		} else if (chars.contains(escapeChar)) {

			throw new IllegalArgumentException("Escape character set cannot contain the escape character");

		}

		char[] charArray = s.toCharArray();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < charArray.length; i++) {

			if (chars.contains(charArray[i]) || charArray[i] == escapeChar) {

				sb.append(escapeChar);

			}

			sb.append(charArray[i]);

		}

		return sb.toString();

	}

	public static boolean containsUnescapedChars(String input, Set<Character> escapeSet, char escapeChar) {

		if (isEmpty(input)) {

			return false;

		}

		if (escapeSet == null) {

			throw new IllegalArgumentException("Substitution map cannot be null");

		} else if (escapeSet.contains(escapeChar)) {

			throw new IllegalArgumentException("Escape character set cannot contain the escape character");

		}

		char[] inputChars = input.toCharArray();

		boolean inEscape = false;

		for (int i = 0; i < inputChars.length; i++) {

			if (inputChars[i] == escapeChar) {

				inEscape = !inEscape;

			} else if (escapeSet.contains(inputChars[i])) {

				if (!inEscape) {

					return true;

				}

				inEscape = false;

			} else {

				if (inEscape) {

					return true;

				}

				inEscape = false;

			}

		}

		if (inEscape) {

			return true;

		}

		return false;

	}

	/*
	 * 
	 * Escapes the characters specified in the chars set by appending escapeChar
	 * in front of their occurances in string s
	 * 
	 * Note that escapeChar itself can be specified in the chars set. in that
	 * case each occurance of escpaeChar in the string,
	 * 
	 * would be preceeded by one more escape char.
	 */

	public static String escapeIfNotAlreadyEscaped(String input, Set<Character> escapeSet, char escapeChar) {

		if (isEmpty(input)) {

			return input;

		}

		if (escapeSet == null) {

			throw new IllegalArgumentException("Substitution map cannot be null");

		} else if (escapeSet.contains(escapeChar)) {

			throw new IllegalArgumentException("Escape character set cannot contain the escape character");

		}

		StringBuffer buffer = new StringBuffer();

		char[] inputChars = input.toCharArray();

		boolean inEscape = false;

		for (int i = 0; i < inputChars.length; i++) {

			if (inputChars[i] == escapeChar) {

				inEscape = !inEscape;

				if (!inEscape) {

					buffer.append(escapeChar);

					buffer.append(escapeChar);

				}

			} else if (escapeSet.contains(inputChars[i])) {

				buffer.append(escapeChar);

				buffer.append(inputChars[i]);

				inEscape = false;

			} else {

				if (inEscape) {

					buffer.append(escapeChar);

					buffer.append(escapeChar);

				}

				buffer.append(inputChars[i]);

				inEscape = false;

			}

		}

		if (inEscape) {

			buffer.append(escapeChar);

			buffer.append(escapeChar);

		}

		return buffer.toString();

	}

	/*
	 * 
	 * Replaces the occureance of the characters of string s specified in the
	 * substitute map keys by the corresponding
	 * 
	 * values in the map. if the substitution character is escaped using the
	 * escpae char, then it is not replaced.
	 * 
	 * for other characters (those are not substitutable), having escape
	 * character ahead of them doesnt make any difference.
	 * 
	 * 
	 * 
	 * Note that escape character cannot be part of substitutes map.
	 * 
	 * 
	 * 
	 * examples:
	 * 
	 * for substitute = [{'*', '%'}]
	 * 
	 * abcdef => abcdef
	 * 
	 * abcdef* => abcdef%
	 * 
	 * abcdef\W => abcdef\W
	 * 
	 * abcdef\* => abcdef*
	 */

	public static String replace(String input, Map<Character, Character> substitutes, Character escapeChar) {

		if (isEmpty(input)) {

			return input;

		}

		if (substitutes == null) {

			throw new IllegalArgumentException("Substitution map cannot be null");

		} else if (substitutes.get(escapeChar) != null) {

			throw new IllegalArgumentException("Substitution map cannot contain the escape character");

		}

		StringBuffer sb = new StringBuffer();

		char[] inputChars = input.toCharArray();

		boolean inEscape = false;

		for (int i = 0; i < inputChars.length; i++) {

			if (inputChars[i] == escapeChar) {

				inEscape = !inEscape;

				// escape character itself is escaped.

				if (!inEscape) {

					sb.append(escapeChar);

				}

			} else {

				Character replacement = substitutes.get(inputChars[i]);

				if (inEscape) {

					if (replacement == null) {

						sb.append(escapeChar);

					}

					sb.append(inputChars[i]);

				} else {

					if (replacement == null) {

						sb.append(inputChars[i]);

					} else {

						sb.append(replacement);

					}

				}

				inEscape = false;

			}

		}

		if (inEscape) {

			sb.append(escapeChar);

		}

		return sb.toString();

	}

	/**
	 * Routine to pluralize a noun.
	 * 
	 * @param num
	 *            If num != 1, then an 's' is appened to 'noun' and
	 *            <p/>
	 *            returned.
	 * @return Returns 'num + " " + noun' plus the optional s.
	 */

	public static String pluralize(int num, String noun) {

		return pluralize((long) num, noun);

	}

	public static String pluralize(long num, String noun) {

		StringBuffer sb = new StringBuffer(Long.toString(num));

		sb.append(" " + noun);

		if (num != 1)

			sb.append("s");

		return new String(sb);

	}

	/**
	 * Return an initCap version of the string
	 */

	public static String initCap(String old) {

		StringBuffer sb = new StringBuffer(old);

		boolean titleChar = true; // True if next letter should be CAPS

		int len = sb.length();

		for (int i = 0; i < len; i++) {

			char c = sb.charAt(i);

			if (Character.isLetter(c)) {

				if (titleChar) {

					titleChar = false;

				} else {

					sb.setCharAt(i, Character.toLowerCase(c));

				}

			} else {

				if ((c != '\'') && (c != '-'))

					titleChar = true;

			}

		}

		return sb.toString();

	}

	/**
	 * @param dir
	 *            A directory name.
	 * @return the string as a directory. That is, adds a trailing '/'
	 *         <p/>
	 *         if one doesn't already exist. Null is returned if dir is null.
	 */

	public static String asDir(String dir) {

		if (dir == null)
			return null;

		int length = dir.length();

		if (dir.charAt(length - 1) == '/') {

			return dir;

		} else {

			return dir + "/";

		}

	}

	/**
	 * Utility function to remove whitespace from a string.
	 * <p/>
	 * Eg, 'joe blow' becomes 'joeblow'.
	 */

	public static String removeWhiteSpace(String s) {

		if (s == null)

			return s;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {

			char c = s.charAt(i);

			if (!Character.isWhitespace(c))

				sb.append(c);

		}

		return new String(sb);

	}

	/**
	 * This method takes a string and returns a space-stripped version of it.
	 * 
	 * @param str
	 *            String to be stripped of spaces.
	 * @return String stripped of spaces.
	 */

	public static String stripSpaces(String str) {

		int i = str.indexOf(" ");

		while (i != -1) {

			StringBuffer strbfr = new StringBuffer(str);

			strbfr.deleteCharAt(i);

			str = strbfr.toString();

			i = str.indexOf(" ");

		}

		return str;

	}

	/**
	 * Append a number to a string prefix, left filling the number with
	 * <p/>
	 * zeroes. Eg, if the args are ("test",34,6), you get back "test000034").
	 * <p/>
	 * If the length of 'num' as a string is >= fieldWidth, then it is
	 * <p/>
	 * simply appended (ie, it is *not* truncated).
	 */

	public static String appendNumber(String prefix, int num, int fieldWidth) {

		StringBuffer sb = new StringBuffer(prefix);

		String snum = Integer.toString(num);

		for (int len = snum.length(); len < fieldWidth; len++)

			sb.append("0");

		sb.append(snum);

		return new String(sb);

	}

	/**
	 * Converts a double to a dollar string.
	 * <P>
	 * 
	 * @param d
	 *            The double.
	 * @return The dollar string.
	 *         <p/>
	 *         ***
	 *         <p/>
	 *         *FIXME* this utility is duplicated on the client side. It should
	 *         <p/>
	 *         probably be in a shared place for both...without impacting client
	 *         <p/>
	 *         size for those that don't need it. It should also be made to use
	 *         <p/>
	 *         the appropriate CurrencyFormat rather than the default locale...
	 *         <p/>
	 *         originally this used CurrencyFormat, but the formatting was not
	 *         <p/>
	 *         correct. (it dropped trailing zeros inappropriately)
	 *         <p/>
	 *         ***
	 */

	public static String doubleToCashString(double d) {

		StringBuffer dollars = new StringBuffer("$");

		dollars.append(Integer.toString((int) d));

		dollars.append(".");

		int cents = ((int) ((d + 0.005) * 100.0)) % 100;

		if (cents < 10) {

			dollars.append("0");

		}

		dollars.append(Integer.toString(cents));

		return (new String(dollars));

	}

	public static String doubleToCashString(double d, Locale locale) {

		NumberFormat cashFormat = NumberFormat.getCurrencyInstance(locale);

		return cashFormat.format(d);

	}

	/**
	 * Returns a string consisting of the toString() result on
	 * <p/>
	 * each object in the array.
	 * 
	 * @param array
	 *            An array of objects, with well-defined toString()
	 *            <p/>
	 *            methods.
	 * @param separator
	 *            A separator between each output.
	 */

	public static String makeString(Object[] array, String separator) {

		if (array == null)

			return "null";

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < array.length; i++) {

			sb.append(array[i] == null ? "null" : array[i].toString());

			if (i != array.length - 1)

				sb.append(separator);

		}

		return new String(sb);

	}

	/**
	 * Returns a string consisting of the toString() result on
	 * <p/>
	 * each object in the array.
	 * 
	 * @param array
	 *            An array of objects, with well-defined toString()
	 *            <p/>
	 *            methods.
	 * @param separator
	 *            A separator between each output.
	 */

	public static String makeString(long[] array, String separator) {

		StringBuffer sb = new StringBuffer("[");

		if (array != null) {

			for (int i = 0; i < array.length; i++) {

				sb.append(Long.toString(array[i]));

				if (i != array.length - 1)

					sb.append(separator);

			}

		}

		sb.append("]");

		return new String(sb);

	}

	public static String makeString(long[] array) {

		return makeString(array, ",");

	}

	/**
	 * Returns a string version of the passed in array.
	 */

	public static String makeString(Object[] array) {

		return makeString(array, ":");

	}

	/**
	 * Returns a printable version of a potentially null string.
	 * <p/>
	 * NULL will be returned as an empty string "".
	 * <p/>
	 * Useful with tomcat, since <%= null %> will print "null" instead
	 * <p/>
	 * of "".
	 */

	public static String nullable(String s) {

		if (s == null) {

			return "";

		} else {

			return s;

		}

	}

	/**
	 * Basically like nullable, only instead of returning an empty string ""
	 * <p/>
	 * it returns the nullString
	 */

	public static String nullableWithString(String printString, String nullString) {

		if (printString == null || printString.trim().length() == 0 || printString.trim().equalsIgnoreCase("null")) {

			return nullString;

		} else {

			return printString;

		}

	}

	/**
	 * Returns the string "null" if the object is null.
	 * <p/>
	 * Otherwise, returns the object's string representation in single quotes.
	 * <p/>
	 * Useful with SQL, where inserting a null value must avoid enclosing it in
	 * quotes.
	 * 
	 * @param obj
	 *            The potentially null object to string-ify
	 */

	public static String nullableSingleQuoted(Object obj) {

		return obj == null ? "null" : "'" + obj.toString() + "'";

	}

	/**
	 * Returns null if the passed in string is null or empty otherwise
	 * <p/>
	 * returns the input.
	 * <p/>
	 * <p/>
	 * <p/>
	 * The passed in string is trimmed of white space prior to
	 * <p/>
	 * checking if it's empty AND the returned string is also
	 * <p/>
	 * trimmed of whitespace.
	 * <p/>
	 * <p/>
	 * <p/>
	 * Convenience so that code only has to check for null instead of
	 * <p/>
	 * null or empty.
	 */

	public static String makeNull(String s) {

		if (s == null)

			return s;

		s = s.trim();

		if (s.length() == 0)

			return null;

		return s;

	}

	/**
	 * Strip enclosing double quotes. See stripChar().
	 */

	public static String stripDoubleQuotes(String s) {

		return stripChar(s, '"');

	}

	/**
	 * Remove enclosing character and return the inner string.
	 * <p/>
	 * Eg, stripChar( "%value%", '%') will return "value".
	 */

	public static String stripChar(String s, char c) {

		if (s == null)

			return s;

		int len = s.length();

		if (len == 0)

			return s;

		int start = 0;

		int end = len;

		if (len > 0 && s.charAt(0) == c)

			start = 1;

		if (len > 1 && s.charAt(len - 1) == c)

			end = len - 1;

		return s.substring(start, end);

	}

	public static String killSingleWhiteSpace(String s) {

		if (s == null) {

			return s;

		}

		if (s.length() == 1 && Character.isWhitespace(s.charAt(0))) {

			return null;

		}

		return s;

	}

	public static String truncateAfterChar(String str, char ch) {

		if (str == null) {

			return str;

		}

		int posOfChar = str.indexOf(ch);

		return posOfChar == -1 ? str : str.substring(0, posOfChar);

	}

	/**
	 * Generate a random string composed of a given set of bytes.
	 * <p/>
	 * By default the bytes used are represent the lowercase letters
	 * <p/>
	 * and 0-9 digits.
	 * 
	 * @param length
	 *            Length of the string to generate.
	 * @return randomStr The generated string.
	 * @throws UnsupportedEncodingException
	 *             Exception thrown when the encoding requested
	 *             <p/>
	 *             is not supported by the VM.
	 */

	public static String generateRandomString(int length)

	throws UnsupportedEncodingException {

		return generateRandomString(length, RANDOM_DEFAULT_BYTES, RANDOM_DEFAULT_ENCODING);

	}

	/**
	 * Generate a random string composed of a given set of bytes.
	 * <p/>
	 * If a valid set of bytes are not specified, then lowercase
	 * <p/>
	 * letters and 0-9 digits will be used. If an encoding is
	 * <p/>
	 * not specified, 'US-ASCII' will be used.
	 * 
	 * @param length
	 *            Length of the string to generate.
	 * @param validBytes
	 *            Byte array to represent valid characters.
	 * @param encoding
	 *            Character encoding desired.
	 * @return randomStr The generated string.
	 * @throws UnsupportedEncodingException
	 *             Exception thrown when the encoding requested
	 *             <p/>
	 *             is not supported by the VM.
	 */

	public static String generateRandomString(int length, byte[] validBytes, String encoding)

	throws UnsupportedEncodingException {

		if (validBytes == null || validBytes.length == 0)

			validBytes = RANDOM_DEFAULT_BYTES;

		if (encoding == null || encoding.length() == 0)

			encoding = RANDOM_DEFAULT_ENCODING;

		String randString = null;

		// Init the randomizer

		Random random = new Random();

		byte[] randomBytes = new byte[length];

		// Generate random characters

		for (int i = 0; i < length; i++) {

			int randNum = random.nextInt() % validBytes.length;

			randomBytes[i] = (byte) validBytes[Math.abs(randNum)];

		}

		randString = new String(randomBytes, 0, length, encoding);

		return randString;

	}

	/**
	 * returns true if all chars in String are letters or digits, else false.
	 * 
	 * @ param String @ return boolean
	 */

	public static boolean isLetterOrDigitString(String str) {

		for (int i = 0; i < str.length(); i++) {

			if (!Character.isLetterOrDigit(str.charAt(i))) {

				;

				return false;

			}

		}

		return true;

	}

	/**
	 * returns true if all chars in String are digits, else false.
	 * 
	 * @ param String @ return boolean
	 */

	public static boolean isDigitString(String str) {

		for (int i = 0; i < str.length(); i++) {

			if (!Character.isDigit(str.charAt(i))) {

				;

				return false;

			}

		}

		return true;

	}

	/**
	 * Remove every instance of char c from string.
	 * <p/>
	 * Eg, killChar( "this-is-a-long-string", '-') will return
	 * "thisisalongstring".
	 */

	public static String killChar(String s, char c) {

		if (s == null)

			return s;

		int pos;

		while ((pos = s.indexOf(c)) != -1) {

			String temp = s.substring(0, pos);

			if ((pos + 1) < s.length())

				temp += s.substring(pos + 1);

			s = temp;

		}

		return s;

	}

	/**
	 * Convert ASCII String to UTF8 String
	 */

	public static String convertToUTF8(String s) {

		if (s == null)

			return s;

		String utf8String = null;

		try {

			byte[] utf8bytes = s.getBytes("UTF-8");

			utf8String = new String(utf8bytes, "UTF-8");

		}

		catch (UnsupportedEncodingException e) {

			utf8String = s;

		}

		return utf8String;

	}

	/**
	 * Convert ASCII String to UTF8 String
	 */

	public static String convertToUTF8(String s, String fromEncoding) {

		if (s == null)

			return s;

		String utf8String = null;

		try {

			byte[] strBytes = s.getBytes(fromEncoding);

			utf8String = new String(strBytes, "UTF-8");

		}

		catch (UnsupportedEncodingException e) {

			utf8String = s;

		}

		return utf8String;

	}

	/**
	 * Returns true if the sval is null or empty after trimming.
	 */

	public static boolean isEmpty(String sval) {

		return sval == null || sval.trim().length() == 0;

	}

	/**
	 * StringBuffer version of {@link #isEmpty(String)}
	 * 
	 * @param stringBuffer
	 *            StringBuffer to check.
	 * @return <code>true</code> if StringBuffer is null or empty,
	 *         <p/>
	 *         <code>false</code> otherwise.
	 */

	public static boolean isEmpty(StringBuffer stringBuffer) {

		return stringBuffer == null || isEmpty(stringBuffer.toString());

	}

	/**
	 * Convert a string representation of a boolean into a
	 * <p/>
	 * boolean. It understands "Y", if set to anything else
	 * <p/>
	 * false is returned.
	 */

	public static boolean booleanFromString(String val) {

		if (val == null)

			return false;

		return val.equals("Y");

	}

	/**
	 * Convert a boolean into a string representation: "Y"=true,
	 * <p/>
	 * "N" = false.
	 */

	public static String booleanToString(boolean val) {

		return val ? "Y" : "N";

	}

	/**
	 * Returns an escaped string given an unescaped string. It uses
	 * <p/>
	 * the backslash to escape any special characters. If a backslash
	 * <p/>
	 * is found in the input, it prepends a backslash character.
	 * 
	 * @param value
	 *            The string to escape.
	 * @param specialChars
	 *            A String containing all the special characters
	 *            <p/>
	 *            to escape.
	 */

	public static String escape(String value, String specialChars) {

		if (value == null || value.length() == 0)

			return value;

		if (specialChars == null || specialChars.length() == 0)

			return value;

		StringBuffer sb = new StringBuffer();

		int len = value.length();

		int slen = specialChars.length();

		for (int i = 0; i < len; i++) {

			char c = value.charAt(i);

			if (c == '\\')

				sb.append("\\");

			else {

				for (int j = 0; j < slen; j++) {

					if (c == specialChars.charAt(j)) {

						sb.append("\\");

						break;

					}

				}

			}

			sb.append(c);

		}

		return sb.toString();

	}

	/**
	 * Converts a map of String keys pointing to String values into
	 * <p/>
	 * a single string of the form:
	 * <p/>
	 * 'key1=value1','key2=value2'
	 * <p/>
	 * Will escape the relevant characters.
	 */

	public static String makeString(Map map) {

		String specialChars = "'=,";

		StringBuffer sb = new StringBuffer();

		boolean first = true;

		for (Iterator itr = map.entrySet().iterator(); itr.hasNext();) {

			if (!first)

				sb.append(",");

			sb.append("'");

			Map.Entry entry = (Map.Entry) itr.next();

			sb.append(escape((String) entry.getKey(), specialChars));

			sb.append("=");

			sb.append(escape((String) entry.getValue(), specialChars));

			sb.append("'");

			first = false;

		}

		return sb.toString();

	}

	/**
	 * Will truncate a string to the passed in max length.
	 * <p/>
	 * If the string is <= maxLength, it is returned untouched,
	 * <p/>
	 * if > maxLength, then only maxLength chars are returned.
	 * <p/>
	 * If a null is passed in, a null is returned.
	 */

	public static String truncate(String s, int maxLength) {

		if (s == null)

			return null;

		if (s.length() <= maxLength)

			return s;

		return s.substring(0, maxLength);

	}

	/**
	 * Will strip HTML characters from a string for 'sanitary' display on
	 * <p/>
	 * an HTML page.
	 * 
	 * @param s
	 *            The string to strip HTML from
	 * @return The sanitized value
	 */

	public static String stripHTML(String s) {

		s = StringUtils.replace(s, "<", "&lt;");

		s = StringUtils.replace(s, ">", "&gt;");

		return s;

	}

	/**
	 * Reads in a file into a string. Clearly for small files. No newline
	 * <p/>
	 * characters appended.
	 * 
	 * @param fname
	 *            the filename to read in.
	 * @throws IOException
	 *             Thrown if there was a problem reading the file
	 *             <p/>
	 *             in.
	 */

	public static String readFile(String fname) throws IOException {

		return readFile(fname, false);

	}

	/**
	 * Reads in a file into a string.
	 * 
	 * @param fname
	 *            the filename to read in.
	 * @param addLineSeparator
	 *            true - add newline characters false otherwise
	 * @throws IOException
	 *             THrown if there was a problem reading the file
	 *             <p/>
	 *             in.
	 */

	public static String readFile(String fname, boolean addLineSeparator) throws IOException {

		return p_readReader(new FileReader(fname), addLineSeparator);

	}

	/**
	 * Read in a classpath relative file and return it as a string.
	 * 
	 * @param fname
	 *            The classpath relative name.
	 */

	public static String readCPFile(String fname) throws IOException {

		InputStream is = fname.getClass().getResourceAsStream(fname);

		if (is == null)

			throw new IOException("File not found: " + fname);

		return p_readReader(new InputStreamReader(is), false);

	}

	private static String p_readReader(Reader rdr, boolean addLineSeparator) throws IOException {

		BufferedReader brdr = new BufferedReader(rdr);

		StringBuffer sb = new StringBuffer();

		String line;

		while ((line = brdr.readLine()) != null) {

			sb.append(line);

			if (addLineSeparator) {

				sb.append(LINE_SEPARATOR);

			}

		}

		try {

			brdr.close();

		}

		catch (Exception e) {

		}

		return sb.toString();

	}

	/**
	 * calculate the total number of bytes will be used when the given string s
	 * is to be encoded using UTF-8
	 * 
	 * @param s
	 *            string to be estimated
	 * @return number of bytes
	 */

	public static int getUTF8Size(String s) {

		int size = 0;

		for (int i = 0; i < s.length(); i++) {

			char ch = s.charAt(i);

			size += getUTF8Size(ch);

		}

		return size;

	}

	/**
	 * calculate the number of bytes will be used when the given character ch is
	 * to be encoded using UTF-8
	 * 
	 * @param ch
	 *            char to be estimated
	 * @return number of bytes
	 */

	public static int getUTF8Size(char ch) {

		int size;

		if (ch < '?') { // 0000 - 007F

			size = 1;

		} else if (ch < '?') { // 0080 - 07FF

			size = 2;

		} else if ('?' <= ch && ch < '') { // INVALID SESSION, D800 - DFFF, so
											// assume max

			size = 3;

		} else { // 0800 - FFFF, escept D800 - DFFF

			size = 3;

		}

		return size;

	}

	/**
	 * Break up a long string into a string with newlines every
	 * <p/>
	 * 'breaklength' characters.
	 */

	public static String breakString(String s, int breakLength) {

		if (s == null || s.length() == 0)

			return s;

		if (s.length() <= breakLength)

			return s;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {

			if (i % breakLength == 0)

				sb.append("\n");

			sb.append(s.charAt(i));

		}

		return sb.toString();

	}

	/**
	 * Lowercases a string and returns it. If the input string is null,
	 * <p/>
	 * returns a null
	 * 
	 * @param s
	 * @return a lowercased version of the string
	 */

	public static String lowerIt(String s) {

		if (s == null)

			return null;

		return s.toLowerCase();

	}

	/**
	 * Returns a String which is truncated to maxLength, if necessary, and
	 * <p/>
	 * appendString is appended, only if the string lengh is greater than
	 * <p/>
	 * maxLength.
	 * 
	 * @param s
	 *            the String to truncate
	 * @param maxLength
	 *            The length to truncate s
	 * @param appendString
	 *            The String to append, if s is truncated.
	 * @return the possibly truncated string.
	 */

	public static String truncate(String s, int maxLength, String appendString) {

		if (s != null && s.length() > maxLength) {

			return new String(s.substring(0, maxLength) + "...");

		} else {

			return new String(s);

		}

	}

	/**
	 * Compares 2 strings for equality taking care of dealing with
	 * <p/>
	 * nulls, as well as ignoring case.
	 */

	public static boolean equalsIgnoreCase(String s1, String s2) {

		if (s1 == null)

			return s2 == null;

		if (s2 == null)

			return false;

		return s1.equalsIgnoreCase(s2);

	}

	/**
	 * Reverese the characters in a string.
	 */

	public static String reverse(String s) {

		if (s == null || s.length() == 0)

			return s;

		StringBuffer sb = new StringBuffer();

		for (int i = s.length() - 1; i >= 0; i--) {

			sb.append(s.charAt(i));

		}

		return sb.toString();

	}

	/**
	 * Strip off a numeric suffix. Eg, "hitweb01" returns "hitweb".
	 * <p/>
	 * <p/>
	 * <p/>
	 * If input is null or empty, the input is returned.
	 * <p/>
	 * If no numeric suffix, then the input is returned.
	 */

	public static String stripNumeric(String s) {

		if (s == null || s.length() == 0)

			return s;

		StringBuffer sb = new StringBuffer();

		for (int i = s.length() - 1; i >= 0; i--) {

			if (!Character.isDigit(s.charAt(i)))

				break;

			sb.append(s.charAt(i));

		}

		String suffix = StringUtils.reverse(sb.toString());

		if (suffix.length() == 0)

			return s;

		return s.substring(0, s.length() - suffix.length());

	}

	/**
	 * Converts a String array into a comma delimited string
	 */

	public static String getCommaStringFromArray(String[] values) {

		StringBuffer returnVal = new StringBuffer();

		if (values != null) {

			for (int i = 0; i < values.length; i++) {

				returnVal.append(values[i]);

				if (i < values.length - 1) {

					returnVal.append(", ");

				}

			}

		}

		return returnVal.toString();

	}

	/**
	 * Converts a comma delimited string into a String Array
	 */

	public static String[] getArrayFromCommaString(String value) {

		String[] result = null;

		if (value != null) {

			result = value.split(",\\s*");

		}

		return result;

	}

	/**
	 * Creates a String with a character and number of repetitions
	 */

	public static String createString(char character, int repetitions) {

		StringBuffer result = new StringBuffer(repetitions);

		for (int i = 0; i < repetitions; i++) {

			result.append(character);

		}

		return result.toString();

	}

	/**
	 * Returns whether a string contains any of a given set of characters.
	 * <p/>
	 * Useful when you want to check <code>"s.indexOf(c) != -1"</code> but
	 * <p/>
	 * you have more than one c.
	 * 
	 * @param s
	 *            string to test
	 * @param characters
	 *            set of characters which you want to find in s
	 * @return true if s contains any characters from <code>characters</code>,
	 *         <p/>
	 *         false otherwise
	 */

	public static boolean contains(String s, String characters) {

		for (int i = 0; i < s.length(); i++) {

			char c = s.charAt(i);

			if (characters.indexOf(c) != -1) {

				return true;

			}

		}

		return false;

	}

	/**
	 * Counts the number of occurrences of the pattern pat in String s
	 * 
	 * @param s
	 * @param pat
	 * @return a count of the number of occurrences of the pattern in the string
	 */

	public static int occurrences(String s, String pat) {

		int pos, found, count;

		pos = 0;

		count = 0;

		do {

			found = s.indexOf(pat, pos);

			if (found != -1) {

				count++;

				pos = found + pat.length();

			}

		} while (pos < s.length() && found != -1);

		return count;

	}

	/**
	 * To check whether string passed is Numeric.
	 * 
	 * @param s
	 *            - String to be evaluated
	 * @return - true if number, else false
	 */

	public static boolean isNumeric(String s) {

		try {

			Long.parseLong(s);

		}

		catch (NumberFormatException ne) {

			return false;

		}

		return true;

	}

	/**
	 * To check whether string passed is Numeric.
	 * 
	 * @param s
	 *            - String to be evaluated
	 * @return - true if number, else false
	 */

	public static boolean isNumericFloatingPoint(String s) {

		try {

			Double.parseDouble(s);

		}

		catch (NumberFormatException ne) {

			return false;

		}

		return true;

	}

	/**
	 * removes all characters except letter and digits from a given string.
	 * 
	 * @param str
	 *            input string
	 * @param removeWhiteSpace
	 *            whether or not to remove white space from string
	 * @return output string
	 */

	public static String removeNonLetterDigitCharacters(String str, boolean removeWhiteSpace)

	{

		if (isEmpty(str))
			return str;

		StringBuffer buf = new StringBuffer();

		char[] charArray = str.toCharArray();

		for (int i = 0; i < charArray.length; i++) {

			if (Character.isLetterOrDigit(charArray[i])) {
				;

				buf.append(charArray[i]);

			} else if (!removeWhiteSpace && Character.isWhitespace(charArray[i]))

			{

				buf.append(charArray[i]);

			}

		}

		return buf.toString();

	}

	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		for (int i = 0; i < length; i++) {
			if (!Character.isLetter(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	public static boolean isDigit(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		for (int i = 0; i < length; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 拆分http请求参数
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> wrapMap(String result) {
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = result.split("\"&");
		for (String str : arr) {
			String[] vs = str.split("=\"");
			map.put(vs[0], vs[1]);
		}
		return map;
	}
	
	public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
            float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (int) Math.ceil(widths[j]);  
            }  
        }  
        return iRet;  
    } 
	
	/**
	 * 是否为邮箱格式
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
		}
}