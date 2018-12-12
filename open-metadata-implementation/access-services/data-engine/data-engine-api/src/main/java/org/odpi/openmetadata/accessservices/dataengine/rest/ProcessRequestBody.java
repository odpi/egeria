package org.odpi.openmetadata.accessservices.dataengine.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessRequestBody extends DataEngineOMASAPIRequestBody {
    private String name;
    private String displayName;
}
