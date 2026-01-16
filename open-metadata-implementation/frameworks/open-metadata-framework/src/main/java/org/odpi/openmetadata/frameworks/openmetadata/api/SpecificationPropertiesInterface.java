/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.List;
import java.util.Map;

/**
 * SpecificationPropertiesInterface shares the retrieval of specification properties for an element.
 */
public interface SpecificationPropertiesInterface
{
    /**
     * Retrieve the specification for this element and a list of nested maps.  This method is used by the REST APIs.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @return map of reference data
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization issue
     */
    Map<String, List<SpecificationProperty>> getSpecification(String userId,
                                                              String elementGUID) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException;
}
