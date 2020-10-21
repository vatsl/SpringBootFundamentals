package ttl.larku.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adminrest/redirector")
public class RedirectingController {

    @Resource
    private StudentService studentService;

    @PostMapping
    public String createStudent(@RequestBody Student s) {
        s = studentService.createStudent(s);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(s.getId())
                .toUri();

//        return ResponseEntity.created(newResource).body(new RestResult(s));
        return "redirect:/adminrest/student";
    }
}
