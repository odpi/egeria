/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataplatform.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;

import java.io.Serializable;
import java.util.List;

/**
 * DataPlatform defines an endpoint and connectors for the runtime environment for a collection of data assets.
 */
public class DataPlatform implements Serializable
{
    /*
     * Lists of objects that make up the infrastructure for the asset.
     */
    private Endpoint               dataPlatformEndpoint  = null;
    private List<ConnectorType>    dataPlatformConnectorType = null;


    /**
     * Default constructor.
     */
    public DataPlatform()
    {

    }

    /**
     * Copy/clone constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the infrastructure clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateDataPlatform - template object to copy
     */
    public DataPlatform( DataPlatform templateDataPlatform)
    {
        /*
         * Only create a child object if the template is not null.
         */

        if (templateDataPlatform != null)
        {
            dataPlatformEndpoint = templateDataPlatform.getDataPlatformEndpoint();
        }
    }


    /**
     * Return the endpoint defined for this data platform.  This value must be set for this to be a
     * valid data platform definition.
     *
     * @return Endpoint definition
     */
    public Endpoint getDataPlatformEndpoint()
    {
        return dataPlatformEndpoint;
    }


    /**
     * Set up the endpoint defined for this data platform.  This value must be set for this to be a
     * valid data platform definition.
     *
     * @param dataPlatformEndpoint - Endpoint definition
     */
    public void setDataPlatformEndpoint(Endpoint dataPlatformEndpoint)
    {
        this.dataPlatformEndpoint = dataPlatformEndpoint;
    }
}
