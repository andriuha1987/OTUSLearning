package otus.api.dto.soap;

import jakarta.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "celsius"
})
@XmlRootElement(name = "CelsiusToFahrenheit")
public class CelsiusToFahrenheit {

    @XmlElement(name = "Celsius")
    protected String celsius;

    public String getCelsius() {
        return celsius;
    }

    public void setCelsius(String value) {
        this.celsius = value;
    }

}
