/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.converters;

import org.odpi.openmetadata.accessservices.glossaryview.rest.ControlledGlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class GlossaryViewEntityDetailFactory {

    private static final String DEFAULT = "Default";
    private static final String GLOSSARY = "Glossary";
    private static final String CATEGORY = "GlossaryCategory";
    private static final String TERM = "GlossaryTerm";
    private static final String CONTROLLED_TERM = "ControlledGlossaryTerm";
    private static final String EXTERNAL_GLOSSARY_LINK = "ExternalGlossaryLink";

    private static final Map<String, Supplier<GlossaryViewEntityDetail>> workers = new HashMap<>();
    static {
        workers.put(DEFAULT, GlossaryViewEntityDetail::new);
        workers.put(GLOSSARY, Glossary::new);
        workers.put(CATEGORY, GlossaryCategory::new);
        workers.put(TERM, GlossaryTerm::new);
        workers.put(CONTROLLED_TERM, ControlledGlossaryTerm::new);
        workers.put(EXTERNAL_GLOSSARY_LINK, ExternalGlossaryLink::new);
    }

    private GlossaryViewEntityDetailFactory() {}

    public static GlossaryViewEntityDetail build(String entityType){
        if(!workers.containsKey(entityType)){
            return workers.get(DEFAULT).get();
        }
        return workers.get(entityType).get();
    }

}
