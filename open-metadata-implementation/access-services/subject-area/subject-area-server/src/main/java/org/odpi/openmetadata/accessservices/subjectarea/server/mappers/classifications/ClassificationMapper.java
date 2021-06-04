/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Static mapping methods to map between an OMAS Classification and the OMRS Classification.
 */
abstract public class ClassificationMapper {
    private static final Logger log = LoggerFactory.getLogger( ClassificationMapper.class);
    private static final String className = ClassificationMapper.class.getName();
    protected final OMRSRepositoryHelper repositoryHelper;
    protected final OMRSAPIHelper omrsapiHelper;
    protected ClassificationMapper(OMRSAPIHelper omrsapiHelper) {
        this.omrsapiHelper= omrsapiHelper;
        this.repositoryHelper = omrsapiHelper.getOMRSRepositoryHelper();
    }
    /**
     * @param omrsClassification - the supplied omrs classification
     * @return equivalent org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification
     */
    public org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification mapOmrsToBean(Classification omrsClassification) {
        String classificationTypeName = omrsClassification.getName();
        if (isTypeCorrect(classificationTypeName)) {
            org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification = createOmasClassification();
            //set core attributes
            SystemAttributes systemAttributes = createSystemAttributesFromOMRSClassification(omrsClassification);
            omasClassification.setSystemAttributes(systemAttributes);
            // Set properties if there are any to set
            InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
            if (omrsClassificationProperties !=null && omrsClassificationProperties.getInstanceProperties() !=null && !omrsClassificationProperties.getInstanceProperties().isEmpty() ) {
                omasClassification.setEffectiveFromTime(omrsClassificationProperties.getEffectiveFromTime());
                omasClassification.setEffectiveToTime(omrsClassificationProperties.getEffectiveToTime());

                Set<String> attributeNameSet = mapKnownAttributesToOmrs(omasClassification, omrsClassificationProperties);

                Iterator<String> omrsPropertyIterator = omrsClassificationProperties.getPropertyNames();
                while (omrsPropertyIterator.hasNext()) {
                    String name = omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsClassificationProperties.getPropertyValue(name);

                    if (!attributeNameSet.contains(name)) {
                        // this is not an attribute we know about so put the attribute in extra Attributes
                        String attributeValue = null;
                        switch (value.getInstancePropertyCategory()) {
                            case PRIMITIVE:
                                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getAdditionalProperties()) {
                                    omasClassification.setAdditionalProperties(Collections.emptyMap());
                                }
                                attributeValue = primitivePropertyValue.valueAsString();
                                break;
                            case ENUM:
                                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getAdditionalProperties()) {
                                    omasClassification.setAdditionalProperties(Collections.emptyMap());
                                }
                                attributeValue = enumPropertyValue.valueAsString();

                                break;
                            case MAP:
                                MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getAdditionalProperties()) {
                                    omasClassification.setAdditionalProperties(Collections.emptyMap());
                                }
                                attributeValue = mapPropertyValue.getMapValues().toString();
                                break;
                            case ARRAY:
                            case STRUCT:
                            case UNKNOWN:
                                // error
                                break;
                        }
                        omasClassification.getAdditionalProperties().put(name, attributeValue);


                    }   // end while
                } // end   if (omrsClassificationProperties !=null)
            }
            return omasClassification;
        } else {
            // TODO wrong type
        }
        return null;
    }
    public Classification mapBeanToOmrs(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification) {
         SystemAttributes systemAttributes = omasClassification.getSystemAttributes();
         org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification();
         SubjectAreaUtils.populateSystemAttributesForInstanceAuditHeader(systemAttributes, omrsClassification);
         // copy over the classification name
         String classificationName = omasClassification.getClassificationName();
         omrsClassification.setName(classificationName);
         // copy over the classification type
         OpenMetadataTypesArchiveAccessor archiveAccessor = OpenMetadataTypesArchiveAccessor.getInstance();
         String classificationTypeGuid = archiveAccessor.getClassificationDefByName(classificationName).getGUID();
         InstanceType instanceType = new InstanceType();
         instanceType.setTypeDefName(classificationName);
         instanceType.setTypeDefGUID(classificationTypeGuid);
         omrsClassification.setType(instanceType);
         // copy over the classification properties
         omrsClassification.setProperties(updateOMRSAttributes(omasClassification));
         return omrsClassification;
    }

    /**
     * get an instance of the classification
     * @return classification instance
     */
    protected abstract org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification createOmasClassification();

    /**
     * Map attributes we know are part of this type to OMRS.
     * @param omasClassification omasclassification
     * @param omrsClassificationProperties omrs properties
     * @return set of property Names that we know about.
     */
    protected abstract Set<String> mapKnownAttributesToOmrs( org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification,InstanceProperties omrsClassificationProperties);

    /**
     * Check whether the type name matches the mapper.
     * @param actualTypeName name of type to check
     * @return true if type is correct otherwise false.
     */
    protected boolean isTypeCorrect(String actualTypeName) {
        return this.repositoryHelper.isTypeOf(this.omrsapiHelper.getServiceName(),actualTypeName,getTypeName());
    }

    /**
     * Get the omas classification type name (same as the classification name) - this is defined in the subclass.
     * @return classsification name
     */
    abstract String getTypeName();

    protected abstract InstanceProperties updateOMRSAttributes(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification);

    public static SystemAttributes createSystemAttributesFromOMRSClassification(Classification omrsClassification) {
        SystemAttributes systemAttributes = new SystemAttributes();
        Status status =  SubjectAreaUtils.convertInstanceStatusToStatus(omrsClassification.getStatus());
        systemAttributes.setStatus(status);
        systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
        systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
        Date createtimeDate = omrsClassification.getCreateTime();
        Long createTimeLong=null;
        if (createtimeDate != null) {
            createTimeLong = createtimeDate.getTime();
        }
        systemAttributes.setCreateTime(createTimeLong);
        Date updatetimeDate = omrsClassification.getUpdateTime();
        Long updateTimeLong=null;
        if (updatetimeDate != null) {
            updateTimeLong = updatetimeDate.getTime();
        }
        systemAttributes.setUpdateTime(updateTimeLong);
        systemAttributes.setVersion(omrsClassification.getVersion());
        return systemAttributes;
    }
}
