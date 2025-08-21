/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GlossaryTermHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with Schema Type elements.
 */
public class GlossaryTermClient extends ConnectorContextClientBase
{
    private final GlossaryTermHandler glossaryTermHandler;


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
    public GlossaryTermClient(ConnectorContextBase     parentContext,
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

        this.glossaryTermHandler = new GlossaryTermHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new glossaryTerm.
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param properties                   properties for the new element.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGlossaryTerm(String                 glossaryGUID,
                                     GlossaryTermProperties properties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setAnchorScopeGUID(glossaryGUID);
        newElementOptions.setParentGUID(glossaryGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

        return this.createGlossaryTerm(newElementOptions,
                                       null,
                                       properties,
                                       null);

    }

    /**
     * Create a new glossaryTerm.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGlossaryTerm(NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     GlossaryTermProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String elementGUID = glossaryTermHandler.createGlossaryTerm(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossaryTerm.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing glossaryTerm to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTermFromTemplate(TemplateOptions        templateOptions,
                                                 String                 templateGUID,
                                                 ElementProperties      replacementProperties,
                                                 Map<String, String>    placeholderProperties,
                                                 RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        String elementGUID = glossaryTermHandler.createGlossaryTermFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a glossary term.
     *
     * @param glossaryTermGUID       unique identifier of the glossaryTerm (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGlossaryTerm(String               glossaryTermGUID,
                                   UpdateOptions        updateOptions,
                                   GlossaryTermProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        glossaryTermHandler.updateGlossaryTerm(connectorUserId, glossaryTermGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(glossaryTermGUID);
        }
    }


    /**
     * Update the properties of a solution blueprint, solution component or solution port.
     *
     * @param glossaryTermGUID   unique identifier of the solution element (returned from create)
     * @param metadataSourceOptions options to control access to open metadata
     * @param status                new status for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateElementStatus(String                glossaryTermGUID,
                                    MetadataSourceOptions metadataSourceOptions,
                                    ElementStatus         status) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        glossaryTermHandler.updateElementStatus(connectorUserId, glossaryTermGUID, metadataSourceOptions, status);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(glossaryTermGUID);
        }
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param templateGUID identifier for the template glossary term
     * @param updateWithTemplateOptions options for the change to the term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermFromTemplate(String                    glossaryTermGUID,
                                               String                    templateGUID,
                                               UpdateWithTemplateOptions updateWithTemplateOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        glossaryTermHandler.updateGlossaryTermFromTemplate(connectorUserId, glossaryTermGUID, templateGUID, updateWithTemplateOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(glossaryTermGUID);
        }
    }


    /**
     * Move a glossary term from one glossary to another.  This involves anchoring to the new glossary and
     * changing the term anchor relationship
     *
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param glossaryGUID           unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void moveGlossaryTerm(String            glossaryTermGUID,
                                 String            glossaryGUID,
                                 DeleteOptions     deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        glossaryTermHandler.moveGlossaryTerm(connectorUserId, glossaryTermGUID, glossaryGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(glossaryTermGUID);
        }
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setupTermRelationship(String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      MetadataSourceOptions    metadataSourceOptions,
                                      GlossaryTermRelationship relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        glossaryTermHandler.setupTermRelationship(connectorUserId,
                                                  relationshipTypeName,
                                                  glossaryTermOneGUID,
                                                  glossaryTermTwoGUID,
                                                  metadataSourceOptions,
                                                  relationshipProperties);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param updateOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateTermRelationship(String                   relationshipTypeName,
                                       String                   glossaryTermOneGUID,
                                       String                   glossaryTermTwoGUID,
                                       UpdateOptions            updateOptions,
                                       GlossaryTermRelationship relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        glossaryTermHandler.updateTermRelationship(connectorUserId, relationshipTypeName, glossaryTermOneGUID, glossaryTermTwoGUID, updateOptions, relationshipProperties);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermRelationship(String        relationshipTypeName,
                                      String        glossaryTermOneGUID,
                                      String        glossaryTermTwoGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        glossaryTermHandler.clearTermRelationship(connectorUserId, relationshipTypeName, glossaryTermOneGUID, glossaryTermTwoGUID, deleteOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsAbstractConcept(String                    glossaryTermGUID,
                                         AbstractConceptProperties properties,
                                         MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        glossaryTermHandler.setTermAsAbstractConcept(connectorUserId, glossaryTermGUID, properties, metadataSourceOptions);
    }

    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsAbstractConcept(String                glossaryTermGUID,
                                           MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        glossaryTermHandler.clearTermAsAbstractConcept(connectorUserId, glossaryTermGUID, metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsDataValue(String                glossaryTermGUID,
                                   DataValueProperties   properties,
                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        glossaryTermHandler.setTermAsDataValue(connectorUserId, glossaryTermGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsDataValue(String                glossaryTermGUID,
                                     MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        glossaryTermHandler.clearTermAsDataValue(connectorUserId, glossaryTermGUID, metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes an activity.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsActivity(String                        glossaryTermGUID,
                                  ActivityDescriptionProperties properties,
                                  MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        glossaryTermHandler.setTermAsActivity(connectorUserId, glossaryTermGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsActivity(String                glossaryTermGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        glossaryTermHandler.clearTermAsActivity(connectorUserId, glossaryTermGUID, metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsContext(String                      glossaryTermGUID,
                                 ContextDefinitionProperties properties,
                                 MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        glossaryTermHandler.setTermAsContext(connectorUserId, glossaryTermGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsContext(String                glossaryTermGUID,
                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        glossaryTermHandler.clearTermAsContext(connectorUserId, glossaryTermGUID, metadataSourceOptions);
    }


    /**
     * Delete a glossaryTerm.
     *
     * @param glossaryTermGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGlossaryTerm(String        glossaryTermGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        glossaryTermHandler.deleteGlossaryTerm(connectorUserId, glossaryTermGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(glossaryTermGUID);
        }
    }


    /**
     * Returns the list of glossaryTerms with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGlossaryTermsByName(String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return glossaryTermHandler.getGlossaryTermsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific glossaryTerm.
     *
     * @param glossaryTermGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGlossaryTermByGUID(String     glossaryTermGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return glossaryTermHandler.getGlossaryTermByGUID(connectorUserId, glossaryTermGUID, getOptions);
    }


    /**
     * Retrieve the list of glossaryTerms metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned glossaryTerms include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGlossaryTerms(String              searchString,
                                                           SearchOptions       queryOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return glossaryTermHandler.findGlossaryTerms(connectorUserId, searchString, queryOptions);
    }
}
