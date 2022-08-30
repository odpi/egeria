/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.converters;

import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ContactMethodType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.PortType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * DataManagerOMASConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public class ITInfrastructureOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public ITInfrastructureOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                         String                 serviceName,
                                         String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */



    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    ContactMethodType getContactMethodTypeFromProperties(InstanceProperties properties)
    {
        final String methodName = "getContactMethodTypeFromProperties";

        ContactMethodType contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataAPIMapper.CONTACT_METHOD_TYPE_PROPERTY_NAME, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    contactMethodType = ContactMethodType.EMAIL;
                    break;

                case 1:
                    contactMethodType = ContactMethodType.PHONE;
                    break;

                case 2:
                    contactMethodType = ContactMethodType.CHAT;
                    break;

                case 3:
                    contactMethodType = ContactMethodType.PROFILE;
                    break;

                case 4:
                    contactMethodType = ContactMethodType.ACCOUNT;
                    break;

                case 99:
                    contactMethodType = ContactMethodType.OTHER;
                    break;
            }
        }

        return contactMethodType;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    ServerAssetUseType getServerAssetUseTypeFromProperties(InstanceProperties   properties)
    {
        final String methodName = "getServerAssetUseTypeFromProperties";

        ServerAssetUseType serverAssetUseType = ServerAssetUseType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_TYPE_NAME, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    serverAssetUseType = ServerAssetUseType.OWNS;
                    break;

                case 1:
                    serverAssetUseType = ServerAssetUseType.GOVERNS;
                    break;

                case 2:
                    serverAssetUseType = ServerAssetUseType.MAINTAINS;
                    break;

                case 3:
                    serverAssetUseType = ServerAssetUseType.USES;
                    break;

                case 99:
                    serverAssetUseType = ServerAssetUseType.OTHER;
                    break;
            }
        }

        return serverAssetUseType;
    }



    /**
     * Extract and delete the portType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PortType enum
     */
    PortType removePortType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePortType";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.PORT_TYPE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (PortType portType : PortType.values())
            {
                if (portType.getOpenTypeOrdinal() == ordinal)
                {
                    return portType;
                }
            }
        }

        return PortType.OTHER;
    }
}
