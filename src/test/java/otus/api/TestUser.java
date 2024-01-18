package otus.api;

import static com.consol.citrus.actions.ApplyTestBehaviorAction.Builder.apply;
import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.LoadPropertiesAction.Builder.load;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.junit.jupiter.CitrusExtension;
import com.consol.citrus.junit.jupiter.CitrusSupport;
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
        runner.$(load("file:src/test/resources/load.properties"));
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
