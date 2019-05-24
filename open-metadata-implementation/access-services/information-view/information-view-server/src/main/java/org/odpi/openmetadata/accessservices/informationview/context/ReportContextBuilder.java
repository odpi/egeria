/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.DeployedReport;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.lookup.ReportLookup;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
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
import java.util.Date;
import java.util.List;

public class ReportContextBuilder {

    private OMRSRepositoryConnector enterpriseConnector;
    private OMEntityDao entityDao;
    private OMRSRepositoryHelper omrsRepositoryHelper;
    private ReportLookup reportLookup;

    public ReportContextBuilder(OMRSRepositoryConnector enterpriseConnector, OMEntityDao entityDao, OMRSAuditLog auditLog) {

        this.enterpriseConnector = enterpriseConnector;
        this.omrsRepositoryHelper = enterpriseConnector.getRepositoryHelper();
        this.entityDao = entityDao;
        this.reportLookup = new ReportLookup(enterpriseConnector, entityDao,null, auditLog);
    }


    public DeployedReport retrieveReport(String userId, String registrationGuid, String reportId) {

        ReportSource source = new ReportSource();
        source.setReportId(reportId);
        EntityDetail reportEntity;
        try {
            reportEntity = reportLookup.lookupEntity(source);
        } catch (UserNotAuthorizedException | FunctionNotSupportedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException e) {
            throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(),
                    ReportLookup.class.getName(),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage("source", source.toString()),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(),
                    InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(),
                    null);
        }
        return buildReport( reportEntity);
    }

    private DeployedReport buildReport( EntityDetail reportEntity) {
        DeployedReport report = new DeployedReport();
        report.setGuid(reportEntity.getGUID());
        report.setAuthor(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.AUTHOR, reportEntity.getProperties(), "retrieveReport"));
        report.setLastModifier(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.LAST_MODIFIER, reportEntity.getProperties(), "retrieveReport"));
        Date lastModifiedDate = enterpriseConnector.getRepositoryHelper().getDateProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.LAST_MODIFIED_TIME, reportEntity.getProperties(), "retrieveReport");
        if(lastModifiedDate!=null) {
            report.setLastModifiedTime(lastModifiedDate.getTime());
        }
        Date createdTime = enterpriseConnector.getRepositoryHelper().getDateProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.CREATE_TIME, reportEntity.getProperties(), "retrieveReport");
        if(createdTime!=null) {
            report.setCreatedTime(createdTime.getTime());
        }
        report.setId(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ID, reportEntity.getProperties(), "retrieveReport"));
        report.setReportName(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, reportEntity.getProperties(), "retrieveReport"));
        report.setRegistrationGuid(reportEntity.getMetadataCollectionId());
        report.setReportUrl(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, reportEntity.getProperties(), "retrieveReport"));
        addReportStructure(report, reportEntity);

        return report;
    }

    private void addReportStructure(DeployedReport report, EntityDetail reportEntity) {
        String reportGuid = reportEntity.getGUID();
        List<Relationship> relationships;
        try {
            relationships = entityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportGuid);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                    OMEntityDao.class.getName(),
                    auditCode.getFormattedErrorMessage(Constants.ASSET_SCHEMA_TYPE, reportGuid, e.getMessage()),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        if(relationships == null || relationships.isEmpty())
            return;
        EntityProxy assetSchemaType = relationships.get(0).getEntityTwoProxy();

        report.setReportElements(getInnerElements( assetSchemaType.getGUID()));

    }

    private List<ReportElement> getInnerElements(String guid) {
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

    private ReportElement buildElement(EntityDetail entityDetail) {
        ReportElement reportElement;
        if(entityDetail.getType().getTypeDefName().equals(Constants.DOCUMENT_SCHEMA_ATTRIBUTE)){
            reportElement = new ReportSection();
            reportElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, entityDetail.getProperties(), "buildElement"));
            try {
                List<Relationship> schemaType = entityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE,
                        entityDetail.getGUID());
                if(schemaType != null && !schemaType.isEmpty()) {
                    ((ReportSection) reportElement).setElements(getInnerElements(schemaType.get(0).getEntityTwoProxy().getGUID()));
                }
            } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
                InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
                throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                                                        OMEntityDao.class.getName(),
                                                        auditCode.getFormattedErrorMessage(Constants.ASSET_SCHEMA_TYPE, entityDetail.getGUID(), e.getMessage()),
                                                        auditCode.getSystemAction(),
                                                        auditCode.getUserAction(),
                                                        e);
            }

        }else{
            reportElement = new ReportColumn();
            reportElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, entityDetail.getProperties(), "buildElement"));
            ((ReportColumn) reportElement).setAggregation(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.AGGREGATING_FUNCTION, entityDetail.getProperties(), "buildElement"));
            ((ReportColumn) reportElement).setFormula(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.FORMULA, entityDetail.getProperties(), "buildElement"));
            ((ReportColumn) reportElement).setBusinessTerm(getAssignedBusinessTerm(entityDetail.getGUID()));
            ((ReportColumn) reportElement).setSources(getSources(entityDetail.getGUID()));

        }
        return reportElement;
    }

    private List<Source> getSources(String guid) {
        return null;
    }

    private BusinessTerm getAssignedBusinessTerm(String entityGuid) {
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


}
