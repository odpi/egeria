/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * AssetOnboardingCSVFileInterface provides the client-side interface for an asset owner to set up the metadata about
 * a CSV file asset.  This includes defining its name, source and license.
 */
public interface AssetOnboardingCSVFileInterface
{
    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String>  addCSVFileToCatalog(String userId,
                                      String displayName,
                                      String description,
                                      String fullPath) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;

    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String>  addCSVFileToCatalog(String       userId,
                                      String       displayName,
                                      String       description,
                                      String       fullPath,
                                      List<String> columnHeaders,
                                      Character    delimiterCharacter,
                                      Character    quoteCharacter) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;
}
