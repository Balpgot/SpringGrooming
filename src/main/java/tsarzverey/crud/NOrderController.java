package tsarzverey.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@Slf4j
@Controller
public class NOrderController {
    private final OrderRepository orderRepo;

    @Autowired
    public NOrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
        Client client = new Client(1L,"dasd","+79995359742","bull","dfdf","ydf");
        orderRepo.save(new NOrder(Long.parseLong("1"),client, Date.valueOf("2019-9-16"),"16:42",1200));
        orderRepo.save(new NOrder(Long.parseLong("2"),client, Date.valueOf("2019-9-17"),"16:30",1200));
        orderRepo.save(new NOrder(Long.parseLong("1"),client,Date.valueOf("2019-9-17"),"17:42",1200));
        orderRepo.save(new NOrder(Long.parseLong("3"),client,Date.valueOf("2019-9-18"),"16:42",1200));
    }

    @GetMapping("/orders")
    String all(Model model){
        List<NOrder> orders = orderRepo.findAllInDateOrder();
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
}
