package com.studymatchmaker.repository;

import com.studymatchmaker.model.Friendship;
import com.studymatchmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
            SELECT f
            FROM Friendship f
            WHERE f.userOne.id = :userId OR f.userTwo.id = :userId
            """)
    List<Friendship> findAllByUserId(@Param("userId") Long userId);

    default List<Friendship> findAllByUser(User user) {
        return findAllByUserId(user.getId());
    }

    @Query("""
            SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
            FROM Friendship f
            WHERE (f.userOne.id = :aId AND f.userTwo.id = :bId)
               OR (f.userOne.id = :bId AND f.userTwo.id = :aId)
            """)
    boolean areFriendsByIds(@Param("aId") Long aId, @Param("bId") Long bId);

    default boolean areFriends(User a, User b) {
        return areFriendsByIds(a.getId(), b.getId());
    }

    @Query("""
            SELECT f.userTwo
            FROM Friendship f
            WHERE f.userOne.id = :userId
            """)
    List<User> findUsersWhereCurrentUserIsUserOne(@Param("userId") Long userId);

    @Query("""
            SELECT f.userOne
            FROM Friendship f
            WHERE f.userTwo.id = :userId
            """)
    List<User> findUsersWhereCurrentUserIsUserTwo(@Param("userId") Long userId);

    default List<User> findFriendUsersByUserId(Long userId) {
        List<User> friends = new ArrayList<>();
        friends.addAll(findUsersWhereCurrentUserIsUserOne(userId));
        friends.addAll(findUsersWhereCurrentUserIsUserTwo(userId));
        return friends;
    }

    default List<User> findFriendUsers(User user) {
        return findFriendUsersByUserId(user.getId());
    }
}
