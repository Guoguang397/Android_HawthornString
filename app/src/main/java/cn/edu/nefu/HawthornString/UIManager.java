package cn.edu.nefu.HawthornString;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class UIManager implements View.OnClickListener {

    private static UIManager Instance;

    private UIManager() {
    }

    private final ImageView[] sticks = new ImageView[6];
    private final int[] sticksId = {R.id.imageView_stick1, R.id.imageView_stick2, R.id.imageView_stick3, R.id.imageView_stick4, R.id.imageView_stick5, R.id.imageView_stick6};
    private final int[] levelImages = {R.drawable.hawthorn1, R.drawable.hawthorn2, R.drawable.hawthorn3, R.drawable.hawthorn4,
            R.drawable.hawthorn5, R.drawable.hawthorn6, R.drawable.hawthorn7, R.drawable.hawthorn8};
    private ImageView baseItem, pickUpItem, previewItem;
    private TextView tv_score;
    private int pixelsBetween, baseMarinBottom, pickupMarginBottom, previewMarginBottom;

    private RelativeLayout layout;

    /**
     * @return 返回UIManager实例
     */
    public static UIManager GetInstance() {
        MainActivity main = MainActivity.Instance;
        if (Instance == null) {
            Instance = new UIManager();
            for (int i = 0; i < 6; i++) {
                Instance.sticks[i] = main.findViewById(Instance.sticksId[i]);
                Instance.sticks[i].setOnClickListener(Instance);
            }
            Instance.layout = main.findViewById(R.id.relativeLayout);
            Instance.tv_score = main.findViewById(R.id.textView_score);
            Instance.baseItem = main.findViewById(R.id.hawthorn_base);
            Instance.pickUpItem = main.findViewById(R.id.hawthorn_pickup);
            Instance.previewItem = main.findViewById(R.id.hawthorn_preview);
        }
        return Instance;
    }

    /** 更新得分
     * @param score 得分
     */
    public void UpdateScore(int score) {
        tv_score.setText(String.valueOf(score));
    }

    /**
     * 移动山楂
     *
     * @param hawthornItems 要移动的山楂
     * @param col           移动到第几串上
     * @param row           最下方的果移动到几号位上
     */
    public void moveItems(List<HawthornItem> hawthornItems, int col, int row) {
        Log.e("Test",String.format("Move %d items to col %d, row %d",hawthornItems.size(),col,row));
        int r = row;
        for (HawthornItem hawthornItem : hawthornItems) {
            int curRow = r++;
            AnimationSet animationSet = new AnimationSet(true);
            RelativeLayout.LayoutParams stickFrom = (RelativeLayout.LayoutParams) sticks[hawthornItem.x].getLayoutParams();
            RelativeLayout.LayoutParams stickTo = (RelativeLayout.LayoutParams) sticks[col].getLayoutParams();
            Log.e("Index X", ""+hawthornItem.x);
            Log.e("to", ""+col);

            TranslateAnimation animation1 = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, stickTo.leftMargin - stickFrom.leftMargin,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0);
            animation1.setDuration(300);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) hawthornItem.imgView.getLayoutParams();
                    layoutParams.leftMargin = dp2px(-15);
                    layoutParams.removeRule(RelativeLayout.ALIGN_START);
                    layoutParams.addRule(RelativeLayout.ALIGN_START, sticksId[col]);
                    hawthornItem.imgView.setLayoutParams(layoutParams);

                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, pickupMarginBottom - baseMarinBottom - dp2px(55) * row);
                    animation2.setDuration(300);
                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutParams.bottomMargin = baseMarinBottom + dp2px(55) * curRow;
                            hawthornItem.imgView.setLayoutParams(layoutParams);
                            hawthornItem.x = col ;
                            hawthornItem.y = curRow;
                            operationCompleted = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    hawthornItem.imgView.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            hawthornItem.imgView.startAnimation(animation1);
        }
    }

    /**
     * 更改山楂等级
     *
     * @param hawthornItem 山楂对象
     * @param to           更改后的等级
     */
    public void changeItem(HawthornItem hawthornItem, int to) {
        hawthornItem.imgView.setImageResource(levelImages[to - 1]);
        hawthornItem.level = to;
    }

    /**
     * 创建山楂
     *
     * @param hawthornLevels 随机结果
     * @return 返回山楂对象
     */
    public List<HawthornItem> createItems(List<Integer> hawthornLevels) {
        int col = 0;
        List<HawthornItem> ret = new ArrayList<>();
        for (int level : hawthornLevels) {
            HawthornItem item = new HawthornItem();
            ImageView imgView = new ImageView(MainActivity.Instance);
            imgView.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(baseItem.getLayoutParams());
            layoutParams.addRule(RelativeLayout.ALIGN_START, sticksId[col]);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            layoutParams.bottomMargin = baseMarinBottom;
            layoutParams.height = dp2px(50);
            layoutParams.width = dp2px(50);
            layoutParams.setMarginStart(dp2px(-15));
            imgView.setLayoutParams(layoutParams);
            imgView.setImageResource(levelImages[level - 1]);
            layout.addView(imgView);
            layout.requestLayout();
            item.imgView = imgView;
            item.level = level;
            item.x = col++;
            item.y = 0;
            ret.add(item);
        }
        return ret;
    }

    /** 删除山楂
     * @param hawthornItem 山楂对象
     */
    public void removeItem(HawthornItem hawthornItem) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                HawthornItem item = (HawthornItem)msg.obj;
                item.imgView.setVisibility(View.GONE);
                layout.removeView(item.imgView);
                return false;
            }
        });
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.obj = hawthornItem;
            handler.sendMessage(msg);
        });
        thread.start();
    }

    /**
     * 预览山楂
     *
     * @param hawthornLevels 山楂对象
     */
    public void previewItems(List<HawthornItem> hawthornLevels) {
        for (HawthornItem item : hawthornLevels) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item.imgView.getLayoutParams();
            layoutParams.bottomMargin = previewMarginBottom;
            item.imgView.setLayoutParams(layoutParams);
            item.imgView.setImageAlpha(128);
            item.imgView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示创建的山楂（初始化时使用）
     *
     * @param hawthornItems 山楂对象
     */
    public void showItems(List<HawthornItem> hawthornItems) {
        for (HawthornItem item : hawthornItems) {
            item.imgView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 抓起山楂
     *
     * @param hawthornItems 山楂对象
     */
    public void pickItems(List<HawthornItem> hawthornItems) {
        for (int i = 0; i < hawthornItems.size(); i++) {
            HawthornItem item = hawthornItems.get(i);
            int pickMarginBottom = pickupMarginBottom + dp2px(50) * (hawthornItems.size() - i - 1);
            int curMarginBottom = baseMarinBottom + item.y*dp2px(55);
            TranslateAnimation animation1 = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, curMarginBottom-pickMarginBottom);
            animation1.setDuration(300);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item.imgView.getLayoutParams();
                    layoutParams.bottomMargin = pickMarginBottom;
                    item.imgView.setLayoutParams(layoutParams);
                    operationCompleted = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            item.imgView.startAnimation(animation1);
        }
        SoundManager.PlayPickupSound();
    }

    /** 放回去
     * @param hawthornItems 要放回去的山楂
     */
    public void putBackItem(List<HawthornItem> hawthornItems) {
        for (int i = 0; i < hawthornItems.size(); i++) {
            HawthornItem item = hawthornItems.get(i);
            int targetMarginBottom = baseMarinBottom + item.y*dp2px(55);
            int curMarginBottom = pickupMarginBottom + dp2px(50) * (hawthornItems.size() - i - 1);
            TranslateAnimation animation1 = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, curMarginBottom-targetMarginBottom);
            animation1.setDuration(300);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item.imgView.getLayoutParams();
                    layoutParams.bottomMargin = targetMarginBottom;
                    item.imgView.setLayoutParams(layoutParams);
                    operationCompleted = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            item.imgView.startAnimation(animation1);
        }
    }


    /**
     * 扔下预览的山楂
     *
     * @param hawthornItems 山楂对象
     * @param rowPosition   每根签子山楂落下的位置
     */
    public void dropItems(List<HawthornItem> hawthornItems, List<Integer> rowPosition) {
        for (int i = 0; i < hawthornItems.size(); i++) {
            HawthornItem item = hawthornItems.get(i);
            item.y = rowPosition.get(i);
            TranslateAnimation animation1 = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, previewMarginBottom - baseMarinBottom - dp2px(55) * rowPosition.get(i));
            animation1.setDuration(600);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item.imgView.getLayoutParams();
                    layoutParams.bottomMargin = baseMarinBottom + dp2px(55) * item.y;
                    item.imgView.setLayoutParams(layoutParams);
                    item.imgView.setImageAlpha(255);

                    //Test Code
//                    HawthornItem item2 = new HawthornItem();
//                    item2.imgView = MainActivity.Instance.findViewById(R.id.hawthorn_base21);
//                    pickItems(Arrays.asList(hawthornItems.get(1), item2));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            item.imgView.startAnimation(animation1);
        }
    }

    public void matchResolution() {
        int width = getScreenWidth();
        pixelsBetween = (int) (width / 7 / 1.5);
        for (ImageView stick : sticks) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) stick.getLayoutParams();
            layoutParams.setMarginStart(pixelsBetween);
            stick.setLayoutParams(layoutParams);
            stick.requestLayout();
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pickUpItem.getLayoutParams();
        layoutParams.bottomMargin = (int) (getScreenHeight() * 0.58);
        pickUpItem.setLayoutParams(layoutParams);
        pickUpItem.requestLayout();

        layoutParams = (RelativeLayout.LayoutParams) previewItem.getLayoutParams();
        layoutParams.bottomMargin = (int) (getScreenHeight() * 0.702);
        previewItem.setLayoutParams(layoutParams);
        previewItem.requestLayout();

        layoutParams = (RelativeLayout.LayoutParams) baseItem.getLayoutParams();
        baseMarinBottom = layoutParams.bottomMargin;

        layoutParams = (RelativeLayout.LayoutParams) pickUpItem.getLayoutParams();
        pickupMarginBottom = layoutParams.bottomMargin;

        layoutParams = (RelativeLayout.LayoutParams) previewItem.getLayoutParams();
        previewMarginBottom = layoutParams.bottomMargin;

        //For test purposes only.
