/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.Date;
import java.util.UUID;

public class RepositoryHelper {

    private OMRSRepositoryConnector enterpriseConnector;

    public RepositoryHelper(OMRSRepositoryConnector enterpriseConnector) {
        this.enterpriseConnector = enterpriseConnector;
    }

    /**
     * Returns an empty entity of the specified type with the given provenanceType and metadataCollectionId
     *
     * @param metadataCollectionId - id of the metadataCollection where it should be stored
     * @param provenanceType
     * @param userName - name of the user
     * @param typeName - name of the entity type
     * @return an empty entity of the specified type
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws TypeDefNotKnownException
     */
    public EntityDetail getSkeletonEntity(String metadataCollectionId,
                                          InstanceProvenanceType provenanceType,
                                          String userName,
                                          String typeName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException {
        EntityDetail entity = new EntityDetail();
        String guid = UUID.randomUUID().toString();

        entity.setInstanceProvenanceType(provenanceType);
        entity.setMetadataCollectionId(metadataCollectionId);
        entity.setCreateTime(new Date());
        entity.setGUID(guid);
        entity.setVersion(1L);

        TypeDef typeDef = enterpriseConnector.getMetadataCollection().getTypeDefByName(userName, typeName);
        InstanceType type = new InstanceType();
        type.setTypeDefGUID(typeDef.getGUID());
        entity.setType(type);
        entity.setStatus(InstanceStatus.ACTIVE);
        entity.setCreatedBy(userName);
        entity.setInstanceURL("");

        return entity;

    }

    /**
     *
     * @param metadataCollectionId
     * @param provenanceType
     * @param userName
     * @param typeName
     * @return
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws TypeDefNotKnownException
     */
    public Relationship getSkeletonRelationship(String metadataCollectionId,
                                                InstanceProvenanceType provenanceType,
                                                String userName,
                                                String typeName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException {

        Relationship relationship = new Relationship();
        String guid = UUID.randomUUID().toString();

        relationship.setInstanceProvenanceType(provenanceType);
        relationship.setMetadataCollectionId(metadataCollectionId);
        relationship.setCreateTime(new Date());
        relationship.setGUID(guid);
        relationship.setVersion(1L);

        TypeDef typeDef = enterpriseConnector.getMetadataCollection().getTypeDefByName(userName, typeName);
        InstanceType type = new InstanceType();
        type.setTypeDefGUID(typeDef.getGUID());
        relationship.setType(type);
        relationship.setStatus(InstanceStatus.ACTIVE);
        relationship.setCreatedBy(userName);
        relationship.setInstanceURL("");

        return relationship;
    }


}
