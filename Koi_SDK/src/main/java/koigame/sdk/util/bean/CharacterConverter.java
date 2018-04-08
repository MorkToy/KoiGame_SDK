package koigame.sdk.util.bean;
public class CharacterConverter extends AbstractConverter {
    public CharacterConverter(Object defaultValue) {
        super(defaultValue);
    }

    public Object convert(Class type, Object value) {
        if (type == null || value == null)
            return defaultValue;

        return new Character(value.toString().charAt(0));
    }

}
