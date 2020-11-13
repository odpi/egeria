/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Mapping methods to map between Confidence and the omrs equivalents.
 */
public class ConfidenceMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( ConfidenceMapper.class);
    private static final String className = ConfidenceMapper.class.getName();
    private static final String typeName = "Confidence";

    public ConfidenceMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        Confidence confidence = (Confidence)omasClassification;

        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"steward",omrsClassificationProperties,"");
        confidence.setSteward(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"notes",omrsClassificationProperties,"");
        confidence.setNotes(stringValue);

        Integer intValue  = repositoryHelper.getIntProperty(omrsapiHelper.getServiceName(),"confidence",omrsClassificationProperties,"");
        confidence.setConfidence(intValue);

        // map enums
        Map<String, InstancePropertyValue> instancePropertyMap = omrsClassificationProperties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get("level");
        if (instancePropertyValue!=null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            ConfidenceLevel level = ConfidenceLevel.valueOf(enumPropertyValue.getSymbolicName());
            confidence.setLevel(level);
        }

        instancePropertyValue = instancePropertyMap.get("status");
        if (instancePropertyValue!=null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            GovernanceClassificationStatus status = GovernanceClassificationStatus.valueOf(enumPropertyValue.getSymbolicName());
            confidence.setStatus(status);
        }
        return Confidence.getPropertyNames();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected Classification createOmasClassification() {
        return new Confidence();
    }

    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        Confidence confidence = (Confidence)omasClassification;
        if (confidence.getSteward()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"steward",confidence.getSteward(),"updateOMRSAttributes");
        }

        if (confidence.getSource()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"source",confidence.getSource(),"updateOMRSAttributes");
        }

        if (confidence.getNotes()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"notes",confidence.getNotes(),"updateOMRSAttributes");
        }

        if (confidence.getConfidence()!=null) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
            primitivePropertyValue.setPrimitiveValue(confidence.getConfidence());
            instanceProperties.setProperty("confidence", primitivePropertyValue);
        }
        if (confidence.getLevel()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(confidence.getLevel().getOrdinal());
            enumPropertyValue.setSymbolicName(confidence.getLevel().getName());
            instanceProperties.setProperty("level",enumPropertyValue);
        }
        if (confidence.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(confidence.getStatus().getOrdinal());
            enumPropertyValue.setSymbolicName(confidence.getStatus().getName());
            instanceProperties.setProperty("status",enumPropertyValue);
        }

        return instanceProperties;
    }
}
