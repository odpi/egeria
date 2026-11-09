/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * SecretsStoreLookup finds the secrets store (KeyStoreFile) that holds a secrets collection.  The secrets file
 * monitor links the two with a DataSetContent relationship.  The store is looked for at both ends, and by type,
 * because a secrets collection may also be linked to other data sets.
 */
class SecretsStoreLookup
{
    private SecretsStoreLookup()
    {
        // static methods only
    }


    /**
     * Return the unique identifier of the secrets store that holds the secrets collection.
     *
     * @param secretsCollection secrets collection retrieved with its DataSetContent relationships
     * @return guid or null
     */
    static String getSecretsStoreGUID(OpenMetadataRootElement secretsCollection)
    {
        List<RelatedMetadataElementSummary> candidates = new ArrayList<>();

        if (secretsCollection.getSupportedDataSets() != null)
        {
            candidates.addAll(secretsCollection.getSupportedDataSets());
        }
        if (secretsCollection.getDataSetContent() != null)
        {
            candidates.addAll(secretsCollection.getDataSetContent());
        }

        for (RelatedMetadataElementSummary candidate : candidates)
        {
            if ((candidate != null) && (candidate.getRelatedElement() != null))
            {
                ElementHeader header = candidate.getRelatedElement().getElementHeader();

                if (isKeyStoreFile(header))
                {
                    return header.getGUID();
                }
            }
        }

        return null;
    }


    /**
     * Is the element a secrets store file?
     *
     * @param header element header
     * @return boolean
     */
    private static boolean isKeyStoreFile(ElementHeader header)
    {
        if ((header == null) || (header.getType() == null))
        {
            return false;
        }

        final String keyStoreFile = OpenMetadataType.KEY_STORE_FILE.typeName;

        return keyStoreFile.equals(header.getType().getTypeName()) ||
               ((header.getType().getSuperTypeNames() != null) && header.getType().getSuperTypeNames().contains(keyStoreFile));
    }
}
