package koigame.android.api;

public class ServiceException extends Exception implements Cloneable {
	private int m_value;

	/**
	 * A name given to the exception at static init time.
	 */
	private String m_name = "";

	/**
	 * Additional detail for a particular instance of the exception in order to
	 * give more info. This should never be used by code to decide on different
	 * code paths. It should only appear in logs.
	 */
	private String m_detail = "";

	/**
	 * This is used to indicate if our ServiceException or subclasses are in a
	 * temporary error state, for example 'TIMEOUT'. This allows us to filter
	 * Exceptions up the framework heirarchy and determine if an alert bug needs
	 * to be logged. This was renamed as JAX-B tried to create a member called
	 * 'transient' which is a keyword.
	 */
	private boolean m_transientException;

	/**
	 * This is used to indicate if the stack trace for the exception object
	 * should be captured. For e.g. - in case of TIMEOUT exceptions, the
	 * location of the occurence of the exception may not really matter.
	 */
	private boolean m_omitStackTrace;
    public static final int BASE_SERVICE                = 0;
    public static final int BASE_NAMING                 = 100;
    public static final int BASE_USER                   = 200;
    public static final int BASE_RATE                   = 300;
    public static final int BASE_AUTH_STATUS            = 400;
    public static final int BASE_VALIDATION             = 500;
    public static final int BASE_DB                     = 600;
    public static final int BASE_LOGIN                  = 700;
    public static final int BASE_TOKEN                  = 800;
    public static final int BASE_LEVEL                  = 900;
    public static final int BASE_ROOM_DATA              = 1000;
    public static final int BASE_HIGH_SCORE             = 1100;
    public static final int BASE_USERASSOCIATION        = 1200;
    public static final int BASE_FRIENDSLIST            = 1300;
    public static final int BASE_OBJSRV                 = 1400;
    public static final int BASE_LOCK                   = 1500;
    public static final int BASE_PRIZE                  = 1600;
    public static final int BASE_ROOM                   = 1700;
    public static final int BASE_RATING                 = 1800;
    public static final int BASE_SUBS                   = 1900;
    public static final int BASE_MAIL                   = 2000;
    public static final int BASE_EMAILCOMPOSE           = 2100;
    public static final int BASE_ESM                    = 2200;
    public static final int BASE_GUESTACCESS            = 2300;
    public static final int BASE_STATS                  = 2400;
    public static final int BASE_AUDIT                  = 2500;
    public static final int BASE_ITEM                   = 8600;
    public static final int BASE_BILLING                = 8800;
    public static final int BASE_POINT                  = 8900;
    public static final int BASE_HIBERNATE              = 8700;
		/** Base for Tax Service **/
	public static final int BASE_TAX_SERVICE            = 2600;
    /**
	 * Base for ProductRegistration
	 */
	public static final int BASE_PRODUCT_REGISTRATION = 2500;

	/**
	 * Base for besl.digitalobject
	 */
	protected static final int BASE_DIGITAL_OBJECT = 2600;

