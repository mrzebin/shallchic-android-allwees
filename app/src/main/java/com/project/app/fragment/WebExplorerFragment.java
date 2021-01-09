package com.project.app.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.view.NumberProgressBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.MyApp;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.fragment.pay.PayStatusFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * create by hb
 */
public class WebExplorerFragment extends BaseMvpQmuiFragment {
    private final static int DEF = 95;
    @BindView(R.id.progress_bar)
    NumberProgressBar mNumberProgressBar;
    @BindView(R.id.wb_explore)
    WebView wb_explore;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private String mLoadUrl;
    private String mType;   //0为支付papal 1为读取本地文件 2为读取网络链接 3为信用卡支付
    private String mTitle;
    private String mPayUuid;
    private String mCreditHtml;  //信用卡使用的html

    @Override
    public int getLayoutId() {
        return R.layout.fragment_webview_explorer;
    }

    @Override
    public void initView() {
        initTopbar();
        initWebView();
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private void initTopbar() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mLoadUrl = bundle.getString("webUrl");
            mType    = bundle.getString("type");
            if(mType.equals("1")){
                mTitle   = bundle.getString("title");
            }else if(mType.equals("0")){
                mPayUuid = bundle.getString("payUuid");
            }else if(mType.equals("3")){
                mCreditHtml = bundle.getString("payParams");
                mPayUuid =    "";
            }
        }
        tv_topTitle.setText(mTitle);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initWebView() {
        WebSettings settings = wb_explore.getSettings();           //和系统webview一样
        wb_explore.requestFocusFromTouch();                        //支持获取手势焦点，输入用户名、密码或其他
        wb_explore.getSettings().setAllowFileAccessFromFileURLs(true);

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(true);

        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setNeedInitialFocus(true);
        settings.setBlockNetworkLoads(false);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        String appCachePath = MyApp.mContext.getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);

//        wb_explore.setWebViewClient(new WriteHandlingWebViewClient(wb_explore));

        wb_explore.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if(mType.equals("3")){
                    Uri uri = request.getUrl();
                    String scheme = uri.getScheme();
                    String requestMethod = request.getMethod();
                    LoggerUtil.i("请求的方式:" + requestMethod);

                    Map<String,String> requestMap = request.getRequestHeaders();

                    if(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")){
                        String interceptUrl = request.getUrl().toString();
                        if(interceptUrl.equalsIgnoreCase(Constant.PAYMENT_CREDIT_URL_STATUS)){
                            EventBus.getDefault().post(new RefreshDataEvent(Constant.REFRESH_AFTER_PAY_CARDATA));   //刷新购物车
                            Bundle bundle = getArguments();
                            bundle.putString("orderId",mPayUuid);
                            bundle.putString("orderType","Paid");
                            Intent goSuccess = HolderActivity.of(getContext(), PayStatusFragment.class,bundle);
                            getContext().startActivity(goSuccess);
                            clearHolderStack();   //清理holderActivity的栈
                        }
                    }
                }
                return null;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
//           注意:有这个方法会不通过google play 审核
//            @Override
//            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                sslErrorHandler.proceed();    //忽略SSL证书错误
//            }

//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                LoggerUtil.i("错误码:" + errorCode + "--failingUrl:" + failingUrl);
////                wb_explore.loadUrl("file//andorid_assets/error.html");    //加载错误
//            }
        });

        wb_explore.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                String webTitle = webView.getTitle() == null?"":webView.getTitle();
                if(mType.equals("0")){
                    if(tv_topTitle != null){
                        tv_topTitle.setText(webTitle);
                        if(title.equalsIgnoreCase("Pay Succeed")){
                            EventBus.getDefault().post(new RefreshDataEvent(Constant.REFRESH_AFTER_PAY_CARDATA));
                            popBackStack();
                            Bundle bundle = getArguments();
                            bundle.putString("orderId",mPayUuid);
                            bundle.putString("orderType","Paid");
                            Intent goSuccess = HolderActivity.of(getContext(), PayStatusFragment.class,bundle);
                            getContext().startActivity(goSuccess);
                        }else if(title.equalsIgnoreCase("Pay Cancel")){
                            popBackStack();
                        }
                    }
                }else if(mType.equals("3")){
                    if(tv_topTitle != null){
                        tv_topTitle.setText(webTitle);
                    }
                }else{
                    if(tv_topTitle != null){
                        tv_topTitle.setText(webTitle);
                    }
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int progress) {
                super.onProgressChanged(webView, progress);
                if(DEF <= progress){
                    if(mNumberProgressBar != null){
                        mNumberProgressBar.setVisibility(View.GONE);
                    }
                }else{
                    if(mNumberProgressBar != null){
                        mNumberProgressBar.setProgress(progress);
                    }
                }
            }
        });

        if(mType.equals("3")){
//            String requestBody = getCreditPayRequest();
            tv_topTitle.setText("loading");
            wb_explore.loadDataWithBaseURL(null,mCreditHtml,"text/html","utf-8",null);
        }else{
            wb_explore.loadUrl(mLoadUrl);
        }
    }

//    public String getCreditPayRequest(){
//        HashMap<String,String> params = new HashMap<>();
//        params.put("account",mCreditObject.getAccount());
//        params.put("terminal",mCreditObject.getTerminal());
//        params.put("signValue",mCreditObject.getSignValue());
//        params.put("backUrl",mCreditObject.getBackUrl());
//        params.put("noticeUrl",mCreditObject.getNoticeUrl());
//        params.put("methods",mCreditObject.getMethods());
//        params.put("pages",mCreditObject.getPages());
//        params.put("order_number",mCreditObject.getOrder_number());
//        params.put("order_currency",mCreditObject.getOrder_currency());
//        params.put("order_amount",mCreditObject.getOrder_amount());
//        params.put("billing_firstName",mCreditObject.getBilling_firstName());
//        params.put("billing_lastName",mCreditObject.getBilling_lastName());
//        params.put("billing_email",mCreditObject.getBilling_email());
//        params.put("billing_phone",mCreditObject.getBilling_phone());
//        params.put("billing_country",mCreditObject.getBilling_country());
//        params.put("billing_state",mCreditObject.getBilling_state());
//        params.put("billing_city",mCreditObject.getBilling_city());
//        params.put("billing_address",mCreditObject.getBilling_address());
//        params.put("billing_zip",mCreditObject.getBilling_zip());
//        params.put("ship_firstName",mCreditObject.getShip_firstName());
//        params.put("ship_lastName",mCreditObject.getShip_lastName());
//        params.put("ship_phone",mCreditObject.getShip_phone());
//        params.put("ship_country",mCreditObject.getShip_country());
//        params.put("ship_state",mCreditObject.getShip_state());
//        params.put("ship_city",mCreditObject.getShip_city());
//        params.put("ship_addr",mCreditObject.getShip_addr());
//        params.put("ship_zip",mCreditObject.getShip_zip());
//        params.put("productSku",mCreditObject.getProductSku());
//        params.put("productName",mCreditObject.getProductName());
//        params.put("productNum",mCreditObject.getProductNum());
//        return  attachHttpGetParams(params);
//    }

    public static String attachHttpGetParams(HashMap<String,String> params){
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i=0;i<params.size();i++ ) {
            String value= String.valueOf(values.next());
            stringBuffer.append(keys.next()).append("=").append(value);
            if (i!=params.size()-1) {
                stringBuffer.append("&");
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(wb_explore != null && wb_explore.canGoBack()){
                wb_explore.goBack();
                return true;
            }else{
                popBackStack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wb_explore != null){
            wb_explore.destroy();   //释放资源
        }
    }
}
