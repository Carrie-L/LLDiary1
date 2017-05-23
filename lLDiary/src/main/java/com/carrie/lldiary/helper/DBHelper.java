package com.carrie.lldiary.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carrie.lldiary.App;
import com.carrie.lldiary.R;
import com.carrie.lldiary.dao.Ann;
import com.carrie.lldiary.dao.AnnDao;
import com.carrie.lldiary.dao.DaoSession;
import com.carrie.lldiary.dao.Diary;
import com.carrie.lldiary.dao.DiaryDao;
import com.carrie.lldiary.dao.EmojiUpdateTime;
import com.carrie.lldiary.dao.EmojiUpdateTimeDao;
import com.carrie.lldiary.dao.Money;
import com.carrie.lldiary.dao.MoneyAccount;
import com.carrie.lldiary.dao.MoneyAccountDao;
import com.carrie.lldiary.dao.MoneyClassify;
import com.carrie.lldiary.dao.MoneyClassifyDao;
import com.carrie.lldiary.dao.MoneyDao;
import com.carrie.lldiary.dao.Plan;
import com.carrie.lldiary.dao.PlanDao;
import com.carrie.lldiary.entity.Label;
import com.carrie.lldiary.entity.ListDialog;
import com.carrie.lldiary.entity.PlanTop;
import com.carrie.lldiary.utils.AppUtils;
import com.carrie.lldiary.utils.DateUtil;
import com.carrie.lldiary.utils.LogUtil;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class DBHelper {
    private static final String TAG = "DBHelper";

    private static DBHelper instance;
    private DaoSession mDaoSession;
    private SQLiteDatabase db;
    private MoneyDao moneyDao;
    private MoneyAccountDao moneyAccountDao;
    private MoneyClassifyDao moneyClassifyDao;
    private EmojiUpdateTimeDao emojiUTDao;
    private DiaryDao diaryDao;
    private PlanDao planDao;
    private AnnDao annDao;

    public static String[] classifyDescriptions = {"衣服", "三餐", "交通", "水果", "零食", "房租水电"};
    public static String[] classifyIcons = {"emoji_196", "emoji_213","emoji_traffic", "emoji_fruit", "emoji_icecream","emoji_house"};

    public static String[] accountDescriptions = {"现金", "微信钱包", "支付宝", "余额宝", "工商银行"};
    public static String[] accountIcons = {"icon_credit_2","icon_credit_18", "icon_credit_11","icon_credit_10", "icon_credit_13"};
    private Cursor cursor;


    public DBHelper() {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            instance.mDaoSession = App.getDaoSession(context);
            instance.db = App.db;
            instance.moneyDao = instance.mDaoSession.getMoneyDao();
            instance.moneyClassifyDao = instance.mDaoSession.getMoneyClassifyDao();
            instance.moneyAccountDao = instance.mDaoSession.getMoneyAccountDao();
            instance.emojiUTDao = instance.mDaoSession.getEmojiUpdateTimeDao();
            instance.planDao = instance.mDaoSession.getPlanDao();
            instance.annDao = instance.mDaoSession.getAnnDao();
            instance.diaryDao = instance.mDaoSession.getDiaryDao();
        }
        return instance;
    }


    //==============================日记Dairy===========================================
    public long insertDiary(Diary entity) {
        return diaryDao.insert(entity);
    }

    public void updateDiary(Diary entity) {
        diaryDao.update(entity);
    }

    public ArrayList<Diary> getDiaryAllList() {
        return (ArrayList<Diary>) diaryDao.queryBuilder().orderDesc(DiaryDao.Properties.UpdateDate).list();
    }

    public ArrayList<Diary> getDiaries(String label){
        return (ArrayList<Diary>)  diaryDao.queryBuilder().where(DiaryDao.Properties.Label.eq(label)).orderAsc(DiaryDao.Properties.UpdateDate).list();
    }

    public void deleteDiaryItem(Diary entity) {
        diaryDao.delete(entity);
    }


    //==============================标签===========================================
    public ArrayList<Label> getLabels() {
        ArrayList<Label> list=new ArrayList<>();
        Label label;
        Cursor cursor=db.rawQuery("select DISTINCT LABEL FROM DIARY",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                label=new Label(cursor.getString(0));
                list.add(label);
            }
            cursor.close();
        }
       return list;
    }

    //==============================便利贴===========================================
    //==============================计划表===========================================
    public ArrayList<Plan> getAllPlans() {
        return (ArrayList<Plan>) planDao.queryBuilder().orderDesc(PlanDao.Properties.Date).orderAsc(PlanDao.Properties.StartTime).list();
    }

    public ArrayList<Object> generatePlanLists(ArrayList<Object> lists) {
        PlanTop planTop;
        Plan plan = null;
        Cursor cursor2 = null;
        String date = "";
        int sum = 0;
        Cursor cursor = db.query(PlanDao.TABLENAME, null, null, null, null, null, " DATE DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                //Long id, String importantDegree, Integer icon, String content, boolean finish, boolean remind, String date, String startTime, String endTime
//              Long id=  cursor.getLong(cursor.getColumnIndex("_id"));
//                cursor.getString(cursor.getColumnIndex("CONTENT"));
//                cursor.getString(cursor.getColumnIndex("IMPORT_DEGREE"));
//                cursor.getInt(cursor.getColumnIndex("ICON"));
//                cursor.getInt(cursor.getColumnIndex("FINISH"));
//                cursor.getInt(cursor.getColumnIndex("REMIND"));
                date = cursor.getString(cursor.getColumnIndex("DATE"));
//                cursor.getString(cursor.getColumnIndex("START_TIME"));
//                cursor.getString(cursor.getColumnIndex("END_TIME"));
                ;
                ;

                plan = new Plan(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("IMPORTANT_DEGREE")), cursor.getInt(cursor.getColumnIndex("ICON")), cursor.getString(cursor.getColumnIndex("CONTENT")),
                        cursor.getInt(cursor.getColumnIndex("FINISH")) == 0 ? false : true, cursor.getInt(cursor.getColumnIndex("REMIND")) == 0 ? false : true, cursor.getString(cursor.getColumnIndex("DATE")), cursor.getString(cursor.getColumnIndex("START_TIME")), cursor.getString(cursor.getColumnIndex("END_TIME")));

//                cursor2 = db.rawQuery("select sum(FINISH) from PLAN WHERE DATE=? group by DATE", new String[]{date});
//                if (cursor2 != null) {
//                    sum = cursor2.getInt(cursor2.getColumnIndex("sum(FINISH)"));
//                }

                lists.add(plan);

            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (cursor2 != null) {
            cursor2.close();
        }
        return lists;
    }

    public void deletePlanItem(Plan plan) {
        planDao.delete(plan);
    }

    public void insertPlanItem(Plan plan) {
        planDao.insertOrReplace(plan);
    }

    public void insertPlans(ArrayList<Plan> plans) {
        planDao.insertOrReplaceInTx(plans);
    }

    public ArrayList<Plan> getPlanFinishCount(ArrayList<Plan> plans) {//WHERE DATE=?  \  new String[]{((Plan)plans.get(i)).getDate()
        int sum = 0;
        String date = "";
        int size = plans.size();
        Plan plan;
        Cursor cursor = null;
        for (int i = 0; i < size; i++) {
            cursor = db.rawQuery("select sum(FINISH) from PLAN  group by DATE", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    plan = new Plan();
                    sum = cursor.getInt(0);
                    plan.sum = sum;
                    plans.set(i, plan);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return plans;
    }

    public ArrayList<Object> getPlanTopCount(ArrayList<Object> plans) {
        int sum = 0;
        String date = "";
        int size = plans.size();
        PlanTop plan;
        Cursor cursor = null;
        for (int i = 0; i < size; i++) {
            cursor = db.rawQuery("select sum(FINISH) from PLAN WHERE DATE=? group by DATE", new String[]{((PlanTop) plans.get(i)).date});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    plan = (PlanTop) plans.get(i);
                    sum = cursor.getInt(0);
                    plan.sum = sum;
                    plans.set(i, plan);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return plans;
    }

    public void updatePlanState(Plan entity) {
//        ContentValues values=new ContentValues();
//        values.put("FINISH",1);
//        db.update(PlanDao.TABLENAME,values,"_id=?",new String[]{id+""});
        planDao.update(entity);
    }

    //select *,count(*) from PLAN group by DATE
    //==============================纪念日===========================================
    public ArrayList<Ann> getAnns(ArrayList<Ann> anns) {
        Ann entity;
        String content, person, date, relationship,icon;
        Long id;

        Cursor cursor = db
                .query("ANN", null, null, null, null, null, " DATE DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex("_id"));
                content = cursor.getString(cursor
                        .getColumnIndex("CONTENT"));
                person = cursor.getString(cursor
                        .getColumnIndex("PERSON"));
                relationship = cursor.getString(cursor
                        .getColumnIndex("RELATIONSHIP"));
                icon = cursor.getString(cursor.getColumnIndex("ICON"));
                date = cursor.getString(cursor.getColumnIndex("DATE"));

                entity = new Ann();
                entity.setId(id);
                entity.setContent(content);
                entity.setPerson(person);
                entity.setRelationship(relationship);
                entity.setIcon(icon);
                entity.setDate(date);
                entity.setRemind(cursor.getString(cursor.getColumnIndex("REMIND")));
                entity.durTime = DateUtil.getDurTime(date);

                AppUtils.logText(TAG, "DATE", date);
                AppUtils.logText(TAG, "DateUtil.getDurTime(date);", DateUtil.getDurTime(date));
                AppUtils.logText(TAG, "entity.durTime", entity.durTime);
                anns.add(entity);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return anns;
    }

    public long insertAnn(Ann ann) {
       return annDao.insert(ann);
    }

    public void updateAnn(Ann ann) {
        annDao.update(ann);
    }

    public void deleteAnnItem(Ann ann) {
        annDao.delete(ann);
    }
    //==============================机器人===========================================

    //==============================记账本===========================================
    public ArrayList<Money> getMoneyLists() {
        return (ArrayList<Money>) moneyDao.queryBuilder().orderDesc(MoneyDao.Properties.Date).list();
    }

    public ArrayList<Money> getTodayList(String date){
        LogUtil.i(TAG,"date="+date);
       return (ArrayList<Money>) moneyDao.queryBuilder().where(MoneyDao.Properties.Date.eq(date)).list();
    }

    public void getIncome(){

    }

    public void insertMoney(Money entity) {
        moneyDao.insert(entity);
    }

    public void insertOrReplace(Money entity) {
        moneyDao.insertOrReplace(entity);
    }

    public void deleteMoneyItem(Money entity) {
        moneyDao.delete(entity);
    }

    public void updateMoney(Money entity) {
        moneyDao.update(entity);
    }

    /**
     * 计算周报
     *
     * @param lastMonday
     */
    public void getMoneyWeek(String lastMonday) {
//        moneyDao.load
    }

    /**
     * 取得阶段数据（包括头尾）
     * @param date1 开始时间，包括
     * @param date2 结束时间，包括
     * @return
     */
    public ArrayList<Money> getMoneyList(String date1,String date2){
       return (ArrayList<Money>) moneyDao.queryBuilder().where(MoneyDao.Properties.Date.between(date1,date2)).orderDesc(MoneyDao.Properties.Date).list();
    }

    /**
     * 获取每个分类的消费情况
     * @param list
     * @return
     */
    public ArrayList<Money> getClassifyExpense(ArrayList<Money> list,String date1,String date2){
        Cursor cursor=db.rawQuery("select CLASSIFY,STATE,SUM(PRICE) AS SUM from MONEY where DATE between ? and ? and STATE!=0 GROUP BY CLASSIFY order by STATE DESC",new String[]{date1,date2});
        if(cursor!=null){
            Money money;
            list=AppUtils.initList(list);
            while (cursor.moveToNext()){
                money=new Money();
                money.setClassify(cursor.getString(0));
                money.setState(cursor.getInt(1));
                money.setExpense(cursor.getString(2));
                list.add(money);
            }
            cursor.close();
        }
        return list;
    }

    //==============================记账本类别===========================================
    public void insertInitClassify() {
        for (int i = 0; i < classifyDescriptions.length; i++) {
            MoneyClassify classify = new MoneyClassify();
            classify.setName(classifyDescriptions[i]);
            classify.setIcon(classifyIcons[i]);

            moneyClassifyDao.insert(classify);

        }
    }

    public ArrayList<ListDialog> getMoneyClassifyLists(ArrayList<ListDialog> lists) {
        Cursor cursor = db.query(MoneyClassifyDao.TABLENAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ListDialog listDialog = new ListDialog();
                listDialog.icon = cursor.getString(0);
                listDialog.name = cursor.getString(1);
                lists.add(listDialog);
                LogUtil.i(TAG, "name=" + name + "///" + listDialog.toString());
            }
        }
        AppUtils.logArrayListToString(lists);
        return lists;
    }

    public void insertMoneyClassify(MoneyClassify entity) {
        moneyClassifyDao.insertOrReplace(entity);
    }

    public void deleteClassifyItem(MoneyClassify entity) {
        moneyClassifyDao.delete(entity);
    }

    public void deleteClassifyItemByKey(String name) {
        moneyClassifyDao.deleteByKey(name);
    }

    public void updateMoneyClassify(MoneyClassify entity) {
        moneyClassifyDao.update(entity);
    }

    public String queryClassifyIcon(String name) {
        return moneyClassifyDao.load(name).getIcon();
    }

    public void insertClassifyAll(final ArrayList<ListDialog> entities) {
        final ContentValues cv = new ContentValues();
        moneyClassifyDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                int size = entities.size();
                for (int i = 0; i < size; i++) {
                    cv.put("ICON", entities.get(i).icon);
                    cv.put("NAME", entities.get(i).name);
                    db.insert(MoneyClassifyDao.TABLENAME, null, cv);
                }
            }
        });
    }

    public void deleteClassifyAll() {
        moneyClassifyDao.deleteAll();
    }

    public boolean isClassifyUnique(String name) {
        return moneyClassifyDao.load(name) == null;
    }

    /**
     * 得到第一行数据
     *
     * @return
     */
    public MoneyClassify getClassifyFirst() {
        return moneyClassifyDao.loadAll().get(0);
    }

    //==============================记账本账户= MoneyAccount==========================================
    public void insertInitAccount() {
        for (int i = 0; i < accountDescriptions.length; i++) {
            MoneyAccount entity = new MoneyAccount();
            entity.setName(accountDescriptions[i]);
            entity.setIcon(accountIcons[i]);
            moneyAccountDao.insertOrReplace(entity);
        }
    }

    public ArrayList<ListDialog> getMoneyAccountLists(ArrayList<ListDialog> lists) {
        Cursor cursor = db.query(MoneyAccountDao.TABLENAME, null, null, null, null, null, null);
        String icon;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ListDialog listDialog = new ListDialog();
                listDialog.icon = cursor.getString(0);
                listDialog.name = cursor.getString(1);
                listDialog.balance = cursor.getString(2);
                lists.add(listDialog);
                LogUtil.i(TAG, "///" + listDialog.toString());
            }
        }
        AppUtils.logArrayListToString(lists);
        return lists;
    }

    public void insertMoneyAccount(MoneyAccount entity) {
        moneyAccountDao.insert(entity);
    }

    public void deleteAccountItem(MoneyAccount entity) {
        moneyAccountDao.delete(entity);
    }

    public void deleteAccountItemByKey(String name) {
        moneyAccountDao.deleteByKey(name);
    }

    public void updateMoneyAccount(MoneyAccount entity) {
        moneyAccountDao.update(entity);
    }

    public String queryAccountIcon(String name) {
        return moneyAccountDao.load(name).getIcon();
    }

    public void insertAccountAll(final ArrayList<ListDialog> entities) {
        final ContentValues cv = new ContentValues();
        moneyAccountDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                int size = entities.size();
                for (int i = 0; i < size; i++) {
                    cv.put("ICON", entities.get(i).icon);
                    cv.put("NAME", entities.get(i).name);
                    cv.put("BALANCE", entities.get(i).balance);
                    db.insert(MoneyAccountDao.TABLENAME, null, cv);
                }
            }
        });
    }

    public MoneyAccount getAccountItem(String account) {
        return moneyAccountDao.load(account);
    }

    public void updateAccountBalanceItem(String account) {

    }

    public void deleteAccountAll() {
        moneyAccountDao.deleteAll();
    }

    public boolean isAccountUnique(String name) {
        return moneyAccountDao.load(name) == null;
    }

    /**
     * 得到第一行数据
     *
     * @return
     */
    public MoneyAccount getAccountFirst() {
        return moneyAccountDao.loadAll().get(0);
    }

    //==============================表情管理= EmojiUpdateTime==========================================

    /**
     * 查询已经下载的emoji，显示在横向列表中
     */
    public ArrayList<EmojiUpdateTime> queryDownedEmoji() {
        return (ArrayList<EmojiUpdateTime>) emojiUTDao.queryBuilder().where(EmojiUpdateTimeDao.Properties.Status.eq(1)).list();
    }

    /**
     * 查询已经下载的emoji，时间大于上次最新时间
     * <p> emoji.setFaceName(emojiPath);
     * <p>select * from EmojiUpdateTime where status=1 and insertTime>? order by insertTime asc</>
     */
    public ArrayList<EmojiUpdateTime> queryLastestDownedEmoji(String lastUpdateAt) {
        return (ArrayList<EmojiUpdateTime>) emojiUTDao.queryBuilder().where(EmojiUpdateTimeDao.Properties.Status.eq(1), EmojiUpdateTimeDao.Properties.InsertTime.gt(lastUpdateAt)).orderAsc(EmojiUpdateTimeDao.Properties.InsertTime).list();
    }

    public void insertEmojiUTItem(EmojiUpdateTime entity) {
        emojiUTDao.insert(entity);
    }

    public ArrayList<EmojiUpdateTime> getEmojiUTList() {
        return (ArrayList<EmojiUpdateTime>) emojiUTDao.loadAll();
    }

    public boolean isEmojiExisted(String objectId) {
        return emojiUTDao.load(objectId) == null;
    }

    public void updateEmojiUT(EmojiUpdateTime entity) {
        emojiUTDao.update(entity);
    }

    public void updateEmojiUTStatus(String objectId) {
        ContentValues cv = new ContentValues();
        cv.put("STATUS", 1);
        db.update("EMOJI_UPDATE_TIME", cv, "OBJECT_ID=?", new String[]{objectId});
    }

    /**
     * 检测是否已下载
     *
     * @param objectId
     * @return 1 true 已下载，0 false 未下载
     */
    public boolean isEmojiDowned(String objectId) {
        return emojiUTDao.load(objectId).getStatus() == 1;
    }

    /**
     * 获取最新的时间
     * select * from "+TABLE_NAME+" where updateAt=(select max(updateAt) from "+TABLE_NAME+")
     *
     * @return updateAt
     */
    public String getEmojiLastUpdateAt() {
//        return emojiUTDao.queryRawCreate("where UPDATE_AT=(select max(UPDATE_AT) from EMOJI_UPDATE_TIME)","").list().get(0).getUpdateAt();

        return getEmojiLastTime("UPDATE_AT");
    }

    /**
     * 获取最新的插入时间
     *
     * @return InsertTime
     */
    public String getEmojiLastInsertTime() {
//        return emojiUTDao.queryRawCreate("where INSERT_TIME=(select max(INSERT_TIME) from EMOJI_UPDATE_TIME)","").toString();

        return getEmojiLastTime("INSERT_TIME");
    }

    private String getEmojiLastTime(String column) {
        String time = "";
        cursor = db.rawQuery("select * from " + EmojiUpdateTimeDao.TABLENAME + " where " + column + " =(select max(" + column + ") from " + EmojiUpdateTimeDao.TABLENAME + ")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                time = cursor.getString(cursor.getColumnIndex(column));
            }
            cursor.close();
        }
        return time;
    }

}
