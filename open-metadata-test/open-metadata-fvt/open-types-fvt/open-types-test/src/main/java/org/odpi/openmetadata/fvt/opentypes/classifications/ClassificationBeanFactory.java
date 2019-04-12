/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

 // This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.fvt.opentypes.common.ClassificationBean;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

import org.odpi.openmetadata.fvt.opentypes.classifications.Task.TaskMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.DataValue.DataValueMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.RequestResponseInterface.RequestResponseInterfaceMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.SpineAttribute.SpineAttributeMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CanonicalVocabulary.CanonicalVocabularyMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.AuditLog.AuditLogMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.EnforcementPoint.EnforcementPointMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Confidence.ConfidenceMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CloudProvider.CloudProviderMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.FileSystem.FileSystemMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.DatabaseServer.DatabaseServerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Criticality.CriticalityMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.DataStoreEncoding.DataStoreEncodingMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.NotificationManager.NotificationManagerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ExceptionLogFile.ExceptionLogFileMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.MeteringLog.MeteringLogMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ContentManager.ContentManagerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.WorkflowEngine.WorkflowEngineMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.MetadataServer.MetadataServerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ContextDefinition.ContextDefinitionMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.StewardshipServer.StewardshipServerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.GlossaryProject.GlossaryProjectMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Campaign.CampaignMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.SpineObject.SpineObjectMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.VerificationPoint.VerificationPointMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.MobileAsset.MobileAssetMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.GovernanceDaemon.GovernanceDaemonMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.AnalyticsEngine.AnalyticsEngineMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ExceptionBacklog.ExceptionBacklogMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.DataVirtualizationEngine.DataVirtualizationEngineMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ReportingEngine.ReportingEngineMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Set.SetMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ActivityDescription.ActivityDescriptionMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Taxonomy.TaxonomyMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.PrimeWord.PrimeWordMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.PrimaryKey.PrimaryKeyMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Webserver.WebserverMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.PublisherInterface.PublisherInterfaceMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Folder.FolderMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.AbstractConcept.AbstractConceptMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Retention.RetentionMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.GovernanceProject.GovernanceProjectMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.RelationalView.RelationalViewMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.SubjectArea.SubjectAreaMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.GovernanceMeasurementsResultsDataSet.GovernanceMeasurementsResultsDataSetMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.SecureLocation.SecureLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ClassWord.ClassWordMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.AuditLogFile.AuditLogFileMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ObjectIdentifier.ObjectIdentifierMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CloudPlatform.CloudPlatformMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.RepositoryProxy.RepositoryProxyMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CyberLocation.CyberLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.FixedLocation.FixedLocationMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.DataMovementEngine.DataMovementEngineMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CloudTenant.CloudTenantMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.CloudService.CloudServiceMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ListenerInterface.ListenerInterfaceMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ControlPoint.ControlPointMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.Confidentiality.ConfidentialityMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.ApplicationServer.ApplicationServerMapper;
import org.odpi.openmetadata.fvt.opentypes.classifications.NamingConventionRule.NamingConventionRuleMapper;


/**
 * A factory to create new instances of classes of open metadata classifications by name. Return null if the classification is not known.
 */
public class ClassificationBeanFactory {
    private static final Logger log = LoggerFactory.getLogger( ClassificationBeanFactory.class);
    private static final String className =  ClassificationBeanFactory.class.getName();
    public static ClassificationBean getClassificationBean(String name, Classification omrsClassification) throws InvalidParameterException {
        if (name.equals("Task")) {
            return TaskMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("DataValue")) {
            return DataValueMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("RequestResponseInterface")) {
            return RequestResponseInterfaceMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("SpineAttribute")) {
            return SpineAttributeMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CanonicalVocabulary")) {
            return CanonicalVocabularyMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("AuditLog")) {
            return AuditLogMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("EnforcementPoint")) {
            return EnforcementPointMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Confidence")) {
            return ConfidenceMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CloudProvider")) {
            return CloudProviderMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("FileSystem")) {
            return FileSystemMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("DatabaseServer")) {
            return DatabaseServerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Criticality")) {
            return CriticalityMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("DataStoreEncoding")) {
            return DataStoreEncodingMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("NotificationManager")) {
            return NotificationManagerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ExceptionLogFile")) {
            return ExceptionLogFileMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("MeteringLog")) {
            return MeteringLogMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ContentManager")) {
            return ContentManagerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("WorkflowEngine")) {
            return WorkflowEngineMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("MetadataServer")) {
            return MetadataServerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ContextDefinition")) {
            return ContextDefinitionMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("StewardshipServer")) {
            return StewardshipServerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("GlossaryProject")) {
            return GlossaryProjectMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Campaign")) {
            return CampaignMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("SpineObject")) {
            return SpineObjectMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("VerificationPoint")) {
            return VerificationPointMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("MobileAsset")) {
            return MobileAssetMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("GovernanceDaemon")) {
            return GovernanceDaemonMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("AnalyticsEngine")) {
            return AnalyticsEngineMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ExceptionBacklog")) {
            return ExceptionBacklogMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("DataVirtualizationEngine")) {
            return DataVirtualizationEngineMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ReportingEngine")) {
            return ReportingEngineMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Set")) {
            return SetMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ActivityDescription")) {
            return ActivityDescriptionMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Taxonomy")) {
            return TaxonomyMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("PrimeWord")) {
            return PrimeWordMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("PrimaryKey")) {
            return PrimaryKeyMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Webserver")) {
            return WebserverMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("PublisherInterface")) {
            return PublisherInterfaceMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Folder")) {
            return FolderMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("AbstractConcept")) {
            return AbstractConceptMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Retention")) {
            return RetentionMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("GovernanceProject")) {
            return GovernanceProjectMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("RelationalView")) {
            return RelationalViewMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("SubjectArea")) {
            return SubjectAreaMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("GovernanceMeasurementsResultsDataSet")) {
            return GovernanceMeasurementsResultsDataSetMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("SecureLocation")) {
            return SecureLocationMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ClassWord")) {
            return ClassWordMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("AuditLogFile")) {
            return AuditLogFileMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ObjectIdentifier")) {
            return ObjectIdentifierMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CloudPlatform")) {
            return CloudPlatformMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("RepositoryProxy")) {
            return RepositoryProxyMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CyberLocation")) {
            return CyberLocationMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("FixedLocation")) {
            return FixedLocationMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("DataMovementEngine")) {
            return DataMovementEngineMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CloudTenant")) {
            return CloudTenantMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("CloudService")) {
            return CloudServiceMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ListenerInterface")) {
            return ListenerInterfaceMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ControlPoint")) {
            return ControlPointMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("Confidentiality")) {
            return ConfidentialityMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("ApplicationServer")) {
            return ApplicationServerMapper.mapOmrsToBean(omrsClassification);
        }
        if (name.equals("NamingConventionRule")) {
            return NamingConventionRuleMapper.mapOmrsToBean(omrsClassification);
        }
        return null;
    }
}
