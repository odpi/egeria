/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.TemplateHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SourcedFromProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.List;

/**
 * Provides services for connectors to work with template elements.
 */
public class TemplateClient extends ConnectorContextClientBase
{
    private final TemplateHandler templateHandler;


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
    public TemplateClient(ConnectorContextBase     parentContext,
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

        this.templateHandler = new TemplateHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }



    /**
     * Classify an element as suitable to be used as a template for cataloguing elements of a similar types.
     *
     * @param elementGUID   unique identifier of the element to classify as a template
     * @param properties    properties of the template
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateClassification(String                 elementGUID,
                                          TemplateProperties properties,
                                          MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        templateHandler.addTemplateClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the classification that indicates that this element can be used as a template.
     * Any specification is also removed.
     *
     * @param elementGUID unique identifier of the element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        templateHandler.removeTemplateClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }



    /**
     * Classify an element as a template substitute.
     *
     * @param elementGUID   unique identifier of the element to classify as a template
     * @param properties    properties of the template
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateSubstituteClassification(String                       elementGUID,
                                                    TemplateSubstituteProperties properties,
                                                    MetadataSourceOptions        metadataSourceOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        templateHandler.addTemplateSubstituteClassification(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the TemplateSubstitute classification.
     *
     * @param elementGUID unique identifier of the element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateSubstituteClassification(String                elementGUID,
                                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        templateHandler.removeTemplateSubstituteClassification(connectorUserId, elementGUID, metadataSourceOptions);
    }


    /**
     * Attach a template to an element that this template is relevant to.   For example, a project.
     *
     * @param elementGUID          unique identifier of the agreement
     * @param templateGUID      unique identifier of the agreement item
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSourcedFrom(String                elementGUID,
                                String                templateGUID,
                                MakeAnchorOptions     makeAnchorOptions,
                                SourcedFromProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        templateHandler.linkSourcedFrom(connectorUserId, elementGUID, templateGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a source template for an element.
     *
     * @param elementGUID     unique identifier of the agreement
     * @param templateGUID unique identifier of the agreement item
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSourcedFrom(String        elementGUID,
                                  String        templateGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        templateHandler.detachSourcedFrom(connectorUserId, elementGUID, templateGUID, deleteOptions);
    }


    /**
     * Attach a template to an element that this template is relevant to.   For example, a project.
     *
     * @param elementGUID          unique identifier of the agreement
     * @param templateGUID      unique identifier of the agreement item
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCatalogTemplate(String                    elementGUID,
                                    String                    templateGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    CatalogTemplateProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        templateHandler.linkCatalogTemplate(connectorUserId,
                                            elementGUID,
                                            templateGUID,
                                            makeAnchorOptions,
                                            relationshipProperties);
    }


    /**
     * Detach a template for an element that this template is relevant to.   For example, a project.
     *
     * @param elementGUID     unique identifier of the agreement
     * @param templateGUID unique identifier of the agreement item
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCatalogTemplate(String        elementGUID,
                                      String        templateGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        templateHandler.detachCatalogTemplate(connectorUserId, elementGUID, templateGUID, deleteOptions);
    }


    /**
     * Return the templates with the requested name.
     *
     * @param name          name to search for
     * @param queryOptions  options to control the query
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> getTemplatesByName(String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return templateHandler.getTemplatesByName(connectorUserId, name, queryOptions);
    }
}
