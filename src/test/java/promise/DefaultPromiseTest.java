package promise;

import org.testng.Assert;
import org.testng.annotations.Test;
import util.ThreadExecutorsUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:jianyang
 * @since 2021-12-24 01:33
 */
public class DefaultPromiseTest {

	private static final Executor promiseTest =  ThreadExecutorsUtil.newCachedThreadPool("test-pool");

	private static final AtomicInteger index = new AtomicInteger();


	/**
	 * 经典用法之一：listeners回调
	 */
	@Test
	public void testListenerNotifyLater() {
		int numListenersBefore = 2; // 设置结果前设置两个listener
		int numListenersAfter = 3; // 设置结果后设置三个listener

		CountDownLatch latch = new CountDownLatch(numListenersBefore + numListenersAfter);
		DefaultPromise<Void> promise = new DefaultPromise<>();

		for (int i = 0; i < numListenersBefore; i++) {
			promise.addListener(new FutureListener<Void>() {
				@Override
				public void operationComplete(Future<Void> future) throws Exception {
					latch.countDown();
				}
			});
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				promise.setSuccess(null);

				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < numListenersAfter; i++) {
							promise.addListener(future -> {
								latch.countDown();
							});
						}
					}
				}).start();
			}
		}).start();

		try {
			Assert.assertTrue(latch.await(100, TimeUnit.SECONDS),
					"expect notify " + (numListenersBefore + numListenersAfter) + " listeners");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testFastThreadPoolWithPromise() {
		DefaultPromise<Void> promise = new DefaultPromise<>();

		// 1、为promise添加10个监听器FutureListener
		for (int i = 0; i < 10; i++) {
			promise.addListener(future -> System.out.println("haha:" + index.getAndIncrement()));
		}

		// 2、使用线程池执行业务逻辑（这里只是设置promise的值，触发promise的监听器执行操作）
		promiseTest.execute(() -> promise.setSuccess(null));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
