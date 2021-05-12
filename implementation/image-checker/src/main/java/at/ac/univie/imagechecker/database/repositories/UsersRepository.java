package at.ac.univie.imagechecker.database.repositories;

import at.ac.univie.imagechecker.database.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {
}
