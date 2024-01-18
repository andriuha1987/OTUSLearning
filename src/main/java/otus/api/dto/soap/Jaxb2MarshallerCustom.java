package otus.api.dto.soap;

import com.consol.citrus.xml.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

//Citrus хочет, чтобы класс имплементил Marshaller
public class Jaxb2MarshallerCustom extends Jaxb2Marshaller implements Marshaller {
}
