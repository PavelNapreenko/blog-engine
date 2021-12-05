![](/images/logo.png)

**В рамках дипломного проекта нужно реализовать блоговый движок.**

*Требования к окружению и компонентам дипломного проекта:*

**Название**               |     **Ограничение**
---------------------------- | ----------------------
Версия JDK                   |     11
Система контроля версий      |     GIT
Версия Spring Boot           |     2.5.4
База данных                  |     MySQL 8
Сборщик проекта              |     Maven

**Запуск приложения**
______________________________________________________

Подготовить окружение:
$ cp .env_example .env

Задать необходимые значения переменным:
PORT,
JDBC_DATABASE_,
MAIL_,
IMAGES_UPLOAD_DIR в файле ".env".

Запустить приложение с нужными переменными окружения:

$ set -a; . ./.env; java -jar target/blog-engine-1.0.jar; set +a

Или в IntelliJ IDEA: Edit Configuration / EnvFile / Enable EnvFile / + .env.

<a href="https://napreenko-java-skillbox.herokuapp.com" class="button">Ссылка на проект на heroku</a>
<br></br>
<img alt="GitHub watchers" src="https://img.shields.io/github/watchers/PavelNapreenko/blog-engine?color=green&style=plastic">
<img alt="GitHub User's stars" src="https://img.shields.io/github/stars/PavelNapreenko?color=blue&style=plastic">
<img alt="GitHub followers" src="https://img.shields.io/github/followers/PavelNapreenko?color=orange&style=plastic">
<img alt="GitHub language count" src="https://img.shields.io/github/languages/count/PavelNapreenko/blog-engine?style=plastic">
<img alt="GitHub top language" src="https://img.shields.io/github/languages/top/PavelNapreenko/blog-engine?color=yellow&style=plastic">
<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/PavelNapreenko/blog-engine?color=brown&style=plastic">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/PavelNapreenko/blog-engine?color=pink&style=plastic">
