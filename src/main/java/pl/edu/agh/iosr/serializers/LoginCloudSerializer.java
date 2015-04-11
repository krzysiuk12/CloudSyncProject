package pl.edu.agh.iosr.serializers;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public class LoginCloudSerializer {

    private String login;
    private String authorizationCode;

    public LoginCloudSerializer() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
