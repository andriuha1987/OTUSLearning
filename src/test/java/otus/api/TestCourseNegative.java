package otus.api;

import static com.consol.citrus.actions.ApplyTestBehaviorAction.Builder.apply;
import static com.consol.citrus.actions.LoadPropertiesAction.Builder.load;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.actions.LoadPropertiesAction;
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
import otus.api.behaviors.HttpMockCourseWrongBehavior;

@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestCourseNegative {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load("file:src/test/resources/load.properties"));
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
