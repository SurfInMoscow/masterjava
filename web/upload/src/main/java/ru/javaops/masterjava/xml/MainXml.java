package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import j2html.tags.ContainerTag;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class MainXml {

    public static List<User> usersInProject(String projectName) throws IOException, JAXBException {
        JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
        JAXB_PARSER.createUnmarshaller().unmarshal(Resources.getResource("payload.xml").openStream());
        Payload payload = JAXB_PARSER.createUnmarshaller().unmarshal(Resources.getResource("payload.xml").openStream());
        List<User> users = payload.getUsers().getUser();
        Project project = payload.getProjects().getProject().stream().filter(p -> p.getName().equals(projectName)).findAny().get();
        Set<Group> groups = new HashSet<>(project.getGroup());
        return users.stream().filter(user -> !Collections.disjoint(groups, user.getGroupRefs())).sorted(Comparator.comparing(User::getValue)).collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException, JAXBException, URISyntaxException {
        String projectName = "masterjava";
        final ContainerTag table = table().with(
                tr().with(th("Name"), th("email")))
                .attr("border", "1")
                .attr("cellpadding", "8")
                .attr("cellspacing", "0");

        usersInProject(projectName).forEach(user -> table.with(tr().with(td(user.getValue()), td(user.getEmail()))));

        File htmlFile = new File("file.html");
        FileOutputStream outputStream = new FileOutputStream(htmlFile);
        outputStream.write(html().with(table).render().getBytes());
    }
}
