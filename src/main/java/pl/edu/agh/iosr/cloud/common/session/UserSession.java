package pl.edu.agh.iosr.cloud.common.session;

import pl.edu.agh.iosr.accounts.UserAccount;

/**
 * Created by ps_krzysztof on 2015-06-01.
 */
public class UserSession {

    private UserAccount userAccount;
    private CloudSession dropboxCloudSession;
    private CloudSession googleDriveCloudSession;
    private CloudSession onedriveCloudSession;

    public UserSession() {
    }

    public UserSession(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserSession(UserAccount userAccount, CloudSession dropboxCloudSession, CloudSession googleDriveCloudSession, CloudSession onedriveCloudSession) {
        this.userAccount = userAccount;
        this.dropboxCloudSession = dropboxCloudSession;
        this.googleDriveCloudSession = googleDriveCloudSession;
        this.onedriveCloudSession = onedriveCloudSession;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public CloudSession getDropboxCloudSession() {
        return dropboxCloudSession;
    }

    public void setDropboxCloudSession(CloudSession dropboxCloudSession) {
        this.dropboxCloudSession = dropboxCloudSession;
    }

    public CloudSession getGoogleDriveCloudSession() {
        return googleDriveCloudSession;
    }

    public void setGoogleDriveCloudSession(CloudSession googleDriveCloudSession) {
        this.googleDriveCloudSession = googleDriveCloudSession;
    }

    public CloudSession getOnedriveCloudSession() {
        return onedriveCloudSession;
    }

    public void setOnedriveCloudSession(CloudSession onedriveCloudSession) {
        this.onedriveCloudSession = onedriveCloudSession;
    }

}
