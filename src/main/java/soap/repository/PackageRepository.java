package soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soap.entity.PackageEntity;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {
}
