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
package org.odpi.openmetadata.accessservices.subjectarea.server.properties.category;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.category.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static mapping methods to map between GlossaryCategory and the omrs equivalents.
 */
public class CategoryMapper {
    private static final Logger log = LoggerFactory.getLogger( CategoryMapper.class);
    private static final String className = CategoryMapper.class.getName();

    /**
     * map Node to GlossaryCategory local attributes
     * @param category
     * @return
     * @throws InvalidParameterException
     */
    static public GlossaryCategory mapCategoryToGlossaryCategory(Category category) throws InvalidParameterException {

        GlossaryCategory glossaryCategory = new GlossaryCategory();
        //Set properties
        if (category.getSystemAttributes() !=null) {
            glossaryCategory.setSystemAttributes(category.getSystemAttributes());
        }
        glossaryCategory.setQualifiedName(category.getQualifiedName());
        glossaryCategory.setDescription(category.getDescription());
        glossaryCategory.setDisplayName(category.getName());
        glossaryCategory.setGlossaryName(category.getGlossaryName());

        return glossaryCategory;
    }

    public static Category mapGlossaryCategorytoCategory(GlossaryCategory glossaryCategory) {
        Category category = new Category();
        category.setClassifications(glossaryCategory.getClassifications());
        category.setDescription(glossaryCategory.getDescription());

        if (glossaryCategory.getSystemAttributes() !=null) {
            category.setSystemAttributes(glossaryCategory.getSystemAttributes());
        }
        category.setName(glossaryCategory.getDisplayName());
        category.setQualifiedName(glossaryCategory.getQualifiedName());

        // Do not set other parts of Category here - as there require other rest calls to get the content.

        return category;
    }
}
