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
import otus.api.behaviors.HttpMockCourseBehavior;

/**
 * autotest
 *
 * @author Home
 * @since 2023-12-03
 */
@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestCourse {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load().filePath("file:src/test/resources/load.properties"));
    }

    @CitrusTest
    @Test
    @DisplayName("Тест на проверку курсовн")
    public void test(@CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        //Отправка сообщения в Mock по пути cource/get/all
        runner.run(http()
                .client("httpClientMock")
                .send()
                .get("cource/get/all")
                .fork(true)
        );

        // Локальное поднятие заглушки по курсам
        runner.run(
                apply(new HttpMockCourseBehavior())
        );

        /*
        Получение сообщения из Mock.
        Проверяем
        - что пришёл HttpStatus.OK
        - название первого курса "QA java"
        - число курсов равно 2
        - цена курса это число
         */
        runner.run(http()
                .client("httpClientMock")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .name("msg")
                .validate(jsonPath()
                        .expression("$[0].name", "QA java")
                        .expression("$.size()", "2")
                        .expression("$[0].price", "@isNumber()@"))
        );

        runner.run(
                echo().message(context.getMessageStore().getMessage("msg").getPayload().toString())
        );
    }
}
