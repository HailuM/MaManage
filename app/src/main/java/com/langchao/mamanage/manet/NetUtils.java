package com.langchao.mamanage.manet;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.activity.MainActivity;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.converter.MaConvert;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.ic_out.Ic_outbill_b;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.image.BillImage;
import com.langchao.mamanage.dialog.MessageDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by miaohl on 2016/6/24.
 */
public class NetUtils {

    public static String getIp() {
        return "http://" + x.app().getSharedPreferences(MaConstants.FILENAME, 0).getString(MaConstants.PARA_IP, "58.221.4.34:9310");

    }


    public static String URL_TOLOGIN = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=ToLogin";

    public static String URL_MOBILE_DOWNLOADORDERINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderInfo";

    public static String URL_MOBILE_DOWNLOADORDERMATERIAL = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderMaterial";

    public static String URL_MOBILE_DOWNLOADORDERCONSUMER = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadOrderconsumer";

    public static String URL_MOBILE_DOWNLOADRECEIVEINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_downloadReceiveInfo";

    public static String URL_MOBILE_DOWNLOADRECEIVEMATERIAL = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadReceiveMaterial";

    public static String URL_MOBILE_DOWNLOADRECEIVECONSUMER = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownloadReceiveconsumer";


    public static String URL_MOBILE_UPLOADZRZCINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_uploadZRZCInfo";

    public static String URL_MOBILE_UPLOADRKINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_uploadrkInfo";

    public static String URL_MOBILE_UPLOADCKINFO = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_uploadckInfo";

    public static String URL_MOBILE_DOWNLOADORDERCOMPLETE = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownLoadOrderComplete";

    public static String URL_MOBILE_DOWNLOADRECEIVECOMPLETE = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_DownLoadReceiveComplete";

    public static String URL_MOBILE_UPLOADRKCOMPLETE = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_uploadrkComplete";

    public static String URL_MOBILE_UPLOADCKCOMPLETE = "/ZNWZCRK/othersource/ZhongNanWuZiMobileServices.asmx?op=Mobile_uploadckComplete";

    public static String URL_MOBILE_UPLOADIMAGEE = "/ReceiveImg/ReceiveImg.ashx?";


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


        String url = getIp() + URL_TOLOGIN;

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

                    callback.onSuccess(xmlrs.split(";")[0], xmlrs.split(";")[2]);
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
     */
    public static JSONObject Mobile_DownloadOrderInfo(final String userOID) throws Throwable {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownloadOrderInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "    </Mobile_DownloadOrderInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);


        String url = getIp() + URL_MOBILE_DOWNLOADORDERINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderInfoResult", result);
        if (xmlrs.startsWith("false")) {

            throw new RuntimeException(xmlrs.replace("false:", ""));

        } else {

            return JSON.parseObject(xmlrs);

        }


    }


