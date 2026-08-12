<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# User Authentication (user-authn)

The user-authn module provides the back-end services for managing the logon and logoff of users using tokens.

* `LoginController` provides a simple token service that logs a user into open metadata, using the Spring
  framework to provide the authentication token.  The user details are managed by the
  [metadata security connector](https://egeria-project.org/concepts/open-metadata-security-connector).
* `LogoutController` provides the REST API to log out a user, and `TokenLogoutSuccessHandler` handles the
  result.
* `TokenService` generates JWT bearer tokens for authenticated users, using `KeyPairGeneratorUtils` and
  `RSAGenerator` to work with the encrypted key pairs used to sign them.  `TokenClient` defines the interface
  for a stateful web token that uses persistence and expiration validation.
* `SecurityConfig` and `PlatformSecurityConfig` configure Spring Security for the platform, and
  `PlatformUserDetails`/`PlatformUserDetailsService` supply the authenticated user's details.
* `RoleService` sets up user roles, and `ComponentService` provides the configuration properties used by the
  UI to show or hide components based on role.
* `AboutController` and `PublicController` provide build and general information about the running
  application.

----
Return to [user-security](..).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.