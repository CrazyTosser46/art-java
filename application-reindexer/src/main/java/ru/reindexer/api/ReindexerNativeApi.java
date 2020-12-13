package ru.reindexer.api;

import java.nio.ByteBuffer;

public class ReindexerNativeApi {

    public native ByteBuffer connect(ByteBuffer byteBuffer);
}
