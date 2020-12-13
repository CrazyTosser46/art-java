//
// Created by MIRZAKHANYAN Robert on 16.10.2020.
//

#include "../api/java/ru_reindexer_api_ReindexerNativeApi.h"
#include "../api/proto/example.pb.h"
#include "core/reindexer.h"
#include "string"

using namespace reindexer;

JNIEXPORT jobject JNICALL Java_ru_reindexer_api_ReindexerNativeApi_connect
        (JNIEnv* env, jobject, jobject buf) {
    char* bufArray = (char  *) env->GetDirectBufferAddress(buf);
    jlong capacity = env->GetDirectBufferCapacity(buf);

    ru::reindexer::api::proto::ConnectionRequest connectionRequest;
    connectionRequest.ParseFromArray(bufArray, capacity);

    auto reindexer = Reindexer();
    auto dbPath = connectionRequest.dns();
    auto connectOpts = ConnectOpts();
    connectOpts.expectedClusterID = connectionRequest.connectionopts().clusterid();
    reindexer.Connect(dbPath, connectOpts);
    const Error &openNamespace= reindexer.OpenNamespace("Test");
    if(!openNamespace.ok()) {
        const Error &addNamespace = reindexer.AddNamespace(NamespaceDef("Test"));
        printf("%s", addNamespace.what().c_str());
    }

    ru::reindexer::api::proto::Error error = ru::reindexer::api::proto::Error();

    error.set_errorcode(ru::reindexer::api::proto::ErrorCode_MIN);
    error.set_errormessage(openNamespace.what());

    size_t errorSize = error.ByteSizeLong();
    void *array = malloc(errorSize);

    error.SerializeToArray(array, errorSize);

    return env->NewDirectByteBuffer(array, errorSize);
}

Error log(JNIEnv* environment, const std::function<Error(void)> &function)
{
    auto error = function();
    printf("[JNI] [%s] Executed reindexer operation with code = '%d' and what: '%s' \n",
           "ReindexerJavaApi",
           error.code(),
           error.what().c_str());
    return error;
}


//JNIEXPORT void JNICALL Java_ru_art_reindexer_api_ReindexerNative_test
//        (JNIEnv *env, jobject, jobject buf) {
//    char *str = (char  *) env->GetDirectBufferAddress(buf);
//    jlong capacity = env->GetDirectBufferCapacity(buf);
//    for ( int i=0; i<capacity; i++) {
//        str[i] = '1';
//    }
//    std::cout << "Finish";
//}
