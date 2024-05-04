package clients.order.requestobj;

import lombok.Data;

import java.util.ArrayList;

@Data
public class IngredientsRequestObj {
    private ArrayList<String> ingredients = new ArrayList<>();
    
    public IngredientsRequestObj() {
    }
    
    public IngredientsRequestObj(String ingredient) {
        ingredients.add(ingredient);
    }
}

