package otus.api;

import static org.citrusframework.actions.ApplyTestBehaviorAction.Builder.apply;
import static org.citrusframework.actions.LoadPropertiesAction.Builder.load;
import static org.citrusframework.http.actions.HttpActionBuilder.http;

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
import otus.api.behaviors.HttpMockCourseWrongBehavior;

@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestCourseNegative {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load().filePath("file:src/test/resources/load.properties"));
    }

    @CitrusTest
    @Test
    @DisplayName("Негативный кейс на проверку курсов")
    public void test(@CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        //Отправка сообщения в Mock по пути cource/update
        runner.run(http()
                .client("httpClientMock")
                .send()
                .get("cource/update")
                .fork(true)
        );

        // Локальное поднятие заглушки по курсам, которая вернёт METHOD_NOT_ALLOWED
        runner.run(
                apply(new HttpMockCourseWrongBehavior())
        );

        /*
        Получение сообщения из Mock.
        Проверяем, что в ответ HttpStatus.METHOD_NOT_ALLOWED
         */
        runner.run(http()
                .client("httpClientMock")
                .receive()
                .response(HttpStatus.METHOD_NOT_ALLOWED)
        );
    }
}
