package tsarzverey.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
public class NOrderController {
    private final OrderRepository orderRepo;

    @Autowired
    public NOrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    @GetMapping("/orders")
    String all(Model model){
        List<NOrder> orders = orderRepo.findAll();
        model.addAttribute("orderList", orders);
        return "orderList";
    }

    @GetMapping("/orders/add")
    String addOrderPage(Model model){
        model.addAttribute("newOrder", new NOrder());
        model.addAttribute("client", new Client());
        return "addOrder";
    }

    @PostMapping(value = "/orders/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addOrder(NOrder order, Client client){
        List<String> phoneNumbers = orderRepo.findAllClientPhones();
        for(String everyNumber:phoneNumbers){
            if(everyNumber.endsWith(client.getMobilePhone())){
                order.setClient(orderRepo.findClient(everyNumber));
                orderRepo.save(order);
                return "redirect:/orders";
            }
        }
        orderRepo.insertClient(orderRepo.findMaxId()+1,"Введите имя", client.getMobilePhone(), "Введите породу","Введите кличку","Введите оценку");
        order.setClient(orderRepo.findClient(client.getMobilePhone()));
        orderRepo.save(order);
        return "redirect:/clients/"+orderRepo.findMaxId();
    }

    @DeleteMapping(value = "/orders/{id}")
    String deleteOrder(@PathVariable Long id){
        if(orderRepo.findById(id).isPresent()){
            orderRepo.deleteById(id);
        }
        return "redirect:/orders";
    }

    @GetMapping(value = "/orders/{id}/edit")
    String editPage(@PathVariable Long id, Model model){
        model.addAttribute("order", orderRepo.findById(id).get());
        model.addAttribute("client", orderRepo.findById(id).get().getClient());
        return "editOrder";
    }

    @PutMapping(value = "/orders/{id}/edit")
    String editOrder(@PathVariable Long id, NOrder order, Client client){
        NOrder oldOrder = orderRepo.findById(id).get();
        oldOrder.setClient(orderRepo.findClient(client.getMobilePhone()));
        oldOrder.setDate(order.getDate());
        oldOrder.setTime(order.getTime());
        oldOrder.setPrice(order.getPrice());
        orderRepo.save(oldOrder);
        return "redirect:/orders";
    }

    @GetMapping(value = "/statistics")
    String clientStatistics(Model model){
        Set<String> breeds = orderRepo.findAllBreeds();
        //List<Integer> amounts = new ArrayList<>();
        Map<String,Integer> amounts = new HashMap<>();
        int sum = 0;
        for(String breed:breeds){
            amounts.put(breed,orderRepo.findAllOrdersWithBreed(breed).size());
            sum+=amounts.get(breed);
            //amounts.add(orderRepo.findAllOrdersWithBreed(breed).size());
        }
        /*for(Integer i:amounts) {
            sum+=i;
        }*/
        model.addAttribute("breeds",breeds);
        model.addAttribute("amounts",amounts);
        model.addAttribute("sum", sum);
        return "statistics";
    }
}
