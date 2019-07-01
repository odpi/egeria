/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.BusinessTerm;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_TAGS;

public class Builder {

    public SchemaElementEntity buildSchemaElement(EntityDetail entityDetail, OMRSRepositoryHelper omrsRepositoryHelper) {

        SchemaElementEntity schemaElementEntity = new SchemaElementEntity();

        schemaElementEntity.setGuid(entityDetail.getGUID());
        schemaElementEntity.setStatus(entityDetail.getStatus().getName());
        schemaElementEntity.setType(entityDetail.getType().getTypeDefName());
        schemaElementEntity.setCreatedBy(entityDetail.getCreatedBy());
        schemaElementEntity.setCreatedTime(entityDetail.getCreateTime());
        schemaElementEntity.setUpdatedBy(entityDetail.getUpdatedBy());
        schemaElementEntity.setUpdatedTime(entityDetail.getUpdateTime());
        schemaElementEntity.setProperties(getProperties(entityDetail.getProperties()));

        return getSecurityClassification(entityDetail, schemaElementEntity, omrsRepositoryHelper);
    }

    public SchemaElementEntity buildSchemaElementContext(EntityDetail schemaElement, EntityDetail glossaryTerm, OMRSRepositoryHelper omrsRepositoryHelper) {
        SchemaElementEntity schemaElementEntity = buildSchemaElement(schemaElement, omrsRepositoryHelper);
        schemaElementEntity.setBusinessTerm(buildBusinessTerm(glossaryTerm));

        return getSecurityClassification(glossaryTerm, schemaElementEntity, omrsRepositoryHelper);
    }

    private SchemaElementEntity getSecurityClassification(EntityDetail entityDetail, SchemaElementEntity schemaElementEntity, OMRSRepositoryHelper omrsRepositoryHelper) {
        if (entityDetail.getClassifications() != null && !entityDetail.getClassifications().isEmpty()) {
            Optional<Classification> securityTag = getSecurityTag(entityDetail.getClassifications());
            securityTag.ifPresent(classification -> schemaElementEntity.setSecurityClassification(buildSecurityTag(classification, omrsRepositoryHelper)));
        }

        return schemaElementEntity;
    }

    public SecurityClassification buildSecurityTag(Classification classification, OMRSRepositoryHelper repositoryHelper) {
        SecurityClassification securityClassification = new SecurityClassification();

        if (classification.getProperties() != null) {

            List<String> securityLabels = repositoryHelper.getStringArrayProperty(SECURITY_OFFICER, SECURITY_LABELS, classification.getProperties(), "buildSecurityTag");
            Map<String, Object> securityProperties = repositoryHelper.getMapFromProperty(SECURITY_OFFICER, SECURITY_PROPERTIES, classification.getProperties(), "buildSecurityTag");

            securityClassification.setSecurityLabels(securityLabels);
            securityClassification.setSecurityProperties(securityProperties);
        }

        return securityClassification;
    }

    private Optional<Classification> getSecurityTag(List<Classification> classifications) {
        return classifications.stream().filter(classification -> classification.getName().equals(SECURITY_TAGS)).findAny();
    }

    private BusinessTerm buildBusinessTerm(EntityDetail entityDetail) {
        BusinessTerm businessTerm = new BusinessTerm();
        businessTerm.setGuid(entityDetail.getGUID());
        businessTerm.setStatus(entityDetail.getStatus().getName());
        businessTerm.setProperties(getProperties(entityDetail.getProperties()));
        return businessTerm;
    }

    private Map<String, String> getProperties(InstanceProperties uniqueProperties) {
        Map<String, String> properties = new HashMap<>();
        uniqueProperties.getInstanceProperties().forEach((k, v) -> properties.put(k, getStringForPropertyValue(v)));
        return properties;
    }

    private String getStringForPropertyValue(InstancePropertyValue ipv) {

        if (ipv instanceof PrimitivePropertyValue) {
            PrimitiveDefCategory primtype =
                    ((PrimitivePropertyValue) ipv).getPrimitiveDefCategory();
            switch (primtype) {
                case OM_PRIMITIVE_TYPE_STRING:
                    return (String) ((PrimitivePropertyValue) ipv).getPrimitiveValue();
                case OM_PRIMITIVE_TYPE_INT:
                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                case OM_PRIMITIVE_TYPE_BOOLEAN:
                case OM_PRIMITIVE_TYPE_BYTE:
                case OM_PRIMITIVE_TYPE_CHAR:
                case OM_PRIMITIVE_TYPE_DATE:
                case OM_PRIMITIVE_TYPE_DOUBLE:
                case OM_PRIMITIVE_TYPE_FLOAT:
                case OM_PRIMITIVE_TYPE_LONG:
                case OM_PRIMITIVE_TYPE_SHORT:
                    return ((PrimitivePropertyValue) ipv).getPrimitiveValue().toString();
                case OM_PRIMITIVE_TYPE_UNKNOWN:
                default:
                    return "";
            }
        } else {
            if (ipv instanceof EnumPropertyValue) {
                return ((EnumPropertyValue) ipv).getSymbolicName();
            } else {
                return "";
            }
        }

    }

}
