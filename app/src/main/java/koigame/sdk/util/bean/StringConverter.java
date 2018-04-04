package koigame.sdk.util.bean;

public class StringConverter extends AbstractConverter {

	public StringConverter(String defaultValue) {
		super(defaultValue);
	}

	public Object convert(Class type, Object value) {
		return value == null ? defaultValue : value.toString();
	}

}
