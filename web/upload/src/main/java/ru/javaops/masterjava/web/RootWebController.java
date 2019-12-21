package ru.javaops.masterjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.javaops.masterjava.web.payload.UserProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class RootWebController {

    private final UserProcessor userProcessor;

    @Autowired
    public RootWebController(UserProcessor userProcessor) {
        this.userProcessor = userProcessor;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @PostMapping("/")
    public String setUploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            File uploadedFile = new File("/Users/vorobyev/Documents/projects/masterjava/tmp/" + file.getOriginalFilename());
            try (OutputStream out = new FileOutputStream(uploadedFile);
                 InputStream filecontent = file.getInputStream()) {
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
            request.setAttribute("users", userProcessor.process(uploadedFile));
        } catch (Exception e) {
            request.setAttribute("exception", e);
            return "exception";
        }
        return "uploadFile";
    }
}
