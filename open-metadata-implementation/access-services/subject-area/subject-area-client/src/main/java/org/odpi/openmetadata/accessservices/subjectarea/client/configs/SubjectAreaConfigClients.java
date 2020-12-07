/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.configs;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;

public interface SubjectAreaConfigClients {

    /**
     * Get the subject area config API class - use this class to issue glossary calls.
     *
     * @return subject area glossary API class
     */
    SubjectAreaConfigClient configs();
}