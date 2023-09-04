package DataProduct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс {@code ProductStorage} обеспечивает функциональность хранения и загрузки продуктов из
 * файла. Файл используется для постоянного хранения информации о продуктах.
 */
public class ProductStorage {

  private static final String FILENAME = "res/products.txt";

  /**
   * Сохраняет список продуктов в файл.
   *
   * @param products Список продуктов для сохранения.
   */
  public void saveProducts(List<Product> products) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
      for (Product product : products) {
        writer.write(product.getName() + "," + product.getCalories());
        writer.newLine();
      }
    } catch (IOException e) {
      System.out.println("Ошибка при сохранении продуктов: " + e.getMessage());
    }
  }

  /**
   * Загружает список продуктов из файла.
   *
   * @return Список продуктов, загруженных из файла.
   */
  public List<Product> loadProducts() {
    List<Product> products = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        products.add(new Product(parts[0], Double.parseDouble(parts[1])));
      }
    } catch (IOException e) {
      System.out.println("Ошибка при загрузке продуктов: " + e.getMessage());
    }
    return products;
  }
}
