/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRootDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * LocationsTabularDataSetConnector enables interaction with the list of locations stored in open metadata
 * as if it is a tabular data set.
 */
public class GovernanceControlsTabularDataSetConnector extends OpenMetadataRootDataSetConnectorBase
{
    private static final String myConnectorName = GovernanceControlsTabularDataSetConnector.class.getName();

    /**
     * Default constructor
     */
    public GovernanceControlsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.GOVERNANCE_CONTROLS);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.GOVERNANCE_CONTROL.typeName);
    }
}