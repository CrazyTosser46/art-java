package ru.art.service.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.service.constants.ServiceExceptionsMessages.SERVICE_COMMAND_HAS_WRONG_FORMAT;
import static ru.art.service.constants.ServiceModuleConstants.SERVICE_COMMAND_REGEX;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceMethodCommand {
    private String serviceId;
    private String methodId;

    public static ServiceMethodCommand parseServiceCommand(String command) {
        String[] commandStringSlitted = command.split(DOT);
        if (commandStringSlitted.length != 2) {
            throw new IllegalArgumentException(format(SERVICE_COMMAND_HAS_WRONG_FORMAT, command));
        }
        String serviceId = commandStringSlitted[0];
        String methodIdWithBraces = commandStringSlitted[1];
        return new ServiceMethodCommand(serviceId, methodIdWithBraces.replaceAll(SERVICE_COMMAND_REGEX, EMPTY_STRING));
    }

    @Override
    public String toString() {
        return serviceId + DOT + methodId + BRACKETS;
    }
}