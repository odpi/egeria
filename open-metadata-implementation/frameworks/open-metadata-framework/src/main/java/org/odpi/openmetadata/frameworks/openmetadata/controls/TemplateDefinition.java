/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.List;

/**
 * Minimal definition of a template.
 */
public interface TemplateDefinition
{

    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    String getTemplateGUID();


    /**
     * Return the name to go in the template classification.
     *
     * @return string
     */
    String getTemplateName();


    /**
     * Return the description to go in the template classification.
     *
     * @return string
     */
    String getTemplateDescription();


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    String getTemplateVersionIdentifier();


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    DeployedImplementationTypeDefinition getDeployedImplementationType();


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    List<PlaceholderPropertyType> getPlaceholders();


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    List<ReplacementAttributeType> getReplacementAttributes();
}
