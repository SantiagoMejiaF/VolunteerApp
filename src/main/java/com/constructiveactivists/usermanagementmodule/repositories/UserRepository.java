package com.constructiveactivists.usermanagementmodule.repositories;

import com.constructiveactivists.usermanagementmodule.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> { }
