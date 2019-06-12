/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

public class GlossaryViewEntityDetailFactory {

    private final static String GLOSSARY = "Glossary";
    private final static String CATEGORY = "GlossaryCategory";
    private final static String TERM = "GlossaryTerm";
    private final static String EXTERNAL_GLOSSARY_LINK = "ExternalGlossaryLink";

    public static GlossaryViewEntityDetail build(String entityType){
        GlossaryViewEntityDetail prototype = null;
        if(entityType.equalsIgnoreCase(GLOSSARY)){
            prototype = buildDefaultGlossary();
        }
        if(entityType.equalsIgnoreCase(CATEGORY)){
            prototype = buildDefaultCategory();
        }
        if(entityType.equalsIgnoreCase(TERM)){
            prototype = buildDefaultTerm();
        }
        if(entityType.equalsIgnoreCase(EXTERNAL_GLOSSARY_LINK)){
            prototype = buildDefaultGlossaryViewEntityDetail();
        }
        return prototype;
    }

    private static GlossaryViewEntityDetail buildDefaultGlossaryViewEntityDetail(){
        return new GlossaryViewEntityDetail();
    }

    private static Glossary buildDefaultGlossary(){
        return new Glossary();
    }

    private static Category buildDefaultCategory(){
        return new Category();
    }

    private static Term buildDefaultTerm(){
        return new Term();
    }

}
