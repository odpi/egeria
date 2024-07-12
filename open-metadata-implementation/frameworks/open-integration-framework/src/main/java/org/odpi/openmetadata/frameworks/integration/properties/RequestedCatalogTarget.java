/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;

import java.util.Objects;

/**
 * RequestedCatalogTarget describes a catalog target that an integration connector should refresh.
 */
public class RequestedCatalogTarget extends CatalogTarget
{
    private Connector catalogTargetConnector = null;

    /**
     * Default constructor
     */
    public RequestedCatalogTarget()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestedCatalogTarget(CatalogTarget template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestedCatalogTarget(RequestedCatalogTarget template)
    {
        super(template);

        if (template != null)
        {
            catalogTargetConnector = template.getCatalogTargetConnector();
        }
    }



    public Connector getCatalogTargetConnector()
    {
        return catalogTargetConnector;
    }


    public void setCatalogTargetConnector(Connector catalogTargetConnector)
    {
        this.catalogTargetConnector = catalogTargetConnector;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestedCatalogTarget{" +
                "catalogTargetConnector=" + catalogTargetConnector +
                "} " + super.toString();
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
        RequestedCatalogTarget that = (RequestedCatalogTarget) objectToCompare;
        return Objects.equals(catalogTargetConnector, that.catalogTargetConnector);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), catalogTargetConnector);
    }
}
