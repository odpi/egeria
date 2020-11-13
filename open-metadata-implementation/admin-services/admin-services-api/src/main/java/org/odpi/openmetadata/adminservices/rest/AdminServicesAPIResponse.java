/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AdminServicesAPIResponse provides a common header for admin services managed response to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = AccessServiceConfigResponse.class, name = "AccessServiceConfigResponse"),
                @JsonSubTypes.Type(value = AccessServicesResponse.class, name = "AccessServicesResponse"),
                @JsonSubTypes.Type(value = CohortConfigResponse.class, name = "CohortConfigResponse"),
                @JsonSubTypes.Type(value = ConnectionListResponse.class, name = "ConnectionListResponse"),
                @JsonSubTypes.Type(value = ConnectionResponse.class, name = "ConnectionResponse"),
                @JsonSubTypes.Type(value = DiscoveryEngineServicesConfigResponse.class, name = "DiscoveryEngineServicesConfigResponse"),
                @JsonSubTypes.Type(value = EventBusConfigResponse.class, name = "EventBusConfigResponse"),
                @JsonSubTypes.Type(value = IntegrationServiceConfigResponse.class, name = "IntegrationServiceConfigResponse"),
                @JsonSubTypes.Type(value = IntegrationServicesResponse.class, name = "IntegrationServicesResponse"),
                @JsonSubTypes.Type(value = OMAGServerConfigResponse.class, name = "OMAGServerConfigResponse"),
                @JsonSubTypes.Type(value = ServerTypeClassificationResponse.class, name = "ServerClassificationResponse"),
                @JsonSubTypes.Type(value = ServerTypeClassificationSummary.class, name = "ServerTypeClassificationSummary"),
                @JsonSubTypes.Type(value = StewardshipEngineServicesConfigResponse.class, name = "StewardshipEngineServicesConfigResponse"),
                @JsonSubTypes.Type(value = SuccessMessageResponse.class, name = "SuccessMessageResponse"),
                @JsonSubTypes.Type(value = ViewServiceConfigResponse.class, name = "ViewServiceConfigResponse"),
                @JsonSubTypes.Type(value = ViewServicesResponse.class, name = "ViewServicesResponse"),
        })
public abstract class AdminServicesAPIResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public AdminServicesAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AdminServicesAPIResponse(AdminServicesAPIResponse  template)
    {
        super(template);
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AdminServicesAPIResponse{" +
                "exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
