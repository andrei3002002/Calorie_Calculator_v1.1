# Калькулятор калорий

Проект "Калькулятор калорий" представляет собой консольное приложение для учета и расчета калорийности продуктов.

## Описание

Приложение позволяет пользователю выполнить следующие действия:

- Просмотреть список продуктов
- Добавить новый продукт
- Посмотреть калорийность продукта
- Рассчитать общую калорийность выбранных продуктов
- Выйти из приложения

## Технические требования

- Версия языка: Java 11
- Зависимости: JUnit 5 (для тестирования)
- Точка входа: `Main` класс
- Необходимые для работы файлы: Файлы с продуктами (`products.txt`)
- Структура проекта: Код разбит на несколько классов:

    - `Product`: Класс, представляющий продукт с названием и калорийностью.
    - `MenuAction`: Перечисление, определяющее действия меню.
    - `UserInterface`: Класс, обеспечивающий интерфейс пользователя.
    - `Calculator`: Класс, выполняющий расчет общей калорийности.
    - `ProductStorage`: Класс, обеспечивающий хранение и загрузку продуктов из файла.

## Запуск

Для запуска приложения выполните следующие шаги:

1. Скомпилируйте все классы с помощью компилятора Java.
2. Запустите класс `Main` в вашей среде разработки или из командной строки.

## Авторы

* **Andrei Vilcu**

## Лицензия

Этот проект лицензирован по лицензии MIT - см. [LICENSE.md](LICENSE.md) файл для деталей.
