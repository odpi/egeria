/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;


/**
 * This is a cache of Type Guids used by the Subject Area OMAS.
 */
public class TypeGuids
{
    private static String mediaReferenceTypeGuid = null;
    private static String  categoryAnchorTypeGuid = null;
    private static String termAnchorTypeGuid = null;
    private static String categoryHierarchyLinkTypeGuid = null;
    public static String getMediaReferenceTypeGuid() {
        if (mediaReferenceTypeGuid ==null)
        {
            RelationshipDef mediaReferenceType = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName("MediaReference");
            mediaReferenceTypeGuid = mediaReferenceType.getGUID();
        }
        return mediaReferenceTypeGuid;
    }

    public static String getCategoryAnchorTypeGuid()
    {
        if (categoryAnchorTypeGuid ==null)
        {
            RelationshipDef anchor = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName("CategoryAnchor");
            categoryAnchorTypeGuid = anchor.getGUID();
        }
        return categoryAnchorTypeGuid;
    }

    public static String getTermAnchorTypeGuid()
    {
        if (termAnchorTypeGuid ==null)
        {
            RelationshipDef anchor = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName("TermAnchor");
            termAnchorTypeGuid = anchor.getGUID();
        }
        return termAnchorTypeGuid;
    }

    public static String getCategoryHierarchyLinkTypeGuid()
    {

        if (categoryHierarchyLinkTypeGuid  ==null)
        {
            RelationshipDef anchor = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName("CategoryHierarchyLink");
            categoryHierarchyLinkTypeGuid = anchor.getGUID();
        }
        return categoryHierarchyLinkTypeGuid;
    }
}