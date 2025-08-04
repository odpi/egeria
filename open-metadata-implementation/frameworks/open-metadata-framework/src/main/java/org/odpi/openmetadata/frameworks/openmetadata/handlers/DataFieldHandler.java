/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.DataFieldConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.DataFieldMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFieldElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MemberDataField;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Data field handler describes how to maintain and query data fields.
 * They are organized into specialized collections called data dictionaries and data specs (supported with the
 * collection manager).
 */
public class DataFieldHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DataFieldHandler(String             localServerName,
                            AuditLog           auditLog,
                            String             serviceName,
                            OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.DATA_FIELD.typeName);
    }



    /**
     * Create a new data field.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDataField(String                                userId,
                                  NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataFieldProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createDataField";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a data field using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new data field.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
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
    public String createDataFieldFromTemplate(String                 userId,
                                              TemplateOptions        templateOptions,
                                              String                 templateGUID,
                                              ElementProperties      replacementProperties,
                                              Map<String, String>    placeholderProperties,
                                              RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }

    /**
     * Update the properties of a data field.
     *
     * @param userId                 userId of user making request.
     * @param dataFieldGUID          unique identifier of the data field (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateDataField(String              userId,
                                String              dataFieldGUID,
                                UpdateOptions       updateOptions,
                                DataFieldProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateDataField";
        final String guidParameterName = "dataFieldGUID";

        super.updateElement(userId,
                            dataFieldGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Connect two data field as parent and child.
     *
     * @param userId                 userId of user making request
     * @param parentDataFieldGUID    unique identifier of the parent data field
     * @param nestedDataFieldGUID    unique identifier of the child data field
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDataFields(String                    userId,
                                     String                    parentDataFieldGUID,
                                     String                    nestedDataFieldGUID,
                                     MetadataSourceOptions     metadataSourceOptions,
                                     MemberDataFieldProperties relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "linkNestedDataFields";
        final String end1GUIDParameterName = "parentDataFieldGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataFieldGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedDataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                        parentDataFieldGUID,
                                                        nestedDataFieldGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two data fields from one another.
     *
     * @param userId                 userId of user making request.
     * @param parentDataFieldGUID    unique identifier of the parent data field.
     * @param nestedDataFieldGUID    unique identifier of the child data field.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDataFields(String        userId,
                                       String        parentDataFieldGUID,
                                       String        nestedDataFieldGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachNestedDataFields";
        final String end1GUIDParameterName = "parentDataFieldGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataFieldGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedDataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                        parentDataFieldGUID,
                                                        nestedDataFieldGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a data field.
     *
     * @param userId                 userId of user making request.
     * @param dataFieldGUID          unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataField(String        userId,
                                String        dataFieldGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "deleteDataField";
        final String guidParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataFieldGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, dataFieldGUID, deleteOptions);
    }


    /**
     * Returns the list of data fields with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<DataFieldElement> getDataFieldsByName(String       userId,
                                                      String       name,
                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getDataFieldsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);

        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 super.addDefaultType(queryOptions));

        return convertDataFields(userId, openMetadataElements, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific data field.
     *
     * @param userId                 userId of user making request
     * @param dataFieldGUID          unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DataFieldElement getDataFieldByGUID(String     userId,
                                               String     dataFieldGUID,
                                               GetOptions getOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getDataFieldByGUID";
        final String guidParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataFieldGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              dataFieldGUID,
                                                                                              getOptions);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, metadataElementTypeName)))
        {
            return convertDataField(userId,
                                    openMetadataElement,
                                    new QueryOptions(getOptions),
                                    methodName);
        }

        return null;
    }


    /**
     * Retrieve the list of data fields metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFieldElement> findDataFields(String        userId,
                                                 String        searchString,
                                                 SearchOptions searchOptions) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "findDataFields";
        final String searchStringParameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, searchStringParameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           super.addDefaultType(searchOptions));

        return convertDataFields(userId, openMetadataElements, super.addDefaultType(searchOptions), methodName);
    }

    /*
     * Converter functions
     */


    /**
     * Convert the open metadata elements retrieved into data field elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param queryOptions multiple options to control the query
     * @param methodName calling method
     * @return list of data fields (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<DataFieldElement> convertDataFields(String                    userId,
                                                     List<OpenMetadataElement> openMetadataElements,
                                                     QueryOptions              queryOptions,
                                                     String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<DataFieldElement> dataFieldElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    dataFieldElements.add(convertDataField(userId, openMetadataElement, queryOptions, methodName));
                }
            }

            return dataFieldElements;
        }

        return null;
    }


    /**
     * Return the data field extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param queryOptions multiple options to control the query
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private DataFieldElement convertDataField(String              userId,
                                              OpenMetadataElement openMetadataElement,
                                              QueryOptions        queryOptions,
                                              String              methodName) throws PropertyServerException
    {
        try
        {
            List<MemberDataField>        nestedDataFields = new ArrayList<>();
            List<RelatedMetadataElement> otherRelatedElements = new ArrayList<>();

            QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
            workingQueryOptions.setStartFrom(0);
            workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  openMetadataElement.getElementGUID(),
                                                                                                                  0,
                                                                                                                  null,
                                                                                                                  workingQueryOptions);
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName)) && (!relatedMetadataElement.getElementAtEnd1()))
                        {
                            nestedDataFields.add(this.convertMemberDataField(userId, relatedMetadataElement, queryOptions, methodName));
                        }
                        else
                        {
                            otherRelatedElements.add(relatedMetadataElement);
                        }
                    }
                }

                workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());
                relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                           openMetadataElement.getElementGUID(),
                                                                                           0,
                                                                                           null,
                                                                                           workingQueryOptions);
            }

            DataFieldConverter<DataFieldElement> converter = new DataFieldConverter<>(propertyHelper, localServiceName, localServerName);
            DataFieldElement dataFieldElement = converter.getNewComplexBean(DataFieldElement.class,
                                                                            openMetadataElement,
                                                                            otherRelatedElements,
                                                                            methodName);
            if (dataFieldElement != null)
            {
                if (! nestedDataFields.isEmpty())
                {
                    dataFieldElement.setNestedDataFields(nestedDataFields);
                }

                DataFieldMermaidGraphBuilder graphBuilder = new DataFieldMermaidGraphBuilder(dataFieldElement);

                dataFieldElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return dataFieldElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                               methodName,
                                                                                                       localServiceName,
                                                                                                               error.getMessage()),
                                      error);
            }

            throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                               localServiceName,
                                                                                                                       error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }
    }
}
