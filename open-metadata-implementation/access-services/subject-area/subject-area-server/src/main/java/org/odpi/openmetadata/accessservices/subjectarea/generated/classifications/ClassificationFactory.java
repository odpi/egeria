/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generated.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Task.Task;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataValue.DataValue;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RequestResponseInterface.RequestResponseInterface;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineAttribute.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CanonicalVocabulary.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AuditLog.AuditLog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.EnforcementPoint.EnforcementPoint;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudProvider.CloudProvider;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.FileSystem.FileSystem;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DatabaseServer.DatabaseServer;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataStoreEncoding.DataStoreEncoding;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.NotificationManager.NotificationManager;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ExceptionLogFile.ExceptionLogFile;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MeteringLog.MeteringLog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ContentManager.ContentManager;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.WorkflowEngine.WorkflowEngine;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MetadataServer.MetadataServer;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ContextDefinition.ContextDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.StewardshipServer.StewardshipServer;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GlossaryProject.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Campaign.Campaign;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineObject.SpineObject;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.VerificationPoint.VerificationPoint;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.MobileAsset.MobileAsset;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceDaemon.GovernanceDaemon;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AnalyticsEngine.AnalyticsEngine;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ExceptionBacklog.ExceptionBacklog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataVirtualizationEngine.DataVirtualizationEngine;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ReportingEngine.ReportingEngine;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Set.Set;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ActivityDescription.ActivityDescription;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PrimeWord.PrimeWord;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PrimaryKey.PrimaryKey;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Webserver.Webserver;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.PublisherInterface.PublisherInterface;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Folder.Folder;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AbstractConcept.AbstractConcept;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceProject.GovernanceProject;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RelationalView.RelationalView;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SubjectArea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.GovernanceMeasurementsResultsDataSet.GovernanceMeasurementsResultsDataSet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SecureLocation.SecureLocation;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ClassWord.ClassWord;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.AuditLogFile.AuditLogFile;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ObjectIdentifier.ObjectIdentifier;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudPlatform.CloudPlatform;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.RepositoryProxy.RepositoryProxy;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CyberLocation.CyberLocation;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.FixedLocation.FixedLocation;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudService.CloudService;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ListenerInterface.ListenerInterface;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.DataMovementEngine.DataMovementEngine;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ControlPoint.ControlPoint;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ForeignKey.ForeignKey;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ApplicationServer.ApplicationServer;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.NamingConventionRule.NamingConventionRule;
/**
 * A factory to create new instances of classes of open metadata classifications by name. Return null if the classification is not known.
 */
