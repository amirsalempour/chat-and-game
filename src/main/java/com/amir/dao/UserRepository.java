package com.amir.dao;

import com.amir.model.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AmirSP on 7/13/2018.
 */
@Repository
public interface UserRepository extends JpaRepository<UserDomain,Long>{
//List<UserDomain> findallById
//    List<UserDomain> findByUserDomain(UserDomain userDomain);

}
