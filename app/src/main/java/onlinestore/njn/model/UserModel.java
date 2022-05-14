package onlinestore.njn.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class UserModel extends SugarRecord {
    public String first_name = "";
    public String last_name = "";
    public String email = "";
    public String password = "";
    public String phone_number = "";
    public String address = "";
    public String profile_photo = "";
    public String reg_date = "";
    public String user_type = "";

    @Unique
    public String user_id = "";

}
