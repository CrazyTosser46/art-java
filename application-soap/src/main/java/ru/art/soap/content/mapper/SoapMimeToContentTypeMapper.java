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

package ru.art.soap.content.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.entity.ContentType;
import ru.art.http.constants.HttpMimeTypes;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.core.mime.MimeType;

@Getter
@AllArgsConstructor
public class SoapMimeToContentTypeMapper {
    private MimeType mimeType;
    private ContentType contentType;

    public static SoapMimeToContentTypeMapper textXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.TEXT_XML, ContentType.TEXT_XML);
    }

    public static SoapMimeToContentTypeMapper applicationXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.APPLICATION_XML, ContentType.APPLICATION_XML);
    }

    public static SoapMimeToContentTypeMapper applicationSoapXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.APPLICATION_SOAP_XML, ContentType.APPLICATION_XML);
    }

    public MimeToContentTypeMapper toHttpMimeToContentTypeMapper() {
        return new MimeToContentTypeMapper(mimeType, contentType);
    }
}