    public static final ServiceException UNKNOWN = new ServiceException(BASE_SERVICE + 0, "UNKNOWN");
    public static final ServiceException CONNECT = new ServiceException(BASE_SERVICE + 1, "CONNECT", "", null, true);
    public static final ServiceException TIMEOUT = new ServiceException(BASE_SERVICE + 2, "TIMEOUT", "", null, true, true);
    public static final ServiceException BAD_PACKET = new ServiceException(BASE_SERVICE + 3, "BAD_PACKET", "", null, true);
    public static final ServiceException MONITOR_ERR = new ServiceException(BASE_SERVICE + 4, "MONITOR_ERR");
    public static final ServiceException GENERIC = new ServiceException(BASE_SERVICE + 5, "GENERIC");
    public static final ServiceException NEED_SYNCH = new ServiceException(BASE_SERVICE + 6, "NEED_SYNCH");
    public static final ServiceException IS_SYNCHING = new ServiceException(BASE_SERVICE + 7, "IS_SYNCHING", "", null, true);
    public static final ServiceException OVERLOADED = new ServiceException(BASE_SERVICE + 8, "OVERLOADED", "", null, true);
    public static final ServiceException CONNECT_IN_PROGRESS = new ServiceException(BASE_SERVICE + 9, "CONNECT_IN_PROGRESS", "", null, true);
    public static final ServiceException ADJUSTMENT_FAILED = new ServiceException(BASE_SERVICE + 10, "ADJUSTMENT_FAILED");
    public static final ServiceException INVALID_ARGUMENT = new ServiceException(BASE_SERVICE + 11, "INVALID_ARGUMENT");
    public static final ServiceException FAST_FAIL_RESOURCE_LIMIT = new ServiceException(BASE_SERVICE + 12, "FAST_FAIL_RESOURCE_LIMIT");
    public static final ServiceException SERIALIZATION_ERROR = new ServiceException(BASE_SERVICE + 13, "SERIALIZATION_ERROR");
    public static final ServiceException UNKNOWN_HOST = new ServiceException(BASE_SERVICE + 14, "UNKNOWN_HOST");
    public static final ServiceException BAD_SERVICE_IMPLEMENTATION = new ServiceException(BASE_SERVICE + 15, "BAD_SERVICE_IMPLEMENTATION");
    public static final ServiceException TTL_EXCEPTION = new ServiceException(BASE_SERVICE + 16, "TTL_EXCEPTION", "", null, true, true);
    public static final ServiceException IO_EXCEPTION = new ServiceException(BASE_SERVICE + 17, "IO_EXCEPTION", "", null, true);
    public static final ServiceException BAD_INVOKER = new ServiceException(BASE_SERVICE + 18, "BAD_INVOKER", "", null, true);
    public static final ServiceException BAD_IP_ADDR = new ServiceException(ServiceException.BASE_SERVICE + 19, "BAD_IP_ADDRESS");
    public static final ServiceException BAD_SIGN = new ServiceException(ServiceException.BASE_SERVICE + 20, "BAD_SIGN");
    public static final ServiceException BAD_SESSION = new ServiceException(BASE_SERVICE + 21, "BAD_SESSION", "", null, true, true);


	protected ServiceException() {
		this(0, "UNKNOWN");
	}

	public ServiceException(String detail) {
		this(GENERIC, detail);
	}

	/**
	 * Ctor this object from another adding a detail msg.
	 * 
	 * @param err
	 *            The exception to use as the source.
	 * @param detail
	 *            Additional detail. This should be used for informational
	 *            purposes only and should not be used by business logic to
	 *            decide on a different code path.
	 */

	public ServiceException(ServiceException err, String detail) {

		this(err.m_value, err.m_name, detail, err.getCause(),
				err.m_transientException, err.m_omitStackTrace);

	}

	public ServiceException(ServiceException err, Throwable cause) {
		this(err.m_value, err.m_name, cause.getMessage(), cause,
				err.m_transientException, err.m_omitStackTrace);
	}

	/**
	 * Ctor this object from another adding a detail msg.
	 * 
	 * @param err
	 *            The exception to use as the source.
	 * @param detail
	 *            Additional detail. This should be used for informational
	 *            purposes only and should not be used by business logic to
	 *            decide on a different code path.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */

	public ServiceException(ServiceException err, String detail, Throwable cause) {
		this(err.m_value, err.m_name, detail, cause, err.m_transientException,
				err.m_omitStackTrace);
	}

	public ServiceException(ServiceException err) {
		this(err.m_value, err.m_name, err.m_detail, err.getCause(),
				err.m_transientException, err.m_omitStackTrace);
	}

	public String getName() {
		return m_name;
	}

	public String getDetail() {
		return m_detail;
	}

	/**
	 * The std equals method.
	 */

	public boolean equals(Object obj) {
		if (!(obj instanceof ServiceException)) {
			return false;
		}
		ServiceException serr = (ServiceException) obj;
		return serr.m_value == m_value;
	}

	/**
	 * The std hashcode method.
	 */

	public int hashCode() {
		return m_value;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(m_name);
		sb.append(":").append(m_detail).append(":");
		return new String(sb);
	}

	/**
	 * Construct the object. This used to be a protected ctor because it should
	 * only be used by subclasses, but the 1.2 compiler gives me an error when i
	 * compile the subclass ( don't know if it's a bug-fix or a bug) so it's now
	 * public.
	 */

