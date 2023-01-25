/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.handler;

import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SEMANTIC_ASSIGNMENT_GUID;

public class SecurityOfficerHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerHandler.class);
    private OMRSMetadataCollection metadataCollection;
    private OMRSRepositoryHelper repositoryHelper;
    private String sourceName;

    /**
     * Construct the connection handler with a link to the property handlers's connector and this access service's
     * official name.
     *
     * @param repositoryConnector - connector to the property handlers.
     * @param sourceName source name
     * @throws PropertyServerException - there is a problem retrieving information from the metadata server
     */
    public SecurityOfficerHandler(OMRSRepositoryConnector repositoryConnector, String sourceName) throws PropertyServerException {
        final String methodName = "SecurityOfficerHandler";

        this.sourceName = sourceName;
        if (repositoryConnector != null) {
            try {
                this.metadataCollection = repositoryConnector.getMetadataCollection();
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            } catch (RepositoryErrorException e) {
                throw new PropertyServerException(SecurityOfficerErrorCode.NO_METADATA_COLLECTION.getMessageDefinition(repositoryConnector.getRepositoryName()),
                                                  this.getClass().getName(), methodName, e);
            }
        }
    }

    public EntityDetail getEntityDetailById(String userId, String assetGuid) {

        try {
            return metadataCollection.getEntityDetail(userId, assetGuid);
        } catch (InvalidParameterException | RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | EntityNotKnownException e) {
            log.debug("Unable to retrieve the entity with guid = {}", assetGuid);
        }

        return null;
    }

    public List<EntityDetail> getSchemaElementsAssignedToBusinessTerms(String userId, String glossaryTermGUID) {

        List<Relationship> relationships = getSemanticAssigmentRelationships(userId, glossaryTermGUID);

        List<EntityDetail> schemaElements = new ArrayList<>();
        if (relationships != null) {
            for (Relationship relationship : relationships) {
                EntityDetail schemaElement = getSchemaElement(userId, glossaryTermGUID, relationship);
                if (schemaElement != null) {
                    schemaElements.add(schemaElement);
                }
            }
        }

        return schemaElements;
    }

    private List<Relationship> getSemanticAssigmentRelationships(String userId, String glossaryTermGUID) {

        try {
            return metadataCollection.getRelationshipsForEntity(userId,
                    glossaryTermGUID,
                    SEMANTIC_ASSIGNMENT_GUID,
                    0,
                    Collections.singletonList(InstanceStatus.ACTIVE),
                    null,
                    null,
                    SequencingOrder.ANY,
                    0);
        } catch (InvalidParameterException | PagingErrorException | FunctionNotSupportedException | EntityNotKnownException | PropertyErrorException | TypeErrorException | UserNotAuthorizedException | RepositoryErrorException e) {
            log.debug("Unable to fetch semantic assignments for {}", glossaryTermGUID);
        }

        return Collections.emptyList();
    }

    private EntityDetail getSchemaElement(String userId, String glossaryTermGUID, Relationship relationship) {
        try {
            if (relationship.getEntityOneProxy().getGUID().equals(glossaryTermGUID)) {
                if (isSchemaElement(relationship.getEntityTwoProxy().getType())) {
                    return metadataCollection.getEntityDetail(userId, relationship.getEntityTwoProxy().getGUID());
                }
            } else {
                if (isSchemaElement(relationship.getEntityOneProxy().getType())) {
                    return metadataCollection.getEntityDetail(userId, relationship.getEntityOneProxy().getGUID());
                }
            }
        } catch (TypeDefNotKnownException| EntityProxyOnlyException | RepositoryErrorException | InvalidParameterException | UserNotAuthorizedException | EntityNotKnownException e) {
            log.debug("Unable to get schema element entity");
        }

        return null;
    }

    private boolean isSchemaElement(InstanceType instanceType) throws UserNotAuthorizedException, RepositoryErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, TypeDefNotKnownException {
        List<TypeDefLink> typeDefSuperTypes = repositoryHelper.getSuperTypes(sourceName, instanceType.getTypeDefName());
        if (typeDefSuperTypes.stream().anyMatch(typeDefLink -> typeDefLink.getName().equals(SCHEMA_ATTRIBUTE))) {
            return true;
        }

        for (TypeDefLink typeDefLink : typeDefSuperTypes) {
            if(metadataCollection.getTypeDefByName(SECURITY_OFFICER, typeDefLink.getName()).getSuperType().getName().equals(SCHEMA_ATTRIBUTE)) {
                return true;
            }
        }
        return false;
    }

}
