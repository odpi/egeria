/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;


import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SourcedFromProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TemplateManager provides specialist methods for managing templates
 */
public class TemplateHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public TemplateHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             localServiceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.REFERENCEABLE.typeName);
    }
    

    /**
     * Classify an element as suitable to be used as a template for cataloguing elements of a similar types.
     *
     * @param userId        calling user
     * @param elementGUID   unique identifier of the element to classify as a template
     * @param properties    properties of the template
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateClassification(String                 userId,
                                          String                 elementGUID,
                                          TemplateProperties     properties,
                                          MetadataSourceOptions  metadataSourceOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String   methodName = "addTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameter, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the classification that indicates that this element can be used as a template.
     * Any specification is also removed.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String                userId,
                                             String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String   methodName = "removeTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameter, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId, elementGUID, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName, metadataSourceOptions);
    }



    /**
     * Classify an element as a template substitute.
     *
     * @param userId        calling user
     * @param elementGUID   unique identifier of the element to classify as a template
     * @param properties    properties of the template
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateSubstituteClassification(String                       userId,
                                                    String                       elementGUID,
                                                    TemplateSubstituteProperties properties,
                                                    MetadataSourceOptions        metadataSourceOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String   methodName = "addTemplateSubstituteClassification";
        final String   elementGUIDParameter = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameter, methodName);



        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the TemplateSubstitute classification.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to declassify
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateSubstituteClassification(String                userId,
                                                       String                elementGUID,
                                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String   methodName = "removeTemplateSubstituteClassification";
        final String   elementGUIDParameter = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameter, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId, elementGUID, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName, metadataSourceOptions);
    }


    /**
     * Attach a template to an element that was created from it.
     *
     * @param userId                 userId of user making request
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSourcedFrom(String                userId,
                                String                elementGUID,
                                String                templateGUID,
                                MetadataSourceOptions metadataSourceOptions,
                                SourcedFromProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkSourcedFrom";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "templateGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(templateGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        templateGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a source template for an element.
     *
     * @param userId            userId of user making request
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSourcedFrom(String        userId,
                                  String        elementGUID,
                                  String        templateGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "detachSourcedFrom";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "templateGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(templateGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        templateGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a template to an element that this template is relevant to.   For example, a project.
     *
     * @param userId                 userId of user making request
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCatalogTemplate(String                    userId,
                                    String                    elementGUID,
                                    String                    templateGUID,
                                    MetadataSourceOptions     metadataSourceOptions,
                                    CatalogTemplateProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkCatalogTemplate";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "templateGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(templateGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        templateGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a template for an element that this template is relevant to.   For example, a project.
     *
     * @param userId            userId of user making request.
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCatalogTemplate(String        userId,
                                      String        elementGUID,
                                      String        templateGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachCatalogTemplate";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "templateGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(templateGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        templateGUID,
                                                        deleteOptions);
    }


    /**
     * Return the templates with the requested name.
     *
     * @param userId        calling user
     * @param name          name to search for
     * @param queryOptions  options to control the query
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> getTemplatesByName(String       userId,
                                                            String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName    = "getTemplatesByName";
        final String parameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);
        propertyHelper.validateMandatoryName(name, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 this.getSearchClassifications(name, PropertyComparisonOperator.EQ),
                                                                                                 queryOptions);

        return super.convertRootElements(userId, openMetadataElements, queryOptions, methodName);
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

        if (PropertyComparisonOperator.LIKE.equals(propertyComparisonOperator))
        {
            requestedPropertyValue.setPrimitiveValue(".*" + Pattern.quote(searchValue) + ".*");
        }
        else
        {
            requestedPropertyValue.setPrimitiveValue(searchValue);
        }
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        PropertyCondition nameCondition = new PropertyCondition();

        nameCondition.setProperty(OpenMetadataProperty.DISPLAY_NAME.name);
        nameCondition.setOperator(propertyComparisonOperator);
        nameCondition.setValue(requestedPropertyValue);

        PropertyCondition resourceNameCondition = new PropertyCondition();

        nameCondition.setProperty(OpenMetadataProperty.RESOURCE_NAME.name);
        nameCondition.setOperator(propertyComparisonOperator);
        nameCondition.setValue(requestedPropertyValue);

        PropertyCondition descriptionCondition = new PropertyCondition(nameCondition);

        descriptionCondition.setProperty(OpenMetadataProperty.DESCRIPTION.name);

        PropertyCondition versionCondition = new PropertyCondition(nameCondition);

        versionCondition.setProperty(OpenMetadataProperty.VERSION_IDENTIFIER.name);

        List<PropertyCondition> conditions = new ArrayList<>();

        conditions.add(nameCondition);
        conditions.add(resourceNameCondition);
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
