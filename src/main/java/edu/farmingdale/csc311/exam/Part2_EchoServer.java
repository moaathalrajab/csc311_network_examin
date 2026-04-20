package edu.farmingdale.csc311.exam;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * PART 2 — TCP Echo Server with Virtual Threads
 * Covers: ServerSocket, Socket streams, one-virtual-thread-per-client.
 *
 * HOW TO TEST:
 *   1. Run this file's main() — it will bind on localhost:5050.
 *   2. In another terminal:  nc localhost 5050
 *   3. Type "hello" and press Enter. The server should send back "HELLO".
 *   4. Type "quit" and press Enter. The server should close that client's socket.
 *   5. Press Ctrl-C on the server terminal — it should print "Server stopped cleanly."
 *
 * NOTES:
 *   - The server spawns one virtual thread per client (slide 17 pattern).
 *   - You may run multiple netcat sessions in parallel — each gets its own VT.
 */
public class Part2_EchoServer {

    private static final int PORT = 5050;
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) throws IOException {
        ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Echo server listening on port " + PORT + "  (Ctrl-C to stop)");

        /*
         * TODO 2.2 — Graceful shutdown.
         *   Register a JVM shutdown hook (Runtime.getRuntime().addShutdownHook(...))
         *   that:
         *     (a) sets running.set(false);
         *     (b) closes the ServerSocket so accept() stops blocking;
         *     (c) shuts down the executor;
         *     (d) prints "Server stopped cleanly." to stdout.
         *   Without this the program leaks resources on Ctrl-C and tests will fail.
         */
        // TODO 2.2: add shutdown hook here

        while (running.get()) {
            try {
                Socket client = server.accept();
                exec.submit(() -> handleClient(client));
            } catch (IOException e) {
                if (running.get()) System.err.println("accept failed: " + e.getMessage());
            }
        }
    }

    /**
     * TODO 2.1 — Per-client echo loop.
     *   Implement the contract:
     *     - Read text lines from the client (use BufferedReader on getInputStream()).
     *     - For each non-null line:
     *         * If the line (trimmed, case-insensitive) equals "quit", break the loop.
     *         * Otherwise send back the line converted to UPPERCASE, terminated by "\n".
     *     - Use PrintWriter with auto-flush so the client sees each response immediately.
     *     - Always close the socket in a try-with-resources or finally block.
     *   Log one line per connection: "connected: <remoteAddress>" on open,
     *   and "disconnected: <remoteAddress>" on close.
     */
    static void handleClient(Socket client) {
        // TODO 2.1: implement the echo loop
        throw new UnsupportedOperationException("TODO 2.1: implement per-client echo loop");
    }
}
