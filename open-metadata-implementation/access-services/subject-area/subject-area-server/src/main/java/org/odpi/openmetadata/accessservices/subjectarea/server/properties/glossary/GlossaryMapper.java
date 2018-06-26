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
package org.odpi.openmetadata.accessservices.subjectarea.server.properties.glossary;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static mapping methods to map between GlossaryTerm and the omrs equivalents.
 */
public class GlossaryMapper {
    private static final Logger log = LoggerFactory.getLogger( GlossaryMapper.class);
    private static final String className = GlossaryMapper.class.getName();

    /**
     * map Glossary to Generated Glossary local attributes
     * @param glossary
     * @return
     * @throws InvalidParameterException
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary mapGlossaryToGeneratedGlossary(Glossary glossary) throws InvalidParameterException {


        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = new org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary();

        //Set properties
        if (glossary.getSystemAttributes() !=null) {
            generatedGlossary.setSystemAttributes(glossary.getSystemAttributes());
        }
        generatedGlossary.setDescription(glossary.getDescription());
        generatedGlossary.setDisplayName(glossary.getName());
        generatedGlossary.setUsage(glossary.getUsage());
        generatedGlossary.setAdditionalProperties((glossary.getAdditionalProperties()));
        return generatedGlossary;
    }

    public static Glossary mapGeneratedGlossarytoGlossary(org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary) {
        Glossary glossary = new Glossary();
        glossary.setClassifications(generatedGlossary.getClassifications());
        glossary.setDescription(generatedGlossary.getDescription());
        if (generatedGlossary.getSystemAttributes() !=null) {
            glossary.setSystemAttributes(generatedGlossary.getSystemAttributes());
        }
        glossary.setName(generatedGlossary.getDisplayName());
        glossary.setQualifiedName(generatedGlossary.getQualifiedName());
        glossary.setUsage(generatedGlossary.getUsage());
        glossary.setAdditionalProperties(generatedGlossary.getAdditionalProperties());
        return glossary;
    }
}
