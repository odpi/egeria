/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * SpecificationPropertyHandler provides methods to define specification properties.
 */
public class SpecificationPropertyHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public SpecificationPropertyHandler(String             localServerName,
                                        AuditLog           auditLog,
                                        String             localServiceName,
                                        OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName);
    }


    /**
     * Create an ope metadata properties object from the values supplied by the caller.
     *
     * @param specificationPropertyType name of property
     * @param specificationProperty properties
     * @return properties for create/update
     */
    private SpecificationPropertyValueProperties getSpecificationPropertyValueProperties(String                    parentGUID,
                                                                                         String                    parentTypeName,
                                                                                         SpecificationPropertyType specificationPropertyType,
                                                                                         SpecificationProperty     specificationProperty)
    {
        SpecificationPropertyValueProperties specificationPropertyValueProperties = new SpecificationPropertyValueProperties();

        specificationPropertyValueProperties.setQualifiedName(parentTypeName + "::" + parentGUID + "::" + specificationPropertyType.getPropertyType() + "::" + specificationProperty.getName());
        specificationPropertyValueProperties.setDisplayName(specificationProperty.getName());
        specificationPropertyValueProperties.setPreferredValue(specificationProperty.getName());
        specificationPropertyValueProperties.setDescription(specificationProperty.getDescription());
        specificationPropertyValueProperties.setIdentifier(specificationPropertyType.getPropertyType());
        specificationPropertyValueProperties.setCategory(Category.SPECIFICATION_PROPERTY.getName());
        specificationPropertyValueProperties.setNamespace(specificationPropertyType.getPropertyType());
        specificationPropertyValueProperties.setUsage(specificationPropertyType.getDescription());
        specificationPropertyValueProperties.setScope(null);
        specificationPropertyValueProperties.setIsCaseSensitive(true);

        Map<String, String> additionalProperties = specificationProperty.getOtherPropertyValues();

        if (additionalProperties == null)
        {
            additionalProperties = new HashMap<>();
        }

        if (specificationProperty instanceof ActionTargetType actionTargetType)
        {
            specificationPropertyValueProperties.setDataType(actionTargetType.getOpenMetadataTypeName());

            additionalProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, actionTargetType.getDeployedImplementationType());
            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(actionTargetType.getRequired()));
        }
        else if (specificationProperty instanceof AnalysisStepType analysisStepType)
        {

            specificationPropertyValueProperties.setPreferredValue(analysisStepType.getName());
        }
        else if (specificationProperty instanceof AnnotationTypeType annotationTypeType)
        {
            specificationPropertyValueProperties.setDataType(annotationTypeType.getOpenMetadataTypeName());

            additionalProperties.put(OpenMetadataProperty.EXPRESSION.name, annotationTypeType.getExpression());
            additionalProperties.put(OpenMetadataProperty.EXPLANATION.name, annotationTypeType.getExplanation());
            additionalProperties.put(OpenMetadataProperty.ANALYSIS_STEP.name, annotationTypeType.getAnalysisStepName());
            additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, annotationTypeType.getOpenMetadataTypeName());
        }
        else if (specificationProperty instanceof ConfigurationPropertyType configurationPropertyType)
        {
            specificationPropertyValueProperties.setDataType(configurationPropertyType.getDataType());

            additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, configurationPropertyType.getExample());
            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(configurationPropertyType.getRequired()));
        }
        else if (specificationProperty instanceof GuardType guardType)
        {
            specificationPropertyValueProperties.setDataType(DataType.STRING.getName());

            if (guardType.getCompletionStatus() != null)
            {
                specificationPropertyValueProperties.setScope(guardType.getCompletionStatus().getName());
            }

            additionalProperties.put("completionStatus", guardType.getCompletionStatus().getName());
            additionalProperties.put("completionStatusDescription", guardType.getCompletionStatus().getDescription());
        }
        else if (specificationProperty instanceof PlaceholderPropertyType placeholderPropertyType)
        {
            specificationPropertyValueProperties.setDataType(placeholderPropertyType.getDataType());

            additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, placeholderPropertyType.getExample());
            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(placeholderPropertyType.getRequired()));
        }
        else if (specificationProperty instanceof ReplacementAttributeType replacementAttributeType)
        {
            specificationPropertyValueProperties.setDataType(replacementAttributeType.getDataType());

            additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, replacementAttributeType.getExample());
            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(replacementAttributeType.getRequired()));
        }
        else if (specificationProperty instanceof RequestParameterType requestParameterType)
        {
            specificationPropertyValueProperties.setDataType(requestParameterType.getDataType());

            additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, requestParameterType.getExample());
            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(requestParameterType.getRequired()));
        }
        else if (specificationProperty instanceof TemplateType templateType)
        {
            specificationPropertyValueProperties.setDataType(templateType.getOpenMetadataTypeName());

            additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(templateType.getRequired()));
        }

        if (! additionalProperties.isEmpty())
        {
            specificationPropertyValueProperties.setAdditionalProperties(additionalProperties);
        }

        return specificationPropertyValueProperties;
    }


    /**
     * Retrieve/create/update the metadata element for a specification property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to connect it to
     * @param specificationProperty the property description
     * @param metadataSourceOptions query options
     *
     * @return unique identifier of new specification property
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public String setUpSpecificationProperty(String                    userId,
                                             String                    elementGUID,
                                             SpecificationProperty     specificationProperty,
                                             MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "setUpSpecificationProperty";
        final String specificationPropertyTypeParameterName = "specificationPropertyType";
        final String specificationPropertyParameterName = "specificationProperty";
        final String specificationPropertyNameParameterName = "specificationProperty.name";

        propertyHelper.validateObject(specificationProperty, specificationPropertyParameterName, methodName);
        propertyHelper.validateObject(specificationProperty.getSpecificationPropertyType(), specificationPropertyTypeParameterName, methodName);
        propertyHelper.validateMandatoryName(specificationProperty.getName(), specificationPropertyNameParameterName, methodName);

        OpenMetadataElement parentElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                        elementGUID,
                                                                                        new GetOptions(metadataSourceOptions));

        if (parentElement != null)
        {
            SpecificationPropertyAssignmentProperties relationshipProperties = new SpecificationPropertyAssignmentProperties();

            relationshipProperties.setPropertyName(specificationProperty.getSpecificationPropertyType().getPropertyType());

            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName);

            return super.createNewElement(userId,
                                          newElementOptions,
                                          null,
                                          this.getSpecificationPropertyValueProperties(parentElement.getElementGUID(),
                                                                                       parentElement.getType().getTypeName(),
                                                                                       specificationProperty.getSpecificationPropertyType(),
                                                                                       specificationProperty),
                                          relationshipProperties,
                                          methodName);
        }

        return null;
    }


    /**
     * Remove a specification property.
     *
     * @param userId caller's userId
     * @param specificationPropertyGUID unique identifier of the specification property element to remove
     * @param deleteOptions options to control the delete operation
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void deleteSpecificationProperty(String        userId,
                                            String        specificationPropertyGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, specificationPropertyGUID, deleteOptions);
    }


    /**
     * Return the possible specification property types.
     *
     * @return string map
     */
    public Map<String, String> getSpecificationPropertyTypes()
    {
        Map<String, String> results = new HashMap<>();

        for (SpecificationPropertyType specificationPropertyType : SpecificationPropertyType.values())
        {
            results.put(specificationPropertyType.getBeanClass(), specificationPropertyType.getPropertyType() + " --> " + specificationPropertyType.getDescription());
        }

        return results;
    }


    /**
     * Return the requested specification property.
     *
     * @param userId       userId of user making request.
     * @param specificationPropertyGUID  unique identifier for the specification property object.
     * @param getOptions multiple options to control the query
     * @return specification property properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getSpecificationPropertyByGUID(String     userId,
                                                                  String     specificationPropertyGUID,
                                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getSpecificationPropertyByGUID";

        return super.getRootElementByGUID(userId, specificationPropertyGUID, getOptions, methodName);
    }


    /**
     * Return the list of keywords exactly matching the supplied specification property type.
     *
     * @param userId the name of the calling user.
     * @param specificationPropertyType name of specificationPropertyType.
     * @param queryOptions multiple options to control the query
     *
     * @return specificationPropertyType list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSpecificationPropertiesByType(String                    userId,
                                                                          SpecificationPropertyType specificationPropertyType,
                                                                          QueryOptions              queryOptions) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getSpecificationPropertiesByType";
        final String nameParameterName = "specificationPropertyType";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(specificationPropertyType, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           specificationPropertyType.getPropertyType(),
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }

    /**
     * Return the list of keywords exactly matching the supplied specification property type.
     *
     * @param userId the name of the calling user.
     * @param name name of name.
     * @param queryOptions multiple options to control the query
     *
     * @return name list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSpecificationPropertiesByName(String       userId,
                                                                          String       name,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getSpecificationPropertiesByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.PREFERRED_VALUE.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Retrieve the list of specification property metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSpecificationProperties(String        userId,
                                                                     String        searchString,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "findSpecificationProperties";

        return super.findRootElements(userId, searchString, searchOptions,methodName);
    }
}
