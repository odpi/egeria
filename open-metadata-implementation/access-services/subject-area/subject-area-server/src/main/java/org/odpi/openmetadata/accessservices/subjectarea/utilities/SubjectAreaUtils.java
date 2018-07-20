/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.RelatedMedia.RelatedMedia;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.MediaUsage;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
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
    public static String getIcon(@PathVariable String userId, Set<RelatedMediaReference> relatedMediaReferenceSet) {
        String iconUrl =null;
        if (relatedMediaReferenceSet !=null && !relatedMediaReferenceSet.isEmpty()) {
            for (RelatedMediaReference relatedMediaReference : relatedMediaReferenceSet) {

                RelatedMedia relatedMedia =  relatedMediaReference.getRelatedMedia();
                //TODO issue call to server to get the relatedMedia content.
                // TODO can we use the the relatedMedia
//                if (relatedMedia.getMediaUsage().equals(MediaUsage.ICON)) {
//                    iconUrl = relatedMedia.getUrl();
//                    break;
//                }
            }
        }
        return iconUrl;
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
        switch (instanceStatus) {
            case ACTIVE:
                status = Status.ACTIVE;
                break;
            case DRAFT:
                status = Status.DRAFT;
                break;
            case DELETED:
                status = Status.DELETED;
                break;
            case PREPARED:
                status = Status.PREPARED;
                break;
            case PROPOSED:
                status = Status.PROPOSED;
                break;
            default:
                status = Status.UNKNOWN;
        }

        return status;
    }

    public static InstanceStatus convertStatusToStatusInstance(Status status) {
        InstanceStatus instanceStatus = null;
        switch (status) {
            case ACTIVE:
                instanceStatus = InstanceStatus.ACTIVE;
                break;
            case DRAFT:
                instanceStatus = InstanceStatus.DRAFT;
                break;
            case DELETED:
                instanceStatus = InstanceStatus.DELETED;
                break;
            case PREPARED:
                instanceStatus = InstanceStatus.PREPARED;
                break;
            case PROPOSED:
                instanceStatus = InstanceStatus.PROPOSED;
                break;
            default:
                instanceStatus = InstanceStatus.UNKNOWN;
        }
        return instanceStatus;
    }
}
