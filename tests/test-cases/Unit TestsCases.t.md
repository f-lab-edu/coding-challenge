# Unit tests cases

## Executor

### Presentation layer

* 7 `test executeCode` : 코드를 실행한다.
    * 사용자 식별자(`Token`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.
    * 사용자가 유효한지 식별하고 식별자를 반환한다.(`ensureUser(Token)`)
    * 코드를 실행한다. (`ExecutorService#executeCode`)

* 8 `test executeCode` : 코드를 실행할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.
    * 유효하지 않은 사용자 식별자(`InvalidToken`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.
    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

* 9 `test findResults` : 결과 리스트를 조회한다.
    * 사용자 식별자(`Token`), 문제 식별자(`QuestionId`)를 입력받는다.
    * 사용자가 유효한지 식별하고 식별자를 반환한다.(`ensureUser(Token)`)

* 10 `test findResults` : 결과 리스트를 조회할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.
    * 사용자 식별자(`InvalidToken`), 문제 식별자(`QuestionId`)를 입력받는다.
    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

* 11 `test findResult` : 결과를 조회한다.
    * 사용자 식별자(`Token`), 결과 식별자(`ResultId`)를 입력받는다.
    * 사용자가 유효한지 식별하고 식별자를 반환한다.(`ensureUser(Token)`)
    * 결과 리스트를 조회한다.(`ExecutorService#findResults`)

* 12 `test findResult` : 결과를 조회할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.
    * 사용자 식별자(`InvalidToken`), 결과 식별자(`ResultId`)를 입력받는다.
    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

* 13 `test findResult` : 결과를 조회할 때, 값이 존재하지 않으면 NotFoundException 예외를 반환한다.
    * 사용자 식별자(`Token`), 결과 식별자(`NotFoundResultId`)를 입력받는다.
    * 결과가 존재하지 않으면 `404 Not Found`를 반환한다.

### Execution service test cases

#### Business layer

* 14 `test executeCode` : 코드를 실행한다.
    * 사용자 식별자(`UserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받아 코드를 실행한다.

* 15 `test executeCode` : 코드를 실행할 때, 사용자 정보를 식별할 수 없다면 `NotFoundException` 예외가 발생한다.
    * 존재하지 않는 사용자의 식별자(`NotFoundUserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.
    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

* 16 `test executeCode` : 코드를 실행할 때, 문제 식별자(`QuestionId`)에 맞는 문제(`Question`)가 없다면 `NotFoundException` 예외가 발생한다.
    * 사용자 식별자(`UserId`), 문제 식별자(`NotFoundQuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.
    * 문제 식별자(`QuestionId`)를 이용해 문제(`Question`)를 조회할 때 존재하지 않는다면 `NotFoundQuestionException` 예외가 발생한다.

* 51 `test executeCode` : 코드를 실행할 때, `executionCode`에서 반환받은 데이터가 없다면 `NotFoundException` 예외가 발생한다.

* 17 `test findResults` : 결과 리스트를 조회한다.
    * 사용자 식별자(`UserId`)와 문제 식별자(`QuestionId`)를 입력받아 결과를 조회한다.

* 18 `test findResults` : 결과 리스트를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.
    * 사용자 식별자(`NotFoundUserId`)와 문제 식별자(`QuestionId`)를 입력받는다.
    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

* 19 `test findResults` : 결과 리스트를 조회할 때 문제 정보(`Question`)가 존재하지 않으면 `NotFountQuestionException` 예외가 발생한다.
    * 사용자 식별자(`UserId`)와 문제 식별자(`NotFoundQuestionId`)를 입력받는다.
    * 문제 식별자(`QuestionId`)에 대한 문제(`Question`)가 있는지 검증할 때, 존재하지 않으면 `NotFountQuestionException` 예외가 발생한다.

* 20 `test findResults` : 결과 리스트를 조회할 때, 값이 비어있으면 빈 리스트를 반환한다.

* 21 `test findResult` : 결과를 조회한다.
    * 사용자 식별자(`UserId`)와 결과 식별자(`ResultId`)를 입력받아 결과를 조회한다.

* 22 `test findResult` : 결과를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.
    * 사용자 식별자(`NotFoundUserId`)와 결과 식별자(`ResultId`)를 입력받는다.
    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

* 23 `test findResult` : 결과를 조회할 때, 결과 식별자(`ResultId`)에 맞는 결과(`Result`)가 없다면 `NotFoundResultException` 예외가 발생한다.
    * 사용자 식별자(`UserId`)와 결과 식별자(`NotFoundResultId`)를 입력받는다.
    * 결과 식별자(`ResultId`)를 이용해 결과(`Result`)를 조회할 때, 존재하지 않는다면 `NotFoundResultException` 예외가 발생한다.

#### Domain layer

##### Code

* 24 `test Code` : 코드를 생성한다.
    * 코드 타입(`Lang`)와 코드(`Code`)를 입력받아 코드를 생성한다.
    * 생성할 코드 타입(`Lang`)은 `Java11`, `Python3` 둘 중 하나의 정보를 가진다.

* 25 `test Code` : 코드를 생성할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다.
    * 코드 타입(`InvalidLang`)과 코드(`Code`)를 입력받는다.
    * 코드 타입(`Lang`)을 검증할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다.

* 26 `test Code` : 코드를 생성할 때, 코드(`Code`)가 없거나 비어있으면 `InvalidCodeException` 예외가 발생한다.
    * 코드 타입(`Lang`)과 코드(`InvalidCode`)를 입력받는다.
    * 코드(`Code`)를 검증할 때, 코드(`Code`)가 없거나 비어있으면 `InvalidCodeException` 예외가 발생한다.

##### ExecutionResult

* 27 `test ExecutionResult` : 테스트 결과를 생성한다.
    * 결과 식별자(`Id`), 사용자의 식별자(`UserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드 정보(`Code`), 사용자 식별자(`UserId`),
      문제 식별자(`QuestionId`), 성공 여부 정보(`IsSucceed`)를 가진다.

* 28 `test ExecutionResult` : 테스트 결과를 생성할 때, 코드(`Code`)가 없으면 `IllegalArgumentException` 예외가 발생한다.
    * 코드(`Code`)가 없으면 `IllegalArgumentException` 예외가 발생한다.

* 29 `test ExecutionResult` : 테스트 결과를 생성할 때, 문제 식별자(`QuestionId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가
  발생한다.
    * 문제 식별자(`QuestionId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가 발생한다.

* 30 `test ExecutionResult` : 테스트 결과를 생성할 때, 사용자 식별자(`UserId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가
  발생한다.

##### SucceededResult

* 31 `test SucceededResult` : 성공한 테스트 결과를 생성한다.
    * 테스트 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `true`이다.
    * 총 실행 시간(`TotalExecutionTime`), 사용한 메모리 평균치 정보(`AverageUsedMemeory`)를 입력받아 성공한 테스트 결과를 생성한다.

* 32 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 총 실행 시간(`TotalExecutionTime`)은
  음수면 `InvalidSucceededResultException` 예외가 발생한다.

* 33 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 총 실행 시간(`TotalExecutionTime`)은 없거나
  비어있으면 `InvalidSucceededResultException` 예외가 발생한다.

* 34 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 사용한 메모리 평균치 정보(`AverageUsedMemeory`)는
  음수면 `InvalidSucceededResultException` 예외가 발생한다.

* 35 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 사용한 메모리 평균치 정보(`AverageUsedMemeory`)는 없거나
  비어있으면 `InvalidSucceededResultException` 예외가 발생한다.

##### FailedResult

* 36 `test FailedResult` : 실패한 테스트 결과를 생성한다.
    * 테스트 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.
    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 테스트 결과를 생성한다.
    * 실패 원인은 `DIFFERENT`, `ERROR` 중 하나의 정보를 가진다.

* 37 `test FailedResult` : 실패한 테스트 결과를 생성할 때, 실패 원인(`Cause`)이 없으면 `InvalidFailedResultException` 예외가 발생한다.

* 38 `test FailedResult` : 실패한 테스트 결과를 생성할 때, 실패 정보(`Message`)가 없거나 비어있으면 `InvalidFailedResultException` 예외가
  발생한다.

##### TestCases

* 39 `test TestCases` : 테스트 케이스 목록(`TestCases`)을 생성한다.
    * 문제(`Question`)의 테스트 케이스의 목록(`TestCases`)을 가진다.

* 40 `test TestCases` : 테스트 케이스 리스트(`TestCases`)는 수정하면 `UnsupportedOperationException` 예외가 발생한다.

* 41 `test TestCases` : 테스트 케이스 리스트(`TestCases`)를 생성할 때, 테스트 케이스 리스트가 없거나 비어있으면 `InvalidTestCasesException` 예외가
  발생한다.

##### TestCase

* 42 `test TestCase` : 테스트 케이스를 생성한다.
    * 테스트 케이스 입력(`Input`)과 테스트 케이스 출력(`Output`)을 입력받아 테스트 케이스를 생성한다.

* 43 `test TestCase` : 테스트 케이스를 생성할 때, 테스트 케이스 출력(`output`)이 없거나 비어있으면 `InvalidTestCaseException` 예외가 발생한다.

### Executor service spec

#### Business layer

* 44 `test executeCode` : 코드(`Code`)를 실행한다.
    * 테스트 케이스(`TestCases`)를 입력해 코드(`Code`)를 실행한다.
    * 코드를 실행해 결과를 반환한다.(`Code#excute`)

#### Domain layer

##### Code

* 45 `test Code` : 코드(`Code`)를 생성한다.
    * 코드(`Code`)와 타입 정보(`Lang`)을 입력해 코드를 생성한다.

* 46 `test execute` : 코드를 여러 번 실항한다.
    * 코드를 여러 번 실행해 실행 결과(`ExecutionResult`)를 반환한다.

* 47 `test execute` : 코드를 실행한다.
    * 코드를 실행해 실행 결과(`ExecutionResult`)를 반환한다.

##### SucceededTestResult

* 48 `test SucceededTestResult` : 성공한 실행 결과를 생성한다.
    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `true`이다.
    * 실행 시간(`ExecutionTime`), 사용한 메모리 평균치 정보(`UsedMemeory`)를 입력받아 성공한 실행 결과를 생성한다.

##### FailedTestResult

* 49 `test FailedTestResult` : 실패 원인이 `DIFFERENT`인 실행 결과를 생성한다.
    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.
    * `DIFFERENT`일 때 실패 정보는 실제 값과 예상 값의 차이 정보가 포함된다.
    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 실행 결과를 생성한다.

* 50 `test FailedTestResult` : 실패 원인이 `ERROR`인 실행 결과를 생성한다.
    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.
    * `ERROR`일 때 실패 정보는 반환받은 에러 클래스 정보이다.
    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 실행 결과를 생성한다.
