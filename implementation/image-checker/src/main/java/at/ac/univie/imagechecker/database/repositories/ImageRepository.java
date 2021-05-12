package at.ac.univie.imagechecker.database.repositories;

import at.ac.univie.imagechecker.database.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, String> {

}
