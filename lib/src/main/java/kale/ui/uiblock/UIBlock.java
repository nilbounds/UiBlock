package kale.ui.uiblock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import kale.ui.uiblock.iface.ActivityLifecycle;

/**
 * @author Jack Tony
 * @date 2015/6/15
 * 1.需要在界面销毁时把回调停止
 */
public abstract class UIBlock<T extends ContainUIBlockActivity> implements ActivityLifecycle{

    private View mRootView;

    private T mActivity;

    // Hint provided by the app that this UIBlock is currently visible to the user.
    boolean mUserVisibleHint = true;

    protected void attachActivity(T activity) {
        onAttach(activity);
        mRootView = ((Activity) activity).findViewById(getRootViewId());
        mRootView = resetRootView(mRootView, LayoutInflater.from(mRootView.getContext()));
        onBindViews();
        beforeSetViews();
        onSetViews();
    }

    /**
     * @return 得到UIBlock作用于的容器view的id
     */
    public abstract @IdRes int getRootViewId();

    protected void onAttach(T activity) {
        mActivity = activity;
    }

    /**
/     * 找到所有的views
     */
    protected abstract void onBindViews();

    /**
     * 在这里初始化设置view的各种资源，比如适配器或各种变量
     */
    protected void beforeSetViews() {}

    /**
     * 设置所有的view
     */
    protected abstract void onSetViews();

    /**
     * 重置根布局
     */
    public View resetRootView(View oldRootView, LayoutInflater inflater) {
        return oldRootView;
    }
    
    public View getRootView() {
        return mRootView;
    }

    protected T getActivity() {
        return mActivity;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        mUserVisibleHint = isVisibleToUser;
        onVisibleToUser(mUserVisibleHint);
    }

    /**
     * @return The current value of the user-visible hint on this fragment.
     * @see #setUserVisibleHint(boolean)
     */
    public boolean isVisibleToUser() {
        return mUserVisibleHint;
    }

    public void onVisibleToUser(boolean isVisible) {}
    
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {}

    public void onRestoreInstanceState(Bundle savedInstanceState) {}

    public void onStart() {}

    public void onResume() {}

    public void onPause() {}

    public void onStop() {}

    public void onRestart() {}

    public void onDestroy() {
        mActivity = null;
        mRootView = null;
    }

    /**
     * @return true if you want to shield back key
     */
    protected boolean onBackPressed() {
        return false;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    protected final <E extends View> E getView(int id) {
        try {
            return (E) mRootView.findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(UIBlock.class.getSimpleName(), "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }
}