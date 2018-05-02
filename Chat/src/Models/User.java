package Models;

import java.util.Objects;

public class User {
    private String userID;

    public String getUserID() {
        return userID;
    }

    public User(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userID);
    }
}
