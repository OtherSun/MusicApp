package com.crs.musicapp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by qf on 2016/11/26.
 */

public class ThreadPoolUtil {
    private static ExecutorService executorService;

    /**
     * 提供一个最大10条线程的线程池
     * @param task 任务线程
     */
    public static void execute (Runnable task){
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(CommonConst.THREAD_AMOUNT);
        }
//        线程池submit方法有返回值，但如果有一条线程出错则后面的线程不会执行
//        executorService.submit(task);
        executorService.execute(task);
    }
}
