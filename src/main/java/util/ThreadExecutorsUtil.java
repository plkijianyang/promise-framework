package util;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author:jianyang
 * @since 2021-12-24 01:39
 */
public class ThreadExecutorsUtil {


	public static Executor newCachedThreadPool(String poolName) {
		return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
	}
}
