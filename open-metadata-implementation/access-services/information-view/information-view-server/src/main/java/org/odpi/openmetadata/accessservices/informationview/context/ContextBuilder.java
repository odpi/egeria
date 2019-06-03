/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.lookup.ReportLookup;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ContextBuilder<T> {

    protected OMRSRepositoryConnector enterpriseConnector;
    protected OMEntityDao entityDao;
    protected OMRSAuditLog auditLog;
    protected OMRSRepositoryHelper omrsRepositoryHelper;


    public ContextBuilder(OMRSRepositoryConnector enterpriseConnector, OMEntityDao entityDao, OMRSAuditLog omrsAuditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.entityDao = entityDao;
        this.omrsRepositoryHelper = enterpriseConnector.getRepositoryHelper();
    }

    /**
     * Return the children elements linked with relationship ATTRIBUTE_FOR_SCHEMA to the entity with unique identifier guid
     *
     * @param guid - unique identifier for which we want to retrieve the children elements
     * @return the list of children elements
     */
    protected List<T> getChildrenElements(String guid) {
        List<Relationship> elementsRelationship;
        try {
            elementsRelationship = entityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, guid);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                    OMEntityDao.class.getName(),
                    auditCode.getFormattedErrorMessage(Constants.ATTRIBUTE_FOR_SCHEMA, guid, e.getMessage()),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        if(elementsRelationship == null || elementsRelationship.isEmpty())
            return Collections.emptyList();
        List elements = new ArrayList();
        for (Relationship element: elementsRelationship){
            try {
                EntityDetail entity = entityDao.getEntityByGuid(element.getEntityTwoProxy().getGUID());
                elements.add(buildElement(entity));

            } catch (RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | InvalidParameterException | EntityNotKnownException e) {
                throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(),
                        ReportLookup.class.getName(),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage(Constants.GUID, element.getGUID()),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(),
                        null);
            }
        }
        return elements;
    }

    abstract T buildElement(EntityDetail entity);


    /**
     * Return the business term associated to the entoty
     * @param entityGuid unique identifier of the entity for which we want to retrieve the business term
     * @return bean describing the business term associated
     */
    protected BusinessTerm getAssignedBusinessTerm(String entityGuid) {
        BusinessTerm businessTerm = null;
        List<Relationship> semanticAssignments ;
        try {
            semanticAssignments = entityDao.getRelationships(Constants.SEMANTIC_ASSIGNMENT, entityGuid);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                    OMEntityDao.class.getName(),
                    auditCode.getFormattedErrorMessage(Constants.SEMANTIC_ASSIGNMENT, entityGuid, e.getMessage()),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        if(semanticAssignments != null && !semanticAssignments.isEmpty()){
            businessTerm = new BusinessTerm();
            String businessTermGuid = semanticAssignments.get(0).getEntityTwoProxy().getGUID();
            EntityDetail businessTermEntity;
            try {
                businessTermEntity = entityDao.getEntityByGuid(businessTermGuid);
            } catch (RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | InvalidParameterException | EntityNotKnownException e) {
                throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(),
                        ReportLookup.class.getName(),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage(Constants.GUID, businessTermGuid),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(),
                        InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(),
                        null);
            }
            businessTerm.setGuid(businessTermGuid);
            businessTerm.setName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setSummary(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.SUMMARY, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setExamples(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.EXAMPLES, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setUsage(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.USAGE, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setQuery(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUERY, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setAbbreviation(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ABBREVIATION, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setDescription(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.DESCRIPTION, businessTermEntity.getProperties(), "retrieveReport"));
            businessTerm.setDisplayName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.DISPLAY_NAME, businessTermEntity.getProperties(), "retrieveReport"));

        }
        return businessTerm;
    }


    /**
     * Return the list of asset schema type
     * @param guid - unique identifier of the entity representing an asset
     * @return the list of asset schema type relationships linked to the entity
     */
    protected List<Relationship> getAssetSchemaTypeRelationships(String guid) {
        List<Relationship> relationships;
        try {
            relationships = entityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, guid);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                    OMEntityDao.class.getName(),
                    auditCode.getFormattedErrorMessage(Constants.ASSET_SCHEMA_TYPE, guid, e.getMessage()),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        return relationships;
    }


}
