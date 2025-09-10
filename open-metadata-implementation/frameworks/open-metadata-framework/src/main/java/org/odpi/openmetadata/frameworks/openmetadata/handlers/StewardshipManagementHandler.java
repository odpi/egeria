/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.OpenMetadataMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.converters.MetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.MetadataRelationshipSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.RelatedMetadataElementSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * StewardshipExchangeClient is the client for assigning relationships and classifications that help govern both metadata and its associated
 * resources.
 */
public class StewardshipManagementHandler extends OpenMetadataHandlerBase
{
    final protected MetadataElementSummaryConverter<MetadataElementSummary> metadataElementSummaryConverter;
    final protected RelatedMetadataElementSummaryConverter<RelatedMetadataElementSummary> relatedMetadataElementSummaryConverter;
    final protected MetadataRelationshipSummaryConverter<MetadataRelationshipSummary>     metadataRelationshipSummaryConverter;
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    private final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public StewardshipManagementHandler(String             localServerName,
                                        AuditLog           auditLog,
                                        String             localServiceName,
                                        OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.OPEN_METADATA_ROOT.typeName);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     localServiceName,
                                                                                     localServerName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   localServiceName,
                                                                                                   localServerName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               localServiceName,
                                                                                               localServerName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public StewardshipManagementHandler(StewardshipManagementHandler template,
                                        String       specificTypeName)
    {
        super(template, specificTypeName);

        this.metadataElementSummaryConverter = new MetadataElementSummaryConverter<>(propertyHelper,
                                                                                     localServiceName,
                                                                                     localServerName);
        this.relatedMetadataElementSummaryConverter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                   localServiceName,
                                                                                                   localServerName);
        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               localServiceName,
                                                                                               localServerName);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param queryOptions multiple options to control the query
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
                                                                        QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String methodName = "getConfidenceClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        queryOptions,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the impact classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param queryOptions multiple options to control the query
     *
     * @return list of elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getImpactClassifiedElements(String              userId,
                                                                    boolean             returnSpecificLevel,
                                                                    int                 levelIdentifier,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getImpactClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.IMPACT_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        queryOptions,
                                                        methodName);
    }



    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param queryOptions multiple options to control the query
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
                                                                         QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "getCriticalityClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        queryOptions,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param queryOptions multiple options to control the query
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
                                                                             QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName = "getConfidentialityClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        queryOptions,
                                                        methodName);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param queryOptions multiple options to control the query
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
                                                                       QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName = "getRetentionClassifiedElements";

        return this.getGovernanceDataClassifiedElements(userId,
                                                        OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                        OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                        returnSpecificLevel,
                                                        levelIdentifier,
                                                        queryOptions,
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
     * @param queryOptions multiple options to control the query
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
                                                                             QueryOptions        queryOptions,
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

            List<OpenMetadataElement> matchingElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 getSearchClassifications(propertyConditions, classificationName),
                                                                                                 queryOptions);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                    queryOptions);
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
     * @param queryOptions multiple options to control the query
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
                                                                  QueryOptions              queryOptions) throws InvalidParameterException,
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
            List<OpenMetadataElement> matchingElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 getSearchClassifications(propertyConditions, OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName),
                                                                                                 queryOptions);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                    queryOptions);
        }
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param owner unique identifier for the owner
     * @param queryOptions multiple options to control the query
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getOwnersElements(String              userId,
                                                          String              owner,
                                                          QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getOwnersElements";

        final String nameParameterName = "owner";

        propertyHelper.validateGUID(owner, nameParameterName, methodName);

        return this.getElementsByClassification(userId,
                                                OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                owner,
                                                Collections.singletonList(OpenMetadataProperty.OWNER.name),
                                                queryOptions,
                                                methodName);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param userId calling user
     * @param properties values to search on - null means any value
     * @param queryOptions multiple options to control the query
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getElementsByOrigin(String                          userId,
                                                            DigitalResourceOriginProperties properties,
                                                            QueryOptions                    queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        final String methodName = "getElementsByOrigin";

        List<PropertyCondition> propertyConditions = null;

        if (properties != null)
        {
            propertyHelper.addStringProperty(null,
                                             OpenMetadataProperty.ORGANIZATION.name,
                                             properties.getOrganization(),
                                             PropertyComparisonOperator.EQ);
            propertyConditions = propertyHelper.addStringProperty(propertyConditions,
                                                                  OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                                  properties.getBusinessCapability(),
                                                                  PropertyComparisonOperator.EQ);
            propertyConditions = propertyHelper.addStringMapToSearchCondition(propertyConditions,
                                                                              OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                                              properties.getOtherOriginValues(),
                                                                              PropertyComparisonOperator.EQ);
        }

        if (propertyConditions != null)
        {
            List<OpenMetadataElement> matchingElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 getSearchClassifications(propertyConditions, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName),
                                                                                                 queryOptions);

            return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                               matchingElements,
                                                               methodName);
        }
        else
        {
            return this.getElementsByClassification(userId,
                                                    OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                                    queryOptions);
        }
    }



    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param queryOptions multiple options to control the query

     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getMembersOfSubjectArea(String              userId,
                                                                String              subjectAreaName,
                                                                QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException

    {
        final String methodName = "getMembersOfSubjectArea";

        final String nameParameterName = "subjectAreaName";

        propertyHelper.validateGUID(subjectAreaName, nameParameterName, methodName);

        return this.getElementsByClassification(userId,
                                                OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                                subjectAreaName,
                                                Collections.singletonList(OpenMetadataProperty.SUBJECT_AREA_NAME.name),
                                                queryOptions,
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
     * @param queryOptions multiple options to control the query
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
                                                         QueryOptions                 queryOptions) throws InvalidParameterException,
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
                                             queryOptions,
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
     * @param queryOptions multiple options to control the query
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
                                                                       QueryOptions                 queryOptions,
                                                                       String                       methodName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException

    {
        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           startingAtEnd,
                                                                                                           OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            if ((queryOptions == null) || (queryOptions.getMetadataElementTypeName() == null))
            {
                matchedElements = relatedMetadataElements.getElementList();
            }
            else
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), queryOptions.getMetadataElementTypeName()))
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
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(relatedMetadataElements.getStartingElement(), matchedElements);
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
     * @param queryOptions multiple options to control the query
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
                                                                  QueryOptions                 queryOptions) throws InvalidParameterException,
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
                                             queryOptions,
                                             methodName);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedByDefinitions(String              userId,
                                                                      String              elementGUID,
                                                                      QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException

    {
        final String methodName = "getGovernedByDefinitions";

        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedElements(String              userId,
                                                                 String              governanceDefinitionGUID,
                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException

    {
        final String methodName = "getGovernedElements";

        final String elementGUIDParameterName = "governanceDefinitionGUID";

        propertyHelper.validateGUID(governanceDefinitionGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       governanceDefinitionGUID,
                                       OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSourceElements(String              userId,
                                                               String              elementGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        final String methodName = "getSourceElements";

        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getElementsSourcedFrom(String              userId,
                                                                    String              elementGUID,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        final String methodName = "getElementsSourcedFrom";

        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getScopes(String              userId,
                                                       String              elementGUID,
                                                       QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException

    {
        final String methodName = "getScopes";

        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param userId calling user
     * @param scopeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getScopedElements(String              userId,
                                                               String              scopeGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        final String methodName = "getScopes";

        final String elementGUIDParameterName = "scopeGUID";

        propertyHelper.validateGUID(scopeGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       scopeGUID,
                                       OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "ResourceList" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getResourceList(String              userId,
                                                             String              elementGUID,
                                                             QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException

    {
        final String methodName = "getResourceList";

        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "ResourceList" relationship to the requested resource.
     *
     * @param userId calling user
     * @param resourceGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSupportedByResource(String              userId,
                                                                    String              resourceGUID,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        final String methodName = "getSupportedByResource";
        final String elementGUIDParameterName = "resourceGUID";

        propertyHelper.validateGUID(resourceGUID, elementGUIDParameterName, methodName);

        return this.getRelatedElements(userId,
                                       resourceGUID,
                                       OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "License" relationship to the requested LicenseType.
     *
     * @param userId calling user
     * @param licenseTypeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getLicensedElements(String              userId,
                                                                 String              licenseTypeGUID,
                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getLicensedElements";
        final String guidParameterName = "licenseTypeGUID";

        propertyHelper.validateGUID(licenseTypeGUID, guidParameterName, methodName);

        return this.getRelatedElements(userId,
                                       licenseTypeGUID,
                                       OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the LicenseTypes linked via a "License" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getLicenses(String       userId,
                                                         String       elementGUID,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getLicenses";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the elements linked via a "Certification" relationship to the requested CertificationType.
     *
     * @param userId calling user
     * @param certificationTypeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getCertifiedElements(String              userId,
                                                                  String              certificationTypeGUID,
                                                                  QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getCertifiedElements";

        final String guidParameterName = "certificationTypeGUID";

        propertyHelper.validateGUID(certificationTypeGUID, guidParameterName, methodName);

        return this.getRelatedElements(userId,
                                       certificationTypeGUID,
                                       OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                       2,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }


    /**
     * Retrieve the CertificationTypes linked via a "Certification" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getCertifications(String              userId,
                                                               String              elementGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getCertifications";

        final String guidParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        return this.getRelatedElements(userId,
                                       elementGUID,
                                       OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                       1,
                                       null,
                                       null,
                                       queryOptions,
                                       methodName);
    }



    /**
     * Classify the element (typically a context event, to do or incident report) to indicate the
     * level of impact to the organization that his event is likely to have.
     * The level of impact is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setImpactClassification(String                             userId,
                                        String                             elementGUID,
                                        GovernanceClassificationProperties properties,
                                        MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.IMPACT_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the impact classification from the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearImpactClassification(String                userId,
                                          String                elementGUID,
                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.IMPACT_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidenceClassification(String                             userId,
                                            String                             elementGUID,
                                            GovernanceClassificationProperties properties,
                                            MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidenceClassification(String                userId,
                                              String                elementGUID,
                                              MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setCriticalityClassification(String                             userId,
                                             String                             elementGUID,
                                             GovernanceClassificationProperties properties,
                                             MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCriticalityClassification(String                 userId,
                                               String                 elementGUID,
                                               MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidentialityClassification(String                             userId,
                                                 String                             elementGUID,
                                                 GovernanceClassificationProperties properties,
                                                 MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidentialityClassification(String                userId,
                                                   String                elementGUID,
                                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setRetentionClassification(String                            userId,
                                           String                            elementGUID,
                                           RetentionClassificationProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearRetentionClassification(String                userId,
                                             String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the governance expectations classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceExpectations(String                           userId,
                                           String                           elementGUID,
                                           GovernanceExpectationsProperties properties,
                                           MetadataSourceOptions            metadataSourceOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the governance expectations classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param updateOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceExpectations(String                           userId,
                                             String                           elementGUID,
                                             GovernanceExpectationsProperties properties,
                                             UpdateOptions                    updateOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the governance expectations classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearGovernanceExpectations(String                userId,
                                            String                elementGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the governance measurements classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the ownership
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceMeasurements(String                           userId,
                                           String                           elementGUID,
                                           GovernanceMeasurementsProperties properties,
                                           MetadataSourceOptions            metadataSourceOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the governance measurements classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param updateOptions  options to control access to open metadata
     * @param properties details of the ownership
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceMeasurements(String                           userId,
                                             String                           elementGUID,
                                             GovernanceMeasurementsProperties properties,
                                             UpdateOptions                    updateOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the governance measurements classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearGovernanceMeasurements(String                userId,
                                            String                elementGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the security tags for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the security tags
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                 userId,
                                 String                 elementGUID,
                                 SecurityTagsProperties properties,
                                 MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the security tags need to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearSecurityTags(String                userId,
                                  String                elementGUID,
                                  MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addOwnership(String                userId,
                              String                elementGUID,
                              OwnershipProperties   properties,
                              MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearOwnership(String                userId,
                               String                elementGUID,
                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the origin classification for a digital resource.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the origin
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addDigitalResourceOrigin(String                          userId,
                                         String                          elementGUID,
                                         DigitalResourceOriginProperties properties,
                                         MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the origin classification from a digital resource.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearDigitalResourceOrigin(String                userId,
                                           String                elementGUID,
                                           MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }



    /**
     * Add the ZoneMembership classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to
     * @param properties details of the origin
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addZoneMembership(String                   userId,
                                  String                   elementGUID,
                                  ZoneMembershipProperties properties,
                                  MetadataSourceOptions    metadataSourceOptions) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the ZoneMembership classification from a digital resource.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearZoneMembership(String                userId,
                                    String                elementGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param properties properties for the relationship
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSemanticAssignment(String                       userId,
                                        String                       elementGUID,
                                        String                       glossaryTermGUID,
                                        SemanticAssignmentProperties properties,
                                        MetadataSourceOptions        metadataSourceOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        elementGUID,
                                                        OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        glossaryTermGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSemanticAssignment(String        userId,
                                        String        elementGUID,
                                        String        glossaryTermGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName            = "clearSemanticAssignment";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        glossaryTermGUID,
                                                        deleteOptions);
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the governance definition to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addScopeToElement(String                userId,
                                  String                elementGUID,
                                  String                scopeGUID,
                                  MetadataSourceOptions metadataSourceOptions,
                                  ScopedByProperties    properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName            = "addScopeToElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "scopeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(scopeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        elementGUID,
                                                        OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                        scopeGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the ScopedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the governance definition to link
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeScopeFromElement(String        userId,
                                       String        elementGUID,
                                       String        scopeGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName            = "removeScopeFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "scopeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(scopeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        scopeGUID,
                                                        deleteOptions);
    }


    /**
     * Link an element to another element using the ResourceList relationship.
     *
     * @param userId calling user
     * @param elementGUID identifier of the governance definition to link
     * @param resourceGUID unique identifier of the metadata element to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addResourceListToElement(String                 userId,
                                         String                 elementGUID,
                                         String                 resourceGUID,
                                         MetadataSourceOptions  metadataSourceOptions,
                                         ResourceListProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        elementGUID,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        resourceGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the ResourceList relationship between two elements.
     *
     * @param userId calling user
     * @param elementGUID identifier of the governance definition to link
     * @param resourceGUID unique identifier of the metadata element to update
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeResourceListFromElement(String        userId,
                                              String        elementGUID,
                                              String        resourceGUID,
                                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName            = "removeResourceListFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "resourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(resourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        resourceGUID,
                                                        deleteOptions);
    }



    /**
     * Link an element to another element using the MoreInformation relationship.
     *
     * @param userId calling user
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addMoreInformationToElement(String                    userId,
                                            String                    elementGUID,
                                            String                    moreInformationGUID,
                                            MetadataSourceOptions     metadataSourceOptions,
                                            MoreInformationProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        elementGUID,
                                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                                        moreInformationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the MoreInformation relationship between two elements.
     *
     * @param userId calling user
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to update
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeMoreInformationFromElement(String        userId,
                                                 String        elementGUID,
                                                 String        moreInformationGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName            = "removeMoreInformationFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "moreInformationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(moreInformationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        moreInformationGUID,
                                                        deleteOptions);
    }



    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId calling user
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummary getMetadataElementByUniqueName(String     userId,
                                                                 String     uniqueName,
                                                                 String     uniquePropertyName,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getMetadataElementByUniqueName";
        final String elementGUIDParameterName = "uniqueName";

        propertyHelper.validateMandatoryName(uniqueName, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByUniqueName(userId, uniqueName, uniquePropertyName, getOptions);

        return metadataElementSummaryConverter.getNewBean(MetadataElementSummary.class,
                                                          openMetadataElement,
                                                          methodName);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param propertyValue value to search for
     * @param propertyNames which properties to look in
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByPropertyValue(String              userId,
                                                                   String              propertyValue,
                                                                   List<String>        propertyNames,
                                                                   QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getElementsByPropertyValue";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        propertyHelper.validateObject(propertyValue, propertyValueProperty, methodName);
        propertyHelper.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.getMetadataElementsByPropertyValue(userId,
                                                                                                               propertyNames,
                                                                                                               propertyValue,
                                                                                                               queryOptions);

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
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> findElementsByPropertyValue(String              userId,
                                                                    String              propertyValue,
                                                                    List<String>        propertyNames,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "findElementsByPropertyValue";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        propertyHelper.validateObject(propertyValue, propertyValueProperty, methodName);
        propertyHelper.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsByPropertyValue(userId,
                                                                                                                propertyNames,
                                                                                                                propertyValue,
                                                                                                                queryOptions);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
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
     * @param queryOptions multiple options to control the query
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
                                                                    QueryOptions        queryOptions,
                                                                    String              methodName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        propertyHelper.validateMandatoryName(classificationName, classificationNameProperty, methodName);
        propertyHelper.validateObject(propertyValue, propertyValueProperty, methodName);
        propertyHelper.validateObject(propertyNames, propertyNamesProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataClient.getMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                 classificationName,
                                                                                                                 propertyNames,
                                                                                                                 propertyValue,
                                                                                                                 queryOptions);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class, elements, methodName);
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
     * @param queryOptions multiple options to control the query
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
                                                                                      QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        final String methodName = "findElementsByClassificationWithPropertyValue";

        final String classificationNameProperty = "classificationName";
        final String propertyValueProperty = "propertyValue";

        propertyHelper.validateMandatoryName(classificationName, classificationNameProperty, methodName);
        propertyHelper.validateObject(propertyValue, propertyValueProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                              classificationName,
                                                                                                                              propertyNames,
                                                                                                                              propertyValue,
                                                                                                                              queryOptions);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class, openMetadataElements, methodName);
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
     * @param queryOptions multiple options to control the query
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
                                                                QueryOptions        queryOptions,
                                                                String              methodName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           startingAtEnd,
                                                                                                           relationshipTypeName,
                                                                                                           queryOptions);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((queryOptions == null) || (queryOptions.getMetadataElementTypeName() == null) || (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), queryOptions.getMetadataElementTypeName())))
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
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(relatedMetadataElements.getStartingElement(), matchedElements);
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
     * @param queryOptions multiple options to control the query
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
                                                                                  QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String methodName = "findRelatedElementsWithPropertyValue";
        final String propertyValueProperty = "propertyValue";

        propertyHelper.validateObject(propertyValue, propertyValueProperty, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           startingAtEnd,
                                                                                                           relationshipTypeName,
                                                                                                           queryOptions);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElement> matchedElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((queryOptions == null) || (queryOptions.getMetadataElementTypeName() == null) || (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), queryOptions.getMetadataElementTypeName())))
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
                OpenMetadataMermaidGraphBuilder graphBuilder = new OpenMetadataMermaidGraphBuilder(relatedMetadataElements.getStartingElement(), matchedElements);
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
     * @param queryOptions multiple options to control the query
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
                                                              QueryOptions        queryOptions,
                                                              String              methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        OpenMetadataRelationshipList relationshipList = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                                    relationshipTypeName,
                                                                                                                    propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                                                                                                    queryOptions);

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
     * @param queryOptions multiple options to control the query
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
                                                                              QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        final String methodName = "findRelationshipsWithPropertyValue";

        OpenMetadataRelationshipList relationshipList = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                                    relationshipTypeName,
                                                                                                                    propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                                                                                                    queryOptions);

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
     * @param getOptions multiple options to control the query
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader retrieveInstanceForGUID(String     userId,
                                                 String     guid,
                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        try
        {
            OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, guid, getOptions);

            if (openMetadataElement != null)
            {
                return propertyHelper.getElementHeader(openMetadataElement);
            }
        }
        catch (InvalidParameterException notFound)
        {
            OpenMetadataRelationship openMetadataRelationship = openMetadataClient.getRelationshipByGUID(userId, guid, getOptions);

            if (openMetadataRelationship != null)
            {
                return propertyHelper.getElementHeader(openMetadataRelationship, openMetadataRelationship.getRelationshipGUID(), null);
            }
        }

        return null;
    }

    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the metadata element
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummary getMetadataElementByGUID(String     userId,
                                                           String     elementGUID,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getMetadataElementByGUID";
        final String elementGUIDParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              elementGUID,
                                                                                              getOptions);

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
     * @param getOptions multiple options to control the query
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String getMetadataElementGUIDByUniqueName(String     userId,
                                                     String     uniqueName,
                                                     String     uniquePropertyName,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataClient.getMetadataElementGUIDByUniqueName(userId, uniqueName, uniquePropertyName, getOptions);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param userId calling user
     * @param findProperties details of the search
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElements(String        userId,
                                                    QueryOptions  findProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getElements";

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 null,
                                                                                                 findProperties);;

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
     * @param queryOptions               multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByClassification(String       userId,
                                                                    String       classificationName,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName                 = "getElementsByClassification";
        final String classificationNameProperty = "classificationName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(classificationName, classificationNameProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataClient.getMetadataElementsByClassification(userId,
                                                                                                    classificationName,
                                                                                                    queryOptions);;

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve related elements of the requested type name.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting end
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName name of relationship
     * @param findProperties  open metadata type to search on
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getRelatedElements(String             userId,
                                                                  String             elementGUID,
                                                                  String             relationshipTypeName,
                                                                  int                startingAtEnd,
                                                                  FindProperties     findProperties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "getRelatedElements";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           startingAtEnd,
                                                                                                           relationshipTypeName,
                                                                                                           findProperties);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((findProperties == null) ||
                            (findProperties.getOpenMetadataTypeName() == null) ||
                            (findProperties.getOpenMetadataTypeName().equals(relatedMetadataElement.getElement().getType().getTypeName())))
                    {
                        results.add(relatedMetadataElementSummaryConverter.getNewBean(RelatedMetadataElementSummary.class,
                                                                                      relatedMetadataElement,
                                                                                      methodName));
                    }
                }
            }

            return results;
        }

        return null;
    }



    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param findProperties search options
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataRelationshipSummary> getRelationships(String         userId,
                                                              String         relationshipTypeName,
                                                              FindProperties findProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getRelationships";

        OpenMetadataRelationshipList openMetadataRelationships = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                                             relationshipTypeName,
                                                                                                                             null,
                                                                                                                             findProperties);

        if (openMetadataRelationships != null)
        {
            return metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                    openMetadataRelationships,
                                                                    methodName);
        }

        return null;
    }
}
