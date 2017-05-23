package com.example;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class LLGreenGenerator {

    public static void main(String[] args) throws Exception {
        System.out.println("main");

        Schema schema=new Schema(1,"com.carrie.lldiary.dao");
        addDiary(schema);
        addPlan(schema);
        addMoney(schema);
        addMoneyClassify(schema);
        addMoneyAccount(schema);
        addEmojiUpdateTime(schema);
        addAnn(schema);
        addMemo(schema);
        schema.enableKeepSectionsByDefault();
        new DaoGenerator().generateAll(schema,"../LLDiary1/lLDiary/src/main/java");//I:\Develop\AndroidStudioWorkspace\LLDiary1\lLDiary\src\main\java
    }

    private static void addDiary(Schema schema){
        Entity entity=schema.addEntity("Diary");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("title");
        entity.addStringProperty("content").notNull();
        entity.addStringProperty("CreateDate").notNull();
        entity.addStringProperty("UpdateDate").notNull();
        entity.addStringProperty("weather");
        entity.addStringProperty("mood");
        entity.addStringProperty("bg");
        entity.addIntProperty("textSize");
        entity.addIntProperty("textColor");
        entity.addStringProperty("label");
    }

    /**
     * 计划表
     * @param schema
     */
    private static void addPlan(Schema schema){
        Entity entity=schema.addEntity("Plan");
        entity.addIdProperty();
        entity.addStringProperty("importantDegree");
        entity.addIntProperty("icon");
        entity.addStringProperty("content").notNull();
        entity.addBooleanProperty("finish").notNull();
        entity.addBooleanProperty("remind").notNull();
        entity.addStringProperty("date").notNull();
        entity.addStringProperty("startTime");
        entity.addStringProperty("endTime");

        entity.implementsSerializable();
    }

    /**
     * 纪念日
     * @param schema
     */
    private static void addAnn(Schema schema){
        Entity entity=schema.addEntity("Ann");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("content");
        entity.addStringProperty("person");
        entity.addStringProperty("relationship");//与“我”的关系
        entity.addStringProperty("icon");
        entity.addStringProperty("date");
        entity.addStringProperty("remind");//1 提醒，0则不提醒
        entity.addStringProperty("remark");//1 提醒，0则不提醒
    }

    /**
     * 记账本
     * @param schema
     */
    private static void addMoney(Schema schema){
        Entity entity=schema.addEntity("Money");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("classifyIcon").notNull();
        entity.addStringProperty("accountIcon").notNull();
        entity.addStringProperty("classify").notNull();
        entity.addStringProperty("account").notNull();
        entity.addStringProperty("content").notNull();
        entity.addStringProperty("income");
        entity.addStringProperty("expense");
        entity.addStringProperty("price");
        entity.addIntProperty("state").notNull();
        entity.addStringProperty("date").notNull();
        entity.implementsSerializable();
    }

    /**
     * 记账本账户
     */
    private static void addMoneyAccount(Schema schema){
        Entity entity=schema.addEntity("MoneyAccount");
        entity.addStringProperty("icon");
        entity.addStringProperty("name").primaryKey();
        entity.addStringProperty("balance");
    }

    private static void addMoneyClassify(Schema schema){
        Entity entity=schema.addEntity("MoneyClassify");
        entity.addStringProperty("icon");
        entity.addStringProperty("name").primaryKey();
    }

    private static void addEmojiUpdateTime(Schema schema){
        Entity entity=schema.addEntity("EmojiUpdateTime");
        entity.addStringProperty("objectId").primaryKey();
        entity.addStringProperty("url");
        entity.addStringProperty("path");
        entity.addStringProperty("name");
        entity.addIntProperty("status");
        entity.addStringProperty("updateAt");
        entity.addStringProperty("insertTime");
    }

    private static void addMemo(Schema schema){
        Entity entity=schema.addEntity("Memo");
        entity.addStringProperty("content");
        entity.addStringProperty("date");
        entity.addStringProperty("bg");
    }
}
