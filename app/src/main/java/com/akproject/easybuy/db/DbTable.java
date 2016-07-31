package com.akproject.easybuy.db;

import android.provider.BaseColumns;

/**
 * Created by Allan on 10/1/2016.
 */
public final class DbTable {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    public static final String EXPORT_SEP = ",";
    public static final String EXPORT_DOUBLE_QUOTE = "\"\"\"\"";
    public static final String EXPORT_CONCATE = " || ";
    public static final String EXPORT_SEP_CONCATE = " || \",\" || ";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DbTable() {}

    /* Inner class that defines the table contents */
    public static abstract class DisplayText implements BaseColumns {
        public static final String TABLE_NAME = "displayText_l";
        public static final String COLUMN_NAME_TEXT_ID = "dtx_textId";
        public static final String COLUMN_NAME_LANG_ID = "dtx_langId";
        public static final String COLUMN_NAME_TEXT = "dtx_text";
        public static final String COLUMN_NAME_UPDATE_USR = "dtx_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "dtx_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_TEXT_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LANG_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE + COMMA_SEP +
                        " PRIMARY KEY (" + COLUMN_NAME_TEXT_ID + "," + COLUMN_NAME_LANG_ID + ") " +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_TEXT_ID + COMMA_SEP +
                        COLUMN_NAME_LANG_ID + COMMA_SEP +
                        COLUMN_NAME_TEXT + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, '%s', %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_TEXT_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_LANG_ID + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_TEXT + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class Attribute implements BaseColumns {
        public static final String TABLE_NAME = "attribute_l";
        public static final String COLUMN_NAME_ATTRIBUTE_ID = "att_attributeId";
        public static final String COLUMN_NAME_TYPE = "att_type";
        public static final String COLUMN_NAME_ATTRIBUTE_TEXT_ID = "att_attributeTextId";
        public static final String COLUMN_NAME_ENABLE = "att_enable";
        public static final String COLUMN_NAME_ORDER = "att_order";
        public static final String COLUMN_NAME_UPDATE_USR = "att_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "att_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ATTRIBUTE_ID + INTEGER_TYPE + " PRIMARY KEY," +
                        COLUMN_NAME_TYPE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_TEXT_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ENABLE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ORDER + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_ATTRIBUTE_ID + COMMA_SEP +
                        COLUMN_NAME_TYPE + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_TEXT_ID + COMMA_SEP +
                        COLUMN_NAME_ENABLE + COMMA_SEP +
                        COLUMN_NAME_ORDER + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, %d, %d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_ATTRIBUTE_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_TYPE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ATTRIBUTE_TEXT_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ENABLE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ORDER + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class PaymentMethod implements BaseColumns {
        public static final String TABLE_NAME = "paymentMethod_l";
        public static final String COLUMN_NAME_PAYMENT_ID = "pmm_paymentMethodId";
        public static final String COLUMN_NAME_PAYMENT_TEXT_ID = "pmm_paymentTextId";
        public static final String COLUMN_NAME_CUTOFF_DAY = "pmm_cutoffDay";
        public static final String COLUMN_NAME_UPDATE_USR = "pmm_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "pmm_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_PAYMENT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                        COLUMN_NAME_PAYMENT_TEXT_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CUTOFF_DAY + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_PAYMENT_ID + COMMA_SEP +
                        COLUMN_NAME_PAYMENT_TEXT_ID + COMMA_SEP +
                        COLUMN_NAME_CUTOFF_DAY + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_PAYMENT_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_PAYMENT_TEXT_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_CUTOFF_DAY + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class Item implements BaseColumns {
        public static final String TABLE_NAME = "item_l";
        public static final String COLUMN_NAME_ITEM_ID = "itm_itemId";
        public static final String COLUMN_NAME_ITEM_TEXT_ID = "itm_itemTextId";
        public static final String COLUMN_NAME_ENABLE = "itm_enable";
        public static final String COLUMN_NAME_ORDER = "itm_order";
        public static final String COLUMN_NAME_UPDATE_USR = "itm_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "itm_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ITEM_ID + INTEGER_TYPE + " PRIMARY KEY," +
                        COLUMN_NAME_ITEM_TEXT_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ENABLE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ORDER + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_ITEM_ID + COMMA_SEP +
                        COLUMN_NAME_ITEM_TEXT_ID + COMMA_SEP +
                        COLUMN_NAME_ENABLE + COMMA_SEP +
                        COLUMN_NAME_ORDER + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, %d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_ITEM_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ITEM_TEXT_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ENABLE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ORDER + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class ItemAttrRel implements BaseColumns {
        public static final String TABLE_NAME = "itemAttrRel_l";
        public static final String COLUMN_NAME_ITEM_ID = "iar_temId";
        public static final String COLUMN_NAME_ATTRIBUTE_ID = "iar_attributeId";
        public static final String COLUMN_NAME_UPDATE_USR = "iar_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "iar_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ITEM_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE + COMMA_SEP +
                        " PRIMARY KEY (" + COLUMN_NAME_ITEM_ID + "," + COLUMN_NAME_ATTRIBUTE_ID + ") " +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_ITEM_ID + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_ID + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_ITEM_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ATTRIBUTE_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class Transaction implements BaseColumns {
        public static final String TABLE_NAME = "transaction_l";
        public static final String COLUMN_NAME_TRANSACTION_ID = "trx_transactionId";
        public static final String COLUMN_NAME_ITEM_ID = "trx_itemId";
        public static final String COLUMN_NAME_BRAND = "trx_brand";
        public static final String COLUMN_NAME_QUANTITY = "trx_quantity";
        public static final String COLUMN_NAME_PRICE = "trx_itemPrice";
        public static final String COLUMN_NAME_SHOP = "trx_shopName";
        public static final String COLUMN_NAME_DATE = "trx_transactionDate";
        public static final String COLUMN_NAME_PAYMENT_METHOD_ID = "trx_paymentMethodId";
        public static final String COLUMN_NAME_REMARK = "trx_remark";
        public static final String COLUMN_NAME_DONE = "trx_done";
        public static final String COLUMN_NAME_UPDATE_USR = "trx_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "trx_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_TRANSACTION_ID + INTEGER_TYPE + " PRIMARY KEY," +
                        COLUMN_NAME_ITEM_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BRAND + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_QUANTITY + REAL_TYPE + COMMA_SEP +
                        COLUMN_NAME_PRICE + REAL_TYPE + COMMA_SEP +
                        COLUMN_NAME_SHOP + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PAYMENT_METHOD_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DONE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_REMARK + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_TRANSACTION_ID + COMMA_SEP +
                        COLUMN_NAME_ITEM_ID + COMMA_SEP +
                        COLUMN_NAME_BRAND + COMMA_SEP +
                        COLUMN_NAME_QUANTITY + COMMA_SEP +
                        COLUMN_NAME_PRICE + COMMA_SEP +
                        COLUMN_NAME_SHOP + COMMA_SEP +
                        COLUMN_NAME_DATE + COMMA_SEP +
                        COLUMN_NAME_PAYMENT_METHOD_ID + COMMA_SEP +
                        COLUMN_NAME_DONE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, '%s', %.2f, %.2f, '%s', '%s', %d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_TRANSACTION_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ITEM_ID + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + "\"-\"" + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        //EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_BRAND + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        "0" + EXPORT_SEP_CONCATE + //COLUMN_NAME_QUANTITY + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_PRICE + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_SHOP + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_PAYMENT_METHOD_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_DONE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;

        public static final String SQL_EXPORT_38 =
                "SELECT " +
                        COLUMN_NAME_TRANSACTION_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ITEM_ID + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + "\"-\"" + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        //EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_BRAND + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        "0" + EXPORT_SEP_CONCATE + //COLUMN_NAME_QUANTITY + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_PRICE + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_SHOP + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_DONE + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

    public static abstract class TransactionAttrRel implements BaseColumns {
        public static final String TABLE_NAME = "trxAttrRel_l";
        public static final String COLUMN_NAME_TRANSACTION_ID = "tar_transactionId";
        public static final String COLUMN_NAME_ATTRIBUTE_ID = "tar_attributeId";
        public static final String COLUMN_NAME_UPDATE_USR = "tar_updateUsr";
        public static final String COLUMN_NAME_UPDATE_DATE = "tar_updateDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_TRANSACTION_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + TEXT_TYPE + COMMA_SEP +
                        " PRIMARY KEY (" + COLUMN_NAME_TRANSACTION_ID + "," + COLUMN_NAME_ATTRIBUTE_ID + ") " +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_INSERT =
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_NAME_TRANSACTION_ID + COMMA_SEP +
                        COLUMN_NAME_ATTRIBUTE_ID + COMMA_SEP +
                        COLUMN_NAME_UPDATE_USR + COMMA_SEP +
                        COLUMN_NAME_UPDATE_DATE + ") " +
                        "VALUES(%d, %d, %d, '%s')";

        public static final String SQL_EXPORT =
                "SELECT " +
                        COLUMN_NAME_TRANSACTION_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_ATTRIBUTE_ID + EXPORT_SEP_CONCATE +
                        COLUMN_NAME_UPDATE_USR + EXPORT_SEP_CONCATE +
                        EXPORT_DOUBLE_QUOTE + EXPORT_CONCATE + COLUMN_NAME_UPDATE_DATE + EXPORT_CONCATE + EXPORT_DOUBLE_QUOTE +
                        " FROM " + TABLE_NAME;
    }

}