/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DeployedReport;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumn;
import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSection;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.lookup.ReportLookup;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.throwRetrieveRelationshipException;

public class ReportContextBuilder extends ContextBuilder{

    private ReportLookup reportLookup;

    public ReportContextBuilder(OMRSRepositoryConnector enterpriseConnector, OMEntityDao entityDao, OMRSAuditLog auditLog) {
        super(enterpriseConnector, entityDao, auditLog);
        this.reportLookup = new ReportLookup(enterpriseConnector, entityDao,null, auditLog);
    }

    /**
     *
     * @param reportId id of the report to retrieve
     * @return the bean representing the report
     */
    public DeployedReport retrieveReport(String reportId) {

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

    /**
     *
     * @param reportEntity - entity describing the top level properties of the report
     * @return the bean representing the report
     */
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
        report.setReportUrl(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, reportEntity.getProperties(), "retrieveReport"));
        addReportStructure(report, reportEntity);

        return report;
    }

    /**
     *
     *
     * @param report - bean describing the report
     * @param reportEntity - entity describing the top level properties of the report
     */
    private void addReportStructure(DeployedReport report, EntityDetail reportEntity) {
        List<Relationship> relationships = getAssetSchemaTypeRelationships(reportEntity.getGUID());

        if(relationships == null || relationships.isEmpty())
           return;
        EntityProxy assetSchemaType = relationships.get(0).getEntityTwoProxy();
        report.setReportElements(getChildrenElements(assetSchemaType.getGUID()));
    }


    /**
     *
     * @param entityDetail entity describing a report element
     * @return
     */
    protected ReportElement buildElement(EntityDetail entityDetail) {

        if(entityDetail.getType().getTypeDefName().equals(Constants.DOCUMENT_SCHEMA_ATTRIBUTE)){
            ReportSection reportElement = new ReportSection();
            reportElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, entityDetail.getProperties(), "buildElement"));
            try {
                List<Relationship> schemaType = entityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE,
                        entityDetail.getGUID());
                if(schemaType != null && !schemaType.isEmpty()) {
                    reportElement.setElements(getChildrenElements(schemaType.get(0).getEntityTwoProxy().getGUID()));
                }
            } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
                throwRetrieveRelationshipException(entityDetail.getGUID(), Constants.ASSET_SCHEMA_TYPE, e, ReportContextBuilder.class.getName());
            }
            return reportElement;
        }else{
            ReportColumn reportElement = new ReportColumn();
            reportElement.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, entityDetail.getProperties(), "buildElement"));
            reportElement.setAggregation(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.AGGREGATING_FUNCTION, entityDetail.getProperties(), "buildElement"));
            reportElement.setFormula(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.FORMULA, entityDetail.getProperties(), "buildElement"));
            reportElement.setBusinessTerms(getAssignedBusinessTerms(entityDetail.getGUID()));
            reportElement.setSources(getSources(entityDetail.getGUID()));
            return reportElement;
        }
    }

    private List<Source> getSources(String guid) {
        return null;
    }


}
