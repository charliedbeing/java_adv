package charlie.day.by.day.threads.test;

import charlie.day.by.day.threads.PizzaDemoAdv;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadMonitoring {

    public static void main(String[] args) {
        // Your pizza store setup and thread creation code here...
        PizzaDemoAdv.test();
        // Monitor threads
        monitorThreads();
    }

    private static void monitorThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Print information about all live threads
        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);

        System.out.println("===== Thread Monitoring =====");
        for (ThreadInfo threadInfo : threadInfos) {
            printThreadInfo(threadInfo);
        }
    }

    private static void printThreadInfo(ThreadInfo threadInfo) {
        System.out.println("Thread ID: " + threadInfo.getThreadId());
        System.out.println("Thread Name: " + threadInfo.getThreadName());
        System.out.println("Thread State: " + threadInfo.getThreadState());
        System.out.println("Blocked Time: " + threadInfo.getBlockedTime());
        System.out.println("Blocked Count: " + threadInfo.getBlockedCount());
        System.out.println("Waited Time: " + threadInfo.getWaitedTime());
        System.out.println("Waited Count: " + threadInfo.getWaitedCount());
        System.out.println("==============================");
    }
}
