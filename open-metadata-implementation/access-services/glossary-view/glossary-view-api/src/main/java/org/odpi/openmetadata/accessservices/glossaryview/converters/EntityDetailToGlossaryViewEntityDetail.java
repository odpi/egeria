/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.converters;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converts an OMRS {@code EntityDetail} into this OMAS's {@code GlossaryViewEntityDetail}
 */
public class EntityDetailToGlossaryViewEntityDetail implements Function<EntityDetail, GlossaryViewEntityDetail> {

    @Override
    public GlossaryViewEntityDetail apply(EntityDetail entityDetail) {
        Optional<InstanceProperties> optionalProperties = Optional.ofNullable(entityDetail.getProperties());

        GlossaryViewEntityDetail glossaryViewEntityDetail = GlossaryViewEntityDetailFactory.build(entityDetail.getType().getTypeDefName())
                .setTypeDefName(entityDetail.getType().getTypeDefName())
                .setCreatedBy(entityDetail.getCreatedBy())
                .setUpdatedBy(entityDetail.getUpdatedBy())
                .setCreateTime(entityDetail.getCreateTime())
                .setUpdateTime(entityDetail.getUpdateTime())
                .setVersion(entityDetail.getVersion())
                .setGuid(entityDetail.getGUID())
                .setStatus(entityDetail.getStatus() == null ? null : entityDetail.getStatus().getName());

        /*Encountered a case where an entity did not have properties. However, this should not be possible in non-dev envs*/
        if(optionalProperties.isPresent()){
            glossaryViewEntityDetail.setEffectiveFromTime(optionalProperties.get().getEffectiveFromTime());
            glossaryViewEntityDetail.setEffectiveToTime(optionalProperties.get().getEffectiveToTime());

            optionalProperties.get().getInstanceProperties()
                    .forEach((key, value) -> glossaryViewEntityDetail.putProperty(key, value.valueAsString()));
        }

        if(entityDetail.getClassifications() != null) {
            glossaryViewEntityDetail.addClassifications(entityDetail.getClassifications().stream()
                    .map(new ClassificationToGlossaryViewClassification()).collect(Collectors.toList()));
        }
        return glossaryViewEntityDetail;
    }
}
