package ru.art.task.deferred.executor;

import static java.util.Objects.nonNull;
import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;
import static java.util.concurrent.ForkJoinTask.adapt;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static ru.art.task.deferred.executor.SchedulerModuleExceptions.ExceptionEvent.*;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Обозреватель очереди отложенных событий
 * Реализует логику проверки готовности выполнения события
 * Делегирует логику обработки события пулу потокв {@link ForkJoinPool}
 * Прерываемый. Во время прерывания в зависимости от конфигурации ожидает отмены всех отложенных событий
 * Осуществляет контроль пула потоков
 */
class DeferredEventObserver {
    private final DeferredExecutorConfiguration configuration;
    private final ForkJoinPool threadPool;
    private final DelayQueue<DeferredEvent<?>> deferredEvents;

    DeferredEventObserver(DeferredExecutorConfiguration configuration) {
        this.configuration = configuration;
        deferredEvents = new DelayQueue<>();
        threadPool = createThreadPool();
        observe();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> task, LocalDateTime triggerTime) {
        if (deferredEvents.size() + 1 > configuration.getEventsQueueMaxSize()) {
            return forceExecuteEvent(task);
        }
        ForkJoinTask<EventResultType> forkJoinTask = adapt(task);
        DeferredEvent<EventResultType> event = new DeferredEvent<>(forkJoinTask, triggerTime, deferredEvents.size());
        deferredEvents.add(event);
        return forkJoinTask;
    }

    void shutdown() {
        threadPool.shutdownNow();
        if (!configuration.awaitAllTasksTerminationOnShutdown()) {
            return;
        }
        try {
            threadPool.awaitTermination(configuration.getThreadPoolTerminationTimeoutMillis(), MILLISECONDS);
        } catch (Exception e) {
            configuration.getExceptionHandler().onException(POOL_SHUTDOWN, e);
        }
    }

    private void observe() {
        threadPool.execute(this::observeQueue);
    }


    @SuppressWarnings("ConstantConditions")
    private void observeQueue() {
        DeferredEvent<?> currentEvent;
        try {
            while (nonNull(currentEvent = deferredEvents.take())) {
                try {
                    ForkJoinTask<?> task = getTaskFromEvent(currentEvent).fork();
                    DeferredEvent<?> nextEvent;
                    if (nonNull(nextEvent = deferredEvents.peek()) && nextEvent.getTriggerDateTime() == currentEvent.getTriggerDateTime()) {
                        task.join();
                    }
                } catch (Exception e) {
                    configuration.getExceptionHandler().onException(TASK_EXECUTION, e);
                }
            }
        } catch (InterruptedException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Exception e) {
            configuration.getExceptionHandler().onException(TASK_OBSERVING, e);
        }
    }

    private ForkJoinTask<?> getTaskFromEvent(DeferredEvent<?> currentEvent) {
        return (ForkJoinTask<?>) currentEvent.getTask();
    }

    private ForkJoinPool createThreadPool() {
        return new ForkJoinPool(configuration.getThreadPoolCoreSize(), defaultForkJoinWorkerThreadFactory, configuration.getThreadPoolExceptionHandler(), true);
    }

    private <EventResultType> ForkJoinTask<? extends EventResultType> forceExecuteEvent(Callable<? extends EventResultType> task) {
        return threadPool.submit(task);
    }

    void clear() {
        deferredEvents.clear();
    }
}