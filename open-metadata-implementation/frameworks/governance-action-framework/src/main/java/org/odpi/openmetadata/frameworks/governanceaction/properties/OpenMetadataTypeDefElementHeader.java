/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTypeDefElementHeader provides a common base for all typedef information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefElementHeader
{
    public static final long  CURRENT_TYPE_DEF_HEADER_VERSION = 1;

    /**
     * Default constructor sets OpenMetadataTypeDef to nulls.
     */
    public OpenMetadataTypeDefElementHeader()
    {
        super();

        /*
         * Nothing to do
         */
    }


    /**
     * Copy/clone constructor set OpenMetadataTypeDef to value in template.
     *
     * @param template OpenMetadataTypeDefElementHeader
     */
    public OpenMetadataTypeDefElementHeader(OpenMetadataTypeDefElementHeader template)
    {
        /*
         * Nothing to do
         */
    }
}
