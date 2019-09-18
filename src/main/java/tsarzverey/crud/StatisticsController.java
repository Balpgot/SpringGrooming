package tsarzverey.crud;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class StatisticsController {
    private final OrderRepository orderRepo;
    private final ClientRepository clientRepo;


    StatisticsController(OrderRepository orderRepo, ClientRepository clientRepo){
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
    }

    @GetMapping(value = "/statistics")
    String statisticsNav(){
        return "statisticsNav";
    }

    @GetMapping(value = "/statistics/animals")
    String statisticsBreed(Model model){
        Set<String> breeds = orderRepo.findAllBreeds();
        Map<String,Double> amounts = new HashMap<>();
        int sum = 0;
        for(String breed:breeds){
            amounts.put(breed,orderRepo.findAllOrdersWithBreed(breed).size()+0.0);
            sum+=amounts.get(breed);
        }
        model.addAttribute("breeds",breeds);
        model.addAttribute("amounts",amounts);
        model.addAttribute("sum", sum);
        return "statisticsBreed";
    }

    @GetMapping(value = "/statistics/finances")
    String statisticsFinances(Model model){
        List<Integer> receives = orderRepo.findAllReceives();
        int sum = 0;
        for (Integer price:receives){
            sum+=price;
        }
        model.addAttribute("sum", sum);
        return "statisticsFinance";
    }
}
