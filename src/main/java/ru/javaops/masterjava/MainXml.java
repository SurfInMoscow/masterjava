package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {

    public static List<User> usersInProject(String projectName) throws IOException, JAXBException {
        JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<User> users = payload.getUsers().getUser();
        List<User> usersInProject = new ArrayList<>();
        users.forEach(user -> {
            user.getGroups().getGroup().forEach(group -> {
                if (group.getName().equals(projectName)) usersInProject.add(user);
            });
        });
        return usersInProject.stream().sorted(Comparator.comparing(User::getFullName)).collect(Collectors.toList());
    }

    public static void staxProcessorUsersInGroups(String projectName) {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (processor.startElement("User", "Users")) {
                String name = processor.getElementValue("fullName");
                while (processor.startElement("Group", "Groups")) {
                    if (processor.getElementValue("name").equals(projectName)) System.out.println(name);
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, JAXBException, URISyntaxException {
        String projectName = "masterjava1";
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "  border: 1px solid black;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<table style=\"width:100%\">\n" +
                "  <tr>\n" +
                "    <th>Name</th>\n" +
                "    <th>Email</th> \n" +
                "  </tr>\n");

        usersInProject(projectName).forEach(user -> builder.append(String.format("<tr>\n" + "    <td>%s</td>\n" +
                        "    <td>%s</td>\n" + "</tr>\n", user.getFullName(), user.getEmail())));
        builder.append("</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        File htmlFile = new File("file.html");
        FileOutputStream outputStream = new FileOutputStream(htmlFile);
        outputStream.write(builder.toString().getBytes());

        staxProcessorUsersInGroups(projectName);
    }
}
