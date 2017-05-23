package com.carrie.lldiary.utils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.carrie.lldiary.R;
import com.carrie.lldiary.base.BasePage;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
/**
 * 页面管理工具
 * @author lu_xiaol
 *
 */
public class PageManager {
	//********************单例模式**********************//
	public static PageManager instance;
	private PageManager(){}
	/**
	 * 获取页面管理工具实例
	 * @return
	 */
	public static PageManager getInstance(){
		if(instance == null){
			instance = new PageManager();
		}
		return instance;
	}
	//********************单例模式**********************//
	
	private FrameLayout mainPage;	// mainactivity中展示页面的布局
	/**
	 * 设置显示页面
	 * @param mainPage
	 */
	public void setMainPage(FrameLayout mainPage) {
		this.mainPage = mainPage;
	}
	/**
	 * 缓存器，记录已经展示了的页面
	 */
	private Map<String,BasePage> viewCache = new HashMap<String, BasePage>();
	private BasePage currentUI;	// 当前正在展示的页面
	
	/**
	 * 获取当前正在展示的页面
	 * @return
	 */
	public BasePage getCurrentUI(){
		return currentUI;
	}
	
	private LinkedList<String> HISTORY = new LinkedList<String>();	// 记录历史UI的线性表，作用是方便操作返回键
	
	/**
	 * 更新当前展示的页面
	 * @param targetClazz	将要打开的页面de字节码
	 */
	public void changeUI(Class<? extends BasePage> targetClazz){
		//判断当前和目标是否相同
		if(currentUI != null && currentUI.getClass().equals(targetClazz)){
			return;
		}
		BasePage targetUI = null;
		String key = targetClazz.getSimpleName();	// 获取将要打开的页面的名称，作为存入页面缓存器viewCache中的唯一标识
		if(viewCache.containsKey(key)){
			// 如果要打开的页面已经被创建过了，复用
			targetUI = viewCache.get(key);
		}else{
			// 没有创建过，创建,并且将它保存到缓存器中去
			try {
				Constructor<? extends BasePage> constructor = targetClazz.getConstructor(Context.class);	// 获取构造器,接受一个Context型的参数
				targetUI = constructor.newInstance(getContext());
				viewCache.put(key, targetUI);	// 将新创建的UI实例，存入缓存器中
			} catch (Exception e) {
				e.printStackTrace();
//				throw new RuntimeException("constructor new instance error");	// 抛出构造器实例化错误
			}
		}
		mainPage.removeAllViews();	// 清除上一个页面
		View child = targetUI.getRootView();
		mainPage.addView(child);	// 将当前页面加载到主页面上去，展示出来
		
		currentUI = targetUI;		// 记录为正在展示的页面
		HISTORY.addFirst(key); 		// 将当前界面的classname压入栈顶
	}
	
	/**
	 * 更新当前展示的页面，两个页面之间传递参数
	 * @param targetClazz	将要打开的页面de字节码
	 * @param bundle	将要传递的参数
	 */
	public void changeUI(Class<? extends BasePage> targetClazz,Bundle bundle){
		//判断当前和目标是否相同
		if(currentUI != null && currentUI.getClass().equals(targetClazz)){
			return;
		}
		BasePage targetUI = null;
		String key = targetClazz.getSimpleName();	// 获取将要打开的页面的名称，作为存入页面缓存器viewCache中的唯一标识
		if(viewCache.containsKey(key)){
			// 如果要打开的页面已经被创建过了，复用
			targetUI = viewCache.get(key);
		}else{
			// 没有创建过，创建,并且将它保存到缓存器中去
			try {
				Constructor<? extends BasePage> constructor = targetClazz.getConstructor(Context.class);	// 获取构造器,接受一个Context型的参数
				targetUI = constructor.newInstance(getContext());
				viewCache.put(key, targetUI);	// 将新创建的UI实例，存入缓存器中
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");	// 抛出构造器实例化错误
			}
		}
		/************************************处理传递信息*************************/
		if(targetUI != null){
			targetUI.setBundle(bundle);
		}
		/************************************处理传递信息*************************/	
		
		if(currentUI != null){
			currentUI.onPause();	
		}
		mainPage.removeAllViews();	// 清除上一个页面
		View child = targetUI.getRootView();
		mainPage.addView(child);	// 将当前页面加载到主页面上去，展示出来
		
		targetUI.onResume(); 		// 调用页面获取焦点时的方法
		currentUI = targetUI;		// 记录为正在展示的页面
		HISTORY.addFirst(key); 		// 将当前界面的classname压入栈顶
	}
	
	
	
	/**
	 * 返回事件,,修改系统的返回按钮
	 * @return
	 */
	public boolean goBack(){
		if(HISTORY.size() >0){
			if(HISTORY.size() == 1){
				return false;
			}
			HISTORY.removeFirst();	// 删除栈顶
			if(HISTORY.size()>0){
				String targetKey = HISTORY.getFirst();
				BasePage targetUI = viewCache.get(targetKey);
				if(targetUI != null){
					currentUI.onPause();
					mainPage.removeAllViews();
					mainPage.addView(targetUI.getRootView());
					targetUI.onResume();
					currentUI = targetUI;
				}
			}
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 获取context
	 * @return
	 */
	private Context getContext(){
		return mainPage.getContext();
	}
}
