package DataProduct;

/**
 * Класс {@code Product} представляет собой продукт с названием и калорийностью.
 */
public class Product {

  private String name;
  private double calories;

  /**
   * Создает новый экземпляр продукта с указанными названием и калорийностью.
   *
   * @param name     Название продукта.
   * @param calories Калорийность продукта.
   */
  public Product(String name, double calories) {
    this.name = name;
    this.calories = calories;
  }

  public String getName() {
    return name;
  }

  public double getCalories() {
    return calories;
  }

  public void setCalories(double newCalories) {
    this.calories = newCalories;
  }
}
