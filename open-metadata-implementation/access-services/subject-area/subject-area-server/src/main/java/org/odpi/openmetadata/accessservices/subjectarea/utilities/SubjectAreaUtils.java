/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.RelatedMedia.RelatedMedia;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.MediaUsage;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectAreaUtils {


    private static final Logger log = LoggerFactory.getLogger(SubjectAreaUtils.class);

    private static final String className = SubjectAreaUtils.class.getName();

    public static boolean isTopLevelGlossaryObject(String entityName) {
        boolean isTopLevelGlossaryObject = false;
        if (entityName.equals("Glossary") ||
                entityName.equals("Node") ||
                entityName.equals("GlossaryCategory" )
                ) {
            isTopLevelGlossaryObject = true;
        }
        return isTopLevelGlossaryObject;
    }
    public static boolean isGovernanceActionClassification(String classificationName) {
        boolean isGovernanceActionClassification  = false;
        if (classificationName.equals("Confidence") ||
                classificationName.equals("Confidentiality") ||
                classificationName.equals("Retention") ||
                classificationName.equals("Criticality" )
                ) {
            isGovernanceActionClassification = true;
        }
        return isGovernanceActionClassification;
    }
    public static InstanceType createTemplateFromTypeDef(TypeDef typeDef) {
        InstanceType template = new InstanceType();
        template.setTypeDefName(typeDef.getName());
        template.setTypeDefCategory(typeDef.getCategory());
        template.setTypeDefDescription(typeDef.getDescription());
        template.setTypeDefDescriptionGUID(typeDef.getDescriptionGUID());
        template.setTypeDefGUID(typeDef.getGUID());

        List supertypes = new ArrayList();
        supertypes.add(typeDef.getSuperType());
        template.setTypeDefSuperTypes(supertypes);
        template.setTypeDefVersion(typeDef.getVersion());
        template.setValidStatusList(typeDef.getValidInstanceStatusList());
        // Not setting template.setValidInstanceProperties(); as I have not got this informaiton fropm the typeDef

        return template;
    }
    public static boolean isTerm(String typeName) {
        if (typeName.equals("GlossaryTerm")) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isCategory(String typeName) {
        if (typeName.equals("GlossaryCategory")) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isGlossary(String typeName) {
        if (typeName.equals("Glossary")) {
            return true;
        } else {
            return false;
        }
    }
    public static void addStringToInstanceProperty(String key, String value, InstanceProperties instanceProperties) {
        PrimitivePropertyValue primitivePropertyValue;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(value);
        instanceProperties.setProperty(key, primitivePropertyValue);
    }

    /**
     * This method finds associated icons
     * @param userId userid under which the the operation is performed
     * @param relatedMediaReferenceSet related media references.
     * @return Set with IconSummary objects.
     */
    public static Set<IconSummary> getIconSummaries(@PathVariable String userId, Set<RelatedMediaReference> relatedMediaReferenceSet) {
        Set<IconSummary> icons =new HashSet<>();
        if (relatedMediaReferenceSet !=null && !relatedMediaReferenceSet.isEmpty()) {
            for (RelatedMediaReference relatedMediaReference : relatedMediaReferenceSet) {
                RelatedMedia relatedMedia =  relatedMediaReference.getRelatedMedia();
                if (relatedMedia.getMediaUsage().equals(MediaUsage.Icon)) {IconSummary icon = new IconSummary();
                    icon.setGuid(relatedMedia.getSystemAttributes().getGUID());
                    icon.setRelationshipguid(relatedMediaReference.getRelationshipGuid());
                    icon.setUrl(relatedMedia.getUrl());
                    icon.setQualifiedName(relatedMedia.getQualifiedName());
                    icons.add(icon);
                }
            }
        }
        return icons;
    }
    public static SubjectAreaOMASAPIResponse checkStatusNotDeleted(Status status, SubjectAreaErrorCode errorCode) {
        final String methodName = "checkStatusNotDeleted";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        if (status.equals(Status.DELETED)) {
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
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
            }

        }
        return status;
    }

    public static InstanceStatus convertStatusToStatusInstance(Status status) {
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
            }
        }
        return instanceStatus;
    }
}