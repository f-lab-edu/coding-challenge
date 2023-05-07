# Executor Unit CheckLists

## Unit tests cases

### Executor

#### Presentation layer

* [done] C7 `test executeCode` : 코드를 실행한다.

    * 사용자 식별자(`Token`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.

    * 코드를 실행한다. (`ExecutorService#executeCode`)

* [done] C9 `test findResults` : 결과 리스트를 조회한다.

    * 사용자 식별자(`Token`), 문제 식별자(`QuestionId`)를 입력받는다.

* [done] C11 `test findResult` : 결과를 조회한다.

    * 사용자 식별자(`Token`), 결과 식별자(`ResultId`)를 입력받는다.

    * 결과 리스트를 조회한다.(`ExecutorService#findResults`)

* [unknown] C12 `test findResult` : 결과를 조회할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.

    * 사용자 식별자(`InvalidToken`), 결과 식별자(`ResultId`)를 입력받는다.

    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

* [unknown] C8 `test executeCode` : 코드를 실행할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.

    * 유효하지 않은 사용자 식별자(`InvalidToken`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.

    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

* [unknown] C10 `test findResults` : 결과 리스트를 조회할 때, 사용자가 유효하지 않으면 UnauthenticationException 예외를 반환한다.

    * 사용자 식별자(`InvalidToken`), 문제 식별자(`QuestionId`)를 입력받는다.

    * 사용자를 검증할 때 유효하지 않으면 `401 UnAuthorized`를 반환한다.

#### Execution service test cases

##### Business layer

* [done] C14 `test executeCode` : 코드를 실행한다.

    * 사용자 식별자(`UserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받아 코드를 실행한다.

* [done] C15 `test executeCode` : 코드를 실행할 때, 사용자 정보를 식별할 수 없다면 `NotFoundException` 예외가 발생한다.

    * 존재하지 않는 사용자의 식별자(`NotFoundUserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.

    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundException` 예외가 발생한다.

* [done] C16 `test executeCode` : 코드를 실행할 때, 문제 식별자(`QuestionId`)에 맞는 문제(`Question`)가
  없다면 `NotFoundException` 예외가 발생한다.

    * 사용자 식별자(`UserId`), 문제 식별자(`NotFoundQuestionId`), 코드 타입(`Lang`), 코드(`Code`)를 입력받는다.

    * 문제 식별자(`QuestionId`)를 이용해 문제(`Question`)를 조회할 때 존재하지 않는다면 `NotFoundException` 예외가 발생한다.

* [done] 51 `test executeCode` : 코드를 실행할 때, `executionCode`에서 반환받은 데이터가 없다면 `NotFoundException` 예외가 발생한다.

* [done] C17 `test findResults` : 결과 리스트를 조회한다.

    * 사용자 식별자(`UserId`)와 문제 식별자(`QuestionId`)를 입력받아 결과를 조회한다.

* [done] C18 `test findResults` : 결과 리스트를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

    * 사용자 식별자(`NotFoundUserId`)와 문제 식별자(`QuestionId`)를 입력받는다.

    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

* [done] C19 `test findResults` : 결과 리스트를 조회할 때 문제 정보(`Question`)가 존재하지 않으면 `NotFountQuestionException` 예외가
  발생한다.

    * 사용자 식별자(`UserId`)와 문제 식별자(`NotFoundQuestionId`)를 입력받는다.

    * 문제 식별자(`QuestionId`)에 대한 문제(`Question`)가 있는지 검증할 때, 존재하지 않으면 `NotFountQuestionException` 예외가 발생한다.

* [done] C20 `test findResults` : 결과 리스트를 조회할 때, 값이 비어있으면 빈 리스트를 반환한다.

* [done] C21 `test findResult` : 결과를 조회한다.

    * 사용자 식별자(`UserId`)와 결과 식별자(`ResultId`)를 입력받아 결과를 조회한다.

* [done] C22 `test findResult` : 결과를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

    * 사용자 식별자(`NotFoundUserId`)와 결과 식별자(`ResultId`)를 입력받는다.

    * 사용자를 조회할 때, 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다.

* [done] C23 `test findResult` : 결과를 조회할 때, 결과 식별자(`ResultId`)에 맞는 결과(`Result`)가 없다면 `NotFoundResultException`
  예외가 발생한다.

    * 사용자 식별자(`UserId`)와 결과 식별자(`NotFoundResultId`)를 입력받는다.

    * 결과 식별자(`ResultId`)를 이용해 결과(`Result`)를 조회할 때, 존재하지 않는다면 `NotFoundResultException` 예외가 발생한다.

##### Domain layer

###### Code

* [done] C24 `test Code` : 코드를 생성한다.

    * 코드 타입(`Lang`)와 코드(`Code`)를 입력받아 코드를 생성한다.

    * 생성할 코드 타입(`Lang`)은 `Java11`, `Python3` 둘 중 하나의 정보를 가진다.

* [done] C25 `test Code` : 코드를 생성할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다.

    * 코드 타입(`InvalidLang`)과 코드(`Code`)를 입력받는다.

    * 코드 타입(`Lang`)을 검증할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다.

* [done] C26 `test Code` : 코드를 생성할 때, 코드(`Code`)가 없거나 비어있으면 `InvalidCodeException` 예외가 발생한다.

    * 코드 타입(`Lang`)과 코드(`InvalidCode`)를 입력받는다.

    * 코드(`Code`)를 검증할 때, 코드(`Code`)가 없거나 비어있으면 `InvalidCodeException` 예외가 발생한다.

###### ExecutionResult

* [unknown] C27 `test ExecutionResult` : 테스트 결과를 생성한다.

    * 결과 식별자(`Id`), 사용자의 식별자(`UserId`), 문제 식별자(`QuestionId`), 코드 타입(`Lang`), 코드 정보(`Code`), 사용자 식별자(`UserId`),
      솔루션 식별자(`SolutionId`), 문제 식별자(`QuestionId`), 성공 여부 정보(`IsSucceed`)를 가진다.

* [unknown] C28 `test ExecutionResult` : 테스트 결과를 생성할 때, 코드(`Code`)가 없으면 `IllegalArgumentException` 예외가 발생한다.

    * 코드(`Code`)가 없으면 `IllegalArgumentException` 예외가 발생한다.

* [unknown] C29 `test ExecutionResult` : 테스트 결과를 생성할 때, 문제 식별자(`QuestionId`)가 없거나
  비어있으면 `IllegalArgumentException`
  예외가 발생한다.

    * 문제 식별자(`QuestionId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가 발생한다.

* [unknown] C30 `test ExecutionResult` : 테스트 결과를 생성할 때, 솔루션 식별자(`SolutionId`)가 없거나
  비어있으면 `IllegalArgumentException`
  예외가 발생한다.

    * 솔루션 식별자(`SolutionId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가 발생한다.

###### SucceededResult

* [unknown] C31 `test SucceededResult` : 성공한 테스트 결과를 생성한다.

    * 테스트 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `true`이다.

    * 총 실행 시간(`TotalExecutionTime`), 사용한 메모리 평균치 정보(`AverageUsedMemeory`)를 입력받아 성공한 테스트 결과를 생성한다.

* [unknown] C32 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 총 실행 시간(`TotalExecutionTime`)은
  음수면 `InvalidSucceededResultException` 예외가 발생한다.

* [unknown] C33 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 총 실행 시간(`TotalExecutionTime`)은 없거나
  비어있으면 `InvalidSucceededResultException` 예외가 발생한다.

* [unknown] C34 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 사용한 메모리 평균치 정보(`AverageUsedMemeory`)는
  음수면 `InvalidSucceededResultException` 예외가 발생한다.

* [unknown] C35 `test SucceededResult` : 성공한 테스트 결과를 생성할 때, 사용한 메모리 평균치 정보(`AverageUsedMemeory`)는 없거나
  비어있으면 `InvalidSucceededResultException` 예외가 발생한다.

###### FailedResult

* [unknown] C36 `test FailedResult` : 실패한 테스트 결과를 생성한다.

    * 테스트 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.

    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 테스트 결과를 생성한다.

    * 실패 원인은 `DIFFERENT`, `ERROR` 중 하나의 정보를 가진다.

* [unknown] C37 `test FailedResult` : 실패한 테스트 결과를 생성할 때, 실패 원인(`Cause`)이 없으면 `InvalidFailedResultException`
  예외가 발생한다.

* [unknown] C38 `test FailedResult` : 실패한 테스트 결과를 생성할 때, 실패 정보(`Message`)가 없거나
  비어있으면 `InvalidFailedResultException` 예외가 발생한다.

###### TestCases

* [done] C39 `test TestCases` : 테스트 케이스 목록(`TestCases`)을 생성한다.

    * 문제(`Question`)의 테스트 케이스의 목록(`TestCases`)을 가진다.

* [done] C40 `test TestCases` : 테스트 케이스 리스트(`TestCases`)는 수정하면 `UnsupportedOperationException` 예외가 발생한다.

* [done] C41 `test TestCases` : 테스트 케이스 리스트(`TestCases`)를 생성할 때, 테스트 케이스 리스트가 없거나
  비어있으면 `InvalidTestCasesException` 예외가 발생한다.

###### TestCase

* [done] C42 `test TestCase` : 테스트 케이스를 생성한다.

    * 테스트 케이스 입력(`Input`)과 테스트 케이스 출력(`Output`)을 입력받아 테스트 케이스를 생성한다.

* [done] C43 `test TestCase` : 테스트 케이스를 생성할 때, 테스트 케이스 출력(`Output`)이 없거나 비어있으면 `InvalidTestCaseException`
  예외가 발생한다.

#### Executor service spec

##### Business layer

* [unknown] C47 `test executeCode` : 코드(`Code`)를 실행한다.

    * 테스트 케이스(`TestCases`)를 입력해 코드(`Code`)를 실행한다.

    * 코드를 실행해 결과를 반환한다.(`Code#excute`)

##### Domain layer

###### Code

* [unknown] C44 `test Code` : 코드(`Code`)를 생성한다.

    * 코드(`Code`)와 타입 정보(`Lang`)을 입력해 코드를 생성한다.

* [unknown] C45 `test execute` : 코드를 여러 번 실항한다.

    * 코드를 여러 번 실행해 실행 결과(`ExecutionResult`)를 반환한다.

* [unknown] C46 `test execute` : 코드를 실행한다.

    * 코드를 실행해 실행 결과(`ExecutionResult`)를 반환한다.

###### SucceededTestResult

* [unknown] C47 `test SucceededTestResult` : 성공한 실행 결과를 생성한다.

    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `true`이다.

    * 실행 시간(`ExecutionTime`), 사용한 메모리 평균치 정보(`UsedMemeory`)를 입력받아 성공한 실행 결과를 생성한다.

###### FailedTestResult

* [unknown] C48 `test FailedTestResult` : 실패 원인이 `DIFFERENT`인 실행 결과를 생성한다.

    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.

    * `DIFFERENT`일 때 실패 정보는 실제 값과 예상 값의 차이 정보가 포함된다.

    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 실행 결과를 생성한다.

* [unknown] C49 `test FailedTestResult` : 실패 원인이 `ERROR`인 실행 결과를 생성한다.

    * 실행 결과를 생성할 때, 성공 여부 정보(`IsSucceed`)가 `false`이다.

    * `ERROR`일 때 실패 정보는 반환받은 에러 클래스 정보이다.

    * 실패 원인(`Cause`)과 실패 정보(`Message`)를 입력받아 실패한 실행 결과를 생성한다.

