/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.userinterface.accessservices.repository;


import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
}
