package ru.art.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.entity.Entity;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ModuleConfiguration {
    private final ModuleKey moduleKey;
    private final Entity configuration;
}