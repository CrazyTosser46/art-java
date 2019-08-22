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

package ru.art.core.extension;

import ru.art.core.constants.*;
import java.util.*;

import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;

public interface StringExtensions {
    static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (isEmpty(coll)) {
            return StringConstants.EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, StringConstants.EMPTY_STRING, StringConstants.EMPTY_STRING);
    }

    static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, StringConstants.COMMA);
    }

    static <T> String toString(T val, String ifNull) {
        if (isNull(val)) return ifNull;
        return val.toString();
    }

    static String rewrite(String current, Map<String, String> rewritingMap) {
        if (isEmpty(rewritingMap)) return current;
        if (isEmpty(current)) return rewritingMap.getOrDefault(current, EMPTY_STRING);
        String newValue;
        return isNull((newValue = rewritingMap.get(current))) ? current : newValue;
    }

    static <T> String emptyIfNull(T val) {
        return isNull(val) || val.equals(NULL_STRING) ? EMPTY_STRING : val.toString();
    }

    static boolean isQuotedString(String s) {
        return s.length() >= 2 && ((s.startsWith(StringConstants.DOUBLE_QUOTES) && s.endsWith(StringConstants.DOUBLE_QUOTES)) || (s.startsWith(StringConstants.SINGLE_QUOTE) && s.endsWith(StringConstants.SINGLE_QUOTE)));
    }

    static String unquote(String s) {
        if (isNull(s)) {
            return EMPTY_STRING;
        }
        return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
    }

}
