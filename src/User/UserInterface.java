package User;

import java.util.Scanner;
import java.util.List;
import DataProduct.*;


public class UserInterface {

  private final Scanner scanner;
  private final ProductStorage storage;

  public UserInterface() {
    this.scanner = new Scanner(System.in);
    this.storage = new ProductStorage();
  }

  // Основной метод для запуска интерфейса пользователя
  public void run() {
    List<Product> products = storage.loadProducts();

    while (true) {
      int choice = -1;

      while (true) {
        try {
          System.out.println("\n\t\t\t\t\t\033[1;94mГлавное меню\033[0m");
          int optionNumber = 1;
          for (MenuAction action : MenuAction.values()) {
            System.out.println(optionNumber + ". " + action.getDescription());
            optionNumber++;
          }
          System.out.print("\033[1;36mВыберите действие: \033[0m");

          choice = Integer.parseInt(scanner.nextLine());

          break;
        } catch (NumberFormatException e) {
          System.out.println("Введите целое число для выбора действия.");
        }
      }

      if (choice < 1 || choice > MenuAction.values().length) {
        System.out.println(
            "Неверный выбор. Пожалуйста, выберите действие из предложенного списка.");
        continue; // Это вернет нас к началу основного цикла while
      }
      // Обработка выбора пользователя
      switch (MenuAction.values()[choice - 1]) {
        case VIEW_PRODUCTS -> displayProductsInColumns(products);
        case ADD_PRODUCT -> addNewProduct(products);
        case EDIT_PRODUCT -> editProductCalories(products);
        case VIEW_CALORIES -> viewCalories(products);
        case CALCULATE_TOTAL_CALORIES -> calculateTotalCalories(products);
        case EXIT -> {
          System.out.println("До свидания!");
          scanner.close();
          return;
        }
        default -> System.out.println("Неверный ввод. Попробуйте снова.");
      }
    }
  }

  /**
   * Рассчитывает и отображает общую калорийность выбранных продуктов.
   *
   * @param products Список всех доступных продуктов.
   */
  private void calculateTotalCalories(List<Product> products) {
    double totalCalories = 0.0;
    StringBuilder result = new StringBuilder();

    displayProductsInColumns(products);

    while (true) {
      System.out.println(
          "\n\033[1;36mВыберите продукт (введите номер или название) или введите\033[0m \033[1;31mSTOP или 0\033[0m \033[1;36mдля завершения:\033[0m");
      String input = scanner.nextLine();

      if ("STOP".equalsIgnoreCase(input) || "0".equalsIgnoreCase(input)) {
        break;
      }

      int index = getProductIndex(products,
          input); // используем функцию для получения индекса продукта

      if (index < 0) {
        System.out.println("\033[1;31mНеверное имя или номер продукта. Попробуйте снова.\033[0m");
        continue;
      }

      System.out.println("Введите вес продукта в граммах:");

      try {
        double weight = inputDoublePosNumber();
        totalCalories += (products.get(index).getCalories() * weight) / 100;

        // Добавляем информацию о продукте и его весе в результат
        result.append(products.get(index).getName())
            .append(", ")
            .append(weight)
            .append(" грамм - ")
            .append((products.get(index).getCalories() * weight) / 100)
            .append(" ккал")
            .append(System.lineSeparator());

      } catch (NumberFormatException e) {
        System.out.println("Неверный ввод. Пожалуйста, введите вес в граммах.");
      }
    }

    System.out.println("Общая калорийность выбранных продуктов:");
    System.out.println("\033[1;33mИтого: " + totalCalories + " ккал\033[0m");
  }

  /**
   * Предоставляет пользователю возможность добавить новый продукт.
   *
   * @param products Список всех доступных продуктов.
   */
  private void addNewProduct(List<Product> products) {
    boolean isInputCorrect = false; // флаг, указывающий на успешный ввод
    do {
      try {
        System.out.println("Введите название продукта (или \033[1;31m'exit'\033[0m для выхода):");
        String name = scanner.nextLine();

        if ("exit".equalsIgnoreCase(name)) { // возможность выйти из цикла
          return;
        }

        System.out.println("Введите калорийность продукта (ккал на 100г):");

        double calories = inputDoublePosNumber();

        products.add(new Product(name, calories));

        // Сохраняем обновленный список продуктов в файл
        storage.saveProducts(products);

        System.out.println("Продукт успешно добавлен!");
        isInputCorrect = true; // данные корректны, выходим из цикла

      } catch (Exception e) {
        System.out.println("Ошибка при добавлении продукта. Попробуйте снова.");
      }
    } while (!isInputCorrect);
  }

