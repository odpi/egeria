/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalStandardMapping defines a mapping of TypeDefs and TypeDefAttributes to an external standard.  It includes the name
 * of the standard, the organization that owns the standard and the equivalent type in the external standard.
 * This mapping is done on a property type by property type basis.  The aim is to create clarity on the meaning
 * of the open metadata types and support importers and exporters between open metadata types and external standards.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalStandardMapping extends TypeDefElementHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private   String standardName = null;
    private   String standardOrganization = null;
    private   String standardTypeName = null;


    /**
     * Default Constructor initializes to null.
     */
    public ExternalStandardMapping()
    {
        /*
         * Initialize superclass.
         */
        super();
    }


    /**
     * Copy/clone constructor copies values from supplied template.
     *
     * @param templateElement template to copy.
     */
    public ExternalStandardMapping(ExternalStandardMapping  templateElement)
    {
        /*
         * Initialize superclass.
         */
        super(templateElement);

        /*
         * Copy the template values over.
         */
        this.standardName = templateElement.getStandardName();
        this.standardOrganization = templateElement.getStandardOrganization();
        this.standardTypeName = templateElement.getStandardTypeName();
    }


    /**
     * Return the name of the standard that this mapping relates to.
     *
     * @return String standard name
     */
    public String getStandardName() {
        return standardName;
    }


    /**
     * Set up the name of the standard that this mapping relates to.
     *
     * @param standardName String standard name
     */
    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }


    /**
     * Return the name of organization that owns the standard that this mapping refers to.
     *
     * @return String organization name
     */
    public String getStandardOrganization() {
        return standardOrganization;
    }


    /**
     * Set up the name of the organization that owns the standard that this mapping refers to.
     *
     * @param standardOrganization String organization name
     */
    public void setStandardOrganization(String standardOrganization)
    {
        this.standardOrganization = standardOrganization;
    }


    /**
     * Return the name of the type from the standard that is equivalent to the linked open metadata type.
     *
     * @return String type name from standard
     */
    public String getStandardTypeName()
    {
        return standardTypeName;
    }


    /**
     * Set up the name of the type from the standard that is equivalent to the linked open metadata type.
     *
     * @param standardTypeName String type name from standard
     */
    public void setStandardTypeName(String standardTypeName) {
        this.standardTypeName = standardTypeName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ExternalStandardMapping{" +
                "standardName='" + standardName + '\'' +
                ", standardOrganization='" + standardOrganization + '\'' +
                ", standardTypeName='" + standardTypeName + '\'' +
                '}';
    }


    /**
     * Verify that supplied object has the same properties.
     *
     * @param objectToCompare object to test
     * @return result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ExternalStandardMapping))
        {
            return false;
        }
        ExternalStandardMapping that = (ExternalStandardMapping) objectToCompare;
        return Objects.equals(getStandardName(), that.getStandardName()) &&
                Objects.equals(getStandardOrganization(), that.getStandardOrganization()) &&
                Objects.equals(getStandardTypeName(), that.getStandardTypeName());
    }


    /**
     * Integer for hashmaps
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getStandardName(), getStandardOrganization(), getStandardTypeName());
    }
}
