/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummaryList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with lineage relationships.
 */
public class ClassificationManagerClient extends ConnectorContextClientBase
{
    private final StewardshipManagementHandler stewardshipManagementHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ClassificationManagerClient(ConnectorContextBase     parentContext,
                                       String                   localServerName,
                                       String                   localServiceName,
                                       String                   connectorUserId,
                                       String                   connectorGUID,
                                       String                   externalSourceGUID,
                                       String                   externalSourceName,
                                       OpenMetadataClient       openMetadataClient,
                                       AuditLog                 auditLog,
                                       int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.stewardshipManagementHandler = new StewardshipManagementHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public ClassificationManagerClient(ClassificationManagerClient template,
                                       String      specificTypeName)
    {
        super(template);

        this.stewardshipManagementHandler = new StewardshipManagementHandler(template.stewardshipManagementHandler, specificTypeName);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
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
    public List<MetadataElementSummary> getConfidenceClassifiedElements(boolean             returnSpecificLevel,
                                                                        int                 levelIdentifier,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        return stewardshipManagementHandler.getConfidenceClassifiedElements(connectorUserId, returnSpecificLevel, levelIdentifier, queryOptions);
    }


    /**
     * Return information about the elements classified with the impact classification.
     *
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
    public List<MetadataElementSummary> getImpactClassifiedElements(boolean             returnSpecificLevel,
                                                                    int                 levelIdentifier,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        return stewardshipManagementHandler.getImpactClassifiedElements(connectorUserId, returnSpecificLevel, levelIdentifier, queryOptions);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
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
    public List<MetadataElementSummary> getCriticalityClassifiedElements(boolean             returnSpecificLevel,
                                                                         int                 levelIdentifier,
                                                                         QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        return stewardshipManagementHandler.getCriticalityClassifiedElements(connectorUserId, returnSpecificLevel, levelIdentifier, queryOptions);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
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
    public List<MetadataElementSummary> getConfidentialityClassifiedElements(boolean             returnSpecificLevel,
                                                                             int                 levelIdentifier,
                                                                             QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        return stewardshipManagementHandler.getConfidentialityClassifiedElements(connectorUserId, returnSpecificLevel, levelIdentifier, queryOptions);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
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
    public List<MetadataElementSummary> getRetentionClassifiedElements(boolean             returnSpecificLevel,
                                                                       int                 levelIdentifier,
                                                                       QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        return stewardshipManagementHandler.getRetentionClassifiedElements(connectorUserId, returnSpecificLevel, levelIdentifier, queryOptions);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
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
    public List<MetadataElementSummary> getSecurityTaggedElements(List<String>              securityLabels,
                                                                  Map<String, Object> securityProperties,
                                                                  Map<String, List<String>> accessGroups,
                                                                  QueryOptions              queryOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        return stewardshipManagementHandler.getSecurityTaggedElements(connectorUserId, securityLabels, securityProperties, accessGroups, queryOptions);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param owner unique identifier for the owner
     * @param queryOptions multiple options to control the query
     *
     * @return list of element summaries
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getOwnersElements(String              owner,
                                                          QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return stewardshipManagementHandler.getOwnersElements(connectorUserId, owner, queryOptions);
    }


    /**
     * Return information about the elements from a specific origin.
     *
     * @param properties values to search on - null means any value
     * @param queryOptions multiple options to control the query
     *
     * @return list of the elements from the origin
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getElementsByOrigin(DigitalResourceOriginProperties properties,
                                                            QueryOptions                    queryOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException

    {
        return stewardshipManagementHandler.getElementsByOrigin(connectorUserId, properties, queryOptions);
    }



    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param subjectAreaName unique identifier for the subject area
     * @param queryOptions multiple options to control the query

     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getMembersOfSubjectArea(String              subjectAreaName,
                                                                QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException

    {
        return stewardshipManagementHandler.getMembersOfSubjectArea(connectorUserId, subjectAreaName, queryOptions);
    }



    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
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
    public RelatedMetadataElementSummaryList getMeanings(String                       elementGUID,
                                                         String                       expression,
                                                         String                       description,
                                                         TermAssignmentStatus status,
                                                         boolean                      returnSpecificConfidence,
                                                         int                          confidence,
                                                         String                       createdBy,
                                                         String                       steward,
                                                         String                       source,
                                                         QueryOptions                 queryOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException

    {
        return stewardshipManagementHandler.getMeanings(connectorUserId, elementGUID, expression, description, status, returnSpecificConfidence, confidence, createdBy, steward, source, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
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
    public RelatedMetadataElementSummaryList getSemanticAssignees(String                       glossaryTermGUID,
                                                                  String                       expression,
                                                                  String                       description,
                                                                  TermAssignmentStatus status,
                                                                  boolean                      returnSpecificConfidence,
                                                                  int                          confidence,
                                                                  String                       createdBy,
                                                                  String                       steward,
                                                                  String                       source,
                                                                  QueryOptions                 queryOptions) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException

    {
        return stewardshipManagementHandler.getSemanticAssignees(connectorUserId, glossaryTermGUID, expression, description, status, returnSpecificConfidence, confidence, createdBy, steward, source, queryOptions);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedByDefinitions(String              elementGUID,
                                                                      QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException

    {
        return stewardshipManagementHandler.getGovernedByDefinitions(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getGovernedElements(String              governanceDefinitionGUID,
                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException

    {
        return stewardshipManagementHandler.getGovernedElements(connectorUserId, governanceDefinitionGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param elementGUID unique identifier of the element
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSourceElements(String              elementGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        return stewardshipManagementHandler.getSourceElements(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getElementsSourcedFrom(String              elementGUID,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        return stewardshipManagementHandler.getElementsSourcedFrom(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getScopes(String              elementGUID,
                                                       QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException

    {
        return stewardshipManagementHandler.getScopes(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param scopeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getScopedElements(String              scopeGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        return stewardshipManagementHandler.getCertifiedElements(connectorUserId, scopeGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "ResourceList" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getResourceList(String              elementGUID,
                                                             QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException

    {
        return stewardshipManagementHandler.getResourceList(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "ResourceList" relationship to the requested resource.
     *
     * @param resourceGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getSupportedByResource(String              resourceGUID,
                                                                    QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException

    {
        return stewardshipManagementHandler.getSupportedByResource(connectorUserId, resourceGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "License" relationship to the requested LicenseType.
     *
     * @param licenseTypeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getLicensedElements(String              licenseTypeGUID,
                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        return stewardshipManagementHandler.getLicensedElements(connectorUserId, licenseTypeGUID, queryOptions);
    }


    /**
     * Retrieve the LicenseTypes linked via a "License" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getLicenses(String       elementGUID,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return stewardshipManagementHandler.getLicenses(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the elements linked via a "Certification" relationship to the requested CertificationType.
     *
     * @param certificationTypeGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getCertifiedElements(String              certificationTypeGUID,
                                                                  QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return stewardshipManagementHandler.getCertifiedElements(connectorUserId, certificationTypeGUID, queryOptions);
    }


    /**
     * Retrieve the CertificationTypes linked via a "Certification" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummaryList getCertifications(String              elementGUID,
                                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return stewardshipManagementHandler.getCertifications(connectorUserId, elementGUID, queryOptions);
    }



    /**
     * Classify the element (typically a context event, to do or incident report) to indicate the
     * level of impact to the organization that his event is likely to have.
     * The level of impact is expressed by the
     * levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setImpactClassification(String                             elementGUID,
                                        GovernanceClassificationProperties properties,
                                        MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        stewardshipManagementHandler.setImpactClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the impact classification from the element.
     *
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearImpactClassification(String                elementGUID,
                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        stewardshipManagementHandler.clearImpactClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidenceClassification(String                             elementGUID,
                                            GovernanceClassificationProperties properties,
                                            MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        stewardshipManagementHandler.setConfidenceClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidenceClassification(String                elementGUID,
                                              MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        stewardshipManagementHandler.clearConfidenceClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setCriticalityClassification(String                             elementGUID,
                                             GovernanceClassificationProperties properties,
                                             MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        stewardshipManagementHandler.setCriticalityClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCriticalityClassification(String                 elementGUID,
                                               MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        stewardshipManagementHandler.clearCriticalityClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidentialityClassification(String                             elementGUID,
                                                 GovernanceClassificationProperties properties,
                                                 MetadataSourceOptions              metadataSourceOptions) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        stewardshipManagementHandler.setConfidentialityClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidentialityClassification(String                elementGUID,
                                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        stewardshipManagementHandler.clearConfidentialityClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Classify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setRetentionClassification(String                            elementGUID,
                                           RetentionProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        stewardshipManagementHandler.setRetentionClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearRetentionClassification(String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        stewardshipManagementHandler.clearRetentionClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Add the governance expectations classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceExpectations(String                           elementGUID,
                                           GovernanceExpectationsProperties properties,
                                           MetadataSourceOptions            metadataSourceOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        stewardshipManagementHandler.addGovernanceExpectations(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Update the governance expectations classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param updateOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceExpectations(String                           elementGUID,
                                             GovernanceExpectationsProperties properties,
                                             UpdateOptions                    updateOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        stewardshipManagementHandler.updateGovernanceExpectations(connectorUserId, elementGUID, properties, updateOptions);
    }


    /**
     * Remove the governance expectations classification from an element.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearGovernanceExpectations(String                elementGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        stewardshipManagementHandler.clearGovernanceExpectations(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Add the governance measurements classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties details of the ownership
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceMeasurements(String                           elementGUID,
                                           GovernanceMeasurementsProperties properties,
                                           MetadataSourceOptions            metadataSourceOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        stewardshipManagementHandler.addGovernanceMeasurements(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Update the governance measurements classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param updateOptions  options to control access to open metadata
     * @param properties details of the ownership
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceMeasurements(String                           elementGUID,
                                             GovernanceMeasurementsProperties properties,
                                             UpdateOptions                    updateOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        stewardshipManagementHandler.updateGovernanceMeasurements(connectorUserId, elementGUID, properties, updateOptions);
    }


    /**
     * Remove the governance measurements classification from an element.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearGovernanceMeasurements(String                elementGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        stewardshipManagementHandler.clearGovernanceMeasurements(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Add the security tags for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the security tags
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                 elementGUID,
                                 SecurityTagsProperties properties,
                                 MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        stewardshipManagementHandler.addSecurityTags(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param elementGUID element where the security tags need to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearSecurityTags(String                elementGUID,
                                  MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        stewardshipManagementHandler.clearSecurityTags(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addOwnership(String                elementGUID,
                              OwnershipProperties   properties,
                              MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        stewardshipManagementHandler.addOwnership(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearOwnership(String                elementGUID,
                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        stewardshipManagementHandler.clearOwnership(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Add the origin classification for a digital resource.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the origin
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addDigitalResourceOrigin(String                          elementGUID,
                                         DigitalResourceOriginProperties properties,
                                         MetadataSourceOptions           metadataSourceOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        stewardshipManagementHandler.addDigitalResourceOrigin(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the origin classification from a digital resource.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearDigitalResourceOrigin(String                elementGUID,
                                           MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        stewardshipManagementHandler.clearDigitalResourceOrigin(connectorUserId, elementGUID, metadataSourceOptions);
    }



    /**
     * Add the ZoneMembership classification for an element.
     *
     * @param elementGUID element to link it to
     * @param properties details of the origin
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addZoneMembership(String                   elementGUID,
                                  ZoneMembershipProperties properties,
                                  MetadataSourceOptions    metadataSourceOptions) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        stewardshipManagementHandler.addZoneMembership(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the ZoneMembership classification from a digital resource.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearZoneMembership(String                elementGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        stewardshipManagementHandler.clearZoneMembership(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param properties properties for the relationship
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSemanticAssignment(String                       elementGUID,
                                        String                       glossaryTermGUID,
                                        SemanticAssignmentProperties properties,
                                        MetadataSourceOptions        metadataSourceOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        stewardshipManagementHandler.setupSemanticAssignment(connectorUserId, elementGUID, glossaryTermGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSemanticAssignment(String        elementGUID,
                                        String        glossaryTermGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        stewardshipManagementHandler.clearSemanticAssignment(connectorUserId, elementGUID, glossaryTermGUID, deleteOptions);
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the governance definition to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addScopeToElement(String                elementGUID,
                                  String                scopeGUID,
                                  MetadataSourceOptions metadataSourceOptions,
                                  ScopedByProperties    properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        stewardshipManagementHandler.addScopeToElement(connectorUserId, elementGUID, scopeGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove the ScopedBy relationship between a governance definition and an element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the governance definition to link
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeScopeFromElement(String        elementGUID,
                                       String        scopeGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        stewardshipManagementHandler.removeScopeFromElement(connectorUserId, elementGUID, scopeGUID, deleteOptions);
    }


    /**
     * Link an element to another element using the ResourceList relationship.
     *
     * @param elementGUID identifier of the governance definition to link
     * @param resourceGUID unique identifier of the metadata element to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addResourceListToElement(String                 elementGUID,
                                         String                 resourceGUID,
                                         MetadataSourceOptions  metadataSourceOptions,
                                         ResourceListProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        stewardshipManagementHandler.addResourceListToElement(connectorUserId, elementGUID, resourceGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove the ResourceList relationship between two elements.
     *
     * @param elementGUID identifier of the governance definition to link
     * @param resourceGUID unique identifier of the metadata element to update
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeResourceListFromElement(String        elementGUID,
                                              String        resourceGUID,
                                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        stewardshipManagementHandler.removeResourceListFromElement(connectorUserId, elementGUID, resourceGUID, deleteOptions);
    }


    /**
     * Link an element to another element using the MoreInformation relationship.
     *
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addMoreInformationToElement(String                    elementGUID,
                                            String                    moreInformationGUID,
                                            MetadataSourceOptions     metadataSourceOptions,
                                            MoreInformationProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        stewardshipManagementHandler.addMoreInformationToElement(connectorUserId, elementGUID, moreInformationGUID, metadataSourceOptions, properties);
    }


    /**
     * Remove the MoreInformation relationship between two elements.
     *
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to update
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeMoreInformationFromElement(String        elementGUID,
                                                 String        moreInformationGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        stewardshipManagementHandler.removeMoreInformationFromElement(connectorUserId, elementGUID, moreInformationGUID, deleteOptions);
    }
}
