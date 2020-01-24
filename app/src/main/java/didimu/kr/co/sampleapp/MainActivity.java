package didimu.kr.co.sampleapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity {

    //private WebView webView = null;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentCamera fragmentCamera = new FragmentCamera();
    private FragmentCall fragmentCall = new FragmentCall();
    private FragmentWebView fragmentWebView = new FragmentWebView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
/*
        //웹뷰 설정
        webView = (WebView)findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



        MyWebViewClient webViewClient = new MyWebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("http://ulotto.didimu.co.kr");
*/

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentWebView).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }
/*
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            Uri url = request.getUrl();
            if(url.toString().indexOf("didimu.co.kr") > -1 ) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            startActivity(intent);

            return true;
        }
    }
*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && fragmentWebView.webView.canGoBack()) {
            fragmentWebView.webView.goBack();
            return true;
        } else {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.isVisible()) {
                    Log.e("현재 fragment", "에러입니다.");
                }
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    /*
    @Override
    public void onBackPressed() {
        //백프레스키만을 위해 지정한다면 여기도 괜찮겠죠?
        //if(this.webView.canGoBack()){
        //this.webView.goBack();
        //}
    }

*/
    // 뒤로가기 버튼 입력시간이 담길 long 객체
    private long pressedTime = 0;

    // 리스너 생성
    public interface OnBackPressedListener {
        public void onBack();
    }

    // 리스너 객체 생성
    private OnBackPressedListener mBackListener;

    // 리스너 설정 메소드
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    @Override
    public void onBackPressed() {

        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
        if(mBackListener != null) {
            mBackListener.onBack();
            Log.e("!!!", "Listener is not null");
            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료됩니다.
        } else {
            Log.e("!!!", "Listener is null");
            if ( pressedTime == 0 ) {
                Snackbar.make(findViewById(R.id.frameLayout),
                        " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            }
            else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if ( seconds > 2000 ) {
                    Snackbar.make(findViewById(R.id.frameLayout),
                            " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                    pressedTime = 0 ;
                }
                else {
                    super.onBackPressed();
                    Log.e("!!!", "onBackPressed : finish, killProcess");
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.webviewItem:
                    transaction.replace(R.id.frameLayout, fragmentWebView).addToBackStack(null).commitAllowingStateLoss();

                    break;
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).addToBackStack(null).commitAllowingStateLoss();

                    break;
                case R.id.cameraItem:
                    transaction.replace(R.id.frameLayout, fragmentCamera).addToBackStack(null).commitAllowingStateLoss();
                    break;
                case R.id.callItem:
                    //transaction.replace(R.id.frameLayout, fragmentCall).addToBackStack(null).commitAllowingStateLoss();
                    Intent intent = new Intent(MainActivity.this, Page1.class);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    }



}
