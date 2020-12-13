import ru.reindexer.api.ReindexerNativeApi;
import ru.reindexer.api.proto.Example;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.*;

public class Main {
   public static native ReindexerError initializeReindexer(String directory);

    static {
        System.load("/Users/ruamzr4/IdeaProjects/art/art-java/application-reindexer/src/main/cpp/cmake-build-debug/libreindexer.x64.dylib");
    }

    public static void main(String[] args) throws IOException {
        ReindexerNativeApi reindexerNative = new ReindexerNativeApi();
//        Path dbPath = Paths.get("db");
//        Files.createDirectories(dbPath);
//        System.out.println(initializeReindexer(dbPath.toAbsolutePath().toString()));

//        ConnectionOpts connectionOpts = new ConnectionOpts();
//        connectionOpts.setClusterId(2);
//
//        Error db = reindexerNative.connect("db", connectionOpts);

        Path dbPath = Paths.get("db");
        Files.createDirectories(dbPath);

        Example.ConnectionRequest connectionOpts = Example.ConnectionRequest.newBuilder().setDns("db")
                .setConnectionOpts(Example.ConnectionOpts.newBuilder().setClusterId(2).build()).build();

        byte[] bytes = connectionOpts.toByteArray();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        byteBuffer.put(bytes);
        ByteBuffer connect = reindexerNative.connect(byteBuffer);
        Example.Error error = Example.Error.parseFrom(connect);
        System.out.println(error.getErrorCode());
        System.out.println(error.getErrorMessage());
    }
}
