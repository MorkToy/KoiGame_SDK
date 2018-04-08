package koigame.sdk.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KGamePriceType implements Serializable {
	//
	private static Map<Integer, KGamePriceType> idMaps = new HashMap<Integer, KGamePriceType>();
	private static Map<String, KGamePriceType> codeMaps = new HashMap<String, KGamePriceType>();

	//
	public static KGamePriceType STEP = new KGamePriceType(1, "step");
	public static KGamePriceType FREE = new KGamePriceType(2, "free");

	//
	private int id;
	private String code;

	//
	private KGamePriceType(int id, String code) {
		this.id = id;
		this.code = code;

		//
		idMaps.put(this.id, this);
		codeMaps.put(this.code, this);
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public static KGamePriceType getById(int id) {
		return idMaps.get(id);
	}

	public static KGamePriceType getByCode(String code) {
		return codeMaps.get(code);
	}

	public int hashCode() {
		return id;
	}

	public String toString() {
		return "GamePriceType: id=[" + id + "], code=[" + code + "].";
	}

	public boolean equals(Object obj) {
		return obj != null && obj instanceof KGamePriceType && id == ((KGamePriceType) obj).getId();
	}

}
