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

package ru.art.http.constants;

import lombok.*;
import org.apache.http.entity.*;
import ru.art.core.mime.*;
import static org.apache.http.entity.ContentType.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.http.constants.HttpMimeTypes.*;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MimeToContentTypeMapper {
    @EqualsAndHashCode.Include
    private MimeType mimeType;
    private ContentType contentType;

    public static MimeToContentTypeMapper all() {
        return new MimeToContentTypeMapper(HttpMimeTypes.ALL, ContentType.WILDCARD);
    }

    public static MimeToContentTypeMapper applicationAtomXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_ATOM_XML, ContentType.APPLICATION_ATOM_XML);
    }

    public static MimeToContentTypeMapper applicationFormUrlencoded() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_FORM_URLENCODED, ContentType.APPLICATION_FORM_URLENCODED);
    }

    public static MimeToContentTypeMapper applicationJson() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_JSON, ContentType.APPLICATION_JSON);
    }

    public static MimeToContentTypeMapper applicationJsonUtf8() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_JSON_UTF8, ContentType.APPLICATION_JSON);
    }

    public static MimeToContentTypeMapper applicationJsonWindows1251() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_JSON_WIN_1251, ContentType.APPLICATION_JSON);
    }

    public static MimeToContentTypeMapper applicationOctetStream() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_OCTET_STREAM, ContentType.APPLICATION_OCTET_STREAM);
    }

    public static MimeToContentTypeMapper applicationOctetStreamUtf8() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_OCTET_STREAM_UTF_8, ContentType.APPLICATION_OCTET_STREAM);
    }

    public static MimeToContentTypeMapper applicationXhtmlXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_XHTML_XML, ContentType.APPLICATION_XHTML_XML);
    }

    public static MimeToContentTypeMapper applicationXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_XML, ContentType.APPLICATION_XML);
    }

    public static MimeToContentTypeMapper imageGif() {
        return new MimeToContentTypeMapper(HttpMimeTypes.IMAGE_GIF, ContentType.IMAGE_GIF);
    }

    public static MimeToContentTypeMapper imageJpeg() {
        return new MimeToContentTypeMapper(HttpMimeTypes.IMAGE_JPEG, ContentType.IMAGE_JPEG);
    }

    public static MimeToContentTypeMapper imagePng() {
        return new MimeToContentTypeMapper(HttpMimeTypes.IMAGE_PNG, ContentType.IMAGE_PNG);
    }

    public static MimeToContentTypeMapper imageWebp() {
        return new MimeToContentTypeMapper(HttpMimeTypes.IMAGE_WEBP, ContentType.IMAGE_WEBP);
    }

    public static MimeToContentTypeMapper multipartFormData() {
        return new MimeToContentTypeMapper(HttpMimeTypes.MULTIPART_FORM_DATA, ContentType.MULTIPART_FORM_DATA);
    }

    public static MimeToContentTypeMapper textHtml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_HTML, ContentType.TEXT_HTML);
    }

    public static MimeToContentTypeMapper textHtmlUtf8() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_HTML_UTF_8, ContentType.TEXT_HTML);
    }

    public static MimeToContentTypeMapper textPlain() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_PLAIN, ContentType.TEXT_PLAIN);
    }

    public static MimeToContentTypeMapper textXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_XML, ContentType.TEXT_XML);
    }

    public static MimeToContentTypeMapper textXmlWin1251() {
        return new MimeToContentTypeMapper(TEXT_XML_WIN_1251, create(TEXT_XML_WIN_1251.getType() + SLASH + TEXT_XML_WIN_1251.getSubtype(), TEXT_XML_WIN_1251.getCharset()));
    }

    public static MimeToContentTypeMapper textXmlUtf8() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_XML_UTF_8, ContentType.TEXT_XML);
    }

    public static MimeToContentTypeMapper applicationSoapXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_SOAP_XML, ContentType.APPLICATION_XML);
    }

    public static MimeToContentTypeMapper applicationRssXml() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_RSS_XML, ContentType.APPLICATION_XML);
    }

    public static MimeToContentTypeMapper applicationPdf() {
        return new MimeToContentTypeMapper(HttpMimeTypes.APPLICATION_PDF, ContentType.create("application/pdf"));
    }

    public static MimeToContentTypeMapper textCsv() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_CSV, ContentType.create("text/csv"));
    }

    public static MimeToContentTypeMapper textCsvUtf8() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_CSV, ContentType.create("text/csv"));
    }

    public static MimeToContentTypeMapper textCss() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_CSS, ContentType.create("text/css"));
    }

    public static MimeToContentTypeMapper textJs() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_JS, ContentType.create("text/js"));
    }

    public static MimeToContentTypeMapper textEventStream() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_EVENT_STREAM, ContentType.create("text/event-stream"));
    }

    public static MimeToContentTypeMapper textMarkdown() {
        return new MimeToContentTypeMapper(HttpMimeTypes.TEXT_MARKDOWN, ContentType.create("text/markdown"));
    }
}
