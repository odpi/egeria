/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataplatform;

/**
 * DataPlatformInterface provides the interface for managing information about data assets hosted on a data platform.
 * The DataInfrastructureInterface provides methods for describing the infrastructure of the data platform.
 */
public interface DataPlatformInterface extends DataConnectionInterface, RelationalDatabaseInterface
{
}
