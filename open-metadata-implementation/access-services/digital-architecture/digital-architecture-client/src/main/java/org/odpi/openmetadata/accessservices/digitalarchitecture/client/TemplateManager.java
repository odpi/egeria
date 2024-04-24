/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.client;


import org.odpi.openmetadata.accessservices.digitalarchitecture.api.ManageTemplates;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.TemplateElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.TemplateClassificationProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TemplateManager provides specialist methods for managing templates
 */
public class TemplateManager extends DigitalArchitectureClientBase implements ManageTemplates
{

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String   serverName,
                           String   serverPlatformURLRoot,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String serverName,
                           String serverPlatformURLRoot,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String   serverName,
                           String   serverPlatformURLRoot,
                           String   userId,
                           String   password,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String serverName,
                           String serverPlatformURLRoot,
                           String userId,
                           String password,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String                        serverName,
                           String                        serverPlatformURLRoot,
                           DigitalArchitectureRESTClient restClient,
                           int                           maxPageSize,
                           AuditLog                      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId        calling user
     * @param elementGUID   unique identifier of the element to classify as a template
     * @param properties    properties of the template
     * @param specification values required to use the template
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void addTemplateClassification(String                                 userId,
                                          String                                 elementGUID,
                                          TemplateClassificationProperties       properties,
                                          Map<String, List<Map<String, String>>> specification) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String   methodName = "addTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        ElementProperties elementProperties = null;

        if (properties != null)
        {
            elementProperties = propertyHelper.addStringProperty(null,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 properties.getName());
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 properties.getDescription());
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                 properties.getVersionIdentifier());
            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    properties.getAdditionalProperties());
        }

        openMetadataStoreClient.classifyMetadataElementInStore(userId,
                                                               null,
                                                               null,
                                                               elementGUID,
                                                               OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                               false,
                                                               false,
                                                               null,
                                                               null,
                                                               elementProperties,
                                                               new Date());

        // todo add/update specification
    }


    /**
     * Remove the classification that indicates that this element can be used as a template.
     * Any specification is also removed.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to declassify
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String userId,
                                             String elementGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "removeTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        // todo - both classification and specification
    }


    /**
     * Return a list of templates for a particular open metadata type of element.
     *
     * @param userId        calling user
     * @param typeName      type name to query (if null, any type is returned)
     * @param effectiveTime optional effective time
     * @param startFrom     starting element
     * @param pageSize      maximum number of elements
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<TemplateElement> getTemplatesForType(String userId,
                                                     String typeName,
                                                     Date   effectiveTime,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getTemplatesForType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);

        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      typeName,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      searchClassifications,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertTemplates(openMetadataElements, userId, openMetadataStoreClient);
    }


    /**
     * Convert templates objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param userId calling user
     * @param openHandler client
     * @return list of collection elements
     * @throws PropertyServerException error in retrieved values
     */
    private List<TemplateElement> convertTemplates(List<OpenMetadataElement>  openMetadataElements,
                                                   String                     userId,
                                                   OpenMetadataStoreClient    openHandler) throws PropertyServerException,
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException
    {
        if (openMetadataElements != null)
        {
            List<TemplateElement> templateElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    TemplateElement templateElement = new TemplateElement();

                    templateElement.setRelatedElement(openMetadataElement);
                    templateElement.setSpecification(openHandler.getSpecification(userId,
                                                                                  openMetadataElement.getElementGUID()));
                    templateElements.add(templateElement);
                }
            }

            return templateElements;
        }

        return null;
    }


    /**
     * Find templates with the requested RegEx in the string properties of the template properties.
     *
     * @param userId        calling user
     * @param searchString  string to search for (regEx)
     * @param typeName      type name to query (if null, any type is returned)
     * @param effectiveTime optional effective time
     * @param startFrom     starting element
     * @param pageSize      maximum number of elements
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<TemplateElement> findTemplates(String userId,
                                               String searchString,
                                               String typeName,
                                               Date   effectiveTime,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName    = "findTemplates";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      typeName,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      this.getSearchClassifications(searchString, PropertyComparisonOperator.LIKE),
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertTemplates(openMetadataElements, userId, openMetadataStoreClient);
    }


    /**
     * Return the templates with the requested name.
     *
     * @param userId        calling user
     * @param name          name to search for
     * @param typeName      type name to query (if null, any type is returned)
     * @param effectiveTime optional effective time
     * @param startFrom     starting element
     * @param pageSize      maximum number of elements
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<TemplateElement> getTemplatesByName(String userId,
                                                    String name,
                                                    String typeName,
                                                    Date   effectiveTime,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName    = "getTemplatesByName";
        final String parameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);
        invalidParameterHandler.validateName(name, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      typeName,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      this.getSearchClassifications(name, PropertyComparisonOperator.EQ),
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertTemplates(openMetadataElements, userId, openMetadataStoreClient);
    }


    /**
     * Set up a search specification to locate elements with the Template classification and
     * either the name or description or versionIdentifier matching the requested search value.
     *
     * @param searchValue value to search on
     * @param propertyComparisonOperator comparison operator to use
     * @return search specification
     */
    private SearchClassifications getSearchClassifications(String                     searchValue,
                                                           PropertyComparisonOperator propertyComparisonOperator)
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        requestedPropertyValue.setPrimitiveValue(searchValue);
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        PropertyCondition nameCondition = new PropertyCondition();

        nameCondition.setProperty(OpenMetadataProperty.NAME.name);
        nameCondition.setOperator(propertyComparisonOperator);
        nameCondition.setValue(requestedPropertyValue);

        PropertyCondition descriptionCondition = new PropertyCondition(nameCondition);

        descriptionCondition.setProperty(OpenMetadataProperty.DESCRIPTION.name);

        PropertyCondition versionCondition = new PropertyCondition(nameCondition);

        versionCondition.setProperty(OpenMetadataProperty.VERSION_IDENTIFIER.name);

        List<PropertyCondition> conditions = new ArrayList<>();

        conditions.add(nameCondition);
        conditions.add(descriptionCondition);
        conditions.add(versionCondition);

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return searchClassifications;
    }
}