public class ClassificationFactory {
    private static final Logger log = LoggerFactory.getLogger( ClassificationFactory.class);
    private static final String className =  ClassificationFactory.class.getName();
    public static Classification getClassification(String name ) {
        if (name.equals("Task")) {
            return new Task();
        }
        if (name.equals("DataValue")) {
            return new DataValue();
        }
        if (name.equals("RequestResponseInterface")) {
            return new RequestResponseInterface();
        }
        if (name.equals("SpineAttribute")) {
            return new SpineAttribute();
        }
        if (name.equals("CanonicalVocabulary")) {
            return new CanonicalVocabulary();
        }
        if (name.equals("AuditLog")) {
            return new AuditLog();
        }
        if (name.equals("EnforcementPoint")) {
            return new EnforcementPoint();
        }
        if (name.equals("Confidence")) {
            return new Confidence();
        }
        if (name.equals("CloudProvider")) {
            return new CloudProvider();
        }
        if (name.equals("FileSystem")) {
            return new FileSystem();
        }
        if (name.equals("DatabaseServer")) {
            return new DatabaseServer();
        }
        if (name.equals("Criticality")) {
            return new Criticality();
        }
        if (name.equals("DataStoreEncoding")) {
            return new DataStoreEncoding();
        }
        if (name.equals("NotificationManager")) {
            return new NotificationManager();
        }
        if (name.equals("ExceptionLogFile")) {
            return new ExceptionLogFile();
        }
        if (name.equals("MeteringLog")) {
            return new MeteringLog();
        }
        if (name.equals("ContentManager")) {
            return new ContentManager();
        }
        if (name.equals("WorkflowEngine")) {
            return new WorkflowEngine();
        }
        if (name.equals("MetadataServer")) {
            return new MetadataServer();
        }
        if (name.equals("ContextDefinition")) {
            return new ContextDefinition();
        }
        if (name.equals("StewardshipServer")) {
            return new StewardshipServer();
        }
        if (name.equals("GlossaryProject")) {
            return new GlossaryProject();
        }
        if (name.equals("Campaign")) {
            return new Campaign();
        }
        if (name.equals("SpineObject")) {
            return new SpineObject();
        }
        if (name.equals("VerificationPoint")) {
            return new VerificationPoint();
        }
        if (name.equals("MobileAsset")) {
            return new MobileAsset();
        }
        if (name.equals("GovernanceDaemon")) {
            return new GovernanceDaemon();
        }
        if (name.equals("AnalyticsEngine")) {
            return new AnalyticsEngine();
        }
        if (name.equals("ExceptionBacklog")) {
            return new ExceptionBacklog();
        }
        if (name.equals("DataVirtualizationEngine")) {
            return new DataVirtualizationEngine();
        }
        if (name.equals("ReportingEngine")) {
            return new ReportingEngine();
        }
        if (name.equals("Set")) {
            return new Set();
        }
        if (name.equals("ActivityDescription")) {
            return new ActivityDescription();
        }
        if (name.equals("Taxonomy")) {
            return new Taxonomy();
        }
        if (name.equals("PrimeWord")) {
            return new PrimeWord();
        }
        if (name.equals("PrimaryKey")) {
            return new PrimaryKey();
        }
        if (name.equals("Webserver")) {
            return new Webserver();
        }
        if (name.equals("PublisherInterface")) {
            return new PublisherInterface();
        }
        if (name.equals("Folder")) {
            return new Folder();
        }
        if (name.equals("AbstractConcept")) {
            return new AbstractConcept();
        }
        if (name.equals("Retention")) {
            return new Retention();
        }
        if (name.equals("GovernanceProject")) {
            return new GovernanceProject();
        }
        if (name.equals("RelationalView")) {
            return new RelationalView();
        }
        if (name.equals("SubjectArea")) {
            return new SubjectArea();
        }
        if (name.equals("GovernanceMeasurementsResultsDataSet")) {
            return new GovernanceMeasurementsResultsDataSet();
        }
        if (name.equals("SecureLocation")) {
            return new SecureLocation();
        }
        if (name.equals("ClassWord")) {
            return new ClassWord();
        }
        if (name.equals("AuditLogFile")) {
            return new AuditLogFile();
        }
        if (name.equals("ObjectIdentifier")) {
            return new ObjectIdentifier();
        }
        if (name.equals("CloudPlatform")) {
            return new CloudPlatform();
        }
        if (name.equals("RepositoryProxy")) {
            return new RepositoryProxy();
        }
        if (name.equals("CyberLocation")) {
            return new CyberLocation();
        }
        if (name.equals("FixedLocation")) {
            return new FixedLocation();
        }
        if (name.equals("CloudService")) {
            return new CloudService();
        }
        if (name.equals("ListenerInterface")) {
            return new ListenerInterface();
        }
        if (name.equals("DataMovementEngine")) {
            return new DataMovementEngine();
        }
        if (name.equals("ControlPoint")) {
            return new ControlPoint();
        }
        if (name.equals("ForeignKey")) {
            return new ForeignKey();
        }
        if (name.equals("Confidentiality")) {
            return new Confidentiality();
        }
        if (name.equals("ApplicationServer")) {
            return new ApplicationServer();
        }
        if (name.equals("NamingConventionRule")) {
            return new NamingConventionRule();
        }
        return null;
    }
}
