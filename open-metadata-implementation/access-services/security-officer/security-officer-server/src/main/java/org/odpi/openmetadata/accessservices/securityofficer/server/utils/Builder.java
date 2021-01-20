/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.server.utils;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.BusinessTerm;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.Entity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecuritySchemaElement;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_TAGS;

public class Builder {

    public SecuritySchemaElement buildSecuritySchemaElement(EntityDetail entityDetail, OMRSRepositoryHelper omrsRepositoryHelper) {
        Entity securitySchemaElement = new SecuritySchemaElement();
        buildEntity(entityDetail, securitySchemaElement);

        SecurityClassification securityClassification = getSecurityClassification(entityDetail, omrsRepositoryHelper);
        if (securityClassification != null && securitySchemaElement instanceof SecuritySchemaElement) {
            ((SecuritySchemaElement) securitySchemaElement).setSecurityClassification(securityClassification);
        }

        return (SecuritySchemaElement) securitySchemaElement;
    }

    private void buildEntity(EntityDetail entityDetail, Entity securitySchemaElement) {
        securitySchemaElement.setGuid(entityDetail.getGUID());
        securitySchemaElement.setStatus(entityDetail.getStatus().getName());
        securitySchemaElement.setType(entityDetail.getType().getTypeDefName());
        securitySchemaElement.setCreatedBy(entityDetail.getCreatedBy());
        securitySchemaElement.setCreatedTime(entityDetail.getCreateTime());
        securitySchemaElement.setUpdatedBy(entityDetail.getUpdatedBy());
        securitySchemaElement.setUpdatedTime(entityDetail.getUpdateTime());
        securitySchemaElement.setProperties(getProperties(entityDetail.getProperties()));
    }

    public SchemaElementEntity buildSchemaElementContext(EntityDetail schemaElement, EntityDetail glossaryTerm) {
        SchemaElementEntity schemaElementEntity = buildSchemaElement(schemaElement);
        schemaElementEntity.setBusinessTerm(buildBusinessTerm(glossaryTerm));

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

    public List<SecuritySchemaElement> buildSecuritySchemaElementList(List<EntityDetail> entityDetailList, OMRSRepositoryHelper repositoryHelper) {
        return entityDetailList.stream().map(entityDetail -> buildSecuritySchemaElement(entityDetail, repositoryHelper)).collect(Collectors.toList());
    }

    private SchemaElementEntity buildSchemaElement(EntityDetail entityDetail) {
        Entity schemaElementEntity = new SchemaElementEntity();

        buildEntity(entityDetail, schemaElementEntity);

        schemaElementEntity.setClassifications(getClassifications(entityDetail.getClassifications()));
        return (SchemaElementEntity) schemaElementEntity;
    }

    private BusinessTerm buildBusinessTerm(EntityDetail entityDetail) {
        Entity businessTerm = new BusinessTerm();
        buildEntity(entityDetail, businessTerm);


        businessTerm.setClassifications(getClassifications(entityDetail.getClassifications()));
        return (BusinessTerm) businessTerm;
    }

    private List<org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification> getClassifications(List<Classification> existingClassifications) {
        if (existingClassifications == null || existingClassifications.isEmpty()) {
            return Collections.emptyList();
        }

        List<org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification> classifications = new ArrayList<>();
        existingClassifications.forEach(classification -> classifications.add(buildClassification(classification)));
        return classifications;
    }

    private org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification buildClassification(Classification classification) {
        org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification newClassification = new org.odpi.openmetadata.accessservices.securityofficer.api.model.Classification();
        newClassification.setName(classification.getName());
        newClassification.setType(classification.getType().getTypeDefName());
        newClassification.setProperties(getProperties(classification.getProperties()));

        return newClassification;
    }

    private SecurityClassification getSecurityClassification(EntityDetail entityDetail, OMRSRepositoryHelper omrsRepositoryHelper) {
        if (entityDetail.getClassifications() != null && !entityDetail.getClassifications().isEmpty()) {
            Optional<Classification> securityTag = getSecurityTag(entityDetail.getClassifications());
            if (securityTag.isPresent()) {
                return buildSecurityTag(securityTag.get(), omrsRepositoryHelper);
            }
        }
        return null;
    }

    private Optional<Classification> getSecurityTag(List<Classification> classifications) {
        return classifications.stream().filter(classification -> classification.getName().equals(SECURITY_TAGS)).findAny();
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
