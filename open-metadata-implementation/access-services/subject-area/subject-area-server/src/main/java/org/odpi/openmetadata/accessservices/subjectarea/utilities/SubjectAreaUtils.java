/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.RelatedMedia.RelatedMedia;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.MediaUsage;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * Subject Area OMAS utilities.
 */
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
     * Set icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     *
     * Note that we shoukd only return the icons that are effective - by checking the effective From and To dates against the current time
     * @param userId userid under which to issue to the get of the related media
     * @param subjectAreaBeansToAccessOMRS bean to access OMRS
     * @param mediaReferenceRelationshipSet set of related media Relationships
     * @return Set of IconSummary objects.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     */
    public static Set<IconSummary> getIconSummarySet(String userId, SubjectAreaBeansToAccessOMRS subjectAreaBeansToAccessOMRS, Set<Line> mediaReferenceRelationshipSet) throws UserNotAuthorizedException, InvalidParameterException, UnrecognizedGUIDException, MetadataServerUncontactableException
    {
        Set<IconSummary> icons = new HashSet<>();
        Set<String> relatedMediaGuidSet =new HashSet<>();
        if (mediaReferenceRelationshipSet !=null && !mediaReferenceRelationshipSet.isEmpty()) {
            for (Line line :mediaReferenceRelationshipSet) {
                MediaReference mediaReference= (MediaReference)line;
                if (isEffective(line))
                {
                    String mediaGuid = mediaReference.getEntity2Guid();
                    relatedMediaGuidSet.add(mediaGuid);
                }
            }
        }
        for (String relatedMediaguid : relatedMediaGuidSet)
        {
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.RelatedMedia.RelatedMedia relatedMedia = subjectAreaBeansToAccessOMRS.getRelatedMediaById(userId, relatedMediaguid);
           if (isEffective(relatedMedia))
           {
               IconSummary icon = SubjectAreaUtils.extractIconSummaryFromRelatedMedia(relatedMedia);
               if (icon != null)
               {
                   icons.add(icon);
               }
           }
        }
        return icons;
    }

    /**
     *
     * @param relatedMedia to check
     * @return boolean indicating whether this Related Media is effective
     */
    private static boolean isEffective(RelatedMedia relatedMedia)
    {
        return isEffective(relatedMedia.getEffectiveFromTime(),relatedMedia.getEffectiveToTime() ) ;
    }

    /**
     *
     * @param line line to check
     * @return boolean indicating whether this line is effective
     */
    public static boolean isEffective(Line line)
    {
        return isEffective(line.getEffectiveFromTime(), line.getEffectiveToTime());
    }

    /**
     * check check whether the current time lies between the from and to
     * @param from Date from which effective
     * @param to Date to which effective
     * @return whether effective.
     */
    public static boolean isEffective(Date from, Date to) {
        boolean isEffective= true;
        long currentDate = new Date().getTime();
        if (from != null) {
            if (currentDate<from.getTime()) {
                isEffective = false;
            }
        }
        if (to != null) {
            if (currentDate>to.getTime()) {
                isEffective = false;
            }
        }
        return isEffective;
    }

    /**
     * Get a Term's icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     * @param userId userid under which to issue to the get of the related media
     * @param subjectAreaBeansToAccessOMRS bean to access OMRS
     * @param line glossary relationship
     * @return Set of IconSummary objects.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     * @throws FunctionNotSupportedException   Function not supported
     */
    public static GlossarySummary getGlossarySummaryForTerm(String userId, SubjectAreaBeansToAccessOMRS subjectAreaBeansToAccessOMRS, Line line) throws UserNotAuthorizedException,
                                                                                                                                                        InvalidParameterException,
                                                                                                                                                        UnrecognizedGUIDException,
                                                                                                                                                        MetadataServerUncontactableException,
                                                                                                                                                        FunctionNotSupportedException
    {
        TermAnchor termAnchorRelationship = (TermAnchor)line;
        String glossaryGuid = termAnchorRelationship.getEntity1Guid();
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary glossary = subjectAreaBeansToAccessOMRS.getGlossaryById(userId, glossaryGuid);
        GlossarySummary glossarySummary =extractGlossarySummaryFromGlossary(glossary);
        if (glossarySummary !=null) {
            //get glossary icons
            String mediaReferenceTypeGuid = TypeGuids.getMediaReferenceTypeGuid();
            Set<Line> mediaReferenceRelationships = subjectAreaBeansToAccessOMRS.getGlossaryRelationships(userId, glossaryGuid, mediaReferenceTypeGuid, 0, null, null, null, 0);
            Set<IconSummary> icons = getIconSummarySet(userId, subjectAreaBeansToAccessOMRS, mediaReferenceRelationships);
            glossarySummary.setIcons(icons);
        }
        return glossarySummary;
    }
    /**
     * Get a Categories icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     * @param userId userid under which to issue to the get of the related media
     * @param subjectAreaBeansToAccessOMRS bean to access OMRS
     * @param line glossary relationship
     * @return Set of IconSummary objects.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied guid was not recognised
     * @throws MetadataServerUncontactableException  not able to communicate with a Metadata respository service.
     * @throws FunctionNotSupportedException   Function not supported
     */
    public static GlossarySummary getGlossarySummaryForCategory(String userId, SubjectAreaBeansToAccessOMRS subjectAreaBeansToAccessOMRS, Line line) throws UserNotAuthorizedException, InvalidParameterException, UnrecognizedGUIDException, MetadataServerUncontactableException, FunctionNotSupportedException
    {
        CategoryAnchor categoryAnchorRelationship = (CategoryAnchor)line;
        String glossaryGuid = categoryAnchorRelationship.getEntity1Guid();
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary glossary = subjectAreaBeansToAccessOMRS.getGlossaryById(userId, glossaryGuid);
        GlossarySummary glossarySummary =extractGlossarySummaryFromGlossary(glossary);
        //get glossary icons
        String mediaReferenceTypeGuid = TypeGuids.getMediaReferenceTypeGuid();
        Set<Line> mediaReferenceRelationships = subjectAreaBeansToAccessOMRS.getGlossaryRelationships(userId, glossaryGuid, mediaReferenceTypeGuid, 0, null, null, null, 0);
        Set<IconSummary> icons =getIconSummarySet(userId,subjectAreaBeansToAccessOMRS,mediaReferenceRelationships);
        glossarySummary.setIcons(icons);
        return glossarySummary;
    }

    /**
     * Get a summary of the parent category. The parent category is optional. We might validly have more than one parent category. this can occur if effectivity dates are being used
     * The parent category is the first relationship we find of the rigght type that is effective.
     * @param userId userid that the requests are issued under
     * @param subjectAreaBeansToAccessOMRS service
     * @param lines set of lines that are of the right type.
     * @return a parent category as a CategorySummary
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException  the supplied guid was not recognised
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws FunctionNotSupportedException   Function not supported
     */
    public static CategorySummary getParentCategorySummary(String userId, SubjectAreaBeansToAccessOMRS subjectAreaBeansToAccessOMRS, Set<Line> lines) throws UserNotAuthorizedException, InvalidParameterException, UnrecognizedGUIDException, MetadataServerUncontactableException, FunctionNotSupportedException
    {
        CategorySummary parentCategorySummary =null;
        final Iterator<Line> iterator = lines.iterator();
        while (iterator.hasNext() && parentCategorySummary ==null)
        {
            CategoryHierarchyLink parentRelationship = (CategoryHierarchyLink) iterator.next();
            if (isEffective(parentRelationship))
            {
                String parentCategoryGuid = parentRelationship.getEntity1Guid();
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory glossaryCategory = subjectAreaBeansToAccessOMRS.getGlossaryCategoryById(userId, parentCategoryGuid);
                parentCategorySummary = extractCategorySummaryFromGlossaryCategory(glossaryCategory);
                //get parent category icons
                String mediaReferenceTypeGuid = TypeGuids.getMediaReferenceTypeGuid();
                Set<Line> mediaReferenceRelationships = subjectAreaBeansToAccessOMRS.getGlossaryCategoryRelationships(userId, parentCategoryGuid, mediaReferenceTypeGuid, 0, null, null, null, 0);
                // the following call checks the icon effectivity
                Set<IconSummary> icons = getIconSummarySet(userId, subjectAreaBeansToAccessOMRS, mediaReferenceRelationships);
                parentCategorySummary.setIcons(icons);
            }
        }


        return parentCategorySummary;
    }

    /**
     * Convert a GlossaryCategory to a Categorysummary
     * @param glossaryCategory to convert
     * @return CategorySummary
     */
    private static CategorySummary extractCategorySummaryFromGlossaryCategory(GlossaryCategory glossaryCategory)
    {
        CategorySummary categorySummary = new CategorySummary();
        categorySummary.setQualifiedName(glossaryCategory.getQualifiedName());
        categorySummary.setName(glossaryCategory.getDisplayName());
        categorySummary.setGuid(glossaryCategory.getSystemAttributes().getGUID());
        return categorySummary;
    }

    /**
     * extract iconSummary if this media is an icon
     * @param relatedMedia related media
     * @return iconSummaru or null
     */
    public static IconSummary extractIconSummaryFromRelatedMedia(RelatedMedia relatedMedia)
    {
        IconSummary icon = null;
        if (relatedMedia.getMediaUsage().contains(MediaUsage.Icon.getOrdinal()))
        {
            icon = new IconSummary();
            icon.setGuid(relatedMedia.getSystemAttributes().getGUID());
            icon.setQualifiedName(relatedMedia.getQualifiedName());
            icon.setUrl(relatedMedia.getUrl());
            icon.setQualifiedName(relatedMedia.getQualifiedName());
            icon.setLabel(relatedMedia.getDisplayName());
        }
        return icon;
    }
    /**
     * Extract glossarySummary
     * @param glossary to extract
     * @return Glossary Summary or null
     */
    public static GlossarySummary extractGlossarySummaryFromGlossary(Glossary glossary)
    {
        if (glossary ==null) return null;
        GlossarySummary glossarySummary = new GlossarySummary();

        glossarySummary.setGuid(glossary.getSystemAttributes().getGUID());
        glossarySummary.setQualifiedName(glossary.getQualifiedName());
        glossarySummary.setName(glossary.getDisplayName());
        return glossarySummary;
    }
    public static void checkStatusNotDeleted(Status status, SubjectAreaErrorCode errorCode) throws InvalidParameterException
    {
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
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
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
     * Convert the OMRS Line objects to the equivalent OMAS objects and return them in a list.
     * @param omrsLines - these are Lines generated by hte generator from the omrs relationships
     * @return omasLines - these are the Lines that exposed in the OMAS API
     */
     public static List<Line> convertOMRSLinesToOMASLines(Set<Line> omrsLines) {
        List omasLines = new ArrayList();
        for (Line omrsLine:omrsLines) {
            String omrsName = omrsLine.getName();
            Line omasLine = null;
            switch (omrsName) {
               case "TermHASARelationship":
                   omasLine =HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship((TermHASARelationship) omrsLine);
                break;
                case "RelatedTerm":
                    omasLine =RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm((RelatedTerm) omrsLine);
                break;
                case "Synonym":
                    omasLine =SynonymMapper.mapOMRSRelationshipBeanToSynonym((Synonym) omrsLine);
                break;
                case "Antonym":
                    omasLine =AntonymMapper.mapOMRSRelationshipBeanToAntonym((Antonym) omrsLine);
                break;
                case "Translation":
                    omasLine =TranslationMapper.mapOMRSRelationshipBeanToTranslation((Translation) omrsLine);
                break;
                case "ISARelationship":
                    omasLine = IsaMapper.mapOMRSRelationshipBeanToISARelationship((ISARelationship) omrsLine);
                break;
                case "PreferredTerm":
                    omasLine =PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm((PreferredTerm) omrsLine);
                break;
                case "TermISATypeOFRelationship":
                    omasLine =IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship((TermISATypeOFRelationship) omrsLine);
                break;
                case "ReplacementTerm":
                    omasLine =TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm((ReplacementTerm) omrsLine);
                break;
                case "TermTYPEDBYRelationship":
                    omasLine =TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship((TermTYPEDBYRelationship) omrsLine);
                break;
                case "UsedInContext":
                    omasLine =UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext((UsedInContext) omrsLine);
                break;
                case "ValidValue":
                    omasLine =ValidValueMapper.mapOMRSRelationshipBeanToValidValue((ValidValue) omrsLine);
                break;
                // external glossary relationships
                case "LibraryCategoryReferenceRelationshipRelationship":
                    // TODO
                break;
                case "LibraryTermReferenceRelationshipRelationship":
                    // TODO
                break;
                        // term to asset
                case "SemanticAssignment":
                    omasLine =SemanticAssignmentMapper.mapOMRSRelationshipBeanToSemanticAssignment((SemanticAssignment) omrsLine);
                break;
                        // category to term
                case "TermCategorization":
                   omasLine = TermCategorizationMapper.mapOMRSRelationshipBeanToTermCategorization((TermCategorization) omrsLine);
                break;
                // Term to glossary
                case "TermAnchor":
                    omasLine =TermAnchorMapper.mapOMRSRelationshipBeanToTermAnchor((TermAnchor) omrsLine);
                    break;
                // Category to glossary
                case "CategoryAnchor":
                    omasLine = CategoryAnchorMapper.mapOMRSRelationshipBeanToCategoryAnchor((CategoryAnchor) omrsLine);
                    break;
            }
            if (omasLine!=null) {
                omasLines.add(omasLine);
            }
        }
        return omasLines;
     }
    /**
     * convert omas sequencing order to omrs sequencing order
     * @param sequencingOrder supplied omas sequencing order
     * @return omrs sequencing order
     */
    static public SequencingOrder convertOMASToOMRSSequencingOrder(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder) {
        SequencingOrder omrsSequencingOrder = null;
        if (sequencingOrder ==null) {
            omrsSequencingOrder = SequencingOrder.ANY;
        } else if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.ANY)) {
            omrsSequencingOrder = SequencingOrder.ANY;
        } else  if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.CREATION_DATE_RECENT)) {
            omrsSequencingOrder = SequencingOrder.CREATION_DATE_RECENT;
        } else  if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.CREATION_DATE_OLDEST)) {
            omrsSequencingOrder = SequencingOrder.CREATION_DATE_OLDEST;
        } else  if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.LAST_UPDATE_RECENT)) {
            omrsSequencingOrder = SequencingOrder.LAST_UPDATE_RECENT;
        } else if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.LAST_UPDATE_OLDEST)) {
            omrsSequencingOrder = SequencingOrder.LAST_UPDATE_OLDEST;
        } else if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.PROPERTY_ASCENDING)) {
            omrsSequencingOrder = SequencingOrder.PROPERTY_ASCENDING;
        } else if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.PROPERTY_DESCENDING)) {
            omrsSequencingOrder = SequencingOrder.PROPERTY_DESCENDING;
        } else if (sequencingOrder.equals(org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder.GUID)) {
            omrsSequencingOrder = SequencingOrder.GUID;
        }
        return  omrsSequencingOrder;
    }
}