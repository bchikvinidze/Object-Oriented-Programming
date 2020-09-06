package main.java;
import java.util.HashMap;
import java.util.Set;

public class ShoppingCart {
    public HashMap<String, Integer> cart;

    /* Constructor for shopping cart */
    public ShoppingCart(){
        cart = new HashMap<String, Integer>();
    }

    /* sets count on hashmap equal to count for some product ID*/
    public void setCount(String productId, int count){
        if(count != 0)
            cart.put(productId, count);
        else
            cart.remove(productId);
    }

    /* straigtforward: adds new item if nonexistant, or increments by one*/
    public void addItem(String productId){
        if(!cart.containsKey(productId))
            cart.put(productId, 1);
        else
            cart.put(productId, cart.get(productId)+1);
    }

    /* returns count of different elements in a cart */
    public int elemsCnt(){
        return cart.size();
    }

    /* returns set of different items that are in cart */
    public Set<String> keys(){
        return cart.keySet();
    }

    /* returns how many of certain product is in the cart*/
    public int getCnt(String productid){
        return cart.get(productid);
    }
}
