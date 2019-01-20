/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes.AttributeMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Captures the details of an implemented AttributeTypeDef (mapping) between OMRS and IGC.
 */
public class ImplementedAttribute {

    private static final Logger log = LoggerFactory.getLogger(ImplementedAttribute.class);

    private AttributeTypeDef attributeTypeDef;
    private AttributeMapping attributeMapping;

    public ImplementedAttribute(AttributeTypeDef attributeTypeDef, Class mapper) {

        this.attributeTypeDef = attributeTypeDef;
        this.attributeMapping = getAttributeMapper(mapper);

    }

    /**
     * Retrieve the OMRS AttributeTypeDef processed by the implemented mapping.
     *
     * @return AttributeTypeDef
     */
    public AttributeTypeDef getAttributeTypeDef() { return this.attributeTypeDef; }

    /**
     * Retrieve the Java object capable of translating between IGC property and an OMRS attribute type.
     *
     * @return EntityMapping
     */
    public AttributeMapping getAttributeMapping() { return this.attributeMapping; }

    /**
     * Indicates whether the implemented mapping matches the provided IGC property: that is, whether the mapping
     * can be used to translate to the provided IGC property.
     *
     * @param igcAssetType the IGC asset type to check the mapping against
     * @param igcPropertyName the IGC property to check the mapping against
     * @return boolean
     */
    public boolean matchesIgcProperty(String igcAssetType, String igcPropertyName) {
        return attributeMapping.matchesProperty(igcAssetType, igcPropertyName);
    }

    /**
     * Introspect a mapping class to retrieve an AttributeMapping.
     *
     * @param mappingClass the mapping class to retrieve an instance of
     * @return RelationshipMapping
     */
    private static final AttributeMapping getAttributeMapper(Class mappingClass) {
        AttributeMapping attributeMapper = null;
        try {
            Method getInstance = mappingClass.getMethod("getInstance");
            attributeMapper = (AttributeMapping) getInstance.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to find or instantiate AttributeMapping class: {}", mappingClass, e);
        }
        return attributeMapper;
    }

}
