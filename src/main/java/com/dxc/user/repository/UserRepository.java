package com.dxc.user.repository;


import com.dxc.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("select u.Id from UserEntity u where u.Id =:Id")
    String findByUserExist(@Param("Id") String Id);

    @Query("select u from UserEntity u where u.deleted = false")
    List<UserEntity> findAllUser();

    @Modifying(clearAutomatically = true)
    @Query("update UserEntity u set u.deleted = true, u.modifiedDate = now() where u.Id =:Id and u.deleted = false")
    int markDeletedByUser(@Param("Id") String Id);

    List<UserEntity> findByFirstNameStartingWithAndLastNameEndingWith(String firstName, String lastName);

    @Modifying(clearAutomatically = true)
    @Query("update UserEntity app set app.activated = true where app.Id =:Id and app.activated = false")
    int markActivedByUser(@Param("Id") String Id);

    @Modifying(clearAutomatically = true)
    @Query("update UserEntity app set app.activated = false where app.Id =:Id and app.activated = true")
    int markDeActivedByUser(@Param("Id") String Id);

    //    // helper unit test
    @Query("select u from UserEntity u where u.Id =:Id ")
    UserEntity findByUserId(@Param("Id") String Id);
}
