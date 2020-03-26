package org.odpi.openmetadata.accessservices.dataplatform.responses;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

public class DataPlatformOMASAPIResponse extends FFDCResponseBase  {

    private static final long serialVersionUID = 1L;

    private SoftwareServerCapability softwareServerCapability;

    public SoftwareServerCapability getSoftwareServerCapability() {
        return softwareServerCapability;
    }

    public void setSoftwareServerCapability(SoftwareServerCapability softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }
}
