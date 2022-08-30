/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceActionProperties;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceActionTypeElement contains the properties and header for an entity retrieved from the metadata
 * repository that represents a governance action type (plus relevant relationships and properties).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader                   elementHeader         = null;
    private List<MetadataCorrelationHeader> correlationHeaders    = null;
    private GovernanceActionProperties      properties            = null;
    private List<RequestSourceElement>      requestSourceElements = null;
    private List<ActionTargetElement>       actionTargetElements  = null;

    /**
     * Default constructor
     */
    public GovernanceActionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionElement(GovernanceActionElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            correlationHeaders = template.getCorrelationHeaders();
            requestSourceElements = template.getRequestSourceElements();
            actionTargetElements = template.getActionTargetElements();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }



    /**
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    @Override
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
        if (correlationHeaders == null)
        {
            return null;
        }
        else if (correlationHeaders.isEmpty())
        {
            return null;
        }

        return correlationHeaders;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    @Override
    public void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders)
    {
        this.correlationHeaders = correlationHeaders;
    }



    /**
     * Return the properties of the governance action.
     *
     * @return properties bean
     */
    public GovernanceActionProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the governance action.
     *
     * @param properties properties bean
     */
    public void setProperties(GovernanceActionProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return the list of elements that triggered this request.
     *
     * @return list of elements
     */
    public List<RequestSourceElement> getRequestSourceElements()
    {
        if (requestSourceElements == null)
        {
            return null;
        }

        if (requestSourceElements.isEmpty())
        {
            return null;
        }

        return requestSourceElements;
    }


    /**
     * Set up the list of elements that triggered this request.
     *
     * @param requestSourceElements list of elements
     */
    public void setRequestSourceElements(List<RequestSourceElement> requestSourceElements)
    {
        this.requestSourceElements = requestSourceElements;
    }


    /**
     * Return the list of elements that the governance action will work on.
     *
     * @return list of elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        if (actionTargetElements == null)
        {
            return null;
        }

        if (actionTargetElements.isEmpty())
        {
            return null;
        }

        return actionTargetElements;
    }


    /**
     * Set up the list of elements that the governance action will work on.
     *
     * @param actionTargetElements list of elements
     */
    public void setActionTargetElements(List<ActionTargetElement> actionTargetElements)
    {
        this.actionTargetElements = actionTargetElements;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionElement{" +
                       "elementHeader=" + elementHeader +
                       ", correlationHeaders=" + correlationHeaders +
                       ", properties=" + properties +
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        GovernanceActionElement that = (GovernanceActionElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(correlationHeaders, that.correlationHeaders) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(requestSourceElements, that.requestSourceElements) &&
                       Objects.equals(actionTargetElements, that.actionTargetElements);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, correlationHeaders, properties, requestSourceElements, actionTargetElements);
    }
}
