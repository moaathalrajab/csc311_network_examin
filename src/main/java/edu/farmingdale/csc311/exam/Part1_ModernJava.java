package edu.farmingdale.csc311.exam;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PART 1 — Modern Java Foundations
 * Covers: records, sealed interfaces, pattern-matching switch, virtual threads.
 *
 * Run this file's main() method. The program should print, in order:
 *   - Three parsed payments (one Cash, one Card, one GiftCard)
 *   - Descriptions produced by describePayment(...)
 *   - Parallel user+order fetch timing (should be ~300 ms, not 600 ms)
 */
public class Part1_ModernJava {

    // ---------- Records (given) ----------

    /** An immutable User DTO. A record gets equals/hashCode/toString for free. */
    public record User(long id, String name, String email) {
        public User {
            if (email == null || !email.contains("@"))
                throw new IllegalArgumentException("bad email: " + email);
        }
    }

    /** An order attached to a user. */
    public record Order(long userId, String item, double total) { }

    // ---------- Sealed payment hierarchy ----------

    /**
     * A sealed interface restricts who may implement it. The compiler uses this to
     * check that a pattern-matching switch is exhaustive.
     *
     * TODO 1.1 — Extend the Payment hierarchy:
     *   Add a new record `GiftCard(double amount, String code)` that implements Payment.
     *   You must update the `permits` clause below AND create the record.
     *   Requirement: the compact constructor must throw IllegalArgumentException
     *   if `code` is null or shorter than 4 characters.
     */
    public sealed interface Payment permits Cash, Card, GiftCard { }

    public record Cash(double amount) implements Payment { }
    public record Card(double amount, String last4) implements Payment {
        public Card {
            if (last4 == null || last4.length() != 4)
                throw new IllegalArgumentException("last4 must be exactly 4 digits");
        }
    }
    public record GiftCard(double amount, String code) implements Payment {
        public GiftCard {
            if (code == null || code.length() < 4)
                throw new IllegalArgumentException("code must be at least 4 characters");
        }
    }

    // ---------- Pattern-matching switch ----------

    /**
     * TODO 1.2 — Implement describePayment(Payment p) using a pattern-matching switch
     *   expression. Each branch must use record-pattern deconstruction (no calls to
     *   p.amount() outside the switch). Return exactly these formats:
     *     Cash      -> "Cash: $AMOUNT"                e.g. "Cash: $42.00"
     *     Card      -> "Card ending in LAST4: $AMOUNT" e.g. "Card ending in 4242: $99.99"
     *     GiftCard  -> "GiftCard CODE: $AMOUNT"        e.g. "GiftCard ABCD1234: $25.00"
     *   All amounts formatted to two decimal places.
     *   The switch must be EXHAUSTIVE — no default branch.
     */
    public static String describePayment(Payment p) {
        return switch (p) {
            case Cash(double amount) -> String.format("Cash: $%.2f", amount);
            case Card(double amount, String last4) -> String.format("Card ending in %s: $%.2f", last4, amount);
            case GiftCard(double amount, String code) -> String.format("GiftCard %s: $%.2f", code, amount);
        };
    }

    // ---------- Virtual-thread parallel fetch (given, used to demonstrate Loom) ----------

    /** Pretends to do a slow network call. */
    private static User fetchUser(long id) {
        sleep(300);
        return new User(id, "student-" + id, "student" + id + "@farmingdale.edu");
    }

    private static Order fetchOrder(long userId) {
        sleep(300);
        return new Order(userId, "CSC311 Textbook", 89.95);
    }

    public record UserPage(User user, Order order) { }

    /**
     * Fan-out two calls in parallel using virtual threads + CompletableFuture.
     * Total wall time should be ~300 ms, not ~600 ms.
     */
    public static UserPage loadPageParallel(long userId) throws Exception {
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            var userF  = CompletableFuture.supplyAsync(() -> fetchUser(userId),  exec);
            var orderF = CompletableFuture.supplyAsync(() -> fetchOrder(userId), exec);
            return new UserPage(userF.join(), orderF.join());
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ---------- Demo driver ----------

    public static void main(String[] args) throws Exception {
        System.out.println("== Part 1: Modern Java Foundations ==");

        // These three lines will only compile once you complete TODO 1.1.
        List<Payment> payments = List.of(
                new Cash(42.00),
                new Card(99.99, "4242"),
                new GiftCard(25.00, "ABCD1234")
        );
        payments.forEach(p -> System.out.println("  " + describePayment(p)));

        long start = System.currentTimeMillis();
        UserPage page = loadPageParallel(7L);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println();
        System.out.println("  Loaded: " + page);
        System.out.println("  Wall time: " + elapsed + " ms  (expect ~300 ms, NOT ~600 ms)");
    }
}
