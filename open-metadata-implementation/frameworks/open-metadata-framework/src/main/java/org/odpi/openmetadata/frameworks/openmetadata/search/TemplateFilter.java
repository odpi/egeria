/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;


/**
 * Defines the options to filter out templates
 */
public enum TemplateFilter
{
    /**
     * Ignore any element that is either classified with the Template classification, or the TemplateSubstitute
     * classification.
     */
    NO_TEMPLATES,

    /**
     * Only return elements that are classified with the Template classification, or the TemplateSubstitute
     * classification.
     */
    ONLY_TEMPLATES,

    /**
     * Return all matching elements irrespective of whether they have the Template classification, or the
     * TemplateSubstitute classification.
     */
    ALL
}
