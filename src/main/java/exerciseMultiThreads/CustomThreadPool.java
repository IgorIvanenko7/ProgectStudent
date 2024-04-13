package exerciseMultiThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CustomThreadPool {

    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private final List<WorkerThread> workerThreads = new ArrayList<>();
    private volatile boolean isShutdown = false;

    Consumer<Integer> sleepFunc = (countSleep) -> {
        try {
            Thread.sleep(countSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public CustomThreadPool(int capacity) {
        for (int i = 0; i <= capacity; i++) {
            WorkerThread worker = new WorkerThread();
            workerThreads.add(worker);
            worker.start();
        }
        System.out.printf("Создан кастомный TreadPool из: %s потоков%n",capacity);
    }

    public void execute(Runnable task) {

        if (isShutdown) {
            throw new IllegalStateException("Пул потоков остановлен, новые задачи не принимаются");
        }
        synchronized (taskQueue) {
            taskQueue.addLast(task);
            // оживление ждущих потоков
            taskQueue.notifyAll();
        }
    }

    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workerThreads) {
            worker.interrupt();
        }
    }

    //Ожидание завершения пула потоков
    public void awaitAllThreadsWaiting() throws InterruptedException {
        while (true) {
            boolean isComplete = workerThreads.stream()
                    .map(th -> th.getState() == Thread.State.WAITING)
                    .reduce(Boolean::logicalAnd)
                    .orElse(false);
            sleepFunc.accept(100);
            // Коньюкция статусов всех потоков пула.
            // Если все потоки в состоянии WAITING и очередь пустая -> продолжаем работу main
            if (isComplete && taskQueue.size() == 0) return;
        }
    }

    private class WorkerThread extends Thread {
        public void run() {
            //--- бесконечный цикл -> удержание потока (потоков) в пуле -----
            while (true) {
                Runnable task;
                // Опрос на наличие новой задачи в листе (очереди)
                synchronized (taskQueue) {
                        while (taskQueue.isEmpty()) {
                            try {
                                taskQueue.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        task = taskQueue.removeFirst();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            //--------------------------------------------------------------
        }
    }
}


