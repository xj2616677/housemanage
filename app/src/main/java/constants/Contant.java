package constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Environment;

import bean.CheckBaseInfoBean;
import bean.CheckTermGroupBean;
import bean.GeneralBean;
import bean.HouseSafeBean;
import bean.InfoAttributeBean;
import bean.PrimeBrokerBean;
import bean.PropertyBean;
import bean.StreetBean;
import bean.TableHeadBean;
import bean.UserBean;


public class Contant {
	public static String userid = "";
	public static String FGSID="11010801";

	public static UserBean USERBEAN = null;

	public static int FLAG= 0;

	public static ArrayList<String> enforceList = new ArrayList<>();
	public static Map<String,List> brokerMap = new HashMap<>();
//所选择的对象
	public static List<PrimeBrokerBean> primeObjectList = new ArrayList<>();
	public static List<PropertyBean> propertyObjectList = new ArrayList<>();
	public static List<GeneralBean> generalObjectList = new ArrayList<>();
	public static List<HouseSafeBean> houseSafeObjectList = new ArrayList<>();
//所选择的检查项
	public static Map<String,List<CheckTermGroupBean>> checkedTerms = new HashMap<>();
//所选择的表头
	public static List<TableHeadBean> primeHeadList = new ArrayList<>();
	public static List<TableHeadBean> propertyHeadList = new ArrayList<>();
	public static List<TableHeadBean> generalHeadList = new ArrayList<>();
	public static List<TableHeadBean> houseHeadList = new ArrayList<>();

	public static List<InfoAttributeBean> infoAttributeBeans = new ArrayList<>();

	public static List<StreetBean> streetBeans = new ArrayList<>();

	public static String streetInfo = "";

	public static CheckBaseInfoBean checkBaseInfoBean;

	public static String filepath = Environment.getExternalStorageDirectory()
			.toString() + File.separator + "海淀房管局文件" + File.separator;


	public static String filepathword = filepath + "word" + File.separator;
	public static String filepath1 = filepath + "videos" + File.separator;

	public static String fileName = "";

	public static boolean isXCZG = false;
	public static boolean isXQZG = false;
	public static boolean isChange = true;

	public static String longitube = "-1";
	public static String latitube = "-1";

	public static boolean isChooseMap = false;

	public static boolean isAddObject = false;

	public static String houseLocate = "";//地下室房屋坐落

	public static String buildsite = "";//房屋坐落

	public static String base_id = "";//增加的地下室id

	public static String buildNo = "";//增加的房屋id


	public static String proName = "";//带到增加页面的项目名称


	public static List<Bitmap> personBitmapList = new ArrayList<>();
	public static List<Bitmap> objectBitmapList = new ArrayList<>();
	public static Bitmap objectBitmap;
	public static Bitmap personBitmap;


	public static boolean isSign = false;
	public static int signIndex = 1;


	public static String dbName = "checkinfo.db";
	public static int dbVersion = 3;//整合所有以前的version为初始版本2


	public static String loginTime = "";

	public static PrimeBrokerBean newAddprimeBrokerBean = null;
	public static PropertyBean newAddpropertyBean = null;

	public static boolean  isAddjjjgorpro = false;









}
