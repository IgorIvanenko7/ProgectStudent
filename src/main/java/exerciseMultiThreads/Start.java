package exerciseMultiThreads;

public class Start {

    final static int DELAY_THREAD = 900;

    public static void main(String[] args) throws InterruptedException {

    System.out.println("### Begin Pool Threads ###");

    // создание кастомного пула фиксированного размера на 3 потока
    CustomThreadPool threadPool = new CustomThreadPool(3);

        // кидаем в пул 7 задач
        for (int i = 0; i < 7; i++) {
            int taskNumber = i;
            threadPool.execute(() -> {
                System.out.printf("Start Выполнение задачи: %s ThreadName %s%n: ", taskNumber, Thread.currentThread().getName());
                threadPool.sleepFunc.accept(DELAY_THREAD);
                System.out.printf("Stop Выполнение задачи: %s ThreadName %s%n: ", taskNumber, Thread.currentThread().getName());
            });
        }

        System.out.println("### Before awaiting ###");
        //---- ожидаем когда все потоки отработают и очередь будет пустая --------
        threadPool.awaitTermination();
        System.out.println("### After awaiting ###");

        // ---- Перевод всех потоков в статус -> TERMINATED
        threadPool.shutdown();
        System.out.println("### End Pool Threads ###");
    }

}
