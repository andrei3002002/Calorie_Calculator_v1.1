package Calculator;

import DataProduct.Product;
import java.util.List;

public class Calculator {

  /**
   * Рассчитывает общую калорийность на основе списка продуктов и их весов.
   *
   * @param products Список продуктов.
   * @param weights  Список весов для каждого продукта. Каждый вес соответствует продукту в списке
   *                 продуктов.
   * @return Общая калорийность продуктов, учитывая их вес.
   */
  public double calculateTotalCalories(List<Product> products, List<Double> weights) {
    double totalCalories = 0;
    for (int i = 0; i < products.size(); i++) {
      totalCalories += products.get(i).getCalories() * weights.get(i) / 100;
    }
    return totalCalories;
  }
}
