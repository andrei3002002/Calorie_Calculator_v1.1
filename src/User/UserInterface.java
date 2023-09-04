package User;

import java.util.Scanner;
import java.util.List;
import DataProduct.*;


public class UserInterface {

  private Scanner scanner;
  private ProductStorage storage;

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
        case VIEW_PRODUCTS:
          displayProductsInColumns(products);
          break;

        case ADD_PRODUCT:
          addNewProduct(products);
          break;

        case VIEW_CALORIES:
          displayProductsInColumns(products);
          System.out.println("\033[1;36mВведите номер продукта: \033[0m");
          try {
            int index =
                Integer.parseInt(scanner.nextLine()) - 1; // Изменено для обработки исключения
            if (index >= 0 && index < products.size()) {
              System.out.println(
                  "Калорийность продукта " + products.get(index).getName() + ": " + products.get(
                      index).getCalories() + " ккал/100г");
            } else {
              System.out.println("Неверный номер.");
            }
          } catch (NumberFormatException e) {
            System.out.println("Ошибка: было введено не целое число. Попробуйте снова.");
          }
          break;

        case CALCULATE_TOTAL_CALORIES:
          calculateTotalCalories(products);
          break;

        case EXIT:
          System.out.println("До свидания!");
          scanner.close();
          return;

        default:
          System.out.println("Неверный ввод. Попробуйте снова.");
          break;
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
    boolean isEnd = false;
    StringBuilder result = new StringBuilder();  // Новый StringBuilder

    displayProductsInColumns(products);

    while (!isEnd) {
      System.out.println(
          "\n\033[1;36mВыберите продукт (введите номер или название) или введите\033[0m \033[1;31mSTOP\033[0m \033[1;36mдля завершения:\033[0m");
      String input = scanner.nextLine();
      if ("STOP".equalsIgnoreCase(input)) {
        isEnd = true;
        break;
      }

      try {
        int index = -1;

        try {
          index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
          for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(input)) {
              index = i;
              break;
            }
          }
        }

        if (index < 0 || index >= products.size()) {
          System.out.println("Неверное имя или номер продукта. Попробуйте снова.");
          continue;
        }

        System.out.println("Введите вес продукта в граммах:");
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
    System.out.println(result.toString());  // Выводим подробную информацию
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
   * Отображает список продуктов в формате пяти колонок.
   *
   * @param products Список всех доступных продуктов.
   */
  private void displayProductsInColumns(List<Product> products) {
    System.out.println(
        "Список продуктов который есть в базе данных (Если вы не нашли ваш продукт, вы можете его самостоятельно добавить нажав на цифру 2.)");
    int fifthSize = (products.size() + 4) / 5; // округляем вверх
    for (int i = 0; i < fifthSize; i++) {
      String firstColumn = (i + 1) + ". " + products.get(i).getName();

      // Проверяем, есть ли продукт для второй колонки
      String secondColumn = "";
      if (i + fifthSize < products.size()) {
        secondColumn = (i + fifthSize + 1) + ". " + products.get(i + fifthSize).getName();
      }

      // Проверяем, есть ли продукт для третьей колонки
      String thirdColumn = "";
      if (i + 2 * fifthSize < products.size()) {
        thirdColumn = (i + 2 * fifthSize + 1) + ". " + products.get(i + 2 * fifthSize).getName();
      }

      // Проверяем, есть ли продукт для четвертой колонки
      String fourthColumn = "";
      if (i + 3 * fifthSize < products.size()) {
        fourthColumn = (i + 3 * fifthSize + 1) + ". " + products.get(i + 3 * fifthSize).getName();
      }

      // Проверяем, есть ли продукт для пятой колонки
      String fifthColumn = "";
      if (i + 4 * fifthSize < products.size()) {
        fifthColumn = (i + 4 * fifthSize + 1) + ". " + products.get(i + 4 * fifthSize).getName();
      }

      // Выводим в пять колонок, выравнивая по левому краю
      System.out.printf("%-35s %-35s %-35s %-35s %-35s%n", firstColumn, secondColumn, thirdColumn,
          fourthColumn, fifthColumn);
    }
  }

  /**
   * Безопасно получает от пользователя положительное число типа Double.
   *
   * @return Возвращает положительное число типа Double.
   */
  public static Double inputDoublePosNumber() {
    Scanner scanner = new Scanner(System.in);
    while (!scanner.hasNextInt()) {
      System.out.println("Введите, пожалуйста, число. Повторите ввод");
      scanner.next();
    }
    Double n = scanner.nextDouble();
    while (n <= 0) {
      System.out.println("Вы ввели отрицательное число. Введите число больше нуля:");
      n = scanner.nextDouble();
    }
    return n;
  }
  private int inputInt(String promptMessage) {
    while (true) {
      System.out.println(promptMessage);
      try {
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Введите целое число.");
      }
    }
  }
}
