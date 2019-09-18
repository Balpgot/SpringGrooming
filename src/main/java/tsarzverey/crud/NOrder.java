package tsarzverey.crud;

import lombok.Data;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.*;

/** TODO
 *  Залить в Основу
 */
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
    private Time time;
    private Integer price;

    NOrder(){}

    NOrder(Long id, Client client, Date date, String time, Integer price){
        this.id = id;
        this.client = client;
        this.date = date;
        this.time = convertStringToTime(time);
        this.price = price;
    }

    NOrder(Long id, Date date, String time, Integer price){
        this.id = id;
        this.date = date;
        this.time = convertStringToTime(time);
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTimeZone(){
        return "Europe/Moscow";
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
        return Date.valueOf(LocalDate.now(ZoneId.of(getTimeZone())).getYear() + strings[1] + strings[0]);
    }

    public Time convertStringToTime(String stringTime){
        return Time.valueOf(stringTime+":00");
    }

}
