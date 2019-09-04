package tsarzverey.crud;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
    @GetMapping("/")
    public String navigation(){
        return "navigation";
    }
}
