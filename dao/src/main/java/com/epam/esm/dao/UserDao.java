package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents an api to interact with the User entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.UserDaoImpl} classes.
 *
 * @since 2.0
 */
public interface UserDao {
    /**
     * This method get User entity by id.
     *
     * @param id User entity's id.
     * @return Optional of User entity.If there is no User with given id, return Optional.empty().
     * @since 2.0
     */
    Optional<User> findById(long id);

    /**
     * This method get User entity by login.
     *
     * @param login User entity's login.
     * @return Optional of User entity. If there is no User with given name, return Optional.empty().
     * @since 1.0
     */
    Optional<User> findByLogin(String login);

    /**
     * This method saves User entity.
     *
     * @param user User without id.
     * @return Saved User entity.
     * @since 2.0
     */
    User save(User user);

    /**
     * This method get list of users with the biggest sum of order costs.
     * There might be several users with the same the biggest sum of order costs.
     *
     * @return list of users with the biggest cost of orders.
     * @since 2.0
     */
    List<User> findWithMaxOrderCost();

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of User entities
     * @since 2.0
     */
    List<User> findBy(Map<String, String[]> reqParams, int limit, int offset);


}
