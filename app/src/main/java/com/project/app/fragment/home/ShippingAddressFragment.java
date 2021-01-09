package com.project.app.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AddressBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.bean.CreateAddressBean;
import com.project.app.bean.ProvinceBean;
import com.project.app.contract.AddressControlContract;
import com.project.app.fragment.address.SelectProvinceCityFragment;
import com.project.app.presenter.AddressControlPresenter;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ShippingAddressFragment extends BaseMvpQmuiFragment<AddressControlPresenter> implements AddressControlContract.View {
    @BindView(R.id.et_addressFirstN)
    EditText et_addressFirstN;
    @BindView(R.id.et_addressLastN)
    EditText et_addressLastN;
    @BindView(R.id.et_defaultAddress)
    EditText et_defaultAddress;
    @BindView(R.id.et_secondAddress)
    EditText et_secondAddress;
    @BindView(R.id.tv_selectCountry)
    TextView tv_selectCountry;
    @BindView(R.id.tv_selectProvince)
    TextView tv_selectProvince;
    @BindView(R.id.et_addresZip)
    TextView et_addresZip;
    @BindView(R.id.et_contactPhone)
    EditText et_contactPhone;
    @BindView(R.id.qmui_addressSave)
    QMUIRoundButton qmui_addressSave;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_displayA1)
    TextView tv_displayA1;
    @BindView(R.id.tv_displayA2)
    TextView tv_displayA2;
    @BindView(R.id.ll_zipCode)
    LinearLayout ll_zipCode;
    @BindView(R.id.ll_note)
    LinearLayout ll_note;
    @BindView(R.id.et_addresNote)
    EditText et_addresNote;
    @BindView(R.id.tv_zipCode)
    TextView tv_zipCode;
    @BindView(R.id.ll_choiceP)
    LinearLayout ll_choiceP;
    @BindView(R.id.ll_choiceCity)
    LinearLayout ll_choiceCity;
    @BindView(R.id.tv_address_city)
    TextView tv_address_city;
    @BindView(R.id.tv_areaCode)
    TextView tv_areaCode;
    @BindView(R.id.v_divisi)
    View v_divisi;

    private AddressBean.AddressItem mSigleAddress;
    private String mFirstName;
    private String mLastName;
    private String mAddress1;
    private String mAddress2;
    private String mProvince;
    private String mProvinceId;
    private String mCity;
    private String mZipCode;
    private String mPhone;                //手机号码
    private String mPhoneAreaCode = "";   //手机号码区号
    private String mCountryCode = "53950000";  //沙特:48650000  阿拉伯:2610000
    private String mDefaultCountry = "";
    private String mEditProvince = "";
    private int mCountryType  = -1;    //默认为美国 1阿拉伯 2为沙特
    private int mRequestType;
    private boolean isDefault = false;
    private boolean isCustomCountry = false;
    private CountryCropBean.CountryItem mWinCountry;
    public  final static int INDEX_ACTION_ADD_ADDRESS_USER = 0;
    public  final static int INDEX_ACTION_ADD_ADDRESS_ME = 2;
    public  final static int INDEX_ACTION_EDIT = 1;     //编辑操作
    private static int INDEX_ACTION_NONE = 0;           //0为新创建 1为修改 2为管理新增

    private final HashMap<String,String> mRequestParams = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shipping_address;
    }

    @Override
    public void initView() {
        initTopbar();
        initBundle();
        initWidget();
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        EventBus.getDefault().register(this);
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            INDEX_ACTION_NONE = bundle.getInt("type");
            if(INDEX_ACTION_NONE == 1){
                String needAddressJson = bundle.getString("data");
                mSigleAddress = JsonUtils.deserialize(needAddressJson,AddressBean.AddressItem.class);
                mCountryCode     = mSigleAddress.getRegion();        //国家的region编号
                mDefaultCountry  = mSigleAddress.getCountry();       //国家的名字
                mEditProvince    = mSigleAddress.getProvinceRegion(); //获取省份
            }else{
                isCustomCountry = LocaleUtil.getInstance().getLocaleCustom();
                if(isCustomCountry){
                    mCountryCode    = LocaleUtil.getInstance().getLocalCustomRegion();   //自己切换的家国
                    mDefaultCountry = LocaleUtil.getInstance().getLocalCustomNameEn();
                    mPhoneAreaCode  = LocaleUtil.getInstance().getCustomPhoneAreaCode();
                }else{
                    mCountryCode = LocaleUtil.getInstance().getRegion();
                    mDefaultCountry = LocaleUtil.getInstance().getCoungryName();
                    mPhoneAreaCode = LocaleUtil.getInstance().getPhoneAreaCode();
                }
                tv_areaCode.setText("+" + mPhoneAreaCode);
                tv_selectCountry.setText(mDefaultCountry);
            }
        }
    }

    private void initWidget() {
        mPresenter = new AddressControlPresenter();
        mPresenter.attachView(this);
        qmui_addressSave.setChangeAlphaWhenPress(true);
        bindData();
    }

    private void bindData() {
        getCountryType();
        if (INDEX_ACTION_NONE == 1) {
            editChannel();
        }
    }

    public void editChannel(){
        if(mSigleAddress != null){
            String phoneNum  = mSigleAddress.getPhone();
            if(phoneNum.contains("(")){
                int lastBrackets = phoneNum.lastIndexOf(")");
                mPhoneAreaCode  = phoneNum.substring(1,lastBrackets);     //手机号码区号
                mPhone = phoneNum.substring(lastBrackets+1,phoneNum.length());  //手机号码
            }else{
                mPhone = phoneNum;    //如果编辑的手机号码没有区号,则获取本地的手机区号
                getPhoneAreaCode();
            }
            inflateInitAddress();
        }
    }

    //获取手机区号
    public void getPhoneAreaCode(){
        isCustomCountry = LocaleUtil.getInstance().getLocaleCustom();
        if(isCustomCountry){
            mPhoneAreaCode  = LocaleUtil.getInstance().getCustomPhoneAreaCode();
        }else{
            mPhoneAreaCode = LocaleUtil.getInstance().getPhoneAreaCode();
        }
    }

    //展示不同的国家的显示方式
    public void getCountryType(){
        String address_street = getResources().getString(R.string.address_s_street_l);
        String address_detail = getResources().getString(R.string.address_s_address_d);
        String address_near   = getResources().getString(R.string.address_s_address_ln);

        if(mCountryCode.equals("53950000")){
            mCountryType = 0;
        }else if(mCountryCode.equals("2610000")){
            mCountryType = 1;
            tv_displayA1.setText(address_street);
            tv_displayA2.setText(address_detail);
            tv_zipCode.setText(address_near);
            ll_note.setVisibility(View.GONE);
        }else if(mCountryCode.equals("48650000")){
            mCountryType = 2;
            tv_displayA1.setText(address_street);
            tv_displayA2.setText(address_detail);
            tv_zipCode.setText(address_near);
            ll_note.setVisibility(View.GONE);
        }else {
            mCountryType = 3;
        }
    }

    @OnClick({R.id.qmui_addressSave,R.id.ll_choiceP,R.id.ll_choiceCity,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.qmui_addressSave:
                if(isInputVaild()){
                    postToService();
                }
                break;
            case R.id.ll_choiceP:
                mRequestType = 0;
                Bundle pBundle = new Bundle();
                pBundle.putInt("type",0);
                if(mCountryType == 3){   //3为其他国家 选择省份
                    pBundle.putString("region","-1");
                }else{
                    if(!TextUtils.isEmpty(mCountryCode)){
                        pBundle.putString("region",mCountryCode);
                    }else{
                        pBundle.putString("region","-1");
                    }
                }
                Intent goPc = HolderActivity.of(getContext(), SelectProvinceCityFragment.class,pBundle);
                getContext().startActivity(goPc);
                break;
            case R.id.ll_choiceCity:
                mRequestType = 1;
                Bundle cBundle = new Bundle();
                cBundle.putInt("type",1);
                String provinceName = tv_selectProvince.getText().toString().trim();
                if(INDEX_ACTION_NONE == 1){   //修改
                    if(!TextUtils.isEmpty(mEditProvince) && !TextUtils.isEmpty(mCountryCode)){
                        cBundle.putString("region",mCountryCode);  //国家区号
                        cBundle.putString("regionCity",mEditProvince);          //省份区号
                    }else{
                        cBundle.putString("region","-1");  //国家区号
                        cBundle.putString("regionCity","-1");          //省份区号
                    }
                    Intent goCity = HolderActivity.of(getContext(), SelectProvinceCityFragment.class,cBundle);
                    getContext().startActivity(goCity);
                }else{
                    if(mCountryType == 0 || mCountryType == 1|| mCountryType == 2){   //0为美国 1为阿拉伯 2为沙特
                        if(TextUtils.isEmpty(provinceName)){
                            String hintEmpty = getContext().getString(R.string.hint_province_empty);
                            ToastUtil.showToast(hintEmpty);
                        }else{
                            if(!TextUtils.isEmpty(mCountryCode) && !TextUtils.isEmpty(mProvinceId) ){
                                cBundle.putString("region",mCountryCode);  //国家区号
                                cBundle.putString("regionCity",mProvinceId);          //省份区号
                            }else{
                                cBundle.putString("region","-1");  //国家区号
                                cBundle.putString("regionCity","-1");          //省份区号
                            }
                            Intent goCity = HolderActivity.of(getContext(), SelectProvinceCityFragment.class,cBundle);
                            getContext().startActivity(goCity);
                        }
                    }else if(mCountryType == 3){    //3为其他国家
                        cBundle.putString("region","-1");
                        cBundle.putString("regionCity","-1");
                        Intent goCity = HolderActivity.of(getContext(), SelectProvinceCityFragment.class,cBundle);
                        getContext().startActivity(goCity);
                    }
                }
                break;
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvented(RefreshDataEvent event){
        if(event.getmMsg().equals(Constant.EVENT_SPECITY_PCITY)){  //列表选择
            ProvinceBean bean = JsonUtils.deserialize(event.getData(),ProvinceBean.class);
            if(mRequestType == 0){
                mProvince = bean.getName();
                mProvinceId = bean.getId();
                tv_selectProvince.setText(mProvince);
            }else if(mRequestType == 1){
                mCity = bean.getName();
                tv_address_city.setText(mCity);
            }
        }else if(event.getmMsg().equals(Constant.EVENT_INPUT_PC)){   //手动输入
            String target = event.getData();
            if(mRequestType == 0){
                tv_selectProvince.setText(target);
            }else if(mRequestType == 1){
                tv_address_city.setText(target);
            }
        }
    }

    /**
     * 欧美
     * 1、必填 ZipCode。
     * 2、必填 AddressLine1。
     * 3、选填 AddressLine2。
     * @return
     *
     * 中东：
     * 1、必填 Street。
     * 2、选填 AddressLine 1。(将街道附近的标志等连接起来传至 AddressLine 1，中间使用#div#。)
     *
     */
    private boolean isInputVaild(){
        String hintInfo  =  getResources().getString(R.string.str_please_provide_information);
        String firstName = et_addressFirstN.getText().toString().trim();
        String lastName  = et_addressLastN.getText().toString().trim();
        String address1  = et_defaultAddress.getText().toString().trim();
        String address2  = et_secondAddress.getText().toString().trim();
        String zipCode   = et_addresZip.getText().toString().trim();
        String phone     = et_contactPhone.getText().toString().trim();
        String country   = tv_selectCountry.getText().toString().trim();
        String note      = et_addresNote.getText().toString().trim();
        String province  = tv_selectProvince.getText().toString().trim();
        String city      = tv_address_city.getText().toString().trim();

        if(TextUtils.isEmpty(firstName)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(TextUtils.isEmpty(lastName)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(TextUtils.isEmpty(address1)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(TextUtils.isEmpty(city)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(TextUtils.isEmpty(province)){
            ToastUtil.showToast(hintInfo);
            return false;
        }

        if(mCountryType == 0){
            if(TextUtils.isEmpty(zipCode)){
                ToastUtil.showToast(hintInfo);
                return false;
            }
        }

        mFirstName = firstName;
        mLastName  = lastName;
        mAddress1  = address1;
        mAddress2  = address2;
        mZipCode   = zipCode;
        mPhone     = phone;

        if(!TextUtils.isEmpty(mPhoneAreaCode)){
            mPhone = "(" + mPhoneAreaCode + ")" + phone;
        }

        if(INDEX_ACTION_NONE == 1){
            String addressUuid = mSigleAddress.getUuid();
            isDefault  = mSigleAddress.isDefault();
            mRequestParams.put("uuid",addressUuid);
        }

        if(mCountryType == 0){
            mRequestParams.put("addressLine1",address1);
            mRequestParams.put("addressLine2",address2);
            mRequestParams.put("city",city);
            mRequestParams.put("country",mCountryCode);
            mRequestParams.put("firstName",firstName);
            mRequestParams.put("lastName",lastName);
            mRequestParams.put("phone",mPhone);
            mRequestParams.put("province",province);
            mRequestParams.put("zipCode",mZipCode);
            mRequestParams.put("isDefault",String.valueOf(isDefault));
            mRequestParams.put("itu","1");
            mRequestParams.put("note",note);
        }else if(mCountryType == 1||mCountryType == 2){
            mRequestParams.put("street",mAddress1);
            mRequestParams.put("addressLine1",mAddress2 + "#div#" + mZipCode);
            mRequestParams.put("city",city);
            mRequestParams.put("country",mCountryCode);
            mRequestParams.put("firstName",mFirstName);
            mRequestParams.put("lastName",mLastName);
            mRequestParams.put("phone",mPhone);
            mRequestParams.put("province",province);
            mRequestParams.put("isDefault",String.valueOf(isDefault));
            mRequestParams.put("itu","1");
            mRequestParams.put("note",note);
        }else {
            mRequestParams.put("addressLine1",address1);
            mRequestParams.put("addressLine2",address2);
            mRequestParams.put("city",city);
            mRequestParams.put("country",mCountryCode);
            mRequestParams.put("firstName",firstName);
            mRequestParams.put("lastName",lastName);
            mRequestParams.put("phone",mPhone);
            mRequestParams.put("province",province);
            mRequestParams.put("zipCode",mZipCode);
            mRequestParams.put("isDefault",String.valueOf(isDefault));
            mRequestParams.put("itu","1");
            mRequestParams.put("note",note);
        }
        return true;
    }

    //修改时候的地址信息提取
    private void inflateInitAddress() {
        mProvinceId = mSigleAddress.getProvince();  //修改的时候获取省份id

        if(!TextUtils.isEmpty(mSigleAddress.getFirstName())){
            et_addressFirstN.setText(mSigleAddress.getFirstName());
        }
        if(!TextUtils.isEmpty(mSigleAddress.getLastName())){
            et_addressLastN.setText(mSigleAddress.getLastName());
        }

        if(!TextUtils.isEmpty(mSigleAddress.getAddressLine2())){
            et_secondAddress.setText(mSigleAddress.getAddressLine2());
        }
        if(!TextUtils.isEmpty(mSigleAddress.getCountry())){
            tv_selectCountry.setText(mSigleAddress.getCountry());
        }
        if(!TextUtils.isEmpty(mSigleAddress.getProvince())){
            tv_selectProvince.setText(mSigleAddress.getProvince());
        }
        if(!TextUtils.isEmpty(mSigleAddress.getCity())){
            tv_address_city.setText(mSigleAddress.getCity());
        }
        if(!TextUtils.isEmpty(mSigleAddress.getPhone())){
            tv_areaCode.setText("+" + mPhoneAreaCode);
            et_contactPhone.setText(mPhone);
        }
        if(!TextUtils.isEmpty(mSigleAddress.getNote())){
            et_addresNote.setText(mSigleAddress.getNote());
        }

        String address1 = mSigleAddress.getAddressLine1();

        if(address1.contains("#div#")){
            String aggreAddress = mSigleAddress.getAddressLine1();
            if(!TextUtils.isEmpty(mSigleAddress.getStreet())){
                et_defaultAddress.setText(mSigleAddress.getStreet());
            }
            if(!TextUtils.isEmpty(aggreAddress) && aggreAddress.contains("#")){
                int endPosition = aggreAddress.lastIndexOf("#");
                int sPosition   = aggreAddress.indexOf("#");
                et_secondAddress.setText(aggreAddress.substring(0,sPosition));
                et_addresZip.setText(aggreAddress.substring(endPosition+1,aggreAddress.length()));
            }
        }else{
            if(!TextUtils.isEmpty(mSigleAddress.getZipCode())){
                et_addresZip.setText(mSigleAddress.getZipCode());
            }
            if(!TextUtils.isEmpty(mSigleAddress.getAddressLine1())){
                et_defaultAddress.setText(mSigleAddress.getAddressLine1());
            }
        }
    }

    private void postToService() {
        qmui_addressSave.setEnabled(false);
        if(INDEX_ACTION_NONE == 0){
            mPresenter.createAddress(mRequestParams);
        }else if(INDEX_ACTION_NONE == 1){
            mPresenter.editAddress(mRequestParams);
        }else if(INDEX_ACTION_NONE == 2){
            mPresenter.createAddress(mRequestParams);
        }
    }


    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void editAddressResult(CreateAddressBean response) {
        String hintSuccess = getResources().getString(R.string.str_success);
        ToastUtil.showToast(hintSuccess);
        qmui_addressSave.setEnabled(true);
        if(INDEX_ACTION_NONE == 1){
            EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_REFRESH_ADDRESS_LIST_DATA));
        }
        popBackStack();
    }

    @Override
    public void addAddressResult(CreateAddressBean response) {   //0为创建 2为管理新增
        String hintSuccess = getResources().getString(R.string.str_success);
        qmui_addressSave.setEnabled(true);
        if(INDEX_ACTION_NONE == 0){
            EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_ADD_ADDRESS_SURE));
        }else if(INDEX_ACTION_NONE == 2){
            EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_REFRESH_ADDRESS_LIST_DATA));
        }
        popBackStack();
        ToastUtil.showToast(hintSuccess);
    }

    @Override
    public void createAddressFail(String result) {
        qmui_addressSave.setEnabled(true);
        ToastUtil.showToast(result);
    }

    @Override
    public void editAddreddFail(String result) {
        qmui_addressSave.setEnabled(true);
        ToastUtil.showToast(result);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
