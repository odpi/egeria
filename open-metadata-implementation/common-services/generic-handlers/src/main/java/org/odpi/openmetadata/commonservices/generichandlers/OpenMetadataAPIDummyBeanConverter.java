/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * OpenMetadataAPIGenericConverter provides the generic methods for the bean converters used to provide translation between
 * specific Open Metadata API beans and the repository services API beans.
 *
 * Generic classes have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Open Metadata API bean.
 */
public class OpenMetadataAPIDummyBeanConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor captures the initial content
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public OpenMetadataAPIDummyBeanConverter(OMRSRepositoryHelper   repositoryHelper,
                                             String                 serviceName,
                                             String                 serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }




    /**
     * Extract the properties from the entity.  Each API creates a specialization of this method for its beans.
     *
     * @param genericBean output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    public void updateNewSimpleBean(B            genericBean,
                                    EntityDetail entity,
                                    Relationship relationship)
    {

    }

}
