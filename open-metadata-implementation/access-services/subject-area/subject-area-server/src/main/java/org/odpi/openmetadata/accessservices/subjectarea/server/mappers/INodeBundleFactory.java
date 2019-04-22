/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 *
 */
public class INodeBundleFactory
{
    private final OMRSAPIHelper omrsapiHelper;

    public INodeBundleFactory(OMRSAPIHelper omrsapiHelper) {
        this.omrsapiHelper = omrsapiHelper;
   }
   public INodeBundle getInstance(String bundleName) {
       INodeBundle bundle =null;
       if (bundleName.equals(Term.class.getName())){
           bundle = new NodeBundle(
                   new TermMapper(omrsapiHelper),
                   Term.class.getName());

       } else if (bundleName.equals(Category.class.getName())){
           bundle = new NodeBundle(
                   new CategoryMapper(omrsapiHelper),
                   Category.class.getName());

       } else  if (bundleName.equals(Glossary.class.getName())){
           bundle = new NodeBundle(
                   new GlossaryMapper(omrsapiHelper),
                   Glossary.class.getName());

       }
       return bundle;
   }
}
