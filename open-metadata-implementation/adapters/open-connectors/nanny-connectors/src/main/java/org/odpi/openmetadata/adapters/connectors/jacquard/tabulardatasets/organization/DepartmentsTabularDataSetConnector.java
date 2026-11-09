/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;

import java.util.Map;


/**
 * DepartmentsTabularDataSetConnector lists the departments in open metadata - the teams that are part of an
 * organization's reporting structure - as if they are a tabular data set.  Each row says which team the
 * department reports to and which organization is at the top of its structure.
 */
public class DepartmentsTabularDataSetConnector extends OrganizationStructureDataSetConnectorBase
{
    private static final String myConnectorName = DepartmentsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public DepartmentsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.DEPARTMENTS);
    }


    /**
     * One row per department.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        OrganizationStructure organizationStructure = getOrganizationStructure();

        for (OpenMetadataRootElement department : organizationStructure.getDepartments())
        {
            String guid = department.getElementHeader().getGUID();

            Map<String, String> values = newValues();

            if (department.getProperties() instanceof TeamProperties teamProperties)
            {
                putValue(values, ProductDataFieldDefinition.TEAM_TYPE, teamProperties.getTeamType());
            }

            putValue(values, ProductDataFieldDefinition.SUPER_TEAM_GUID, organizationStructure.getSuperTeamGUID(guid));

            OpenMetadataRootElement superTeam = organizationStructure.getSuperTeam(guid);

            if (superTeam != null)
            {
                putValue(values, ProductDataFieldDefinition.SUPER_TEAM_NAME, OrganizationStructure.getDisplayName(superTeam.getProperties()));
            }
            else
            {
                putValue(values, ProductDataFieldDefinition.SUPER_TEAM_NAME, null);
            }

            OpenMetadataRootElement organization = organizationStructure.getOrganization(guid);

            if (organization != null)
            {
                putValue(values, ProductDataFieldDefinition.ORGANIZATION_GUID, organization.getElementHeader().getGUID());
                putValue(values, ProductDataFieldDefinition.ORGANIZATION_NAME, OrganizationStructure.getDisplayName(organization.getProperties()));
            }
            else
            {
                putValue(values, ProductDataFieldDefinition.ORGANIZATION_GUID, null);
                putValue(values, ProductDataFieldDefinition.ORGANIZATION_NAME, null);
            }

            putValue(values, ProductDataFieldDefinition.SUB_TEAM_COUNT, organizationStructure.getSubTeamCount(guid));

            addRow(department, values);
        }
    }
}
