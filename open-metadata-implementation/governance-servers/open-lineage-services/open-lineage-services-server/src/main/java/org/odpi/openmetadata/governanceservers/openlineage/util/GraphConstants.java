/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.openlineage.util;

public class GraphConstants {


    // Short names for core properties

    public static final String PROPERTY_NAME_GUID                             = "guid";
    public static final String PROPERTY_NAME_QUALIFIED_NAME                   = "qualifiedNameÁ";
    public static final String PROPERTY_NAME_NAME                             = "name";

    /*
     *  Elements
     */

    public static final String PROPERTY_KEY_PREFIX_Element                  = "ve";

    public static final String PROPERTY_KEY_ENTITY_GUID                    = PROPERTY_KEY_PREFIX_Element + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME            = PROPERTY_KEY_PREFIX_Element + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_ENTITY_NAME                    = PROPERTY_KEY_PREFIX_Element + PROPERTY_NAME_NAME;

}
