/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.converters;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewClassification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.function.Function;

/**
 * Converts an OMRS {@code Classification} into this OMAS's {@code GlossaryViewClassification}
 */
class ClassificationToGlossaryViewClassification implements Function<Classification, GlossaryViewClassification> {

    @Override
    public GlossaryViewClassification apply(Classification classification) {
        GlossaryViewClassification glossaryViewClassification = new GlossaryViewClassification();
        glossaryViewClassification.setName(classification.getName());
        glossaryViewClassification.setClassificationType(classification.getType().getTypeDefName());
        glossaryViewClassification.setCreatedBy(classification.getCreatedBy());
        glossaryViewClassification.setUpdatedBy(classification.getUpdatedBy());
        glossaryViewClassification.setCreateTime(classification.getCreateTime());
        glossaryViewClassification.setUpdateTime(classification.getUpdateTime());
        glossaryViewClassification.setStatus(classification.getStatus().getName());

        InstanceProperties properties = classification.getProperties();
        if(properties != null && properties.getInstanceProperties() != null) {
            properties.getInstanceProperties().forEach(
                    (key, value) -> glossaryViewClassification.addProperty(key, value.valueAsString()));
        }

        return glossaryViewClassification;
    }

}
