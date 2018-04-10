
package koigame.android.user;

import java.io.Serializable;

/**
 * 用户类型
 * 
 * @author Mike
 */
public class UserType implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int code;

    private String name;

    public static final UserType TEMPORARY = new UserType(0, "temporary"); // 临时账号，本地登陆

    public static final UserType THIRD_NO_BIND = new UserType(1, "third_no_bind"); // 第三方未绑定手机账号

    public static final UserType HILINK_NO_BIND = new UserType(2, "hilink_no_bind"); // 哈邻未绑定手机账号

    public static final UserType THIRD_FORMAL = new UserType(3, "third_formal"); // 第三方正式账号

    public static final UserType HILINK_FORMAL = new UserType(4, "hilink_formal"); // 哈邻正式账号

    private UserType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserType other = (UserType) obj;
        if (code != other.code)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
