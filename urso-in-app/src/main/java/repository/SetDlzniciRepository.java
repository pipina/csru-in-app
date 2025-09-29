package repository;

import model.SetDlznici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetDlzniciRepository extends JpaRepository<SetDlznici, Long> {

    List<SetDlznici> findAllBySync(boolean sync);

}
