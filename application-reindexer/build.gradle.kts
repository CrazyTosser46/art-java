
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

import com.google.protobuf.gradle.*
import java.nio.file.Files
import java.nio.file.Paths

plugins {
    id("com.google.protobuf")
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    embedded("com.google.protobuf:protobuf-java:3.13.0")
}

sourceSets {
    main {
        proto.srcDir("src/main/proto")
        java.srcDir("${buildDir}/generated/source/proto/main/java")
    }
}

protobuf {
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.33.1"
        }
    }
    protoc {
        artifact = "com.google.protobuf:protoc:3.13.0"
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.builtins {
                create("cpp") {
                }
            }
        }
    }
}

tasks.register<Copy>("copyProtoFilesForCpp") {
    Files.createDirectories( Paths.get("${projectDir}/src/main/cpp/src/api/proto"))
    from("${buildDir}/generated/source/proto/main/cpp")
    into("${projectDir}/src/main/cpp/src/api/proto")
}

tasks.clean {
    delete.add("${projectDir}/src/main/cpp/src/api/proto")
    delete.add("${projectDir}/src/main/cpp/src/api/java")
}

tasks.compileJava {
    options.compilerArgs.addAll(mutableListOf("-h", "${projectDir}/src/main/cpp/src/api/java"))
    options.isFork = true
    options.forkOptions.executable = "javac"
}

tasks.classes {
    dependsOn("copyProtoFilesForCpp")
}