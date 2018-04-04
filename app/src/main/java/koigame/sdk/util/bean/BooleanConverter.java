package koigame.sdk.util.bean;
public class BooleanConverter extends AbstractConverter {

    public BooleanConverter(Object defaultValue) {
        super(defaultValue);
    }

    /**
     * The set of strings that are known to map to Boolean.TRUE.
     */
    private final static String[] trueStrings = {"true", "yes", "y", "on", "1"};

    /**
     * The set of strings that are known to map to Boolean.FALSE.
     */
    private final static String[] falseStrings = {"false", "no", "n", "off", "0"};


    public Object convert(Class type, Object value) {
        String stringValue = value.toString().toLowerCase();

        for (int i = 0; i < trueStrings.length; ++i) {
            if (trueStrings[i].equals(stringValue)) {
                return Boolean.TRUE;
            }
        }

        for (int i = 0; i < falseStrings.length; ++i) {
            if (falseStrings[i].equals(stringValue)) {
                return Boolean.FALSE;
            }
        }
        return defaultValue;
    }

}
