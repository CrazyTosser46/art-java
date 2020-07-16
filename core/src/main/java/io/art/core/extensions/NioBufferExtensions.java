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

package io.art.core.extensions;

import io.netty.buffer.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.NettyBufferExtensions.*;
import static io.netty.buffer.ByteBufAllocator.*;
import static io.netty.buffer.Unpooled.*;
import static java.nio.ByteBuffer.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;

@UtilityClass
public class NioBufferExtensions {
    public static ByteBuffer from(InputStream value) {
        return from(InputStreamExtensions.toByteArray(value));
    }

    public static ByteBuffer from(String value) {
        return from(value.getBytes());
    }

    public static ByteBuffer from(byte[] array) {
        if (isEmpty(array)) {
            return allocateDirect(0);
        }
        ByteBuffer buffer = allocateDirect(array.length);
        buffer.put(array);
        return buffer;
    }

    public static ByteBuffer from(ByteBuf buffer) {
        return toNioBuffer(buffer);
    }

    public static byte[] toByteArray(ByteBuffer buffer) {
        if (isEmpty(buffer)) {
            return EMPTY_BYTES;
        }
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuf toNettyBuffer(ByteBuffer nioBuffer) {
        if (isEmpty(nioBuffer)) {
            return EMPTY_BUFFER;
        }
        if (nioBuffer.isDirect()) {
            ByteBuf buffer = DEFAULT.directBuffer(nioBuffer.capacity());
            buffer.writeBytes(nioBuffer);
            return buffer;
        }
        ByteBuf buffer = DEFAULT.heapBuffer(nioBuffer.capacity());
        buffer.writeBytes(nioBuffer);
        return buffer;
    }

    public static String toString(ByteBuffer buffer) {
        return new String(toByteArray(buffer), contextConfiguration().getCharset());
    }

    public static String toString(ByteBuffer buffer, Charset charset) {
        return new String(toByteArray(buffer), charset);
    }

}
