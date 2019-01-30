/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.userinterface.accessservices.repository;

import org.odpi.openmetadata.userinterface.accessservices.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    Optional<Item> findOneByName(String name);
}
