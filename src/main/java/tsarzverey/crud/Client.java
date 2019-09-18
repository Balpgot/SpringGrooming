package tsarzverey.crud;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(exclude = "orders")
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;
    @Column(name = "name")
    private String clientName;
    @Column(name = "phone")
    private String mobilePhone;
    @Column(name = "poroda")
    private String poroda;
    @Column(name = "petname")
    private String petName;
    @Column(name = "behavior")
    private String behavior;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private List<NOrder> orders;

    Client(){}

    public Client(Long id, String clientName, String mobilePhone, String poroda, String petName, String behavior){
        this.id = id;
        this.clientName = clientName;
        this.mobilePhone = mobilePhone;
        this.poroda = poroda;
        this.petName = petName;
        this.behavior = behavior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPoroda() {
        return poroda;
    }

    public void setPoroda(String poroda) {
        this.poroda = poroda;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public List<NOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<NOrder> orders) {
        this.orders = orders;
    }
}
