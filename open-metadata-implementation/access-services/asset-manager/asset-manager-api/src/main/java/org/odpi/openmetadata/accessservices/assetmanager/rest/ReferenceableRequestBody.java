/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ReferenceableRequestBody describes the request body used to create/update properties in referenceables.  The properties
 * of the specific element must inherit from ReferenceableProperties and be included in the subtype list of the ReferenceableProperties class
 * to make it possible for the server to correctly unpack this bean.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableRequestBody
{
    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private ReferenceableProperties       elementProperties             = null;
    private String                        parentGUID                    = null;


    /**
     * Default constructor
     */
    public ReferenceableRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReferenceableRequestBody(ReferenceableRequestBody template)
    {
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            elementProperties = template.getElementProperties();
            parentGUID = template.getParentGUID();
        }
    }


    /**
     * Return the properties used to correlate the external metadata element with the open metadata element.
     *
     * @return properties object
     */
    public MetadataCorrelationProperties getMetadataCorrelationProperties()
    {
        return metadataCorrelationProperties;
    }


    /**
     * Set up the properties used to correlate the external metadata element with the open metadata element.
     *
     * @param metadataCorrelationProperties properties object
     */
    public void setMetadataCorrelationProperties(MetadataCorrelationProperties metadataCorrelationProperties)
    {
        this.metadataCorrelationProperties = metadataCorrelationProperties;
    }


    /**
     * Return the properties for the element.
     *
     * @return properties object
     */
    public ReferenceableProperties getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param elementProperties properties object
     */
    public void setElementProperties(ReferenceableProperties elementProperties)
    {
        this.elementProperties = elementProperties;
    }


    /**
     * Return an optional parent GUID to attach the new element to.
     *
     * @return guid
     */
    public String getParentGUID()
    {
        return parentGUID;
    }


    /**
     * Set up an optional parent GUID to attach the new element to.
     *
     * @param parentGUID guid
     */
    public void setParentGUID(String parentGUID)
    {
        this.parentGUID = parentGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceableRequestBody{" +
                       "metadataCorrelationProperties=" + metadataCorrelationProperties +
                       ", elementProperties=" + elementProperties +
                       ", parentGUID='" + parentGUID + '\'' +
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
        if (! (objectToCompare instanceof ReferenceableRequestBody that))
        {
            return false;
        }
        return Objects.equals(metadataCorrelationProperties, that.metadataCorrelationProperties) &&
                       Objects.equals(elementProperties, that.elementProperties) &&
                       Objects.equals(parentGUID, that.parentGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), metadataCorrelationProperties, elementProperties, parentGUID);
    }
}