  /**
   * Отображает информацию о калорийности выбранного пользователем продукта из списка. Пользователь
   * может продолжить выбор продукта или вернуться в главное меню.
   *
   * @param products Список продуктов для отображения и выбора.
   */
  private void viewCalories(List<Product> products) {
    while (true) {
      displayProductsInColumns(products);
      System.out.println("\033[1;36mВведите номер или имя продукта: \033[0m");

      String input = scanner.nextLine();
      int index = getProductIndex(products, input);

      if (index != -1) {
        System.out.println("\033[1;33mКалорийность продукта " + products.get(index).getName() + ": "
            + products.get(index).getCalories() + " ккал/100г\033[0m");

        System.out.println("\033[1;36mЧто вы хотите сделать дальше? \033[0m");
        System.out.println("1. Просмотреть другой продукт");
        System.out.println("2. Вернуться в главное меню");

        String choice = scanner.nextLine();

        switch (choice) {
          case "1":
            break;
          case "2":
            run();
            return;
          default:
            System.out.println("\033[1;31mНеверный выбор. Попробуйте ещё раз.\033[0m");
        }
      } else {
        System.out.println("\033[1;31mНеверное имя или номер продукта. Попробуйте снова.\033[0m");
      }
    }
  }


  /**
   * Отображает список продуктов в формате пяти колонок.
   *
   * @param products Список всех доступных продуктов.
   */
  private void displayProductsInColumns(List<Product> products) {
    System.out.println(
        "Список продуктов который есть в базе данных (Если вы не нашли ваш продукт, вы можете его самостоятельно добавить нажав на цифру 2.");

    int numOfColumns = 5;
    int fifthSize = (products.size() + numOfColumns - 1) / numOfColumns;

    for (int i = 0; i < fifthSize; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        int index = i + j * fifthSize;
        if (index < products.size()) {
          System.out.printf("%-35s", (index + 1) + ". " + products.get(index).getName());
        } else {
          System.out.printf("%-35s", "");
        }
      }
      System.out.println();
    }
  }

  /**
   * Безопасно получает от пользователя положительное число типа Double.
   *
   * @return Возвращает положительное число типа Double.
   */
  public static Double inputDoublePosNumber() {
    Scanner scanner = new Scanner(System.in);
    while (!scanner.hasNextDouble()) {
      System.out.println("Введите, пожалуйста, число. Повторите ввод");
      scanner.next();
    }
    Double n = scanner.nextDouble();
    while (n < 0) {
      System.out.println("Вы ввели отрицательное число. Введите положительное число:");
      n = inputDoublePosNumber();
    }
    return n;
  }

  /**
   * Редактирует калорийность выбранного продукта из списка. Пользователю предоставляется
   * возможность выбора продукта по номеру или имени. После успешного выбора продукта пользователь
   * вводит новую калорийность для продукта. Метод также сохраняет обновленный список продуктов в
   * файл после изменения калорийности. Если пользователь вводит "STOP", редактирование
   * прекращается. В случае ошибки при выборе продукта или вводе калорийности, метод выводит
   * соответствующие сообщения об ошибках.
   *
   * @param products Список продуктов для редактирования.
   */
  public void editProductCalories(List<Product> products) {
    displayProductsInColumns(products);

    while (true) {
      System.out.println(
          "\033[1;36mВыберите продукт (введите номер или название) или введите\033[0m \033[1;31mSTOP\033[0m \033[1;36mдля завершения:\033[0m");
      String input = scanner.nextLine();
      if ("STOP".equalsIgnoreCase(input)) {
        return;
      }

      int index = getProductIndex(products, input);
      if (index == -1) {
        System.out.println("Неверное имя или номер продукта. Попробуйте снова.");
        continue;
      }

      System.out.println("Введите новую калорийность продукта (ккал на 100г):");
      double newCalories = inputDoublePosNumber();
      if (newCalories < 0) {
        System.out.println("Калорийность не может быть отрицательной. Попробуйте снова.");
        continue;
      }

      products.get(index).setCalories(newCalories);
      storage.saveProducts(products);
      System.out.println(
          "Калорийность продукта " + products.get(index).getName() + " изменена на " + newCalories
              + " ккал/100г");
      break;
    }
  }

  /**
   * Возвращает индекс продукта в списке на основе введенной строки. Входная строка может быть либо
   * числом (представляющим индекс продукта), либо именем продукта.
   *
   * @param products Список всех доступных продуктов.
   * @param input    Введенная пользователем строка, которая может быть либо числом (индексом
   *                 продукта), либо именем продукта.
   * @return Индекс продукта в списке. Если продукт не найден, возвращает -1.
   */
  private int getProductIndex(List<Product> products, String input) {
    try {
      int index = Integer.parseInt(input) - 1;
      if (index >= 0 && index < products.size()) {
        return index;
      }
    } catch (NumberFormatException e) {
      for (int i = 0; i < products.size(); i++) {
        if (products.get(i).getName().equalsIgnoreCase(input)) {
          return i;
        }
      }
    }
    return -1;
  }
}
