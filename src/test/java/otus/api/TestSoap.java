package otus.api;

import static org.citrusframework.actions.LoadPropertiesAction.Builder.load;
import static org.citrusframework.ws.actions.SoapActionBuilder.soap;

import org.citrusframework.TestCaseRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.junit.jupiter.CitrusExtension;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.citrusframework.message.builder.MarshallingPayloadBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import otus.api.dto.soap.CelsiusToFahrenheit;

@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты для тестирования сервиса SOAP")
public class TestSoap {

    @BeforeEach
    public void init(@CitrusResource TestCaseRunner runner) {
        runner.$(load().filePath("file:src/test/resources/load.properties"));
    }

    @CitrusTest
    @Test
    @DisplayName("Тест на проверку SOAP")
    public void test(@CitrusResource TestCaseRunner runner) {

        //Отправка сообщения в SOAP Service - Farenheit to Celsius Conversion получение температуры
        runner.run(soap()
                .client("soapClient")
                .send()
                .message()
                .soapAction("https://www.w3schools.com/xml/CelsiusToFahrenheit")
                .body(new MarshallingPayloadBuilder(getCelsiusToFahrenheit("20"), "jaxb2Marshaller"))
        );

        /*
        Получение сообщения ответного
        Проверка заголовка "Content-Type"
        Проверка что CelsiusToFahrenheitResult равен 68.
        !!! Не получилось разобраться с Xpath из-за namespace внутри, поэтому вставил проверку по тексту. Нехорошо, но победить не получилось
         */
        runner.run(soap()
                .client("soapClient")
                .receive()
                .message()
                .name("msg")
                .header("Content-Type", "application/soap+xml; charset=utf-8")
                .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><CelsiusToFahrenheitResponse xmlns=\"https://www.w3schools.com/xml/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><CelsiusToFahrenheitResult>68</CelsiusToFahrenheitResult></CelsiusToFahrenheitResponse>")
        );

        //Отправка сообщения в SOAP Service с неверным значением
        runner.run(soap()
                .client("soapClient")
                .send()
                .message()
                .soapAction("https://www.w3schools.com/xml/CelsiusToFahrenheit")
                .body(new MarshallingPayloadBuilder(getCelsiusToFahrenheit("asd"), "jaxb2Marshaller"))
        );

        /*
        Получение сообщения ответного с ошибкой
        !!! Не получилось разобраться с Xpath из-за namespace внутри, поэтому вставил проверку по тексту. Нехорошо, но победить не получилось
         */
        runner.run(soap()
                .client("soapClient")
                .receive()
                .message()
                .name("msg")
                .header("Content-Type", "application/soap+xml; charset=utf-8")
                .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><CelsiusToFahrenheitResponse xmlns=\"https://www.w3schools.com/xml/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><CelsiusToFahrenheitResult>Error</CelsiusToFahrenheitResult></CelsiusToFahrenheitResponse>")
        );
    }

    public CelsiusToFahrenheit getCelsiusToFahrenheit(String value) {
        CelsiusToFahrenheit celsiusToFahrenheit = new CelsiusToFahrenheit();
        celsiusToFahrenheit.setCelsius(value);
        return celsiusToFahrenheit;
    }
}
