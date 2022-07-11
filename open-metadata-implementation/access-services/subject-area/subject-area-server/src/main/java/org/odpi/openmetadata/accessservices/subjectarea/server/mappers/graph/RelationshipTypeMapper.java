/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Static mapping methods to map between the omas relationship Type and omrs relationship type.
 */
public class RelationshipTypeMapper {
    private static final Logger log = LoggerFactory.getLogger( RelationshipTypeMapper.class);
    private static final String className = RelationshipTypeMapper.class.getName();

    /**
     * Map omas RelationshipType to omrs relationship type guid.
     *
     * The omasRelationshipType is the type of node that is exposed in the omasRelationshipType API. The subject Area OMAS needs to convert
     * the OMAS RelationshipType into a guid of an relationship type so it can be used to call omrs.
     * @param operation operation being attempted
     * @param omasRelationshipType omasRelationshipType this is the type of relationship that is exposed in the RelationshipType as part of the OMAS API.
     *
     * @return relationship Type guid.
     * @throws InvalidParameterException Invalid parameter error found
     */
    static public String mapOMASRelationshipTypeToOMRSRelationshipTypeGuid(String operation, RelationshipType omasRelationshipType) throws InvalidParameterException {
        String guid = null;
        if (omasRelationshipType == null) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_RELATIONSHIPTYPES_FOR_GRAPH.getMessageDefinition();

            String invalidPropertyName = "relationshipFilter";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                operation,
                                                invalidPropertyName,
                                                null);
        } else {
            String relationshipTypeName = omasRelationshipType.name();
            if (omasRelationshipType.equals(RelationshipType.HasA)) {
                relationshipTypeName = "TermHASARelationship";
            } else if (omasRelationshipType.equals(RelationshipType.IsA)) {
                relationshipTypeName = "ISARelationship";
            } else if (omasRelationshipType.equals(RelationshipType.IsATypeOfDeprecated)) {
                relationshipTypeName = "TermISATypeOFRelationship";
            } else if (omasRelationshipType.equals(RelationshipType.IsATypeOf)) {
                relationshipTypeName = "IsATypeOfRelationship";
            } else if (omasRelationshipType.equals(RelationshipType.TypedBy)) {
                relationshipTypeName = "TermTYPEDBYRelationship";
            }

            RelationshipDef relationshipDef = OpenMetadataTypesArchiveAccessor.getInstance().getRelationshipDefByName(relationshipTypeName);
            if (relationshipDef == null) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_RELATIONSHIPTYPES_FOR_GRAPH.getMessageDefinition();

                String invalidPropertyName = "relationshipFilter";
                messageDefinition.setMessageParameters(invalidPropertyName, relationshipTypeName);
                throw new InvalidParameterException(messageDefinition,
                                                    className,
                                                    operation,
                                                    invalidPropertyName,
                                                    relationshipTypeName);
            } else {
                guid = relationshipDef.getGUID();
            }
        }
        return guid;
    }
}
