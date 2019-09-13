package tsarzverey.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/statistics", produces = "application/json")
@CrossOrigin(origins = "*")
public class StatisticsController {
    private OrderRepository orderRepo;
    private ClientRepository clientRepo;

    @Autowired
    StatisticsController(OrderRepository orderRepo, ClientRepository clientRepo){
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
    }

    @GetMapping
    String clientStatistics(Model model){
        Set<String> breeds = orderRepo.findAllBreeds();
        Map<String,Integer> amounts = new HashMap<>();
        int sum = 0;
        for(String breed:breeds){
            amounts.put(breed,orderRepo.findAllOrdersWithBreed(breed).size());
            sum+=amounts.get(breed);
        }
        model.addAttribute("breeds",breeds);
        model.addAttribute("amounts",amounts);
        model.addAttribute("sum", sum);
        return "statistics";
    }
}
