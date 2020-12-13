package ru.art.reindexer.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConnectionOpts implements Serializable {
    private int clusterId;
    private Boolean openNamespace;
    private Boolean allowNamespaceError;
    private Boolean autorepair;
    private Boolean checkClusterId;
}
