package otus.api;

import static org.citrusframework.actions.ApplyTestBehaviorAction.Builder.apply;
import static org.citrusframework.actions.EchoAction.Builder.echo;
import static org.citrusframework.actions.LoadPropertiesAction.Builder.load;
import static org.citrusframework.http.actions.HttpActionBuilder.http;
import static org.citrusframework.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import org.citrusframework.TestCaseRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.context.TestContext;
import org.citrusframework.junit.jupiter.CitrusExtension;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import otus.api.behaviors.HttpMockUserBehavior;

/**

 */
@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestUser {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load().filePath("file:src/test/resources/load.properties"));
    }

    @CitrusTest
    @Test
    @DisplayName("Тест на проверку пользователей")
    public void test(@CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        //Отправка сообщения в Mock по пути user/get/all
        runner.run(http()
                .client("httpClientMock")
                .send()
                .get("user/get/all")
                .fork(true)
        );

        // Локальное поднятие заглушки по пользователям
        runner.run(
                apply(new HttpMockUserBehavior())
        );

        /*
        Получение сообщения из Mock.
        Проверяем
        - что пришёл HttpStatus.OK
        - имя первого пользователя "Test user"
        - число пользователей равно 3
        - возраст пользователя это число
        - email подходит под регулярку (взял попроще)
        - возраст второго пользователя больше 44
         */
        runner.run(http()
                .client("httpClientMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$[0].name", "Test user")
                        .expression("$.size()", "3")
                        .expression("$[0].age", "@isNumber()@")
                        .expression("$[0].email", "@matches('^\\S+@\\S+\\.\\S+$')@")
                        .expression("$[1].age", "@greaterThan(44)@"))
        );

        runner.run(
                echo().message(context.getMessageStore().getMessage("msg").getPayload().toString())
        );
    }
}
