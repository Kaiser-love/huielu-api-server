package com.ronghui.service.jpa.dao;

import com.ronghui.service.entity.Picture;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PictureDao extends JpaRepository<Picture, Long> {
    Picture findByPicid(long picid);

    List<Picture> findPicturesByDirectory_Dirid(long dirid);

    @Modifying
    @Query(value = "delete from Picture p where p.picid in (:picList)")
    void deletePictureList(@Param("picList") ArrayList<Long> picList);

}
