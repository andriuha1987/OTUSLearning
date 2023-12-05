package otus.api.behaviors;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.TestBehavior;
import org.springframework.http.HttpStatus;

/*
Mock для негативной реакции
 */
public class HttpMockCourseWrongBehavior implements TestBehavior {

    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .server("httpMock")
                .receive()
                .get("/cource/update")
        );

        testActionRunner.run(http()
                .server("httpMock")
                .send()
                .response(HttpStatus.METHOD_NOT_ALLOWED)
        );
    }
}
