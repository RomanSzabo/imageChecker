package at.ac.univie.imagechecker.database.repositories;

import at.ac.univie.imagechecker.database.entities.MessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessagesEntity, String> {

    public List<MessagesEntity> findAllByReceiver(String receiver);

}
