/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.authfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;

/**
 * Shared helpers for the auth-fvt suite.
 * <br><br>
 * The calls here are made with the JDK's own HTTP client rather than one of Egeria's clients.  That is
 * deliberate: this suite is testing the HTTP-level authentication contract - which endpoints are open,
 * what a rejected logon looks like, whether a bearer token is accepted - and an Egeria client would sit
 * between the test and exactly the behaviour being checked.
 */
final class AuthFvtTestSupport
{
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
                                                            .connectTimeout(Duration.ofSeconds(20))
                                                            .build();

    /**
     * An endpoint that requires authentication.  The platform origin endpoint next to it is explicitly
     * permitAll in SecurityConfig, so it would pass with or without a token and proves nothing.
     */
    static final String PROTECTED_PATH = "/open-metadata/platform-services/server-platform/security/user-list";

    private AuthFvtTestSupport()
    {
        // no instances
    }


    /**
     * Request a bearer token for a user.
     *
     * @param userId user to log on as
     * @param password current password
     * @param newPassword new password, or null to log on without changing it
     * @return the platform's response - a raw token string on success
     * @throws Exception the request could not be sent
     */
    static HttpResponse<String> requestToken(String userId,
                                             String password,
                                             String newPassword) throws Exception
    {
        StringBuilder body = new StringBuilder("{\"userId\":\"").append(userId).append('"');

        if (password != null)
        {
            body.append(",\"password\":\"").append(password).append('"');
        }

        if (newPassword != null)
        {
            body.append(",\"newPassword\":\"").append(newPassword).append('"');
        }

        body.append('}');

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + "/api/token"))
                                         .header("Content-Type", "application/json")
                                         .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                                         .build();

        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Log on and return the token, failing if the logon did not succeed.
     *
     * @param userId user to log on as
     * @param password current password
     * @return bearer token
     * @throws Exception the logon failed or could not be sent
     */
    static String logon(String userId,
                        String password) throws Exception
    {
        HttpResponse<String> response = requestToken(userId, password, null);

        if (response.statusCode() != 200)
        {
            throw new IllegalStateException("Logon for " + userId + " failed with status " + response.statusCode() +
                                                    " and body: " + response.body());
        }

        return response.body();
    }


    /**
     * Issue a GET against the platform, optionally presenting a bearer token.
     *
     * @param path path below the platform url root
     * @param token bearer token, or null to send no Authorization header
     * @return the platform's response
     * @throws Exception the request could not be sent
     */
    static HttpResponse<String> get(String path,
                                    String token) throws Exception
    {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                                                 .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + path))
                                                 .GET();

        if (token != null)
        {
            builder.header("Authorization", "Bearer " + token);
        }

        return HTTP_CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Issue a POST of a JSON body against the platform, optionally presenting a bearer token.
     *
     * @param path path below the platform url root
     * @param token bearer token, or null to send no Authorization header
     * @param jsonBody request body
     * @return the platform's response
     * @throws Exception the request could not be sent
     */
    static HttpResponse<String> post(String path,
                                     String token,
                                     String jsonBody) throws Exception
    {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                                                 .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + path))
                                                 .header("Content-Type", "application/json")
                                                 .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        if (token != null)
        {
            builder.header("Authorization", "Bearer " + token);
        }

        return HTTP_CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Issue a DELETE against the platform, presenting a bearer token.
     *
     * @param path path below the platform url root
     * @param token bearer token
     * @return the platform's response
     * @throws Exception the request could not be sent
     */
    static HttpResponse<String> delete(String path,
                                       String token) throws Exception
    {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(OMAGPlatformExtension.getPlatformURLRoot() + path))
                                         .header("Authorization", "Bearer " + token)
                                         .DELETE()
                                         .build();

        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Read the secrets recorded for a user in the working copy of the user directory, so that a test can
     * check what the platform actually persisted rather than only what it accepted over HTTP.
     *
     * @param userId user to look up
     * @return the user's secrets map, or null if the user is not in the directory
     * @throws Exception the file could not be read
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> readStoredSecrets(String userId) throws Exception
    {
        JsonNode root = YAML.readTree(Files.readString(OMAGPlatformExtension.getUserDirectoryPath()));
        JsonNode user = root.path("secretsCollections")
                            .path(OMAGPlatformExtension.SECRETS_COLLECTION)
                            .path("users")
                            .path(userId);

        if (user.isMissingNode() || user.isNull())
        {
            return null;
        }

        JsonNode secrets = user.path("secrets");

        if (secrets.isMissingNode() || secrets.isNull())
        {
            return Map.of();
        }

        return JSON.convertValue(secrets, Map.class);
    }


    /**
     * Read the account status recorded for a user in the working copy of the user directory.
     *
     * @param userId user to look up
     * @return the status as written in the file, or null if the user is not there
     * @throws Exception the file could not be read
     */
    static String readStoredAccountStatus(String userId) throws Exception
    {
        JsonNode root = YAML.readTree(Files.readString(OMAGPlatformExtension.getUserDirectoryPath()));
        JsonNode user = root.path("secretsCollections")
                            .path(OMAGPlatformExtension.SECRETS_COLLECTION)
                            .path("users")
                            .path(userId);

        if (user.isMissingNode() || user.isNull())
        {
            return null;
        }

        JsonNode status = user.path("userAccountStatus");

        return status.isMissingNode() ? null : status.asText();
    }
}
