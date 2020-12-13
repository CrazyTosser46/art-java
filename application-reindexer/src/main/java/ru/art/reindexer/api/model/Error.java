package ru.art.reindexer.api.model;

import lombok.Data;

@Data
public class Error {
    private ErrorCode errorCode;
    private String message;
}

enum ErrorCode {
    OK,
    PARSE_SQL,
    QUERY_EXEC,
    PARAMS,
    LOGIC,
    PARSE_JSON,
    PARSE_DSL,
    CONFLICT,
    PARSE_BIN,
    FORBIDDEN,
    WAS_RELOCK,
    NOT_VALID,
    NETWORK,
    NOT_FOUND,
    STATE_INVALIDATED,
    BAD_TRANSACTION,
    OUT_DATED_WAL,
    NO_WAL,
    DATA_HASHM_ISMATCH,
    TIMEOUT,
    CANCELED,
    TAGS_MISSMATCH,
    REPL_PARAMS,
    NAMESPACE_INVALIDATED,
    PARSE_MSG_PACK,
}