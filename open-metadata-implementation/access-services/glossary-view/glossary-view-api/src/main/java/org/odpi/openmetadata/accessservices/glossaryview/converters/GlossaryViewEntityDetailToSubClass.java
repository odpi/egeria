/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.converters;

import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;

/**
 * Converts an OMRS {@code EntityDetail} into this OMAS's {@code GlossaryViewEntityDetail}
 */
public class GlossaryViewEntityDetailToSubClass<R extends GlossaryViewEntityDetail> {

    public R apply(GlossaryViewEntityDetail glossaryViewEntityDetail) {
        GlossaryViewEntityDetail subclass = GlossaryViewEntityDetailFactory.build(glossaryViewEntityDetail.getTypeDefName())
                .setTypeDefName(glossaryViewEntityDetail.getTypeDefName())
                .setCreatedBy(glossaryViewEntityDetail.getCreatedBy())
                .setUpdatedBy(glossaryViewEntityDetail.getUpdatedBy())
                .setCreateTime(glossaryViewEntityDetail.getCreateTime())
                .setUpdateTime(glossaryViewEntityDetail.getUpdateTime())
                .setVersion(glossaryViewEntityDetail.getVersion())
                .setGuid(glossaryViewEntityDetail.getGuid())
                .setStatus(glossaryViewEntityDetail.getStatus())
                .setEffectiveFromTime(glossaryViewEntityDetail.getEffectiveFromTime())
                .setEffectiveToTime(glossaryViewEntityDetail.getEffectiveToTime());

        subclass.setProperties(glossaryViewEntityDetail.allProperties());
        subclass.setClassifications(glossaryViewEntityDetail.getClassifications());

        return (R) subclass;
    }

    public static class ToGlossary extends GlossaryViewEntityDetailToSubClass<Glossary> implements ToSubClass<Glossary> { }

    public static class ToCategory extends GlossaryViewEntityDetailToSubClass<GlossaryCategory> implements ToSubClass<GlossaryCategory> { }

    public static class ToTerm extends GlossaryViewEntityDetailToSubClass<GlossaryTerm> implements ToSubClass<GlossaryTerm>{ }

//    Commented until needed
//    public static class ToControlledTerm extends GlossaryViewEntityDetailToSubClass<ControlledGlossaryTerm> implements ToSubClass<ControlledGlossaryTerm>{ }

    public static class ToExternalGlossaryLink extends GlossaryViewEntityDetailToSubClass<ExternalGlossaryLink> implements ToSubClass<ExternalGlossaryLink>{ }

}
