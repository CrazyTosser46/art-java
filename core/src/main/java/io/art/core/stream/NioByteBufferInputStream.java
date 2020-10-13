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

package io.art.core.stream;

import lombok.*;
import static java.lang.Math.*;
import java.io.*;
import java.nio.*;

@AllArgsConstructor
public class NioByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    @Override
    public int available() {
        return buffer.remaining();
    }

    @Override
    public int read() throws IOException {
        return buffer.hasRemaining() ? (buffer.get() & 0xFF) : -1;
    }

    @Override
    public int read(byte[] bytes, int off, int lenght) {
        if (!buffer.hasRemaining()) return -1;
        lenght = min(lenght, buffer.remaining());
        buffer.get(bytes, off, lenght);
        return lenght;
    }
}