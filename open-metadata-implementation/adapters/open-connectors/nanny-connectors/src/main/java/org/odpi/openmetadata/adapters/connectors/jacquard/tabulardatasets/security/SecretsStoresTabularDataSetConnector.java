/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRootDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * SecretsStoresTabularDataSetConnector supports the list of secrets stores known to open metadata as if it is a tabular data set.
 */
public class SecretsStoresTabularDataSetConnector extends OpenMetadataRootDataSetConnectorBase
{
    private static final String myConnectorName = SecretsStoresTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public SecretsStoresTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.SECRETS_STORES);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.KEY_STORE_FILE.typeName);
    }
}