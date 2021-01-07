/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

import java.text.MessageFormat;

/**
 * OMAGServerConfigStoreConnectorBase provides the base class for an OMAG Server's configuration document store.
 * It defines the specific interface for this type of connector.  A new connector instance is created for each
 * OMAG Server configuration document that is to be retrieved or stored.
 */
public abstract class OMAGServerConfigStoreConnectorBase extends ConnectorBase implements OMAGServerConfigStore
{
    protected String serverName = null;


    /**
     * Set up the name of the server for this configuration document.
     *
     * @param serverName name of the server
     */
    @Override
    public void setServerName(String  serverName)
    {
        this.serverName = serverName;
    }


    /**
     * This method provides the ability to generate the name of the store based on a template and insert
     * values.  Typically this is to allow the server name to be embedded into the store name, but other
     * values, such as the platform name may also be needed for some store implementations.
     *
     * @param template this is the template with placeholders
     * @param params this is the list of parameters that can be embedded in the template if placeholders are present
     *               (no error results of the placeholders are not present.
     * @return formatted string
     */
    protected String getStoreName(String template,
                                  String ...params)
    {
        if (template != null)
        {
            MessageFormat mf = new MessageFormat(template);
            return mf.format(params);
        }

        return null;
    }
}
