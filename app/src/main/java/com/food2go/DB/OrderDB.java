package com.food2go.DB;

/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.food2go.Model.Order;

import java.util.ArrayList;
import java.util.List;


public class OrderDB extends SQLiteAssetHelper{

    private static final String DB_NAME="food2go.db";
    private static final int DB_VER=1;

    public OrderDB(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCart(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"productID", "productName","quantity", "price", "discount"};
        String sqlTable = "orderDetails";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect, null,null,null,null,null);

        final  List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(c.getString(c.getColumnIndex("productID")),
                        c.getString(c.getColumnIndex("productName")),
                        c.getInt(c.getColumnIndex("quantity")),
                        c.getDouble(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO orderDetails(productID,productName,quantity,price,discount) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                Integer.parseInt(String.valueOf(order.getQuantity())),
                Double.parseDouble(String.valueOf(order.getPrice())),
                order.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM orderDetails");
        db.execSQL(query);
    }

}