# Banner Alphabet

<details>
<summary>Русское описание</summary>

![Banners](https://cdn.modrinth.com/data/cached_images/51dd5e4b4045c7ff8e7a5ce044fda5e237980131.jpeg)

**Banner Alphabet** это мод, который позволяет создавать баннеры с предопределенными узорами, используя **два цвета**: основной цвет и цвет фона.

Мод предназначен в первую очередь для создания **букв и других символов**. Вместо того, чтобы вручную применять каждый узор к баннеру, вы можете выбрать два цвета и получить готовый баннер из интерфейса мода.

## Меню баннеров

Меню баннеров можно открыть с помощью кнопки, добавленной в **инвентарь креатива**.

![Menu button](https://cdn.modrinth.com/data/cached_images/76510920513acb2b28c2679c4a5b47f09b15849d.png)

Меню содержит два переключателя цвета:

* **Основной цвет** — основной цвет, используемый в шаблонах баннеров.
* **Цвет фона** — базовый цвет баннера.

После выбора цветов настроенные баннеры генерируются с использованием этих цветов. Полученные баннеры затем можно будет взять из меню вывода.

![Banner menu](https://cdn.modrinth.com/data/cached_images/40bda075b3b38e5dfd13a613be97bda4018e5b42.png)

## Commands

| Команда            | Описание                                                                |
| ------------------ | -------------------------------------------------------------------------- |
| `/banners help`    | Показывает список команд                                                      |
| `/banners add`     | Добавляет в конфигурацию баннер из основной руки                              |
| `/banners rem`     | Удаляет баннер из основной руки из конфигурации                            |
| `/banners replace` | Заменяет в конфигурации баннер из дополнительной руки на баннер из основной руки |
| `/banners save`    | Сохраняет конфигурацию                                                     |
| `/banners load`    | Перезагружает конфигурацию                                                   |

<details>
<summary>Подробнее о командах</summary>
  
### `/banners add`
Добавляет в конфигурацию баннер, который в данный момент находится в **основной руке**.
Это можно использовать для создания новых пользовательских баннеров прямо в игре.

### `/banners rem`
Удаляет баннер, который в данный момент находится в **основной руке**, из конфигурации.

### `/banners replace`
Заменяет баннер в **дополнительной руке** на баннер из **основной руки** в конфигурации.
Это может быть полезно при изменении существующего дизайна баннера, сохраняя при этом его положение в конфигурации.

### `/banners save`
Вручную сохраняет текущую конфигурацию баннера в `banner-alphabet.json`.
Конфигурация также сохраняется автоматически при выходе из Майнкрафта.

### `/banners load`
Заставляет мод перезагрузить конфигурацию из `banner-alphabet.json`.
Это полезно после редактирования файла конфигурации вручную во время работы Майнкрафта.

</details>

## Конфигурация

Определения баннеров хранятся в файле `config/banner-alphabet.json`. Файл содержит массив JSON, где каждый элемент представляет один баннер. Каждый баннер содержит массив «patterns». Каждый «patterns» имеет два свойства:

| Property  | Description                                             |
| --------- | --------------------------------------------------------|
| `pattern` | Идентификатор шаблона баннера                           |
| `color`   | Определяет, какой из двух выбранных цветов используется |

### Цветовые значения

```text
0 = Цвет фона
1 = Основной цвет
```

Например:

```json
[
  {
    "patterns": [
      {
        "pattern": "minecraft:stripe_top",
        "color": 1
      },
      {
        "pattern": "minecraft:border",
        "color": 0
      }
    ]
  },
  {
    "patterns": [
      {
        "pattern": "minecraft:bricks",
        "color": 1
      }
    ]
  }
]
```

Порядок баннеров в массиве определяет их порядок в Баннерном меню..

</details>

![Banners](https://cdn.modrinth.com/data/cached_images/51dd5e4b4045c7ff8e7a5ce044fda5e237980131.jpeg)

**Banner Alphabet** is a mod that allows you to create banners with predefined patterns using **two colors**: a main color and a background color.

The mod is designed primarily for creating **letters and other predefined symbols**. Instead of manually applying every pattern to a banner, you can select two colors and get the finished banner from the mod's interface.

## Banner Menu

The banner menu can be opened using a button added to the **Creative Mode inventory**.

![Menu button](https://cdn.modrinth.com/data/cached_images/76510920513acb2b28c2679c4a5b47f09b15849d.png)

The menu contains two color selectors:

* **Main Color** — the primary color used by the banner patterns.
* **Background Color** — the base color of the banner.

After selecting the colors, the configured banners are generated using those colors. The resulting banners can then be taken from the output menu.

![Banner menu](https://cdn.modrinth.com/data/cached_images/40bda075b3b38e5dfd13a613be97bda4018e5b42.png)

## Commands

| Command            | Description                                                                |
| ------------------ | -------------------------------------------------------------------------- |
| `/banners help`    | Show the command list                                                      |
| `/banners add`     | Add the main-hand banner to the configuration                              |
| `/banners rem`     | Remove the main-hand banner from the configuration                         |
| `/banners replace` | Replace the off-hand banner with the main-hand banner in the configuration |
| `/banners save`    | Save the configuration                                                     |
| `/banners load`    | Reload the configuration                                                   |

<details>
<summary>More about commands</summary>
  
### `/banners add`
Adds the banner currently held in the **main hand** to the configuration.
This can be used to create new custom banners directly in-game.

### `/banners rem`
Removes the banner currently held in the **main hand** from the configuration.

### `/banners replace`
Replaces the banner in the **off hand** with the banner from the **main hand** in the configuration.
This can be useful when modifying an existing banner design while keeping its position in the configuration.

### `/banners save`
Manually saves the current banner configuration to `banner-alphabet.json`.
The configuration is also saved automatically when leaving Minecraft.

### `/banners load`
Forces the mod to reload the configuration from `banner-alphabet.json`.
This is useful after manually editing the configuration file while Minecraft is running.

</details>

## Configuration

The banner definitions are stored in: `config/banner-alphabet.json`. The file contains a JSON array where each element represents one banner. Each banner contains a `patterns` array. Every pattern has two properties:

| Property  | Description                                         |
| --------- | --------------------------------------------------- |
| `pattern` | The banner pattern identifier                       |
| `color`   | Determines which of the two selected colors is used |

### Color Values

```text
0 = Background Color
1 = Main Color
```

For example:

```json
[
  {
    "patterns": [
      {
        "pattern": "minecraft:stripe_top",
        "color": 1
      },
      {
        "pattern": "minecraft:border",
        "color": 0
      }
    ]
  },
  {
    "patterns": [
      {
        "pattern": "minecraft:bricks",
        "color": 1
      }
    ]
  }
]
```

The order of banners in the array determines their order in the Banner Menu.