	public ServiceException(int value, String name) {
		this(value, name, "");
	}

	/**
	 * This was the constructor normall used before jdk1.4. Now the constructor
	 * that carries a possible cause should be used instead.
	 * 
	 * @param value
	 *            This is the integer code uniquely identifying this type of
	 *            exception.
	 * @param name
	 *            This is the name of the exception. Strictly for informational
	 *            purposes (ie, it appears in logs)
	 * @param detail
	 *            Additional detail to differentiate between instances of the
	 *            same type of exception. Again, strictly for informational
	 *            purposes; not to be used by business logic. If you need
	 *            additional data in your exception that is used by business
	 *            logic, then define your own exception class.
	 */

	public ServiceException(int value, String name, String detail) {
		this(value, name, detail, null);
	}

	/**
	 * Ctor this object from another adding a detail msg.
	 * 
	 * @param err
	 *            The exception to use as the source.
	 * @param detail
	 *            Additional detail. This should be used for informational
	 *            purposes only and should not be used by business logic to
	 *            decide on a different code path.
	 */

	public ServiceException(ServiceException err, String detail,
			boolean isTransient) {
		this(err.m_value, err.m_name, detail, null, isTransient);
	}

	/**
	 * This is the old constructor, before adding isTransient methods.
	 * 
	 * @param value
	 *            This is the integer code uniquely identifying this type of
	 *            exception.
	 * @param name
	 *            This is the name of the exception. Strictly for informational
	 *            purposes (ie, it appears in logs)
	 * @param detail
	 *            Additional detail to differentiate between instances of the
	 *            same type of exception. Again, strictly for informational
	 *            purposes; not to be used by business logic. If you need
	 *            additional data in your exception that is used by business
	 *            logic, then define your own exception class (see
	 *            service.juser.VerifyNameException as an example).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */

	public ServiceException(int value, String name, String detail,
			Throwable cause) {
		this(value, name, detail, cause, false, false);
	}

	/**
	 * Constructs the ServiceException object
	 * 
	 * @param value
	 *            Integer code of the exception
	 * @param name
	 *            Name of the exception
	 * @param detail
	 *            Detailed message of the exception
	 * @param cause
	 *            Cause of the exception, or the stack trace
	 * @param isTransient
	 *            Whether the exception is transient in nature or not
	 */

	public ServiceException(int value, String name, String detail,
			Throwable cause, boolean isTransient) {
		this(value, name, detail, cause, isTransient, false);
	}

	/**
	 * This is THE constructor. All other constructors should call this one to
	 * make sure that all fields are properly initialized.
	 * 
	 * @param value
	 *            This is the integer code uniquely identifying this type of
	 *            exception.
	 * @param name
	 *            This is the name of the exception. Strictly for informational
	 *            purposes (ie, it appears in logs)
	 * @param detail
	 *            Additional detail to differentiate between instances of the
	 *            same type of exception. Again, strictly for informational
	 *            purposes; not to be used by business logic. If you need
	 *            additional data in your exception that is used by business
	 *            logic, then define your own exception class .
	 * @param isTransient
	 *            - This is used to indicate if our ServiceException or
	 *            subclasses are in a temporary error state, for example
	 *            'TIMEOUT'.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 * @param omitStackTraceInfo
	 *            Indicates whether the stack trace of the exception is to be
	 *            preserved or not. If set to true, then the stack trace for the
	 *            exception will not be preserved.
	 */

	public ServiceException(int value, String name, String detail,
			Throwable cause, boolean isTransient,

			boolean omitStackTraceInfo) {
		super(name, cause);
		m_value = value;
		m_name = name;
		m_detail = detail;
		m_transientException = isTransient;
		m_omitStackTrace = omitStackTraceInfo;
	}

	/**
	 * Returns the value of this exception
	 * 
	 * @return int, the value of this exception.
	 */

	public int getValue() {
		return m_value;
	}

	public boolean isTransient() {
		return m_transientException;
	}

	public boolean omitStackTrace() {
		return m_omitStackTrace;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("ServiceException.clone: assertion failure");
		}
	}
}
