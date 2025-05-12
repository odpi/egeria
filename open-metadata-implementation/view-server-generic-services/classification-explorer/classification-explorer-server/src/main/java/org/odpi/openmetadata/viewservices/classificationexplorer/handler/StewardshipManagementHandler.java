/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.classificationexplorer.handler;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.mermaid.OpenMetadataMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.converters.MetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.MetadataRelationshipSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.RelatedMetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * StewardshipExchangeClient is the client for assigning relationships and classifications that help govern both metadata and its associated
 * resources.
 */
public class StewardshipManagementHandler
{
    private final OpenMetadataStoreHandler client;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    final protected MetadataElementSummaryConverter<MetadataElementSummary>               metadataElementSummaryConverter;
    final protected RelatedMetadataElementSummaryConverter<RelatedMetadataElementSummary> relatedMetadataElementSummaryConverter;
    final protected MetadataRelationshipSummaryConverter<MetadataRelationshipSummary>     metadataRelationshipSummaryConverter;
    private final AuditLog                 auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Create a new client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipManagementHandler(String   localServerName,
                                        String   serverName,
                                        String   serverPlatformURLRoot,
                                        AuditLog auditLog,
                                        String   accessServiceURLMarker,
                                        int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "StewardshipManagementHandler";

        this.client = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog = auditLog;

        String serviceName = ViewServiceDescription.CLASSIFICATION_EXPLORER.getViewServiceFullName();

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     serviceName,
                                                                                     serverName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   serviceName,
                                                                                                   serverName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               serviceName,
                                                                                               serverName);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getConfidenceClassifiedElements(String              userId,
                                                                        boolean             returnSpecificLevel,
                                                                        int                 levelIdentifier,
                                                                        String              openMetadataTypeName,
                                                                        List<ElementStatus> limitResultsByStatus,
                                                                        Date                asOfTime,
                                                                        String              sequencingProperty,
                                                                        SequencingOrder     sequencingOrder,
                                                                        int                 startFrom,
                                                                        int                 pageSize,
                                                                        Date                effectiveTime,
                                                                        boolean             forLineage,
                                                                        boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        final String methodName = "getConfidenceClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        openMetadataTypeName,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getCriticalityClassifiedElements(String              userId,
                                                                         boolean             returnSpecificLevel,
                                                                         int                 levelIdentifier,
                                                                         String              openMetadataTypeName,
                                                                         List<ElementStatus> limitResultsByStatus,
                                                                         Date                asOfTime,
                                                                         String              sequencingProperty,
                                                                         SequencingOrder     sequencingOrder,
                                                                         int                 startFrom,
                                                                         int                 pageSize,
                                                                         Date                effectiveTime,
                                                                         boolean             forLineage,
                                                                         boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        final String methodName = "getCriticalityClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        openMetadataTypeName,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getConfidentialityClassifiedElements(String              userId,
                                                                             boolean             returnSpecificLevel,
                                                                             int                 levelIdentifier,
                                                                             String              openMetadataTypeName,
                                                                             List<ElementStatus> limitResultsByStatus,
                                                                             Date                asOfTime,
                                                                             String              sequencingProperty,
                                                                             SequencingOrder     sequencingOrder,
                                                                             int                 startFrom,
                                                                             int                 pageSize,
                                                                             Date                effectiveTime,
                                                                             boolean             forLineage,
                                                                             boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        final String methodName = "getConfidentialityClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        openMetadataTypeName,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getRetentionClassifiedElements(String              userId,
                                                                       boolean             returnSpecificLevel,
                                                                       int                 levelIdentifier,
                                                                       String              openMetadataTypeName,
                                                                       List<ElementStatus> limitResultsByStatus,
                                                                       Date                asOfTime,
                                                                       String              sequencingProperty,
                                                                       SequencingOrder     sequencingOrder,
                                                                       int                 startFrom,
                                                                       int                 pageSize,
                                                                       Date                effectiveTime,
                                                                       boolean             forLineage,
                                                                       boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        final String methodName = "getRetentionClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        openMetadataTypeName,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param classificationName required classification name
     * @param identifierPropertyName name of property used to
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<MetadataElementSummary> getGovernanceDataClassifiedElements(String              userId,
                                                                             String              classificationName,
                                                                             String              identifierPropertyName,
                                                                             boolean             returnSpecificLevel,
                                                                             int                 levelIdentifier,
                                                                             String              openMetadataTypeName,
                                                                             List<ElementStatus> limitResultsByStatus,
                                                                             Date                asOfTime,
                                                                             String              sequencingProperty,
                                                                             SequencingOrder     sequencingOrder,
                                                                             int                 startFrom,
                                                                             int                 pageSize,
                                                                             Date                effectiveTime,
                                                                             boolean             forLineage,
                                                                             boolean             forDuplicateProcessing,
                                                                             String              methodName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        if (returnSpecificLevel)
        {
            List<PropertyCondition> propertyConditions = propertyHelper.addIntProperty(null,
                                                                                       identifierPropertyName,
                                                                                       levelIdentifier,
                                                                                       PropertyComparisonOperator.EQ);

            List<OpenMetadataElement> matchingElements = client.findMetadataElements(userId,
                                                                                     openMetadataTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     limitResultsByStatus,
                                                                                     asOfTime,
                                                                                     getSearchClassifications(propertyConditions, classificationName),
                                                                                     sequencingProperty,
                                                                                     sequencingOrder,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     startFrom,
                                                                                     pageSize);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                    openMetadataTypeName,
                                                    limitResultsByStatus,
                                                    asOfTime,
                                                    sequencingProperty,
                                                    sequencingOrder,
                                                    startFrom,
                                                    pageSize,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
    }


    /**
     * Construct the classification search properties for locating specific properties in the named classification.
     *
     * @param propertyConditions properties to match
     * @param classificationName name of the classification
     * @return search classification structure
     */
    private SearchClassifications getSearchClassifications(List<PropertyCondition> propertyConditions,
                                                           String                  classificationName)
    {
        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition classificationCondition = new ClassificationCondition();
        SearchProperties        searchProperties        = new SearchProperties();

        searchProperties.setConditions(propertyConditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        classificationCondition.setName(classificationName);
        classificationCondition.setSearchProperties(searchProperties);

        classificationConditions.add(classificationCondition);

        SearchClassifications searchClassifications = new SearchClassifications();
        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return searchClassifications;
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param securityLabels       description of the query
     * @param securityProperties    description of the query
     * @param accessGroups       description of the query
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getSecurityTaggedElements(String                    userId,
                                                                  List<String>              securityLabels,
                                                                  Map<String, Object>       securityProperties,
                                                                  Map<String, List<String>> accessGroups,
                                                                  String                    openMetadataTypeName,
                                                                  List<ElementStatus>       limitResultsByStatus,
                                                                  Date                      asOfTime,
                                                                  String                    sequencingProperty,
                                                                  SequencingOrder           sequencingOrder,
                                                                  int                       startFrom,
                                                                  int                       pageSize,
                                                                  Date                      effectiveTime,
                                                                  boolean                   forLineage,
                                                                  boolean                   forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        final String methodName = "getSecurityTaggedElements";

        List<PropertyCondition> propertyConditions = propertyHelper.addStringArrayToSearchCondition(null,
                                                                                                    OpenMetadataProperty.SECURITY_LABELS.name,
                                                                                                    securityLabels,
                                                                                                    PropertyComparisonOperator.EQ);
        propertyConditions = propertyHelper.addObjectMapToSearchCondition(propertyConditions,
                                                                          OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                                          securityProperties,
                                                                          PropertyComparisonOperator.EQ);

        propertyConditions = propertyHelper.addListStringMapToSearchCondition(propertyConditions,
                                                                              OpenMetadataProperty.ACCESS_GROUPS.name,
                                                                              accessGroups,
                                                                              PropertyComparisonOperator.EQ);

        if (propertyConditions != null)
        {
            List<OpenMetadataElement> matchingElements = client.findMetadataElements(userId,
                                                                                     openMetadataTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     limitResultsByStatus,
                                                                                     asOfTime,
                                                                                     getSearchClassifications(propertyConditions, OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName),
                                                                                     sequencingProperty,
                                                                                     sequencingOrder,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     startFrom,
                                                                                     pageSize);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                    openMetadataTypeName,
                                                    limitResultsByStatus,
                                                    asOfTime,
                                                    sequencingProperty,
                                                    sequencingOrder,
                                                    startFrom,
                                                    pageSize,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param owner unique identifier for the owner
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getOwnersElements(String              userId,
                                                          String              owner,
                                                          String              openMetadataTypeName,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          String              sequencingProperty,
                                                          SequencingOrder     sequencingOrder,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          Date                effectiveTime,
                                                          boolean             forLineage,
                                                          boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getOwnersElements";

        final String nameParameterName = "owner";

        invalidParameterHandler.validateGUID(owner, nameParameterName, methodName);

        return this.getElementsByClassification(userId,
                                                OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                                owner,
                                                Collections.singletonList(OpenMetadataProperty.OWNER.name),
                                                openMetadataTypeName,
                                                limitResultsByStatus,
                                                asOfTime,
                                                sequencingProperty,
                                                sequencingOrder,
                                                startFrom,
                                                pageSize,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param userId calling user
     * @param organizationGUID values to search on - null means any value
     * @param businessCapabilityGUID values to search on - null means any value
     * @param otherOriginValues values to search on - null means any value
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getAssetsByOrigin(String              userId,
                                                          String              organizationGUID,
                                                          String              businessCapabilityGUID,
                                                          Map<String, String> otherOriginValues,
                                                          String              openMetadataTypeName,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          String              sequencingProperty,
                                                          SequencingOrder     sequencingOrder,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          Date                effectiveTime,
                                                          boolean             forLineage,
                                                          boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        final String methodName = "getAssetsByOrigin";

        List<PropertyCondition> propertyConditions = propertyHelper.addStringProperty(null,
                                                                                      OpenMetadataProperty.ORGANIZATION.name,
                                                                                      organizationGUID,
                                                                                      PropertyComparisonOperator.EQ);
        propertyConditions = propertyHelper.addStringProperty(propertyConditions,
                                                              OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                              businessCapabilityGUID,
                                                              PropertyComparisonOperator.EQ);
        propertyConditions = propertyHelper.addStringMapToSearchCondition(propertyConditions,
                                                                          OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                                          otherOriginValues,
                                                                          PropertyComparisonOperator.EQ);

        if (propertyConditions != null)
        {
            List<OpenMetadataElement> matchingElements = client.findMetadataElements(userId,
                                                                                     openMetadataTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     limitResultsByStatus,
                                                                                     asOfTime,
                                                                                     getSearchClassifications(propertyConditions, OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION.typeName),
                                                                                     sequencingProperty,
                                                                                     sequencingOrder,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     startFrom,
                                                                                     pageSize);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION.typeName,
                                                    openMetadataTypeName,
                                                    limitResultsByStatus,
                                                    asOfTime,
                                                    sequencingProperty,
                                                    sequencingOrder,
                                                    startFrom,
                                                    pageSize,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
    }



    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getMembersOfSubjectArea(String              userId,
                                                                String              subjectAreaName,
                                                                String              openMetadataTypeName,
                                                                List<ElementStatus> limitResultsByStatus,
                                                                Date                asOfTime,
                                                                String              sequencingProperty,
                                                                SequencingOrder     sequencingOrder,
                                                                int                 startFrom,
                                                                int                 pageSize,
                                                                Date                effectiveTime,
                                                                boolean             forLineage,
                                                                boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException

    {
        final String methodName = "getMembersOfSubjectArea";

        final String nameParameterName = "subjectAreaName";

        invalidParameterHandler.validateGUID(subjectAreaName, nameParameterName, methodName);

        return this.getElementsByClassification(userId,
                                                OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                                subjectAreaName,
                                                Collections.singletonList(OpenMetadataProperty.NAME.name),
                                                openMetadataTypeName,
                                                limitResultsByStatus,
                                                asOfTime,
                                                sequencingProperty,
                                                sequencingOrder,
                                                startFrom,
                                                pageSize,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }



    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param expression       Optional relationship property  to match
     * @param description       Optional relationship property  to match
     * @param status       Optional relationship property  to match
     * @param returnSpecificConfidence  should it match on the confidence value
     * @param confidence       Optional relationship property  to match
     * @param createdBy       Optional relationship property  to match
     * @param steward       Optional relationship property  to match
     * @param source       Optional relationship property  to match
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getMeanings(String                       userId,
                                                         String                       elementGUID,
                                                         String                       expression,
                                                         String                       description,
                                                         GlossaryTermAssignmentStatus status,
                                                         boolean                      returnSpecificConfidence,
                                                         int                          confidence,
                                                         String                       createdBy,
                                                         String                       steward,
                                                         String                       source,
                                                         String                       openMetadataTypeName,
                                                         List<ElementStatus>          limitResultsByStatus,
                                                         Date                         asOfTime,
                                                         String                       sequencingProperty,
                                                         SequencingOrder              sequencingOrder,
                                                         int                          startFrom,
                                                         int                          pageSize,
                                                         Date                         effectiveTime,
                                                         boolean                      forLineage,
                                                         boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException

    {
        final String methodName = "getMeanings";

        return this.getSemanticRelationships(userId,
                                             elementGUID,
                                             1,
                                             expression,
                                             description,
                                             status,
                                             returnSpecificConfidence,
                                             confidence,
                                             createdBy,
                                             steward,
                                             source,
                                             openMetadataTypeName,
                                             limitResultsByStatus,
                                             asOfTime,
                                             sequencingProperty,
                                             sequencingOrder,
                                             startFrom,
                                             pageSize,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startingAtEnd which end?
     * @param expression       Optional relationship property  to match
     * @param description       Optional relationship property  to match
     * @param status       Optional relationship property  to match
     * @param returnSpecificConfidence  should it match on the confidence value
     * @param confidence       Optional relationship property  to match
     * @param createdBy       Optional relationship property  to match
     * @param steward       Optional relationship property  to match
     * @param source       Optional relationship property  to match
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private RelatedMetadataElementSummaryList getSemanticRelationships(String                       userId,
                                                                       String                       elementGUID,
                                                                       int                          startingAtEnd,
                                                                       String                       expression,
                                                                       String                       description,
                                                                       GlossaryTermAssignmentStatus status,
                                                                       boolean                      returnSpecificConfidence,
                                                                       int                          confidence,
                                                                       String                       createdBy,
                                                                       String                       steward,
                                                                       String                       source,
                                                                       String                       openMetadataTypeName,
                                                                       List<ElementStatus>          limitResultsByStatus,
                                                                       Date                         asOfTime,
                                                                       String                       sequencingProperty,
                                                                       SequencingOrder              sequencingOrder,
                                                                       int                          startFrom,
                                                                       int                          pageSize,
                                                                       Date                         effectiveTime,
                                                                       boolean                      forLineage,
                                                                       boolean                      forDuplicateProcessing,
                                                                       String                       methodName) throws InvalidParameterException,
                                                                                                                                   UserNotAuthorizedException,
                                                                                                                                   PropertyServerException

    {
        RelatedMetadataElementList relatedMetadataElements = client.getRelatedMetadataElements(userId,
                                                                                               elementGUID,
                                                                                               startingAtEnd,
                                                                                               OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                               limitResultsByStatus,
                                                                                               asOfTime,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               effectiveTime,
                                                                                               startFrom,
                                                                                               pageSize);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            if (openMetadataTypeName == null)
            {
                matchedElements = relatedMetadataElements.getElementList();
            }
            else
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), openMetadataTypeName))
                        {
                            matchedElements.add(relatedMetadataElement);
                        }
                    }
                }
            }

            matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.EXPRESSION.name, expression);
            matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.DESCRIPTION.name, description);
            matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.CREATED_BY.name, createdBy);
            matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.STEWARD.name, steward);
            matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.SOURCE.name, source);
            if (returnSpecificConfidence)
            {
                matchedElements = this.matchElements(matchedElements, OpenMetadataProperty.CONFIDENCE.name, confidence);
            }
            matchedElements = this.matchElements(matchedElements, status);

            RelatedMetadataElementSummaryList summaryList = new RelatedMetadataElementSummaryList();

            summaryList.setElementList(relatedMetadataElementSummaryConverter.getNewBeans(RelatedMetadataElementSummary.class,
                                                                                          matchedElements,
                                                                                          methodName));

            if (! matchedElements.isEmpty())
            {
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(elementGUID, matchedElements);
                summaryList.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return summaryList;
        }

        return null;
    }


    /**
     * Return the list of related elements that have a matching property name.
     *
     * @param relatedMetadataElements starting list of elements
     * @param propertyName name of property
     * @param propertyValue value to match on
     * @return matched elements
     */
    private List<RelatedMetadataElement> matchElements(List<RelatedMetadataElement> relatedMetadataElements,
                                                       String                       propertyName,
                                                       String                       propertyValue)
    {
        if ((relatedMetadataElements == null) || (relatedMetadataElements.isEmpty()) || (propertyValue == null))
        {
            return relatedMetadataElements;
        }
        else // match on a specific value
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();

                    if (relationshipProperties != null)
                    {
                        PropertyValue relationshipPropertyValue = relationshipProperties.getPropertyValue(propertyName);

                        if (relationshipPropertyValue != null)
                        {
                            if (relationshipPropertyValue.valueAsString().equals(propertyValue))
                            {
                                matchedElements.add(relatedMetadataElement);
                                break;
                            }
                        }
                    }
                }
            }

            return matchedElements;
        }
    }


