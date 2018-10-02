/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode.GovernedNode;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

public class Category extends GovernedNode{
    private GlossarySummary glossary =null;
    private CategorySummary parentCategory;

    public Category() {
        nodeType = NodeType.Category;
    }

    public GlossarySummary getGlossary() {
        return glossary;
    }

    public void setGlossary(GlossarySummary glossary) {
        this.glossary = glossary;
    }

    public CategorySummary getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategorySummary parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("Category=");
        sb.append(super.toString(sb));


        if (glossary!=null ){
            sb.append(", glossary=");
            sb.append(glossary.toString(sb));

        }
        if (parentCategory!=null ){
            sb.append(", parent Category=");
            sb.append(parentCategory.toString(sb));

        }
        sb.append('}');
        return sb;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        Node node = (Node) o;
        if (!(node.equals((Node)o))) return false;
        if (glossary != null ? !glossary.equals(category.glossary) : category.glossary != null) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int  result =super.hashCode();
        result = 31 * result + (glossary != null ? glossary.hashCode() : 0);
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0);
        return result;
    }

}
