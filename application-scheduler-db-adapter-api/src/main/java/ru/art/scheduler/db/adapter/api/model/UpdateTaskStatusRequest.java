package ru.art.scheduler.db.adapter.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateTaskStatusRequest {
    private String taskId;
    private TaskStatus status;
}