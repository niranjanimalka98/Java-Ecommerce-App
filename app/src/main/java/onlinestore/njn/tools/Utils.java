package onlinestore.njn.tools;

import android.util.Log;

import java.util.List;

import onlinestore.njn.model.UserModel;

public class Utils {

    public static final UserModel get_logged_in_user() {
        try {
            List<UserModel> allUsers = UserModel.listAll(UserModel.class);
            if(allUsers == null){
                return null;
            }
            if (allUsers.isEmpty()){
                return null;
            }
            return allUsers.get(0);

        }catch (Exception e){
            return null;
        }

    }
}
