/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.CriticalityLevel;
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
 * Mapping methods to map between Criticality and the omrs equivalents.
 */
public class CriticalityMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( CriticalityMapper.class);
    private static final String className = CriticalityMapper.class.getName();
    private static final String typeName = "Criticality";

    public CriticalityMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        Criticality criticality = (Criticality)omasClassification;

        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"steward",omrsClassificationProperties,"");
        criticality.setSteward(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"source",omrsClassificationProperties,"");
        criticality.setSource(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(),"notes",omrsClassificationProperties,"");
        criticality.setNotes(stringValue);

        Integer intValue  = repositoryHelper.getIntProperty(omrsapiHelper.getServiceName(),"confidence",omrsClassificationProperties,"");
        criticality.setConfidence(intValue);

        Map<String, InstancePropertyValue> instancePropertyMap = omrsClassificationProperties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get("level");
        if (instancePropertyValue!=null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            CriticalityLevel level = CriticalityLevel.valueOf(enumPropertyValue.getSymbolicName());
            criticality.setLevel(level);
        }
        return Criticality.PROPERTY_NAMES_SET;
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }
    @Override
    protected Classification createOmasClassification() {
        return new Criticality();
    }

    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        Criticality criticality = (Criticality)omasClassification;
        if (criticality.getSteward()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"steward",criticality.getSteward(),"updateOMRSAttributes");
        }

        if (criticality.getSource()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"source",criticality.getSource(),"updateOMRSAttributes");
        }

        if (criticality.getNotes()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"notes",criticality.getNotes(),"updateOMRSAttributes");
        }

        if (criticality.getConfidence()!=null) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
            primitivePropertyValue.setPrimitiveValue(criticality.getConfidence());
            instanceProperties.setProperty("confidence", primitivePropertyValue);
        }
        if (criticality.getLevel()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(criticality.getLevel().getOrdinal());
            enumPropertyValue.setSymbolicName(criticality.getLevel().getName());
            instanceProperties.setProperty("level",enumPropertyValue);
        }

        return instanceProperties;
    }
}
