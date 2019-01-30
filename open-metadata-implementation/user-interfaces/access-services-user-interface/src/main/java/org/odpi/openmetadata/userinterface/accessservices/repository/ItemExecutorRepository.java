/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.repository;

import org.odpi.openmetadata.userinterface.accessservices.domain.Item;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

public interface ItemExecutorRepository extends JpaSpecificationExecutor<Item>, Repository<Item, Long> {

}

