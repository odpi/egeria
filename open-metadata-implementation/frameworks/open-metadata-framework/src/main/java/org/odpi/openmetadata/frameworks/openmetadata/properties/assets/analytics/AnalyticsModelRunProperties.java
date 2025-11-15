/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.analytics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.TransientEmbeddedProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AnalyticsModelRunProperties describes an analytics model run.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsModelRunProperties extends TransientEmbeddedProcessProperties
{
    /**
     * Default constructor
     */
    public AnalyticsModelRunProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ANALYTICS_MODEL_RUN.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AnalyticsModelRunProperties(TransientEmbeddedProcessProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AnalyticsModelRunProperties{} " + super.toString();
    }
}
