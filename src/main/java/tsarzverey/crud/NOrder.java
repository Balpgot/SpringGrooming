package tsarzverey.crud;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import java.sql.Date;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Data
@Entity
@Table(name = "ORDERS_LIST")
public class NOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "phone", nullable = false)
    private Client client;
    private Date date;
    private String time;
    private Integer price;

    NOrder(){}

    NOrder(Long id, Client client, Date date, String time, Integer price){
        this.id = id;
        this.client = client;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    NOrder(Long id, Date date, String time, Integer price){
        this.id = id;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public String getFormattedDate(){
        String[] strings = this.date.toString().split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(strings[2]);
        sb.append(".");
        sb.append(strings[1]);
        return sb.toString();
    }

    public Date setFormattedDate(String date) {
        String[] strings = this.date.toString().split(".");
        return Date.valueOf(LocalDate.now(ZoneId.of("Europe/Moscow")).getYear() + strings[1] + strings[0]);
    }

}
