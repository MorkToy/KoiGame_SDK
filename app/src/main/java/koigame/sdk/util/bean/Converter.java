package koigame.sdk.util.bean;

public interface Converter {
    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type  Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value
     */
    public Object convert(Class type, Object value);

}
