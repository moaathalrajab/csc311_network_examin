# CSC311 — Advanced Java Networking Exam

Professor: Mo Alrajam — Farmingdale State College
Course: CSC311 Advanced Programming

## Setup

1. Install JDK 21 (or newer). Verify with `java -version`.
2. Open the project folder in IntelliJ IDEA (File → Open → select `csc311_exam`).
3. IntelliJ will detect the Maven project and download dependencies automatically.
4. Once indexing finishes, open the **TODO** tool window (View → Tool Windows → TODO). You should see **10 TODOs** — two per part.

## Running each part

Each `Part*.java` file has a `main()` method. Right-click the file in IntelliJ → **Run 'PartX_...main()'**.

| Part | File                         | What it does                                    |
|------|------------------------------|-------------------------------------------------|
| 1    | `Part1_ModernJava.java`      | Runs locally, prints to console                 |
| 2    | `Part2_EchoServer.java`      | Starts TCP server on `localhost:5050`           |
| 3    | `Part3_HttpClientRetry.java` | Hits `jsonplaceholder.typicode.com`             |
| 4    | `Part4_UserApi.java`         | Starts REST API on `localhost:7070`             |
| 5    | `Part5_Observability.java`   | Starts instrumented API on `localhost:7080`     |

## How to test Part 2 (TCP echo)

In a separate terminal:
```
nc localhost 5050
hello
HELLO
quit
```

## How to test Part 4 (REST API)

```
curl -i -X POST localhost:7070/users \
     -H "Content-Type: application/json" \
     -d '{"name":"Ada","email":"ada@ex.com"}'

curl -i localhost:7070/users
curl -i -X DELETE localhost:7070/users/1
```

## How to test Part 5 (observability)

```
curl -i localhost:7080/hello
curl -i localhost:7080/boom
curl -i localhost:7080/missing/42
```

Every log line in your console should contain `[reqId=...]` — proof that MDC works.

## Submission

- Push the project to a private GitHub repo and share it with me, **or**
- Zip the entire `csc311_exam` folder (Maven project only — no `target/`, no `.idea/`) and email it to `alrajam@farmingdale.edu`.
- Include a short paragraph at the bottom of this README describing which TODOs you struggled with and why.

## Rubric (100 points total)

Each part is worth 20 points:
- 14 points — both TODOs implemented correctly
- 4 points — code compiles and runs
- 2 points — reasonable error handling + comments

## Academic integrity

You may consult the deck, the JDK docs, and the Javalin/Jackson/SLF4J docs. You may **not** copy code from another student or paste the problem into an AI chatbot. If you get stuck, come to office hours.
