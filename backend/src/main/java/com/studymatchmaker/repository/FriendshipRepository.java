package com.studymatchmaker.repository;

import com.studymatchmaker.model.Friendship;
import com.studymatchmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.userOne = :user OR f.userTwo = :user")
    List<Friendship> findAllByUser(@Param("user") User user);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friendship f " +
           "WHERE (f.userOne = :a AND f.userTwo = :b) OR (f.userOne = :b AND f.userTwo = :a)")
    boolean areFriends(@Param("a") User a, @Param("b") User b);

    @Query("SELECT CASE WHEN f.userOne = :user THEN f.userTwo ELSE f.userOne END " +
           "FROM Friendship f WHERE f.userOne = :user OR f.userTwo = :user")
    List<User> findFriendUsers(@Param("user") User user);
}
