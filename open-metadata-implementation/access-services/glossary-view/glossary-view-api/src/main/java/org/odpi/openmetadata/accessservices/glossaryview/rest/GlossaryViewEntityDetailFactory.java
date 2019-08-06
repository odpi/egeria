/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GlossaryViewEntityDetailFactory {

    private final static String DEFAULT = "Default";
    private final static String GLOSSARY = "Glossary";
    private final static String CATEGORY = "GlossaryCategory";
    private final static String TERM = "GlossaryTerm";
    private final static String CONTROLLED_TERM = "ControlledGlossaryTerm";
    private final static String EXTERNAL_GLOSSARY_LINK = "ExternalGlossaryLink";

    private final static Map<String, Supplier<GlossaryViewEntityDetail>> workers = new HashMap<>();
    static{
        workers.put(DEFAULT, GlossaryViewEntityDetail::new);
        workers.put(GLOSSARY, Glossary::new);
        workers.put(CATEGORY, Category::new);
        workers.put(TERM, Term::new);
        workers.put(CONTROLLED_TERM, ControlledTerm::new);
        workers.put(EXTERNAL_GLOSSARY_LINK, GlossaryViewEntityDetail::new);
    }

    public static GlossaryViewEntityDetail build(String entityType){
        if(!workers.containsKey(entityType)){
            return workers.get(DEFAULT).get();
        }
        return workers.get(entityType).get();
    }

}
