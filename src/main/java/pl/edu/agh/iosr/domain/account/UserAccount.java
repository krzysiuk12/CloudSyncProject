package pl.edu.agh.iosr.domain.account;

import java.util.Date;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public class UserAccount {

    private Long id;
    private String login;
    private Date creationDate;
    private Date lastLoginDate;

    public UserAccount() {
    }

    public UserAccount(String login) {
        this.login = login;
    }

    public UserAccount(String login, Date creationDate) {
        this.login = login;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
