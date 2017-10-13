package com.example.asus.gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private int images[] = {R.mipmap.xk1,R.mipmap.xk2,R.mipmap.xk3,R.mipmap.xk4,R.mipmap.xk5,R.mipmap.xk6,};
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                width*5/10,
                height*6/20);

        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        viewPager.setLayoutParams(params);
        viewPager.setAdapter(new ViewPagerAdapter());

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

        viewPager.setOffscreenPageLimit(2); //设置预加载数量
        viewPager.setPageMargin(100);//设置每页之间的左右间隔
        viewPager.setClipChildren(false); //用来定义他的子控件是否要在他应有的边界内进行绘制
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());//设置ViewPager切换效果，即实现画廊效果

        relativeLayout.setClipChildren(false);

    }
    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(images[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }

    //实现的原理是，在当前显示页面放大至原来的MAX_SCALE     其他页面才是正常的的大小MIN_SCALE
    //设置切换动画
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer{
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 1.0f;//0.85f

        @Override
        public void transformPage(View page, float position) {
            //setScaleY只支持api11以上
            if (position<=1){
                float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);
                page.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                if(position>0){
                    page.setTranslationX(-scaleFactor*2);
                }else if(position<0){
                    page.setTranslationX(scaleFactor*2);
                }
                page.setScaleY(scaleFactor);
            }else {
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            }
        }
    }
}