/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The base class for all mappings between OMRS AttributeTypeDefs and IGC properties.
 */
public abstract class AttributeMapping {

    private static final Logger log = LoggerFactory.getLogger(AttributeMapping.class);

    private IGCPropertyType igcPropertyType;
    private String omrsAttributeTypeDefName;
    private String igcAssetType;
    private String igcPropertyName;

    public enum IGCPropertyType {
        STRING, BOOLEAN, DATETIME, NUMBER, ENUM,
        STRING_ARRAY, NUMBER_ARRAY, DATETIME_ARRAY,
        REFERENCE, REFERENCE_LIST
    }

    public AttributeMapping(IGCPropertyType igcPropertyType,
                            String omrsAttributeTypeDefName) {
        this(null, null, igcPropertyType, omrsAttributeTypeDefName);
    }

    public AttributeMapping(String igcAssetType,
                            String igcPropertyName,
                            IGCPropertyType igcPropertyType,
                            String omrsAttributeTypeDefName) {
        this.igcAssetType = igcAssetType;
        this.igcPropertyName = igcPropertyName;
        this.igcPropertyType = igcPropertyType;
        this.omrsAttributeTypeDefName = omrsAttributeTypeDefName;
    }

    /**
     * Indicates whether this mapping is for a general attribute (true) or a specific attribute (false).
     *
     * @return boolean
     */
    public boolean isForGeneralAttribute() { return this.igcAssetType == null; }

    /**
     * Indicates whether this mapping is for a specific attribute (true) or a general attribute (false).
     *
     * @return boolean
     */
    public boolean isForSpecificAttribute() { return !isForGeneralAttribute(); }

    /**
     * Retrieve the IGC asset type to which this attribute mapping applies. Note that this will only return a non-null
     * value when the attribute mapping is for a specific attribute.
     *
     * @return String
     * @see #isForSpecificAttribute()
     */
    public String getIgcAssetType() { return this.igcAssetType; }

    /**
     * Retrieve the OMRS AttributeTypeDef name for this attribute mapping applies.
     *
     * @return String
     */
    public String getOmrsAttributeTypeDefName() { return this.omrsAttributeTypeDefName; }

    /**
     * Retrieve the IGC property type for this attribute mapping.
     *
     * @return IGCPropertyType
     */
    public IGCPropertyType getIgcPropertyType() { return this.igcPropertyType; }

    /**
     * Indicates whether this attribute mapping can be applied to the provided IGC property (for the provided IGC asset
     * type) (true) or not (false).
     *
     * @param igcAssetType name of the IGC asset type
     * @param igcPropertyName name of the IGC property on that asset type
     * @return boolean
     */
    public boolean matchesProperty(String igcAssetType, String igcPropertyName) {
        // TODO...
        return false;
    }

    /**
     * Returns the OMRS PrimitivePropertyValue represented by the provided value.
     *
     * @param value the value to represent as an OMRS PrimitivePropertyValue
     * @return PrimitivePropertyValue
     */
    public static PrimitivePropertyValue getPrimitivePropertyValue(Object value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        PrimitiveDef primitiveDef = new PrimitiveDef();
        if (value instanceof Boolean) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
        } else if (value instanceof Date) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        } else if (value instanceof Integer) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        } else if (value instanceof Number) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
        } else {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        }
        propertyValue.setPrimitiveValue(value);
        propertyValue.setPrimitiveDefCategory(primitiveDef.getPrimitiveDefCategory());
        propertyValue.setTypeGUID(primitiveDef.getGUID());
        propertyValue.setTypeName(primitiveDef.getName());
        return propertyValue;
    }

}
