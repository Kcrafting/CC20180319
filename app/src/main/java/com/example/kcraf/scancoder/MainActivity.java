package com.example.kcraf.scancoder;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是新建的一个碎片，由系统的新建碎片建立的，但是注意此碎片并没有使用
 */
//import org.w3c.dom.Text;
//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends ZJFActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String LoginName = "12345@";//用于判断用户的登陆状态
    private static boolean AdministratorLoginStats = false;//控制显示是否设置管理设置页面为选中状态
    private static MenuItem Gloitem = null;//设置一个全局的变量来设置菜单的选中状态
    public static Button Button_Save = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//重载的创建事件，尝试从之前的视图恢复
        super.onCreate(savedInstanceState);
        //
        Log.d("主活动:", "活动编号 onCreate->" + getTaskId());
        Log.d("主活动:", "活动编号 LoginName->" + LoginName);
        if (LoginName.equals("")) {//如果用户没有登陆
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            //如果已经登陆
            ActivityCollector.clearActivityExceptThis("MainActivity");
            //以上，清除除了本活动之外的其他活动
        }
        Button_Save = findViewById(R.id.app_bar_save);
        //
        setContentView(R.layout.activity_main);//切换主视图
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//获取工具栏的界面布局
        toolbar.setTitle("扫码");//设置标题栏显示的内容
        toolbar.setTitleTextColor(Color.parseColor("#00FF00") );
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF") );
        toolbar.setTitleTextColor(Color.parseColor("#FF0000") );
        setSupportActionBar(toolbar);//将工具栏配置到应用
        int ToolbarHeight=toolbar.getHeight();//获取到Toolbar的高度
        NavigationView navigationView1=(NavigationView)findViewById(R.id.nav_view);
        navigationView1.setTop(ToolbarHeight);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//获取浮动按钮的实例
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//设置单击的时间监听
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//获取到布局的实例
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(//
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();//同步状态

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//获取到导航的实例
        navigationView.setNavigationItemSelectedListener(this);//设置菜单上条目被选择的监听
        /**
         TextView ContentTitle=(TextView)findViewById(R.id.contentTitle);
         ContentTitle.setText("This is Test");
         //以下内容切换程序碎片
         Toolbar newtoolbar=(Toolbar)findViewById(R.id.toolbar);
         toolbar.setTitle("yes");*/ //修改工具栏的文字示例
        replaceFragment(new scan_layout_fragment(), false, R.id.WholeFragmentLayout);//默认登陆扫码界面

        //TextView textView1=(TextView)findViewById(R.id.r_username);
        //textView1.setText("aaa");


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {//取消工具栏上Overflow菜单的创建
        return false;
    }

    public void replaceFragment(android.support.v4.app.Fragment fragment, boolean IfAddToBackStack, int Control_R_id) {//用于替换主视图中的碎片
        //新加入参数IfAddToBackStack，有些时候，我们不希望退回后函数返回某个Fragment，那么我们可以传入false，不把Fragment加入返回栈
        //control_r_id要替换的碎片id,本项目中都是主视图中的R.id.WholeFragmentLayout但是为了方法的灵活性将参数单独提出
        Log.d("主活动:", "活动编号 replaceFragment()");
        FragmentManager fragmentManager = getSupportFragmentManager();//新建一个碎片管理器实例
        FragmentTransaction transaction = fragmentManager.beginTransaction();//新建一个碎片变换实例
        transaction.replace(Control_R_id, fragment);//替换碎片
        if (IfAddToBackStack) {
            //
            transaction.addToBackStack(null);//将碎片加入返回栈
        }
        transaction.commit();//替换之后必须提交
    }

    public void InputBox(Context context)//自定义输入框
    {
        LayoutInflater inflater = LayoutInflater.from(this);//新建布局渲染器
        final View InputView = inflater.inflate(R.layout.inputbox, null);//渲染布局
        final EditText InputName = (EditText) InputView.findViewById(R.id.Account);//获取到账号框
        final EditText InputPassword = (EditText) InputView.findViewById(R.id.Password);//获取密码输入框
        Log.d("主活动:", "活动编号 InputBox->获取文本成功");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);//构造类
        builder.setCancelable(false);
        //builder.setIcon()
        Log.d("主活动:", "活动编号 new AlertDialog.Builder(context)->成功");
        builder.setTitle("请输入管理员信息");
        builder.setView(InputView);//设置AlertDialog的视图
        builder.setPositiveButton("登陆",//设置登陆的响应事件
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击登录后校验密码
                        String password = InputPassword.getText().toString();
                        String username = InputName.getText().toString();
                        if (username.equals("") && password.equals("")) {
                            AdministratorLoginStats = true;//当登陆成功后设置登陆状态为已登录
                            Log.d("主活动:", "活动编号 管理员登陆->成功！");
                            //隐藏拍照的浮动按钮
                            FloatingActionButton FloatButton = (FloatingActionButton) findViewById(R.id.fab);//获取到浮动按钮的实例
                            FloatButton.setVisibility(View.INVISIBLE);//设置按钮不可见
                            replaceFragment(new setting_layout_fragment(), true, R.id.WholeFragmentLayout);
                            //以上登陆成功后，切换到设置界面
                            Gloitem.setCheckable(true);
                            Gloitem.setChecked(true);
                        } else {
                            AlertDialog.Builder Dialog = new AlertDialog.Builder(MainActivity.this);
                            Dialog.setTitle("提示");
                            Dialog.setMessage("请输入正确的账户和密码！");
                            Dialog.setCancelable(false);
                            Dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击后无动作
                                    Gloitem.setCheckable(false);
                                    Gloitem.setChecked(false);
                                }
                            });
                            Dialog.show();
                        }
                    }
                }
        );
        builder.setNegativeButton("取消",//设置取消的相应事件
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消后需要任何动作

                    }
                }
        );
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("主活动:", "活动编号 onStart()->" + getTaskId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("主活动:", "活动编号 onResume()->" + getTaskId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("主活动:", "活动编号 onPause()->" + getTaskId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("主活动:", "活动编号 onStop()->" + getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("主活动:", "活动编号 onDestroy()->" + getTaskId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {//一旦活动被系统回收就会执行此函数来保存状态
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {//应用重新恢复事件,
        super.onRestart();
        ActivityCollector.clearActivityExceptThis("MainActivity");
        //以上，清除除了本活动之外的其他活动
        if (!LoginName.equals("")) {//确定登陆成功
            TextView NavLoginName = (TextView) findViewById(R.id.Login_ShowName);//设置显示名称
            TextView NavLoginMessage = (TextView) findViewById(R.id.Login_ShowMessage);//设置显示信息
            ImageView NavIcon = (ImageView) findViewById(R.id.Login_Icon);//显示图标实例
            NavLoginName.setText(LoginName);//设置显示登陆名称
            NavLoginMessage.setText("It Shows");//设置显示信息
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//获取到布局的实例
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //drawer.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();//调用父类方法
        }
        drawer.bringToFront();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //弹出菜单，此方法添加条目到动作条 如果被允许的话
        getMenuInflater().inflate(R.menu.main, menu);
        //在点击菜单的弹出按钮的时候刷新用户信息，登陆名称，相关信息
        if (!LoginName.equals("")) {//确定登陆成功
            TextView NavLoginName = (TextView) findViewById(R.id.Login_ShowName);//设置显示名称
            TextView NavLoginMessage = (TextView) findViewById(R.id.Login_ShowMessage);//设置显示信息
            ImageView NavIcon = (ImageView) findViewById(R.id.Login_Icon);//显示图标实例
            NavLoginName.setText(LoginName);//设置显示登陆名称
            NavLoginMessage.setText("It Shows");//设置显示信息
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//当选项条目别选择时
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //这里抓取动作条目单击事件，动作条将会自动抓取单击事件在
        //只要你指定一个父活动在AndroidManifest.xml文件中
        int id = item.getItemId();//获取条目的id

        //noinspection SimplifiableIfStatement
        //不可省里的if声明
        /**
         if (id == R.id.action_settings) {
         return true;
         }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //处理导航的视图条目单击这里
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //处理相机点击事件
            Log.d("主活动:", "活动编号 nav_camera onClick()");
            FloatingActionButton FloatButton = (FloatingActionButton) findViewById(R.id.fab);//获取到浮动按钮的实例
            FloatButton.setVisibility(View.VISIBLE);//设置按钮可见
            //以下内容用于隐藏输入法
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);//获取到输入方法管理器
            inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);//隐藏输入框
            //隐藏完成
            replaceFragment(new scan_layout_fragment(), true, R.id.WholeFragmentLayout);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            replaceFragment(new layout_setting_app(), true, R.id.WholeFragmentLayout);
        } else if (id == R.id.nav_manage) {
            AdministratorLoginStats = false;//每次点击都会先清空登陆状态
            Gloitem = item;//赋值全局的item，便于再其他方法中修改状态
            InputBox(MainActivity.this);//当点击设置时。弹出输入账户密码的框
            //NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
            //navigationView.setCheckedItem(R.id.nav_camera);
            if (AdministratorLoginStats) {
                Log.d("主活动:", "活动编号 正在判定管理员登陆状态->");
                //注意这里永远都不会被执行，登陆成功的检测，是在点击了登陆框的登陆按钮之后进行的
            } else {//如果登陆不成功，不现实设置界面，toolbar的图标不显示选中状态
                Log.d("主活动:", "活动编号 正在设置Toolbar的选中状态");
                item.setCheckable(false);//先设置不可用
                item.setChecked(false);//再将选中状态设置为否
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //获取到布局的实例
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
