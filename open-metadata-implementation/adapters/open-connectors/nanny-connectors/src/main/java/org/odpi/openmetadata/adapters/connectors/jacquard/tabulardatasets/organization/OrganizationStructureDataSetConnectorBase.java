/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * OrganizationStructureDataSetConnectorBase retrieves the teams and their reporting structure for the data sets
 * that describe the organization.
 */
public abstract class OrganizationStructureDataSetConnectorBase extends OpenMetadataRowDataSetConnectorBase
{
    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     * @param productDefinition definition of the product that this connector serves
     */
    public OrganizationStructureDataSetConnectorBase(String            connectorName,
                                                     ProductDefinition productDefinition)
    {
        super(connectorName, productDefinition);
    }


    /**
     * Retrieve the teams and the TeamStructure relationships between them.
     *
     * @return organization structure
     * @throws Exception problem retrieving the metadata
     */
    OrganizationStructure getOrganizationStructure() throws Exception
    {
        return new OrganizationStructure(getAllElements(OpenMetadataType.TEAM.typeName, 0),
                                         getAllRelationships(OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName));
    }
}
