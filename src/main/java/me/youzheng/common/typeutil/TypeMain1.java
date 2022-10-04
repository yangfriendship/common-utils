package me.youzheng.common.typeutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TypeMain1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        CompletableFuture<Integer> completableFuture1 = new CompletableFuture<>();
        CompletableFuture<Integer> completableFuture2 = new CompletableFuture<>();
        System.out.println("Start");

        completableFuture1.complete(getNumber(3000, 3));
        completableFuture2.complete(getNumber(1000, 1));

        CompletableFuture<Integer> thenCombine = completableFuture1.thenCombineAsync(completableFuture2, (integer, integer2) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("integer1 + intger2 + " + (integer + integer2));
            return integer + integer2;
        });
        System.out.println("submit1");
        executorService.submit(() -> completableFuture1);
        System.out.println("submit2");
        executorService.submit(() -> completableFuture2);
        System.out.println("submit3");
        executorService.submit(() -> thenCombine);

        System.out.println(thenCombine.get() + " :result");
        System.out.println("shutdown top");
        executorService.shutdown();
        System.out.println("shutdown down");
        System.out.println(thenCombine.get() + " :result");
    }

    public static Integer getNumber(long delay, int value) {
        try {
            System.out.println(Thread.currentThread().getName() + " : " + value);
            Thread.sleep(delay);
        } catch (InterruptedException e) {

        }
        return value;
    }

    public static class TimeCallable implements Callable<Integer> {
        private final long delay;
        private final int value;

        private TimeCallable(final long delay, final int value) {
            this.delay = delay;
            this.value = value;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " : " + value);
            Thread.sleep(delay);
            return value;
        }
    }


}
