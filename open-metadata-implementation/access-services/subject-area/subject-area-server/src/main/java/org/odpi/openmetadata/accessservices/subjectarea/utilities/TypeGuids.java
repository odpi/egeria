/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SubjectArea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * This is a cache of Type Guids used by the Subject Area OMAS.
 */
public class TypeGuids
{
    private static String mediaReferenceTypeGuid = null;
    private static String  categoryAnchorTypeGuid = null;
    private static String termAnchorTypeGuid = null;
    private static String categoryHierarchyLinkTypeGuid = null;
    private static String termAnchorGuid = null;
    public static String getMediaReferenceTypeGuid() {
        if (mediaReferenceTypeGuid ==null)
        {
            RelationshipDef mediaReferenceType = OMRSArchiveAccessor.getInstance().getRelationshipDefByName("MediaReference");
            mediaReferenceTypeGuid = mediaReferenceType.getGUID();
        }
        return mediaReferenceTypeGuid;
    }

    public static String getCategoryAnchorTypeGuid()
    {
        if (categoryAnchorTypeGuid ==null)
        {
            RelationshipDef anchor = OMRSArchiveAccessor.getInstance().getRelationshipDefByName("CategoryAnchor");
            categoryAnchorTypeGuid = anchor.getGUID();
        }
        return categoryAnchorTypeGuid;
    }

    public static String getTermAnchorTypeGuid()
    {
        if (termAnchorTypeGuid ==null)
        {
            RelationshipDef anchor = OMRSArchiveAccessor.getInstance().getRelationshipDefByName("TermAnchor");
            termAnchorTypeGuid = anchor.getGUID();
        }
        return termAnchorTypeGuid;
    }

    public static String getCategoryHierarchyLinkTypeGuid()
    {

        if (categoryHierarchyLinkTypeGuid  ==null)
        {
            RelationshipDef anchor = OMRSArchiveAccessor.getInstance().getRelationshipDefByName("CategoryHierarchyLink");
            categoryHierarchyLinkTypeGuid = anchor.getGUID();
        }
        return categoryHierarchyLinkTypeGuid;
    }
}