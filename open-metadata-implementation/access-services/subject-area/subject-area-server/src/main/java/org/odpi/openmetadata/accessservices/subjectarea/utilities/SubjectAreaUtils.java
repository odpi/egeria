/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.GlossarySummaryResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.IconSummarySetResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.NodeSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.LinesResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.*;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
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

        List<TypeDefLink> supertypes = new ArrayList<>();
        supertypes.add(typeDef.getSuperType());
        template.setTypeDefSuperTypes(supertypes);
        template.setTypeDefVersion(typeDef.getVersion());
        template.setValidStatusList(typeDef.getValidInstanceStatusList());
        // Not setting template.setValidInstanceProperties(); as I have not got this informaiton fropm the typeDef

        return template;
    }
    public static boolean isTerm(String typeName) {
        return typeName.equals("GlossaryTerm");
    }
    public static boolean isCategory(String typeName) {
        return typeName.equals("GlossaryCategory");
    }
    public static boolean isGlossary(String typeName) {
        return typeName.equals("Glossary");
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
     * Note that we should only return the icons that are effective - by checking the effective From and To dates against the current time
     * @param userId userid under which to issue to the get of the related media
     * @param oMRSAPIHelper interface to  OMRS
     * @param guid to get associated icons from
     * @return response with Set of IconSummary objects or an Exception response.
     */
    public static SubjectAreaOMASAPIResponse  getIconSummarySet(String userId, OMRSAPIHelper oMRSAPIHelper, String guid)
    {
        // if there are no icons then return an empty set

        //TODO implement icon logic
        SubjectAreaOMASAPIResponse response;
        Set<IconSummary> icons = new HashSet<>();
        response = new IconSummarySetResponse(icons);
        return response;

    }

    /**
     * Get a Term's icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     * @param restAPIName rest API Name
     * @param userId userid under which to issue to the get of the related media
     * @param omrsapiHelper helper to access OMRS
     * @param termAnchorRelationship term glossary relationship
     * @param term supplied term
     * @return response object - glossary summary or an error
     */
    public static  SubjectAreaOMASAPIResponse  getGlossarySummaryForTerm(String restAPIName, String userId, OMRSAPIHelper omrsapiHelper, TermAnchor termAnchorRelationship, Term term)  {
        String glossaryGuid = termAnchorRelationship.getGlossaryGuid();
        SubjectAreaOMASAPIResponse response = omrsapiHelper.callOMRSGetEntityByGuid(restAPIName, userId,glossaryGuid);
        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
            EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
            EntityDetail glossaryEntity = entityDetailResponse.getEntityDetail();
            Glossary glossary = null;
            try {
                glossary = new GlossaryMapper(omrsapiHelper).mapEntityDetailToNode(glossaryEntity);

                GlossarySummary glossarySummary = extractGlossarySummaryFromGlossary(glossary,termAnchorRelationship);
                response = new GlossarySummaryResponse(glossarySummary);
                // TODO sort out icons
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        return response;
    }
    /**
     * Get a Categories icon summaries from related media relationships by issuing a call to omrs using the related media guid - which is at one end of the relationship.
     * @param restAPIName rest API Name
     * @param userId userid under which to issue to the get of the related media
     * @param omrsapiHelper helper to access OMRS
     * @param line glossary relationship
     * @return Glossary summary for Category
     */
    public static  SubjectAreaOMASAPIResponse  getGlossarySummaryForCategory(String restAPIName, String userId, OMRSAPIHelper omrsapiHelper, Line line)  {
        CategoryAnchor categoryAnchorRelationship = (CategoryAnchor) line;
        String glossaryGuid = categoryAnchorRelationship.getGlossaryGuid();
        SubjectAreaOMASAPIResponse response = omrsapiHelper.callOMRSGetEntityByGuid(restAPIName, userId, glossaryGuid);
        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
            EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
            EntityDetail glossaryEntity = entityDetailResponse.getEntityDetail();
            Glossary glossary = null;
            try {
                glossary = new GlossaryMapper(omrsapiHelper).mapEntityDetailToNode(glossaryEntity);

                GlossarySummary glossarySummary = extractGlossarySummaryFromGlossary(glossary,categoryAnchorRelationship);

                if (glossarySummary != null) {
                    response = new GlossarySummaryResponse(glossarySummary);
// TODO sort out icons
                }
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        return response;
    }
    /**
     * Get a summary of the parent category. The parent category is optional. We might validly have more than one parent category. this can occur if effectivity dates are being used
     * The parent category is the first relationship we find of the right type that is effective.
     * @param restAPIName rest API Name
     * @param userId userid that the requests are issued under
     * @param omrsapiHelper helper to access OMRS
     * @param lines set of Lines that are of the right type.
     * @return a parent category as a CategorySummary
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException  the supplied guid was not recognised
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws FunctionNotSupportedException   Function not supported
     */
    public static CategorySummary getParentCategorySummary(String restAPIName, String userId, OMRSAPIHelper omrsapiHelper,  Set<Line> lines) throws UserNotAuthorizedException, InvalidParameterException, UnrecognizedGUIDException, MetadataServerUncontactableException, FunctionNotSupportedException
    {
        CategorySummary parentCategorySummary =null;
        SubjectAreaOMASAPIResponse response = null;
        final Iterator<Line> iterator = lines.iterator();
        while (iterator.hasNext() && parentCategorySummary ==null)
        {
            CategoryHierarchyLink parentRelationship = (CategoryHierarchyLink) iterator.next();
            String parentCategoryGuid = parentRelationship.getSuperCategoryGuid();
            response = omrsapiHelper.callOMRSGetEntityByGuid(restAPIName, userId, parentCategoryGuid);
            if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                EntityDetailResponse parentCategoryEntityResponse  = (EntityDetailResponse)response;
                EntityDetail parentCategoryEntity = parentCategoryEntityResponse.getEntityDetail();
                Category category = (Category) new CategoryMapper(omrsapiHelper).mapEntityDetailToNode(parentCategoryEntity);
                parentCategorySummary = extractCategorySummaryFromCategory(category,parentRelationship);
                // TODO implement icon logic
            }
        }

        return parentCategorySummary;
    }

    /**
     * Convert a Category to a CategorySummary
     * @param category to convert
     * @param line
     * @return CategorySummary
     */
    private static CategorySummary extractCategorySummaryFromCategory(Category category,Line line )
    {
        CategorySummary categorySummary = new CategorySummary();
        extractNodeSummary(category, line, categorySummary);
        return categorySummary;
    }

    private static void extractNodeSummary(Node node, Line line, NodeSummary nodeSummary) {
        nodeSummary.setQualifiedName(node.getQualifiedName());
        nodeSummary.setName(node.getName());
        nodeSummary.setGuid(node.getSystemAttributes().getGUID());
        nodeSummary.setFromEffectivityTime(node.getEffectiveFromTime());
        nodeSummary.setToEffectivityTime(node.getEffectiveToTime());
        nodeSummary.setRelationshipguid(line.getGuid());
        nodeSummary.setFromRelationshipEffectivityTime(line.getEffectiveFromTime());
        nodeSummary.setToRelationshipEffectivityTime(line.getEffectiveToTime());
    }

    /**
     * extract iconSummary if this media is an icon
     * @param relatedMedia related media
     * @return iconSummaru or null
     */
    public static IconSummary extractIconSummaryFromRelatedMedia(EntityDetail relatedMedia)
    {
        IconSummary icon = null;
        // if (relatedMedia.getMediaUsage().contains(MediaUsage.Icon.getOrdinal()))
        {
            icon = new IconSummary();
            //TODO sort out icons - add a mapper ?
        }
        return icon;
    }
    /**
     * Extract glossarySummary
     * @param glossary the glossary that is to be summarised
     * @param line the line to the glossary, which feeds part of the node summary
     * @return Glossary Summary or null
     */
    public static GlossarySummary extractGlossarySummaryFromGlossary(Glossary glossary,Line line)
    {
        if (glossary ==null) return null;
        GlossarySummary glossarySummary = new GlossarySummary();
        extractNodeSummary(glossary,line, glossarySummary);
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
            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition();
            throw new InvalidParameterException(messageDefinition,
                    className,
                    methodName,
                    "Status",
                    Status.DELETED.name());
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
     * Convert the OMRS Lines to their equivalent OMAS objects and return them in a Response of type lines.
     * @param  omrsapiHelper OMRS API helper - used to construct the mappers
     * @param omrsRelationships - these are OMRS relationships
     * @return omasLines - these are the Lines (the OMAS relationships) that are exposed in the OMAS API - note the list in the response can be empty
     */
    public static  SubjectAreaOMASAPIResponse convertOMRSRelationshipsToOMASLines(OMRSAPIHelper omrsapiHelper, List<Relationship> omrsRelationships)
    {
        List<Line> omasLines = new ArrayList<>();
        for (Relationship omrsRelationship : omrsRelationships) {
            String omrsName = omrsRelationship.getType().getTypeDefName();
            Line omasLine = null;
            switch (omrsName) {
                case "TermHASARelationship":
                    omasLine = new TermHASARelationshipMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "RelatedTerm":
                    omasLine = new RelatedTermMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "Synonym":
                    omasLine = new SynonymMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "Antonym":
                    omasLine = new AntonymMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "Translation":
                    omasLine = new TranslationMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "ISARelationship":
                    omasLine = new ISARelationshipMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "PreferredTerm":
                    omasLine = new PreferredTermMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "TermISATypeOFRelationship":
                    omasLine = new TermISATypeOFRelationshipMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "ReplacementTerm":
                    omasLine = new ReplacementTermMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "TermTYPEDBYRelationship":
                    omasLine = new TermTYPEDBYRelationshipMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "UsedInContext":
                    omasLine = new UsedInContextMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                case "ValidValue":
                    omasLine = new ValidValueMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
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
                    omasLine = new SemanticAssignmentMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                // category to term
                case "TermCategorization":
                    omasLine = new TermCategorizationMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                // Term to glossary
                case "TermAnchor":
                    omasLine = new TermAnchorMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
                // Category to glossary
                case "CategoryAnchor":
                    omasLine = new CategoryAnchorMapper(omrsapiHelper).mapRelationshipToLine(omrsRelationship);
                    break;
            }
            if (omasLine != null) {
                omasLines.add(omasLine);
            }
        }
        return  new LinesResponse(omasLines);
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
    /**
     * create SystemAttributes instance from an insance header object
     * @param instanceHeader omrs instance header
     * @return SystemAttributes
     */
    public static SystemAttributes createSystemAttributes(InstanceHeader instanceHeader) {
        //set core attributes
        SystemAttributes systemAttributes = new SystemAttributes();

        InstanceStatus instanceStatus =  instanceHeader.getStatus();
        Status omas_status = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
        systemAttributes.setStatus(omas_status);

        systemAttributes.setCreatedBy(instanceHeader.getCreatedBy());
        systemAttributes.setUpdatedBy(instanceHeader.getUpdatedBy());
        systemAttributes.setCreateTime(instanceHeader.getCreateTime());
        systemAttributes.setUpdateTime(instanceHeader.getUpdateTime());
        systemAttributes.setVersion(instanceHeader.getVersion());
        systemAttributes.setGUID(instanceHeader.getGUID());
        return systemAttributes;
    }
    static public void populateSystemAttributesForInstanceAuditHeader(SystemAttributes systemAttributes, InstanceAuditHeader instanceAuditHeader) {
        if (systemAttributes != null) {
            if (systemAttributes.getCreatedBy() != null)
                instanceAuditHeader.setCreatedBy(systemAttributes.getCreatedBy());
            if (systemAttributes.getUpdatedBy() != null)
                instanceAuditHeader.setUpdatedBy(systemAttributes.getUpdatedBy());
            if (systemAttributes.getCreateTime() != null)
                instanceAuditHeader.setCreateTime(systemAttributes.getCreateTime());
            if (systemAttributes.getUpdateTime() != null)
                instanceAuditHeader.setUpdateTime(systemAttributes.getUpdateTime());
            if (systemAttributes.getVersion() != null)
                instanceAuditHeader.setVersion(systemAttributes.getVersion());
        }
    }
    /**
     * Set the String value into the InstanceProperties.
     * @param instanceProperties supplied instanceproperties
     * @param stringValue string value
     * @param propertyName property name.
     */
    public static void setStringPropertyInInstanceProperties(InstanceProperties instanceProperties, String stringValue, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(stringValue);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }
    /**
     * Set the Integer value into the InstanceProperties.
     * @param instanceProperties supplied instanceproperties
     * @param integerValue integer value
     * @param propertyName property name.
     */
    public static void setIntegerPropertyInInstanceProperties(InstanceProperties instanceProperties, Integer integerValue, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        primitivePropertyValue.setPrimitiveValue(integerValue);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }

    /**
     * set the Date value into the InstanceProperties
     * @param instanceProperties supplied instanceproperties
     * @param date Datew value
     * @param propertyName property name
     */
    public static void setDatePropertyInInstanceProperties(InstanceProperties instanceProperties,  Date date, String propertyName) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        Long timestamp = date.getTime();
        primitivePropertyValue.setPrimitiveValue(timestamp);
        instanceProperties.setProperty(propertyName, primitivePropertyValue);
    }
}