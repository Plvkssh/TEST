## TEST
# Wikipedia Test Automation Project

Automated testing framework for Wikipedia web version and mobile application.

## 📋 Project Structure
src/main/java/ru/javabruse/
├── pages/ # Page Object classes
│ ├── WikipediaPage.java # Web page
│ └── WikipediaAppPage.java # Mobile app page
└── utils/ # Utilities
└── WebDriverFactory.java # Driver factory

src/test/java/ru/javabruse/
├── web/ # Web tests
│ └── WikipediaTests.java
└── mobile/ # Mobile tests
└── WikipediaMobileTests.java

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome browser (for web tests)
- Appium server (for mobile tests)
- Android Emulator or device


### Тестируемые функции
##Веб-версия (WikipediaTests)
✅ Загрузка главной страницы

✅ Поиск статей

✅ Открытие случайной статьи

✅ Доступность поискового поля

##Мобильное приложение (WikipediaMobileTests)
✅ Видимость поиска на главном экране

✅ Поиск и открытие статей

✅ Навигация назад к главному экрану
