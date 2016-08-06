package de.hundhase.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public interface PictureDao extends CrudRepository<Picture, Long> {

    @Query("FROM Picture p WHERE YEAR(p.exifDate) = :year AND MONTH(p.exifDate) = :month")
    List<Picture> findPictures(@Param("year") int year, @Param("month") int month);

    @Query("FROM Picture p WHERE YEAR(p.exifDate) = :year AND MONTH(p.exifDate) = :month")
    List<Picture> findPictures(@Param("year") int year, @Param("month") int month, Pageable pageable);

    Page<Picture> findAll(Pageable pageable);

}
