
# Android-UI是一款Android开发中常用的UI库，目前只包括手势键盘(GestureView)，后续会添加其他。

## 1、集成
## 2、GestureView
### 介绍
Gesture是一款手势识别视图，9个原型触碰区分别代表数字1~9，触碰动作结束时会以数字字符串的形式回调结果。
开发者可自定义识别成功和失败时图形颜色、间距、密码等。

AndroidWidgetLib/gesture.gif
![](/AndroidWidgetLib/gesture.gif)
![avatar](/AndroidWidgetLib/gesture.gif)

https://github.com/mingliangYan/Android-UI/blob/117ae00c43571c295cd4306d61fc38a7dcd710e6/AndroidWidgetLib/gesture.gif
![image](https://github.com/mingliangYan/Android-UI/blob/117ae00c43571c295cd4306d61fc38a7dcd710e6/AndroidWidgetLib/gesture.gif)
[![Watch the video]https://github.com/mingliangYan/Android-UI/blob/117ae00c43571c295cd4306d61fc38a7dcd710e6/AndroidWidgetLib/gesture.gif)](https://github.com/mingliangYan/Android-UI/blob/117ae00c43571c295cd4306d61fc38a7dcd710e6/AndroidWidgetLib/gesture.gif)
**注意** 手势滑动区不可重复，即手势密码不可重复，如12369871，识别出来的结果将会去除重复数字1，即1236987
### 使用
  **在资源文件添加GestureView**

     <com.yml.ui.widget.gesture.GestureView
         android:layout_width="match_parent"
         android:layout_height="500dp"
         android:padding="20dp"
        
         <!--圆形区默认颜色-->
         app:default_color="@android:color/black"
         <!--圆形区触碰后颜色-->
         app:selected_color="@android:color/holo_green_light"
         <!--圆形区识别错误颜色-->
         app:error_color="@android:color/holo_orange_light"
         <!--密码-->
         app:password="12369"
         <!--圆形区间距-->
         app:space="40dp"
         <!--手势识别时间:ms-->
         app:checkout_time="2000"/>
** 添加识别结果回调**

         gestureView.setResultListener(new OnResultListener() {
             @Override
             public void onError(String result) {
                 Toast.makeText(MainActivity.this, "input error: " + result, Toast.LENGTH_SHORT).show();
             }

             @Override
            public void onSuccess(String result) {
                 Toast.makeText(MainActivity.this, "input success: " + result, Toast.LENGTH_SHORT).show();
             }
         });

## 3、ViewPagerPlus
### 介绍
ViewPagerPlus支持ViewPager中元素轮播，页面缩放功能。
