## Feature TODO list

**HIGH Priority**
- Migrate to DB
- Add test data to database
- Separate models to DTO and ORM. Decided to keep it simple for now since we don't use DB.
- Implement transaction commit/rollback mechanism while transfer
- Implement proper Dependency Injection (Inverted) or use suitable lib
- Add filters parameters to retrieve data
- Add E2E REST Tests

**MID Priority**
- Make API Async
- Add more logging
- Add more tests
- Enhance API response with more clear statuses and messages
- Add new checks for error responses
- Add input validation


**LOW Priority**
- Replace Date with LocalDateTime and fix serialization
- Put messages to resource files and localize
- Improve log configuration
- Add proper mocks to test


Methods for retrieving all users and all accounts weren't implemented intentionally since it will be huge amount of entries.
Moreover this feature is rarely used without filters.

Test scripts for database initialization and correctness check is stored in test_data_init.sh

## REST endpoints
API path /api/*

**User:**
**/user**
    **GET /{id} ** - retrieve user info by id
    **GET /{id}/balance ** - retrieve user total balance from all accounts
    **GET /{id}/accounts ** - retrieve info about all user's accounts
    **POST** - create user
    **PUT** - update user
    **DELETE** - deactivate user

**Account:**
**/account**
    **GET /{id} ** - retrieve user info by id
    **GET /{id}/balance** - retrieve user total balance from all accounts
    **POST** - create user
    **PUT** - update user
    **DELETE** - deactivate user

**Transaction:**
**/transaction**
    **POST /deposit** - deposit money to account
    **POST /withdraw** - withdraw money from account
    **POST /transfer** - transfer money from one account to another

##### Test coverage 

Test coverage 55% for all lines.
For most critical packages:
 model - 89%
 repository - 72%
 service - 100%

Notice: junit test show incorrect if run classes together since they use single instance of storage
and test runs order is unknown in advance. Will be fixed in future with proper mocks.
