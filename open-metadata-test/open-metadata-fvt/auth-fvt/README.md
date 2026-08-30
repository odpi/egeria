<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Authentication FVT (auth-fvt)

This suite exercises the OMAG Server Platform's own authentication: logging on, the bearer token that
results, changing a password, and managing user accounts.

## Why it exists

`user-authn` ships in every platform and is the first thing a user meets, yet before this suite it had
**no automated coverage of any kind** - not a single unit test, and no functional test either. Every other
FVT suite, and the BVT, deliberately exclude it and install a permit-all filter chain so they can run
hermetically. That is the right call for those suites, but it left the logon and token path untested.

It also happens to be the part of the platform built on the pieces of Spring Security that move most
between releases - `SecurityFilterChain`, `authorizeHttpRequests`, `AuthenticationManager`,
`NimbusJwtDecoder`, `BCryptPasswordEncoder` - and a mis-wired filter chain compiles perfectly and fails at
runtime. That combination is why this suite was written before the Spring upgrade rather than after it.

## Running it

```
./gradlew :open-metadata-test:open-metadata-fvt:auth-fvt:test -PrunAuthFvt
```

It needs no database and loads no archives, so it takes well under a minute. Like the other FVT suites it
is opt-in and does not run as part of a normal build.

## How it is set up

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/authfvt/OMAGPlatformExtension.java) starts one
platform in-process for the whole run, on a randomly allocated port. Three things make it different from
every other suite's platform:

* `user-authn` is **not** excluded - it is the component under test.
* Spring Security's auto-configuration is **not** switched off, so requests go through the real filter chain.
* `authentication.source=platform` is set, along with a `platform.security.provider` and a user directory,
  so `PlatformUserDetailsService` and `PlatformSecurityConfig` are both active.

The platform is configured programmatically rather than from an `application.properties` like the other
suites, because the user directory location has to be computed at runtime - see below.

## The user directory is a copy, and that matters

Changing a password **rewrites the user directory file in place** (`YAMLSecretsStoreConnector.saveUser`).
So the extension copies [auth-fvt-user-directory.omsecrets](src/test/resources/auth-fvt-user-directory.omsecrets)
into `build/auth-fvt-data/` on every run and points the platform at the copy.

Running against the file in `src/test/resources` would modify the repository's own source tree, and the
second run would start from a different state than the first - accounts would already have encrypted
passwords, and the tests that check the clear-text-to-encrypted migration would fail. Taking a fresh copy
each time is what makes the suite repeatable.

Each test uses its own account for the same reason: a password change persists, so tests sharing an
account would depend on the order they ran in.

## What it covers

[LogonFVT](src/test/java/org/odpi/openmetadata/authfvt/LogonFVT.java)

* valid credentials return a three-part JWT
* the token opens an endpoint that requires authentication
* the same endpoint returns 401 without a token
* a token whose signature has been altered is rejected
* the wrong password, an unknown user, and `DISABLED`/`LOCKED` accounts are all refused
* the endpoints `SecurityConfig` lists as `permitAll` stay reachable without a token

[PasswordChangeFVT](src/test/java/org/odpi/openmetadata/authfvt/PasswordChangeFVT.java) - the platform has
no separate change-password endpoint: `LoginRequest` carries `(userId, password, newPassword)` and
supplying the third field to `POST /api/token` changes the password and returns a token in one call.

* the new password works afterwards and the old one stops working
* a clear-text password is replaced by an encrypted one, not left beside it
* an account with `CREDENTIALS_EXPIRED` is refused an ordinary logon, admitted when it supplies a new
  password, and has its status cleared to `AVAILABLE` as a result
* a change presented with the wrong current password is rejected and writes nothing

[AccountLifecycleFVT](src/test/java/org/odpi/openmetadata/authfvt/AccountLifecycleFVT.java)

* an account created through the platform security API can log on, and cannot once it is deleted
* an account created with `CREDENTIALS_EXPIRED` honours that status
* account management itself requires a token

## A note on assertions

Several assertions here deliberately check an exact status code rather than "not 401". An earlier draft
asserted only that a protected endpoint did not return 401, and passed against a URL that did not
support `GET` at all - a 405 is also "not 401". The same draft tampered with the *last* character of the
JWT signature, which can land on base64url padding bits and leave the decoded signature unchanged, so the
token still verified and that test passed without testing anything.

Both are worth remembering when adding tests here: a negative assertion in an authentication suite can
pass because the thing under test is broken in a different way.

----
Return to [open-metadata-fvt](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