    /**
     * 下载订单表体
     *
     * @param userOID
     */
    public static JSONArray Mobile_DownloadOrderMaterial(final String userOID, final String orderId, final String rktokenStr) throws Throwable {

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


        String url = getIp() + URL_MOBILE_DOWNLOADORDERMATERIAL;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.

        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderMaterialResult", result);
        if (xmlrs.startsWith("false")) {

            throw new RuntimeException(xmlrs.replace("false:", ""));

        } else {

            JSONArray array = JSON.parseArray(xmlrs);
            if (array.size() > 0) {
                return array;
            } else {
                // System.out.println(array.size()  + "///" +  xmlrs);
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
    public static JSONArray Mobile_DownloadOrderconsumer(final String userOID, final String orderId, final String rktokenStr) throws Throwable {

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


        String url = getIp() + URL_MOBILE_DOWNLOADORDERCONSUMER;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadOrderconsumerResult", result);
        if (xmlrs.startsWith("false")) {

            throw new RuntimeException(xmlrs.replace("false:", ""));

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
     */
    public static JSONObject Mobile_downloadReceiveInfo(final String userOID) {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_downloadReceiveInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "    </Mobile_downloadReceiveInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("(userOID)", userOID);


        String url = getIp() + URL_MOBILE_DOWNLOADRECEIVEINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = null;
        try {
            result = callForResult(params);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        if (null == result) {
            return null;
        }
        String xmlrs = NetUtils.getValueFromXML("Mobile_downloadReceiveInfoResult", result);
        if (xmlrs.startsWith("false")) {

            throw new RuntimeException(xmlrs.replace("false:", ""));

        } else {

            return JSON.parseObject(xmlrs);
        }


    }


    /**
     * 下载入库单表体
     *
     * @param userOID
     */
    public static JSONArray Mobile_DownloadReceiveMaterial(final String userOID, final String receiveId, final String cktokenStr) {

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


        String url = getIp() + URL_MOBILE_DOWNLOADRECEIVEMATERIAL;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = null;
        try {
            result = callForResult(params);
        } catch (Throwable throwable) {
            return null;
        }
        if (null == result) {
            return null;
        }
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadReceiveMaterialResult", result);
        if (xmlrs.startsWith("false")) {

            throw new RuntimeException(xmlrs.replace("false:", ""));

        } else {

            JSONArray array = JSON.parseArray(xmlrs);

            return array;
        }


    }


    /**
     * 下载入库单领料商
     *
     * @param userOID
     */
    public static JSONArray Mobile_DownloadReceiveconsumer(final String userOID, final String receiveId, final String cktokenStr) throws Throwable {

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


        String url = getIp() + URL_MOBILE_DOWNLOADRECEIVECONSUMER;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        try {
            String result = callForResult(params);
            String xmlrs = NetUtils.getValueFromXML("Mobile_DownloadReceiveconsumerResult", result);
            if (xmlrs.startsWith("false")) {

                throw new RuntimeException(xmlrs.replace("false:", ""));

            } else {
                JSONArray array = JSON.parseArray(xmlrs);
                return array;
            }
        } catch (Throwable throwable) {
            throw throwable;
        }

    }


    /**
     * 直入直出上传
     *
     * @param bill
     * @param rkToken
     * @param userId
     * @return
     * @throws Throwable
     */
    public static boolean uploadZrzc(Ic_diroutbill_b bill, String rkToken, String userId) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_uploadZRZCInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "      <jsonData>(jsonData)</jsonData>\n" +
                "    </Mobile_uploadZRZCInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        Ic_diroutbill head = new MaDAO().queryNewHeadDir(bill.getOrderid());
        JSONObject jo = new JSONObject();
        jo.put("orderid", bill.getSourceId()); //来源订单主表ID
        jo.put("preparertime", MaConvert.formatData(bill.getTime())); //制单时间  yyyy-mm-dd hh:mm:ss

        jo.put("consumerid", head.getConsumerid());//领料商ID
        jo.put("orderEntryid", bill.getSourcebId()); //来源订单子表ID
        jo.put("zrzcid", bill.getOrderentryid());//生成的直入直出ID  UUID
        jo.put("qty", bill.getSourceQty());//数量
        jo.put("printcount", bill.getPrintcount() == null ? 0 : bill.getPrintcount()); //打印次数
        jo.put("deliverNo", head.getNumber()); //生成的出库单号

        jo.put("receiverOID", head.getReceiverOID()); //来源的入库单的 主表ID

        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(rktokenStr)", rkToken);
        xml = xml.replace("(jsonData)", jo.toJSONString());

        String url = getIp() + URL_MOBILE_UPLOADZRZCINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_uploadZRZCInfoResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }
    }

    public static String getValueFromXML(String field, String xml) {
        int start = xml.indexOf("<" + field + ">") + ("<" + field + ">").length();

        int end = xml.indexOf("</" + field + ">");

        return xml.substring(start, end);
    }

    /**
     * 上传入库单
     *
     * @param bill
     * @param rkToken
     * @param userId
     * @return
     * @throws Throwable
     */
    public static boolean uploadInbill(Ic_inbill_b bill, String rkToken, String userId) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_uploadrkInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "      <jsonData>(jsonData)</jsonData>\n" +
                "    </Mobile_uploadrkInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        JSONObject jo = new JSONObject(); //上传的时候按行明细上传
        jo.put("orderid", bill.getSourceId());  //来源订单表头ID
        jo.put("preparertime", MaConvert.formatData(bill.getTime()));  //制单时间  格式 yyyy-mm-dd hh:mm:ss

        jo.put("orderEntryid", bill.getSourcebId()); //来源订单表体ID
        jo.put("receiveid", bill.getOrderid());  //生成的入库单主表ID  我使用的是UUID
        jo.put("qty", bill.getSourceQty()); //入库单行的数量

        jo.put("wareentryid", bill.getOrderentryid()); //入库单行ID    UUID生成

        xml = xml.replace("(userOID)", userId);  //用户ID
        xml = xml.replace("(rktokenStr)", rkToken); //上次下载订单后的  入库TOKEN
        xml = xml.replace("(jsonData)", jo.toJSONString());

        String url = getIp() + URL_MOBILE_UPLOADRKINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_uploadrkInfoResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }
    }


    /**
     * 上传出库单
     *
     * @param bill
     * @param ckToken
     * @param userId
     * @param type
     * @return
     * @throws Throwable
     */
    public static boolean uploadOutbill(Ic_outbill_b bill, String ckToken, String userId, String type) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_uploadckInfo xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <cktokenStr>(cktokenStr)</cktokenStr>\n" +
                "      <jsonData>(jsonData)</jsonData>\n" +
                "      <type>(type)</type>\n" +
                "    </Mobile_uploadckInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        Ic_outbill head = new MaDAO().queryNewHead(bill.getOrderid());

        JSONObject jo = new JSONObject();
        jo.put("orderid", bill.getSourceId());  //来源订单主表ID   从订单带到入库单 带到出库单
        if (null == bill.getCreateType() || !bill.getCreateType().equals(MaConstants.TYPE_SYNC)) {
            // type = "rkck";
        }
        jo.put("preparertime", MaConvert.formatData(bill.getTime())); //制单时间  yyyy-mm-dd hh:mm:ss
        jo.put("deliverNo", bill.getNumber()); //生成的出库单号  打印的时候有个单号 用来和系统对应  我使用的年月日+自增长+纳秒后5位   2016-02-03-1*****
        jo.put("consumerid", head.getConsumerid());//领料商ID  界面选择的
        jo.put("orderEntryid", bill.getSourcebId()); //来源订单子表ID 从订单带到入库单带到出库单
        jo.put("deliverid", bill.getOrderentryid()); //生成的出库单子表ID
        jo.put("qty", bill.getSourceQty()); //出库数量
        jo.put("printcount", bill.getPrintcount() == null ? 0 : bill.getPrintcount()); //打印次数
        jo.put("receiveid", bill.getReceiveid()); //来源的入库单的 主表ID

        // jo.put("inbillid",bill.getReceiveid()); //来源的入库单的 主表ID

        jo.put("receiverOID", head.getReceiverOID()); //领用商ID
        jo.put("wareentryid", bill.getWareentryid()); //来源的入库单的 子表ID


        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(cktokenStr)", ckToken);
        xml = xml.replace("(jsonData)", jo.toJSONString());
        xml = xml.replace("(type)", type);  //区分是 同步入库的时候  还是 同步出库的时候上传的

        String url = getIp() + URL_MOBILE_UPLOADCKINFO;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_uploadckInfoResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }
    }

