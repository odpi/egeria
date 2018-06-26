/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.server.properties.term;

import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static mapping methods to map between GlossaryTerm and the omrs equivalents.
 */
public class TermMapper {
    private static final Logger log = LoggerFactory.getLogger( TermMapper.class);
    private static final String className = TermMapper.class.getName();

    /**
     * map Node to GlossaryTerm local attributes
     * @param term
     * @return
     * @throws InvalidParameterException
     */
    static public GlossaryTerm mapTermToGlossaryTerm(Term term) throws InvalidParameterException {

        GlossaryTerm glossaryTerm = new GlossaryTerm();
        //Set properties
        if (term.getSystemAttributes() !=null) {
            glossaryTerm.setSystemAttributes(term.getSystemAttributes());
        }
        glossaryTerm.setQualifiedName(term.getQualifiedName());
        glossaryTerm.setDescription(term.getDescription());
        glossaryTerm.setDisplayName(term.getName());
        glossaryTerm.setSummary(term.getSummary());
        glossaryTerm.setGlossaryName(term.getGlossaryName());

        return glossaryTerm;
    }

    public static Term mapGlossaryTermtoTerm(GlossaryTerm glossaryTerm) {
        Term term = new Term();
        term.setClassifications(glossaryTerm.getClassifications());
        term.setDescription(glossaryTerm.getDescription());

        if (glossaryTerm.getSystemAttributes() !=null) {
            term.setSystemAttributes(glossaryTerm.getSystemAttributes());
        }
        term.setName(glossaryTerm.getDisplayName());
        term.setSummary(glossaryTerm.getSummary());
        term.setQualifiedName(glossaryTerm.getQualifiedName());

        // Do not set other parts of Term here - as there require other rest calls to get the content.

        return term;
    }
}
