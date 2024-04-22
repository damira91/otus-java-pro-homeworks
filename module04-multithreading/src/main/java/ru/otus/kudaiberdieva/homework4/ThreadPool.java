package ru.otus.kudaiberdieva.homework4;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class ThreadPool {
    private final int capacity;
    private final WorkerThread[] workers;
    private final Queue<Runnable> taskQueue;
    private volatile boolean isShutdown;


    public ThreadPool(int capacity) {
        this.capacity = capacity;
        this.workers = new WorkerThread[capacity];
        this.taskQueue = new LinkedList<>();
        this.isShutdown = false;


        for (int i = 0; i < capacity; i++) {
            workers[i] = new WorkerThread();
            workers[i].start();
        }
    }

    public synchronized void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is already shut down");
        }
        taskQueue.offer(task);
        notifyAll();
    }

    public void shutdown() {
        isShutdown = true;
    }

    private class WorkerThread extends Thread {
        int finishedTasks = 0;

        @Override
        public void run() {

            while (!taskQueue.isEmpty() || !isShutdown) {
                nextTask().ifPresent(runnable -> {
                    runnable.run();
                    finishedTasks++;
                });
            }
            System.out.println(getName() + " finished " + finishedTasks + " tasks");
        }
    }

    private synchronized Optional<Runnable> nextTask() {
        if (taskQueue.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taskQueue.poll());
    }
}


