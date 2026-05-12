/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRootDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * OrganizationsTabularDataSetConnector enables interaction with the list of organizations stored in open metadata
 * as if it is a tabular data set.
 */
public class OrganizationsTabularDataSetConnector extends OpenMetadataRootDataSetConnectorBase
{
    private static final String myConnectorName = OrganizationsTabularDataSetConnector.class.getName();

    /**
     * Default constructor
     */
    public OrganizationsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.ORGANIZATIONS);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.ORGANIZATION.typeName);
    }
}