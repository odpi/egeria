/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * The SampleDataExtension describes a method to retrieve a selection of data to show to an end user.
 * The connector can choose how to return the data and whether to return real data or synthetic data that is typical
 * for this type of resource.
 *
 * The result is a map of names to lists of values.  If the data is tabular, the names are the column names and the
 * list of values are the values for that column.  The order of the values in the lists is consistent
 * column to column so that the first item in the lists are for row 1 etc.  If the data is name-value pairs,
 * the names are the names of the properties and the values are single elements in the list.
 * If the data is a single element, or a nested structure, it is formatted as a string that goes in the
 * first name in the map with a null list of values.
 */
public interface SampleDataExtension
{
    /**
     * Return sample data for the default user configured for this connector.
     *
     * @param pageSize maximum number of elements to return in the map
     * @return a map of names to lists of values.
     * @throws InvalidParameterException the page size is a negative number
     * @throws UserNotAuthorizedException the connector is not permitted to issue the request
     * @throws PropertyServerException the connector can not connect to the data source
     */
    Map<String, List<String>> getSampleData(int pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Return sample data for the calling user.
     *
     * @param userId calling user
     * @param pageSize maximum number of elements to return in the map
     * @return a map of names to lists of values.
     * @throws InvalidParameterException the page size is a negative number
     * @throws UserNotAuthorizedException the connector is not permitted to issue the request
     * @throws PropertyServerException the connector can not connect to the data source
     */
    Map<String, List<String>> getSampleData(String userId,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;
}

