/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * Mapping methods to map between Retention and the omrs equivalents.
 */
public class RetentionMapper extends ClassificationMapper{
    private static final Logger log = LoggerFactory.getLogger( RetentionMapper.class);
    private static final String className = RetentionMapper.class.getName();
    private static final String typeName = "Retention";

    public RetentionMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    @Override
    protected Set<String> mapKnownAttributesToOmrs(Classification omasClassification, InstanceProperties omrsClassificationProperties) {
        Retention retention = (Retention) omasClassification;

        String stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(), "steward", omrsClassificationProperties, "");
        retention.setSteward(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(), "source", omrsClassificationProperties, "");
        retention.setSteward(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(), "notes", omrsClassificationProperties, "");
        retention.setNotes(stringValue);

        stringValue = repositoryHelper.getStringProperty(omrsapiHelper.getServiceName(), "associatedGUID", omrsClassificationProperties, "");
        retention.setAssociatedGUID(stringValue);

        Date dateValue = repositoryHelper.getDateProperty(omrsapiHelper.getServiceName(), "archiveAfter", omrsClassificationProperties, "");
        if (dateValue !=null) {
            retention.setArchiveAfter(dateValue.getTime());
        }
        dateValue = repositoryHelper.getDateProperty(omrsapiHelper.getServiceName(), "deleteAfter", omrsClassificationProperties, "");
        if (dateValue !=null) {
            retention.setDeleteAfter(dateValue.getTime());
        }
        Integer intValue = repositoryHelper.getIntProperty(omrsapiHelper.getServiceName(), "confidence", omrsClassificationProperties, "");
        retention.setConfidence(intValue);

        Map<String, InstancePropertyValue> instancePropertyMap = omrsClassificationProperties.getInstanceProperties();
        if (instancePropertyMap != null) {
            InstancePropertyValue instancePropertyValue = instancePropertyMap.get("status");
            if (instancePropertyValue != null) {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
                GovernanceClassificationStatus status = GovernanceClassificationStatus.valueOf(enumPropertyValue.getSymbolicName());
                retention.setStatus(status);
            }
            instancePropertyValue = instancePropertyMap.get("basis");
            if (instancePropertyValue != null) {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
                RetentionBasis basis = RetentionBasis.valueOf(enumPropertyValue.getSymbolicName());
                retention.setBasis(basis);
            }
        }

        return Retention.getPropertyNames();
    }

    @Override
    protected String getTypeName() {
        return typeName;
    }

    @Override
    protected Classification createOmasClassification() {
        return new Retention();
    }
    @Override
    protected InstanceProperties updateOMRSAttributes(Classification omasClassification) {
        InstanceProperties instanceProperties = new InstanceProperties();
        Retention retention = (Retention)omasClassification;
        if (retention.getSteward()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"steward",retention.getSteward(),"updateOMRSAttributes");
        }

        if (retention.getSource()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"source",retention.getSource(),"updateOMRSAttributes");
        }

        if (retention.getNotes()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"notes",retention.getNotes(),"updateOMRSAttributes");
        }

        if (retention.getAssociatedGUID()!=null) {
            repositoryHelper.addStringPropertyToInstance(omrsapiHelper.getServiceName(),instanceProperties,"associatedGUID",retention.getAssociatedGUID(),"updateOMRSAttributes");
        }

        if (retention.getArchiveAfter()!=null) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
            primitivePropertyValue.setPrimitiveValue(retention.getArchiveAfter());
            instanceProperties.setProperty("archiveAfter", primitivePropertyValue);
        }

        if (retention.getDeleteAfter()!=null) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
            primitivePropertyValue.setPrimitiveValue(retention.getDeleteAfter());
            instanceProperties.setProperty("deleteAfter", primitivePropertyValue);
        }

        if (retention.getConfidence()!=null) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
            primitivePropertyValue.setPrimitiveValue(retention.getConfidence());
            instanceProperties.setProperty("confidence", primitivePropertyValue);
        }
        if (retention.getBasis()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(retention.getBasis().getOrdinal());
            enumPropertyValue.setSymbolicName(retention.getBasis().getName());
            instanceProperties.setProperty("basis",enumPropertyValue);
        }
        if (retention.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(retention.getStatus().getOrdinal());
            enumPropertyValue.setSymbolicName(retention.getStatus().getName());
            instanceProperties.setProperty("status",enumPropertyValue);
        }

        return instanceProperties;
    }
}
