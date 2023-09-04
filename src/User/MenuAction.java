package User;

/**
 * Перечисление {@code MenuAction} определяет действия, которые пользователь может выбрать в меню.
 * Каждое действие ассоциируется с описательным текстом.
 */
public enum MenuAction {
  VIEW_PRODUCTS("Просмотреть список продуктов"),
  ADD_PRODUCT("\tДобавить новый продукт"),
  VIEW_CALORIES("\t\tПосмотреть калорийность продукта"),
  CALCULATE_TOTAL_CALORIES("\t\t\tРассчитать общую калорийность продуктов"),
  EXIT("\033[1;31mВыйти\033[0m");

  private final String description;

  MenuAction(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
