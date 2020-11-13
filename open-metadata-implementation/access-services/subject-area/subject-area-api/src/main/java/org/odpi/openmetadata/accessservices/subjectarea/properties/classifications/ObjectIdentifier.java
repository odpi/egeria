/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Identifies a glossary term that describes an attribute that can be used to identify an instance.
 */

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectIdentifier extends Classification {
    private static final Logger log = LoggerFactory.getLogger(ObjectIdentifier.class);
    private static final String className = ObjectIdentifier.class.getName();
    private Map<String, String> extraAttributes;

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    // note the below definitions needs to be fully qualified
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));

    /**
     * Default constructor
     */
    public ObjectIdentifier() {
        super.classificationName = "ObjectIdentifier";
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     *
     * @return extra attributes
     */
    public Map<String, String> getAdditionalProperties() {
        return extraAttributes;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.extraAttributes = additionalProperties;
    }
}
