/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ValidMetadataValueHandler provides methods to define valid metadata values.
 */
public class ValidMetadataValueHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public ValidMetadataValueHandler(String             localServerName,
                                     AuditLog           auditLog,
                                     String             localServiceName,
                                     OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.VALID_METADATA_VALUE.typeName);
    }


    /**
     * Create a valid metadata values property object from the values supplied by the caller.
     *
     * @param typeName type name (optional)
     * @param propertyName name of property (mandatory)
     * @param mapName map name (optional)
     * @param validMetadataValue properties (mandatory)
     * @return properties for create/update
     */
    private ValidMetadataValueProperties getValidMetadataProperties(String             typeName,
                                                                    String             propertyName,
                                                                    String             mapName,
                                                                    ValidMetadataValue validMetadataValue)
    {
        ValidMetadataValueProperties validMetadataValueProperties = new ValidMetadataValueProperties();

        validMetadataValueProperties.setQualifiedName(OpenMetadataValidValues.constructValidValueQualifiedName(typeName,
                                                                                                               propertyName,
                                                                                                               mapName,
                                                                                                               validMetadataValue.getPreferredValue()));
        validMetadataValueProperties.setIdentifier(propertyName);
        validMetadataValueProperties.setNamespace(mapName);
        validMetadataValueProperties.setUsage(typeName);
        validMetadataValueProperties.setScope(OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE);
        validMetadataValueProperties.setCategory(validMetadataValue.getCategory());
        validMetadataValueProperties.setDisplayName(validMetadataValue.getDisplayName());
        validMetadataValueProperties.setDescription(validMetadataValue.getDescription());
        validMetadataValueProperties.setPreferredValue(validMetadataValue.getPreferredValue());
        validMetadataValueProperties.setDataType(validMetadataValue.getDataType());
        validMetadataValueProperties.setIsCaseSensitive(validMetadataValue.getIsCaseSensitive());
        validMetadataValueProperties.setAdditionalProperties(validMetadataValue.getAdditionalProperties());
        validMetadataValueProperties.setEffectiveFrom(validMetadataValue.getEffectiveFrom());
        validMetadataValueProperties.setEffectiveTo(validMetadataValue.getEffectiveTo());

        return validMetadataValueProperties;
    }


    /**
     * Retrieve/create the parent set for an element.
     *
     * @param userId calling user
     * @param propertyName the property name is used to define the qualified name
     *
     * @return unique identifier of the parent set
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    private String getParentSet(String userId,
                                String propertyName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "getParentSet";

        String qualifiedName = OpenMetadataValidValues.constructValidValueQualifiedName(null,
                                                                                        propertyName,
                                                                                        null,
                                                                                        null);

        OpenMetadataElement validValueSet = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                              qualifiedName,
                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                              null);

        if (validValueSet == null)
        {
            ValidMetadataValueProperties validMetadataValueProperties = new ValidMetadataValueProperties();

            validMetadataValueProperties.setQualifiedName(qualifiedName);
            validMetadataValueProperties.setIdentifier(propertyName);
            validMetadataValueProperties.setDescription("Set of valid values for the " + propertyName + " property.");
            validMetadataValueProperties.setUsage(OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE);
            validMetadataValueProperties.setScope(OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE);

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);

            return super.createNewElement(userId,
                                          newElementOptions,
                                          null,
                                          validMetadataValueProperties,
                                          null,
                                          methodName);
        }
        else
        {
            return validValueSet.getElementGUID();
        }
    }


    /**
     * Retrieve/create the GUID for the metadata element representing a valid value.
     *
     * @param userId calling user
     * @param propertyName the property name is used to define the qualified name
     *
     * @return unique identifier of the parent set
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    private String getValidValueGUID(String userId,
                                     String typeName,
                                     String propertyName,
                                     String mapName,
                                     String preferredValue) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String qualifiedName = OpenMetadataValidValues.constructValidValueQualifiedName(null,
                                                                                        propertyName,
                                                                                        mapName,
                                                                                        preferredValue);

        String validValueGUID =  openMetadataClient.getMetadataElementGUIDByUniqueName(userId,
                                                                                       qualifiedName,
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                       null);

        if (validValueGUID == null)
        {
            qualifiedName = OpenMetadataValidValues.constructValidValueQualifiedName(typeName,
                                                                                     propertyName,
                                                                                     mapName,
                                                                                     preferredValue);

            validValueGUID =  openMetadataClient.getMetadataElementGUIDByUniqueName(userId,
                                                                                    qualifiedName,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    null);
        }

        return validValueGUID;
    }


    /**
     * Retrieve/create the GUID for the metadata element representing a valid value.
     *
     * @param userId calling user
     * @param propertyName the property name is used to define the qualified name
     *
     * @return unique identifier of the parent set
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    private ValidMetadataValueDetail getValidValue(String userId,
                                                   String typeName,
                                                   String propertyName,
                                                   String mapName,
                                                   String preferredValue) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        String qualifiedName = OpenMetadataValidValues.constructValidValueQualifiedName(typeName,
                                                                                        propertyName,
                                                                                        mapName,
                                                                                        preferredValue);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                    qualifiedName,
                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                    null);

        return this.convertValidValue(openMetadataElement);
    }


    /**
     * Retrieve/create the GUID for the metadata element representing a valid value.
     *
     * @param openMetadataElement retrieved element
     *
     * @return unique identifier of the parent set
     */
    private ValidMetadataValueDetail convertValidValue(OpenMetadataElement openMetadataElement)
    {
        final String methodName = "convertValidValue";

        if (openMetadataElement != null)
        {
            ValidMetadataValueDetail validMetadataValue = new ValidMetadataValueDetail();

            /*
             * These first three values show how the property name, type name and map name are stored.
             * These values are controlled by Egeria
             */
            validMetadataValue.setPropertyName(propertyHelper.getStringProperty(localServiceName,
                                                                                OpenMetadataProperty.IDENTIFIER.name,
                                                                                openMetadataElement.getElementProperties(),
                                                                                methodName));
            validMetadataValue.setTypeName(propertyHelper.getStringProperty(localServiceName,
                                                                            OpenMetadataProperty.USAGE.name,
                                                                            openMetadataElement.getElementProperties(),
                                                                            methodName));
            validMetadataValue.setMapName(propertyHelper.getStringProperty(localServiceName,
                                                                            OpenMetadataProperty.NAMESPACE.name,
                                                                            openMetadataElement.getElementProperties(),
                                                                            methodName));

            /*
             * These are the values that are controlled by the caller.
             */
            validMetadataValue.setCategory(propertyHelper.getStringProperty(localServiceName,
                                                                            OpenMetadataProperty.CATEGORY.name,
                                                                            openMetadataElement.getElementProperties(),
                                                                            methodName));
            validMetadataValue.setDisplayName(propertyHelper.getStringProperty(localServiceName,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               openMetadataElement.getElementProperties(),
                                                                               methodName));
            validMetadataValue.setDescription(propertyHelper.getStringProperty(localServiceName,
                                                                               OpenMetadataProperty.DESCRIPTION.name,
                                                                               openMetadataElement.getElementProperties(),
                                                                               methodName));
            validMetadataValue.setPreferredValue(propertyHelper.getStringProperty(localServiceName,
                                                                                  OpenMetadataProperty.PREFERRED_VALUE.name,
                                                                                  openMetadataElement.getElementProperties(),
                                                                                  methodName));
            validMetadataValue.setDataType(propertyHelper.getStringProperty(localServiceName,
                                                                            OpenMetadataProperty.DATA_TYPE.name,
                                                                            openMetadataElement.getElementProperties(),
                                                                            methodName));

            validMetadataValue.setIsCaseSensitive(propertyHelper.getBooleanProperty(localServiceName,
                                                                                    OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                                                    openMetadataElement.getElementProperties(),
                                                                                    methodName));
            validMetadataValue.setAdditionalProperties(propertyHelper.getStringMapFromProperty(localServiceName,
                                                                                               OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                               openMetadataElement.getElementProperties(),
                                                                                               methodName));

            validMetadataValue.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
            validMetadataValue.setEffectiveTo(openMetadataElement.getEffectiveToTime());

            return validMetadataValue;
        }

        return null;
    }


    /**
     * Retrieve/create/update the metadata element for a valid metadata value.
     *
     * @param userId calling user
     * @param propertyName the property name is used to define the qualified name
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    private void setUpValidValue(String             userId,
                                 String             typeName,
                                 String             propertyName,
                                 String             mapName,
                                 ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "getValidValue";

        String qualifiedName = OpenMetadataValidValues.constructValidValueQualifiedName(typeName,
                                                                                        propertyName,
                                                                                        mapName,
                                                                                        validMetadataValue.getPreferredValue());

        OpenMetadataElement validValueSet = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                              qualifiedName,
                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                              null);

        /*
         * The valid value set will either be created, or updated depending on whether it exists or not.
         * Convert the supplied properties into the appropriate bean.
         */
        ValidMetadataValueProperties validMetadataValueProperties = getValidMetadataProperties(typeName, propertyName, mapName, validMetadataValue);

        String validValueGUID;

        if (validValueSet == null)
        {
            /*
             * The element needs creating.  It is set up as a member of the parent set.
             */
            String parentSetGUID = this.getParentSet(userId, propertyName);


            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentSetGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(parentSetGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);

            ValidValueMemberProperties memberProperties = new ValidValueMemberProperties();

            memberProperties.setIsDefaultValue(false);

            validValueGUID = super.createNewElement(userId,
                                                    newElementOptions,
                                                    null,
                                                    validMetadataValueProperties,
                                                    memberProperties,
                                                    methodName);
        }
        else
        {
            /*
             * The element is updated - if no changes have been made then the
             * update is ignored.
             */
            final String guidParameterName = "validValueSet.getElementGUID()";

            UpdateOptions updateOptions = new UpdateOptions();

            updateOptions.setMergeUpdate(false);

            super.updateElement(userId,
                                validValueSet.getElementGUID(),
                                guidParameterName,
                                updateOptions,
                                validMetadataValueProperties,
                                methodName);

            validValueGUID = validValueSet.getElementGUID();
        }

        auditLog.logMessage(methodName, OMFAuditCode.VALID_METADATA_UPDATE.getMessageDefinition(validMetadataValue.getPreferredValue(),
                                                                                                propertyName,
                                                                                                validValueGUID));
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataValue(String             userId,
                                        String             typeName,
                                        String             propertyName,
                                        ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "setUpValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateObject(validMetadataValue, propertiesParameterName, methodName);

        setUpValidValue(userId,
                        typeName,
                        propertyName,
                        null,
                        validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapName(String             userId,
                                          String             typeName,
                                          String             propertyName,
                                          ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateObject(validMetadataValue, propertiesParameterName, methodName);

        setUpValidValue(userId,
                        typeName,
                        propertyName,
                        null,
                        validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapValue(String             userId,
                                           String             typeName,
                                           String             propertyName,
                                           String             mapName,
                                           ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertiesParameterName = "validMetadataValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateObject(validMetadataValue, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(mapName, mapNameParameterName, methodName);

        setUpValidValue(userId,
                        typeName,
                        propertyName,
                        mapName,
                        validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param deleteOptions options to control the delete operation
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataValue(String        userId,
                                        String        typeName,
                                        String        propertyName,
                                        String        preferredValue,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "clearValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, null, preferredValue);

        if (validValueGUID != null)
        {
            openMetadataClient.deleteMetadataElementInStore(userId, validValueGUID, deleteOptions);
        }

        auditLog.logMessage(methodName,
                            OMFAuditCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName));

        throw new InvalidParameterException(OMFErrorCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName),
                                            this.getClass().getName(),
                                            methodName,
                                            OpenMetadataProperty.PREFERRED_VALUE.name);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param deleteOptions options to control the delete operation
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapName(String        userId,
                                          String        typeName,
                                          String        propertyName,
                                          String        preferredValue,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, null, preferredValue);

        if (validValueGUID != null)
        {
            openMetadataClient.deleteMetadataElementInStore(userId, validValueGUID, deleteOptions);
        }

        auditLog.logMessage(methodName,
                            OMFAuditCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName));

        throw new InvalidParameterException(OMFErrorCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName),
                                            this.getClass().getName(),
                                            methodName,
                                            OpenMetadataProperty.PREFERRED_VALUE.name);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param deleteOptions options to control the delete operation
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapValue(String        userId,
                                           String        typeName,
                                           String        propertyName,
                                           String        mapName,
                                           String        preferredValue,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);
        propertyHelper.validateMandatoryName(mapName, mapNameParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, null, preferredValue);

        if (validValueGUID != null)
        {
            openMetadataClient.deleteMetadataElementInStore(userId, validValueGUID, deleteOptions);
        }

        auditLog.logMessage(methodName,
                            OMFAuditCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName));

        throw new InvalidParameterException(OMFErrorCode.VALID_METADATA_MISSING.getMessageDefinition(preferredValue, propertyName),
                                            this.getClass().getName(),
                                            methodName,
                                            OpenMetadataProperty.PREFERRED_VALUE.name);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param userId caller's userId
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
    public boolean validateMetadataValue(String userId,
                                         String typeName,
                                         String propertyName,
                                         String actualValue) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "validateMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "actualName";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(actualValue, propertyValueParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, null, actualValue);

        return (validValueGUID != null);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param userId caller's userId
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
    public boolean validateMetadataMapName(String userId,
                                           String typeName,
                                           String propertyName,
                                           String actualValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "validateMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "actualName";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(actualValue, propertyValueParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, null, actualValue);

        return (validValueGUID != null);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public boolean validateMetadataMapValue(String userId,
                                            String typeName,
                                            String propertyName,
                                            String mapName,
                                            String actualValue) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "validateMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertyValueParameterName = "actualName";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(actualValue, propertyValueParameterName, methodName);
        propertyHelper.validateMandatoryName(mapName, mapNameParameterName, methodName);

        String validValueGUID = this.getValidValueGUID(userId, typeName, propertyName, mapName, actualValue);

        return (validValueGUID != null);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param userId caller's userId
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
    public ValidMetadataValueDetail getValidMetadataValue(String userId,
                                                          String typeName,
                                                          String propertyName,
                                                          String preferredValue) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);

        return this.getValidValue(userId,
                                  typeName,
                                  propertyName,
                                  null,
                                  preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param userId caller's userId
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
    public ValidMetadataValueDetail getValidMetadataMapName(String userId,
                                                            String typeName,
                                                            String propertyName,
                                                            String preferredValue) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "getValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);

        return this.getValidValue(userId,
                                  typeName,
                                  propertyName,
                                  null,
                                  preferredValue);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueDetail getValidMetadataMapValue(String userId,
                                                             String typeName,
                                                             String propertyName,
                                                             String mapName,
                                                             String preferredValue) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);
        propertyHelper.validateMandatoryName(mapName, mapNameParameterName, methodName);

        return this.getValidValue(userId,
                                  typeName,
                                  propertyName,
                                  mapName,
                                  preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param userId caller's userId
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
    public List<ValidMetadataValueDetail> getValidMetadataValues(String userId,
                                                                 String typeName,
                                                                 String propertyName,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getValidMetadataValues";
        final String propertyNameParameterName = "propertyName";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);

        String parentSetGUID = this.getParentSet(userId, propertyName);

        if (parentSetGUID != null)
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.VALID_METADATA_VALUE.typeName);
            queryOptions.setStartFrom(startFrom);
            queryOptions.setPageSize(pageSize);

            RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                               parentSetGUID,
                                                                                                               0,
                                                                                                               OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                                               queryOptions);
            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                List<ValidMetadataValueDetail> results = new ArrayList<>();
                Map<String, List<ValidMetadataValue>> mapValues = new HashMap<>();

                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        String usage = propertyHelper.getStringProperty(localServiceName,
                                                                        OpenMetadataProperty.USAGE.name,
                                                                        relatedMetadataElement.getElement().getElementProperties(),
                                                                        methodName);

                        String namespace = propertyHelper.getStringProperty(localServiceName,
                                                                            OpenMetadataProperty.NAMESPACE.name,
                                                                            relatedMetadataElement.getElement().getElementProperties(),
                                                                            methodName);

                        if ((typeName == null) || (usage == null) || (usage.equals(typeName))) // todo need to do a proper subtype test
                        {
                            ValidMetadataValueDetail validMetadataValue = this.convertValidValue(relatedMetadataElement.getElement());

                            if (namespace == null)
                            {
                                results.add(validMetadataValue);
                            }
                            else
                            {
                                List<ValidMetadataValue> mapList = mapValues.get(namespace);

                                if (mapList == null)
                                {
                                    mapList = new ArrayList<>();
                                }

                                mapList.add(validMetadataValue);

                                mapValues.put(namespace, mapList);
                            }
                        }
                    }
                }

                if (! mapValues.isEmpty())
                {
                    for (String mapValue : mapValues.keySet())
                    {
                        ValidMetadataValueDetail validMetadataValueDetail = new ValidMetadataValueDetail();

                        validMetadataValueDetail.setDescription("Set of valid values for the " + mapValue + " map name of property" + propertyName + ".");
                        validMetadataValueDetail.setDisplayName(mapValue);

                        validMetadataValueDetail.setValidMapNameValues(mapValues.get(mapValue));

                        results.add(validMetadataValueDetail);
                    }
                }

                return results;
            }
        }


        return null;
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName optional name of map key that this valid value applies
     * @param preferredValue the value to match against
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<ValidMetadataValueDetail> getConsistentMetadataValues(String userId,
                                                                      String typeName,
                                                                      String propertyName,
                                                                      String mapName,
                                                                      String preferredValue,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getConsistentMetadataValues";
        final String propertyNameParameterName = "propertyName";
        final String propertyValueParameterName = "preferredValue";

        propertyHelper.validateMandatoryName(propertyName, propertyNameParameterName, methodName);
        propertyHelper.validateMandatoryName(preferredValue, propertyValueParameterName, methodName);

        String validValueGUID = getValidValueGUID(userId, typeName, propertyName, mapName, preferredValue);

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.VALID_METADATA_VALUE.typeName);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(pageSize);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           validValueGUID,
                                                                                                           0,
                                                                                                           OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<ValidMetadataValueDetail> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(this.convertValidValue(relatedMetadataElement.getElement()));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Set up consistent metadata values relationship between the two property values.
     *
     * @param userId caller's userId
     * @param typeName1 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName1 name of property that this valid value applies
     * @param mapName1 optional name of map key that this valid value applies
     * @param preferredValue1 the value to match against
     * @param typeName2 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName2 name of property that this valid value applies
     * @param mapName2 optional name of map key that this valid value applies
     * @param preferredValue2 the value to match against
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setConsistentMetadataValues(String userId,
                                            String typeName1,
                                            String propertyName1,
                                            String mapName1,
                                            String preferredValue1,
                                            String typeName2,
                                            String propertyName2,
                                            String mapName2,
                                            String preferredValue2) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "setConsistentMetadataValues";
        final String propertyName1ParameterName = "propertyName1";
        final String preferredValue1ParameterName = "preferredValue1";
        final String propertyName2ParameterName = "propertyName2";
        final String preferredValue2ParameterName = "preferredValue2";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(propertyName1, propertyName1ParameterName, methodName);
        propertyHelper.validateObject(preferredValue1, preferredValue1ParameterName, methodName);
        propertyHelper.validateMandatoryName(propertyName2, propertyName2ParameterName, methodName);
        propertyHelper.validateObject(preferredValue2, preferredValue2ParameterName, methodName);

        String validValue1GUID = getValidValueGUID(userId, typeName1, propertyName1, mapName1, preferredValue1);
        String validValue2GUID = getValidValueGUID(userId, typeName2, propertyName2, mapName2, preferredValue2);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                                        validValue1GUID,
                                                        validValue2GUID,
                                                        new MetadataSourceOptions(),
                                                        null);
    }
}
