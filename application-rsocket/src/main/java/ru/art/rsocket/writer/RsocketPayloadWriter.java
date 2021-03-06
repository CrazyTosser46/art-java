/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.rsocket.writer;

import io.rsocket.*;
import lombok.experimental.*;
import ru.art.entity.*;
import ru.art.rsocket.constants.RsocketModuleConstants.*;
import ru.art.rsocket.exception.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static ru.art.entity.Value.*;
import static ru.art.json.descriptor.JsonEntityWriter.*;
import static ru.art.message.pack.descriptor.MessagePackEntityWriter.*;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.xml.descriptor.XmlEntityWriter.*;

@UtilityClass
public class RsocketPayloadWriter {
    public static Payload writePayloadData(Value value, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobufToBytes(value));
            case JSON:
                return create(writeJsonToBytes(value));
            case XML:
                return create(writeXmlToBytes(asXmlEntity(value)));
            case MESSAGE_PACK:
                return create(writeMessagePackToBytes(value));

        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDataFormat()));
    }

    public static Payload writePayloadMetaData(Value dataValue, Value metadataValue, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobufToBytes(dataValue), writeProtobufToBytes(metadataValue));
            case JSON:
                return create(writeJsonToBytes(dataValue), writeJsonToBytes(metadataValue));
            case XML:
                return create(writeXmlToBytes(asXmlEntity(dataValue)), writeXmlToBytes(asXmlEntity(metadataValue)));
            case MESSAGE_PACK:
                return create(writeMessagePackToBytes(dataValue), writeMessagePackToBytes(metadataValue));

        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDataFormat()));
    }
}
