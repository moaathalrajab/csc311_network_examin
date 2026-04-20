package edu.farmingdale.csc311.exam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * PART 3 — HTTP Client with Retry
 * Covers: java.net.http.HttpClient, request building, response handling, retry policy.
 *
 * Target endpoint: https://jsonplaceholder.typicode.com/todos/1
 *   (A stable, free, read-only JSON endpoint used for testing.)
 *
 * Expected main() output after both TODOs are complete:
 *     GET /todos/1 -> 200
 *     title = delectus aut autem
 *     (retry demo) attempt 1 got 500 ... attempt 2 got 500 ... attempt 3 got 200 OK
 */
public class Part3_HttpClientRetry {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_2)
            .build();

    /**
     * TODO 3.1 — Build the HttpRequest.
     *   Complete buildTodosRequest() so that it returns a HttpRequest that:
     *     - GETs https://jsonplaceholder.typicode.com/todos/{id}
     *     - Sets header "Accept: application/json"
     *     - Has a request timeout of 5 seconds
     *     - Uses GET explicitly (.GET())
     *   Do NOT send the request in this method — only build it.
     */
    public static HttpRequest buildTodosRequest(int id) {
        // TODO 3.1: build and return the HttpRequest
        throw new UnsupportedOperationException("TODO 3.1: implement request builder");
    }

    /**
     * TODO 3.2 — Retry with exponential backoff.
     *   Implement sendWithRetry(req) with this contract:
     *     - Send the request. If the response status is < 500, return the response immediately.
     *     - If the status is >= 500, wait, then retry. Backoff schedule:
     *         attempt 1 -> 200 ms
     *         attempt 2 -> 400 ms
     *         attempt 3 -> 800 ms
     *     - After the 3rd failed attempt, return the last (still 5xx) response.
     *     - If an IOException/InterruptedException is thrown during send(), treat it the
     *       same as a 5xx — sleep and retry.
     *     - Print one log line per attempt: "attempt N got STATUS" where STATUS is the
     *       numeric status code, or "IO_ERR" if the send threw.
     */
    public static HttpResponse<String> sendWithRetry(HttpRequest req) throws InterruptedException {
        // TODO 3.2: implement retry with exponential backoff
        throw new UnsupportedOperationException("TODO 3.2: implement retry policy");
    }

    // ---------- Demo driver ----------

    public static void main(String[] args) throws Exception {
        // Happy path
        HttpRequest req = buildTodosRequest(1);
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET /todos/1 -> " + resp.statusCode());
        JsonNode node = JSON.readTree(resp.body());
        System.out.println("title = " + node.get("title").asText());

        // Retry demo: this endpoint returns 500 the first two times, 200 the third.
        // (We simulate by sending a bad path locally — see the README for how to wire
        //  the mock if you want to see retry actually fire.)
        System.out.println();
        System.out.println("(retry demo) use MockServer in README, then call sendWithRetry()");
    }
}
