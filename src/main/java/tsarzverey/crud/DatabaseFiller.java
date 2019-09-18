package tsarzverey.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Random;

/** TODO
 * Подумать над библиотекой для заполнения таблиц
 */

@Service
public class DatabaseFiller {

    private OrderRepository orderRepo;
    private ClientRepository clientRepo;
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private Random random;

    @Autowired
    DatabaseFiller(OrderRepository orderRepo, ClientRepository clientRepo){
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
        random = new Random();
    }

    private String generateMobile(){
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for(int i = 0; i<11; i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateWord(int length){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<length; i++){
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    /** TODO
     *  Дописать
     */
    private Date generateDate(){
        StringBuilder sb = new StringBuilder();
        return null;
    }

    public void fillClients(int amount){
        long clientCounter = 1L;
        for(int i = 0; i<amount; i++){
            clientRepo.save(new Client(clientCounter++,generateWord(5),generateMobile(),generateWord(6),generateWord(8),generateWord(6)));
        }
    }

    public void fillOrders(int amount){
        long orderCounter = 1L;
        int clientsCount = clientRepo.findAll().size();
        for(int i = 0; i<amount; i++){
            orderRepo.save(new NOrder(orderCounter++,clientRepo.findById((long)clientsCount).get(),Date.valueOf("2019-9-18"),"16:42",1200));
        }
    }
}
