package tsarzverey.crud;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** TODO
 *  Добавить контроллер в основу
 */

@Controller
public class ScheduleController {
    private final String timeZone = "Europe/Moscow";
    private final OrderRepository orderRepo;
    private final LocalTime startTime = LocalTime.of(10,0);
    private final LocalTime endTime = LocalTime.of(18,0);

    ScheduleController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    @GetMapping(value = "/week", produces = MediaType.APPLICATION_JSON_VALUE)
    List<List<NOrder>> getWeeklyOrders(){
        int currentDay = LocalDate.now(ZoneId.of(timeZone)).getDayOfWeek().getValue();
        List<List<NOrder>> result = new ArrayList<>();
        switch (currentDay){
            case 0:
                for(long i = 0; i<7; i++){
                    result.add(orderRepo.findAllByDate(Date.valueOf(LocalDate.now(ZoneId.of(timeZone)).minusDays(i))));
                }
                Collections.reverse(result);
                break;
            default:
                for(long j = currentDay-1; j>=0; j--){
                    result.add(orderRepo.findAllByDate(Date.valueOf(LocalDate.now(ZoneId.of(timeZone)).minusDays(j))));
                }
                Collections.reverse(result);
                for(long i = currentDay; i<7; i++){
                    result.add(orderRepo.findAllByDate(Date.valueOf(LocalDate.now(ZoneId.of(timeZone)).plusDays(i))));
                }
                break;
        }
        System.out.println("ЫЫЫЫЫЫЫЫЫ");
        for(int i = 0; i<result.size(); i++){
            System.out.println(result.get(i));
        }
        return result;
    }

    @GetMapping(value = "/day")
    List<NOrder> exactDayOrders(LocalDate date){
        return orderRepo.findAllByDate(Date.valueOf(date));
    }

    @GetMapping(value = "/free")
    List<NOrder> freeSpace(Date date){
        List<NOrder> orders = orderRepo.findAllByDateAfter(date);
        LocalTime currentTime;
        List<NOrder> freeTime = new ArrayList<>();
        for (int i = 0; i<orders.size(); i++) {
            currentTime = orders.get(i).getTime().toLocalTime();
            if(currentTime.plusHours(1L).plusMinutes(30L).isBefore(orders.get(i+1).getTime().toLocalTime()));
        }
        return orders;
    }
}
