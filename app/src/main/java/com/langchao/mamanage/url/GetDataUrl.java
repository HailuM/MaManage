package com.langchao.mamanage.url;

public class GetDataUrl {

	private static String URL = "http://172.16.20.110:8080/huihuan-system/";// 项目地址
	private static String HWMANAGE_URL = "http://172.16.20.110:8080/huihuan-hwmanage/";// 项目地址
	
	/** 用户登录**/
	public static String USER_LOGIN = URL + "api/checkUser.do";

	/**查询数据字典**/
	public static String CONSULT= URL + "api/consult.do";

	/**获得危废列表**/
	public static String GET_WASTE_LIST= URL + "api/getWasteList.do";

	/**库存统计**/
	public static String GET_STORE= HWMANAGE_URL + "app/getStore.do";

	/**绑定标签**/
	public static String SAVE_RFID= HWMANAGE_URL + "app/saveRfid.do";

	/**获得仓库列表**/
	public static String GET_COMPANY_STORAGE= URL + "api/getCompanyStorage.do";

	/**根据rfid查询危废信息**/
	public static String FIND_RFID= HWMANAGE_URL + "app/findRfid.do";

	/**称重入库**/
	public static String SAVE_STORE= HWMANAGE_URL + "app/saveStore.do";

	/**查询联单**/
	public static String GET_TRANSFER_MAINFEST= HWMANAGE_URL + "app/getTransferMainfest.do";

	/**出库查询危废信息**/
	public static String GET_WASTE_PUT_INFO= HWMANAGE_URL + "app/getwastePutInfo.do";

	/**保存出库信息**/
	public static String SAVE_TRANS_FERRF_ID= HWMANAGE_URL + "app/saveTransferRfid.do";

	/**查询联单详情**/
	public static String GET_WASTE_INFO_DETAIL= HWMANAGE_URL + "app/getwasteInfoDetail.do";

	/**经营单位-危废库存详情**/
	public static String GET_WASTE_DETAIL= HWMANAGE_URL + "app/getWasteDetail.do";

	/**经营单位-联单管理-联单查询**/
	public static String GET_WDTRANSFER_MAINFEST= HWMANAGE_URL + "app/getWDTransferMainfest.do";
}
