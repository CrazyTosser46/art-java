package ru.art.task.deferred.executor;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;

public interface SchedulerModuleConfiguration extends ModuleConfiguration {
    DeferredExecutor getDeferredExecutor();

    PeriodicExecutor getPeriodicExecutor();

    @Getter
    class SchedulerModuleDefaultConfiguration implements SchedulerModuleConfiguration {
        private DeferredExecutor deferredExecutor = DeferredExecutorImpl.builder().withExceptionHandler(new DeferredExecutorExceptionHandler()).build();
        private PeriodicExecutor periodicExecutor = new PeriodicExecutor(DeferredExecutorImpl.builder().withExceptionHandler(new DeferredExecutorExceptionHandler()).build());
    }

}