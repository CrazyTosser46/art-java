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

art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        with(embedded("org.apache.kafka", "kafka_2.12", kafkaVersion)) {
            exclude("org.slf4j", "slf4j-api")
            exclude("org.slf4j", "slf4j-log4j12")
            exclude("org.slf4j", "jul-to-slf4j")
        }
    }
}