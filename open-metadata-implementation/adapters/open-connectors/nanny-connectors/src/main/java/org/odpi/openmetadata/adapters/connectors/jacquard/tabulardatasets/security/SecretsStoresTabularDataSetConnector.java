/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;


/**
 * SecretsStoresTabularDataSetConnector lists the secrets stores known to open metadata as if they are a tabular
 * data set.  There is one row per secrets store file (KeyStoreFile).  The network address is the file's path name.
 * Only the description of the store is published - never its content.
 */
public class SecretsStoresTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
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
     * One row per secrets store.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement secretsStore : getAllElements(OpenMetadataType.KEY_STORE_FILE.typeName, 0))
        {
            Map<String, String> values = newValues();

            if (secretsStore.getProperties() instanceof DataStoreProperties dataStoreProperties)
            {
                putValue(values, ProductDataFieldDefinition.NETWORK_ADDRESS, dataStoreProperties.getPathName());
            }

            addRow(secretsStore, values);
        }
    }
}
