package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
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
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {

    public static List<User> usersInProject(String projectName) throws IOException, JAXBException {
        JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<User> users = payload.getUsers().getUser();
        Project project = payload.getProjects().getProject().stream().filter(p -> p.getName().equals(projectName)).findAny().get();
        Set<Group> groups = new HashSet<>(project.getGroup());
        return users.stream().filter(user -> !Collections.disjoint(groups, user.getGroupRefs())).sorted(Comparator.comparing(User::getValue)).collect(Collectors.toList());
    }

    //TODO
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

    //TODO
    public static void main(String[] args) throws IOException, JAXBException, URISyntaxException {
        String projectName = "masterjava";
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
                        "    <td>%s</td>\n" + "</tr>\n", user.getValue(), user.getEmail())));
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
