public class Main {
    static void main(String[] args) {
        Pizza basePizza = new Pizza(false);
        basePizza.addExtraToppings();
        basePizza.addExtraCheese();
        basePizza.takeAway();
        basePizza.getBill();


        DeluxePizza DP = new DeluxePizza(true);
        DP.takeAway();
        DP.getBill();

    }
}