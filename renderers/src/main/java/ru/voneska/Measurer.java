package ru.voneska;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;
import ru.voneska.runnable.RendererRunnable;

public class Measurer {

	public static void measureMemoryUsage(RendererRunnable runnable, @SuppressWarnings("SameParameterValue") int durationSecs, @SuppressWarnings("SameParameterValue") int intervalMilliSecs) {
		try {
			System.out.println();
			System.out.println("Memory measure for : " + runnable.getClass().getSimpleName() + " and " + runnable.getFile().getName());
			CompletableFuture<Void> memUsageFuture = CompletableFuture.runAsync(() -> {
				long mem = 0;
				long count = durationSecs * 1000 / intervalMilliSecs;
				for (int i = 0; i < count; i++) {
					long memUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					mem = Math.max(memUsed, mem);
					try {
						TimeUnit.MILLISECONDS.sleep(intervalMilliSecs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(i + " : Max memory used (mb): " + mem / 1024 / 1024);
				}
			});
			CompletableFuture<Void> mainProcessFuture = CompletableFuture.runAsync(runnable);

			CompletableFuture<Void> allOf = CompletableFuture.allOf(mainProcessFuture, memUsageFuture);
			allOf.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void measureTimeUsage(RendererRunnable runnable) {
		try {
			System.out.println();
			System.out.println("Time measure for : " + runnable.getClass().getSimpleName() + " and " + runnable.getFile().getName());

			final Stopwatch stopwatch = Stopwatch.createStarted();
			CompletableFuture<Void> mainProcessFuture = CompletableFuture.runAsync(runnable);
			mainProcessFuture.get();
			stopwatch.stop();
			System.out.println("Elapsed time in millisec ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
