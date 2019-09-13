package tsarzverey.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(path = "/orders", produces = "application/json")
@CrossOrigin(origins = "*")
public class NOrderController {
    private final OrderRepository orderRepo;

    @Autowired
    public NOrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    @GetMapping
    List<NOrder> all(){
        return orderRepo.findAll();
    }

    @GetMapping("/add")
    String addOrderPage(Model model){
        model.addAttribute("newOrder", new NOrder());
        model.addAttribute("client", new Client());
        return "addOrder";
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NOrder> addOrder(NOrder order, Client client){
        List<String> phoneNumbers = orderRepo.findAllClientPhones();
        for(String everyNumber:phoneNumbers){
            if(everyNumber.endsWith(client.getMobilePhone())){
                order.setClient(orderRepo.findClient(everyNumber));
                orderRepo.save(order);
                return new ResponseEntity<>(order, HttpStatus.CREATED);
            }
        }
        orderRepo.insertClient(orderRepo.findMaxId()+1,"Введите имя", client.getMobilePhone(), "Введите породу","Введите кличку","Введите оценку");
        order.setClient(orderRepo.findClient(client.getMobilePhone()));
        orderRepo.save(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    String deleteOrder(@PathVariable Long id){
        if(orderRepo.findById(id).isPresent()){
            orderRepo.deleteById(id);
        }
        return "redirect:/orders";
    }

    @GetMapping(value = "/{id}/edit")
    String editPage(@PathVariable Long id, Model model){
        model.addAttribute("order", orderRepo.findById(id).get());
        model.addAttribute("client", orderRepo.findById(id).get().getClient());
        return "editOrder";
    }

    @PutMapping(value = "/{id}/edit")
    String editOrder(@PathVariable Long id, NOrder order, Client client){
        NOrder oldOrder = orderRepo.findById(id).get();
        oldOrder.setClient(orderRepo.findClient(client.getMobilePhone()));
        oldOrder.setDate(order.getDate());
        oldOrder.setTime(order.getTime());
        oldOrder.setPrice(order.getPrice());
        orderRepo.save(oldOrder);
        return "redirect:/orders";
    }

    @GetMapping(value = "/next")
    List<NOrder> getNextOrder(@RequestParam Date date){
        return orderRepo.findAllByDate(date);
    }

    @GetMapping(value = "/current_week")
    List<NOrder> getNextWeekOrders(){
        List<NOrder> orders = new ArrayList<>();
        LocalDate date = LocalDate.now(ZoneId.of("Europe/Moscow"));
        int dayOfWeek = date.getDayOfWeek();
        for(long i = 0; i<7; i++){
            orderRepo.findAllByDate()
        }
    }
}
