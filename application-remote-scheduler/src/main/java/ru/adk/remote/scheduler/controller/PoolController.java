package ru.adk.remote.scheduler.controller;

import ru.adk.remote.scheduler.api.model.DeferredTask;
import ru.adk.remote.scheduler.api.model.InfinityProcess;
import ru.adk.remote.scheduler.api.model.PeriodicTask;
import ru.adk.service.ServiceController;
import ru.adk.task.deferred.executor.*;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptySet;
import static ru.adk.remote.scheduler.action.TaskExecutionActions.*;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Methods.GET_ALL_DEFERRED_TASKS;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Methods.GET_ALL_PERIODIC_TASKS;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.REMOTE_SCHEDULER_SERVICE_ID;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.REFRESH_DEFERRED_TASK_KEY;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.REFRESH_PERIODIC_TASK_KEY;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModule;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModuleState;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.GET_ALL_INFINITY_PROCESSES;
import static ru.adk.task.deferred.executor.SchedulerModule.schedulerModule;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronous;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;
import java.time.LocalDateTime;
import java.util.Set;

public interface PoolController {
    static void fillAllPools() {
        fillDeferredPool();
        fillPeriodicPool();
        fillInfinityProcessPool();
    }

    static void startPoolRefreshingTask() {
        PeriodicExecutor periodicRefreshExecutor = new PeriodicExecutor(DeferredExecutorImpl.builder()
                .withExceptionHandler(new DeferredExecutorExceptionHandler())
                .build());

        IdentifiedRunnable refreshDeferredTask = new IdentifiedRunnable(REFRESH_DEFERRED_TASK_KEY, PoolController::fillDeferredPool);
        IdentifiedRunnable refreshPeriodicTask = new IdentifiedRunnable(REFRESH_PERIODIC_TASK_KEY, PoolController::fillPeriodicPool);

        periodicRefreshExecutor.executePeriodic(refreshDeferredTask, now().plusMinutes(remoteSchedulerModule().getRefreshDeferredPeriodMinutes()), ofMinutes(remoteSchedulerModule().getRefreshDeferredPeriodMinutes()));
        periodicRefreshExecutor.executePeriodic(refreshPeriodicTask, now().plusMinutes(remoteSchedulerModule().getRefreshPeriodicPeriodMinutes()), ofMinutes(remoteSchedulerModule().getRefreshPeriodicPeriodMinutes()));
    }

    static void fillDeferredPool() {
        schedulerModule().getDeferredExecutor().clear();
        Set<DeferredTask> deferredTasks = ServiceController.<Set<DeferredTask>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_DEFERRED_TASKS).orElse(emptySet());
        for (DeferredTask task : deferredTasks) {
            LocalDateTime triggerTime = task.getExecutionDateTime();
            if (triggerTime.isBefore(now())) {
                asynchronous(() -> executeDeferredTask(task));
                continue;
            }
            SchedulerModuleActions.asynchronous(() -> executeDeferredTask(task), triggerTime);
        }
    }

    static void fillPeriodicPool() {
        schedulerModule().getPeriodicExecutor().clear();
        Set<PeriodicTask> periodicTasks = ServiceController.<Set<PeriodicTask>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_PERIODIC_TASKS).orElse(emptySet());
        for (PeriodicTask task : periodicTasks) {
            IdentifiedRunnable runnableTask = new IdentifiedRunnable(task.getId(), () -> submitPeriodicTask(task));
            LocalDateTime executionDateTime = task.getExecutionDateTime();
            if (executionDateTime.isBefore(now())) {
                asynchronousPeriod(runnableTask, ofSeconds(task.getExecutionPeriodSeconds()));
                continue;
            }
            SchedulerModuleActions.asynchronousPeriod(runnableTask, executionDateTime, ofSeconds(task.getExecutionPeriodSeconds()));
        }
    }

    static void fillInfinityProcessPool() {
        remoteSchedulerModuleState().getPeriodicInfinityExecutor().clear();
        Set<InfinityProcess> infinityProcesses = ServiceController.<Set<InfinityProcess>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_INFINITY_PROCESSES).orElse(emptySet());
        for (InfinityProcess process : infinityProcesses) {
            IdentifiedRunnable runnableTask = new IdentifiedRunnable(process.getId(), () -> submitInfinityProcess(process));
            remoteSchedulerModuleState().getPeriodicInfinityExecutor().executePeriodic(runnableTask,
                    LocalDateTime.now().plus(ofSeconds(process.getExecutionDelay())),
                    ofSeconds(process.getExecutionPeriodSeconds()));
        }
    }
}
