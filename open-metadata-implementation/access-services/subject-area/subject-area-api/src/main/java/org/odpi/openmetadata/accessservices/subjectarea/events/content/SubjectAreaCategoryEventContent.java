/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

public class SubjectAreaCategoryEventContent extends SubjectAreaEventContent{
    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    Category category = null;


}
