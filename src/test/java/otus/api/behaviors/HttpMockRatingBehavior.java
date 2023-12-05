package otus.api.behaviors;

import static org.citrusframework.http.actions.HttpActionBuilder.http;

import org.citrusframework.TestActionRunner;
import org.citrusframework.TestBehavior;
import org.citrusframework.http.actions.HttpServerActionBuilder;
import org.citrusframework.message.MessageType;
import org.citrusframework.message.builder.ObjectMappingPayloadBuilder;
import org.springframework.http.HttpStatus;
import otus.api.dto.http.RatingDto;

/*
/user/get/{id} для получения оценки пользователя

Для оценки:
    {
    "name":"Test user",
    "score": 78
    }
 */
public class HttpMockRatingBehavior implements TestBehavior {

    int id;

    public HttpMockRatingBehavior(int id) {
        this.id = id;
    }

    @Override
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(http()
                .server("httpMock")
                .receive()
                .get("/user/get/" + id)
        );

        HttpServerActionBuilder.HttpServerSendActionBuilder httpServerSendActionBuilder = http()
                        .server("httpMock")
                        .send();
        if (id == 100) {
            testActionRunner.run(http()
                    .server("httpMock")
                    .send()
                    .response()
                    .message()
                    .type(MessageType.JSON)
                    .body(new ObjectMappingPayloadBuilder(getRatingDto(), "objectMapper"))
            );
        } else {
            testActionRunner.run(http()
                    .server("httpMock")
                    .send()
                    .response(HttpStatus.NOT_FOUND)
                    .message()
                    .body("{"
                            + "\"msg\":\"Test user not found\""
                            + "}")
            );
        }
    }

    public RatingDto getRatingDto() {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setName("Test user");
        ratingDto.setScore(85);
        return ratingDto;
    }
}
