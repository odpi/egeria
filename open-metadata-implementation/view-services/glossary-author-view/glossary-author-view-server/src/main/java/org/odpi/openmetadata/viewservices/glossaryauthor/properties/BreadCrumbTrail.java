/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryauthor.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * This contains information that a user interface can use to display a bread crumb trail. The first bread crumb in the list is the
 * highest element in the bread crumb and would in Western cultures be displayed on the left. The subsequent breadcrumbs are a sequence of
 * child bread crumbs. The current breadcrumb is the last element in the array
 */
public class BreadCrumbTrail {

    List<BreadCrumb> breadCrumbsWithoutPaths;
    public BreadCrumbTrail(List<BreadCrumb> breadCrumbs) {
        breadCrumbsWithoutPaths = breadCrumbs;
    }

    /**
     * Return breadcrumbs in the right order with the path information in them
     *
     * @return list of breadcrumbs
     */

    public List<BreadCrumb> getBreadCrumbs() {
        List<BreadCrumb> fullyFormedBreadCrumbs = new ArrayList<>();
        BreadCrumb previousBreadCrumb = null;
        // read the supplied breadcrumbs in reverse
        for (int i= breadCrumbsWithoutPaths.size()-1; i>=0; i-- ) {
            BreadCrumb breadCrumb = breadCrumbsWithoutPaths.get(i);
            BreadCrumb newBreadCrumb = new BreadCrumb(breadCrumb, previousBreadCrumb);
            previousBreadCrumb = newBreadCrumb;
            fullyFormedBreadCrumbs.add(newBreadCrumb);
        }
        return fullyFormedBreadCrumbs;
    }
}
