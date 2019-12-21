package ru.javaops.masterjava.web.payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.model.UserFlag;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserProcessor {

    private final PayloadController payloadController;

    @Autowired
    public UserProcessor(PayloadController payloadController) {
        this.payloadController = payloadController;
    }

    public List<User> process(File uploadedFile) throws IOException, JAXBException {
        Payload payload = payloadController.getPayload(uploadedFile);
        List<User> users = new ArrayList<>();
        payload.getUsers().getUser().forEach(u -> {
            users.add(new User(u.getValue(), u.getEmail(), UserFlag.valueOf(u.getFlag().value())));
        });
        return users;
    }
}
