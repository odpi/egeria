/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * UserAccountsTabularDataSetConnector lists the user accounts defined in the secrets collections known to open
 * metadata as if they are a tabular data set.  There is one row per account in each collection: the same user
 * may have an account in several collections.
 * <br><br>
 * The accounts are the ones the OMAG Server Platform cataloguer records: a UserAccount relationship from the
 * secrets collection to the user identity, with the account's security groups and roles in the user identity's
 * SecurityListMembership classification.  The secrets stores themselves are not read, so no secret can reach a row.
 */
public class UserAccountsTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = UserAccountsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public UserAccountsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.USER_ACCOUNTS);
    }


    /**
     * One row per user account in each secrets collection.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement userIdentity : getAllElements(OpenMetadataType.USER_IDENTITY.typeName,
                                                                   1,
                                                                   List.of(OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName)))
        {
            if (userIdentity.getConfiguredInSecurityCollections() != null)
            {
                /*
                 * One row per collection, even if the relationship has been recorded more than once.
                 */
                Set<String> collectionGUIDs = new HashSet<>();

                for (RelatedMetadataElementSummary collection : userIdentity.getConfiguredInSecurityCollections())
                {
                    if ((collection != null) && (collection.getRelatedElement() != null))
                    {
                        MetadataElementSummary collectionElement = collection.getRelatedElement();
                        String                 collectionGUID    = collectionElement.getElementHeader().getGUID();

                        if (collectionGUIDs.add(collectionGUID))
                        {
                            Map<String, String> values = newValues();

                            putValue(values, ProductDataFieldDefinition.SECRETS_COLLECTION_GUID, collectionGUID);
                            putValue(values, ProductDataFieldDefinition.SECRETS_COLLECTION_NAME, getCollectionName(collectionElement));

                            addRow(userIdentity, values);
                        }
                    }
                }
            }
        }
    }


    /**
     * Return the name of a secrets collection: its resource name, which is how the secrets store knows it, or
     * its display name if it has none.
     *
     * @param collectionElement secrets collection
     * @return name or null
     */
    private String getCollectionName(MetadataElementSummary collectionElement)
    {
        if ((collectionElement.getProperties() instanceof AssetProperties assetProperties) && (assetProperties.getResourceName() != null))
        {
            return assetProperties.getResourceName();
        }
        else if (collectionElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            return referenceableProperties.getDisplayName();
        }

        return null;
    }
}
