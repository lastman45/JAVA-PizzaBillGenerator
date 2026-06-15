# Pizza Ordering System — Java OOP Demo

A simple Java application that models a pizza ordering system using object-oriented principles including **inheritance**, **method overriding**, and **encapsulation**. It demonstrates how a base class can be extended to create specialized subclasses with predefined configurations.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Class Overview](#class-overview)
  - [Pizza.java](#pizzajava)
  - [DeluxePizza.java](#deluxepizzajava)
  - [Main.java](#mainjava)
- [Pricing Model](#pricing-model)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
- [How to Run](#how-to-run)
- [Sample Output](#sample-output)
- [Known Issues & Suggested Improvements](#known-issues--suggested-improvements)

---

## Project Structure

```
pizza-system/
├── Pizza.java         # Base class — core pizza logic and pricing
├── DeluxePizza.java   # Subclass — pre-configured deluxe pizza
└── Main.java          # Entry point — creates and bills pizza orders
```

---

## Class Overview

### `Pizza.java`

The base class representing a standard pizza. It manages all pricing state and exposes methods to add optional upgrades.

**Fields**

| Field | Type | Default | Description |
|---|---|---|---|
| `price` | `int` | Set by constructor | Running total price of the pizza |
| `veg` | `Boolean` | Passed in | Whether the pizza is vegetarian |
| `extraCheesePrice` | `int` | `100` | Fixed cost of adding extra cheese |
| `extraToppingsPrice` | `int` | `150` | Fixed cost of adding extra toppings |
| `backPackPrice` | `int` | `20` | Fixed cost of take-away packaging |
| `basePizzaPrice` | `int` | Set by constructor | Snapshot of the base price (used in billing) |
| `isExtraCheeseAdded` | `boolean` | `false` | Tracks whether extra cheese was added |
| `isExtraToppingsAdded` | `boolean` | `false` | Tracks whether extra toppings were added |
| `isOptedForTakeAway` | `boolean` | `false` | Tracks whether take-away was selected |

**Constructor**

```java
public Pizza(Boolean veg)
```

Sets the base price based on pizza type:
- Vegetarian: **₹300**
- Non-Vegetarian: **₹400**

**Methods**

| Method | Description |
|---|---|
| `addExtraCheese()` | Adds ₹100 to the price and sets the cheese flag |
| `addExtraToppings()` | Adds ₹150 to the price and sets the toppings flag |
| `takeAway()` | Adds ₹20 packaging cost and sets the take-away flag |
| `getBill()` | Prints an itemised bill to the console |

---

### `DeluxePizza.java`

A subclass of `Pizza` that represents a pre-configured deluxe pizza. Extra cheese and extra toppings are added automatically at construction time via `super` calls.

```java
public class DeluxePizza extends Pizza {
    public DeluxePizza(Boolean veg) {
        super(veg);
        super.addExtraCheese();
        super.addExtraToppings();
    }

    @Override
    public void addExtraCheese() { }  // No-op: intentionally disabled

    @Override
    public void addExtraToppings() { } // No-op: intentionally disabled
}
```

**Design intent:** The overridden `addExtraCheese()` and `addExtraToppings()` methods are left empty to prevent callers from adding these upgrades a second time after the deluxe configuration is already applied. Only the `super.*` variants called in the constructor take effect.

---

### `Main.java`

The entry point of the application. It creates two orders and prints their bills.

```java
public class Main {
    static void main(String[] args) {
        // Order 1: Plain non-veg pizza with all add-ons
        Pizza basePizza = new Pizza(false);
        basePizza.addExtraToppings();
        basePizza.addExtraCheese();
        basePizza.takeAway();
        basePizza.getBill();

        // Order 2: Deluxe vegetarian pizza with take-away
        DeluxePizza DP = new DeluxePizza(true);
        DP.takeAway();
        DP.getBill();
    }
}
```

> **Note:** The `main` method is declared `static void` without the `public` modifier. This will cause a compilation error in standard Java. See [Known Issues](#known-issues--suggested-improvements) below.

---

## Pricing Model

| Item | Price |
|---|---|
| Base pizza (vegetarian) | ₹300 |
| Base pizza (non-vegetarian) | ₹400 |
| Extra cheese | +₹100 |
| Extra toppings | +₹150 |
| Take-away packaging | +₹20 |

**Example — Order 1 (non-veg + all add-ons + take-away):**

```
400 (base) + 150 (toppings) + 100 (cheese) + 20 (take-away) = ₹670
```

**Example — Order 2 (deluxe veg + take-away):**

```
300 (base) + 100 (cheese) + 150 (toppings) + 20 (take-away) = ₹570
```

---

## OOP Concepts Demonstrated

### 1. Encapsulation
All fields in `Pizza` are declared `private`. External code interacts only through public methods (`addExtraCheese()`, `getBill()`, etc.), preventing direct manipulation of internal pricing state.

### 2. Inheritance
`DeluxePizza` extends `Pizza`, inheriting all its fields and methods. The subclass reuses the base pricing engine without duplicating code.

### 3. Method Overriding
`DeluxePizza` overrides `addExtraCheese()` and `addExtraToppings()` with empty bodies. This is a form of **defensive overriding** — it locks the deluxe configuration in place by making those public methods no-ops after construction.

### 4. `super` keyword
The `DeluxePizza` constructor uses `super(veg)` to invoke the parent constructor, and `super.addExtraCheese()` / `super.addExtraToppings()` to call the parent's actual implementations, bypassing the overridden (no-op) versions.

---

## How to Run

**Prerequisites:** Java Development Kit (JDK) 8 or higher.

```bash
# 1. Compile all source files
javac Pizza.java DeluxePizza.java Main.java

# 2. Run the program
java Main
```

---

## Sample Output

```
Total Pizza Price
Plain Pizza: 400
Extra Toppings Added: 150
Extra Cheese Added: 100
Take Away Opted For: 20
Bill: 670

Total Pizza Price
Plain Pizza: 300
Extra Cheese Added: 100
Extra Toppings Added: 150
Take Away Opted For: 20
Bill: 570
```

---

## Known Issues & Suggested Improvements

### Bug: `main` method missing `public` modifier

In `Main.java`, the entry point is declared as:

```java
static void main(String[] args) { ... }
```

The JVM requires `public static void main(String[] args)`. Without `public`, the program will compile but fail at runtime with:

```
Main method not found in class Main, please define the main method as:
   public static void main(String[] args)
```

**Fix:**

```java
public static void main(String[] args) { ... }
```

### Suggested Improvements

- **Guard against double-adding:** Currently, calling `addExtraCheese()` twice on a base `Pizza` charges the customer twice. Add a guard:
  ```java
  public void addExtraCheese() {
      if (!isExtraCheeseAdded) {
          isExtraCheeseAdded = true;
          this.price += extraCheesePrice;
      }
  }
  ```

- **Use `boolean` instead of `Boolean`:** The constructor takes `Boolean` (the wrapper class) instead of the primitive `boolean`. This allows `null` to be passed, which would throw a `NullPointerException` inside the constructor. Prefer primitive `boolean` for this use case.

- **Extract an interface or abstract class:** For extensibility, consider defining a `Customizable` interface or an abstract `AbstractPizza` class so new pizza types (e.g., `GourmetPizza`, `VeganPizza`) can be added cleanly.

- **Separate bill formatting from printing:** `getBill()` mixes string building with `System.out.println`. Consider returning a formatted `String` from a `generateBill()` method and letting the caller decide how to display it — this makes the class easier to test.

- **Use currency formatting:** Prices are plain `int` values. For production code, consider using `java.text.NumberFormat` or `java.util.Currency` for locale-aware currency display.
