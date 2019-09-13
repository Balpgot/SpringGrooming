package tsarzverey.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/clients", produces = "application/json")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientRepository clientRepo;

    @Autowired
    public ClientController(ClientRepository clientRepo){
        this.clientRepo = clientRepo;
        clientRepo.save(new Client(Long.parseLong("1"),"2","3","4","5","6"));
        clientRepo.save(new Client(Long.parseLong("2"),"2","3","4","5","6"));
        clientRepo.save(new Client(Long.parseLong("3"),"2","3","4","5","6"));
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<Client> findClientByPhone(@RequestBody Client client){
        List<Client> clients = clientRepo.findAll();
        for(Client cli:clients){
            if(cli.getMobilePhone().endsWith(client.getMobilePhone())){
                return new ResponseEntity<>(clientRepo.findClientByPhone(cli.getMobilePhone()),HttpStatus.FOUND);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/add")
    String addClientPage(Model model){
        model.addAttribute("newClient", new Client());
        return "addClient";
    }

    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity <Client> addClient(Client client){
        clientRepo.save(client);
        return new ResponseEntity<>(client,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepo.findById(id);
        if(client.isPresent()){
            return new ResponseEntity<>(client.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/edit")
    String editClient(@PathVariable Long id, Model model){
        model.addAttribute("client", clientRepo.findById(id).get());
        return "editClient";
    }

    @PutMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

    @DeleteMapping("/{id}")
    @Transactional
    String deleteClient(@PathVariable Long id){
        if(clientRepo.existsById(id)){
            clientRepo.deleteById(id);
        }
        return "redirect:/clients";
    }

    @GetMapping("/{id}/orders")
    String getOrders(@PathVariable Long id, Model model){
        String s = clientRepo.findById(id).get().getMobilePhone();
        System.out.println(clientRepo.findAllClientOrders(s));
        model.addAttribute("orderList",clientRepo.findAllClientOrders(s));
        return "orderList";
    }
}
