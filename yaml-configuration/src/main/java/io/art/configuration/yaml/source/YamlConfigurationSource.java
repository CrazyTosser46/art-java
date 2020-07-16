/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.configuration.yaml.source;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.dataformat.yaml.*;
import io.art.configuration.yaml.exception.*;
import io.art.core.module.*;
import lombok.*;
import static com.fasterxml.jackson.databind.node.JsonNodeType.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.parser.DurationParser.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;
import java.io.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

@Getter
public class YamlConfigurationSource implements ModuleConfigurationSource {
    private final String type;
    private final File file;
    private final JsonNode configuration;
    private final static YAMLMapper yamlMapper = new YAMLMapper();

    public YamlConfigurationSource(File file) {
        this.file = file;
        type = YamlConfigurationSource.class.getSimpleName() + COLON + file.getAbsolutePath();
        try {
            configuration = yamlMapper.readTree(file);
        } catch (IOException exception) {
            throw new YamlConfigurationLoadingException(exception);
        }
    }

    public YamlConfigurationSource(File file, JsonNode configuration) {
        this.file = file;
        type = YamlConfigurationSource.class.getSimpleName() + COLON + file.getAbsolutePath();
        this.configuration = configuration;
    }

    @Override
    public Integer getInt(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asInt);
    }

    @Override
    public Long getLong(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asLong);
    }

    @Override
    public Boolean getBool(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asBoolean);
    }

    @Override
    public Double getDouble(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asDouble);
    }

    @Override
    public String getString(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asText);
    }

    @Override
    public Duration getDuration(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), node -> parseDuration(node.asText()));
    }

    @Override
    public ModuleConfigurationSource getInner(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), node -> new YamlConfigurationSource(file, node));
    }

    @Override
    public List<Integer> getIntList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asInt)
                .collect(toList());
    }

    @Override
    public List<Long> getLongList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asLong)
                .collect(toList());
    }

    @Override
    public List<Boolean> getBoolList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asBoolean)
                .collect(toList());
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asDouble)
                .collect(toList());

    }

    @Override
    public List<String> getStringList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asText)
                .collect(toList());
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(node -> parseDuration(node.asText()))
                .collect(toList());

    }

    @Override
    public List<ModuleConfigurationSource> getInnerList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(node -> new YamlConfigurationSource(file, node))
                .collect(toList());
    }

    @Override
    public Map<String, Integer> getIntMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getInt));
    }

    @Override
    public Map<String, Long> getLongMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getLong));
    }

    @Override
    public Map<String, Boolean> getBoolMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getBool));
    }

    @Override
    public Map<String, Double> getDoubleMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getDouble));
    }

    @Override
    public Map<String, String> getStringMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getString));
    }

    @Override
    public Map<String, Duration> getDurationMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getDuration));
    }

    @Override
    public Map<String, ModuleConfigurationSource> getInnerMap(String path) {
        return getKeys().stream().collect(toMap(identity(), this::getInner));
    }

    @Override
    public Set<String> getKeys() {
        return stream(((Iterable<String>) configuration::fieldNames).spliterator(), false).collect(toSet());
    }

    @Override
    public boolean has(String path) {
        JsonNodeType nodeType = getYamlConfigNode(path).getNodeType();
        return nodeType != NULL && nodeType != MISSING;
    }

    private JsonNode getYamlConfigNode(String path) {
        JsonNode yamlConfig = configuration;
        JsonNode node = yamlConfig.path(path);
        JsonNodeType nodeType = node.getNodeType();
        if (nodeType != NULL && nodeType != MISSING) {
            return node;
        }
        int dotIndex = path.indexOf(DOT);
        if (dotIndex == -1) {
            return MissingNode.getInstance();
        }
        node = yamlConfig.path(path.substring(0, dotIndex));
        path = path.substring(dotIndex + 1);
        while (true) {
            JsonNode valueNode = node.path(path);
            JsonNodeType valueNodeType = valueNode.getNodeType();
            switch (valueNodeType) {
                case OBJECT:
                case BINARY:
                case BOOLEAN:
                case NUMBER:
                case ARRAY:
                case STRING:
                    return valueNode;
                case MISSING:
                case POJO:
                case NULL:
                    break;
            }
            dotIndex = path.indexOf(DOT);
            if (dotIndex == -1) {
                return MissingNode.getInstance();
            }
            node = node.path(path.substring(0, dotIndex));
            path = path.substring(dotIndex + 1);
        }
    }
}