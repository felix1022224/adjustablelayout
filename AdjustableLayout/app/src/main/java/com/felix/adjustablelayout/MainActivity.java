package com.felix.adjustablelayout;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity {

    //fragment的适配器
    private MainTabFragmentAdapter mainTabFragmentAdapter;

    //viewpager
    private ViewPager mViewPager;

    //AppBarLayout
    private AppBarLayout mAppBarLayout;

    //顶部HeaderLayout
    private LinearLayout headerLayout;

    private SmartTabLayout mTabs;

    //是否隐藏了头部
    private boolean isHideHeaderLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //初始化方法
    private void init(){
        mTabs = (SmartTabLayout) findViewById(R.id.tabs);
        mainTabFragmentAdapter = new MainTabFragmentAdapter(getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mainTabFragmentAdapter);
        mTabs.setViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(mainTabFragmentAdapter.getCount());
        headerLayout = (LinearLayout) findViewById(R.id.ll_header_layout);
        initAppBarLayout();
    }

    // 初始化AppBarLayout
    private void initAppBarLayout(){
        LayoutTransition mTransition = new LayoutTransition();
        /**
         * 添加View时过渡动画效果
         */
        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "translationY", 0, 1.f).
                setDuration(mTransition.getDuration(LayoutTransition.APPEARING));
        mTransition.setAnimator(LayoutTransition.APPEARING, addAnimator);

        //header layout height
        final int headerHeight = getResources().getDimensionPixelOffset(R.dimen.header_height);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.setLayoutTransition(mTransition);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                if ( verticalOffset >= headerHeight ){
                    isHideHeaderLayout = true;
                    //当偏移量超过顶部layout的高度时，我们认为他已经完全移动出屏幕了
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) headerLayout.getLayoutParams();
                            mParams.setScrollFlags(0);
                            headerLayout.setLayoutParams(mParams);
                            headerLayout.setVisibility(View.GONE);
                        }
                    },100);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ){
            //监听返回键
            if ( isHideHeaderLayout ){
                isHideHeaderLayout = false;
                ((MainTabFragment)mainTabFragmentAdapter.getFragments().get(0)).getRvList().scrollToPosition(0);
                headerLayout.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) headerLayout.getLayoutParams();
                        mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|
                                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                        headerLayout.setLayoutParams(mParams);
                    }
                },300);
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
