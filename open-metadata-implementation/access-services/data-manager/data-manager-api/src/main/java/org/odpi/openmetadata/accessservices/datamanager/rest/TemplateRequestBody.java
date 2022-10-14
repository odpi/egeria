/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TemplateRequestBody carries the parameters for creating a new asset using a template.
 * The external source identifiers indicate whether the new asset should be created as an external element or not.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateRequestBody extends TemplateProperties
{
    private static final long    serialVersionUID = 1L;

    private String externalSourceGUID         = null;
    private String externalSourceName         = null;
    private String parentGUID = null;


    /**
     * Default constructor
     */
    public TemplateRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TemplateRequestBody(TemplateRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
            parentGUID         = template.getParentGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TemplateRequestBody(TemplateProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
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
        return "TemplateRequestBody{" +
                       "qualifiedName='" + getQualifiedName() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", pathName='" + getPathName() + '\'' +
                       ", networkAddress='" + getNetworkAddress() + '\'' +
                       ", externalSourceGUID='" + externalSourceGUID + '\'' +
                       ", externalSourceName='" + externalSourceName + '\'' +
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
        if (! (objectToCompare instanceof TemplateRequestBody))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        TemplateRequestBody that = (TemplateRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                       Objects.equals(externalSourceName, that.externalSourceName) && Objects.equals(parentGUID, that.parentGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName, parentGUID);
    }
}