package koigame.sdk.util;

import java.util.*;

public class CollectionUtils {

    public static boolean isEmpty(Collection c) {
        if (c == null)
            return true;
        return c.isEmpty();
    }


    public static boolean isEmpty(Map m) {
        if (m == null)
            return true;
        return m.isEmpty();
    }


    public static boolean isEmpty(Object[] m) {
        return m == null || m.length == 0;
    }

    public static void clear(List list) {
        if (isEmpty(list))
            return;
        for (Object o : list) {
            list.remove(o);
        }
        System.gc();
    }

    public static Object[] subArray(Object[] objs, int from, int count) {
        if (from < 0 || count > (length(objs))) {
            throw new IndexOutOfBoundsException("Size=" + length(objs) + ",from: " + from + ", count: " + count);
        }
        Object[] dest = new Object[count];
        System.arraycopy(objs, 0, dest, from, count);
        return dest;
    }

    public static boolean contains(List list, Object obj) {
        if (isEmpty(list) || obj == null)
            return false;
        return list.contains(obj);
    }

    public static int length(Object[] args) {
        return args == null ? 0 : args.length;
    }

    public static List covert(Set set) {
        if (isEmpty(set))
            return Collections.EMPTY_LIST;

        List list = new LinkedList();
        list.addAll(set);
        return list;
    }

    public static Set covert(List list) {
        if (isEmpty(list))
            return Collections.EMPTY_SET;

        Set set = new HashSet();
        set.addAll(list);
        return set;
    }

    public static void clear(Collection c) {
        if (c != null)
            c.clear();
    }

    public static void clear(Map m) {
        if (m != null)
            m.clear();
    }

    public static void clear(Object[] o) {
        if (o != null) {
            for (int i = 0; i < o.length; i++) {
                o[i] = null;
            }
        }

    }

    public static int size(Collection c) {
        return isEmpty(c) ? 0 : c.size();
    }

    public static int size(Map m) {
        return isEmpty(m) ? 0 : m.size();
    }

    public static int size(Object[] o) {
        return isEmpty(o) ? 0 : o.length;
    }

    public static void addToList(Object[] srcList, List desList) {
        if (isEmpty(srcList) || desList == null)
            return;
        for (Object o : srcList)
            desList.add(o);
    }

    public static boolean contain(Object[] os, Object o) {
        if (isEmpty(os) || o == null)
            return false;

        for (Object obj : os) {
            if (obj == o)
                return true;
        }
        return false;
    }

    public static boolean contains(Collection c, Object o) {
        if (isEmpty(c) || o == null)
            return false;

        return c.contains(o);
    }

    public static void print(List list) {
        if (isEmpty(list))
            return;
        for (Object o : list) {
            System.out.println(">>>>>>: " + o);
        }
    }

    public static void print(float[] list) {
        if (list == null)
            return;
        for (Object o : list) {
            System.out.print(": " + o);
        }
        System.out.println();
    }
}
