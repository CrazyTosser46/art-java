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

package ru.art.generator.spec.http.servicespec.model;

import com.squareup.javapoet.*;
import lombok.*;
import ru.art.generator.spec.http.servicespec.constants.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Data used in generating httpService constant's codeblocks and
 * in filling array of codeblocks in proper order for generated httpService constant.
 */
@Getter
@Setter
@Builder
public class HttpServiceBlockData {
    StaticImports imports;
    Map<HttpServiceSpecAnnotations, CodeBlock> generatedFields;
    Method method;
    HttpServiceMethodsAnnotations hasAnnotations;
    HttpServiceSpecAnnotations fromAnnotation;
    HttpServiceSpecAnnotations validationPolicy;
}
