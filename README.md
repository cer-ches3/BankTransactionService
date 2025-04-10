# BankTransactionService v1.0
### Описание задачи
Напишите приложение, которое по REST принимает запрос вида:

<strong><u> POST api/v1/wallet </u></strong>

```
{
 valletId: UUID,
 operationType: DEPOSIT or WITHDRAW,
 amount: 1000
}
```

После приложение должно выполнять логику по изменению счета в базе данных.

Также должна быть возможность получить баланс кошелька:

<strong><u> GET api/v1/wallets/{WALLET_UUID} </u></strong>

Стек: Java 8-17, Spring 3, Postgresql.

### Требования
- Должны быть написаны миграции для базы данных с помощью liquibase;
- Обратите особое внимание проблемам при работе в конкурентной среде (1000 RPS по одному кошельку). Ни один запрос не должен быть не обработан (50Х error);
- Предусмотрите соблюдение формата ответа для заведомо неверных запросов, когда кошелька не существует, не валидный json, или недостаточно средств;
- Приложение должно запускаться в докер контейнере, база данных тоже, вся система должна подниматься с помощью docker-compose;
- Предусмотрите возможность настраивать различные параметры как на стороне приложения, так и базы данных без пересборки контейнеров;
- Эндпоинты должны быть покрыты тестами.

### Запуск приложения
Прежде чем запустить приложение, вы можете переопределить настройки сборки контейнера в файле .env:
```
POSTGRES_DB=bank_transaction_system
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```
Вы можете установить своё имя для базы данных в POSTGRES_DB, а так же изменить пользовательские данные для доступа в POSTGRES_USER и POSTGRES_PASSWORD.

1. Собрать приложение в jar-файл;
2. Запустить контейнер с приложением и базой данных, выполнив в консоли:
```
docker compose up
```

### Работа приложения
Для упрощения проверки работоспособности предлагаю использовать готовые запросы для Postman.<br>
Что нужно сделать:<br>
- Импортировать коллекцию в Postman из <u>src/main/resources/other/BankTransactionService.postman_collection.json</u>
- В Postman создать окружение, в котором указать:

   | Variable | Type    | Initial value      | Current value |
      |----------|---------|--------------------|---------------|
   | baseUrl  | default | http://localhost:8080/ |http://localhost:8080/|

Принцип работы:
1. Создайте кошелёк выполнив CreateNewWallet. Метод не принимает никаких параметров, он создаёт новый кошелёк с рандомным UUID и устанавливает баланс, равный 0.
После создания метод вернёт данные созданного кошелька, скопируйте UUID кошелька для дальнейшей работы.
2. Что бы запросить баланс выполните GetBalance. В качестве параметра метод принимает UUID кошелька. Если кошелёк с таким UUID существует, то метод вернёт баланс, 
в противном случае - сообщение об ошибке.
3. Для удобства работы с несколькими кошельками был добавлен метод GetAllWallets. Метод не принимает никаких параметров, он вернёт список всех существующих кошельков.
4. Для совершения транзакции выполните PerformOperation. Метод принимает JSON в теле запроса:
```
{
    "walletId": "95a821e4-e702-450e-804b-78fd8fefedc3",
    "operationType": "DEPOSIT",
    "amount": 1000.0
}
```
Возможные значения для operationType - DEPOSIT, WITHDRAW. Минимальная сумма операции 0,01(amount).

Если по какой-либо причине не удалось импортировать коллекцию Postman ниже перечислены запросы для тестирования:
1. CreateNewWallet - POST localhost:8080/api/v1/wallet/create
2. GetBalance - GET localhost:8080/api/v1/wallet/{wallet_id}
3. GetAllWallets - GET localhost:8080/api/v1/wallet
4. PerformOperation - POST localhost:8080/api/v1/wallet

### Стек технологий::<br>
- Java Core
- Spring Boot
- JPA
- Liquibase
- PostgreSQL
- Docker
- Lombok
- Junit-jupiter
- Mockito-Core
