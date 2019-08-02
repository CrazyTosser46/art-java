/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GrpcServlet.proto

package ru.adk.grpc.servlet;

public final class GrpcServletMessage {
  private GrpcServletMessage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ru_adk_grpc_servlet_GrpcRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ru_adk_grpc_servlet_GrpcResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ru_adk_grpc_servlet_GrpcResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021GrpcServlet.proto\022\023ru.adk.grpc.servlet" +
      "\032\013Value.proto\"n\n\013GrpcRequest\022\021\n\tserviceI" +
      "d\030\001 \001(\t\022\020\n\010methodId\030\002 \001(\t\022:\n\013requestData" +
      "\030\003 \001(\0132%.ru.adk.protobuf.entity.Protobuf" +
      "Value\"t\n\014GrpcResponse\022\021\n\terrorCode\030\001 \001(\t" +
      "\022;\n\014responseData\030\002 \001(\0132%.ru.adk.protobuf" +
      ".entity.ProtobufValue\022\024\n\014errorMessage\030\003 " +
      "\001(\tB+\n\023ru.adk.grpc.servletB\022GrpcServletM" +
      "essageP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          ru.adk.protobuf.entity.ProtobufValueMessage.getDescriptor(),
        }, assigner);
    internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ru_adk_grpc_servlet_GrpcRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor,
        new java.lang.String[] { "ServiceId", "MethodId", "RequestData", });
    internal_static_ru_adk_grpc_servlet_GrpcResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ru_adk_grpc_servlet_GrpcResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ru_adk_grpc_servlet_GrpcResponse_descriptor,
        new java.lang.String[] { "ErrorCode", "ResponseData", "ErrorMessage", });
    ru.adk.protobuf.entity.ProtobufValueMessage.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
