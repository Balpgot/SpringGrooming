package tsarzverey.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface
ClientRepository extends JpaRepository<Client,Long> {

    @Query("SELECT c FROM Client c WHERE c.mobilePhone = :phone")
    Client findClientByPhone(@Param("phone") String phone);

    @Query("SELECT mobilePhone FROM Client")
    List<String> findPhones();

    @Query("SELECT o FROM NOrder o WHERE o.client.mobilePhone = :mobilePhone")
    List<NOrder> findAllClientOrders(@Param("mobilePhone") String mobilePhone);
}
