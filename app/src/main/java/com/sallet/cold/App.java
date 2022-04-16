/*
 * Copyright 2022 salletone developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sallet.cold;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.hk.offline.utils.LangUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sallet.cold.bean.CoinSetBean;
import com.sallet.cold.utils.ConfigContent;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * App entry
 */

public class App extends Application {
    public static SharedPreferences sp;//(Globally unique SharedPreferences)
    public static    String passWord="";//(Globally saved hardware wallet password)
    public static    int passWordTime=5;//(The number of incorrect passwords entered, the global save countdown is 5 times)
    public static  final  String passSubmit="passSubmit";//  password hint
    public static  final  String passWordDecode="passWord";// wallet password
    public static  final  String word="word";// mnemonic
    public static  final  String password= "password";// mnemonic
    public static  final  String defautBTCAddress="defautBTCAddress";// BTC default address
    public static  final  String AddressList="AddressList";// Generated address list
    public static  final  String isLogin="isLogin";// Verify that you are logged in
    public static  final  String language="language";// hardware language
    public static  final  String deletePass="deletePass";// Delete wallet password
    public static final String isTracFirst="ISTracFIRST";// Is it the first time to log in
    public static  Context context;
    public  static boolean isDebug=false;
    // Language list
    public static List<Locale> supportLang= Arrays.asList(
            Locale.SIMPLIFIED_CHINESE,
            Locale.US,
            new Locale("kh", "KH"),
            new Locale("lo", "LA"),
            new Locale("ms", "MY"),
            new Locale("in", "ID"),
            new Locale("vi", "VN"),
            new Locale("ar", "AE"),
            Locale.FRANCE,
            new Locale("hi", "IN"),
            Locale.ITALY,
            new Locale("ru", "RU"),
            Locale.KOREA,
            new Locale("my", "MM"),
            Locale.JAPAN,
            new Locale("th", "TH"),
            new Locale("es", "ES"),
            new Locale("fi", "FI"),
            new Locale("pt", "PT"),
            new Locale("sv", "SE"),
            new Locale("sr", "BA"),
            new Locale("da", "DK"),
            Locale.GERMANY,
            new Locale("no", "NO"),
            new Locale("el", "GR")
    );


    @Override
    public void onCreate() {

        super.onCreate();
        setupBouncyCastle();
         MultiDex.install(this);
        // get user store
         sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        context=this;
        // set default language
        LangUtils.defaultLocale=new Locale(getSpString(language));
        // Get device devicecode
        ConfigContent.deviceCode = ConfigContent.getDeviceId(this);
        // get app versionname
        ConfigContent.deviceVersion=getAppVersionName(this);
        CrashReport.initCrashReport(getApplicationContext(), "357f9ec492", false);
        // Set jar package language
        setJarLanguage();
    }

    public void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * Set jar package language
     */

    private void setJarLanguage(){
        Map<String,String  > map=new HashMap<>();
        for(Locale locale:supportLang){
            // Add support language to map
            map.put(locale.getLanguage(),locale.getCountry());
        }
        // Get the default language saved by the APP
        String country=map.get(getSpString(language));
        //     * Set jar package language
        LangUtils.defaultLocale=new Locale(getSpString(language),country);

    }

    /**
     * @param context
     * @return
     */


    public static String getAppVersionName(Context context) {

        String versionName = "";

        try {

            PackageManager pm = context.getPackageManager();

            PackageInfo p1 = pm.getPackageInfo(context.getPackageName(), 0);

            versionName = p1.versionName;

            if (TextUtils.isEmpty(versionName) || versionName.length() <= 0) {

                return "";

            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        return versionName;

    }

    /**
     *  Save string related content
     * @param key
     * @param value
     */

    public static void saveString(String key,String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     *  Default language returns to English
     * @return
     */
    private static String getCurrentLauguage(){
        return "en";
    }


    /**
     *  Get the string saved in storage
     * @param key
     * @return
     */
    public static String getSpString(String key) {
        if(language.equals(key)){
            return  sp.getString(key, getCurrentLauguage());
        }else {
            return sp.getString(key, null);
        }
    }







    /**
     *  Save address list
     * @param list
     */


    public static void saveAddressList(List<CoinSetBean>list){
        Collections.sort(list); //
        Gson gson = new Gson();
        String jsonStr=gson.toJson(list); //

        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString(AddressList, jsonStr) ; //
        editor.apply() ;  //
    }


    /**
     *  Get address list
     * @return
     */
    public static List<CoinSetBean> getAddressList() {
        String list = sp.getString(AddressList,"");
        if(!list.equals(""))  //
        {
            Gson gson = new Gson();
            return gson.fromJson(list, new TypeToken<List<CoinSetBean>>() {}.getType()); //
        }else {
            return null;
        }
    }


    /**
     * clear data
     */

    public static void clear() {
        App.passWordTime=5;
        // Get user data in storage
        SharedPreferences preferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // clear user data
        editor.clear();
        editor.commit();
    }

}
