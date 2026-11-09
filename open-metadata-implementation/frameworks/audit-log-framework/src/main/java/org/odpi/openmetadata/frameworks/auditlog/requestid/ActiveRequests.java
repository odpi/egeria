/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog.requestid;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ActiveRequests tracks the REST requests that are being processed by the servers running in this JVM (platform).
 * <p>
 * A request from an external caller often fans out into further REST calls between the servers on the platform -
 * for example, a view server calls the metadata access server on behalf of the same user.  Egeria's REST clients
 * pass the caller's request identifier on each of these calls, so a request that arrives carrying the identifier of
 * a request that is still being processed on this platform is a nested call made on behalf of that original
 * (root) request, rather than a new request from an external caller.
 * <p>
 * This lets the activity reporting (the METADATA-OBSERVABILITY audit log messages) record each external request
 * once, and each anchor element it touches once, rather than once per internal hop.  A request identifier is only
 * treated as nested while the root request is in flight, so a client thread that reuses the same request
 * identifier for a sequence of separate calls is still seen as making separate requests.
 * <p>
 * Calls between servers on different platforms cannot be detected and are reported as new requests.
 */
public final class ActiveRequests
{
    /*
     * An entry left behind by a request whose end was never recorded is ignored once it is this old.
     */
    static final long MAX_REQUEST_AGE_MS = 10 * 60 * 1000L;

    /*
     * When the map of active requests grows beyond this size, the stale entries are swept out.
     */
    private static final int SWEEP_THRESHOLD = 2000;

    /*
     * A thread that has more unfinished requests than this has lost track of their ends.
     */
    private static final int MAX_THREAD_DEPTH = 32;


    /**
     * The state of a root request and the nested calls made on its behalf.
     */
    private static class ActiveRequest
    {
        private final long        startTime = System.currentTimeMillis();
        private int               depth     = 1;
        private final Set<String> reported  = ConcurrentHashMap.newKeySet();

        boolean isStale(long now)
        {
            return (now - startTime) > MAX_REQUEST_AGE_MS;
        }
    }


    /**
     * A request being processed by the current thread.
     *
     * @param requestId request identifier
     * @param request shared state of the root request
     * @param nested is this a nested call made on behalf of a request already in flight?
     */
    private record ThreadRequest(String requestId, ActiveRequest request, boolean nested)
    {
    }


    private static final Map<String, ActiveRequest>        activeRequests = new ConcurrentHashMap<>();
    private static final ThreadLocal<Deque<ThreadRequest>> threadRequests = ThreadLocal.withInitial(ArrayDeque::new);


    /**
     * Private constructor - this class only has static methods.
     */
    private ActiveRequests()
    {
    }


    /**
     * Record that the current thread has started processing a REST request.  Each call must be matched by a call
     * to {@link #endRequest()} once the request is complete.
     *
     * @param requestId identifier of the request (null is ignored)
     */
    public static void beginRequest(String requestId)
    {
        if (requestId == null)
        {
            return;
        }

        Deque<ThreadRequest> stack = threadRequests.get();

        if (stack.size() >= MAX_THREAD_DEPTH)
        {
            /*
             * The ends of these requests were never recorded.  Release them so neither the thread nor the
             * map of active requests grows without limit.
             */
            while (! stack.isEmpty())
            {
                endRequest();
            }

            stack = threadRequests.get();
        }

        long now = System.currentTimeMillis();

        if (activeRequests.size() > SWEEP_THRESHOLD)
        {
            activeRequests.values().removeIf(activeRequest -> activeRequest.isStale(now));
        }

        boolean[] nested = { false };

        ActiveRequest request = activeRequests.compute(requestId, (key, existing) ->
        {
            if ((existing == null) || (existing.isStale(now)))
            {
                return new ActiveRequest();
            }

            existing.depth++;
            nested[0] = true;
            return existing;
        });

        stack.push(new ThreadRequest(requestId, request, nested[0]));
    }


    /**
     * Record that the current thread has finished processing its most recent REST request.
     */
    public static void endRequest()
    {
        Deque<ThreadRequest> stack = threadRequests.get();

        ThreadRequest threadRequest = stack.poll();

        if (threadRequest != null)
        {
            activeRequests.computeIfPresent(threadRequest.requestId(), (key, existing) ->
            {
                if (existing != threadRequest.request())
                {
                    /*
                     * The original entry went stale and was replaced - leave the new one alone.
                     */
                    return existing;
                }

                existing.depth--;
                return (existing.depth <= 0) ? null : existing;
            });
        }

        if (stack.isEmpty())
        {
            threadRequests.remove();
        }
    }


    /**
     * Is the current thread processing a nested call made on behalf of a request that was already being
     * processed on this platform?  It returns false when no request is being tracked for the thread.
     *
     * @return boolean
     */
    public static boolean isNestedRequest()
    {
        ThreadRequest threadRequest = threadRequests.get().peek();

        return (threadRequest != null) && (threadRequest.nested());
    }


    /**
     * Record that an activity has been reported for the root request that the current thread is working on, and
     * return whether this is the first time it has been reported.  When no request is being tracked for the
     * thread, every report is treated as the first.
     *
     * @param activityKey key identifying the activity - for example the anchor element's unique identifier and the
     *                    kind of activity
     * @return true if the activity has not been reported before for this request
     */
    public static boolean isFirstReport(String activityKey)
    {
        ThreadRequest threadRequest = threadRequests.get().peek();

        if ((threadRequest == null) || (activityKey == null))
        {
            return true;
        }

        return threadRequest.request().reported.add(activityKey);
    }


    /**
     * Return the number of requests being tracked.  Used by the tests.
     *
     * @return count
     */
    static int getActiveRequestCount()
    {
        return activeRequests.size();
    }
}
