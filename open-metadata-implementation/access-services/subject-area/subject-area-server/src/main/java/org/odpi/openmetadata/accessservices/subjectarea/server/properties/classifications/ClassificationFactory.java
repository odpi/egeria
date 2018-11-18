/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.properties.classifications;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AbstractConcept.AbstractConceptMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ActivityDescription.ActivityDescriptionMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AnalyticsEngine.AnalyticsEngineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ApplicationServer.ApplicationServerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AuditLog.AuditLogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AuditLogFile.AuditLogFileMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Campaign.CampaignMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CanonicalVocabulary.CanonicalVocabularyMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ClassWord.ClassWordMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudPlatform.CloudPlatformMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudProvider.CloudProviderMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudService.CloudServiceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.ConfidenceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.ConfidentialityMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ContentManager.ContentManagerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ContextDefinition.ContextDefinitionMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ControlPoint.ControlPointMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.CriticalityMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CyberLocation.CyberLocationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataMovementEngine.DataMovementEngineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataStoreEncoding.DataStoreEncodingMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataValue.DataValueMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataVirtualizationEngine.DataVirtualizationEngineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DatabaseServer.DatabaseServerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.EnforcementPoint.EnforcementPointMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ExceptionBacklog.ExceptionBacklogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ExceptionLogFile.ExceptionLogFileMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.FileSystem.FileSystemMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.FixedLocation.FixedLocationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Folder.FolderMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GlossaryProject.GlossaryProjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceDaemon.GovernanceDaemonMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceMeasurementsResultsDataSet.GovernanceMeasurementsResultsDataSetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceProject.GovernanceProjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ListenerInterface.ListenerInterfaceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MetadataServer.MetadataServerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MeteringLog.MeteringLogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MobileAsset.MobileAssetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.NamingConventionRule.NamingConventionRuleMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.NotificationManager.NotificationManagerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ObjectIdentifier.ObjectIdentifierMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PrimaryKey.PrimaryKeyMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PrimeWord.PrimeWordMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PublisherInterface.PublisherInterfaceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RelationalView.RelationalViewMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ReportingEngine.ReportingEngineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RepositoryProxy.RepositoryProxyMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RequestResponseInterface.RequestResponseInterfaceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.RetentionMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SecureLocation.SecureLocationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Set.SetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineAttribute.SpineAttributeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineObject.SpineObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.StewardshipServer.StewardshipServerMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SubjectArea.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Task.TaskMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.TaxonomyMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.VerificationPoint.VerificationPointMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Webserver.WebserverMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.WorkflowEngine.WorkflowEngineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory to create new instances of classes of open metadata classifications by name. Return null if the classification is not known.
 */
public class ClassificationFactory {
    private static final Logger log = LoggerFactory.getLogger( ClassificationFactory.class);
    private static final String className =  ClassificationFactory.class.getName();
     public static Classification getClassification(String name, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification) throws InvalidParameterException {
        if (name.equals("Task")) {
            return TaskMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("DataValue")) {
            return DataValueMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("RequestResponseInterface")) {
            return RequestResponseInterfaceMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("SpineAttribute")) {
            return SpineAttributeMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("CanonicalVocabulary")) {
            return CanonicalVocabularyMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("AuditLog")) {
            return AuditLogMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("EnforcementPoint")) {
            return EnforcementPointMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Confidence")) {
            return ConfidenceMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("CloudProvider")) {
            return CloudProviderMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("FileSystem")) {
            return FileSystemMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("DatabaseServer")) {
            return DatabaseServerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Criticality")) {
            return CriticalityMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("DataStoreEncoding")) {
            return DataStoreEncodingMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("NotificationManager")) {
            return NotificationManagerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ExceptionLogFile")) {
            return ExceptionLogFileMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("MeteringLog")) {
            return MeteringLogMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ContentManager")) {
            return ContentManagerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("WorkflowEngine")) {
            return WorkflowEngineMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("MetadataServer")) {
            return MetadataServerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ContextDefinition")) {
            return ContextDefinitionMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("StewardshipServer")) {
            return StewardshipServerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("GlossaryProject")) {
            return GlossaryProjectMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Campaign")) {
            return CampaignMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("SpineObject")) {
            return SpineObjectMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("VerificationPoint")) {
            return VerificationPointMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("MobileAsset")) {
            return MobileAssetMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("GovernanceDaemon")) {
            return GovernanceDaemonMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("AnalyticsEngine")) {
            return AnalyticsEngineMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ExceptionBacklog")) {
            return ExceptionBacklogMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("DataVirtualizationEngine")) {
            return DataVirtualizationEngineMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ReportingEngine")) {
            return ReportingEngineMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Set")) {
            return SetMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ActivityDescription")) {
            return ActivityDescriptionMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Taxonomy")) {
            return TaxonomyMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("PrimeWord")) {
            return PrimeWordMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("PrimaryKey")) {
            return PrimaryKeyMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Webserver")) {
            return WebserverMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("PublisherInterface")) {
            return PublisherInterfaceMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Folder")) {
            return FolderMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("AbstractConcept")) {
            return AbstractConceptMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Retention")) {
            return RetentionMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("GovernanceProject")) {
            return GovernanceProjectMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("RelationalView")) {
            return RelationalViewMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("SubjectArea")) {
            return SubjectAreaMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("GovernanceMeasurementsResultsDataSet")) {
            return GovernanceMeasurementsResultsDataSetMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("SecureLocation")) {
            return SecureLocationMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ClassWord")) {
            return ClassWordMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("AuditLogFile")) {
            return AuditLogFileMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ObjectIdentifier")) {
            return ObjectIdentifierMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("CloudPlatform")) {
            return CloudPlatformMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("RepositoryProxy")) {
            return RepositoryProxyMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("CyberLocation")) {
            return CyberLocationMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("FixedLocation")) {
            return FixedLocationMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("CloudService")) {
            return CloudServiceMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ListenerInterface")) {
            return ListenerInterfaceMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("DataMovementEngine")) {
            return DataMovementEngineMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ControlPoint")) {
            return ControlPointMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("Confidentiality")) {
            return ConfidentialityMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("ApplicationServer")) {
            return ApplicationServerMapper.mapOmrsToOmas(omrsClassification);
        }
        if (name.equals("NamingConventionRule")) {
            return NamingConventionRuleMapper.mapOmrsToOmas(omrsClassification);
        }
        return null;
    }
}
