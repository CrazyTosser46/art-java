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

package ru.art.generator.spec.http.proxyspec.constants;

import ru.art.http.constants.*;

/**
 * Enum containing all {@link HttpMimeTypes} and theirs {@link MimeToContentTypeMapper} method's names.
 */
public enum MimeTypes {
    ALL("all()"),
    APPLICATION_ATOM_XML("applicationAtomXml()"),
    APPLICATION_FORM_URLENCODED("applicationFormUrlencoded()"),
    APPLICATION_JSON("applicationJson()"),
    APPLICATION_JSON_UTF8("applicationJsonUtf8()"),
    APPLICATION_JSON_WIN_1251("applicationJsonWindows1251()"),
    APPLICATION_OCTET_STREAM("applicationOctetStream()"),
    APPLICATION_OCTET_STREAM_UTF_8("applicationOctetStreamUtf8()"),
    APPLICATION_XHTML_XML("applicationXhtmlXml()"),
    APPLICATION_XML("applicationXml()"),
    APPLICATION_SOAP_XML("applicationSoapXml()"),
    APPLICATION_PDF("applicationPdf()"),
    APPLICATION_RSS_XML("applicationRssXml()"),
    IMAGE_GIF("imageGif()"),
    IMAGE_JPEG("imageJpeg()"),
    IMAGE_PNG("imagePng()"),
    IMAGE_WEBP("imageWebp()"),
    MULTIPART_FORM_DATA("multipartFormData()"),
    TEXT_HTML("textHtml()"),
    TEXT_PLAIN("textPlain()"),
    TEXT_XML("textXml()"),
    TEXT_XML_UTF_8("textXmlUtf8()"),
    TEXT_CSV("textCsv()"),
    TEXT_CSV_UTF_8("textCsvUtf8()"),
    TEXT_CSS("textCss()"),
    TEXT_JS("textJs()"),
    TEXT_EVENT_STREAM("textEventStream()"),
    TEXT_HTML_UTF_8("textHtmlUtf8()"),
    TEXT_MARKDOWN("textMarkdown()"),
    TEXT_XML_WIN_1251("textXmlWin1251()");

    private String method;

    public String getMethod() {
        return method;
    }

    MimeTypes(String method) {
        this.method = method;
    }
}
