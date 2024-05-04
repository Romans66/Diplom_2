package clients.order.responseobj;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class IngredientsResponseObj {
    private boolean success;
    private ArrayList<DataIngredientsResponseObj> data;
    
    public IngredientsResponseObj() {
    }
}
