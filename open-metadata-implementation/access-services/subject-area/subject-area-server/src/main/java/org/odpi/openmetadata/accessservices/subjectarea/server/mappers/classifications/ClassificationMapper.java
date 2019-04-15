/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Static mapping methods to map between an OMAS Classificaiton and the OMRS Classification.
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
            // Set properties
            InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
            if (omrsClassificationProperties != null) {
                omasClassification.setEffectiveFromTime(omrsClassificationProperties.getEffectiveFromTime());
                omasClassification.setEffectiveToTime(omrsClassificationProperties.getEffectiveToTime());

                Set<String> attributeNameSet = mapKnownAttributesToOmrs(omasClassification, omrsClassificationProperties);

                Iterator omrsPropertyIterator = omrsClassificationProperties.getPropertyNames();
                while (omrsPropertyIterator.hasNext()) {
                    String name = (String) omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsClassificationProperties.getPropertyValue(name);

                    if (!attributeNameSet.contains(name)) {
                        // this is not an attribute we know about so put the attribute in extra Attributes
                        Object attributeValue=null;
                        switch (value.getInstancePropertyCategory()) {
                            case PRIMITIVE:
                                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getExtraAttributes()) {
                                    omasClassification.setExtraAttributes(new HashMap<String, Object>());
                                }
                                attributeValue=primitivePropertyValue;
                                break;
                            case ENUM:
                                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getExtraAttributes()) {
                                    omasClassification.setExtraAttributes(new HashMap<String, Object>());
                                }
                                attributeValue= enumPropertyValue;

                                break;
                            case MAP:
                                MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                // put out the omrs value object
                                if (null == omasClassification.getExtraAttributes()) {
                                    omasClassification.setExtraAttributes(new HashMap<String, Object>());
                                }
                                attributeValue= mapPropertyValue.getMapValues();
                                break;
                            case ARRAY:
                            case STRUCT:
                            case UNKNOWN:
                                // error
                                break;
                        }
                        omasClassification.getExtraAttributes().put(name,attributeValue);
                    }

                }   // end while
            }
            return omasClassification;
        } else {
            // TODO wrong type
        }
        return null;
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
        systemAttributes.setCreateTime(omrsClassification.getCreateTime());
        systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
        systemAttributes.setVersion(omrsClassification.getVersion());
        return systemAttributes;
    }
}
