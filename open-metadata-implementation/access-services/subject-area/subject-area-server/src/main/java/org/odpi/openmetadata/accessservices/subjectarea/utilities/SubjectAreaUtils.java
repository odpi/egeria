/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.NodeSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Subject Area OMAS utilities.
 */
public class SubjectAreaUtils {

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaUtils.class);

    private static final String className = SubjectAreaUtils.class.getName();

    public static InstanceType createTemplateFromTypeDef(TypeDef typeDef) {
        InstanceType template = new InstanceType();
        template.setTypeDefName(typeDef.getName());
        template.setTypeDefCategory(typeDef.getCategory());
        template.setTypeDefGUID(typeDef.getGUID());
        template.setTypeDefVersion(typeDef.getVersion());

        return template;
    }

    /**
     * Convert a Category to a CategorySummary
     *
     * @param category to convert
     * @param relationship {@link Relationship}
     * @return CategorySummary
     */
    public static CategorySummary extractCategorySummaryFromCategory(Category category, Relationship relationship) {
        CategorySummary categorySummary = new CategorySummary();
        extractNodeSummary(category, relationship, categorySummary);
        return categorySummary;
    }

    private static void extractNodeSummary(Node node, Relationship relationship, NodeSummary nodeSummary) {
        nodeSummary.setQualifiedName(node.getQualifiedName());
        nodeSummary.setName(node.getName());
        nodeSummary.setGuid(node.getSystemAttributes().getGUID());
        nodeSummary.setFromEffectivityTime(node.getEffectiveFromTime());
        nodeSummary.setToEffectivityTime(node.getEffectiveToTime());
        nodeSummary.setRelationshipguid(relationship.getGuid());
        nodeSummary.setFromRelationshipEffectivityTime(relationship.getEffectiveFromTime());
        nodeSummary.setToRelationshipEffectivityTime(relationship.getEffectiveToTime());
    }



    /**
     * Extract Glossary Summary
     *
     * @param glossary the glossary that is to be summarised
     * @param relationship     the relationship to the glossary, which feeds part of the node summary
     * @return Glossary Summary or null
     */
    public static GlossarySummary extractGlossarySummaryFromGlossary(Glossary glossary, Relationship relationship) {
        if (glossary == null) return null;
        GlossarySummary glossarySummary = new GlossarySummary();
        extractNodeSummary(glossary, relationship, glossarySummary);
        return glossarySummary;
    }

    /**
     * Get glossary guid from anchors
     *
     * @param relationship - {@link TermAnchor} or {@link CategoryAnchor}
     * @return glossaryGuid
     * */
    public static String getGlossaryGuidFromAnchor(Relationship relationship) {
        String glossaryGuid = null;
        if (relationship instanceof  TermAnchor || relationship instanceof CategoryAnchor) {
            glossaryGuid = relationship.getEnd1().getNodeGuid();
        }
        return glossaryGuid;
    }

    public static void checkStatusNotDeleted(Status status, SubjectAreaErrorCode errorCode) throws InvalidParameterException {
        final String methodName = "checkStatusNotDeleted";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        if (status.equals(Status.DELETED)) {
            String propertyName =   "Status";
            String propertyValue =  Status.DELETED.name();
            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition(propertyName,propertyValue);
            throw new InvalidParameterException(messageDefinition,
                    className,
                    methodName,
                    propertyName,
                    propertyValue);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
    }

    public static Status convertInstanceStatusToStatus(InstanceStatus instanceStatus) {
        Status status = null;

        if (instanceStatus == null) {
            //default to ACTIVE
            status = Status.ACTIVE;
        } else {
            switch (instanceStatus) {
                case ACTIVE:
                    status = Status.ACTIVE;
                    break;
                case DELETED:
                    status = Status.DELETED;
                    break;
                case DRAFT:
                    status = Status.DRAFT;
                    break;
                case UNKNOWN:
                    status = Status.UNKNOWN;
                    break;
                case PREPARED:
                    status = Status.PREPARED;
                    break;
                case PROPOSED:
                    status = Status.PROPOSED;
                    break;
                case APPROVED:
                    status = Status.APPROVED;
                    break;
            }

        }
        return status;
    }

    public static InstanceStatus convertStatusToInstanceStatus(Status status) {
        InstanceStatus instanceStatus = null;
        if (status == null) {
            //default to ACTIVE
            instanceStatus = InstanceStatus.ACTIVE;
        } else {
            switch (status) {
                case ACTIVE:
                    instanceStatus = InstanceStatus.ACTIVE;
                    break;
                case DELETED:
                    instanceStatus = InstanceStatus.DELETED;
                    break;
                case DRAFT:
                    instanceStatus = InstanceStatus.DRAFT;
                    break;
                case UNKNOWN:
                    instanceStatus = InstanceStatus.UNKNOWN;
                    break;
                case PREPARED:
                    instanceStatus = InstanceStatus.PREPARED;
                    break;
                case PROPOSED:
                    instanceStatus = InstanceStatus.PROPOSED;
                    break;
                case APPROVED:
                    instanceStatus = InstanceStatus.APPROVED;
                    break;
            }
        }
        return instanceStatus;
    }

    /**
     * create SystemAttributes instance from an insance header object
     *
     * @param instanceHeader omrs instance header
     * @return SystemAttributes
     */
    public static SystemAttributes createSystemAttributes(InstanceHeader instanceHeader) {
        //set core attributes
        SystemAttributes systemAttributes = new SystemAttributes();

        InstanceStatus instanceStatus = instanceHeader.getStatus();
        Status omas_status = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
        systemAttributes.setStatus(omas_status);

        systemAttributes.setCreatedBy(instanceHeader.getCreatedBy());
        systemAttributes.setUpdatedBy(instanceHeader.getUpdatedBy());
        Date createTimeDate = instanceHeader.getCreateTime();
        Long createTimeLong =null;
        if (createTimeDate != null) {
            createTimeLong = createTimeDate.getTime();
        }
        systemAttributes.setCreateTime(createTimeLong);
        Date updateTimeDate = instanceHeader.getUpdateTime();
        Long updateTimeLong =null;
        if (updateTimeDate != null) {
            updateTimeLong = updateTimeDate.getTime();
        }
        systemAttributes.setUpdateTime(updateTimeLong);
        systemAttributes.setVersion(instanceHeader.getVersion());
        systemAttributes.setGUID(instanceHeader.getGUID());
        return systemAttributes;
    }

    public static void populateSystemAttributesForInstanceAuditHeader(SystemAttributes systemAttributes, InstanceAuditHeader instanceAuditHeader) {
        if (systemAttributes != null) {
            if (systemAttributes.getCreatedBy() != null)
                instanceAuditHeader.setCreatedBy(systemAttributes.getCreatedBy());
            if (systemAttributes.getUpdatedBy() != null)
                instanceAuditHeader.setUpdatedBy(systemAttributes.getUpdatedBy());
            if (systemAttributes.getCreateTime() != null)
                instanceAuditHeader.setCreateTime(new Date(systemAttributes.getCreateTime()));
            if (systemAttributes.getUpdateTime() != null)
                instanceAuditHeader.setUpdateTime(new Date(systemAttributes.getUpdateTime()));
            if (systemAttributes.getVersion() != null)
                instanceAuditHeader.setVersion(systemAttributes.getVersion());
        }
    }

    /**
     * Set the String value into the InstanceProperties.
     *
     * @param instanceProperties supplied instanceproperties
     * @param stringValue        string value
     * @param propertyName       property name.
     */
    public static void setStringPropertyInInstanceProperties(InstanceProperties instanceProperties, String stringValue, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(stringValue);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }

    /**
     * Set the Integer value into the InstanceProperties.
     *
     * @param instanceProperties supplied instanceproperties
     * @param integerValue       integer value
     * @param propertyName       property name.
     */
    public static void setIntegerPropertyInInstanceProperties(InstanceProperties instanceProperties, Integer integerValue, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        primitivePropertyValue.setPrimitiveValue(integerValue);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }

    /**
     * set the Date value into the InstanceProperties
     *
     * @param instanceProperties supplied instanceproperties
     * @param date               Datew value
     * @param propertyName       property name
     */
    public static void setDatePropertyInInstanceProperties(InstanceProperties instanceProperties, Date date, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        Long timestamp = date.getTime();
        primitivePropertyValue.setPrimitiveValue(timestamp);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }

    /**
     * Set icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     *
     * Note that we should only return the icons that are effective - by checking the effective From and To dates against the current time
     * @param userId userid under which to issue to the get of the related media
     * @param guid to get associated icons from
     * @return response with Set of IconSummary objects or an Exception response.
     */
    public SubjectAreaOMASAPIResponse<IconSummary> getIconSummarySet(String userId, String guid) {
        // if there are no icons then return an empty set

        //TODO implement icon logic
        SubjectAreaOMASAPIResponse<IconSummary> response = new SubjectAreaOMASAPIResponse<>();
        Set<IconSummary> icons = new HashSet<>();
        response.addAllResults(icons);
        return response;
    }

    /**
     * Set status values into instance properties.
     *
     * @param instanceProperties supplied instanceproperties
     * @param status               Status value
     * @param propertyName       property name
     */
    public static void setStatusPropertyInInstanceProperties(InstanceProperties instanceProperties, TermRelationshipStatus status, String propertyName) {
        EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
        enumPropertyValue.setOrdinal(status.getOrdinal());
        enumPropertyValue.setSymbolicName(status.getName());
        instanceProperties.setProperty(propertyName, enumPropertyValue);
    }
}