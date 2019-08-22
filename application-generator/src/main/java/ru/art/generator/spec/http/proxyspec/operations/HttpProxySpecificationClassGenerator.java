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

package ru.art.generator.spec.http.proxyspec.operations;

import com.squareup.javapoet.*;
import lombok.*;
import ru.art.generator.spec.common.exception.*;
import ru.art.generator.spec.http.proxyspec.constants.*;
import ru.art.generator.spec.http.proxyspec.exception.*;
import ru.art.generator.spec.http.proxyspec.model.*;
import ru.art.http.client.specification.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import static java.text.MessageFormat.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.common.constants.Constants.*;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.common.constants.ExceptionConstants.*;
import static ru.art.generator.common.operations.CommonOperations.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.SpecificationGeneratorExceptions.*;
import static ru.art.generator.spec.common.constants.SpecificationType.*;
import static ru.art.generator.spec.common.operations.ConstantsFieldSpecGenerator.*;
import static ru.art.generator.spec.common.operations.ExecuteMethodGenerator.*;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.*;
import static ru.art.generator.spec.http.proxyspec.operations.HttpProxyAuxiliaryOperations.*;
import static ru.art.generator.spec.http.proxyspec.operations.HttpProxyBlockGenerator.*;

/**
 * Interface contains operations for HTTP Proxy specification generator.
 */
public interface HttpProxySpecificationClassGenerator {

    List<String> methodIds = new ArrayList<>();
    Map<String, HttpProxyMethodsAnnotations> methodAnnotations = new HashMap<>();
    Map<String, Map<String, HttpProxySpecAnnotations>> notGeneratedFieldsForMethod = new HashMap<>();

    /**
     * Method creates http proxy specification class based on service interface.
     *
     * @param service       - service model interface.
     * @param genPackage    - string value of spec's package.
     * @param jarPathToMain - classpath from root to main.
     * @throws HttpProxySpecGeneratorException is thrown when
     *                                         StringIndexOutOfBoundsException, IOException or NullPointerException
     *                                         occurs while writing to file.
     */
    static void createProxySpecificationClass(Class<?> service, String genPackage, String jarPathToMain) throws HttpProxySpecGeneratorException {
        printMessage(format(START_GENERATING, service.getSimpleName()) + SPECIFICATION);
        Map<Method, StaticImports> importsForMethods = new HashMap<>();
        TypeSpec type = generateSpecification(service, importsForMethods);

        try {
            JavaFile.Builder javaFileBuilder = JavaFile.builder(genPackage, type)
                    .indent(TABULATION);

            addStaticImports(javaFileBuilder, importsForMethods);

            String classJarPath = defineClassJarPath(service, jarPathToMain);

            javaFileBuilder.build().writeTo(new File(classJarPath.subSequence(0, classJarPath.indexOf(BUILD)).toString() + SRC_MAIN_JAVA));
            printMessage(format(GENERATED_SUCCESSFULLY, service.getSimpleName() + PROXY_SPEC));
        } catch (StringIndexOutOfBoundsException e) {
            throw new HttpProxySpecGeneratorException(format(UNABLE_TO_PARSE_JAR_PATH, service.getSimpleName()), e);
        } catch (IOException e) {
            throw new HttpProxySpecGeneratorException(format(UNABLE_TO_WRITE_TO_FILE, service.getSimpleName() + SPECIFICATION), e);
        } catch (NullPointerException e) {
            throw new HttpProxySpecGeneratorException(format(UNABLE_TO_FIND_A_PATH_FOR_CLASS, service.getSimpleName()), e);
        } catch (Throwable e) {
            throw new HttpProxySpecGeneratorException(format(UNABLE_TO_CREATE_FILE_UNKNOWN_ERROR, service.getSimpleName(), e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Generate block with "serviceId", "httpService" constants and "executeMethod" method for class.
     *
     * @param serviceClass      - service model interface.
     * @param importsForMethods - map with boolean values to define imports to include.
     *                          to define imports witch need to be included based on each method of service.
     * @return TypeSpec containing http service specification.
     * @throws HttpProxySpecGeneratorException is thrown when
     *                                         ExecuteMethodGenerationException or any other exception is occurred.
     */
    static TypeSpec generateSpecification(Class<?> serviceClass, Map<Method, StaticImports> importsForMethods) throws HttpProxySpecGeneratorException {
        try {
            return TypeSpec.classBuilder(serviceClass.getSimpleName() + PROXY_SPEC)
                    .addAnnotation(Getter.class)
                    .addAnnotation(RequiredArgsConstructor.class)
                    .addModifiers(PUBLIC)
                    .addField(generateServiceId(serviceClass, httpProxySpec))
                    .addFields(generateProxySpecFieldsBlock())
                    .addFields(generateHttpProxyBlock(serviceClass, importsForMethods))
                    .addFields(generateMethodIdsConstantsBlock(httpProxySpec))
                    .addMethod(generateExecuteMethod(serviceClass, httpProxySpec))
                    .addSuperinterface(HttpCommunicationSpecification.class)
                    .addJavadoc(JAVADOC, serviceClass)
                    .build();
        } catch (ExecuteMethodGenerationException | SpecificationTypeDefinitionException e) {
            throw new HttpProxySpecGeneratorException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new HttpProxySpecGeneratorException(format(UNABLE_TO_GENERATE_SPECIFICATION, serviceClass.getSimpleName(), e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Generate constants for each service's methods separately by theirs annotations.
     *
     * @param serviceClass      - service model interface.
     * @param importsForMethods - map with boolean values to define imports to include.
     *                          to define imports witch need to be included based on each method of service.
     * @return List of FieldSpecs containing constants.
     */
    static Iterable<FieldSpec> generateHttpProxyBlock(Class<?> serviceClass, Map<Method, StaticImports> importsForMethods) {
        ArrayList<FieldSpec> fields = new ArrayList<>();
        for (Method method : serviceClass.getDeclaredMethods()) {
            FieldSpec newMethod = generateCurrentMethodBlock(method, importsForMethods);
            if (isNotEmpty(newMethod))
                fields.add(newMethod);
        }
        return fields;
    }
}
