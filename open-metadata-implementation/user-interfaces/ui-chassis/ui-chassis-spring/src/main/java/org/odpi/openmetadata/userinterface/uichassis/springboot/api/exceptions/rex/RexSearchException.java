/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RexSearchException extends RuntimeException {

    private final String searchText;
    private final String serverName;
    private final String message;
    private final String searchCategory;

    public RexSearchException(String message, String searchCategory) {
        this.message = message;
        this.searchCategory = searchCategory;
        this.searchText = null;
        this.serverName = null;
    }

    public RexSearchException(String message, String serverName, String searchText, String searchCategory) {
        this.message = message;
        this.serverName = serverName;
        this.searchText = searchText;
        this.searchCategory = searchCategory;
    }

    public String getMessage() {
        return message;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public String getSearchText() {
        return searchText;
    }

    public String getServerName() {
        return serverName;
    }
}
