package com.langchao.mamanage.manet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by miaohl on 2016/6/24.
 */
public class NetUtils {

    public static String ip = "http://58.221.4.34:9310";


    public static String URL_TOLOGIN = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=ToLogin";

    public static String URL_MOBILE_DOWNLOADORDERINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderInfo";

    public static String URL_MOBILE_DOWNLOADORDERMATERIAL = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderMaterial";

    public static String URL_MOBILE_DOWNLOADORDERCONSUMER = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderconsumer";

    public static String URL_MOBILE_DOWNLOADRECEIVEINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_downloadReceiveInfo";

    public static String URL_MOBILE_DOWNLOADRECEIVEMATERIAL = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadReceiveMaterial";

    public static String URL_MOBILE_DOWNLOADRECEIVECONSUMER = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadReceiveconsumer";


    /**
     * 登录接口
     *
     * @param userName
     * @param pwd
     * @param callback
     */
    public static void ToLogin(final String userName, String pwd, final MaCallback.ToLoginCallBack callback) {

        String xml = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <ToLogin xmlns=\"http://tempuri.org/\">\n" +
                "      <userName>(userName)</userName>\n" +
                "      <pwd>(pwd)</pwd>\n" +
                "    </ToLogin>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        xml = xml.replace("(userName)", userName);
        xml = xml.replace("(pwd)", pwd);


        String url = ip + URL_TOLOGIN;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String xmlrs = NetUtils.getValueFromXML("ToLoginResult", result);
                if (xmlrs.contains(userName)) {

                    callback.onSuccess(xmlrs.split(";")[0]);
                    //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
                } else {
                    callback.onError(new RuntimeException("登录失败"));
                }

            }

            @Override
            public void onError(Throwable ex, boolean isCallBack) {
                callback.onError(ex);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    /**
     * 下载订单表头
     *
     * @param userOID
     * @param callback
     */
    public static void Mobile_DownloadOrderInfo(final String userOID, final MaCallback.MainInfoCallBack callback) {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadOrderInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "    </Mobile_DownloadOrderInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);


        String url = ip + URL_MOBILE_DOWNLOADORDERINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderInfoResult", result);
                if (xmlrs.startsWith("false")) {

                    callback.onError(new RuntimeException(xmlrs));

                } else {

                    try {
                        callback.onSuccess(JSON.parseObject(xmlrs));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isCallBack) {
                callback.onError(ex);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    /**
     * 下载订单表体
     *
     * @param userOID
     *
     */
    public static JSONArray Mobile_DownloadOrderMaterial(final String userOID, final String orderId, final String rktokenStr ) throws Throwable {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadOrderMaterial xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <orderId>(orderId)</orderId>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "    </Mobile_DownloadOrderMaterial>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);
        xml = xml.replace("(orderId)", orderId);
        xml = xml.replace("(rktokenStr)", rktokenStr);


        String url = ip + URL_MOBILE_DOWNLOADORDERMATERIAL;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.

        String result = x.http().postSync(params, String.class);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderMaterialResult", result);
        if (xmlrs.startsWith("false")) {

            return null;

        } else {

            JSONArray array = JSON.parseArray(xmlrs);
            if (array.size() > 0) {
                return array;
            } else {
               return null;
            }
        }


//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderMaterialResult", result);
//                if (xmlrs.startsWith("false")) {
//
//                    callback.onError(new RuntimeException(xmlrs));
//
//                }else{
//
//                    JSONArray array =  JSON.parseArray(xmlrs);
//                    if(array.size() > 0)
//                    {
//                        callback.onSuccess(JSON.parseArray(xmlrs));
//                    }else{
//                        callback.onError(new RuntimeException("下载失败"));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable ex ,boolean isCallBack) {
//                callback.onError(ex);
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });


    }


    /**
     * 下载订单领料商
     *
     * @param userOID

     */
    public static JSONArray Mobile_DownloadOrderconsumer(final String userOID, final String orderId, final String rktokenStr ) throws Throwable {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadOrderconsumer xmlns=\"http://tempuri.org/\">\n" +
                "       <userOID>(userOID)</userOID>\n" +
                "      <orderId>(orderId)</orderId>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "    </Mobile_DownloadOrderconsumer>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);
        xml = xml.replace("(orderId)", orderId);
        xml = xml.replace("(rktokenStr)", rktokenStr);


        String url = ip + URL_MOBILE_DOWNLOADORDERCONSUMER;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = x.http().postSync(params,String.class);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderconsumerResult", result);
        if (xmlrs.startsWith("false")) {

           return null;

        } else {

            JSONArray array = JSON.parseArray(xmlrs);
            if (array.size() > 0) {
                return JSON.parseArray(xmlrs);
            } else {
               return null;
            }
        }


    }


    /**
     * 下载入库表头
     *
     * @param userOID
     *
     */
    public static JSONObject Mobile_downloadReceiveInfo(final String userOID)   {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_downloadReceiveInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "    </Mobile_downloadReceiveInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);


        String url = ip + URL_MOBILE_DOWNLOADRECEIVEINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = null;
        try {
            result = x.http().postSync(params,String.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if(null == result){
            return null;
        }
        String xmlrs = NetUtils.getValueFromXML("Mobile_downloadReceiveInfoResult", result);
        if (xmlrs.startsWith("false")) {

            return null;

        } else {

            return JSON.parseObject(xmlrs);
        }


    }


    /**
     * 下载入库单表体
     *
     * @param userOID

     */
    public static JSONArray Mobile_DownloadReceiveMaterial(final String userOID, final String receiveId, final String cktokenStr ) {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadReceiveMaterial xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <receiveId>(receiveId)</receiveId>\n" +
                "      <cktokenStr>(cktokenStr)</cktokenStr>\n" +
                "    </Mobile_DownloadReceiveMaterial>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);
        xml = xml.replace("(receiveId)", receiveId);
        xml = xml.replace("(cktokenStr)", cktokenStr);


        String url = ip + URL_MOBILE_DOWNLOADRECEIVEMATERIAL;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = null;
        try {
            result = x.http().postSync(params, String.class);
        } catch (Throwable throwable) {
            return null;
        }
        if(null == result){
            return null;
        }
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadReceiveMaterialResult", result);
        JSONArray array = JSON.parseArray(xmlrs);

        return array;

    }


    /**
     * 下载入库单领料商
     *
     * @param userOID

     */
    public static JSONArray  Mobile_DownloadReceiveconsumer(final String userOID, final String receiveId, final String cktokenStr ) {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadReceiveconsumer xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <receiveId>(receiveId)</receiveId>\n" +
                "      <cktokenStr>(cktokenStr)</cktokenStr>\n" +
                "    </Mobile_DownloadReceiveconsumer>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);
        xml = xml.replace("(receiveId)", receiveId);
        xml = xml.replace("(cktokenStr)", cktokenStr);


        String url = ip + URL_MOBILE_DOWNLOADRECEIVECONSUMER;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        try {
            String result = x.http().postSync(params, String.class);
            String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadReceiveconsumerResult", result);
            JSONArray array = JSON.parseArray(xmlrs);
            return  array;
        } catch (Throwable throwable) {
            return null;
        }

    }


    public static String getValueFromXML(String field, String xml) {
        int start = xml.indexOf("<" + field + ">") + ("<" + field + ">").length();

        int end = xml.indexOf("</" + field + ">");

        return xml.substring(start, end);
    }
}
