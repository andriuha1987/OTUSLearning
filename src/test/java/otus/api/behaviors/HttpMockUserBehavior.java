package otus.api.behaviors;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.TestBehavior;
import org.citrusframework.message.MessageType;
import org.citrusframework.message.builder.ObjectMappingPayloadBuilder;
import otus.api.dto.http.UserDto;

import java.util.List;

/*
/user/get/all - для получения списка всех пользователей

Для user
    {
    "name":"Test user",
    "cource":"QA",
    "email":"test@test.test"
    "age": 23
    }

 */
public class HttpMockUserBehavior implements TestBehavior {

    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .server("httpMock")
                .receive()
                .get("/user/get/all")
        );

        testActionRunner.run(http()
                .server("httpMock")
                .send()
                .response()
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(getUsersDto(), "objectMapper"))
        );
    }

    public List<UserDto> getUsersDto() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("Test user");
        userDto1.setCourse("QA");
        userDto1.setEmail("test@test.ru");
        userDto1.setAge(23);
        UserDto userDto2 = new UserDto();
        userDto2.setName("Test user 2");
        userDto2.setCourse("JAVA");
        userDto2.setEmail("test2@test.com");
        userDto2.setAge(45);
        UserDto userDto3 = new UserDto();
        userDto3.setName("Test user 3");
        userDto3.setCourse("Scala");
        userDto3.setEmail("test3@testing.net");
        userDto3.setAge(17);
        return List.of(userDto1, userDto2, userDto3);
    }
}
