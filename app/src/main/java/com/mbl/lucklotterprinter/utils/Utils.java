package com.mbl.lucklotterprinter.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.core.common.BuildConfig;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.LineModel;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class Utils {

    public static OkHttpClient getUnsafeOkHttpClient(int readTimeOut, int connectTimeOut, String apiKey) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

            }};

            // Install the all-trusting trust manager
            final SSLContext tls = SSLContext.getInstance("TLS");
            tls.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = tls
                    .getSocketFactory();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            builder.readTimeout(readTimeOut, TimeUnit.SECONDS)
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(hostnameVerifier);//org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER

            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                   /* String credentials = "lottnet:dms";
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);*/
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Authorization", "Basic bHVja3lsb3R0ZXI6dmluYXR0aQ==")
                            .addHeader("API_KEY", apiKey); // <-- this is the important line BuildConfig.API_KEY

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            builder.retryOnConnectionFailure(true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String getKenoPlus(int id){
        String name = "";
        switch (id){
            case 1:
                name = "Chẵn";
                break;
            case 2:
                name = "Lẻ";
                break;
            case 3:
                name = "Lớn";
                break;
            case 4:
                name = "Nhỏ";
                break;
            case 5:
                name = "Hòa Chẵn-Lẻ";
                break;
            case 6:
                name = "Hòa Lớn-Nhỏ";
                break;
            case 7:
                name = "Chẵn 11-12";
                break;
            case 8:
                name = "Lẻ 11-12";
                break;
        }
        return name;
    }

    public static String getCodePrintKeno(int id){
        String name = "";
        switch (id){
            case 1:
                name = Constants.KENO_EVEN;
                break;
            case 2:
                name = Constants.KENO_ODD;
                break;
            case 3:
                name = Constants.KENO_BIG;
                break;
            case 4:
                name = Constants.KENO_SMALL;
                break;
            case 5:
                name = Constants.KENO_EVEN_ODD;
                break;
            case 6:
                name = Constants.KENO_BIG_SMALL;
                break;
            case 7:
                name = Constants.KENO_EVEN_11_12;
                break;
            case 8:
                name = Constants.KENO_ODD_11_12;
                break;
        }
        return name;
    }


    public static String getProductName(int id) {
        String name = "";
        switch (id) {
            case Constants.PRODUCT_KENO:
                name = "Keno";
                break;
            case Constants.PRODUCT_MAX3D:
                name = "Max 3D";
                break;
            case Constants.PRODUCT_MAX3D_PLUS:
                name = "Max 3D+";
                break;
            case Constants.PRODUCT_MAX3D_PRO:
                name = "Max 3D Pro";
                break;
            case Constants.PRODUCT_MAX4D:
                name = "Max 4D";
                break;
            case Constants.PRODUCT_MEGA:
                name = "Mega 6/45";
                break;
            case Constants.PRODUCT_POWER:
                name = "Power 6/55";
                break;
            default:
                name = "Các sản phẩm khác";
                break;
        }
        return name;
    }

    public static CharSequence getInfoImageAfter(Activity activity, String name, String _pidNumber, String _mobileNumber) {
        int textSize18 = activity.getResources().getDimensionPixelSize(R.dimen._18sdp);

        CharSequence finalText = null;
        String fullName = name;
        String pidNumber = _pidNumber;
        String mobileNumber = _mobileNumber;

        if (fullName != null) {
            fullName = "Người nhận: " + fullName;

            SpannableString spanFullname = new SpannableString(fullName);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spanFullname.setSpan(boldSpan, 0, fullName.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanFullname.setSpan(new AbsoluteSizeSpan(textSize18), 0, fullName.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanFullname.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, fullName.length(), 0);// set color
            finalText = spanFullname;
        }
        if (pidNumber != null) {
            pidNumber = "Số CMND: " + pidNumber;

            SpannableString spanPidNumber = new SpannableString(pidNumber);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spanPidNumber.setSpan(boldSpan, 0, pidNumber.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanPidNumber.setSpan(new AbsoluteSizeSpan(textSize18), 0, pidNumber.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanPidNumber.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, pidNumber.length(), 0);// set color
            finalText = TextUtils.concat(finalText, "\n", spanPidNumber);
        }
        if (mobileNumber != null) {
            mobileNumber = "Điện thoại: " + mobileNumber;

            SpannableString spanMobileNumber = new SpannableString(mobileNumber);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spanMobileNumber.setSpan(boldSpan, 0, mobileNumber.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanMobileNumber.setSpan(new AbsoluteSizeSpan(textSize18), 0, mobileNumber.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanMobileNumber.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, mobileNumber.length(), 0);// set color
            finalText = TextUtils.concat(finalText, "\n", spanMobileNumber);
        }

        return finalText;
    }

    public static CharSequence getInfoImageBefore(Activity activity, List<LineModel> tickets, String gameName, String draw, boolean isAmount) {
        CharSequence finalText;

        int textSize18 = activity.getResources().getDimensionPixelSize(R.dimen._18sdp);
        int textSize14 = activity.getResources().getDimensionPixelSize(R.dimen._14sdp);

        SpannableString span1 = new SpannableString(gameName);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        span1.setSpan(boldSpan, 0, gameName.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span1.setSpan(new AbsoluteSizeSpan(textSize18), 0, gameName.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span1.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), 0, gameName.length(), 0);// set color
        span1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
                , 0, gameName.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finalText = span1;

        for (int i = 0; i < tickets.size(); i++) {
            LineModel itemTicket = tickets.get(i);

            String textDraw;

            StringBuilder boso = new StringBuilder();
            switch (i) {
                case 0:
                    boso = new StringBuilder("A: ");
                    break;
                case 1:
                    boso = new StringBuilder("B: ");
                    break;
                case 2:
                    boso = new StringBuilder("C: ");
                    break;
                case 3:
                    boso = new StringBuilder("D: ");
                    break;
                case 4:
                    boso = new StringBuilder("E: ");
                    break;
                case 5:
                    boso = new StringBuilder("F: ");
                    break;
            }
            String[] arrLine = itemTicket.getLine().split(",");
            for (String s : arrLine) {
                boso.append(StringUtils.leftPad(s, 2, '0')).append(" ");
            }
//            if (itemTicket.getType().equals(Constants.TICKET_NO_AMOUNT)) {
            textDraw = boso.toString();
//            } else {
//                textDraw = boso + "    " + NumberUtils.formatPriceNumber(itemTicket.getAmount());
//            }

            if (!TextUtils.isEmpty(textDraw) && finalText != null) {
                SpannableString spannableString = new SpannableString(textDraw);
                spannableString.setSpan(new AbsoluteSizeSpan(textSize18), 0, textDraw.length(), SPAN_INCLUSIVE_INCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, textDraw.length(), 0);// set color
                finalText = TextUtils.concat(finalText, "\n", spannableString);
            }

            if (isAmount) {
                String text2 = NumberUtils.formatPriceNumber(itemTicket.getAmount());
                if(itemTicket.getProductID() == Constants.PRODUCT_MAX3D_PRO && itemTicket.getItemType() == 2)
                    text2 = "X" + itemTicket.getSystematic() + "  " + text2;

                if (!TextUtils.isEmpty(text2) && finalText != null) {
                    SpannableString span2 = new SpannableString(text2);
                    span2.setSpan(new AbsoluteSizeSpan(textSize18), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
                    span2.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), 0, text2.length(), 0);// set color
                    finalText = TextUtils.concat(finalText, "  ", span2);
                }
            }
        }

        String text2 = draw;
        if (!TextUtils.isEmpty(text2) && finalText != null) {
            SpannableString span2 = new SpannableString(text2);
            span2.setSpan(new AbsoluteSizeSpan(textSize14), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
            span2.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, text2.length(), 0);// set color
            span2.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
                    , 0, text2.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            finalText = TextUtils.concat(finalText, "\n", span2);
        }

        return finalText;
    }
}
