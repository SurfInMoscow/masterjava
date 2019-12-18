package ru.javaops.masterjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.javaops.masterjava.web.payload.PayloadController;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.*;

@Controller
public class RootWebController {

    private final PayloadController payloadController;

    @Autowired
    public RootWebController(PayloadController payloadController) {
        this.payloadController = payloadController;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @PostMapping("/")
    public String setUploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, JAXBException {
        File uploadedFile = new File("/Users/vorobyev/Documents/projects/masterjava/tmp/" + file.getOriginalFilename());
        try (OutputStream out = new FileOutputStream(uploadedFile);
             InputStream filecontent = file.getInputStream()) {
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        }
        Payload payload = payloadController.getPayload(uploadedFile);
        request.setAttribute("users", payload.getUsers().getUser());
        return "uploadFile";
    }
}
