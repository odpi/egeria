/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CatalogTarget contains the properties for one of the integration connector's catalog target.
 * It is linked via the CatalogTarget relationship to one or more elements that the integration connector is working on.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogTarget extends ElementStub
{
    @Serial
    private static final long     serialVersionUID = 1L;

    private String catalogTargetName = null;


    /**
     * Default constructor
     */
    public CatalogTarget()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CatalogTarget(CatalogTarget template)
    {
        super (template);

        if (template != null)
        {
            catalogTargetName = template.getCatalogTargetName();
        }
    }


    /**
     * Return the name of the catalog target.
     *
     * @return string
     */
    public String getCatalogTargetName()
    {
        return catalogTargetName;
    }


    /**
     * Set up the name of the catalog target.
     *
     * @param catalogTargetName string
     */
    public void setCatalogTargetName(String catalogTargetName)
    {
        this.catalogTargetName = catalogTargetName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CatalogTarget{" +
                "catalogTargetName='" + catalogTargetName + '\'' +
                ", uniqueName='" + getUniqueName() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", origin=" + getOrigin() +
                ", versions=" + getVersions() +
                ", headerVersion=" + getHeaderVersion() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CatalogTarget that = (CatalogTarget) objectToCompare;
        return Objects.equals(catalogTargetName, that.catalogTargetName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), catalogTargetName);
    }
}
