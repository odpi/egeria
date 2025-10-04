/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValueDetail;

import java.util.List;

/**
 * ValidMetadataValues maintains valid value sets for the properties found in open metadata elements.  Valid values can be set up
 * for a property name within a specific type - or for all types that use that property name.  The named property may be a string type,
 * and array of string or a map of names to string values.
 */
public class ValidMetadataValuesClient  extends ConnectorContextClientBase
{
    private final OpenMetadataClient openMetadataClient;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ValidMetadataValuesClient(ConnectorContextBase     parentContext,
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
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.openMetadataClient = openMetadataClient;
    }


    /**
     * Create or update the valid value for a particular open metadata property.  This property may be of type string or an array of strings.
     * The valid value is stored in the preferredValue property of validMetadataValue.  If the property is of type string, this property
     * can be stored as the property value.  If the property is an array of strings, an element in the array can be of this value.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
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
                                        ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        openMetadataClient.setUpValidMetadataValue(connectorUserId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapName(String             typeName,
                                          String             propertyName,
                                          ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        openMetadataClient.setUpValidMetadataMapName(connectorUserId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapValue(String             typeName,
                                           String             propertyName,
                                           String             mapName,
                                           ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        openMetadataClient.setUpValidMetadataMapValue(connectorUserId, typeName, propertyName, mapName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.  The match is done on preferred name.
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
        openMetadataClient.clearValidMetadataValue(connectorUserId, typeName, propertyName, preferredValue);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapName(String typeName,
                                          String propertyName,
                                          String preferredValue) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        openMetadataClient.clearValidMetadataMapName(connectorUserId, typeName, propertyName, preferredValue);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapValue(String typeName,
                                           String propertyName,
                                           String mapName,
                                           String preferredValue) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataClient.clearValidMetadataMapValue(connectorUserId, typeName, propertyName, mapName, preferredValue);
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
        return openMetadataClient.validateMetadataValue(connectorUserId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
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
    public boolean validateMetadataMapName(String typeName,
                                           String propertyName,
                                           String actualValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return openMetadataClient.validateMetadataMapName(connectorUserId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
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
    public boolean validateMetadataMapValue(String typeName,
                                            String propertyName,
                                            String mapName,
                                            String actualValue) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return openMetadataClient.validateMetadataMapValue(connectorUserId, typeName, propertyName, mapName, actualValue);
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
    public ValidMetadataValueProperties getValidMetadataValue(String typeName,
                                                              String propertyName,
                                                              String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return openMetadataClient.getValidMetadataValue(connectorUserId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
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
    public ValidMetadataValueProperties getValidMetadataMapName(String typeName,
                                                                String propertyName,
                                                                String preferredValue) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return openMetadataClient.getValidMetadataMapName(connectorUserId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
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
    public ValidMetadataValueProperties getValidMetadataMapValue(String typeName,
                                                                 String propertyName,
                                                                 String mapName,
                                                                 String preferredValue) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return openMetadataClient.getValidMetadataMapValue(connectorUserId, typeName, propertyName, mapName, preferredValue);
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
    public List<ValidMetadataValueDetail> getValidMetadataValues(String typeName,
                                                                 String propertyName,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return openMetadataClient.getValidMetadataValues(connectorUserId, typeName, propertyName, startFrom, pageSize);
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
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
    public List<ValidMetadataValueProperties> getConsistentMetadataValues(String typeName,
                                                                          String propertyName,
                                                                          String mapName,
                                                                          String preferredValue,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return openMetadataClient.getConsistentMetadataValues(connectorUserId, typeName, propertyName, mapName, preferredValue, startFrom, pageSize);
    }
}
