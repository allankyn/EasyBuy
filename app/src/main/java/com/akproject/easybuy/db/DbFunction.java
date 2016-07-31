package com.akproject.easybuy.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akproject.easybuy.model.Attribute;
import com.akproject.easybuy.model.Item;
import com.akproject.easybuy.model.PaymentMethod;
import com.akproject.easybuy.model.Transaction;
import com.akproject.easybuy.utility.DateManager;
import com.akproject.easybuy.utility.LogManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Allan on 16/1/2016.
 */
public class DbFunction {
    /*
        * -1 for not found
        */
    public int getMaxTextId(Activity activity) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int maxTextId = -1;
        try {
            String sql = "Select max(" + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + ") from " +
                    DbTable.DisplayText.TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null && cursor.moveToFirst())
                maxTextId = cursor.getInt(0);
            cursor.close();
        }
        catch(Exception e){
            maxTextId = -1;
        }
        db.close();
        mDbHelper.close();

        return maxTextId;
    }

    /* Insert */
    public long insertText(Activity activity, int textId, int langId, String text, int userId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.DisplayText.COLUMN_NAME_TEXT_ID, textId);
        values.put(DbTable.DisplayText.COLUMN_NAME_LANG_ID, langId);
        values.put(DbTable.DisplayText.COLUMN_NAME_TEXT, text);
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

        long newRowId = -1;
        try {
            newRowId = db.insert(DbTable.DisplayText.TABLE_NAME, "NULL", values);
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return newRowId;
    }

    public long insertAttribute(Activity activity, int attributeType, int attributeTextId, int userId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Attribute.COLUMN_NAME_TYPE, attributeType);
        values.put(DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_TEXT_ID, attributeTextId);
        values.put(DbTable.Attribute.COLUMN_NAME_ORDER, 10001);
        values.put(DbTable.Attribute.COLUMN_NAME_ENABLE, 1);
        values.put(DbTable.Attribute.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Attribute.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

        long newRowId = -1;
        try {
            newRowId = db.insert(DbTable.Attribute.TABLE_NAME, "NULL", values);
        } catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return newRowId;
    }

    public long insertPaymentMethod(Activity activity, int paymentMethodTextId, int cutOffDay, int userId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_TEXT_ID, paymentMethodTextId);
        values.put(DbTable.PaymentMethod.COLUMN_NAME_CUTOFF_DAY, cutOffDay);
        values.put(DbTable.PaymentMethod.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.PaymentMethod.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

        long newRowId = -1;
        try {
            newRowId = db.insert(DbTable.PaymentMethod.TABLE_NAME, "NULL", values);
        } catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return newRowId;
    }

    public long insertItem(Activity activity, int itemTextId, int[] attributeIds, int userId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID, itemTextId);
        values.put(DbTable.Item.COLUMN_NAME_ENABLE, 1);
        values.put(DbTable.Item.COLUMN_NAME_ORDER, 10001);
        values.put(DbTable.Item.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Item.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

        long newItemId = -1;
        try {
            newItemId = db.insert(DbTable.Item.TABLE_NAME, "NULL", values);
        } catch(Exception e){
        }

        long newRowId;
        if (newItemId > 0 && attributeIds != null && attributeIds.length > 0) {
            for (int i=0; i<attributeIds.length; i++) {
                values = new ContentValues();
                values.put(DbTable.ItemAttrRel.COLUMN_NAME_ITEM_ID, newItemId);
                values.put(DbTable.ItemAttrRel.COLUMN_NAME_ATTRIBUTE_ID, attributeIds[i]);
                values.put(DbTable.ItemAttrRel.COLUMN_NAME_UPDATE_USR, userId);
                values.put(DbTable.ItemAttrRel.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

                newRowId = -1;
                try {
                    newRowId = db.insert(DbTable.ItemAttrRel.TABLE_NAME, "NULL", values);
                } catch (Exception e) {
                }
            }
        }
        db.close();
        mDbHelper.close();

        return newItemId;
    }

    public long insertTransaction(Activity activity, int itemId, float itemPrice, String shopName, String transactionDate, boolean hasBuy, int paymentMethodId, int[] attributeIds, int userId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Transaction.COLUMN_NAME_ITEM_ID, itemId);
        values.put(DbTable.Transaction.COLUMN_NAME_SHOP, shopName);
        values.put(DbTable.Transaction.COLUMN_NAME_PRICE, itemPrice);
        values.put(DbTable.Transaction.COLUMN_NAME_DATE, transactionDate);
        values.put(DbTable.Transaction.COLUMN_NAME_DONE, hasBuy);
        values.put(DbTable.Transaction.COLUMN_NAME_PAYMENT_METHOD_ID, paymentMethodId);
        values.put(DbTable.Transaction.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Transaction.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

        long newTransactionId = -1;
        try {
            newTransactionId = db.insert(DbTable.Transaction.TABLE_NAME, "NULL", values);
        } catch(Exception e){
        }

        long newRowId;
        if (newTransactionId > 0 && attributeIds != null && attributeIds.length > 0) {
            for (int i=0; i<attributeIds.length; i++) {
                values = new ContentValues();
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID, newTransactionId);
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID, attributeIds[i]);
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_UPDATE_USR, userId);
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());

                newRowId = -1;
                try {
                    newRowId = db.insert(DbTable.TransactionAttrRel.TABLE_NAME, "NULL", values);
                } catch (Exception e) {
                }
            }
        }
        db.close();
        mDbHelper.close();

        return newTransactionId;
    }

    /* Update */
    public boolean updateAttribute(Context context, Attribute attribute, int userId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Attribute.COLUMN_NAME_ENABLE, attribute.isEnable());
        values.put(DbTable.Attribute.COLUMN_NAME_ORDER, 10001);
        values.put(DbTable.Attribute.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Attribute.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected1 = -1;
        try {
            rowAffected1 = db.update(DbTable.Attribute.TABLE_NAME, values,
                    DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + "=" + attribute.getAttributeId(), null);
        } catch(Exception e){
        }

        values = new ContentValues();
        values.put(DbTable.DisplayText.COLUMN_NAME_TEXT, attribute.getAttributeName());
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected2 = -1;
        try {
            rowAffected2 = db.update(DbTable.DisplayText.TABLE_NAME, values,
                    DbTable.DisplayText.COLUMN_NAME_TEXT_ID + "=" + attribute.getAttributeNameTextId(), null);
        } catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        if (rowAffected1 == -1 && rowAffected2 == -1)
            return false;
        else
            return true;
    }

    public boolean updateItem(Context context, Item item, int userId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Item.COLUMN_NAME_ENABLE, item.isEnable());
        //values.put(DbTable.Item.COLUMN_NAME_ORDER, item.getOrder());
        values.put(DbTable.Item.COLUMN_NAME_ORDER, 10001);
        values.put(DbTable.Item.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Item.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected1 = -1;
        try {
            rowAffected1 = db.update(DbTable.Item.TABLE_NAME, values,
                    DbTable.Item.COLUMN_NAME_ITEM_ID + "=" + item.getItemId(), null);
        } catch(Exception e){
        }

        values = new ContentValues();
        values.put(DbTable.DisplayText.COLUMN_NAME_TEXT, item.getItemName());
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected2 = -1;
        try {
            rowAffected2 = db.update(DbTable.DisplayText.TABLE_NAME, values,
                    DbTable.DisplayText.COLUMN_NAME_TEXT_ID + "=" + item.getItemNameTextId(), null);
        } catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        if (rowAffected1 == -1 && rowAffected2 == -1)
            return false;
        else
            return true;
    }

    public boolean updateTransaction(Context context, Transaction transaction, int[] attributeIds, int userId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.Transaction.COLUMN_NAME_ITEM_ID, transaction.getItem().getItemId());
        values.put(DbTable.Transaction.COLUMN_NAME_PRICE, transaction.getPrice());
        values.put(DbTable.Transaction.COLUMN_NAME_SHOP, transaction.getShop());
        values.put(DbTable.Transaction.COLUMN_NAME_DATE, DateManager.displayDate(transaction.getDate()));
        values.put(DbTable.Transaction.COLUMN_NAME_DONE, transaction.isHasBuy());
        values.put(DbTable.Transaction.COLUMN_NAME_PAYMENT_METHOD_ID, transaction.getPaymentMethodId());
        values.put(DbTable.Transaction.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.Transaction.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected = -1;
        try {
            rowAffected = db.update(DbTable.Transaction.TABLE_NAME, values,
                    DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + "=" + transaction.getId(), null);
        } catch(Exception e){
        }

        long rowAffected2 = -1;
        try {
            rowAffected2 = db.delete(DbTable.TransactionAttrRel.TABLE_NAME, DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + "=" + transaction.getId(), null);
        } catch(Exception e){
        }

        long rowAffected3;
        if (attributeIds != null && attributeIds.length > 0) {
            for (int i=0; i<attributeIds.length; i++) {
                values = new ContentValues();
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID, transaction.getId());
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID, attributeIds[i]);
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_UPDATE_USR, userId);
                values.put(DbTable.TransactionAttrRel.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
                try {
                    rowAffected3 = db.insert(DbTable.TransactionAttrRel.TABLE_NAME, "NULL", values);
                } catch (Exception e) {
                }
            }
        }

        db.close();
        mDbHelper.close();
        if (rowAffected == -1)
            return false;
        else
            return true;
    }

    public boolean updatePaymentMethod(Context context, PaymentMethod paymentMethod, int userId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTable.PaymentMethod.COLUMN_NAME_CUTOFF_DAY, paymentMethod.getCutOffDay());
        values.put(DbTable.PaymentMethod.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.PaymentMethod.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected1 = -1;
        try {
            rowAffected1 = db.update(DbTable.PaymentMethod.TABLE_NAME, values,
                    DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + "=" + paymentMethod.getPaymentMethodId(), null);
        } catch(Exception e){
        }

        values = new ContentValues();
        values.put(DbTable.DisplayText.COLUMN_NAME_TEXT, paymentMethod.getPaymentMethodName());
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_USR, userId);
        values.put(DbTable.DisplayText.COLUMN_NAME_UPDATE_DATE, DateManager.displayCurrentDateTime());
        long rowAffected2 = -1;
        try {
            rowAffected2 = db.update(DbTable.DisplayText.TABLE_NAME, values,
                    DbTable.DisplayText.COLUMN_NAME_TEXT_ID + "=" + paymentMethod.getPaymentMethodNameTextId(), null);
        } catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        if (rowAffected1 == -1 && rowAffected2 == -1)
            return false;
        else
            return true;
    }

    /* Delete */
    public boolean deleteTransaction(Context context, int transactionId, int userId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long rowAffected = -1;
        try {
            rowAffected = db.delete(DbTable.TransactionAttrRel.TABLE_NAME, DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + "=" + transactionId, null);
            rowAffected = db.delete(DbTable.Transaction.TABLE_NAME, DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + "=" + transactionId, null);
        } catch(Exception e){
            LogManager.logSystem(e.getMessage());
        }
        db.close();
        mDbHelper.close();

        if (rowAffected == -1)
            return false;
        else
            return true;
    }

    /* Gets */
    public Attribute getAttribute(Activity activity, int attributeId, int langId) {
        Attribute[] attributes = getAttributeList(activity, null, attributeId, langId);
        if (attributes != null && attributes.length > 0)
            return attributes[0];
        return null;
    }

    public Attribute[] getAttributeList(Activity activity, int attributeType, int langId) {
        int[] attributeTypes = {attributeType};
        return getAttributeList(activity, attributeTypes, -1, langId);
    }

    public Attribute[] getAttributeList(Activity activity, int[] attributeTypes, int langId) {
        return getAttributeList(activity, attributeTypes, -1, langId);
    }

    public Attribute[] getAttributeList(Activity activity, int[] attributeTypes, int attributeId, int langId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Attribute[] attributes = null;
        try {
            String sql = "Select " +
                    DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + ", " +
                    DbTable.Attribute.COLUMN_NAME_TYPE + ", " +
                    DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_TEXT_ID + ", " +
                    DbTable.Attribute.COLUMN_NAME_ORDER + ", " +
                    DbTable.DisplayText.COLUMN_NAME_LANG_ID + ", " +
                    DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    DbTable.Attribute.COLUMN_NAME_ENABLE + ", " +
                    DbTable.Attribute.COLUMN_NAME_ORDER + " " +
                    "From " + DbTable.Attribute.TABLE_NAME + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " " +
                    "On " + DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_TEXT_ID +
                        " = " + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And " + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " " +
                    "Where " + DbTable.Attribute.COLUMN_NAME_ENABLE + " = " + 1 + " ";
            if (attributeTypes != null && attributeTypes.length > 0) {
                String attributeTypesStr = "";
                for (int i=0; i<attributeTypes.length; i++) {
                    attributeTypesStr += attributeTypes[i] + ",";
                }
                attributeTypesStr = attributeTypesStr.substring(0, attributeTypesStr.length() - 1);
                sql += "And " + DbTable.Attribute.COLUMN_NAME_TYPE + " in (" + attributeTypesStr + ") ";
            }
            if (attributeId > 0) {
                sql += "And " + DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + " = " + attributeId + " ";
            }
            sql += "Order By " + DbTable.Attribute.COLUMN_NAME_TYPE + ", " + DbTable.Attribute.COLUMN_NAME_ORDER + ", " + DbTable.DisplayText.COLUMN_NAME_TEXT;

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null)
                attributes = new Attribute[cursor.getCount()];
                int i = 0;
                if (cursor.moveToFirst()) {
                    do {
                        attributes[i] = new Attribute(cursor.getInt(0), cursor.getInt(2), cursor.getString(5), ((cursor.getInt(6) == 1) ? true:false), cursor.getInt(7));
                        i++;
                    } while (cursor.moveToNext());
                }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return attributes;
    }

    public PaymentMethod getPaymentMethod(Activity activity, int paymentMethodId, int langId) {
        PaymentMethod[] paymentMethods = getPaymentMethodList(activity, paymentMethodId, langId);
        if (paymentMethods != null && paymentMethods.length > 0)
            return paymentMethods[0];
        return null;
    }

    public PaymentMethod[] getPaymentMethodList(Activity activity, int langId) {
        return getPaymentMethodList(activity, -1, langId);
    }

    public PaymentMethod[] getPaymentMethodList(Activity activity, int paymentMethodId, int langId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        PaymentMethod[] paymentMethods = null;
        try {
            String sql = "Select " +
                    DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + ", " +
                    DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_TEXT_ID + ", " +
                    DbTable.PaymentMethod.COLUMN_NAME_CUTOFF_DAY + ", " +
                    DbTable.DisplayText.COLUMN_NAME_LANG_ID + ", " +
                    DbTable.DisplayText.COLUMN_NAME_TEXT + " " +
                    "From " + DbTable.PaymentMethod.TABLE_NAME + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " " +
                    "On " + DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_TEXT_ID +
                    " = " + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And " + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " ";
            if (paymentMethodId > 0) {
                sql += "And " + DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + " = " + paymentMethodId + " ";
            }
            sql += "Order By " + DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + ", " + DbTable.DisplayText.COLUMN_NAME_TEXT;

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null)
                paymentMethods = new PaymentMethod[cursor.getCount()];
            int i = 0;
            if (cursor.moveToFirst()) {
                do {
                    paymentMethods[i] = new PaymentMethod(cursor.getInt(0), cursor.getInt(1), cursor.getString(4), cursor.getInt(2));
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return paymentMethods;
    }

    public Item[] getItemList(Activity activity, int attributeId, int langId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Item[] attributes = null;
        try {
            String sql = "Select " +
                    DbTable.Item.COLUMN_NAME_ITEM_ID + ", " +
                    DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + ", " +
                    DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    DbTable.Item.COLUMN_NAME_ENABLE + ", " +
                    DbTable.Item.COLUMN_NAME_ORDER + " " +
                    "From " + DbTable.Item.TABLE_NAME + " " +
                    "Inner Join " + DbTable.ItemAttrRel.TABLE_NAME + " " +
                    "On " + DbTable.Item.COLUMN_NAME_ITEM_ID +
                    " = " + DbTable.ItemAttrRel.COLUMN_NAME_ITEM_ID + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " " +
                    "On " + DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID +
                    " = " + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And " + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " " +
                    "Where " + DbTable.ItemAttrRel.COLUMN_NAME_ATTRIBUTE_ID + " = " + Integer.toString(attributeId) + " " +
                    "And " + DbTable.Item.COLUMN_NAME_ENABLE + " = " + 1 + " " +
                    "Order By " + DbTable.Item.COLUMN_NAME_ORDER + ", " + DbTable.DisplayText.COLUMN_NAME_TEXT;

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null)
                attributes = new Item[cursor.getCount()];
            int i = 0;
            if (cursor.moveToFirst()) {
                do {
                    attributes[i] = new Item(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), ((cursor.getInt(3) == 1) ? true:false), cursor.getInt(4));
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return attributes;
    }

    public Item getItem(Context context, int itemId, int langId) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Item item = null;
        try {
            String sql = "Select " +
                    DbTable.Item.COLUMN_NAME_ITEM_ID + ", " +
                    DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + ", " +
                    DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    DbTable.Item.COLUMN_NAME_ENABLE + ", " +
                    DbTable.Item.COLUMN_NAME_ORDER + " " +
                    "From " + DbTable.Item.TABLE_NAME + " " +
                    "Inner Join " + DbTable.ItemAttrRel.TABLE_NAME + " " +
                    "On " + DbTable.Item.COLUMN_NAME_ITEM_ID +
                    " = " + DbTable.ItemAttrRel.COLUMN_NAME_ITEM_ID + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " " +
                    "On " + DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID +
                    " = " + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And " + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " " +
                    "Where " + DbTable.Item.COLUMN_NAME_ITEM_ID + " = " + Integer.toString(itemId) + " " +
                    "And " + DbTable.Item.COLUMN_NAME_ENABLE + " = " + 1 + " ";

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null && cursor.moveToFirst()) {
                item = new Item(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), ((cursor.getInt(3) == 1) ? true:false), cursor.getInt(4));
            }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return item;
    }

    public Transaction getTransaction(Activity activity, int transactionId, int langId) {
        Transaction[] transactions = getTransactions(activity, transactionId, null, null, -1, langId);
        if (transactions != null && transactions.length > 0)
            return transactions[0];
        return null;
    }

    public Transaction[] getTransactions(Activity activity, int langId) {
        return getTransactions(activity, -1, null, null, -1, langId);
    }

    public Transaction[] getTransactions(Activity activity, Date startDate, Date endDate, int paymentMethodId, int langId) {
        return getTransactions(activity, -1, startDate, endDate, paymentMethodId, langId);
    }

    public Transaction[] getTransactions(Activity activity, int transactionId, Date startDate, Date endDate, int inputPaymentMethodId, int langId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        com.akproject.easybuy.model.Transaction[] result = null;
        try {
            String transactionSql = "";
            String attributeSql = "";
            String transactionFieldSql = "";
            String whereSql = "";
            String orderSql = "";

            transactionFieldSql = "Select " +
                    DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + ", " +
                    DbTable.Transaction.COLUMN_NAME_PRICE + ", " +
                    DbTable.Transaction.COLUMN_NAME_SHOP + ", " +
                    DbTable.Transaction.COLUMN_NAME_DATE + ", " +
                    DbTable.Transaction.COLUMN_NAME_DONE + ", " +
                    DbTable.Transaction.COLUMN_NAME_ITEM_ID + ", " +
                    DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + ", " +
                    "item_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    DbTable.Item.COLUMN_NAME_ENABLE + ", " +
                    DbTable.Transaction.COLUMN_NAME_BRAND + ", " +
                    DbTable.Transaction.COLUMN_NAME_QUANTITY + ", " +
                    DbTable.Item.COLUMN_NAME_ORDER + ", " +
                    DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + ", " +
                    "payment_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + " ";

            whereSql =
                    "From " + DbTable.Transaction.TABLE_NAME + " " +
                    "Inner Join " + DbTable.Item.TABLE_NAME + " " +
                    "On " + DbTable.Transaction.COLUMN_NAME_ITEM_ID + " = " + DbTable.Item.COLUMN_NAME_ITEM_ID + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " item_text " +
                    "On " + DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + " = item_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And item_text." + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " " +
                    "Left Join " + DbTable.PaymentMethod.TABLE_NAME + " " +
                    "On " + DbTable.Transaction.COLUMN_NAME_PAYMENT_METHOD_ID + " = " + DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_ID + " " +
                    "Left Join " + DbTable.DisplayText.TABLE_NAME + " payment_text " +
                    "On " + DbTable.PaymentMethod.COLUMN_NAME_PAYMENT_TEXT_ID + " = payment_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "And payment_text." + DbTable.DisplayText.COLUMN_NAME_LANG_ID  + " = " + Integer.toString(langId) + " " +
                    "Where 1=1 ";
            if (startDate != null && endDate != null) {
                whereSql += "And " + DbTable.Transaction.COLUMN_NAME_DATE + " >= '" + DateManager.displayDate(startDate) + "' " +
                        "And " + DbTable.Transaction.COLUMN_NAME_DATE + " < '" + DateManager.displayDate(endDate) + "' ";
            }
            if (transactionId > 0) {
                whereSql += "And " + DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + " = " + Integer.toString(transactionId) + " ";
            }
            if (inputPaymentMethodId > 0) {
                whereSql += "And " + DbTable.Transaction.COLUMN_NAME_PAYMENT_METHOD_ID + " = " + Integer.toString(inputPaymentMethodId) + " ";
            }
            orderSql += "Order By " + DbTable.Transaction.COLUMN_NAME_DATE + ", " + DbTable.Item.COLUMN_NAME_ORDER;

            transactionSql = transactionFieldSql + whereSql + orderSql;
            attributeSql = "Select " +
                    DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + ", " +
                    DbTable.Attribute.COLUMN_NAME_TYPE + ", " +
                    DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_TEXT_ID + ", " +
                    DbTable.Attribute.COLUMN_NAME_ORDER + ", " +
                    DbTable.DisplayText.COLUMN_NAME_LANG_ID + ", " +
                    DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    DbTable.Attribute.COLUMN_NAME_ENABLE + ", " +
                    DbTable.Attribute.COLUMN_NAME_ORDER + ", " +
                    DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + " " +
                    "From " + DbTable.TransactionAttrRel.TABLE_NAME + " " +
                    "Inner Join " + DbTable.Attribute.TABLE_NAME + " " +
                    "On " + DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID + " = " + DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + " " +
                    "Inner Join " + DbTable.DisplayText.TABLE_NAME + " " +
                    "On " + DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_TEXT_ID + " = " + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " " +
                    "Where " + DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + " In (" +
                    "   Select " + DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + " " +
                    whereSql +
                    ")";

            LogManager.logSystem(attributeSql);
            HashMap<Integer,ArrayList<Attribute>> transactionAttributeMap = new HashMap<Integer,ArrayList<Attribute>>();
            Cursor cursor = db.rawQuery(attributeSql, new String[]{});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int tmpTransactionId = cursor.getInt(8);
                    Attribute attribute = new Attribute(cursor.getInt(0), cursor.getInt(2), cursor.getString(5), ((cursor.getInt(6) == 1) ? true:false), cursor.getInt(7));

                    ArrayList<Attribute> attributeList;
                    if (transactionAttributeMap.containsKey(tmpTransactionId)) {
                        attributeList = transactionAttributeMap.get(tmpTransactionId);
                    } else {
                        attributeList = new ArrayList<Attribute>();
                    }
                    attributeList.add(attribute);
                    transactionAttributeMap.put(tmpTransactionId, attributeList);
                } while (cursor.moveToNext());
            }

            LogManager.logSystem(transactionSql);
            cursor = db.rawQuery(transactionSql, new String[]{});
            if (cursor != null)
                result = new com.akproject.easybuy.model.Transaction[cursor.getCount()];
            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    Item item = new Item(cursor.getInt(5), cursor.getInt(6), cursor.getString(7), ((cursor.getInt(8) == 1) ? true:false), cursor.getInt(11));
                    int id = cursor.getInt(0);
                    String brand = cursor.getString(9);
                    float quantity = cursor.getFloat(10);
                    float price = cursor.getFloat(1);
                    String shop = cursor.getString(2);
                    String dateStr = cursor.getString(3);
                    int paymentMethodId = cursor.getInt(12);
                    String paymentMethod = cursor.getString(13);
                    boolean hasBuy = (cursor.getInt(4) == 1) ? true : false;
                    Transaction transaction = new Transaction(id, item, brand, quantity, price, shop, DateManager.getDateFromString(dateStr), paymentMethodId, paymentMethod, hasBuy);
                    if (transactionAttributeMap.containsKey(id)) {
                        ArrayList<Attribute> attributeList = transactionAttributeMap.get(id);
                        Attribute[] attributes = new Attribute[attributeList.size()];
                        attributeList.toArray(attributes);
                        transaction.setAttributes(attributes);
                    }
                    result[i] = transaction;

                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return result;
    }

    public String[] getShopName(Activity activity, int itemId) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] result = {""};
        try {
            String sql = "";

            sql = "Select distinct " +
                    "Trim(" + DbTable.Transaction.COLUMN_NAME_SHOP + ") " +
                    "From " + DbTable.Transaction.TABLE_NAME + " " +
                    "Group By " + DbTable.Transaction.COLUMN_NAME_SHOP + " " +
                    "Order By Count(1) desc, " + DbTable.Transaction.COLUMN_NAME_SHOP;

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null && cursor.moveToFirst()) {
                result = new String[cursor.getCount()];
                int i = 0;
                if (cursor.moveToFirst()) {
                    do {
                        result[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        catch(Exception e){
        }
        db.close();
        mDbHelper.close();

        return result;
    }

    /* Export */
    public String getExport(Activity activity, SQLiteDatabase db1, String sql) {
        SQLiteDatabase db = null;
        if (activity != null) {
            DbHelper mDbHelper = new DbHelper(activity);
            db = mDbHelper.getWritableDatabase();
        } else if (db1 != null) {
            db = db1;
        }

        if (db != null) {
            LogManager.logSystem(sql);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{});
                if (cursor.moveToFirst()) {
                    do {
                        stringBuilder.append(cursor.getString(0));
                        stringBuilder.append("\r\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } catch (Exception e) {
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public boolean trancateTables(Activity activity) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        boolean success = true;
        try {
            String sql;
            sql = "DELETE FROM " + DbTable.TransactionAttrRel.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.Transaction.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.PaymentMethod.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.ItemAttrRel.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.Item.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.Attribute.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
            sql = "DELETE FROM " + DbTable.DisplayText.TABLE_NAME;
            LogManager.logSystem(sql);
            db.execSQL(sql);
        } catch (Exception e) {
            success = false;
        }
        db.close();
        mDbHelper.close();
        return success;
    }

    public void execSQL(Activity activity, String sql) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        LogManager.logSystem(sql);
        db.execSQL(sql);
    }

    public void migratePayment (Activity activity) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            String sql = "Select " +
                    DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + ", " + DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID + " " +
                    "From " + DbTable.TransactionAttrRel.TABLE_NAME + " " +
                    "Inner Join " + DbTable.Attribute.TABLE_NAME + " " +
                    "On " + DbTable.Attribute.COLUMN_NAME_ATTRIBUTE_ID + " = " + DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID + " " +
                    "Where " + DbTable.Attribute.COLUMN_NAME_TYPE + " = 3;";

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        LogManager.logSystem(cursor.getInt(0) + " " + cursor.getInt(1));

                        int transactionId = cursor.getInt(0);
                        int paymentMethodId = cursor.getInt(1);

                        // Replace Item ID in transactions
                        ContentValues values = new ContentValues();
                        values.put(DbTable.Transaction.COLUMN_NAME_PAYMENT_METHOD_ID, paymentMethodId);
                        long rowAffected = db.update(DbTable.Transaction.TABLE_NAME, values,
                                DbTable.Transaction.COLUMN_NAME_TRANSACTION_ID + "=" + transactionId, null);

                        // Delete Item Attribute
                        rowAffected = db.delete(DbTable.TransactionAttrRel.TABLE_NAME,
                                DbTable.TransactionAttrRel.COLUMN_NAME_TRANSACTION_ID + "=" + transactionId + " And " + DbTable.TransactionAttrRel.COLUMN_NAME_ATTRIBUTE_ID + "=" + paymentMethodId,
                                null);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

        } catch(Exception e){

        }
        db.close();
        mDbHelper.close();
    }

    public void mergeItem(Activity activity) {
        DbHelper mDbHelper = new DbHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            String sql = "Select " +
                    "def_item." + DbTable.Item.COLUMN_NAME_ITEM_ID + ", " +
                    "def_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + ", " +
                    "def_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + ", " +
                    "input_item." + DbTable.Item.COLUMN_NAME_ITEM_ID + ", " +
                    "input_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + ", " +
                    "input_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + " " +
            "From " + DbTable.DisplayText.TABLE_NAME + " def_text, " + DbTable.DisplayText.TABLE_NAME + " input_text, " +
                    DbTable.Item.TABLE_NAME + " def_item, " + DbTable.Item.TABLE_NAME + " input_item " +
            "Where def_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + " = input_text." + DbTable.DisplayText.COLUMN_NAME_TEXT + " " +
            "And def_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " = def_item." + DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + " " +
            "And input_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " = input_item." + DbTable.Item.COLUMN_NAME_ITEM_TEXT_ID + " " +
            "And def_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " <= 10000 " +
            "And input_text." + DbTable.DisplayText.COLUMN_NAME_TEXT_ID + " > 10000 " +
            "And def_item." + DbTable.Item.COLUMN_NAME_ITEM_ID + " <= 10000 " +
            "And input_item." + DbTable.Item.COLUMN_NAME_ITEM_ID + " > 10000";

            LogManager.logSystem(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        LogManager.logSystem(cursor.getInt(0) + " " + cursor.getInt(1) + " " + cursor.getString(2) + " " +
                                cursor.getInt(3) + " " + cursor.getInt(4) + " " + cursor.getString(5) + " ");

                        int defineItemID = cursor.getInt(0);
                        int inputItemID = cursor.getInt(3);

                        // Replace Item ID in transactions
                        ContentValues values = new ContentValues();
                        values.put(DbTable.Transaction.COLUMN_NAME_ITEM_ID, defineItemID);
                        long rowAffected = db.update(DbTable.Transaction.TABLE_NAME, values,
                                DbTable.Transaction.COLUMN_NAME_ITEM_ID + "=" + inputItemID, null);

                        // Delete Item Attribute
                        rowAffected = db.delete(DbTable.ItemAttrRel.TABLE_NAME, DbTable.ItemAttrRel.COLUMN_NAME_ITEM_ID + "=" + inputItemID, null);

                        // Delete Item
                        rowAffected = db.delete(DbTable.Item.TABLE_NAME, DbTable.Item.COLUMN_NAME_ITEM_ID + "=" + inputItemID, null);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

        } catch(Exception e){

        }
        db.close();
        mDbHelper.close();
    }
}
