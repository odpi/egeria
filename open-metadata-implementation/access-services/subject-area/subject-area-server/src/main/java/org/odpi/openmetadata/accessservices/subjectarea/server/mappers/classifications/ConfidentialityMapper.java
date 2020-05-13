/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;


/**
 * Mapping methods to map between Confidentiality and the omrs equivalents.
 */
public class ConfidentialityMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( ConfidentialityMapper.class);
    private static final String className = ConfidentialityMapper.class.getName();
    private static final String typeName = "Confidentiality";

    public ConfidentialityMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        Confidentiality confidentiality = (Confidentiality)omasClassification;

        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"steward",omrsClassificationProperties,"");
        confidentiality.setSteward(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"notes",omrsClassificationProperties,"");
        confidentiality.setNotes(stringValue);

        int intValue  = repositoryHelper.getIntProperty(omrsapiHelper.getServiceName(),"confidence",omrsClassificationProperties,"");
        confidentiality.setConfidence(intValue);
        intValue  = repositoryHelper.getIntProperty(omrsapiHelper.getServiceName(),"level",omrsClassificationProperties,"");
        confidentiality.setLevel(intValue);

        // map enums
        Map<String, InstancePropertyValue> instancePropertyMap = omrsClassificationProperties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get("status");
        if (instancePropertyValue!=null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            GovernanceClassificationStatus status = GovernanceClassificationStatus.valueOf(enumPropertyValue.getSymbolicName());
            confidentiality.setStatus(status);
        }

        return Confidentiality.getPropertyNames();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected Classification createOmasClassification() {
        return new Confidentiality();
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        Confidentiality confidentiality = (Confidentiality)omasClassification;
        if (confidentiality.getSteward()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"steward",confidentiality.getSteward(),"updateOMRSAttributes");
        }

        if (confidentiality.getSource()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"source",confidentiality.getSource(),"updateOMRSAttributes");
        }

        if (confidentiality.getNotes()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"notes",confidentiality.getNotes(),"updateOMRSAttributes");
        }

        if (confidentiality.getConfidence()!=null) {
            repositoryHelper.addIntPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"confidence",confidentiality.getConfidence(),"updateOMRSAttributes");
        }
        if (confidentiality.getLevel()!=null) {
            repositoryHelper.addIntPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"level",confidentiality.getLevel(),"updateOMRSAttributes");
        }
        if (confidentiality.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(confidentiality.getStatus().getOrdinal());
            enumPropertyValue.setSymbolicName(confidentiality.getStatus().getName());
            instanceProperties.setProperty("status",enumPropertyValue);
        }

        return instanceProperties;
    }
}
