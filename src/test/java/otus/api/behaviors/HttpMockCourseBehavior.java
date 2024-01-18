package otus.api.behaviors;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.TestBehavior;
import org.citrusframework.message.MessageType;
import org.citrusframework.message.builder.ObjectMappingPayloadBuilder;
import otus.api.dto.http.CourseDto;

import java.util.List;

/*
/cource/get/all для получения списка курсов

Для курсов:
    [
    {
    "name":"QA java",
    "price": 15000
    },
    {
    "name":"Java",
    "price": 12000
    }
 */
public class HttpMockCourseBehavior implements TestBehavior {

    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .server("httpMock")
                .receive()
                .get("/cource/get/all")
        );

        testActionRunner.run(http()
                .server("httpMock")
                .send()
                .response()
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(getCourses(), "objectMapper"))
        );
    }

    public List<CourseDto> getCourses() {
        CourseDto courseDto1 = new CourseDto();
        courseDto1.setName("QA java");
        courseDto1.setPrice(15000);
        CourseDto courseDto2 = new CourseDto();
        courseDto2.setName("Java");
        courseDto2.setPrice(12000);
        return List.of(courseDto1, courseDto2);
    }
}