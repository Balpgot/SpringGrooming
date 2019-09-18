package tsarzverey.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Set;


public interface OrderRepository extends JpaRepository<NOrder, Long> {
    @Query("SELECT c FROM Client c WHERE c.mobilePhone = :phone")
    Client findClient(@Param("phone") String phone);

    @Query("SELECT mobilePhone from Client")
    List<String> findAllClientPhones();

    @Query("SELECT DISTINCT poroda FROM Client")
    Set<String> findAllBreeds();

    @Query("SELECT price FROM NOrder")
    List<Integer> findAllReceives();

    @Query("SELECT o FROM NOrder o ORDER BY o.date")
    List<NOrder> findAllInDateOrder();

    @Query("SELECT o FROM NOrder o WHERE o.date = :date")
    List<NOrder> findAllByDate(@Param("date")Date date);

    @Query("SELECT o FROM NOrder o WHERE o.client.poroda = :poroda")
    List<NOrder> findAllOrdersWithBreed(@Param("poroda") String poroda);

    List<NOrder> findAllByDateAfter(Date date);

    @Modifying
    @Query(value = "Insert into Client(client_id,name,phone,poroda,petname,behavior) " +
            "values (:id,:clientName,:mobilePhone,:poroda,:petName,:behavior)", nativeQuery = true)
    @Transactional
    void insertClient(@Param("id") Long client_id, @Param("clientName") String clientName, @Param("mobilePhone") String mobilePhone,
                        @Param("poroda") String poroda, @Param("petName") String petName,
                        @Param("behavior") String behavior);

    @Query(value = "SELECT MAX(id) from Client")
    Long findMaxId();
}
