package ru.javaops.masterjava.web.payload;

import org.springframework.stereotype.Controller;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Controller
public class PayloadController {
    private JaxbParser JAXB_PARSER;

    public PayloadController() {
        JAXB_PARSER = new JaxbParser(ObjectFactory.class);
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public Payload getPayload(File file) throws IOException, JAXBException {
        return JAXB_PARSER.unmarshal(Files.newInputStream(file.toPath(), StandardOpenOption.READ));
    }
}
