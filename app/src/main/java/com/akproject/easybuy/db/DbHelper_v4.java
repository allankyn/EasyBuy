package com.akproject.easybuy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akproject.easybuy.utility.LogManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Allan on 10/1/2016.
 */
public class DbHelper_v4 extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 23;
    public static final String DATABASE_NAME = "EasyBuy.db";
    private Context context;
    private String EXPORT_PREFIX = "db_backup";

    public DbHelper_v4(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbTable.DisplayText.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.ItemAttrRel.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Item.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Attribute.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Transaction.SQL_CREATE_ENTRIES);

        initializeTables(db, 0);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DbFunction dbFunction = new DbFunction();

        // Export backup
        StringBuffer exportStringBuilder = new StringBuffer();
        exportStringBuilder.append(DbTable.DisplayText.TABLE_NAME);
        exportStringBuilder.append('\n');
        exportStringBuilder.append(dbFunction.getExport(null, db, DbTable.DisplayText.SQL_EXPORT));
        exportStringBuilder.append(DbTable.Attribute.TABLE_NAME);
        exportStringBuilder.append('\n');
        exportStringBuilder.append(dbFunction.getExport(null, db, DbTable.Attribute.SQL_EXPORT));
        exportStringBuilder.append(DbTable.Item.TABLE_NAME);
        exportStringBuilder.append('\n');
        exportStringBuilder.append(dbFunction.getExport(null, db, DbTable.Item.SQL_EXPORT));
        exportStringBuilder.append(DbTable.ItemAttrRel.TABLE_NAME);
        exportStringBuilder.append('\n');
        exportStringBuilder.append(dbFunction.getExport(null, db, DbTable.ItemAttrRel.SQL_EXPORT));
        exportStringBuilder.append(DbTable.Transaction.TABLE_NAME);
        exportStringBuilder.append('\n');
        exportStringBuilder.append(dbFunction.getExport(null, db, DbTable.Transaction.SQL_EXPORT));

        String filename = EXPORT_PREFIX;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.context.openFileOutput(filename, Context.MODE_PRIVATE)));
            bufferedWriter.write(exportStringBuilder.toString());
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Drop tables
        db.execSQL(DbTable.DisplayText.SQL_DELETE_ENTRIES);
        db.execSQL(DbTable.ItemAttrRel.SQL_DELETE_ENTRIES);
        db.execSQL(DbTable.Item.SQL_DELETE_ENTRIES);
        db.execSQL(DbTable.Attribute.SQL_DELETE_ENTRIES);
        db.execSQL(DbTable.Transaction.SQL_DELETE_ENTRIES);

        // Create tables
        db.execSQL(DbTable.DisplayText.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.ItemAttrRel.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Item.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Attribute.SQL_CREATE_ENTRIES);
        db.execSQL(DbTable.Transaction.SQL_CREATE_ENTRIES);

        // Initialize or Import backup
        String strLine;
        int importCount = 0;
        try {
            int tableNum = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.context.openFileInput(filename)));
            while ((strLine = bufferedReader.readLine()) != null)   {
                if (strLine.equals(DbTable.DisplayText.TABLE_NAME)) {
                    tableNum = 1;
                }else if (strLine.equals(DbTable.Attribute.TABLE_NAME)) {
                    tableNum = 2;
                }else if (strLine.equals(DbTable.Item.TABLE_NAME)) {
                    tableNum = 3;
                }else if (strLine.equals(DbTable.ItemAttrRel.TABLE_NAME)) {
                    tableNum = 4;
                }else if (strLine.equals(DbTable.Transaction.TABLE_NAME)) {
                    tableNum = 5;
                }else {
                        importCount++;
                        String[] strLineParts = strLine.split(DbTable.EXPORT_SEP);
                        switch (tableNum) {
                        case 1:
                            if (strLineParts.length == 5) {
                                db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT,
                                        Integer.parseInt(strLineParts[0]),
                                        Integer.parseInt(strLineParts[1]),
                                        strLineParts[2].replace("\"", ""),
                                        Integer.parseInt(strLineParts[3]),
                                        strLineParts[4].replace("\"", "")));
                            }
                            break;
                        case 2:
                            if (strLineParts.length == 7) {
                                db.execSQL(String.format(DbTable.Attribute.SQL_INSERT,
                                        Integer.parseInt(strLineParts[0]),
                                        Integer.parseInt(strLineParts[1]),
                                        Integer.parseInt(strLineParts[2]),
                                        Integer.parseInt(strLineParts[3]),
                                        Integer.parseInt(strLineParts[4]),
                                        Integer.parseInt(strLineParts[5]),
                                        strLineParts[6].replace("\"", "")));
                            }
                            break;
                        case 3:
                            if (strLineParts.length == 6) {
                                db.execSQL(String.format(DbTable.Item.SQL_INSERT,
                                        Integer.parseInt(strLineParts[0]),
                                        Integer.parseInt(strLineParts[1]),
                                        Integer.parseInt(strLineParts[2]),
                                        Integer.parseInt(strLineParts[3]),
                                        Integer.parseInt(strLineParts[4]),
                                        strLineParts[5].replace("\"", "")));
                            }
                            break;
                        case 4:
                            if (strLineParts.length == 4) {
                                db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT,
                                        Integer.parseInt(strLineParts[0]),
                                        Integer.parseInt(strLineParts[1]),
                                        Integer.parseInt(strLineParts[2]),
                                        strLineParts[3].replace("\"", "")));
                            }
                            break;
                        case 5:
                            if (strLineParts.length == 10) {
                                db.execSQL(String.format(DbTable.Transaction.SQL_INSERT,
                                        Integer.parseInt(strLineParts[0]),
                                        Integer.parseInt(strLineParts[1]),
                                        strLineParts[2].replace("\"", ""),
                                        Float.parseFloat(strLineParts[3]),
                                        Float.parseFloat(strLineParts[4]),
                                        strLineParts[5].replace("\"", ""),
                                        strLineParts[6].replace("\"", ""),
                                        Integer.parseInt(strLineParts[7]),
                                        Integer.parseInt(strLineParts[8]),
                                        strLineParts[9].replace("\"", "")));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize if no import from backup
        initializeTables(db, oldVersion);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void initializeTables(SQLiteDatabase db, int oldVersion) {

        if (oldVersion <= 1) {
            LogManager.logSystem("Start Initialize V3");
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 1, 1, "主食", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 2, 1, "肉類", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 3, 1, "海鮮", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 4, 1, "蔬菜", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 5, 1, "水果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 6, 1, "罐頭", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 7, 1, "調味料", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 8, 1, "零食", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 9, 1, "飲品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 10, 1, "個人護理", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 11, 1, "醫藥", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 12, 1, "急救", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 13, 1, "家庭用品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 14, 1, "廚房用品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 15, 1, "浴室用品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 16, 1, "清潔用品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 17, 1, "文具", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 18, 1, "衣服", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 19, 1, "鞋", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 20, 1, "旅遊用品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 21, 1, "麵包", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 22, 1, "米", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 23, 1, "米紛", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 24, 1, "即食麵", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 25, 1, "杯麵", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 26, 1, "烏冬", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 27, 1, "拉麵", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 28, 1, "韓式麵", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 29, 1, "年糕", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 30, 1, "亞洲麵", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 31, 1, "意粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 32, 1, "通心粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 33, 1, "雞肉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 34, 1, "牛肉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 35, 1, "豬肉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 36, 1, "香腸", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 37, 1, "火腿", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 38, 1, "魚", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 39, 1, "蝦", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 40, 1, "蟹", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 41, 1, "生菜", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 42, 1, "白菜", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 43, 1, "菜心", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 44, 1, "芥蘭", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 45, 1, "蘋果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 46, 1, "橙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 47, 1, "啤梨", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 48, 1, "雪梨", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 49, 1, "蕉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 50, 1, "奇異果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 51, 1, "火龍果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 52, 1, "罐頭午餐肉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 53, 1, "罐頭粟米湯", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 54, 1, "罐頭菠蘿", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 55, 1, "罐頭沙甸魚", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 56, 1, "糖", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 57, 1, "鹽", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 58, 1, "生粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 59, 1, "胡椒粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 60, 1, "冰糖", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 61, 1, "蜜糖", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 62, 1, "糖漿", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 63, 1, "豉油", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 64, 1, "蠔油", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 65, 1, "雞湯", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 66, 1, "雞粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 67, 1, "油", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 68, 1, "麵粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 69, 1, "班㦸粉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 70, 1, "火鍋湯包", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 71, 1, "果醬", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 72, 1, "花生醬", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 73, 1, "煉奶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 74, 1, "朱古力", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 75, 1, "糖果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 76, 1, "蛋糕", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 77, 1, "餅乾", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 78, 1, "薯片", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 79, 1, "蝦片", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 80, 1, "粟米條", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 81, 1, "爆谷", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 82, 1, "紫菜", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 83, 1, "香口膠", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 84, 1, "魷魚絲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 85, 1, "蛋卷", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 86, 1, "肉乾", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 87, 1, "花生果仁", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 88, 1, "啫喱涼果", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 89, 1, "水", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 90, 1, "運動飲品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 91, 1, "汽水", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 92, 1, "果汁", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 93, 1, "無汽飲品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 94, 1, "牛奶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 95, 1, "豆奶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 96, 1, "咖啡", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 97, 1, "奶茶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 98, 1, "茶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 99, 1, "茶包", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 100, 1, "茶葉", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 101, 1, "啤酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 102, 1, "紅酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 103, 1, "白酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 104, 1, "香檳", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 105, 1, "果酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 106, 1, "甜酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 107, 1, "威士忌", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 108, 1, "烈酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 109, 1, "清酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 110, 1, "米酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 111, 1, "中國酒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 112, 1, "傷風感冒", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 113, 1, "化痰止咳", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 114, 1, "腸胃", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 115, 1, "止痛/消炎", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 116, 1, "眼睛", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 117, 1, "濕疹/皮膚", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 118, 1, "肌肉酸痛/按摩膏", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 119, 1, "冷熱墊/降溫貼", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 120, 1, "防蚊", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 121, 1, "急救用品/膠布", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 122, 1, "衛生卷紙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 123, 1, "盒裝紙巾", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 124, 1, "燈泡", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 125, 1, "保鮮紙/袋 ", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 126, 1, "微波爐紙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 127, 1, "錫紙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 128, 1, "牛油紙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 129, 1, "廚房紙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 130, 1, "濕紙巾", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 131, 1, "檯布", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 132, 1, "垃圾袋", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 133, 1, "洗頭水", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 134, 1, "沐浴露", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 135, 1, "漂白水", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 9999, 1, "其他", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 10000, 1, "其他", 1, "2016-01-01"));
            LogManager.logSystem("Initialize on DisplayText completes");
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 1, 1, 1, 1, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 2, 1, 2, 1, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 3, 1, 3, 1, 3, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 4, 1, 4, 1, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 5, 1, 5, 1, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 6, 1, 6, 1, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 7, 1, 7, 1, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 8, 1, 8, 1, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 9, 1, 9, 1, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 10, 1, 10, 0, 10, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 11, 1, 11, 1, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 12, 1, 12, 1, 12, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 13, 1, 13, 1, 13, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 14, 1, 14, 1, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 15, 1, 15, 1, 15, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 16, 1, 16, 1, 16, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 17, 1, 17, 1, 17, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 18, 1, 18, 1, 18, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 19, 1, 19, 1, 20, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 20, 1, 20, 1, 21, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 10000, 1, 9999, 1, 10000, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on Attribute completes");
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 1, 21, 1, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 2, 22, 1, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 3, 23, 1, 3, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 4, 24, 1, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 5, 25, 1, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 6, 26, 1, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 7, 27, 1, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 8, 28, 1, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 9, 29, 1, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 10, 30, 1, 10, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 11, 31, 1, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 12, 32, 1, 12, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 13, 33, 1, 13, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 14, 34, 1, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 15, 35, 1, 15, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 16, 36, 1, 16, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 17, 37, 1, 17, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 18, 38, 1, 18, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 19, 39, 1, 19, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 20, 40, 1, 20, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 21, 41, 1, 21, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 22, 42, 1, 22, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 23, 43, 1, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 24, 44, 1, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 25, 45, 1, 25, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 26, 46, 1, 26, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 27, 47, 1, 27, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 28, 48, 1, 28, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 29, 49, 1, 29, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 30, 50, 1, 30, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 31, 51, 1, 31, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 32, 52, 1, 32, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 33, 53, 1, 33, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 34, 54, 1, 34, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 35, 55, 1, 35, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 36, 56, 1, 36, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 37, 57, 1, 37, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 38, 58, 1, 38, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 39, 59, 1, 39, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 40, 60, 1, 40, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 41, 61, 1, 41, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 42, 62, 1, 42, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 43, 63, 1, 43, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 44, 64, 1, 44, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 45, 65, 1, 45, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 46, 66, 1, 46, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 47, 67, 1, 47, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 48, 68, 1, 48, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 49, 69, 1, 49, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 50, 70, 1, 50, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 51, 71, 1, 51, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 52, 72, 1, 52, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 53, 73, 1, 53, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 54, 74, 1, 54, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 55, 75, 1, 55, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 56, 76, 1, 56, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 57, 77, 1, 57, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 58, 78, 1, 58, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 59, 79, 1, 59, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 60, 80, 1, 60, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 61, 81, 1, 61, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 62, 82, 1, 62, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 63, 83, 1, 63, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 64, 84, 1, 64, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 65, 85, 1, 65, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 66, 86, 1, 66, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 67, 87, 1, 67, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 68, 88, 1, 68, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 69, 89, 1, 69, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 70, 90, 1, 70, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 71, 91, 1, 71, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 72, 92, 1, 72, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 73, 93, 1, 73, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 74, 94, 1, 74, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 75, 95, 1, 75, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 76, 96, 1, 76, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 77, 97, 1, 77, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 78, 98, 1, 78, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 79, 99, 1, 79, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 80, 100, 1, 80, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 81, 101, 1, 81, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 82, 102, 1, 82, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 83, 103, 1, 83, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 84, 104, 1, 84, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 85, 105, 1, 85, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 86, 106, 1, 86, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 87, 107, 1, 87, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 88, 108, 1, 88, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 89, 109, 1, 89, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 90, 110, 1, 90, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 91, 111, 1, 91, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 92, 112, 1, 92, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 93, 113, 1, 93, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 94, 114, 1, 94, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 95, 115, 1, 95, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 96, 116, 1, 96, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 97, 117, 1, 97, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 98, 118, 1, 98, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 99, 119, 1, 99, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 100, 120, 1, 100, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 101, 121, 1, 101, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 102, 122, 1, 102, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 103, 123, 1, 103, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 104, 124, 1, 104, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 105, 125, 1, 105, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 106, 126, 1, 106, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 107, 127, 1, 107, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 108, 128, 1, 108, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 109, 129, 1, 109, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 110, 130, 1, 110, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 111, 131, 1, 111, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 112, 132, 1, 112, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 113, 133, 1, 113, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 114, 134, 1, 114, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 115, 135, 1, 115, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 10000, 10000, 1, 10000, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on Item completes");
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 1, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 2, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 3, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 4, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 5, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 6, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 7, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 8, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 9, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 10, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 11, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 12, 1, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 13, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 14, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 15, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 16, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 17, 2, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 18, 3, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 19, 3, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 20, 3, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 21, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 22, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 23, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 24, 4, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 25, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 26, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 27, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 28, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 29, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 30, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 31, 5, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 32, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 33, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 34, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 35, 6, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 36, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 37, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 38, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 39, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 40, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 41, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 42, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 43, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 44, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 45, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 46, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 47, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 48, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 49, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 50, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 51, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 52, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 53, 7, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 54, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 55, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 56, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 57, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 58, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 59, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 60, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 61, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 62, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 63, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 64, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 65, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 66, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 67, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 68, 8, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 69, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 70, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 71, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 72, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 73, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 74, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 75, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 76, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 77, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 78, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 79, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 80, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 81, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 82, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 83, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 84, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 85, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 86, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 87, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 88, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 89, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 90, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 91, 9, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 92, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 93, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 94, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 95, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 96, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 97, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 98, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 99, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 100, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 101, 11, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 102, 13, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 103, 13, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 104, 13, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 105, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 106, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 107, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 108, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 109, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 110, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 111, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 112, 14, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 113, 15, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 114, 15, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 115, 16, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 10000, 10000, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on ItemAttrRel completes");
        }
        if (oldVersion <= 22) {
            LogManager.logSystem("Start Initialize V4");
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 136, 1, "褲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 137, 1, "電子產品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 138, 1, "用餐", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 139, 1, "交通", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 140, 1, "個人護理", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 141, 1, "娛樂", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 142, 1, "收入", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 143, 1, "T-Shirt", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 144, 1, "西褲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 145, 1, "長褲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 146, 1, "短褲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 147, 1, "相機", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 148, 1, "記憶卡", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 149, 1, "USB線", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 150, 1, "早餐", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 151, 1, "午餐", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 152, 1, "下午茶", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 153, 1, "晚餐", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 154, 1, "甜品", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 155, 1, "飲嘢", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 156, 1, "都會票", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 157, 1, "小巴", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 158, 1, "的士", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 159, 1, "租車", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 160, 1, "理髮", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 161, 1, "睇醫生", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 162, 1, "洗牙", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 163, 1, "睇戲", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 164, 1, "唱K", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 165, 1, "簽帳夾錢", 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.DisplayText.SQL_INSERT, 166, 1, "Claim錢", 1, "2016-01-01"));
            LogManager.logSystem("Initialize on DisplayText completes");
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 21, 1, 136, 1, 19, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 22, 1, 137, 1, 22, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 23, 1, 138, 1, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 24, 1, 139, 1, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 25, 1, 140, 1, 25, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 26, 1, 141, 1, 26, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Attribute.SQL_INSERT, 27, 1, 142, 1, 27, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on Attribute completes");
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 116, 143, 1,  116, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 117, 144, 1,  117, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 118, 145, 1,  118, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 119, 146, 1,  119, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 120, 147, 1,  120, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 121, 148, 1,  121, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 122, 149, 1,  122, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 123, 150, 1,  123, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 124, 151, 1,  124, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 125, 152, 1,  125, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 126, 153, 1,  126, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 127, 154, 1,  127, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 128, 155, 1,  128, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 129, 156, 1,  129, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 130, 157, 1,  130, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 131, 158, 1,  131, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 132, 159, 1,  132, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 133, 160, 1,  133, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 134, 161, 1,  134, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 135, 162, 1,  135, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 136, 163, 1,  136, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 137, 164, 1,  137, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 138, 165, 1,  138, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.Item.SQL_INSERT, 139, 166, 1,  139, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on Item completes");
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 116, 18, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 117, 21, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 118, 21, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 119, 21, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 120, 22, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 121, 22, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 122, 22, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 123, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 124, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 125, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 126, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 127, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 128, 23, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 129, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 130, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 131, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 132, 24, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 133, 25, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 134, 25, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 135, 25, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 136, 26, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 137, 26, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 138, 27, 1, "2016-01-01"));
            db.execSQL(String.format(DbTable.ItemAttrRel.SQL_INSERT, 139, 27, 1, "2016-01-01"));
            LogManager.logSystem("Initialize on ItemAttrRel completes");
        }
        LogManager.logSystem("Initialize on Transaction completes");
    }

}