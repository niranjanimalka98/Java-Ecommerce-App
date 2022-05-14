package onlinestore.njn.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class CartModel extends SugarRecord {
   public String product_name = "";

    @Unique
    public String product_id = "";
    public String product_photo = "";
    public int quantity = 1;
    public int product_price = 0;


}