    /**
     * Return the list of related elements that have a matching property name.
     *
     * @param relatedMetadataElements starting list of elements
     * @param propertyName name of property
     * @param propertyValue value to match on
     * @return matched elements
     */
    private List<RelatedMetadataElement> matchElements(List<RelatedMetadataElement> relatedMetadataElements,
                                                       String                       propertyName,
                                                       int                          propertyValue)
    {
        if ((relatedMetadataElements == null) || (relatedMetadataElements.isEmpty()))
        {
            return relatedMetadataElements;
        }
        else // match on a specific value
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();

                    if (relationshipProperties != null)
                    {
                        PropertyValue relationshipPropertyValue = relationshipProperties.getPropertyValue(propertyName);

                        if (relationshipPropertyValue != null)
                        {
                            if (relationshipPropertyValue.valueAsString().equals(Integer.toString(propertyValue)))
                            {
                                matchedElements.add(relatedMetadataElement);
                                break;
                            }
                        }
                    }
                }
            }

            return matchedElements;
        }
    }


    /**
     * Return the list of related elements that have a matching property name.
     *
     * @param relatedMetadataElements starting list of elements
     * @param status status of semantic assignment
     * @return matched elements
     */
    private List<RelatedMetadataElement> matchElements(List<RelatedMetadataElement> relatedMetadataElements,
                                                       GlossaryTermAssignmentStatus status)
    {
        if ((relatedMetadataElements == null) || (relatedMetadataElements.isEmpty()) || (status == null))
        {
            return relatedMetadataElements;
        }
        else // match on a specific value
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();

                    if (relationshipProperties != null)
                    {
                        PropertyValue relationshipPropertyValue = relationshipProperties.getPropertyValue(OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name);

                        if (relationshipPropertyValue instanceof EnumTypePropertyValue enumTypePropertyValue)
                        {
                            if (enumTypePropertyValue.getSymbolicName().equals(status.getName()))
                            {
                                matchedElements.add(relatedMetadataElement);
                                break;
                            }
                        }
                    }
                }
            }

            return matchedElements;
        }
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param expression       Optional relationship property  to match
     * @param description       Optional relationship property  to match
     * @param status       Optional relationship property  to match
     * @param returnSpecificConfidence  should it match on the confidence value
     * @param confidence       Optional relationship property  to match
     * @param createdBy       Optional relationship property  to match
     * @param steward       Optional relationship property  to match
     * @param source       Optional relationship property  to match
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSemanticAssignees(String                       userId,
                                                                  String                       glossaryTermGUID,
                                                                  String                       expression,
                                                                  String                       description,
                                                                  GlossaryTermAssignmentStatus status,
                                                                  boolean                      returnSpecificConfidence,
                                                                  int                          confidence,
                                                                  String                       createdBy,
                                                                  String                       steward,
                                                                  String                       source,
                                                                  String                       openMetadataTypeName,
                                                                  List<ElementStatus>          limitResultsByStatus,
                                                                  Date                         asOfTime,
                                                                  String                       sequencingProperty,
                                                                  SequencingOrder              sequencingOrder,
                                                                  int                          startFrom,
                                                                  int                          pageSize,
                                                                  Date                         effectiveTime,
                                                                  boolean                      forLineage,
                                                                  boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException

    {
        final String methodName = "getSemanticAssignees";

        return this.getSemanticRelationships(userId,
                                             glossaryTermGUID,
                                             2,
                                             expression,
                                             description,
                                             status,
                                             returnSpecificConfidence,
                                             confidence,
                                             createdBy,
                                             steward,
                                             source,
                                             openMetadataTypeName,
                                             limitResultsByStatus,
                                             asOfTime,
                                             sequencingProperty,
                                             sequencingOrder,
                                             startFrom,
                                             pageSize,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedByDefinitions(String              userId,
                                                                      String              elementGUID,
                                                                      String              openMetadataTypeName,
                                                                      List<ElementStatus> limitResultsByStatus,
                                                                      Date                asOfTime,
                                                                      String              sequencingProperty,
                                                                      SequencingOrder     sequencingOrder,
                                                                      int                 startFrom,
                                                                      int                 pageSize,
                                                                      Date                effectiveTime,
                                                                      boolean             forLineage,
                                                                      boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException

    {
        final String methodName = "getGovernedByDefinitions";

        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       openMetadataTypeName,
                                       limitResultsByStatus,
                                       asOfTime,
                                       sequencingProperty,
                                       sequencingOrder,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedElements(String              userId,
                                                                 String              governanceDefinitionGUID,
                                                                 String              openMetadataTypeName,
                                                                 List<ElementStatus> limitResultsByStatus,
                                                                 Date                asOfTime,
                                                                 String              sequencingProperty,
                                                                 SequencingOrder     sequencingOrder,
                                                                 int                 startFrom,
                                                                 int                 pageSize,
                                                                 Date                effectiveTime,
                                                                 boolean             forLineage,
                                                                 boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException

    {
        final String methodName = "getGovernedElements";

        final String elementGUIDParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateGUID(governanceDefinitionGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       governanceDefinitionGUID,
                                       OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       openMetadataTypeName,
                                       limitResultsByStatus,
                                       asOfTime,
                                       sequencingProperty,
                                       sequencingOrder,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSourceElements(String              userId,
                                                               String              elementGUID,
                                                               String              openMetadataTypeName,
                                                               List<ElementStatus> limitResultsByStatus,
                                                               Date                asOfTime,
                                                               String              sequencingProperty,
                                                               SequencingOrder     sequencingOrder,
                                                               int                 startFrom,
                                                               int                 pageSize,
                                                               Date                effectiveTime,
                                                               boolean             forLineage,
                                                               boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException

    {
        final String methodName = "getSourceElements";

        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       openMetadataTypeName,
                                       limitResultsByStatus,
                                       asOfTime,
                                       sequencingProperty,
                                       sequencingOrder,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getElementsSourcedFrom(String              userId,
                                                                    String              elementGUID,
                                                                    String              openMetadataTypeName,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    Date                effectiveTime,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException

    {
        final String methodName = "getElementsSourcedFrom";

        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       openMetadataTypeName,
                                       limitResultsByStatus,
                                       asOfTime,
                                       sequencingProperty,
                                       sequencingOrder,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       methodName);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the metadata element
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummary getMetadataElementByGUID(String  userId,
                                                           String  elementGUID,
                                                           Date    asOfTime,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getMetadataElementByGUID";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = client.getMetadataElementByGUID(userId, elementGUID, forLineage, forDuplicateProcessing, asOfTime, effectiveTime);

        return metadataElementSummaryConverter.getNewBean(MetadataElementSummary.class,
                                                          openMetadataElement,
                                                          methodName);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId calling user
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummary getMetadataElementByUniqueName(String  userId,
                                                                 String  uniqueName,
                                                                 String  uniquePropertyName,
                                                                 Date    asOfTime,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getMetadataElementByUniqueName";
        final String elementGUIDParameterName = "uniqueName";

        invalidParameterHandler.validateName(uniqueName, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = client.getMetadataElementByUniqueName(userId, uniqueName, uniquePropertyName, forLineage, forDuplicateProcessing, asOfTime, effectiveTime);

        return metadataElementSummaryConverter.getNewBean(MetadataElementSummary.class,
                                                          openMetadataElement,
                                                          methodName);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId calling user
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String getMetadataElementGUIDByUniqueName(String  userId,
                                                     String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    asOfTime,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return client.getMetadataElementGUIDByUniqueName(userId, uniqueName, uniquePropertyName, forLineage, forDuplicateProcessing, asOfTime, effectiveTime);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElements(String              userId,
                                                    String              openMetadataTypeName,
                                                    List<ElementStatus> limitResultsByStatus,
                                                    Date                asOfTime,
                                                    String              sequencingProperty,
                                                    SequencingOrder     sequencingOrder,
                                                    int                 startFrom,
                                                    int                 pageSize,
                                                    Date                effectiveTime,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getElements";

        List<OpenMetadataElement> openMetadataElements  = client.findMetadataElements(userId,
                                                                                      openMetadataTypeName,
                                                                                      null,
                                                                                      null,
                                                                                      limitResultsByStatus,
                                                                                      asOfTime,
                                                                                      null,
                                                                                      sequencingProperty,
                                                                                      sequencingOrder,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      startFrom,
                                                                                      pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByPropertyValue(String              userId,
                                                                   String              propertyValue,
                                                                   List<String>        propertyNames,
                                                                   String              openMetadataTypeName,
                                                                   List<ElementStatus> limitResultsByStatus,
                                                                   Date                asOfTime,
                                                                   String              sequencingProperty,
                                                                   SequencingOrder     sequencingOrder,
                                                                   int                 startFrom,
                                                                   int                 pageSize,
                                                                   Date                effectiveTime,
                                                                   boolean             forLineage,
                                                                   boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName = "getElementsByPropertyValue";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = client.getMetadataElementsByPropertyValue(userId,
                                                                                                   openMetadataTypeName,
                                                                                                   null,
                                                                                                   propertyNames,
                                                                                                   propertyValue,
                                                                                                   limitResultsByStatus,
                                                                                                   asOfTime,
                                                                                                   sequencingProperty,
                                                                                                   sequencingOrder,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   effectiveTime,
                                                                                                   startFrom,
                                                                                                   pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> findElementsByPropertyValue(String              userId,
                                                                    String              propertyValue,
                                                                    List<String>        propertyNames,
                                                                    String              openMetadataTypeName,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    Date                effectiveTime,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String methodName = "findElementsByPropertyValue";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = client.findMetadataElementsByPropertyValue(userId,
                                                                                                    openMetadataTypeName,
                                                                                                    null,
                                                                                                    propertyNames,
                                                                                                    propertyValue,
                                                                                                    limitResultsByStatus,
                                                                                                    asOfTime,
                                                                                                    sequencingProperty,
                                                                                                    sequencingOrder,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing,
                                                                                                    effectiveTime,
                                                                                                    startFrom,
                                                                                                    pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }



    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByClassification(String              userId,
                                                                    String              classificationName,
                                                                    String              openMetadataTypeName,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    Date                effectiveTime,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String methodName                 = "getElementsByClassification";
        final String classificationNameProperty = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);

        List<OpenMetadataElement> elements = client.getMetadataElementsByClassification(userId,
                                                                                        openMetadataTypeName,
                                                                                        null,
                                                                                        classificationName,
                                                                                        limitResultsByStatus,
                                                                                        asOfTime,
                                                                                        sequencingProperty,
                                                                                        sequencingOrder,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        startFrom,
                                                                                        pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByClassification(String              userId,
                                                                    String              classificationName,
                                                                    String              propertyValue,
                                                                    List<String>        propertyNames,
                                                                    String              openMetadataTypeName,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    Date                effectiveTime,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing,
                                                                    String              methodName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> elements = client.getMetadataElementsByClassificationPropertyValue(userId,
                                                                                                     openMetadataTypeName,
                                                                                                     null,
                                                                                                     classificationName,
                                                                                                     propertyNames,
                                                                                                     propertyValue,
                                                                                                     limitResultsByStatus,
                                                                                                     asOfTime,
                                                                                                     sequencingProperty,
                                                                                                     sequencingOrder,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveTime,
                                                                                                     startFrom,
                                                                                                     pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> findElementsByClassificationWithPropertyValue(String              userId,
                                                                                      String              classificationName,
                                                                                      String              propertyValue,
                                                                                      List<String>        propertyNames,
                                                                                      String              openMetadataTypeName,
                                                                                      List<ElementStatus> limitResultsByStatus,
                                                                                      Date                asOfTime,
                                                                                      String              sequencingProperty,
                                                                                      SequencingOrder     sequencingOrder,
                                                                                      int                 startFrom,
                                                                                      int                 pageSize,
                                                                                      Date                effectiveTime,
                                                                                      boolean             forLineage,
                                                                                      boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         PropertyServerException
    {
        final String methodName = "findElementsByClassificationWithPropertyValue";

        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";

        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = client.findMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                  openMetadataTypeName,
                                                                                                                  null,
                                                                                                                  classificationName,
                                                                                                                  propertyNames,
                                                                                                                  propertyValue,
                                                                                                                  limitResultsByStatus,
                                                                                                                  asOfTime,
                                                                                                                  sequencingProperty,
                                                                                                                  sequencingOrder,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  effectiveTime,
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the requested value
     * found in one of the relationship's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getRelatedElements(String              userId,
                                                                String              elementGUID,
                                                                String              relationshipTypeName,
                                                                int                 startingAtEnd,
                                                                String              propertyValue,
                                                                List<String>        propertyNames,
                                                                String              openMetadataTypeName,
                                                                List<ElementStatus> limitResultsByStatus,
                                                                Date                asOfTime,
                                                                String              sequencingProperty,
                                                                SequencingOrder     sequencingOrder,
                                                                int                 startFrom,
                                                                int                 pageSize,
                                                                Date                effectiveTime,
                                                                boolean             forLineage,
                                                                boolean             forDuplicateProcessing,
                                                                String              methodName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        RelatedMetadataElementList relatedMetadataElements = client.getRelatedMetadataElements(userId,
                                                                                               elementGUID,
                                                                                               startingAtEnd,
                                                                                               relationshipTypeName,
                                                                                               limitResultsByStatus,
                                                                                               asOfTime,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               effectiveTime,
                                                                                               startFrom,
                                                                                               pageSize);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((openMetadataTypeName == null) || (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), openMetadataTypeName)))
                    {
                        if (propertyValue == null)
                        {
                            matchedElements.add(relatedMetadataElement);
                        }
                        else // match on a specific value
                        {
                            ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();

                            if (relationshipProperties != null)
                            {
                                if (propertyNames != null)
                                {
                                    for (String propertyName : propertyNames)
                                    {
                                        if (propertyName != null)
                                        {
                                            PropertyValue relationshipPropertyValue = relationshipProperties.getPropertyValue(propertyName);

                                            if (relationshipPropertyValue != null)
                                            {
                                                if (relationshipPropertyValue.valueAsString().equals(propertyValue))
                                                {
                                                    matchedElements.add(relatedMetadataElement);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else // match on any value
                                {
                                    for (PropertyValue relationshipPropertyValue : relationshipProperties.getPropertyValueMap().values())
                                    {
                                        if (relationshipPropertyValue != null)
                                        {
                                            if (relationshipPropertyValue.valueAsString().equals(propertyValue))
                                            {
                                                matchedElements.add(relatedMetadataElement);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            RelatedMetadataElementSummaryList summaryList = new RelatedMetadataElementSummaryList();

            summaryList.setElementList(relatedMetadataElementSummaryConverter.getNewBeans(RelatedMetadataElementSummary.class,
                                                                                          matchedElements,
                                                                                          methodName));

            if (! matchedElements.isEmpty())
            {
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(elementGUID, matchedElements);
                summaryList.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return summaryList;
        }

        return null;
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must be contained in one of the properties specified (or any property if no property names are specified).
     * An open metadata type name may be supplied to restrict the linked elements that are matched.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param openMetadataTypeName optional type for the resulting elements
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList findRelatedElementsWithPropertyValue(String              userId,
                                                                                  String              elementGUID,
                                                                                  String              relationshipTypeName,
                                                                                  int                 startingAtEnd,
                                                                                  String              propertyValue,
                                                                                  List<String>        propertyNames,
                                                                                  String              openMetadataTypeName,
                                                                                  List<ElementStatus> limitResultsByStatus,
                                                                                  Date                asOfTime,
                                                                                  String              sequencingProperty,
                                                                                  SequencingOrder     sequencingOrder,
                                                                                  int                 startFrom,
                                                                                  int                 pageSize,
                                                                                  Date                effectiveTime,
                                                                                  boolean             forLineage,
                                                                                  boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     PropertyServerException
    {
        final String methodName = "findRelatedElementsWithPropertyValue";
        final String propertyValueProperty = "propertyValue";

        invalidParameterHandler.validateObject(propertyValue, propertyValueProperty, methodName);

        RelatedMetadataElementList relatedMetadataElements = client.getRelatedMetadataElements(userId,
                                                                                               elementGUID,
                                                                                               startingAtEnd,
                                                                                               relationshipTypeName,
                                                                                               limitResultsByStatus,
                                                                                               asOfTime,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               effectiveTime,
                                                                                               startFrom,
                                                                                               pageSize);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((openMetadataTypeName == null) || (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), openMetadataTypeName)))
                    {
                        ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();

                        if (relationshipProperties != null)
                        {
                            if (propertyNames != null)
                            {
                                for (String propertyName : propertyNames)
                                {
                                    if (propertyName != null)
                                    {
                                        PropertyValue relationshipPropertyValue = relationshipProperties.getPropertyValue(propertyName);

                                        if (relationshipPropertyValue != null)
                                        {
                                            if (relationshipPropertyValue.valueAsString().contains(propertyValue))
                                            {
                                                matchedElements.add(relatedMetadataElement);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                for (PropertyValue relationshipPropertyValue : relationshipProperties.getPropertyValueMap().values())
                                {
                                    if (relationshipPropertyValue != null)
                                    {
                                        if (relationshipPropertyValue.valueAsString().contains(propertyValue))
                                        {
                                            matchedElements.add(relatedMetadataElement);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            RelatedMetadataElementSummaryList summaryList = new RelatedMetadataElementSummaryList();

            summaryList.setElementList(relatedMetadataElementSummaryConverter.getNewBeans(RelatedMetadataElementSummary.class,
                                                                                          matchedElements,
                                                                                          methodName));

            if (! matchedElements.isEmpty())
            {
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(elementGUID, matchedElements);
                summaryList.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return summaryList;
        }

        return null;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName  calling method
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummaryList   getRelationships(String              userId,
                                                              String              relationshipTypeName,
                                                              String              propertyValue,
                                                              List<String>        propertyNames,
                                                              List<ElementStatus> limitResultsByStatus,
                                                              Date                asOfTime,
                                                              String              sequencingProperty,
                                                              SequencingOrder     sequencingOrder,
                                                              int                 startFrom,
                                                              int                 pageSize,
                                                              Date                effectiveTime,
                                                              boolean             forLineage,
                                                              boolean             forDuplicateProcessing,
                                                              String              methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        OpenMetadataRelationshipList relationshipList = client.findRelationshipsBetweenMetadataElements(userId,
                                                                                                        relationshipTypeName,
                                                                                                        propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                                                                                        limitResultsByStatus,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        startFrom,
                                                                                                        pageSize);

        if (relationshipList != null)
        {
            MetadataRelationshipSummaryList summaryList = new MetadataRelationshipSummaryList();

            summaryList.setElementList(metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                                        relationshipList,
                                                                                        methodName));

            summaryList.setMermaidGraph(relationshipList.getMermaidGraph());

            return summaryList;
        }

        return null;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummaryList findRelationshipsWithPropertyValue(String              userId,
                                                                              String              relationshipTypeName,
                                                                              String              propertyValue,
                                                                              List<String>        propertyNames,
                                                                              List<ElementStatus> limitResultsByStatus,
                                                                              Date                asOfTime,
                                                                              String              sequencingProperty,
                                                                              SequencingOrder     sequencingOrder,
                                                                              int                 startFrom,
                                                                              int                 pageSize,
                                                                              Date                effectiveTime,
                                                                              boolean             forLineage,
                                                                              boolean             forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException
    {
        final String methodName = "findRelationshipsWithPropertyValue";

        OpenMetadataRelationshipList relationshipList = client.findRelationshipsBetweenMetadataElements(userId,
                                                                                                        relationshipTypeName,
                                                                                                        propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                                                                                        limitResultsByStatus,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        startFrom,
                                                                                                        pageSize);

        if (relationshipList != null)
        {
            MetadataRelationshipSummaryList summaryList = new MetadataRelationshipSummaryList();

            summaryList.setElementList(metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                                        relationshipList,
                                                                                        methodName));

            summaryList.setMermaidGraph(relationshipList.getMermaidGraph());

            return summaryList;
        }

        return null;
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.
     * It may be an element (entity) or a relationship between elements.
     *
     * @param userId  name of the server instance to connect to
     * @param guid identifier to use in the lookup
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime effective time
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader retrieveInstanceForGUID(String  userId,
                                                 String  guid,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        try
        {
            OpenMetadataElement openMetadataElement = client.getMetadataElementByGUID(userId, guid, forLineage, forDuplicateProcessing, null, effectiveTime);

            if (openMetadataElement != null)
            {
                ElementHeader elementHeader = new ElementHeader(openMetadataElement);

                elementHeader.setGUID(openMetadataElement.getElementGUID());
                elementHeader.setClassifications(metadataElementSummaryConverter.getElementClassifications(openMetadataElement.getClassifications()));

                return elementHeader;
            }
        }
        catch (InvalidParameterException notFound)
        {
            OpenMetadataRelationship openMetadataRelationship = client.getRelationshipByGUID(userId, guid, forLineage, forDuplicateProcessing, null, effectiveTime);

            if (openMetadataRelationship != null)
            {
                ElementHeader elementHeader = new ElementHeader(openMetadataRelationship);

                elementHeader.setGUID(openMetadataRelationship.getRelationshipGUID());

                return elementHeader;
            }
        }

        return null;
    }
}
