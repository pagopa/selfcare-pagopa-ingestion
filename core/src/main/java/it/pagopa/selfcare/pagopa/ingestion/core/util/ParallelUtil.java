package it.pagopa.selfcare.pagopa.ingestion.core.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class ParallelUtil {
    public static void runParallel(final int maxThreads, Runnable task) throws RuntimeException {
        ForkJoinPool forkJoinPool = null;
        try {
            forkJoinPool = new ForkJoinPool(maxThreads);
            forkJoinPool.submit(task).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            if (forkJoinPool != null) {
                forkJoinPool.shutdown();
            }
        }
    }
}