//        HawthornItem item = new HawthornItem();
//        item.imgView = pickUpItem;
//        moveItems(Arrays.asList(item), 4, 3);
//        changeItem(item, 6);
//        List<HawthornItem> items = createItems(Arrays.asList(1, 2, 3, 4, 5, 6));
//        previewItems(items);
//        dropItems(items, Arrays.asList(0, 1, 2, 1, 0, 1));
    }

    public static int dp2px(int dpValue) {
        final float scale = MainActivity.Instance.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenWidth() {
        getAndroidScreenProperty();
        WindowManager wm = (WindowManager) MainActivity.Instance.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getScreenHeight() {
        getAndroidScreenProperty();
        WindowManager wm = (WindowManager) MainActivity.Instance.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public void getAndroidScreenProperty() {
        WindowManager wm = (WindowManager) MainActivity.Instance.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）

        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + dm.xdpi);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }

    private boolean pickedUp = false;
    List<HawthornItem> pickedHawthornItems = null;
    private int pickedId = -1;
    private boolean operationCompleted = true;

    private int getIndex(int[] arr, int data) {
        for(int i=0;i<arr.length;i++) {
            if(data == arr[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        if(!operationCompleted)return;
        int col = getIndex(sticksId, v.getId());
        if( col != -1) {
            operationCompleted = false;
            try {
                if(pickedUp) {
                    if(pickedId == col) {
                        putBackItem(pickedHawthornItems);
                    } else {
                        Log.e("Test", ""+pickedId+" "+col);
                        GameManager.getSingleton().moveItems(pickedId, col);
                    }
                    pickedUp = false;
                } else {
                    pickedHawthornItems = GameManager.getSingleton().pickItems(col);
                    if(pickedHawthornItems != null) {
                        pickedId = col;
                        pickItems(pickedHawthornItems);
                        pickedUp = true;
                    } else {
                        operationCompleted = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Test", ""+pickedUp+" "+pickedId);
        }
    }
}
