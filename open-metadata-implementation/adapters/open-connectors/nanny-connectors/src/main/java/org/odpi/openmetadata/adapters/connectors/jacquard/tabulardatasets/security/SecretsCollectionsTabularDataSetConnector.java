/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;


/**
 * SecretsCollectionsTabularDataSetConnector lists the secrets collections known to open metadata as if they are
 * a tabular data set.  There is one row per secrets collection.  The account counts come from the collection's
 * UserAccountProfile classification, and the secrets store is the KeyStoreFile linked to the collection through
 * the DataSetContent relationship.
 */
public class SecretsCollectionsTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = SecretsCollectionsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public SecretsCollectionsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.SECRETS_COLLECTIONS);
    }


    /**
     * One row per secrets collection.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement secretsCollection : getAllElements(OpenMetadataType.SECRETS_COLLECTION.typeName,
                                                                        1,
                                                                        List.of(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName)))
        {
            Map<String, String> values = newValues();

            putValue(values, ProductDataFieldDefinition.SECRETS_STORE_GUID, SecretsStoreLookup.getSecretsStoreGUID(secretsCollection));

            addRow(secretsCollection, values);
        }
    }
}
