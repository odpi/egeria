/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.commonservices.gaf.properties.TranslationDetail;
import org.odpi.openmetadata.commonservices.gaf.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentDependency;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentImpactedElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataGovernanceService provides services for invoking metadata governance services in Egeria.
 */
public class OpenMetadataGovernanceService
{
    private final OpenMetadataStoreClient     openMetadataStoreClient;
    private final String                      userId;
    private final String                      connectorName;


    /**
     * Create a new client to invoke open metadata governance.
     *
     * @param openMetadataStoreClient client for exchange requests
     * @param userId integration daemon's userId
     * @param connectorName name of the connector using this context
     */
    OpenMetadataGovernanceService(OpenMetadataStoreClient  openMetadataStoreClient,
                                  String                   userId,
                                  String                   connectorName)
    {
        this.openMetadataStoreClient     = openMetadataStoreClient;
        this.userId                      = userId;
        this.connectorName               = connectorName;
    }



    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  String createIncidentReport(String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties,
                                        String                        originatorGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataStoreClient.createIncidentReport(userId,
                                                            qualifiedName,
                                                            domainIdentifier,
                                                            background,
                                                            impactedResources,
                                                            previousIncidents,
                                                            incidentClassifiers,
                                                            additionalProperties,
                                                            originatorGUID);
    }


    /**
     * Create a To-Do request for someone to work on.
     *
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String openToDo(String toDoQualifiedName,
                           String title,
                           String instructions,
                           int    priority,
                           Date   dueDate,
                           String assignTo) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        return openMetadataStoreClient.openToDo(userId, toDoQualifiedName, title, instructions, priority, dueDate, assignTo);
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setTranslation(String            elementGUID,
                               TranslationDetail translationDetail) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        openMetadataStoreClient.setTranslation(userId, elementGUID, translationDetail);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @throws InvalidParameterException  the language is null or not known or not unique (add locale)
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearTranslation(String elementGUID,
                                 String language,
                                 String locale) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        openMetadataStoreClient.clearTranslation(userId, elementGUID, language, locale);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public TranslationDetail getTranslation(String elementGUID,
                                            String language,
                                            String locale) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return openMetadataStoreClient.getTranslation(userId, elementGUID, language, locale);
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<TranslationDetail> getTranslations(String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return openMetadataStoreClient.getTranslations(userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataValue(String             typeName,
                                        String             propertyName,
                                        ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataStoreClient.setUpValidMetadataValue(userId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataValue(String typeName,
                                        String propertyName,
                                        String preferredValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        openMetadataStoreClient.clearValidMetadataValue(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public boolean validateMetadataValue(String typeName,
                                         String propertyName,
                                         String actualValue) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return openMetadataStoreClient.validateMetadataValue(userId, typeName, propertyName, actualValue);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValue getValidMetadataValue(String typeName,
                                                    String propertyName,
                                                    String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return openMetadataStoreClient.getValidMetadataValue(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<ValidMetadataValue> getValidMetadataValues(String typeName,
                                                           String propertyName,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataStoreClient.getValidMetadataValues(userId, typeName, propertyName, startFrom, pageSize);
    }
}
