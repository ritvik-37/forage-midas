# Midas Core - JPMC Advanced Software Engineering Forage

Project repo for the JPMC Advanced Software Engineering Forage program.

## Task 1: Environment Setup and Initialization
Set up local environment, configured Java 17 and Maven wrapper. Added necessary dependencies (Spring Web, Spring Data JPA, H2 Database, Spring Kafka, Spring Test) and application properties (`application.yml`).
**Answer:**
```text
---begin output ---
1142725631254665682354316777216387420489
---end output ---
```

## Task 2: Kafka message broker integration
Integrated an embedded Kafka queue. Implemented consumers to track streaming financial transactions to decouple Midas Frontend with Midas Core. Deserialized transactions into objects and set up incoming listeners.
**Answer:** The amounts attached to the first four transactions received by Midas Core:
```text
122.86, 42.87, 161.79, 22.22
```

## Task 3: Database Integration
Integrated H2 Database (In-Memory Database for testing). Intercepted incoming transactions, validated the entities (checking sender, recipient format and available balance) and appropriately updated the active balances atomically utilizing Spring Data JPA.
**Answer:** Waldorf's balance after processing all transactions:
```text
627.86
```

## Task 4: Incentive API Integration
Wired Midas Core to query an external Incentive API acting on port `8080`. Handled POST requests using `RestTemplate` that retrieve fractional incentives per transaction and appropriately credit the recipients' accounts alongside storing `TransactionRecord` entities into the persistence layer.
**Answer:** Wilbur's balance after all transactions have been processed (rounded down to the nearest integer):
```text
3089
```

## Task 5: REST API Controller
Launched a REST controller bound to port `33400`. Added a `/balance` GET endpoint accepting `userId` request parameters that surfaces user balance JSON objects so customers can query accounts independently of the transaction pipeline processing.
**Answer:**
```text
---begin output ---
Balance {amount=0.0}
Balance {amount=1326.98}
Balance {amount=2567.52}
Balance {amount=2740.33}
Balance {amount=140.96999}
Balance {amount=10.419973}
Balance {amount=845.49005}
Balance {amount=657.49}
Balance {amount=99.189995}
Balance {amount=3434.0002}
Balance {amount=2157.1902}
Balance {amount=779421.3}
Balance {amount=0.0}
---end output ---
```
