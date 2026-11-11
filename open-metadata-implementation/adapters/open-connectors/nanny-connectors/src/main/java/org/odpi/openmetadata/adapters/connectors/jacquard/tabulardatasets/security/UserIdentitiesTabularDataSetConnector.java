/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;


/**
 * UserIdentitiesTabularDataSetConnector lists the user identities known to open metadata as if they are a tabular
 * data set.  There is one row per user identity.  The actor profile comes from the ProfileIdentity relationship,
 * the security groups and roles from the SecurityListMembership classification, and the secrets collections
 * from the UserAccount relationships.
 */
public class UserIdentitiesTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = UserIdentitiesTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public UserIdentitiesTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.USER_IDENTITIES);
    }


    /**
     * One row per user identity.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement userIdentity : getAllElements(OpenMetadataType.USER_IDENTITY.typeName,
                                                                   1,
                                                                   List.of(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                           OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName)))
        {
            Map<String, String> values = newValues();

            String profileGUID = null;
            String profileName = null;

            RelatedMetadataElementSummary profile = userIdentity.getUserProfile();

            if ((profile != null) && (profile.getRelatedElement() != null))
            {
                profileGUID = profile.getRelatedElement().getElementHeader().getGUID();

                if (profile.getRelatedElement().getProperties() instanceof ReferenceableProperties profileProperties)
                {
                    profileName = profileProperties.getDisplayName();
                }
            }

            putValue(values, ProductDataFieldDefinition.PROFILE_GUID, profileGUID);
            putValue(values, ProductDataFieldDefinition.PROFILE_NAME, profileName);
            putValue(values, ProductDataFieldDefinition.SECRETS_COLLECTION_NAME, getSecretsCollectionNames(userIdentity));

            addRow(userIdentity, values);
        }
    }


    /**
     * Return the names of the secrets collections that the user identity has an account in.  A user may
     * have accounts in several collections, so the names are sorted and comma-separated.
     *
     * @param userIdentity user identity element
     * @return names or null
     */
    static String getSecretsCollectionNames(OpenMetadataRootElement userIdentity)
    {
        if (userIdentity.getConfiguredInSecurityCollections() == null)
        {
            return null;
        }

        TreeSet<String> names = new TreeSet<>();

        for (RelatedMetadataElementSummary collection : userIdentity.getConfiguredInSecurityCollections())
        {
            if ((collection != null) && (collection.getRelatedElement() != null))
            {
                MetadataElementSummary collectionElement = collection.getRelatedElement();
                String                 name              = null;

                if ((collectionElement.getProperties() instanceof AssetProperties assetProperties) && (assetProperties.getResourceName() != null))
                {
                    name = assetProperties.getResourceName();
                }
                else if (collectionElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
                {
                    name = referenceableProperties.getDisplayName();
                }

                /*
                 * A TreeSet rejects null (even in remove), so a collection without a name is skipped here.
                 */
                if (name != null)
                {
                    names.add(name);
                }
            }
        }

        if (names.isEmpty())
        {
            return null;
        }

        return String.join(",", names);
    }
}
