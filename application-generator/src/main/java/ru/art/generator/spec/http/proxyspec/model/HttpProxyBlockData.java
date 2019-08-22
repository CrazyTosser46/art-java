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

package ru.art.generator.spec.http.proxyspec.model;

import com.squareup.javapoet.*;
import lombok.*;
import ru.art.generator.spec.http.proxyspec.constants.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Data used in generating method's constants' codeblocks and
 * in filling array of codeblocks in proper order for these constants.
 */
@Builder
@Getter
@Setter
public class HttpProxyBlockData {
    Method method;
    HttpProxyMethodsAnnotations hasAnnotations;
    Map<HttpProxySpecAnnotations, CodeBlock> generatedFields;
    Class reqMapper;
    Class respMapper;
    StaticImports imports;
}
