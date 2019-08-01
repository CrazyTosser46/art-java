package ru.adk.remote.scheduler.constants;

public interface RemoteSchedulerModuleConstants {
    String REMOTE_SCHEDULER_MODULE_ID = "REMOTE_SCHEDULER_MODULE";
    String REFRESH_DEFERRED_TASK_KEY = "REFRESH_DEFERRED_TASK_KEY";
    String REFRESH_PERIODIC_TASK_KEY = "REFRESH_PERIODIC_TASK_KEY";
    int ZERO = 0;

    String ADD_DEFERRED_PATH = "/addDeferred";
    String ADD_PERIODIC_PATH = "/addPeriodic";
    String ADD_PROCESS_PATH = "/addProcess";
    String GET_DEFERRED_PATH = "/getDeferred";
    String GET_PERIODIC_PATH = "/getPeriodic";
    String GET_ALL_DEFERRED_PATH = "/getAllDeferred";
    String GET_ALL_PERIODIC_PATH = "/getAllPeriodic";
    String GET_ALL_PROCESSES_PATH = "/getAllProcesses";
    String cancel_PATH = "/cancel";

    interface LoggingMessages {
        String TASK_STARTED_MESSAGE = "Task started. Task: ''{0}''";
        String PROCESS_STARTED_MESSAGE = "Process was ras. Process: ''{0}''";
        String TASK_COMPLETED_MESSAGE = "Task completed. Task: ''{0}'' completed with response: ''{1}''";
        String TASK_FAILED_MESSAGE = "Task failed. Task: ''{0}''";
        String PROCESS_COMPLETED_MESSAGE = "ProcessExecution completed. Task: ''{0}'' completed with response: ''{1}''";
        String PROCESS_FAILED_MESSAGE = "Process execution failed. Task: ''{0}''";
    }

    interface SchedulerConfigKeys {
        String BALANCER_SECTION_ID = "balancer";
        String BALANCER_HOST = "host";
        String BALANCER_PORT = "port";

        String SCHEDULER_SECTION_ID = "scheduler";
        String REFRESH_DEFERRED_PERIOD_MINUTES = "refreshDeferredPeriodMinutes";
        String REFRESH_PERIODIC_PERIOD_MINUTES = "refreshPeriodicPeriodMinutes";
        String HTTP_PATH_KEY = "http.path";

        String DB_ADAPTER_SECTION_ID = "db";
        String SERVICE_ID = "adapter.serviceId";
    }

    interface ExceptionMessages {
        String ID_IS_EMPTY = "Id is empty";
    }
}
