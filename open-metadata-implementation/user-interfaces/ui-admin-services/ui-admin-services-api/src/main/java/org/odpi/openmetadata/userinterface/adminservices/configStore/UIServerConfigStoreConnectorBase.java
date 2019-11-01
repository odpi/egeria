/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configStore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

/**
 * UserStoreConnectorBase provides the base class for a UI Server's configuration configStore.  It defines the
 * specific interface for this type of connector.
 */
public abstract class UIServerConfigStoreConnectorBase extends ConnectorBase implements UIServerConfigStore
{
}
