package org.se.lab.service.dao;

import java.util.List;

import org.se.lab.data.User;
import org.se.lab.data.UserContact;

public interface UserContactDAO {

    UserContact insert(UserContact contact);
    UserContact update(UserContact contact);
    void delete(UserContact contact);

    List<UserContact> findAll();

    UserContact findById(int id);

    boolean doesContactExistForUserId(int id, int userId);
    void deleteContactForUserIdAndContactId(int contactId,int userId);
    List<UserContact> findContactsbyUser(User user);

}
