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
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TemplateManager provides specialist methods for managing templates
 */
public class TemplateHandler extends StewardshipManagementHandler
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
              openMetadataClient);
    }
    

    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
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

        ElementProperties elementProperties = null;

        if (properties != null)
        {
            elementProperties = propertyHelper.addStringProperty(null,
                                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                                 properties.getDisplayName());
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

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          new NewElementProperties(elementProperties));
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
    public void removeTemplateClassification(String userId,
                                             String elementGUID,
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
     * Return a list of templates for a particular open metadata type of element.
     *
     * @param userId        calling user
     * @param queryOptions options for query
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> getTemplatesForType(String      userId,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getTemplatesForType";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);

        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 searchClassifications,
                                                                                                 queryOptions);

        return super.convertRootElements(userId, openMetadataElements, queryOptions, methodName);
    }



    /**
     * Find templates with the requested RegEx in the string properties of the template properties.
     *
     * @param userId        calling user
     * @param searchString  string to search for (regEx)
     * @param searchOptions options to control the search
     * @return list of templates
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<OpenMetadataRootElement> findTemplates(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName    = "findTemplates";
        final String parameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(searchOptions, openMetadataClient.getMaxPagingSize(), methodName);
        propertyHelper.validateSearchString(searchString, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 null,
                                                                                                 this.getSearchClassifications(searchString, PropertyComparisonOperator.LIKE),
                                                                                                 searchOptions);

        return super.convertRootElements(userId, openMetadataElements, searchOptions, methodName);
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
     * Build a descriptions of the classification to look for.
     *
     * @param getTemplates look for templates?
     * @return match classifications
     */
    private SearchClassifications getMatchClassifications(boolean getTemplates)
    {
        SearchClassifications matchClassifications = null;

        if (getTemplates)
        {
            /*
             * Attempt to retrieve only elements that have the template classification.
             */
            matchClassifications = new SearchClassifications();

            List<ClassificationCondition> classificationConditions  = new ArrayList<>();
            ClassificationCondition classificationCondition = new ClassificationCondition();
            classificationCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);
            classificationConditions.add(classificationCondition);
            matchClassifications.setConditions(classificationConditions);
            matchClassifications.setMatchCriteria(MatchCriteria.ALL);
        }

        return matchClassifications;
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
