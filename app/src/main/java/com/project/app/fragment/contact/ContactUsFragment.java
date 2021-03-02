package com.project.app.fragment.contact;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ContactUsMethodBean;
import com.project.app.contract.ContactUsContract;
import com.project.app.presenter.ContactUsPresenter;
import com.project.app.ui.PackageCheckUtil;
import com.project.app.utils.WhatsAppUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 联系我们
 */
public class ContactUsFragment extends BaseMvpQmuiFragment<ContactUsPresenter> implements ContactUsContract.View {
    @BindView(R.id.rlv_allContactMethods)
    RecyclerView rlv_allContactMethods;
    private ContactUsMethodAdapter mAdapter;
    private List<ContactUsMethodBean> mDatas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mPresenter = new ContactUsPresenter();
        mPresenter.attachView(this);
        rlv_allContactMethods.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactUsMethodAdapter(mDatas);
        rlv_allContactMethods.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetctContactUsMethods();
    }

    @Override
    public void fetchContactMethodsSuccess(String result) {
        if(!TextUtils.isEmpty(result)){
            try {
                JSONObject resultObject = new JSONObject(result);
                String dataJson   = resultObject.getString("data");
                List<ContactUsMethodBean> linkMethods = JsonUtils.deserializeList(dataJson,ContactUsMethodBean[].class);
                mAdapter.setNewInstance(linkMethods);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void fetchContactMethodsFail(String msg) {

    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }
    private String mWhatAppNumber = "";

    private class ContactUsMethodAdapter extends BaseQuickAdapter<ContactUsMethodBean, BaseViewHolder>{

        public ContactUsMethodAdapter(List<ContactUsMethodBean> layoutResId) {
            super(R.layout.item_contact_us);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, ContactUsMethodBean contactUsMethodBean) {
            if(!TextUtils.isEmpty(contactUsMethodBean.getIcon())){
                ImageLoader.getInstance().displayImage(helper.getView(R.id.iv_contactIcon),contactUsMethodBean.getIcon(),R.mipmap.allwees_ic_default_goods);
            }
            if(!TextUtils.isEmpty(contactUsMethodBean.getName())){
                helper.setText(R.id.tv_contactChannel,contactUsMethodBean.getName());
            }
            if(contactUsMethodBean.getName().equalsIgnoreCase("Email")){
                helper.setText(R.id.iv_contactLink,contactUsMethodBean.getAcctNum());
            }else if(contactUsMethodBean.getName().equalsIgnoreCase("WhatsApp")){
                mWhatAppNumber = contactUsMethodBean.getAcctNum();
                helper.setText(R.id.iv_contactLink,getResources().getString(R.string.contact_us_title));
            }

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contactUsMethodBean.getName().equalsIgnoreCase("WhatsApp")){
                        checkWhatsInstall(contactUsMethodBean.getAcctNum());
                    }else if(contactUsMethodBean.getName().equalsIgnoreCase("Facebook")){
                        checkWhatsInstall(mWhatAppNumber);
                    }else if(!contactUsMethodBean.getName().equalsIgnoreCase("Email")){
                        openBrowser(contactUsMethodBean.getAcctNum());
                    }
                }
            });
        }
    }


    /**
     * 判断有没有安装whatsApp
     */
    private void checkWhatsInstall(String mWhatAppNumber) {
        String errorInstall = getContext().getResources().getString(R.string.goods_detail_no_install_whats_hint);
        boolean isInstallWhats = PackageCheckUtil.isAppInstalled(getContext(), Constant.PACKAGE_NAME_WAHTS);
        if(!isInstallWhats){
            ToastUtil.showToast(errorInstall);
        }else{
            if(!TextUtils.isEmpty(mWhatAppNumber)){
                WhatsAppUtil.chatInWhatsApp(getContext(),mWhatAppNumber);
            }
        }
    }

    public void openBrowser(String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        getContext().startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
