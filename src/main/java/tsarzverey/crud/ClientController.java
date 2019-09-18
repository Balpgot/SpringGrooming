package tsarzverey.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
public class ClientController {

    public final ClientRepository clientRepo;

    @Autowired
    public ClientController(ClientRepository clientRepo){
        this.clientRepo = clientRepo;
    }

    @GetMapping("/clients")
    String all(Model model) {
        List<Client> clients = clientRepo.findAll();
        model.addAttribute("clientList",clients);
        model.addAttribute("client",new Client());
        return "clientList";
    }

    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String findClient(Client client){
        List<String> phones = clientRepo.findPhones();
        for(String phone:phones){
            if(phone.endsWith(client.getMobilePhone())){
                return "redirect:/clients/"+clientRepo.findClientByPhone(phone).getId();
            }
        }
        return "redirect:/clients";
    }

    @GetMapping("/clients/add")
    String addClientPage(Model model){
        model.addAttribute("newClient", new Client());
        return "addClient";
    }

    @PostMapping(value = "/clients/add",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addClient(Client client){
        clientRepo.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/clients/{id}")
    String one(@PathVariable Long id,Model model) {
        if(clientRepo.findById(id).isPresent()){
            model.addAttribute("client", clientRepo.findById(id).get());
        }
        return "clientCard";
    }

    @GetMapping(value = "/clients/{id}/edit")
    String editClient(@PathVariable Long id, Model model){
        model.addAttribute("client", clientRepo.findById(id).get());
        return "editClient";
    }

    @PutMapping(value = "/clients/{id}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String replaceClient(@PathVariable Long id, Client client) {
        Client oldClient = clientRepo.findById(id).get();
        oldClient.setClientName(client.getClientName());
        oldClient.setMobilePhone(client.getMobilePhone());
        oldClient.setPetName(client.getPetName());
        oldClient.setPoroda(client.getPoroda());
        oldClient.setBehavior(client.getBehavior());
        oldClient.setOrders(client.getOrders());
        clientRepo.save(oldClient);
        return "redirect:/clients/"+id;
    }

    @DeleteMapping("/clients/{id}")
    @Transactional
    String deleteClient(@PathVariable Long id){
        if(clientRepo.existsById(id)){
            clientRepo.deleteById(id);
        }
        return "redirect:/clients";
    }

    @GetMapping("/clients/{id}/orders")
    String getOrders(@PathVariable Long id, Model model){
        String s = clientRepo.findById(id).get().getMobilePhone();
        model.addAttribute("orderList",clientRepo.findAllClientOrders(s));
        return "orderList";
    }
}
