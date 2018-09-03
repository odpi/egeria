/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;


import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservice.assetcatalog.util.ExceptionUtil;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.Arrays;
import java.util.List;

/**
 * The AssetCatalogRelationshipService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset relationships and details about specific relationships.
 */
public class AssetCatalogRelationshipService {

    private static OMRSMetadataCollection metadataCollection;

    private Converter converter = new Converter();
    private ExceptionUtil exceptionUtil = new ExceptionUtil();

    public AssetCatalogRelationshipService() {
        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_CATALOG_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetCatalogAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param localRepositoryConnector - link to the local repository responsible for servicing the REST calls.
     *                                 If localRepositoryConnector is null when a REST calls is received, the request
     *                                 is rejected.
     */
    public static void setRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector) {
        try {
            AssetCatalogRelationshipService.metadataCollection = localRepositoryConnector.getMetadataCollection();
        } catch (Throwable error) {
        }
    }

    public RelationshipsResponse getRelationshipById(String userId, String relationshipId) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            Relationship relationship = metadataCollection.getRelationship(userId, relationshipId);
            response.setRelationships(Arrays.asList(converter.toRelationship(relationship)));
        } catch (RepositoryErrorException
                | RelationshipNotKnownException
                | UserNotAuthorizedException
                | InvalidParameterException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public RelationshipsResponse getRelationshipByProperty(
            String userId, String relationshipTypeGUID, String matchProperty,
            String propertyValue, Integer pageSize, Integer fromElement, SequenceOrderType orderType,
            String orderProperty, Status status) {

        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);
        SequencingOrder order = converter.getSequencingOrder(orderType);
        InstanceProperties matchProperties = converter.getMatchProperties(matchProperty, propertyValue);
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationshipsByProperty = metadataCollection.findRelationshipsByProperty(userId,
                    relationshipTypeGUID,
                    matchProperties,
                    MatchCriteria.ANY,
                    fromElement,
                    limitResultsByStatus,
                    null,
                    orderProperty,
                    order,
                    pageSize);

            if (relationshipsByProperty != null && !relationshipsByProperty.isEmpty()) {
                response.setRelationships(converter.toRelationships(relationshipsByProperty));
            }
        } catch (TypeErrorException
                | RepositoryErrorException
                | PropertyErrorException
                | PagingErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException
                | InvalidParameterException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }


    public RelationshipsResponse searchForRelationships(String userId, String relationshipTypeGUID,
                                                        String searchCriteria, Integer pageSize, Integer fromElement,
                                                        String orderProperty, SequenceOrderType orderType, Status status) {
        SequencingOrder order = converter.getSequencingOrder(orderType);
        List<InstanceStatus> statusList = converter.getInstanceStatuses(status);
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationshipsByPropertyValue =
                    metadataCollection.findRelationshipsByPropertyValue(userId,
                            relationshipTypeGUID,
                            searchCriteria,
                            fromElement,
                            statusList,
                            null,
                            orderProperty,
                            order,
                            pageSize);

            if (relationshipsByPropertyValue != null && !relationshipsByPropertyValue.isEmpty()) {
                response.setRelationships(converter.toRelationships(relationshipsByPropertyValue));
            }
        } catch (TypeErrorException
                | RepositoryErrorException
                | PropertyErrorException
                | PagingErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException
                | InvalidParameterException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }
}
