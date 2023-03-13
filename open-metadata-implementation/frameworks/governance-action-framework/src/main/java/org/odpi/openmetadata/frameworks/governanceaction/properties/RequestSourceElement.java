/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestSourceElement describes the element that triggered the request to the governance action service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestSourceElement implements Serializable
{
    private static final long      serialVersionUID = 1L;

    private String              requestSourceName       = null;
    private OpenMetadataElement requestSourceElement    = null;
    private String              originGovernanceService = null;
    private String              originGovernanceEngine  = null;


    /**
     * Typical Constructor
     */
    public RequestSourceElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public RequestSourceElement(RequestSourceElement template)
    {
        if (template != null)
        {
            requestSourceName       = template.getRequestSourceName();
            requestSourceElement    = template.getRequestSourceElement();
            originGovernanceService = template.getOriginGovernanceService();
            originGovernanceEngine  = template.getOriginGovernanceEngine();
        }
    }


    /**
     * Return the assigned to this request source.  This name heps to guide the governance service in its processing.
     *
     * @return string name
     */
    public String getRequestSourceName()
    {
        return requestSourceName;
    }


    /**
     * Set up the assigned to this request source.  This name heps to guide the governance service in its processing.
     *
     * @param requestSourceName string name
     */
    public void setRequestSourceName(String requestSourceName)
    {
        this.requestSourceName = requestSourceName;
    }


    /**
     * Return the specific element that caused the governance action service to be started.
     *
     * @return metadata element
     */
    public OpenMetadataElement getRequestSourceElement()
    {
        return requestSourceElement;
    }


    /**
     * Set up the specific element that caused the governance action service to be started.
     *
     * @param requestSourceElement metadata element
     */
    public void setRequestSourceElement(OpenMetadataElement requestSourceElement)
    {
        this.requestSourceElement = requestSourceElement;
    }


    /**
     * Return the governance service that created this governance action (if any).
     *
     * @return string name
     */
    public String getOriginGovernanceService()
    {
        return originGovernanceService;
    }


    /**
     * Set up the governance service that created this governance action (if any).
     *
     * @param originGovernanceService string name
     */
    public void setOriginGovernanceService(String originGovernanceService)
    {
        this.originGovernanceService = originGovernanceService;
    }


    /**
     * Return the governance engine that created this governance action (if any).
     *
     * @return string name
     */
    public String getOriginGovernanceEngine()
    {
        return originGovernanceEngine;
    }


    /**
     * Set up the governance engine that created this governance action (if any).
     *
     * @param originGovernanceEngine string name
     */
    public void setOriginGovernanceEngine(String originGovernanceEngine)
    {
        this.originGovernanceEngine = originGovernanceEngine;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RequestSourceElement{" +
                       "requestSourceName=" + requestSourceName +
                       ", requestSourceElement=" + requestSourceElement +
                       ", originGovernanceService=" + originGovernanceService +
                       ", originGovernanceEngine=" + originGovernanceEngine +
                       '}';
    }



    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        RequestSourceElement that = (RequestSourceElement) objectToCompare;
        return Objects.equals(requestSourceName, that.requestSourceName) &&
                       Objects.equals(requestSourceElement, that.requestSourceElement) &&
                       Objects.equals(originGovernanceService, that.originGovernanceService) &&
                       Objects.equals(originGovernanceEngine, that.originGovernanceEngine);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(requestSourceName, requestSourceElement, originGovernanceService, originGovernanceEngine);
    }
}
