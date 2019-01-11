/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * The base class for all Enum mappings between OMRS AttributeTypeDefs and IGC properties.
 */
public class EnumMapping extends AttributeMapping {

    private static final Logger log = LoggerFactory.getLogger(EnumMapping.class);

    private HashMap<String, EnumElementDef> enumDefByIgcValue;
    private EnumElementDef defaultEnum;

    public EnumMapping(String omrsAttributeTypeDefName) {
        super(IGCPropertyType.STRING, omrsAttributeTypeDefName);
        enumDefByIgcValue = new HashMap<>();
    }

    public EnumMapping(String omrsAttributeTypeDefName,
                       String igcAssetType,
                       String igcPropertyName) {
        super(igcAssetType, igcPropertyName, IGCPropertyType.STRING, omrsAttributeTypeDefName);
        enumDefByIgcValue = new HashMap<>();
    }

    public void addDefaultEnumMapping(int omrsOrdinal, String omrsSymbolicName) {
        defaultEnum = new EnumElementDef();
        defaultEnum.setOrdinal(omrsOrdinal);
        defaultEnum.setValue(omrsSymbolicName);
    }

    public void addEnumMapping(String igcValue, int omrsOrdinal, String omrsSymbolicName) {
        EnumElementDef enumElementDef = new EnumElementDef();
        enumElementDef.setOrdinal(omrsOrdinal);
        enumElementDef.setValue(omrsSymbolicName);
        enumDefByIgcValue.put(igcValue, enumElementDef);
    }

    public EnumPropertyValue getEnumMappingByIgcValue(String igcValue) {
        EnumPropertyValue value = new EnumPropertyValue();
        EnumElementDef element = null;
        if (enumDefByIgcValue.containsKey(igcValue)) {
            element = enumDefByIgcValue.get(igcValue);
        } else {
            if (defaultEnum != null) {
                element = defaultEnum;
            } else {
                log.error("Could not find corresponding enum value for {}, and no default enum defined for {}.", igcValue, getOmrsAttributeTypeDefName());
            }
        }
        if (element != null) {
            value.setOrdinal(element.getOrdinal());
            value.setSymbolicName(element.getValue());
        }
        return value;
    }

}
