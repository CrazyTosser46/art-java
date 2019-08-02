package ru.art.core.extension;

import ru.art.core.exception.InternalRuntimeException;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteBuffer.wrap;
import static java.nio.channels.FileChannel.open;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.*;
import static ru.art.core.constants.BufferConstants.MAX_FILE_BUFFER_SIZE;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.context.Context.contextConfiguration;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public interface FileExtensions {
    static String readFile(String path) {
        return readFile(get(path), MAX_FILE_BUFFER_SIZE);
    }

    static String readFileQuietly(String path) {
        return readFileQuietly(get(path), MAX_FILE_BUFFER_SIZE);
    }

    static String readFile(Path path) {
        return readFile(path, MAX_FILE_BUFFER_SIZE);
    }

    static String readFileQuietly(Path path) {
        return readFileQuietly(path, MAX_FILE_BUFFER_SIZE);
    }

    static String readFile(String path, int bufferSize) {
        return readFile(get(path), bufferSize);
    }

    static String readFile(Path path, int bufferSize) {
        ByteBuffer byteBuffer = allocateDirect(bufferSize);
        StringBuilder result = new StringBuilder(EMPTY_STRING);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(byteBuffer);
                byteBuffer.flip();
                result.append(contextConfiguration().getCharset().newDecoder().decode(byteBuffer).toString());
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
        return result.toString();
    }

    static String readFileQuietly(Path path, int bufferSize) {
        ByteBuffer byteBuffer = allocateDirect(bufferSize);
        StringBuilder result = new StringBuilder(EMPTY_STRING);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(byteBuffer);
                byteBuffer.flip();
                result.append(contextConfiguration().getCharset().newDecoder().decode(byteBuffer).toString());
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    static byte[] readFileBytes(String path) {
        return readFileBytes(get(path), MAX_FILE_BUFFER_SIZE);
    }

    static byte[] readFileBytesQuietly(String path) {
        return readFileBytesQuietly(get(path), MAX_FILE_BUFFER_SIZE);
    }

    static byte[] readFileBytes(Path path) {
        return readFileBytes(path, MAX_FILE_BUFFER_SIZE);
    }

    static byte[] readFileBytesQuietly(Path path) {
        return readFileBytesQuietly(path, MAX_FILE_BUFFER_SIZE);
    }

    static byte[] readFileBytes(String path, int bufferSize) {
        return readFileBytes(get(path), bufferSize);
    }

    static byte[] readFileBytes(Path path, int bufferSize) {
        byte[] result = new byte[bufferSize];
        ByteBuffer byteBuffer = allocateDirect(bufferSize);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(byteBuffer);
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
        byteBuffer.flip();
        byteBuffer.get(result);
        return result;
    }

    static byte[] readFileBytesQuietly(Path path, int bufferSize) {
        byte[] result = new byte[bufferSize];
        ByteBuffer byteBuffer = allocateDirect(bufferSize);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(byteBuffer);
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteBuffer.flip();
        byteBuffer.get(result);
        return result;
    }


    static void writeFile(String path, String content) {
        writeFile(get(path), content);
    }

    static void writeFileQuietly(String path, String content) {
        writeFileQuietly(get(path), content);
    }

    static void writeFile(Path path, String content) {
        writeFileQuietly(path, content.getBytes());
    }

    static void writeFileQuietly(Path path, String content) {
        writeFileQuietly(path, content.getBytes());
    }


    static void writeFile(String path, byte[] content) {
        writeFile(get(path), content);
    }

    static void writeFileQuietly(String path, byte[] content) {
        writeFileQuietly(get(path), content);
    }

    static void writeFile(Path path, byte[] content) {
        ByteBuffer byteBuffer = wrap(content);
        try {
            createDirectories(path.getParent());
            FileChannel fileChannel = open(path, CREATE, TRUNCATE_EXISTING, WRITE);
            fileChannel.write(byteBuffer);
            fileChannel.close();
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
    }

    static void writeFileQuietly(Path path, byte[] content) {
        ByteBuffer byteBuffer = wrap(content);
        try {
            createDirectories(path.getParent());
            FileChannel fileChannel = open(path, CREATE, TRUNCATE_EXISTING, WRITE);
            fileChannel.write(byteBuffer);
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}