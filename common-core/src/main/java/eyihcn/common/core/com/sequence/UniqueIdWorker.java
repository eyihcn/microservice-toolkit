package eyihcn.common.core.com.sequence;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法。
 * <p>
 * 优化开源项目 https://gitee.com/yu120/sequence
 * </p>
 *
 * @author hubin
 * @since 2016-08-01
 */
public abstract class UniqueIdWorker {

	/**
	 * 主机和进程的机器码
	 */
	private static Sequence WORKER = new Sequence();

	public static long getId() {
		return WORKER.nextId();
	}

	public static String getIdStr() {
		return String.valueOf(WORKER.nextId());
	}

	private static final String DASH = "-";
	private static final String EMPTY = "";

	/**
	 * 使用ThreadLocalRandom获取UUID获取更优的效果 去掉"-"
	 */
	public static String get32UUID() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return new UUID(random.nextLong(), random.nextLong()).toString().replace(DASH, EMPTY);
	}

}
