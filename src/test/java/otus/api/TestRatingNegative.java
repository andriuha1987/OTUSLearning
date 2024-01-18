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
import otus.api.behaviors.HttpMockRatingBehavior;

/**
 * autotest
 *
 * @author Home
 * @since 2023-12-03
 */
@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestRatingNegative {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load("file:src/test/resources/load.properties"));
    }

    @CitrusTest
    @Test
    @DisplayName("Тест на проверку отсутствия оценки")
    public void test(@CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        int id = 999;

        //Отправка сообщения в Mock по пути /user/get/100
        runner.run(http()
                .client("httpClientMock")
                .send()
                .get("/user/get/" + id)
                .fork(true)
        );

        // Локальное поднятие заглушки по оценке
        runner.run(
                apply(new HttpMockRatingBehavior(id))
        );

        /*
        Получение сообщения из Mock.
        Проверяем
        - что пришёл HttpStatus.NOT_FOUND
        - msg равно "Test user not found"
         */
        runner.run(http()
                .client("httpClientMock")
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$.msg", "Test user not found")
                )
        );

        runner.run(
                echo().message(context.getMessageStore().getMessage("msg").getPayload().toString())
        );
    }
}
