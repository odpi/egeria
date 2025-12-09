/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.util.List;

/**
 * InvalidParameterHandler is a common error handler.  It provides validation for incoming parameters.
 */
public class InvalidParameterHandler
{
    private  int   maxPagingSize = 500;

    /**
     * Default constructor
     */
    public InvalidParameterHandler()
    {
    }


    /**
     * Override the default maximum paging size.  Zero means no paging limit.
     *
     * @param maxPagingSize new value
     */
    public void setMaxPagingSize(int maxPagingSize)
    {
        this.maxPagingSize = maxPagingSize;
    }


    /**
     * Get the maximum paging size.
     *
     * @return maxPagingSize new value
     */
    public int getMaxPagingSize()
    {
        return maxPagingSize;
    }


    /**
     * Throw an exception if a server URL or  has not been supplied.  It is typically
     * used in OMAG Clients or OMAG Servers that call other OMAG Servers.
     *
     * @param omagServerPlatformURL url of the server
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the server URL or server name are not set
     */
    public void validateOMAGServerPlatformURL(String omagServerPlatformURL,
                                              String methodName) throws InvalidParameterException
    {
        if (omagServerPlatformURL == null)
        {
            final String parameterName = "omagServerPlatformURL";

            throw new InvalidParameterException(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if a server URL or  has not been supplied.  It is typically
     * used in OMAG Clients or OMAG Servers that call other OMAG Servers.
     *
     * @param omagServerPlatformURL url of the server
     * @param serverName requested server
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the server URL or server name are not set
     */
    public void validateOMAGServerPlatformURL(String omagServerPlatformURL,
                                              String serverName,
                                              String methodName) throws InvalidParameterException
    {
        this.validateOMAGServerPlatformURL(omagServerPlatformURL, methodName);

        if (serverName == null)
        {
            final String parameterName = "serverName";

            throw new InvalidParameterException(OMAGCommonErrorCode.SERVER_NAME_NOT_SPECIFIED.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    public void validateUserId(String userId,
                               String methodName) throws InvalidParameterException
    {
        if ((userId == null) || (userId.isEmpty()))
        {
            final String parameterName = "userId";

            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_USER_ID.getMessageDefinition(methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied GUID is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validateGUID(String guid,
                             String guidParameter,
                             String methodName) throws InvalidParameterException
    {
        if ((guid == null) || (guid.isEmpty()))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_GUID.getMessageDefinition(guidParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name           unique name to validate
     * @param nameParameter  name of the parameter that passed the name.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the name is null
     */
    public void validateName(String name,
                             String nameParameter,
                             String methodName) throws InvalidParameterException
    {
        if ((name == null) || (name.isEmpty()))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_NAME.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }


    /**
     * Throw an exception if the supplied search string is null
     *
     * @param searchString   searchString to validate
     * @param searchParameter  name of the parameter that passed the searchString.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the searchString is null
     */
    public void validateSearchString(String searchString,
                                     String searchParameter,
                                     String methodName) throws InvalidParameterException
    {
        if ((searchString == null) || (searchString.isEmpty()))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_SEARCH_STRING.getMessageDefinition(searchParameter,
                                                                                                            methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                searchParameter);
        }
        else
        {
            /*
             * This test just validated that the regular expression in the search parameter is valid.
             */
            final String testString = "abcdefghijklmnopqrstuvwxyz";

            try
            {
                testString.matches(searchString);
            }
            catch (Exception error)
            {
                throw new InvalidParameterException(OMAGCommonErrorCode.INVALID_SEARCH_STRING.getMessageDefinition(searchParameter,
                                                                                                                   methodName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    searchParameter);
            }
        }
    }


    /**
     * Throw an exception if the supplied object is null
     *
     * @param object         object to validate
     * @param nameParameter  name of the parameter that passed the object.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the object is null
     */
    public void validateObject(Object object,
                               String nameParameter,
                               String methodName) throws InvalidParameterException
    {
        if (object == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_OBJECT.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }


    /**
     * Throw an exception if the supplied object is null
     *
     * @param object         object to validate
     * @param nameParameter  name of the parameter that passed the object.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the object is null
     */
    public void throwInvalidParameter(Object object,
                                      String nameParameter,
                                      String methodName) throws InvalidParameterException
    {
        if (object != null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.INVALID_PARAMETER.getMessageDefinition(object.toString(), nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
        else
        {
            validateObject(null, nameParameter, methodName);
        }
    }


    /**
     * Throw an exception if the supplied enum is null
     *
     * @param enumValue  enum value to validate
     * @param parameterName  name of the parameter that passed the enum.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the enum is null
     */
    public void validateEnum(Object enumValue,
                             String parameterName,
                             String methodName) throws InvalidParameterException
    {
        if (enumValue == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_ENUM.getMessageDefinition(parameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied text field is null
     *
     * @param text  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    public void validateText(String text,
                             String parameterName,
                             String methodName) throws InvalidParameterException
    {
        if (text == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_TEXT.getMessageDefinition(parameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied paging values don't make sense. If page size is zero it means return as much as there is.
     * in which case this method sets the page size to the maximum value for this server.  If the server has its max page size set to zero
     * then zero is used.
     *
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName  name of the method making the call.
     * @return validated page size.
     * @throws InvalidParameterException the paging options are incorrect
     */
    public int  validatePaging(int    startFrom,
                               int    pageSize,
                               String methodName) throws InvalidParameterException
    {
        final  String   startFromParameterName = "startFrom";
        final  String   pageSizeParameterName  = "pageSize";

        if (startFrom < 0)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NEGATIVE_START_FROM.getMessageDefinition(Integer.toString(startFrom),
                                                                                                             startFromParameterName,
                                                                                                             methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                startFromParameterName);
        }


        if (pageSize < 0)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NEGATIVE_PAGE_SIZE.getMessageDefinition(Integer.toString(pageSize),
                                                                                                            pageSizeParameterName,
                                                                                                            methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                pageSizeParameterName);
        }

        if ((maxPagingSize != 0) && (pageSize > maxPagingSize))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.MAX_PAGE_SIZE.getMessageDefinition(Integer.toString(pageSize),
                                                                                                       pageSizeParameterName,
                                                                                                       methodName,
                                                                                                       Integer.toString(maxPagingSize)),
                                                this.getClass().getName(),
                                                methodName,
                                                pageSizeParameterName);
        }

        if (pageSize == 0)
        {
            return maxPagingSize;
        }
        else
        {
            return pageSize;
        }
    }


    /**
     * Throw an exception if the supplied array is null or empty
     *
     * @param stringArray  object to validate
     * @param parameterName  name of the parameter that passed the array.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the array is null or empty
     */
    public void validateStringArray(List<String>  stringArray,
                                    String        parameterName,
                                    String        methodName) throws InvalidParameterException
    {
        if ((stringArray == null) || (stringArray.isEmpty()))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_ARRAY_PARAMETER.getMessageDefinition(parameterName,
                                                                                                              methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * When attached elements are being retrieved for an anchor, this method checks that the anchor is set up correctly
     * in the element's anchors classification.
     *
     * @param anchorGUID unique identifier of the expected anchor
     * @param anchorGUIDParameterName parameter name used to supply anchorGUID
     * @param anchorEntity current setting for the anchor
     * @param elementGUID unique identifier of the element being retrieved
     * @param elementTypeName type of element being retrieved
     * @param methodName calling method
     * @throws InvalidParameterException exception raise when the anchors do not match
     */
    public void validateAnchorGUID(String       anchorGUID,
                                   String       anchorGUIDParameterName,
                                   EntityDetail anchorEntity,
                                   String       elementGUID,
                                   String       elementTypeName,
                                   String       methodName) throws InvalidParameterException
    {
        if ((anchorGUID != null) && (anchorEntity != null))
        {
            if (! anchorGUID.equals(anchorEntity.getGUID()))
            {
                if (! anchorGUID.equals(elementGUID))
                {
                    throw new InvalidParameterException(OMAGCommonErrorCode.WRONG_ANCHOR_GUID.getMessageDefinition(elementTypeName,
                                                                                                                   elementGUID,
                                                                                                                   anchorGUID,
                                                                                                                   anchorEntity.getGUID(),
                                                                                                                   methodName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        anchorGUIDParameterName);
                }
            }
        }
    }


    /**
     * Throw an exception if the supplied connection is null
     *
     * @param connection  object to validate
     * @param parameterName  name of the parameter that passed the connection.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the connection is null
     */
    public void validateConnection(Connection connection,
                                   String     parameterName,
                                   String     methodName) throws InvalidParameterException
    {
        if (connection == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_CONNECTION_PARAMETER.getMessageDefinition(parameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (connection.getConnectorType() == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.NULL_CONNECTOR_TYPE_PARAMETER.getMessageDefinition(parameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied type name is not of the correct subclass
     *
     * @param typeName name of type
     * @param superTypeName name of expected supertype (or null if no super type)
     * @param serviceName calling service
     * @param methodName calling method
     * @param repositoryHelper helper class that can return information about type
     *
     * @return unique identifier (guid) of typeName
     * @throws InvalidParameterException the type name is not correct
     */
    public TypeDef validateTypeDefName(String                typeName,
                                       String                superTypeName,
                                       String                serviceName,
                                       String                methodName,
                                       OMRSRepositoryHelper repositoryHelper) throws InvalidParameterException

    {
        final String parameterName = "typeName";

        TypeDef typeDef = null;

        if (typeName != null)
        {
            typeDef = repositoryHelper.getTypeDefByName(serviceName, typeName);
        }
        else if (superTypeName != null)
        {
            typeDef = repositoryHelper.getTypeDefByName(serviceName, superTypeName);
        }


        if (typeDef == null)
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.UNRECOGNIZED_TYPE_NAME.getMessageDefinition(typeName,
                                                                                                                methodName,
                                                                                                                serviceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (! repositoryHelper.isTypeOf(serviceName, typeDef.getName(), superTypeName))
        {
            throw new InvalidParameterException(OMAGCommonErrorCode.BAD_SUB_TYPE_NAME.getMessageDefinition(typeName,
                                                                                                           methodName,
                                                                                                           serviceName,
                                                                                                           superTypeName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        return typeDef;
    }


    /**
     * Throw an exception if the supplied type name is not of the correct subclass
     *
     * @param typeName name of type
     * @param superTypeName name of expected supertype (or null if no super type)
     * @param serviceName calling service
     * @param methodName calling method
     * @param repositoryHelper helper class that can return information about type
     *
     * @return unique identifier (guid) of typeName
     * @throws InvalidParameterException the type name is not correct
     */
    public String validateTypeName(String                typeName,
                                   String                superTypeName,
                                   String                serviceName,
                                   String                methodName,
                                   OMRSRepositoryHelper  repositoryHelper) throws InvalidParameterException

    {
        TypeDef validTypeDef = this.validateTypeDefName(typeName, superTypeName, serviceName, methodName, repositoryHelper);

        return validTypeDef.getGUID();
    }


    /**
     * Throw an exception if the supplied guid returned an instance of the wrong type
     *
     * @param guid  unique identifier of instance
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved instance
     * @param expectedType  type the instance should be
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    public void handleWrongTypeForGUIDException(String guid,
                                                String methodName,
                                                String actualType,
                                                String expectedType) throws InvalidParameterException
    {
        throw new InvalidParameterException(OMAGCommonErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID.getMessageDefinition(methodName,
                                                                                                                  guid,
                                                                                                                  actualType,
                                                                                                                  expectedType),
                                            this.getClass().getName(),
                                            methodName,
                                            expectedType);

    }


    /**
     * Compare the supported zones with the zones stored in the element.  If the element is not in
     * one of the supported zones then throw an exception. Otherwise, return ok.
     * Null values in either returns ok.
     * Note the error message implies that the element does not exist.  This is because the consequence
     * of not being in the visible zone is that the element is invisible - just like it does not exist.
     *
     * @param elementGUID unique identifier of the asset
     * @param parameterName name of the parameter that passed the asset guid
     * @param zoneMembership list of zone names from the element
     * @param visibleZones list of zone names visible to the caller
     * @param serviceName calling service
     * @param methodName calling method
     *
     * @throws InvalidParameterException the asset is not in the supported zone.
     */
    public void validateElementInSupportedZone(String        elementGUID,
                                               String        parameterName,
                                               List<String>  zoneMembership,
                                               List<String>  visibleZones,
                                               String        serviceName,
                                               String        methodName) throws InvalidParameterException
    {
        if ((visibleZones == null) || (visibleZones.isEmpty()))
        {
            return;
        }

        if ((zoneMembership == null) || (zoneMembership.isEmpty()))
        {
            return;
        }

        for (String    memberZone : zoneMembership)
        {
            if (memberZone != null)
            {
                for (String  supportedZoneName : visibleZones)
                {
                    if (memberZone.equals(supportedZoneName))
                    {
                        return;
                    }
                }
            }
        }

        throw new InvalidParameterException(OMAGCommonErrorCode.NOT_IN_THE_ZONE.getMessageDefinition(elementGUID, serviceName),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Throw an exception to indicate that the call to a method is not supported.
     * This is a temporary situation.
     *
     * @param userId      user name to validate
     * @param serviceName name of called service
     * @param serverName name of this server
     * @param methodName  name of the called method.
     *
     * @throws InvalidParameterException the method is not supported
     */
    public void throwMethodNotSupported(String userId,
                                        String serviceName,
                                        String serverName,
                                        String methodName) throws InvalidParameterException
    {
        final String parameterName = "methodName";

        throw new InvalidParameterException(OMAGCommonErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName,
                                                                                                            userId,
                                                                                                            serverName,
                                                                                                            serviceName),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Throw an exception to indicate that the requested element is not recognized.
     * This is probably due to the types of metadata repositories that this server is connected to.
     *
     * @param userId      user name to validate
     * @param guid  unique identifier of element
     * @param type  type of element
     * @param serviceName name of called service
     * @param serverName name of this server
     * @param methodName  name of the called method.
     *
     * @throws InvalidParameterException the element is not known
     */
    public void throwUnknownElement(String userId,
                                    String guid,
                                    String type,
                                    String serviceName,
                                    String serverName,
                                    String methodName) throws InvalidParameterException
    {
        final String parameterName = "guid";

        throw new InvalidParameterException(OMAGCommonErrorCode.UNKNOWN_ELEMENT.getMessageDefinition(guid,
                                                                                                     type,
                                                                                                     userId,
                                                                                                     methodName,
                                                                                                     serviceName,
                                                                                                     serverName),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Throw an exception to indicate that the requested element is not recognized.
     * This is probably due to the types of metadata repositories that this server is connected to.
     *
     * @param userId      user name to validate
     * @param qualifiedName  unique name of element
     * @param type  type of element
     * @param serviceName name of called service
     * @param serverName name of this server
     * @param methodName  name of the called method.
     *
     * @throws InvalidParameterException the element is not known
     */
    public void throwUnknownElementQualifiedName(String userId,
                                                 String qualifiedName,
                                                 String type,
                                                 String serviceName,
                                                 String serverName,
                                                 String methodName) throws InvalidParameterException
    {
        final String parameterName = "qualifiedName";

        throw new InvalidParameterException(OMAGCommonErrorCode.UNKNOWN_ELEMENT.getMessageDefinition(qualifiedName,
                                                                                                     type,
                                                                                                     userId,
                                                                                                     methodName,
                                                                                                     serviceName,
                                                                                                     serverName),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Report the fact that a unique name that was requested for a new entity is already in use or not a permitted value.
     *
     * @param uniqueName value of unique parameter that is in error
     * @param uniqueNameParameterName parameter that passed the qualified name
     * @param typeName requested type name
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException the unique name is in use
     */
    public void throwUniqueNameInUse(String uniqueName,
                                     String uniqueNameParameterName,
                                     String typeName,
                                     String serviceName,
                                     String methodName) throws InvalidParameterException
    {
        throw new InvalidParameterException(OMAGCommonErrorCode.UNIQUE_NAME_ALREADY_IN_USE.getMessageDefinition(methodName,
                                                                                                                serviceName,
                                                                                                                typeName,
                                                                                                                uniqueNameParameterName,
                                                                                                                uniqueName),
                                            this.getClass().getName(),
                                            methodName,
                                            uniqueNameParameterName);
    }


    /**
     * Throw an exception to indicate that the call to a method is not supported.
     * This is a temporary situation.
     *
     * @param guid  unique identifier of element
     * @param type  type of element
     * @param serviceName name of called service
     * @param methodName  name of the called method.
     *
     * @throws InvalidParameterException the guid is in use
     */
    public void throwCannotDeleteElementInUse(String guid,
                                              String type,
                                              String serviceName,
                                              String methodName) throws InvalidParameterException
    {
        final String parameterName = "guid";

        throw new InvalidParameterException(OMAGCommonErrorCode.CANNOT_DELETE_ELEMENT_IN_USE.getMessageDefinition(methodName,
                                                                                                                  serviceName,
                                                                                                                  type,
                                                                                                                  guid),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }
}