    public static boolean Mobile_DownLoadOrderComplete(String userId, String rktokenStr) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownLoadOrderComplete xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "    </Mobile_DownLoadOrderComplete>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";


        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(rktokenStr)", rktokenStr);

        String url = getIp() + URL_MOBILE_DOWNLOADORDERCOMPLETE;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownLoadOrderCompleteResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }

    }


    public static boolean Mobile_DownLoadReceiveComplete(String userId, String cktokenStr) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_DownLoadReceiveComplete xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <cktokenStr>(cktokenStr)</cktokenStr>\n" +
                "    </Mobile_DownLoadReceiveComplete>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";


        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(cktokenStr)", cktokenStr);

        String url = getIp() + URL_MOBILE_DOWNLOADRECEIVECOMPLETE;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_DownLoadReceiveCompleteResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }

    }


    public static boolean Mobile_uploadrkComplete(String userId, String rktokenStr, int zrzcBillCount, int rkBillCount, int ckBillCount) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_uploadrkComplete xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <rktokenStr>(rktokenStr)</rktokenStr>\n" +
                "      <zrzcBillCount>(zrzcBillCount)</zrzcBillCount>\n" +
                "      <rkBillCount>(rkBillCount)</rkBillCount>\n" +
                "      <ckBillCount>(ckBillCount)</ckBillCount>\n" +
                "    </Mobile_uploadrkComplete>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";


        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(rktokenStr)", rktokenStr);
        xml = xml.replace("(zrzcBillCount)", zrzcBillCount + "");
        xml = xml.replace("(rkBillCount)", rkBillCount + "");
        xml = xml.replace("(ckBillCount)", ckBillCount + "");

        String url = getIp() + URL_MOBILE_UPLOADRKCOMPLETE;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);
        String xmlrs = NetUtils.getValueFromXML("Mobile_uploadrkCompleteResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }

    }


    public static boolean Mobile_uploadckComplete(String userId, String cktokenStr, int ckBillCount) throws Throwable {
        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Mobile_uploadckComplete xmlns=\"http://tempuri.org/\">\n" +
                "      <userOID>(userOID)</userOID>\n" +
                "      <cktokenStr>(cktokenStr)</cktokenStr>\n" +
                "      <ckBillCount>(ckBillCount)</ckBillCount>\n" +
                "    </Mobile_uploadckComplete>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";


        xml = xml.replace("(userOID)", userId);
        xml = xml.replace("(cktokenStr)", cktokenStr);
        xml = xml.replace("(ckBillCount)", ckBillCount + "");

        String url = getIp() + URL_MOBILE_UPLOADCKCOMPLETE;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter(
                "data",
                xml,
                "text/xml"); // 如果文件没有扩展名, 最好设置contentType参数.
        String result = callForResult(params);

        String xmlrs = NetUtils.getValueFromXML("Mobile_uploadckCompleteResult", result);
        if (xmlrs.contains("true")) {

            return true;
            //Toast.makeText(x.app(),  xmlrs.split(";")[0], Toast.LENGTH_LONG).show();
        } else {
            throw new RuntimeException(xmlrs.replace("false:", ""));
        }

    }

    public static String callForResult(RequestParams params) throws Exception {

        try {

            //params.addBodyParameter("image",new File(""));

            String result = x.http().postSync(params, String.class);
            return result;
        } catch (Throwable throwable) {
            throw new Exception("当前的网络连接不可用，数据上传和下载功能无法使用，请退出应用并到有网络的环境中再打开！");

        }

    }

    public static void uploadImage(final Context context, BillImage billImage) throws Exception {

//        String url = "http://58.221.4.34:5598/ReceiveImg/ReceiveImg.ashx?" + "id=" + UUID.randomUUID().toString() + "&lx=" + billImage.getLx();
        String url = getIp() + URL_MOBILE_UPLOADIMAGEE+"id="+billImage.getBillid()+"&lx="+billImage.getLx();
        RequestParams params = new RequestParams(url);
        File image = new File(billImage.getImagePath());
        if(!image.exists())
            return;

//        Toast.makeText(context,image.length()+"",Toast.LENGTH_SHORT).show();
//       String path =   saveCroppedImage(BitmapFactory.decodeFile(billImage.getImagePath()));
//        File image2 = new File(path);
//        Toast.makeText(context,image2.length()+"",Toast.LENGTH_SHORT).show();
//        if(!image2.exists()){
//            return;
//
//        }
        params.addBodyParameter("file2", image);
        //  MessageDialog.show(context,callForResult(params));
        callForResult(params);
//
//        Callback.Cancelable result = x.http().post(params, new Callback.CommonCallback<String>() {
//
//
//            @Override
//            public void onSuccess(String result) {
//                MessageDialog.show(context, result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                //MessageDialog.show(context, "onError");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//               // MessageDialog.show(context, "onCancelled");
//            }
//
//            @Override
//            public void onFinished() {
//
//                //MessageDialog.show(context, "onFinished");
//            }
//        });
    }

    private static String saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
        String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            return file.getPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
