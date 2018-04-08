package koigame.sdk.util;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

public class RUtils {
	private static Class R_LAYOUT_CLASS = null;
	private static Class R_ANIM_RCLASS = null;
	private static Class R_STRING_RCLASS = null;
	private static Class R_DRAWABLE_RCLASS = null;
	private static Class R_ID_RCLASS = null;
	private static Class R_Styleable_RCLASS = null;
	private static Class R_Style_RCLASS = null;
	private static Class R_Style_COLOR = null;
	private static Class R_XML_RCLASS = null;

	public static void init(String rclass) {
		try {
			Class RCLASS = Class.forName(rclass);

			Class classes[] = RCLASS.getDeclaredClasses();
			for (Class c : classes) {
				if (c.getName().equals(rclass + "$layout")) {
					R_LAYOUT_CLASS = c;
				} else if (c.getName().equals(rclass + "$anim")) {
					R_ANIM_RCLASS = c;
				} else if (c.getName().equals(rclass + "$string")) {
					R_STRING_RCLASS = c;
				} else if (c.getName().equals(rclass + "$drawable")) {
					R_DRAWABLE_RCLASS = c;
				} else if (c.getName().equals(rclass + "$id")) {
					R_ID_RCLASS = c;
				} else if (c.getName().equals(rclass + "$styleable")) {
					R_Styleable_RCLASS = c;
				} else if (c.getName().equals(rclass + "$style")) {
					R_Style_RCLASS = c;
				} else if (c.getName().equals(rclass + "$color")) {
					R_Style_COLOR = c;
				} else if (c.getName().equals(rclass + "$xml")) {
					R_XML_RCLASS = c;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int getColorId(String id) {
		return getIdByName(R_Style_COLOR, id);
	}

	public static int getViewId(String id) {
		return getIdByName(R_ID_RCLASS, id);
	}

	public static int getStyle(String id) {
		return getIdByName(R_Style_RCLASS, id);
	}

	public static int getStyleableId(String id) {
		return getIdByName(R_Styleable_RCLASS, id);
	}

	public static int getAnimId(String id) {
		return getIdByName(R_ANIM_RCLASS, id);
	}

	public static int getLayoutId(String id) {
		return getIdByName(R_LAYOUT_CLASS, id);
	}

	public static int getStringId(String id) {
		return getIdByName(R_STRING_RCLASS, id);
	}

	public static int getDrawableId(String id) {
		return getIdByName(R_DRAWABLE_RCLASS, id);
	}

	public static int getXml(String id) {
		return getIdByName(R_XML_RCLASS, id);
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

	public static String getNameById(Class R, int id) {
		List<Field> fs = getFieldList(R);
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				String n = f.getName();
				int i = f.getInt(R);
				if (id == i)
					return n;
			}
		} catch (IllegalArgumentException e) {
			Log.w("RUtils", e);
		} catch (IllegalAccessException e) {
			Log.w("RUtils", e);
		}
		return "";
	}

	public static int getIdByName(Class R, String id) {
		List<Field> fs = getFieldList(R);
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				String n = f.getName();
				int i = f.getInt(R);
				if (equalsIgnoreCase(id, n))
					return i;
			}
		} catch (IllegalArgumentException e) {
			Log.w("RUtils", e);
		} catch (IllegalAccessException e) {
			Log.w("RUtils", e);
		}
		return -1;
	}

	public static List<Integer> getIds(Class R, String prefix) {
		List<Integer> retList = new ArrayList<Integer>();
		List<Field> fs = getFieldList(R);
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				String n = f.getName();
				int i = f.getInt(R);
				if (n.startsWith(prefix, 0))
					retList.add(i);
			}
		} catch (IllegalArgumentException e) {
			Log.w("RUtils", e);
		} catch (IllegalAccessException e) {
			Log.w("RUtils", e);
		}
		return retList;
	}

	public static List<Integer> getIdsByContains(Class R, String prefix) {
		List<Integer> retList = new ArrayList<Integer>();
		List<Field> fs = getFieldList(R);
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				String n = f.getName();
				int i = f.getInt(R);
				if (n.contains(prefix))
					retList.add(i);
			}
		} catch (IllegalArgumentException e) {
			Log.w("RUtils", e);
		} catch (IllegalAccessException e) {
			Log.w("RUtils", e);
		}
		return retList;
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

}
