package distChat.model;

import distChat.operation.RepeatOperation;
import kademlia.operation.Operation;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OperationScheduler {


    private final int queueFlushInterval = 1000;

    private volatile Boolean operationLock = false;

    private Queue<Operation> operationQueue = new LinkedList<>();

    private Set<RepeatOperation> repeatingOperations;

    private Boolean shutdown = false;


    public OperationScheduler(Set<RepeatOperation> repeatingOperations) {
        this.repeatingOperations = repeatingOperations;
        initQueueThread();
        initRepeatingOperation();
    }


    private void initQueueThread() {
        Runnable task = () -> {
            while (!shutdown) {

                if (!operationQueue.isEmpty() && !operationLock) {
                    try {
                        operationQueue.poll().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(queueFlushInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(task).start();

    }

    private void initRepeatingOperation() {

        for (RepeatOperation repeatingOperation : repeatingOperations) {

            Runnable task = () -> {
                while (!shutdown) {

                    try {
                        repeatingOperation.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(repeatingOperation.getInterval());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(task).start();

        }


    }


    public Boolean getShutdown() {
        return shutdown;
    }

    public void setShutdown(Boolean shutdown) {
        this.shutdown = shutdown;
    }

    public void queueOperation(Operation operation) {
        this.operationQueue.add(operation);
    }

    public void changeOperationLock(Boolean locked) {
        this.operationLock = locked;
    }

}
