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

public class Category extends GovernedNode{
    private String glossaryName =null;
    private String parentCategoryGuid;
    private String parentCategoryName;

    public Category() {
        nodeType = NodeType.Category;
    }

    public String getGlossaryName() {
        return glossaryName;
    }

    public void setGlossaryName(String glossaryName) {
        this.glossaryName = glossaryName;
    }

    public String getParentCategoryGuid() {
        return parentCategoryGuid;
    }

    public void setParentCategoryGuid(String parentCategoryGuid) {
        this.parentCategoryGuid = parentCategoryGuid;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("Category=");
        sb.append(super.toString(sb));


        if (glossaryName!=null ){
            sb.append(", glossaryName=");
            sb.append(glossaryName);

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
        if (glossaryName != null ? !glossaryName.equals(category.glossaryName) : category.glossaryName != null) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int  result = ((Node)this).hashCode();
        result = 31 * result + (glossaryName != null ? glossaryName.hashCode() : 0);
        return result;
    }

}
