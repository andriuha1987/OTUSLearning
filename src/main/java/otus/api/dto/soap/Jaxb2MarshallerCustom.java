package otus.api.dto.soap;

import org.citrusframework.xml.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

//Citrus хочет, чтобы класс имплементил Marshaller
public class Jaxb2MarshallerCustom extends Jaxb2Marshaller implements Marshaller {
}
