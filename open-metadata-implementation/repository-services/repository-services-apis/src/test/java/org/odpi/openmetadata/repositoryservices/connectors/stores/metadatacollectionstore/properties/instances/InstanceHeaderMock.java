/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceHeaderMock provides a concrete class to test InstanceHeader
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceHeaderMock extends InstanceHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public InstanceHeaderMock()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public InstanceHeaderMock(InstanceHeader template)
    {
        super(template);
    }
}
