package ash.wasset.models;

/**
 * Created by ahmed on 12/19/16.
 */

public class PasswordModel {

    private String NewPassword;
    private String OldPassword;

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }
}
