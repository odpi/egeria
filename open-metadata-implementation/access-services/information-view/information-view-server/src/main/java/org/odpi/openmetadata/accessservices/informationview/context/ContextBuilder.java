/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        elementsRelationship = entityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, guid);

        return Optional.ofNullable(elementsRelationship).
                map(Collection::stream).
                orElseGet(Stream::empty).
                map(e -> buildElement(entityDao.getEntityByGuid(e.getEntityTwoProxy().getGUID()))).
                collect(Collectors.toList());
    }

    abstract T buildElement(EntityDetail entity);


    /**
     * Return the business term associated to the entity
     *
     * @param entityGuid unique identifier of the entity for which we want to retrieve the business term
     * @return bean describing the business term associated
     */
    protected List<BusinessTerm> getAssignedBusinessTerms(String entityGuid) {
        List<Relationship> semanticAssignments = entityDao.getRelationships(Constants.SEMANTIC_ASSIGNMENT, entityGuid);
        return Optional.ofNullable(semanticAssignments)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(r -> retrieveBusinessTerm(r.getEntityTwoProxy().getGUID()))
                .collect(Collectors.toList());
    }

    private BusinessTerm retrieveBusinessTerm(String businessTermGuid) {
        BusinessTerm businessTerm = new BusinessTerm();
        EntityDetail businessTermEntity = entityDao.getEntityByGuid(businessTermGuid);

        businessTerm.setGuid(businessTermGuid);
        businessTerm.setName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setSummary(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.SUMMARY, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setExamples(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.EXAMPLES, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setUsage(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.USAGE, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setQuery(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUERY, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setAbbreviation(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ABBREVIATION, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setDescription(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.DESCRIPTION, businessTermEntity.getProperties(), "retrieveReport"));
        businessTerm.setDisplayName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.DISPLAY_NAME, businessTermEntity.getProperties(), "retrieveReport"));
        return businessTerm;
    }


    /**
     * Return the list of asset schema type
     *
     * @param guid - unique identifier of the entity representing an asset
     * @return the list of asset schema type relationships linked to the entity
     */
    protected List<Relationship> getAssetSchemaTypeRelationships(String guid) {
        return entityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, guid);
    }


    protected List<Source> getSources(String guid) {
        List<Relationship> relationships = entityDao.getRelationships(Constants.SCHEMA_QUERY_IMPLEMENTATION, guid);
        List<EntityDetail> entities = Optional.ofNullable(relationships)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(r -> entityDao.getEntityByGuid(r.getEntityTwoProxy().getGUID()))
                .collect(Collectors.toList());

        List<Source> sources = Optional.ofNullable(entities)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(this::buildSource)
                .collect(Collectors.toList());

        return sources;
    }

    protected Source buildSource(EntityDetail entityDetail) {
        String methodName = "buildSource";
        if (omrsRepositoryHelper.isTypeOf(Constants.INFORMATION_VIEW_OMAS_NAME, entityDetail.getType().getTypeDefName(), Constants.RELATIONAL_COLUMN)) {
            Source columnSource = new DatabaseColumnSource();
            columnSource.setGuid(entityDetail.getGUID());
            columnSource.setQualifiedName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, entityDetail.getProperties(), methodName));
            return columnSource;
        }
        return null;
    }


}
