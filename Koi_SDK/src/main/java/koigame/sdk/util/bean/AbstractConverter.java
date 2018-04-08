package koigame.sdk.util.bean;


public abstract class AbstractConverter implements Converter {

    protected Object defaultValue = null;

    public AbstractConverter() {
    }

    public AbstractConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    protected String convertToString(Object value) {
        return value == null ? "" : value.toString();
    }

}
