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

/**
 * Protobuf type {@code ru.adk.grpc.servlet.GrpcRequest}
 */
public  final class GrpcRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:ru.adk.grpc.servlet.GrpcRequest)
    GrpcRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use GrpcRequest.newBuilder() to construct.
  private GrpcRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GrpcRequest() {
    serviceId_ = "";
    methodId_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private GrpcRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            serviceId_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            methodId_ = s;
            break;
          }
          case 26: {
            ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder subBuilder = null;
            if (requestData_ != null) {
              subBuilder = requestData_.toBuilder();
            }
            requestData_ = input.readMessage(ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(requestData_);
              requestData_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return ru.adk.grpc.servlet.GrpcServletMessage.internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return ru.adk.grpc.servlet.GrpcServletMessage.internal_static_ru_adk_grpc_servlet_GrpcRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            ru.adk.grpc.servlet.GrpcRequest.class, ru.adk.grpc.servlet.GrpcRequest.Builder.class);
  }

  public static final int SERVICEID_FIELD_NUMBER = 1;
  private volatile java.lang.Object serviceId_;
  /**
   * <code>string serviceId = 1;</code>
   */
  public java.lang.String getServiceId() {
    java.lang.Object ref = serviceId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      serviceId_ = s;
      return s;
    }
  }
  /**
   * <code>string serviceId = 1;</code>
   */
  public com.google.protobuf.ByteString
      getServiceIdBytes() {
    java.lang.Object ref = serviceId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      serviceId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int METHODID_FIELD_NUMBER = 2;
  private volatile java.lang.Object methodId_;
  /**
   * <code>string methodId = 2;</code>
   */
  public java.lang.String getMethodId() {
    java.lang.Object ref = methodId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      methodId_ = s;
      return s;
    }
  }
  /**
   * <code>string methodId = 2;</code>
   */
  public com.google.protobuf.ByteString
      getMethodIdBytes() {
    java.lang.Object ref = methodId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      methodId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REQUESTDATA_FIELD_NUMBER = 3;
  private ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue requestData_;
  /**
   * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
   */
  public boolean hasRequestData() {
    return requestData_ != null;
  }
  /**
   * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
   */
  public ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue getRequestData() {
    return requestData_ == null ? ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.getDefaultInstance() : requestData_;
  }
  /**
   * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
   */
  public ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValueOrBuilder getRequestDataOrBuilder() {
    return getRequestData();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getServiceIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, serviceId_);
    }
    if (!getMethodIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, methodId_);
    }
    if (requestData_ != null) {
      output.writeMessage(3, getRequestData());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getServiceIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, serviceId_);
    }
    if (!getMethodIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, methodId_);
    }
    if (requestData_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getRequestData());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof ru.adk.grpc.servlet.GrpcRequest)) {
      return super.equals(obj);
    }
    ru.adk.grpc.servlet.GrpcRequest other = (ru.adk.grpc.servlet.GrpcRequest) obj;

    boolean result = true;
    result = result && getServiceId()
        .equals(other.getServiceId());
    result = result && getMethodId()
        .equals(other.getMethodId());
    result = result && (hasRequestData() == other.hasRequestData());
    if (hasRequestData()) {
      result = result && getRequestData()
          .equals(other.getRequestData());
    }
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SERVICEID_FIELD_NUMBER;
    hash = (53 * hash) + getServiceId().hashCode();
    hash = (37 * hash) + METHODID_FIELD_NUMBER;
    hash = (53 * hash) + getMethodId().hashCode();
    if (hasRequestData()) {
      hash = (37 * hash) + REQUESTDATA_FIELD_NUMBER;
      hash = (53 * hash) + getRequestData().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static ru.adk.grpc.servlet.GrpcRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(ru.adk.grpc.servlet.GrpcRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code ru.adk.grpc.servlet.GrpcRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:ru.adk.grpc.servlet.GrpcRequest)
      ru.adk.grpc.servlet.GrpcRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ru.adk.grpc.servlet.GrpcServletMessage.internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ru.adk.grpc.servlet.GrpcServletMessage.internal_static_ru_adk_grpc_servlet_GrpcRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ru.adk.grpc.servlet.GrpcRequest.class, ru.adk.grpc.servlet.GrpcRequest.Builder.class);
    }

    // Construct using ru.adk.grpc.servlet.GrpcRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      serviceId_ = "";

      methodId_ = "";

      if (requestDataBuilder_ == null) {
        requestData_ = null;
      } else {
        requestData_ = null;
        requestDataBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return ru.adk.grpc.servlet.GrpcServletMessage.internal_static_ru_adk_grpc_servlet_GrpcRequest_descriptor;
    }

    @java.lang.Override
    public ru.adk.grpc.servlet.GrpcRequest getDefaultInstanceForType() {
      return ru.adk.grpc.servlet.GrpcRequest.getDefaultInstance();
    }

    @java.lang.Override
    public ru.adk.grpc.servlet.GrpcRequest build() {
      ru.adk.grpc.servlet.GrpcRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public ru.adk.grpc.servlet.GrpcRequest buildPartial() {
      ru.adk.grpc.servlet.GrpcRequest result = new ru.adk.grpc.servlet.GrpcRequest(this);
      result.serviceId_ = serviceId_;
      result.methodId_ = methodId_;
      if (requestDataBuilder_ == null) {
        result.requestData_ = requestData_;
      } else {
        result.requestData_ = requestDataBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof ru.adk.grpc.servlet.GrpcRequest) {
        return mergeFrom((ru.adk.grpc.servlet.GrpcRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(ru.adk.grpc.servlet.GrpcRequest other) {
      if (other == ru.adk.grpc.servlet.GrpcRequest.getDefaultInstance()) return this;
      if (!other.getServiceId().isEmpty()) {
        serviceId_ = other.serviceId_;
        onChanged();
      }
      if (!other.getMethodId().isEmpty()) {
        methodId_ = other.methodId_;
        onChanged();
      }
      if (other.hasRequestData()) {
        mergeRequestData(other.getRequestData());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      ru.adk.grpc.servlet.GrpcRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (ru.adk.grpc.servlet.GrpcRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object serviceId_ = "";
    /**
     * <code>string serviceId = 1;</code>
     */
    public java.lang.String getServiceId() {
      java.lang.Object ref = serviceId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        serviceId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string serviceId = 1;</code>
     */
    public com.google.protobuf.ByteString
        getServiceIdBytes() {
      java.lang.Object ref = serviceId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        serviceId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string serviceId = 1;</code>
     */
    public Builder setServiceId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      serviceId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string serviceId = 1;</code>
     */
    public Builder clearServiceId() {
      
      serviceId_ = getDefaultInstance().getServiceId();
      onChanged();
      return this;
    }
    /**
     * <code>string serviceId = 1;</code>
     */
    public Builder setServiceIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      serviceId_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object methodId_ = "";
    /**
     * <code>string methodId = 2;</code>
     */
    public java.lang.String getMethodId() {
      java.lang.Object ref = methodId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        methodId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string methodId = 2;</code>
     */
    public com.google.protobuf.ByteString
        getMethodIdBytes() {
      java.lang.Object ref = methodId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        methodId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string methodId = 2;</code>
     */
    public Builder setMethodId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      methodId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string methodId = 2;</code>
     */
    public Builder clearMethodId() {
      
      methodId_ = getDefaultInstance().getMethodId();
      onChanged();
      return this;
    }
    /**
     * <code>string methodId = 2;</code>
     */
    public Builder setMethodIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      methodId_ = value;
      onChanged();
      return this;
    }

    private ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue requestData_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValueOrBuilder> requestDataBuilder_;
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public boolean hasRequestData() {
      return requestDataBuilder_ != null || requestData_ != null;
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue getRequestData() {
      if (requestDataBuilder_ == null) {
        return requestData_ == null ? ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.getDefaultInstance() : requestData_;
      } else {
        return requestDataBuilder_.getMessage();
      }
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public Builder setRequestData(ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue value) {
      if (requestDataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        requestData_ = value;
        onChanged();
      } else {
        requestDataBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public Builder setRequestData(
        ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder builderForValue) {
      if (requestDataBuilder_ == null) {
        requestData_ = builderForValue.build();
        onChanged();
      } else {
        requestDataBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public Builder mergeRequestData(ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue value) {
      if (requestDataBuilder_ == null) {
        if (requestData_ != null) {
          requestData_ =
            ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.newBuilder(requestData_).mergeFrom(value).buildPartial();
        } else {
          requestData_ = value;
        }
        onChanged();
      } else {
        requestDataBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public Builder clearRequestData() {
      if (requestDataBuilder_ == null) {
        requestData_ = null;
        onChanged();
      } else {
        requestData_ = null;
        requestDataBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder getRequestDataBuilder() {
      
      onChanged();
      return getRequestDataFieldBuilder().getBuilder();
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    public ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValueOrBuilder getRequestDataOrBuilder() {
      if (requestDataBuilder_ != null) {
        return requestDataBuilder_.getMessageOrBuilder();
      } else {
        return requestData_ == null ?
            ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.getDefaultInstance() : requestData_;
      }
    }
    /**
     * <code>.ru.adk.protobuf.entity.ProtobufValue requestData = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValueOrBuilder> 
        getRequestDataFieldBuilder() {
      if (requestDataBuilder_ == null) {
        requestDataBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.Builder, ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValueOrBuilder>(
                getRequestData(),
                getParentForChildren(),
                isClean());
        requestData_ = null;
      }
      return requestDataBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:ru.adk.grpc.servlet.GrpcRequest)
  }

  // @@protoc_insertion_point(class_scope:ru.adk.grpc.servlet.GrpcRequest)
  private static final ru.adk.grpc.servlet.GrpcRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new ru.adk.grpc.servlet.GrpcRequest();
  }

  public static ru.adk.grpc.servlet.GrpcRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GrpcRequest>
      PARSER = new com.google.protobuf.AbstractParser<GrpcRequest>() {
    @java.lang.Override
    public GrpcRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new GrpcRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<GrpcRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GrpcRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public ru.adk.grpc.servlet.GrpcRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

