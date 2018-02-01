package com.example.kcraf.scancoder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
//import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 * 一个提供邮箱地址/密码登陆的界面
 * 用于程序的登陆，如果从界面点击登陆，那么会判断用户是否已登录（由变量MainActivity.LoginName进行判定）
 * 一旦MainActivity被销毁，则需要重新登陆
 */
public class LoginActivity extends ZJFActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     * ID来识别READ_CONTACTS权限请求
     */
    private static final int REQUEST_READ_CONTACTS = 0;//读取联系人权限
    /**
     * A dummy authentication store containing known user names and passwords.
     * 一个储存了认证的已知用户名称和密码
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{//用于验证账号密码的临时值
            "12345@", "123456"//后续改为在数据库中取值/
    };
    private String m_PassWord = "123456";//测试密码
    private String m_Account = "12345@";//测试账户
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 保持一个登陆任务的跟踪来确保我们可以按要求取消它
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;//输入账号的控件实例
    private EditText mPasswordView;//输入密码的控件实例
    private View mProgressView;//进度圈实例
    private View mLoginFormView;//登陆框的实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {//重写的创建时时间 参数上次保存的试图
        super.onCreate(savedInstanceState);//从上次返回的视图中恢复
        setContentView(R.layout.activity_login);
        if (savedInstanceState != null)//上一次的状态是否保存
        {
            AutoCompleteTextView Account = (AutoCompleteTextView) findViewById(R.id.email);//获取到账号框实例
            EditText Password = (EditText) findViewById(R.id.password);//获取密码框实例
            Account.setText(savedInstanceState.getString("ACCOUNT"));
            Password.setText(savedInstanceState.getString("PASSWORD"));
        }
        Log.d("登陆活动 onCreate :", "活动编号 ->" + getTaskId());
        // Set up the login form.
        //设置登陆表框
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);//获取到输入账号的实例
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);//获取大盘输入密码的实例
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {//监听密码输入动作
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();//尝试登陆
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);//获取到登陆的实例
        mEmailSignInButton.setOnClickListener(new OnClickListener() {//设置监听按钮的单击事件
            @Override
            public void onClick(View view) {
                attemptLogin();
            }//尝试登陆
        });//监听点击事件

        mLoginFormView = findViewById(R.id.login_form);//获取到登陆表格实例
        mProgressView = findViewById(R.id.login_progress);//获取到进度条实例

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("登陆活动 onStart :", "活动编号 ->" + getTaskId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("登陆活动 onResume :", "活动编号 ->" + getTaskId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("登陆活动 onPause :", "活动编号 ->" + getTaskId());
        if (MainActivity.LoginName.equals("")) {//如果没有登陆
            Log.d("登陆活动 onPause :", "由于没有登陆且活动进入后台！");
            ActivityCollector.finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        ///登陆成功后，应该清空回溯栈,卸载登陆成功后面，即主活动的onRestart事件中
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("登陆活动 onStop :", "活动编号 ->" + getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("登陆活动 onDestroy :", "活动编号 ->" + getTaskId());
    }

    private void populateAutoComplete() {//自动填充完成
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);//未知
    }

    private boolean mayRequestContacts() {//可能需要联系人？
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//先判断SDK版本
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {//先判断是否有读取联系人权限
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {//如果没有，弹出确认框让客户确认授权
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {//监听用户的点击事件
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);//授权
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);//如果有权限读取联系人直接授权
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     * 收到回调当一个许可请求完成
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//当有结果且同意授权时
                populateAutoComplete();//自动填充联系人
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * 尝试登陆或者注册账户用我们在登陆框中指定的信息
     * 如果指定的信息有误（无效的邮箱地址，没有填写的信息等）
     * 就显示错误信息，且不会发出登陆尝试
     */
    private void attemptLogin() {//尝试登陆函数
        if (mAuthTask != null) {//未知
            return;
        }
        // Reset errors.
        mEmailView.setError(null);//设置控件无错误
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();//将账号转换文本
        String password = mPasswordView.getText().toString();//将密码转换文本

        boolean cancel = false;//取消
        View focusView = null;//焦点视图

        // Check for a valid password, if the user entered one.
        //如果用户键入了密码检查它的有效性
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {//账号不为null且长度不为0，密码符合函数要求
            mPasswordView.setError(getString(R.string.error_invalid_password));//设置错误信息
            focusView = mPasswordView;
            cancel = true;//设置取消
        }

        // Check for a valid email address.
        //检查邮箱的有效性
        if (TextUtils.isEmpty(email)) {//如果账号没填
            mEmailView.setError(getString(R.string.error_field_required));//设置错误信息
            focusView = mEmailView;
            cancel = true;//设置取消
        } else if (!isEmailValid(email)) {//密码无效
            mEmailView.setError(getString(R.string.error_invalid_email));//设置错误信息
            focusView = mEmailView;
            cancel = true;//设置取消
        }

        if (cancel) {//如果取消设置了值
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //这里有一个错误，不要尝试登陆并且聚焦在第一个表格字段用错误？
            focusView.requestFocus();
        } else {//进行登陆
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //显示一个进度条，并展开一个后台任务来显示用户登陆
            showProgress(true);//显示一个尝试登陆的界面
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {//验证是否是邮件
        //TODO: Replace this with your own logic
        return email.contains("@");//验证是否含有@
    }

    private boolean isPasswordValid(String password) {//检测密码有效性，
        //TODO: Replace this with your own logic
        return password.length() > 4;//必须大于四位
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {//一旦活动被系统回收就会执行此函数来保存状态
        super.onSaveInstanceState(outState);
        Log.d("登陆活动onSaveInstance事件:", "活动编号 ->" + getTaskId());
        AutoCompleteTextView Account = (AutoCompleteTextView) findViewById(R.id.email);//获取到账号框实例
        EditText Password = (EditText) findViewById(R.id.password);//获取密码框实例
        String saveaccount = Account.getText().toString();//获取账号文本
        String savepassword = Password.getText().toString();//获取密码文本
        outState.putString("ACCOUNT", saveaccount);//保存账号
        outState.putString("PASSWORD", savepassword);//保存密码
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度界面并隐藏登陆框体
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {//显示一个尝试登陆的界面
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //在Honeycomb MR2中我们有ViewPropertyAnimator APIs，它可以允许建单的动画，如果可用，我们可以用这些APIS在进度条上淡入
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {//先判断SDK版本
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //获取到用于显示的设置
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);//根据传值判定是否显示
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(//设置透明度
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);//设置可见性
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);//设置进度条的可见行
            mProgressView.animate().setDuration(shortAnimTime).alpha(//设置透明度
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);//设置可见性
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);//根据传值判定是否显示
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);//根据传值判定是否显示
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {//创建载入联系人
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                //检索设备用户的“配置文件”联系人的数据行。
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                //只选择电子邮件
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                //首先显示主邮箱，注意如果用户没有指定主邮箱，那么主邮箱将不存在
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {//载入完成后
        List<String> emails = new ArrayList<>();//初始化一个新的邮件数组
        cursor.moveToFirst();//将游标移动到第一个未知
        while (!cursor.isAfterLast()) {//当游标没有到达末尾时
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);//将邮件添加到自动完成
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {//载入器重置

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {//传入数组
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        //创建一个适配器来告诉自动完成文本视图在下拉框中显示一个列表
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {//设置信息查询
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,//是邮箱地址
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,//是主邮箱
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     * 表示用于验证身份的异步登录/注册任务。
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {//后台进行事项
            // TODO: attempt authentication against a network service.
            //尝试验证网络服务

            try {
                // Simulate network access.
                //模拟网络验证
                Thread.sleep(2000);
            } catch (InterruptedException e) {//catch到错误 退出
                return false;
            }

            /**   for (String credential : DUMMY_CREDENTIALS) {
             String[] pieces = credential.split(":");
             if (pieces[0].equals(mEmail)) {
             // Account exists, return true if the password matches.
             //账户存在，返回真如果密码匹配
             //return pieces[1].equals(mPassword);//返回是否密码匹配的结果
             if (pieces[1].equals(mPassword))
             return true;3
             }
             if(pieces[0].equals(mEmail) && pieces[1].equals(mPassword))
             return true;
             }*/
            if (m_Account.equals(mEmail) && m_PassWord.equals(mPassword)) {
                MainActivity.LoginName = mEmail;
                return true;
            }
            // TODO: register the new account here.
            //在这里注册新账户
            //return true;
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {//执行POST
            mAuthTask = null;
            showProgress(false);//显示进度条？

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));//设置密码框显示的错误
                mPasswordView.requestFocus();//高亮输入框要求修改
            }
        }

        @Override
        protected void onCancelled() {//取消
            mAuthTask = null;
            showProgress(false);
        }
    }
}

