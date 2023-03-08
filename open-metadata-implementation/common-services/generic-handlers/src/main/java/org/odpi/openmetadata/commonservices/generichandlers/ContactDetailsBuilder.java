/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;


/**
 * ContactDetailsBuilder is able to build the properties for a ContactDetails entity.
 */
public class ContactDetailsBuilder extends OpenMetadataAPIGenericBuilder
{
    private final String name;
    private final String contactType;
    private final int    contactMethodType;
    private final String contactMethodService;
    private final String contactMethodValue;


    /**
     * Constructor.
     *
     * @param name name of this contact method - eg my office phone number
     * @param contactType type of contact eg "Office phone number" - typically controlled from a valid value set
     * @param contactMethodType type of contact method
     * @param contactMethodService service to call
     * @param contactMethodValue identity of recipient for this service
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ContactDetailsBuilder(String               name,
                                 String               contactType,
                                 int                  contactMethodType,
                                 String               contactMethodService,
                                 String               contactMethodValue,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_GUID,
              OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.name = name;
        this.contactType = contactType;
        this.contactMethodType = contactMethodType;
        this.contactMethodService = contactMethodService;
        this.contactMethodValue = contactMethodValue;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_ENUM_TYPE_NAME,
                                                                    contactMethodType,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.STARS_PROPERTY_NAME);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONTACT_TYPE_PROPERTY_NAME,
                                                                  contactType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONTACT_METHOD_SERVICE_PROPERTY_NAME,
                                                                  contactMethodService,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.CONTACT_METHOD_VALUE_PROPERTY_NAME,
                                                                      contactMethodValue,
                                                                      methodName);

        return properties;
    }
}
