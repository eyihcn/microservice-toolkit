package eyihcn.common.core.constants;

public class CommonConstant {

	/** 成功 */
	public static final int SUCCESS = 0;

	/** 头部封装用户信息 KEY */
	public static final String HEAD_USER_INFO_KEY = "x-client-token-user";

	public static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";

	/** 消息重发的最大次数 */
	public static int MASSGE_MAX_RESEND_TIMES = 5;
	/** 重发消息的间隔时间 ，三天转换为ms值 */
	public static final int THREE_DAY_MILLIS = 3 * 24 * 60 * 60 * 1000;
	/** 批量处理消息页大小 */
	public static final int BATCH_HANDLE_PAGE_SIZE = 2000;
	// 最大分页size
	public static final int MAX_PAGE_SIZE = 5000;
	// 最小分页size
	public static final int MIN_PAGE_SIZE = 100;
	// 默认分页size
	public static final int DEFAULT_BATCH_SIZE = 2000;

	public static final String UNKNOWN = "unknown";

	public static final class Symbol {
		private Symbol() {
		}

		/**
		 * The constant COMMA.
		 */
		public static final String COMMA = ",";
		public static final String SPOT = ".";
		/**
		 * The constant UNDER_LINE.
		 */
		public final static String UNDER_LINE = "_";
		/**
		 * The constant PER_CENT.
		 */
		public final static String PER_CENT = "%";
		/**
		 * The constant AT.
		 */
		public final static String AT = "@";
		/**
		 * The constant PIPE.
		 */
		public final static String PIPE = "||";
		public final static String SHORT_LINE = "-";
		public final static String SPACE = " ";
		public static final String SLASH = "/";
		public static final String MH = ":";

	}

	/**
	 * The constant Y.
	 */
	public static final Integer Y = 1;
	/**
	 * The constant N.
	 */
	public static final Integer N = 0;

	/** 密码截取 */
	public static final String PASSWORD_SPLIT_SIGN = "##&&&";

	/** 判定超时时间边界 毫秒 */
	public static final int MESSAGE_TIMEOUT_DURATION = 10 * 1000;

	/** 公共跳转地址 */
	public static final String KEY_LIST = "/list";
	public static final String KEY_ADD = "/add";
	public static final String KEY_UPDATE = "/update";
	public static final String KEY_DELETE = "/delete";
	public static final String KEY_BATCH_REMOVE = "/batchRemove";
	public static final String KEY_BATCH_CHANGE = "/batchChange";
	public static final String APP_PUSH_MESSAGE = "app_push_message";
	public static final String STR_RESULT = "result";
	public static final String STR_ID = "id";
	/**
	 * 客服电话code(字典表中定义)
	 */
	public final static String CUSTOM_TELEPHONE_CODE = "CustomTelephone";

	public final static String HTTP_PREFIX = "http";

	public final static String HTTPS_PREFIX = "https";
}
