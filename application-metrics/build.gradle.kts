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
        applicationService()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("io.micrometer", "micrometer-registry-prometheus", micrometerPrometheusVersion)
        embedded("io.github.mweirauch", "micrometer-jvm-extras", micrometerJvmExtrasVersion)
                .exclude("org.slf4j")
        embedded("io.prometheus", "simpleclient_dropwizard", prometheusDropwizardSimpleClientVersion)
                .exclude("org.slf4j")
        embedded("io.dropwizard.metrics", "metrics-jvm", dropwizardMetricsVersion)
                .exclude("io.dropwizard.metrics", "metrics-core")
                .exclude("org.slf4j")
    }
}