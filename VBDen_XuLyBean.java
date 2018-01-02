/**
 *--------------------CREATE----------------------
 * @creator  : ltbao
 * @date     : May 23, 2014 9:32:48 AM
 * @filename : VBDen_XuLyBean.java on project hcdt-portlet
 * @purpose  : Bean điều khiển chức năng xử lý văn bản đến
 * @version  : v1.0
 * @copyright: CUSC
 *---------------------EDIT-----------------------
 * @editor      : ltbao
 * @date        : 9:32:48 AM
 * @purpose     : 
 * @changedSign : (ex: //@nlphuong) 
 */
package com.cusc.hcdt.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.event.DateSelectEvent;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cusc.hcdt.bayux.client.NotificationClient;
import com.cusc.hcdt.bayux.client.NotificationVariables.NotificationsVar;
import com.cusc.hcdt.dao.EmailDataProvider;
import com.cusc.hcdt.facade.CauHinhDoMatFacade;
import com.cusc.hcdt.facade.DmCachThucXuLyFacade;
import com.cusc.hcdt.facade.DmCapDonViFacade;
import com.cusc.hcdt.facade.DmDoKhanFacade;
import com.cusc.hcdt.facade.DmDoMatFacade;
import com.cusc.hcdt.facade.DmDonViFacade;
import com.cusc.hcdt.facade.DmLinhVucFacade;
import com.cusc.hcdt.facade.DmLinkFacade;
import com.cusc.hcdt.facade.DmLoaiVanBanFacade;
import com.cusc.hcdt.facade.DmSoVanBanFacade;
import com.cusc.hcdt.facade.DmVanBanLienDonViFacade;
import com.cusc.hcdt.facade.HoSoVanBanFacade;
import com.cusc.hcdt.facade.KetQuaXuLyFacade;
import com.cusc.hcdt.facade.KetQuaXuLyTapTinFacade;
import com.cusc.hcdt.facade.MailPhoBienFacade;
import com.cusc.hcdt.facade.TapTinDinhKemBieuMauFacade;
import com.cusc.hcdt.facade.TapTinDinhKemLuanChuyenFacade;
import com.cusc.hcdt.facade.TapTinDinhKemVanBanFacade;
import com.cusc.hcdt.facade.TapTinGopYFacade;
import com.cusc.hcdt.facade.ThongTinGopYFacade;
import com.cusc.hcdt.facade.VBDenFacade;
import com.cusc.hcdt.facade.VBDen_LuanChuyenFacade;
import com.cusc.hcdt.facade.VBDen_XuLyFacade;
import com.cusc.hcdt.facade.VanBanPhoiHopFacade;
import com.cusc.hcdt.model.CauHinhDoMatModel;
import com.cusc.hcdt.model.ChuyenBoSungModel;
import com.cusc.hcdt.model.DmCapDonViModel;
import com.cusc.hcdt.model.DmDonViModel;
import com.cusc.hcdt.model.DmLinhVucModel;
import com.cusc.hcdt.model.DmSoVanBanModel;
import com.cusc.hcdt.model.DmVanBanLienDonViModel;
import com.cusc.hcdt.model.DsEmail;
import com.cusc.hcdt.model.KetQuaXuLyModel;
import com.cusc.hcdt.model.KetQuaXuLyTapTinModel;
import com.cusc.hcdt.model.MailPhoBienModel;
import com.cusc.hcdt.model.TapTinDinhKemLuanChuyenModel;
import com.cusc.hcdt.model.TapTinDinhKemVanBanModel;
import com.cusc.hcdt.model.TapTinGopYModel;
import com.cusc.hcdt.model.ThongTinGopYModel;
import com.cusc.hcdt.model.VBDenModel;
import com.cusc.hcdt.model.VBDen_LuanChuyenModel;
import com.cusc.hcdt.model.VBDen_XuLyFilterModel;
import com.cusc.hcdt.model.VanBanPhoiHopModel;
import com.cusc.hcdt.props.VBDen_XuLyProps;
import com.cusc.hcdt.util.CollectionUtil;
import com.cusc.hcdt.util.ExcelFormatUtils;
import com.cusc.hcdt.util.HCDTResource;
import com.cusc.hcdt.util.JSFformUtil;
import com.cusc.hcdt.util.MapComparator;
import com.cusc.hcdt.util.UserUtil;
import com.cusc.hcdt.util.Validator;
import com.cusc.hcdt.util.common.CommonUtils;
import com.cusc.hcdt.util.common.CommonUtils.Hcdt;
import com.cusc.hcdt.util.common.DateUtils;
import com.cusc.hcdt.util.common.IActionMode;
import com.cusc.hcdt.util.common.IContanst;
import com.cusc.hcdt.util.jsf.JavascriptUtils;
import com.cusc.hcdt.util.permission.HcdtRoles;
import com.cusc.hcdt.util.permission.HcdtRoles.HcdtRole;
import com.cusc.hcdt.util.uploadFile.CheckFileExtensionStatus;
import com.cusc.hcdt.util.uploadFile.CheckMimeTypeStatus;
import com.cusc.hcdt.util.uploadFile.MaxFileSizeStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.liferay.faces.bridge.util.FileNameUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * @author ltbao
 * @date May 23, 2014
 * @purpose 
 */
@ManagedBean(name="VBDen_XuLyBean")
@ViewScoped
public class VBDen_XuLyBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1432371716667617972L;	
	
	/*Props*/
	@ManagedProperty(value ="#{VBDen_XuLyProps}")
	private VBDen_XuLyProps props = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{VBDen_XuLyProps}", VBDen_XuLyProps.class);
	@ManagedProperty(value ="#{HcdtThongSoLoader}")
	private HcdtThongSoLoader loader = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{HcdtThongSoLoader}", HcdtThongSoLoader.class);
	@ManagedProperty(value ="#{DmDonViBean}")
	private DmDonViBean propsDonVi = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{DmDonViBean}", DmDonViBean.class);
	@ManagedProperty(value ="#{LienKetVanBanBean}")
	private LienKetVanBanBean propsLkvb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{LienKetVanBanBean}", LienKetVanBanBean.class);
	@ManagedProperty(value ="#{HoSoVanBanPopupBean}")
	private HoSoVanBanPopupBean propsHsvb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{HoSoVanBanPopupBean}", HoSoVanBanPopupBean.class);
	@ManagedProperty(value ="#{DmLoaiVanBanBean}")
	private DmLoaiVanBanBean propsLoaiVb = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{DmLoaiVanBanBean}", DmLoaiVanBanBean.class);
	@ManagedProperty(value ="#{DmSoVanBanPopupBean}")
	private DmSoVanBanPopupBean propsSoVanBan = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{DmSoVanBanPopupBean}", DmSoVanBanPopupBean.class);
	@ManagedProperty(value ="#{VanBanLogBean}")
	private VanBanLogBean vanBanLogBean = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{VanBanLogBean}", VanBanLogBean.class);
	@ManagedProperty(value ="#{DmLinhVucBean}")
	private DmLinhVucBean propsLinhVuc = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{DmLinhVucBean}", DmLinhVucBean.class);
	
	/*Facade*/
	private VBDen_XuLyFacade              objVBDen_XuLyFacade              =  new VBDen_XuLyFacade();
	private TapTinDinhKemVanBanFacade     objTaptinDinhKemVanBanFacade     =  new TapTinDinhKemVanBanFacade();
	private VBDenFacade                   objVBDenFacade                   =  new VBDenFacade();
	private DmLinhVucFacade               objDmLinhVucFacade               =  new DmLinhVucFacade();
	private VBDen_LuanChuyenFacade        objVBDen_LuanChuyenFacade        =  new VBDen_LuanChuyenFacade();
	private TapTinDinhKemLuanChuyenFacade objTapTinDinhKemLuanChuyenFacade =  new TapTinDinhKemLuanChuyenFacade();
	private TapTinGopYFacade              objTapTinGopYFacade              =  new TapTinGopYFacade();
	private ThongTinGopYFacade            objThongTinGopYFacade            =  new ThongTinGopYFacade();
	private DmLoaiVanBanFacade            objDmLoaiVanBanFacade            =  new DmLoaiVanBanFacade();
	private DmSoVanBanFacade              objDmSoVanBanFacade              =  new DmSoVanBanFacade();
	private DmDoMatFacade                 objDmDoMatFacade                 =  new DmDoMatFacade();
	private DmDoKhanFacade                objDmDoKhanFacade                =  new DmDoKhanFacade();
	private MailPhoBienFacade             objMailPhoBienFacade             =  new MailPhoBienFacade();
	private DmCachThucXuLyFacade          objDmCachThucXuLyFacade          =  new DmCachThucXuLyFacade();
	private DmLinkFacade				  objDmLinkFacade                  =  new DmLinkFacade();
	private NotificationClient			  objNotificationClient			   =  new NotificationClient();
	private DmVanBanLienDonViFacade		  objBanLienDonViFacade            =  new DmVanBanLienDonViFacade();
	private HoSoVanBanFacade			  objHoSoVanBanFacade			   =  new HoSoVanBanFacade();
	private TapTinDinhKemBieuMauFacade	  objTapTinDinhKemBieuMauFacade	   =  new TapTinDinhKemBieuMauFacade();	
	private CauHinhDoMatFacade			  objCauHinhDoMatFacade			   =  new CauHinhDoMatFacade();	
	private DmDonViFacade      			  objDmDonViFacade      			= new DmDonViFacade();
	private DmCapDonViFacade   			  objDmCapDonViFacade				= new DmCapDonViFacade();
	
	//private VBDi_PhatHanhFacade       	  objVBDi_PhatHanhFacade			=	new VBDi_PhatHanhFacade();
	private KetQuaXuLyFacade			  objKetQuaXuLyFacade				= new KetQuaXuLyFacade();
	private KetQuaXuLyTapTinFacade		  objKetQuaXuLyTapTinFacade			= new KetQuaXuLyTapTinFacade();
	private VanBanPhoiHopFacade 		  objVanBanPhoiHopFacade 			= new VanBanPhoiHopFacade();
	private LienKetVanBanBean 			  objLienKetVanBanBean = new LienKetVanBanBean();
	private Map<String, Map<String, String>> mapBasicUserInfo;
	private List<Map<String,Object>> listAllUserNhanXuLy = null;
	private List<Map<String,Object>> listAllUserNhanPhoBien = null;
	
	//Số đến trước khi sửa
	@SuppressWarnings("unused")
	private String soDenTmp;
	/*Biến khác*/
	private static final Logger log=LoggerFactory.getLogger(VBDen_XuLyBean.class);
	private long       userId;
	private long       organizationId;
	private long       organizationIdVb;
	
	public long getOrganizationIdVb() {
		return organizationIdVb;
	}

	public void setOrganizationIdVb(long organizationIdVb) {
		this.organizationIdVb = organizationIdVb;
	}

	private long       companyId;
	private long       phongban_org;
	private int        tabIndex=0;
	private boolean    mailPhoBien=false;
	private Resource   resource;
	private String     fileName;
	private long       vbden_id;
	private long       vbden_lc_id;
	private int        selectIndex;
	private int        tabSelect = 0;
	private String     orgTen;
	private boolean    hienHoSoVanBan = false;
	private int 	   soNgayToiHan = 0;
	private String     soDenOld;
	private long       lcIdXuLyBoSungTmp;
	//Id của người xử lý chính để gửi email
	private String 	   emailXuLyChinh = "";
	/*Object*/
	private UserUtil     objUserUtil =new UserUtil();	 
	/*List*/
	private List<SelectItem> listFilterPhongBan = new ArrayList<SelectItem>();
	private List<SelectItem> listFilterDonVi = new ArrayList<SelectItem>();
	private String vFilterDonVi;

	private List<SelectItem> listFilterPhongBanNguoiDung = new ArrayList<SelectItem>();	
	
	/*Phân quyền*/
	private boolean coQuyen         = false;
	private boolean quyenGopY       = false;
	private boolean quyenNhapButPhe = false;
	private boolean quyenLuuTru     = false;
	private boolean quyenXoa        = false;	
	private boolean quyenVanThu     = false; 
	private boolean quyenxlchinh    = false;
	private boolean quyenphobien    = false;
	private boolean hientabxuly     = false;
	private boolean suaHanXlToanVb  = false;
	private boolean quyenSuaVanBan  = false;
	private boolean hienTabSet      = false;
	private int     phamViVanThu;//1: phạm vi đơn vị, 2: phạm vi phòng ban
	private boolean xuLyThay        = false;
	//Kiểm tra quyền xử lý liên đơn vị
	private boolean hienTabLienDonVi     = false;
	private boolean hienControlLienDonVi = false;
	/*Path*/
	private String  vTmpPathLc      = "resources/upload_temp/vanban/vb_den/vb_den_lc/";
	private String  vPathLc         = "resources/upload/vanban/vb_den/vb_den_lc/";
	private String  vTmpPathGy      = "resources/upload_temp/vanban/vb_den/vb_den_gy/";
	private String  vPathGy         = "resources/upload/vanban/vb_den/vb_den_gy/";
	private String  vTmpPath        = "resources/upload_temp/vanban/vb_den/";
	private String  vPath           = "resources/upload/vanban/vb_den/";
	private String  vPathBieuMau 	= "resources/upload/bieumau/";
	private int     timeStamp       = 0;
	private int     modeVaiTro      = 0;  //0: Chuyên viên, 1:Lãnh đạo phòng; 2:lãnh đạo đơn vị
	private boolean coCauHinhCaNhan;
	//Tham số Lazy loading
	private int firstdau;
	private int sizeFull = 0;
	private int firstcuoi;
	private int sizeChange;
	private int preRowload;
	private int cPageSize;
	private int pageLoad = 3;
	private String request;
	
	//nduong nâng cấp
	private List<Long> listDvUser=new ArrayList<Long>();
	private List<Organization> listDonVi = new ArrayList<Organization>();
	private List<SelectItem> listFilterDonViTheoNguoiDung = new ArrayList<SelectItem>();
	private long orgIdTemp = 0;

	//@ptgiang Kết quả xử lý của đơn vị
	private int idxEditKqxl = -1;
	private boolean butPhe;
	private boolean coQuyenLanhDao = false;
	private String butPheHientai = null;
//	private boolean coQuyenLienDonVi;
//	private boolean coQuyenLienDonViTab;
	/**
	 * Hàm khởi tạo
	 * @throws SystemException 
	 * @throws PortalException 
	 */ 
	public VBDen_XuLyBean() throws PortalException, SystemException{
		log.info("Khởi tạo VBDen_XuLyBean");
		
		//Thêm Item phòng ban
		for(int i = 0 ; i < getUser().getOrganizations().size() ; i++){		
			listDvUser.add(getUser().getOrganizations().get(i).getOrganizationId());
		}
		propsDonVi.setListDvUser(listDvUser);
		
		//Thêm Item phòng ban
		// lnakhang: bổ sung kiểm tra nếu không có cha thì không get id
		List<Organization> lstOrganization =getUser().getOrganizations(); 
		for(int i = 0 ; i < lstOrganization.size() ; i++){
			//kiểm tra có phải đơn vị hay không 
			if(objUserUtil.isDonVi(lstOrganization.get(i))){
				if(lstOrganization.get(i).getParentOrganization() != null){
					long idOrgan = lstOrganization.get(i).getParentOrganization().getParentOrganizationId();
						if(idOrgan != 0){
							listFilterDonViTheoNguoiDung.add(new SelectItem(lstOrganization.get(i).getOrganizationId(), checkOrganNull(objUserUtil.getOrganization(idOrgan).getName())+ checkOrganNull(lstOrganization.get(i).getParentOrganization().getName())+ lstOrganization.get(i).getName()));				
						}else{
							listFilterDonViTheoNguoiDung.add(new SelectItem(lstOrganization.get(i).getOrganizationId(), checkOrganNull(lstOrganization.get(i).getParentOrganization().getName())+ lstOrganization.get(i).getName()));
						}
				}else{
					listFilterDonViTheoNguoiDung.add(new SelectItem(lstOrganization.get(i).getOrganizationId(), lstOrganization.get(i).getName()));
				}
			 }
		}			
		coQuyen=objUserUtil
				.checkLoggedIn()&&
				HcdtRoles.userHasRole(
						HcdtRole.LanhDaoDonVi,
						HcdtRole.LanhDaoPhong,
						HcdtRole.CBChuyenVien,
						HcdtRole.CBVanThu, 
						HcdtRole.MASTER); 
		quyenVanThu = (HcdtRoles.userHasRole(HcdtRole.CBVanThu));
		coCauHinhCaNhan = (Long.parseLong(loader.getUserParams().get("vOrgId").toString())!=-1000)&&(Long.parseLong(loader.getUserParams().get("vPhongBanId").toString())!=-1000);
		if(coQuyen&&coCauHinhCaNhan){
			cPageSize = Integer.parseInt(loader.getParams().get("vSoDong").toString());
			props.setVaiTro(CommonUtils.getRoleArray());
			//Kiem tra  quyen de hien tab chuyen van ban lien don vi
			hienTabLienDonVi = HcdtRoles.userHasRole(HcdtRole.Administrator,HcdtRole.CBVanThu, HcdtRole.LanhDaoDonVi, HcdtRole.LanhDaoPhong, HcdtRole.MASTER);
			hienControlLienDonVi = HcdtRoles.userHasRole(HcdtRole.Administrator,HcdtRole.CBVanThu, HcdtRole.LanhDaoDonVi, HcdtRole.MASTER)&&hienTabLienDonVi;
			
//			coQuyenLienDonVi = hienControlLienDonVi;
//			coQuyenLienDonViTab = hienTabLienDonVi;
			//Thông tin chung
			userId         = objUserUtil.getUserId();
			//nduong custom
			setOrganizationId(Long.parseLong(loader.getUserParams().get("vOrgId").toString()));
			setOrgNotification(getOrganizationId());
			//setOrganizationId(getOrgIdTemp());
			phongban_org   = Long.parseLong(loader.getUserParams().get("vPhongBanId").toString());
			phamViVanThu   = Integer.parseInt(loader.getParams().get("vPhamViVanThu").toString());
			companyId      = objUserUtil.getCompanyId();			
			props.setBasePath(CommonUtils.getBasePath());
			if(loader.getParams().get("vSoNgayThongBao")!=null){
				try{
					soNgayToiHan=Integer.parseInt(loader.getParams().get("vSoNgayThongBao").toString());
				}catch(Exception e){
					soNgayToiHan=0;
				}
			}
			try{
				orgTen = OrganizationLocalServiceUtil.getOrganization(getOrganizationId()).getName();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			props.setListBieuMau	   (objTapTinDinhKemBieuMauFacade.getDsBieuMau(1, getOrganizationId()));
			props.setListCachThucXuLy  (objDmCachThucXuLyFacade.getDsCachThucXuLy(getCompanyId(), getOrganizationId()));
			props.setListLoaiVanBan	   (objDmLoaiVanBanFacade.getDsLoaiVanBan(getCompanyId(), 0, getListDvUser()));
			props.setListSoVanBan	   (objDmSoVanBanFacade.getDsSoVbTheoNvb(1, companyId, 0,getListDvUser()));
			props.setListDoKhan	  	   (objDmDoKhanFacade.getDsDoKhan(companyId, getOrganizationId()));
			props.setListDoMat		   (objDmDoMatFacade.getDsDoMat(companyId, getOrganizationId()));
			propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade.getDsSoVbTheoNvb(1, companyId, 0,getListDvUser()));
			props.setObjVBDen_XuLyFilterModel(new VBDen_XuLyFilterModel()); 
			/*Xác định phạm vi người dùng thuộc mức độ nào*/
			butPhe = HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi,HcdtRole.LanhDaoPhong);
			coQuyenLanhDao = HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi,HcdtRole.LanhDaoPhong);
			if(HcdtRoles.userHasRole(HcdtRole.LanhDaoPhong)){
				modeVaiTro = 1;
			} else if(HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi)){
				modeVaiTro = 2;
			} else if(HcdtRoles.userHasRole(HcdtRole.CBVanThu) ){
				if(phamViVanThu == 1){
					modeVaiTro = 2;//Văn thư của đơn vị
				} else if(phamViVanThu==2){
					modeVaiTro = 1;
				}
			}
			//Nếu là MASTER thì thấy hết 18.07.2014
			if(HcdtRoles.userHasRole(HcdtRole.MASTER)){
				modeVaiTro = 2;
				xuLyThay  = true;
			}
			props.setObjVBDen_XuLyFilterModel(new VBDen_XuLyFilterModel());
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			pmGetDsChuyenXuLy();
			long time = stopWatch.getTime();
			pmGetDsPhoBien();
			stopWatch.stop();
			log.info("Time khởi tạo menu" + time);
			StopWatch stopWatch2 = new StopWatch();
					stopWatch2.start();
			//Khởi tạo filter phòng ban
			SelectItem all=new SelectItem("","--- Tất cả ---");
			all.setNoSelectionOption(true);
			listFilterPhongBan.add(all);
			List<Organization> lstOrg = objUserUtil.getDsPhongBanTheoDonVi(getOrganizationId());
			for(Organization org:lstOrg){
				listFilterPhongBan.add(new SelectItem(org.getOrganizationId(),org.getName()));
			}
			//Filter phòng ban người dùng
			List<OrganizationBase> listPhongBanNguoiDung=objUserUtil.getDsPhongBanCuaNguoiDungQuery(userId);
			listFilterPhongBanNguoiDung=new ArrayList<SelectItem>();
			for(OrganizationBase org:listPhongBanNguoiDung){
				listFilterPhongBanNguoiDung.add(new SelectItem(org.getOrganizationid(), org.getName()));
			}
			//Lấy danh sách văn bản, kiểm tra tham số từ url
			urlFilter();
			String KEY = "instant";
			request = CommonUtils.getUrlParameter(KEY);
			taoDuLieuThongKe(null);	//có lấy tổng cho lazy load nên ko bỏ được		
			props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));		
			//Set list ho so van ban
			props.setListFilterHoSoVanBan(objHoSoVanBanFacade.getDsHoSoVanBan(getUserId(), 1));
			
			//Cấu hình bật tắt chức năng
			if(loader.getCompanyParams().get("TrichLocDuLieu") != null){
				props.setTrichLocDuLieu(Boolean.parseBoolean(loader.getCompanyParams().get("TrichLocDuLieu").toString()));
			}
			if(loader.getCompanyParams().get("HoSoVanBan") != null){
				props.setHoSoVanBan(Boolean.parseBoolean(loader.getCompanyParams().get("HoSoVanBan").toString()));
			}
			if(loader.getCompanyParams().get("LienKetVanBan") != null){
				props.setLienKetVanBan(Boolean.parseBoolean(loader.getCompanyParams().get("LienKetVanBan").toString()));
			}
			if(loader.getCompanyParams().get("XuatThongTin") != null){
				props.setXuatThongTin(Boolean.parseBoolean(loader.getCompanyParams().get("XuatThongTin").toString()));
			}
			time = stopWatch2.getTime();
			stopWatch2.stop();
			log.info("Time khởi tạo " + time);
		}
	}

	public String checkOrganNull(Object name){
		if(name.toString() == null){
			return "";
		}else{
			return name.toString()+"/";
		}
	}
	
	public void actionDonViVBCHTChange(ValueChangeEvent ev){
		setOrganizationId(Long.parseLong(ev.getNewValue().toString()));
		try {
			if(getOrganizationId() != 0){
				orgTen = objUserUtil.getOrganization(getOrganizationId()).getName();
			}else{
				orgTen = "";
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} 
		propsDonVi.setOrgId(getOrganizationId());
		propsLinhVuc.setOrganizationid(getOrganizationId());
		propsLoaiVb.setOrganizationid(getOrganizationId());
		props.setListLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(companyId, getOrganizationId(), getListDvUser()));
		props.setListSoVanBan(objDmSoVanBanFacade.getDsSoVbTheoNvb(1, companyId, getOrganizationId(),getListDvUser()));
	}
	
	public void actionChonThemMoiLVB(ActionEvent ae){
		try {
			log.info(">>actionChonThemMoiLVB");
			props.getObjVBDenModel().setLvb_id(propsLoaiVb.getLvbId_ThemMoi()); 
		} catch (Exception ex) {
			log.error("err ", ex);		
		}
	}
	public void actionChonThemMoiSVB(ActionEvent ae){
		try {
			log.info(">>actionChonThemMoiSVB");
			props.getObjVBDenModel().setSvb_id(propsSoVanBan.getSvbId_ThemMoi()); 
		} catch (Exception ex) {
			log.error("err ", ex);
		}
	}
	
	public void reloadListFilterHSVB(AjaxBehaviorEvent evt){
		String dsHsvbId = "";
		if(props.getListFilterHoSoVanBan()!=null){
			for(Map<String,Object> map:props.getListFilterHoSoVanBan()){
				if(map.get("hsvb_id")!=null&&Boolean.parseBoolean(map.get("chon").toString())){
					dsHsvbId = dsHsvbId + "@"+map.get("hsvb_id").toString()+"@";
				}
			}
		}
		props.setListFilterHoSoVanBan(objHoSoVanBanFacade.getDsHoSoVanBan(getUserId(), 1));
		for(Map<String,Object> map:props.getListFilterHoSoVanBan()){
			if(map.get("hsvb_id")!=null&&dsHsvbId.contains("@"+map.get("hsvb_id").toString()+"@")){
				map.put("chon",true);
			}
		}
	}
	
	@SuppressWarnings({ "serial"})
	private LazyDataModel<Map<String,Object>> getLazyDataModel( final int sotrang, final int sodongcuatrang, boolean chonLai, final VBDen_XuLyFilterModel objLocTheoHsvb) {		
		firstdau = 0;
		firstcuoi = (sotrang -1) * sodongcuatrang ;		
		props.setListVanBanDen(objVBDen_XuLyFacade
				.locDanhSachXuLyVanBan(objLocTheoHsvb!=null?objLocTheoHsvb:props.getObjVBDen_XuLyFilterModel()
						, userId, companyId, getOrganizationId(), phongban_org
						, modeVaiTro, soNgayToiHan
						, props.getVaiTro()
						,sotrang * sodongcuatrang, 0, request, getListDvUser(), getOrgIdTemp()));
		//ttdat update lấy đúng tổng số văn bản đến
		sizeFull = (int)objVBDen_XuLyFacade
				.countLocDanhSachXuLyVanBan(objLocTheoHsvb!=null?objLocTheoHsvb:props.getObjVBDen_XuLyFilterModel()
						, userId, companyId, getOrganizationId(), phongban_org
						, modeVaiTro, soNgayToiHan
						, props.getVaiTro()
						, request, getListDvUser(), getOrgIdTemp());
		log.info(">>>userId: " + userId);
		log.info(">>>organizationId: " + getOrganizationId());
		if(!xuLyThay){phanQuyenTheoDoMat();}
		if(props.getListVanBanDen().size()>0&&chonLai){
			selectIndex=0;
			hienTabSet=true;
			loadThongTin(props.getListVanBanDen().get(0));	
			taoCayLuanChuyen();	
			phanQuyen();					
		}
		for (Map<String, Object> map : props.getListVanBanDen()) {
			if(map.get("vbden_xl_daxem").toString().equals("1")) {
				map.put("vbden_xl_daxem", true);
			}else {
				map.put("vbden_xl_daxem", false);
			}
		}
		sizeChange = props.getListVanBanDen().size();
		preRowload = sodongcuatrang;
		LazyDataModel<Map<String,Object>> objLazyDataModel = null ;
		objLazyDataModel  = new LazyDataModel<Map<String,Object>>() {
			@Override
			public List<Map<String,Object>>load(int first, int pageSize, SortCriteria[] criteria, Map<String, String> filters) {				
				List <Map<String,Object>> listHStmp = new ArrayList<Map<String,Object>>();
				cPageSize = pageSize;
				if ( firstdau <=first && first  <= firstcuoi && ( preRowload >= pageSize ) ) {
					log.info("first " + first);
					// tìm danh sách các phần tử theo thứ tự 
					for(  int i  = first - firstdau ; i < first - firstdau + pageSize ; i++ ) {
						if (sizeChange > i ) {
							listHStmp.add( props.getListVanBanDen().get(i));
						}
					}
				} else {
					log.info("-----------------------Khi click trang ngoai  cac trang da load --------------------");
					int loadfrom = 0;
					preRowload = pageSize; 
					if (first != 0) {
						// nếu trang lick khác không thì lùi trang lại
						loadfrom  = first -  (sotrang/2)*pageSize;
						if (loadfrom  <= 0) {
							loadfrom = 0;
							firstdau = 0;
							firstcuoi = pageSize* (sotrang-1 );
						}  else {
							firstdau  = first - pageSize*(sotrang/2) ;
							firstcuoi =  firstdau + pageSize*(sotrang - 1);
							// lấy chỉ số trang  cuối cùng. nếu pageSize = 5,tong so 105 - -> chỉ số là 105-1 = 104
							//104/5  =20 -> 10 *5= 100, chỉ số của trang là 100.  Nếu tổng là 106 --> chỉ số trang cuối là 21 
							int trangcuoi = ( (sizeFull - 1 ) / pageSize ) *( pageSize) ;
							log.info("Chi so trang cuoi cung" + trangcuoi);
							if ( firstcuoi > trangcuoi) {
								firstcuoi = trangcuoi ;
								firstdau = trangcuoi - (sotrang - 1)*pageSize;
								// neu firstdau< 0. do nếu PageSize sau khi thay đổi > PageSize trước đó thì.
								if (firstdau <= 0) {
									firstdau  = 0 ;
									firstcuoi = (sotrang -1 )*pageSize;
								}
								loadfrom = firstdau ;
							}
						}
					} else {
						loadfrom = 0;
						firstdau = 0;
						firstcuoi = pageSize*(sotrang-1);	
					}
					// nếu sự kiện click Paginator first nằm ngoài list cũ
					props.getListVanBanDen().clear();
					props.setListVanBanDen(objVBDen_XuLyFacade
							.locDanhSachXuLyVanBan(objLocTheoHsvb!=null?objLocTheoHsvb:props.getObjVBDen_XuLyFilterModel()
									, userId, companyId, getOrganizationId(), phongban_org, modeVaiTro, 
									soNgayToiHan, props.getVaiTro(),pageSize*sotrang, loadfrom,"",getListDvUser(),getOrgIdTemp()));
					//ttdat update lấy đúng tổng số văn bản đến
					sizeFull = (int)objVBDen_XuLyFacade
							.countLocDanhSachXuLyVanBan(objLocTheoHsvb!=null?objLocTheoHsvb:props.getObjVBDen_XuLyFilterModel()
									, userId, companyId, getOrganizationId(), phongban_org
									, modeVaiTro, soNgayToiHan
									, props.getVaiTro()
									, request, getListDvUser(), getOrgIdTemp());
					if(!xuLyThay){phanQuyenTheoDoMat();}
					sizeChange = props.getListVanBanDen().size();				
					//load trang khi click vào
					for ( int i = first - loadfrom; i < first - loadfrom + pageSize; i ++ ) {
						if (sizeChange > i) {
							listHStmp.add( props.getListVanBanDen().get(i));
						}
					}					
				}				
				return listHStmp;
			}
			
		};
		objLazyDataModel.setRowCount(sizeFull);
		
		return objLazyDataModel;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Lọc theo liên kết từ notification
	 * @date    Jul 28, 2014 :: 1:37:34 PM
	 */
	private void urlFilter(){
		boolean hasSetFilter = false;
		
		String KEY = NotificationsVar.THAMSO_CUATOI;
		String parameter = CommonUtils.getUrlParameter(KEY);
		if(parameter.compareTo("")!=0){
			props.getObjVBDen_XuLyFilterModel().setTrangthai_cuatoi(Integer.parseInt(parameter));
			hasSetFilter = true;
		}
		
		String param2 = CommonUtils.getUrlParameter("hinhthuc1");
		String param3 = CommonUtils.getUrlParameter("hinhthuc2");
		
		if(param3.equals("")==false&&param2.equals("")==false&&CommonUtils.isNumberic(param2)&&CommonUtils.isNumberic(param3)){
			props.getObjVBDen_XuLyFilterModel().setHinhthuc1(Integer.parseInt(param2));
			props.getObjVBDen_XuLyFilterModel().setHinhthuc2(Integer.parseInt(param3));
			hasSetFilter = true;
		}
		
		if(hasSetFilter){
			CommonUtils.expandColapseFilterNone();
		}
		
	}
	public void showUserInfo(long uid){
		 try {
			log.info(">>showUserInfo");
			if(mapBasicUserInfo == null){
				mapBasicUserInfo = new HashMap<String, Map<String,String>>();
			}
			if(!mapBasicUserInfo.containsKey(String.valueOf(uid))){
				User u = objUserUtil.getUser(uid);
				Map<String, String> info = new HashMap<String, String>();
				info.put("phongban", objUserUtil.getStringPhongBanNguoiDung(uid, getOrganizationId()));
				info.put("chucvu", u.getJobTitle());
				info.put("taikhoan", u.getScreenName());
				info.put("email", u.getEmailAddress());
				mapBasicUserInfo.put(String.valueOf(uid), info);
			}
			
			

		} catch (Exception ex) {
			log.error("err ", ex);
		}
	}
	/**
	 * @author  ltbao
	 * @purpose Tạo dữ liệu thống kê
	 * @date    Jul 3, 2014 :: 7:32:29 AM
	 */
	public void taoDuLieuThongKe(VBDen_XuLyFilterModel filterModel){
		int tong=0, hoanthanhtvb = 0, chuahoanthanhtvb=0, chuaxuly=0, daxuly = 0, phoihop = 0, theodoi = 0;		
		String re = objVBDen_XuLyFacade
				.thongKeSoLuong(filterModel!=null?filterModel:props.getObjVBDen_XuLyFilterModel()
						, userId, companyId
						, getOrganizationId()
						, phongban_org
						, modeVaiTro
						, soNgayToiHan
						, props.getVaiTro()
						, request, getListDvUser(), getOrgIdTemp());
		log.info("Sô lượng thống kê {}", re);
		if(re!=null){
			String[] array = re.split("@");
			tong =Integer.parseInt(array[0]);
			//sizeFull = tong;
			hoanthanhtvb = Integer.parseInt(array[1]);
			chuaxuly = Integer.parseInt(array[2]);
			daxuly = Integer.parseInt(array[3]);
			phoihop = Integer.parseInt(array[4]);
			theodoi = Integer.parseInt(array[5]);
			
			props.getObjThongKe().put("tong", tong);
			props.getObjThongKe().put("hoanthanhtvb", hoanthanhtvb);
			props.getObjThongKe().put("chuahoanthanhtvb", chuahoanthanhtvb);
			props.getObjThongKe().put("chuaxuly", chuaxuly);
			props.getObjThongKe().put("daxuly", daxuly);
			props.getObjThongKe().put("phoihop", phoihop);
			props.getObjThongKe().put("theodoi", theodoi);
			//listTmp.clear();
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Phân quyền danh sách văn bản theo độ mật
	 * @date    Jun 20, 2014 :: 2:05:13 PM
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	private void phanQuyenTheoDoMat(){
		try {
			if(props.getListVanBanDen()!=null){
				int listSize = props.getListVanBanDen().size();
				for(int i=0;i<listSize;i++){				
					Map map=props.getListVanBanDen().get(i);
					//Khởi tạo màu, tạo dữ liệu thống kê	
					map.put("vColor", loader.getParams().get(map.get("color")));
					map.put("vColorToanVb", loader.getParams().get(map.get("color_toanvanban")));
					String tmpTrichYeu = map.get("vbden_trichyeu").toString();
					if(tmpTrichYeu.length()>200){
						tmpTrichYeu = tmpTrichYeu.substring(0, 200);
						tmpTrichYeu = tmpTrichYeu+" ...";
					}
					map.put("tmpTrichYeu", tmpTrichYeu);				
					if(!Boolean.parseBoolean(map.get("xlChinh").toString()) 
							&& !Boolean.parseBoolean(map.get("xlPhoiHop").toString())){
						if(HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi)){
							if(map.get("lanhdaodv_taptin")==null){
								map.put("hienthitaptin", false);
							}
							else {
								map.put("hienthitaptin", Boolean.parseBoolean(map.get("lanhdaodv_taptin").toString()));
							}
						}
						else if(HcdtRoles.userHasRole(HcdtRole.LanhDaoPhong)){
							if(map.get("lanhdaop_taptin")==null){
								map.put("hienthitaptin", false);
							}
							else {
								map.put("hienthitaptin", Boolean.parseBoolean(map.get("lanhdaop_taptin").toString()));
							}
						}
						if(HcdtRoles.userHasRole(HcdtRole.CBVanThu)){
							if(map.get("vanthu_taptin")==null){
								map.put("hienthitaptin", false);
							}
							else {
								map.put("hienthitaptin", Boolean.parseBoolean(map.get("vanthu_taptin").toString()));
							}
							
							if(map.get("vanthu_capnhat")==null){
								map.put("suavanban", false);
							}
							else{
								map.put("suavanban", Boolean.parseBoolean(map.get("vanthu_capnhat").toString()));
							}
						}
					}
					else {
						map.put("hienthitaptin", true);
					}
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * @author  ltbao
	 * @purpose Load thông tin tab 1
	 * @date    Jun 14, 2014 :: 10:03:07 AM 
	 * @param map
	 */
	private void loadThongTin(Map<String,Object> map){
		props.setObjXemVanBan(null);
		if(props.getObjVanBanDen()!=null){
			props.getObjVanBanDen().put("select", 0);
		}
		map.put("select",1);
		props.setObjVanBanDen(map);
		setOrganizationIdVb(Long.parseLong(map.get("vbden_lc_xlchinh_dvid").toString()));
		setOrgNotification(Long.parseLong(props.getObjVanBanDen().get("vbden_lc_xlchinh_dvid").toString()));
		log.info("Org notification "+ getOrgNotification() );
		vbden_id=Long.parseLong(props.getObjVanBanDen().get("vbden_id").toString());				
		vbden_lc_id=Long.parseLong(props.getObjVanBanDen().get("vbden_lc_id").toString());
		props.setObjXemVanBan(getMapVanBanDen(vbden_id, vbden_lc_id));
		props.setListLinhVuc(objDmLinhVucFacade.getDsLinhVucTheoDsId(pmExtractString(props.getObjXemVanBan().get("vbden_linhvuc"), "{}")));
		props.setListTapTinVanBan(objTaptinDinhKemVanBanFacade.getDsTapTinTheoVanBan(vbden_id, 1));
		//Lấy danh sách phổ biến tại bước
		props.setListPhoBienTaiBuoc(objVBDen_LuanChuyenFacade.getListNguoiPhoBienTheoBuocLuanChuyen(vbden_lc_id, getOrganizationId()));
		
		//Khởi tạo ds liên kết văn bản với văn bản được chọn
		propsLkvb.setVbId(vbden_id);
		propsLkvb.setNhomVb(1);
		//propsLkvb.getDsVanBan(vbden_id, 1);
		//propsLkvb.getDsVanBanDaLk(vbden_id, 1);
		propsLkvb.kiemTraQuyenTiepNhan();
		
		propsHsvb.setVbId(vbden_id);
		propsHsvb.setOrgId(getOrganizationId());
		propsHsvb.setNhomVb(1);
		propsHsvb.loadDsHsvb();
		hienHoSoVanBan = true;
		suaHanXlToanVb=false;
		
		//Set id cho log văn bản đến Bean
		vanBanLogBean.setVanBanId(vbden_id);
		vanBanLogBean.demSoVanBan(vbden_id, 1);
		
		//@ptgiang Load link xem chi tiết
		props.getObjXemVanBan().put("vbden_phucdap", pmGetIdPhVbDiBySoHieuGoc(props.getObjXemVanBan().get("vbden_phucdap")));
		
		//Thời hạn xử lý yêu cầu từ phát hành
		/*if( !com.cusc.hcdt.util.StringUtils.isNullOrEmpty(map.get("phdi_id")) ){ 
			VBDi_PhatHanhModel vbPhModel = objVBDi_PhatHanhFacade.getObjVBDi_PhatHanhModel(Long.parseLong(map.get("phdi_id").toString()));
			if( vbPhModel != null ){
				props.getObjXemVanBan().put("thoiHanXuLyTuPh", vbPhModel.getPhdi_thoihanthuchien());
				props.getObjXemVanBan().put("tenDonViPh", vbPhModel.getOrganizationten());
			}
		}*/
		
		//Cập nhật lại trạng thái id các xử lý phối hợp đã xem văn bản
		String userIdDvID = String.format(IContanst.PATTERN_USERID_DVID, userId, getOrganizationId());
		long lc_id = Long.parseLong(map.get("vbden_lc_id").toString());
		String dsId = objVBDen_XuLyFacade.getDsIdXlDaXem(lc_id);
		dsId = dsId==null || dsId.equals("") ? "{}" : dsId;
		if(!dsId.contains(userIdDvID)){
			if(dsId.equals("{}")){
				dsId = "{"+userIdDvID+"}";
			}else{
				dsId = dsId.replace("}", ","+userIdDvID+"}");
			}
			objVBDen_XuLyFacade.updateDsIdXlDaXem(dsId, lc_id);
			map.put("vbden_xl_daxem", true);//Cập nhật lại trạng thái đã xem
		}else {
			map.put("vbden_xl_daxem", false);
		}
		
		//Cập nhật trạng thái đã xem văn bản nhận trực tiếp từ phát hành
		long phdi_id = props.getObjXemVanBan().get("phdi_id") != null ? Long.parseLong(props.getObjXemVanBan().get("phdi_id").toString()) : 0;
		//log.info("phdi_id {} userId {} organizationId {}", phdi_id, userId, organizationId); 
		if( phdi_id > 0 ){
			KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade.getObjKetQuaXuLyTheoUserIdDvIdNhan(phdi_id, userId, getOrganizationId());
			//log.info("kqxlModel {}", kqxlModel); 
			if(kqxlModel != null && kqxlModel.getKqxl_daxem() != true){
				kqxlModel.setKqxl_daxem(true);
				kqxlModel.setKqxl_trangthai(IContanst.DA_XEM);
				kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil.getUserFullName(userId));
				kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils.getCurrentDate());
				objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);
			}
		}
		
		
		//load tab danh sách nơi nhận liên đơn vị
		try {
			loadTabVanBanLienDonVi();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	private String pmExtractString(Object str, String def) {
		if(str == null) return def;
		return str.toString();
	}

	/**
	 * @author  ltbao
	 * @purpose Lấy danh sách quyền người dùng
	 * @date    Jun 18, 2014 :: 10:33:31 AM 
	 * @param userid
	 * @return
	 */
	private String getChucVu(long userid){
		String s="";		
				try {
					User user= UserLocalServiceUtil.getUser(userid);
					s=user.getJobTitle();
				} catch (PortalException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}			
		return s;
	}
	
	/**
	 * @author  ltbao
	 * @purpose 
	 * @date    Jun 13, 2014 :: 3:10:16 PM 
	 * @param path
	 * @return
	 */
	public void actionDownloadFile(AjaxBehaviorEvent evt){		
		Object path=evt.getComponent().getAttributes().get("path");
		Object name=evt.getComponent().getAttributes().get("filename");
		if(path!=null&&name!=null){
			File f=new File(path.toString());
			if(f.exists()){
				log.info(path.toString());
				resource=new HCDTResource(f);				
				fileName=name.toString();
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
			}
			else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Tập tin không tồn tại',4)");
			}
		}		
	}
	
	/**
	 * @author  lqthai
	 * @purpose Lấy đường dẫn theo nhóm văn bản
	 * @date    Nov 9, 2016 :: 3:49:53 PM 
	 * @param nvb
	 * @return
	 */
	public String getPathUpload(int nvb){
		try {
			log.info(">>getPathUpload");
			return CommonUtils.getPathUpload(nvb);
		} catch (Exception ex) {
			log.error("err ", ex);
			return "/";
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadThongTinTabXuLy(){
		//Reset lai list nhan chuyen xu ly va list pho bien
		log.info("||loadThongTinTabXuLy");
		vbden_lc_id =Long.parseLong(objVBDen_XuLyFacade.getMaxLuanChuyenId(vbden_id));
		pmGetDsPhoBien();
		pmGetDsChuyenXuLy();
		if(Integer.parseInt(props.getObjXemVanBan().get("vbden_lc_lbxl").toString())==1
				&&loader.getParams().get("vLanhDaoButPhe").toString().trim().compareTo("")!=0){
			for(Map map: props.getListChuyenXuLy()){
				if(map.get("userid").toString().compareTo(loader.getParams().get("vLanhDaoButPhe").toString())==0){
					map.put("chon", true);
					map.put("xlchinh", true);
					map.put("checked", true);
					break;
				}else {
					map.put("chon", false);
					map.put("xlchinh", false);
					map.put("checked", false);
				}
			}
		}
		props.setObjVBDen_LuanChuyenModel(objVBDen_LuanChuyenFacade.getObjLuanChuyen(vbden_lc_id));
		if(props.getObjVBDen_LuanChuyenModel() != null
				&& props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe() != null
				&& props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe().trim().isEmpty()){
			props.getObjVBDen_LuanChuyenModel().setVbden_lc_butphe(null);
		}
			
		butPheHientai = props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe();
		//Set phòng ban mặc định
		if(props.getObjVBDen_LuanChuyenModel()!=null&&props.getObjVBDen_LuanChuyenModel().getVbden_lc_phongban()!=null){
			phongban_org = props.getObjVBDen_LuanChuyenModel().getVbden_lc_phongban();
		}
		
		props.setObjVBDen_LuanChuyenModelTiepTheo(new VBDen_LuanChuyenModel());
		props.setListTapTinLuanChuyen(objTapTinDinhKemLuanChuyenFacade.getDsTapTinTheoLuanChuyen(vbden_lc_id,1));
		props.setListThemTapTinLuanChuyen(new ArrayList<TapTinDinhKemLuanChuyenModel>());
		props.setListXoaTapTinLuanChuyen(new ArrayList<TapTinDinhKemLuanChuyenModel>());
		props.setLoaiPhoBien(kiemTraKieuPhoBien());
		actionLoadDsEmailPhoBien();
		
		props.setChuyenXuLy(false);
		if(Boolean.parseBoolean(props.getObjXemVanBan().get("vbden_hoanthanh").toString())){
			props.setHoanThanhXuLyToanVanBan(true);
		} else{
			props.setHoanThanhXuLyToanVanBan(false);
		}
		if(props.getListTapTinLuanChuyen()!=null){
			for(TapTinDinhKemLuanChuyenModel model:props.getListTapTinLuanChuyen()){
				File f=new File(props.getBasePath()+loader.getParams().get("vPath_vbden")+"/vb_den_lc/"+model.getLctt_tenluutru());
				log.info(props.getBasePath()+loader.getParams().get("vPath_vbden")+"/vb_den_lc/"+model.getLctt_tenluutru());
				if(f.exists()){
					HCDTResource resource=new HCDTResource(f);
					model.setDownload(resource);
					model.setType(CommonUtils.getFileExtension(f));
					model.setPath(props.getBasePath()+loader.getParams().get("vPath_vbden")+"/vb_den_lc/"+model.getLctt_tenluutru());
				}
			}
		}
	}
		
	public void loadTabGopY(){
		props.setObjThongTinGopYModel(new ThongTinGopYModel());	
		props.setListTapTinGopY(new ArrayList<TapTinGopYModel>());
	}	
	/**
	 * @author  ltbao
	 * @purpose Phân quyền lại mỗi khi chọn một văn bản khác, và lúc khởi tạo
	 * @date    Jun 10, 2014 :: 1:45:43 PM
	 */
	public void phanQuyen(){		
		if(xuLyThay){
			quyenGopY=quyenNhapButPhe=quyenxlchinh=quyenphobien=quyenLuuTru=quyenSuaVanBan = true;
			hientabxuly=(!Boolean.parseBoolean(props.getObjVanBanDen().get("vbden_lc_hoanthanh").toString())) 
					|| (Boolean.parseBoolean(props.getObjVanBanDen().get("vbden_hoanthanh").toString())
					&&vbden_lc_id==Long.parseLong(objVBDen_XuLyFacade.getMaxLuanChuyenId(vbden_id)));			
		}
		else{
			quyenxlchinh = Integer.parseInt(props.getObjXemVanBan().get("xlChinh").toString()) == 1;
			
			//Quyền góp ý: Là phối hợp xử lý hoặc đã từng xử lý chính.
			quyenGopY = quyenxlchinh == false &&
					( (Boolean.parseBoolean(props.getObjVanBanDen().get("xlPhoiHop").toString()))
							|| (Boolean.parseBoolean(loader.getParams().get("vHienGopY").toString()) && 
									objVBDen_XuLyFacade.kiemTraXuLyChinh(
											Long.parseLong(props.getObjVanBanDen().get("vbden_id").toString()), userId, getOrganizationId())) );
			
			//Quyền nhập bút phê
			quyenNhapButPhe = HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi, HcdtRole.LanhDaoPhong);
			
			//Quyền xóa, nếu là văn thư và là bước tiếp nhận		
			
			quyenphobien = HcdtRoles.userHasRole(HcdtRole.CBVanThu, HcdtRole.LanhDaoDonVi);
			
			//Là văn thư tạo ra văn bản
			quyenLuuTru=(quyenVanThu&&(Long.parseLong(props.getObjXemVanBan().get("userid").toString())==getUserId()));
			
			hientabxuly = (!Boolean.parseBoolean(props.getObjVanBanDen().get("vbden_lc_hoanthanh").toString()) && quyenxlchinh) 
					|| (quyenxlchinh && Boolean.parseBoolean(props.getObjVanBanDen().get("vbden_hoanthanh").toString())
							&&vbden_lc_id == Long.parseLong(objVBDen_XuLyFacade.getMaxLuanChuyenId(vbden_id)));
			
			boolean cauHinhDoMat = false;
			CauHinhDoMatModel objCauHinh = objCauHinhDoMatFacade.getObjCauHinhDoMat(getOrganizationId(), Integer.parseInt(props.getObjVanBanDen().get("dm_id").toString()));
			if(objCauHinh!=null){
				cauHinhDoMat =  objCauHinh.getVanthu_capnhat()==null?true:objCauHinh.getVanthu_capnhat();
			}
			
			quyenSuaVanBan = (userId==Long.parseLong(props.getObjXemVanBan().get("userid").toString())) || (quyenVanThu && cauHinhDoMat) ;
			
			//Khi chuyển đến người khác xly chính ẩn đi tab xử lý
			//(fix lỗi hiện tại khi đang là xl chính chuyển cho ng khác xly chính, mình xly phối hợp)
			if(!Boolean.parseBoolean(props.getObjVanBanDen().get("xlChinh").toString())
					&& Boolean.parseBoolean(props.getObjVanBanDen().get("xlPhoiHop").toString())){
				log.info("Có vào đây không hả.....................");
				hientabxuly = false;
			}
		}
		if(hientabxuly){
			loadThongTinTabXuLy();
		}
		if(quyenGopY){
			loadTabGopY();
		}
		if(quyenphobien){
			props.setListMailPhoBien(objMailPhoBienFacade.getListMailPhoBien(vbden_id, 1));
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Khởi tạo cây luân chuyển
	 * @date    Jun 9, 2014 :: 7:59:45 PM
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void taoCayLuanChuyen(){
		log.info("||taoCayLuanChuyen");
		props.setListLuanChuyen(objVBDen_LuanChuyenFacade.getListLuanChuyenTheoVanBanDen(vbden_id));
		//Nếu có bước luân chuyển
		if( !props.getListLuanChuyen().isEmpty() ){
			//Khởi tạo danh sách tập tin luân chuyển
			int lastIndexAllowChuyenBoSung = -1;
			int idx = 0;
			int soBuocLuanChuyen = props.getListLuanChuyen().size();
			for(Map map : props.getListLuanChuyen()){
				/*30.07.2014 Thêm thông tin chức năng thu hồi*/
				boolean thuhoi = false;
				
				//Chỉ thu hồi các bước không phải là bước tiếp nhận
				if(Integer.parseInt(map.get("vbden_lc_lbxl").toString())==0){
					//Cho phép thu hồi khi bước xử lý do mình chuyển và vẫn chưa thao tác và vẩn chưa từng bị thu hồi
					VBDen_LuanChuyenModel lcModel = objVBDen_LuanChuyenFacade.getObjLuanChuyen(Long.parseLong(map.get("vbden_lc_cha").toString()));
					if(lcModel.getVbden_lc_xlchinh_userid()==objUserUtil.getUserId()
							&& !Boolean.parseBoolean(map.get("vbden_lc_thaotac").toString())
							&& !Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())){
						thuhoi = true;
					}
					if(xuLyThay){
						if(!Boolean.parseBoolean(map.get("vbden_lc_thaotac").toString())
								&&!Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())){
							thuhoi = true;
						}
					}
				}
				map.put("duocthuhoi", thuhoi);
				
				/*END 30.07.2014*/
				long useridtmp = Long.parseLong(map.get("vbden_lc_xlchinh_userid").toString());
				//map.put("phongban", objUserUtil.getStringPhongBanNguoiDung(useridtmp,organizationId));
				List<TapTinDinhKemLuanChuyenModel> listTapTin=new ArrayList<TapTinDinhKemLuanChuyenModel>();
				listTapTin=objTapTinDinhKemLuanChuyenFacade.getDsTapTinTheoLuanChuyen(Long.parseLong(map.get("vbden_lc_id").toString()),1);
				
				//Tạo resource cho tập tin				
				map.put("taptin", listTapTin); 
				//map.put("nguoixuly", UserUtil.getUserFullName(Long.parseLong(map.get("vbden_lc_xlchinh_userid").toString()))); 
				//Khởi tạo danh sách thông tin góp ý
				long lcId = Long.parseLong(map.get("vbden_lc_id").toString());
				List<ThongTinGopYModel> listGopY = objThongTinGopYFacade.getListThongTinGopY(lcId, 1);
				
				//Khởi tạo tập tin góp ý
				if(listGopY!=null){	
					for(ThongTinGopYModel model:listGopY){
						model.setListTapTin(objTapTinGopYFacade.getListTapTinGopY(model.getTtgy_id()));
						//Tạo resource		
					}
				}				
				map.put("listgopy", listGopY);				
				map.put("listPhoBien", objVBDen_XuLyFacade.getDsPhoBienCaNhan(Long.parseLong(map.get("vbden_lc_id").toString())));
				
				//Cho phép chuyển xử lý bổ sung
				if(idx < soBuocLuanChuyen - 1 ){	
					map.put("isChuyenXuLyBoSung", isChuyenXuLyBoSung(props.getListLuanChuyen().get(idx + 1).get("vbden_lc_xlbosung"))); 
				}
				map.put("allowChuyenXuLyBoSung", false);	
				
				log.info(">>idx: "+idx);
				log.info(">>soBuocLuanChuyen: "+soBuocLuanChuyen);
				log.info(">>useridtmp: "+useridtmp);
				log.info(">>userId: "+userId);
				if( props.isHoanThanhXuLyToanVanBan() == false && thuhoi == false && 
						useridtmp == userId &&  idx < soBuocLuanChuyen - 1 ){
					lastIndexAllowChuyenBoSung = idx;
				}
				
				/*List<Map<String, Object>> listPhoiHop = objVBDen_XuLyFacade.getDsPhoiHopXuLyBuocTiepTheo(lcId);
				int sl = 0;
				if(listPhoiHop!=null){
					sl = listPhoiHop.size();
				}*/
				log.info("lc id {}",lcId);
				String dsId = objVBDen_XuLyFacade.getDsIdPhoiHopXuLy(lcId);
				if(dsId!=null && !dsId.equals("")){
					List<String> dsUserIdDvId = (List<String>)CollectionUtil.convertJsonStringToListString(dsId);
					int sl = dsUserIdDvId.size();
					sl = sl > 0 ? sl-1 : 0;
					map.put("soLuongPhoiHop", sl);
				}else{
					map.put("soLuongPhoiHop", 0);
				}
				
				idx++;
			}		
			long userIdLastLuanChuyen = Long.parseLong(props.getListLuanChuyen().get(soBuocLuanChuyen - 1).get("vbden_lc_xlchinh_userid").toString());
			if(userIdLastLuanChuyen==userId){//Nếu người xử lý cuối là người đang đăng nhập thì khong hieenjxl bổ sung
				lastIndexAllowChuyenBoSung = -1;
			}
			log.info(">>lastIndexAllowChuyenBoSung: "+lastIndexAllowChuyenBoSung);
			if( lastIndexAllowChuyenBoSung >= 0 ){
				props.getListLuanChuyen().get(lastIndexAllowChuyenBoSung).put("allowChuyenXuLyBoSung", true);
			}
			
			Map<String, Object> mapLastBuocLc = props.getListLuanChuyen().get(soBuocLuanChuyen-1);
			long lcId = Long.parseLong(mapLastBuocLc.get("vbden_lc_id").toString());
			long userIdXlc = Long.parseLong(mapLastBuocLc.get("vbden_lc_xlchinh_userid").toString());
			boolean isDaXem = mapLastBuocLc.get("vbden_lc_daxem") == null ? false : Boolean.parseBoolean(mapLastBuocLc.get("vbden_lc_daxem").toString());
			if( isDaXem == false && userIdXlc == userId){
				objVBDen_LuanChuyenFacade.capNhatDaXem(lcId, true);
				mapLastBuocLc.put("vbden_lc_daxem", true);
			}
//			else {
//				mapLastBuocLc.put("vbden_lc_daxem", false);
//			}
		}		
		kiemTraMauBuocCuoi();
		props.setKichThuocLuanChuyen(props.getListLuanChuyen().size());
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Kiểm tra có chuyển xử lý bổ sung hay không
	 * @date    Oct 20, 2016 :: 9:50:59 AM 
	 * @param vbden_lc_xlbosung
	 * @return true -> nếu đã có chuyển xử lý bổ sung, false -> nếu không có chuyển bổ sung
	 */
	private boolean isChuyenXuLyBoSung(Object vbden_lc_xlbosung){
		boolean isChuyenXuLyBoSung = false;
		
		if( com.cusc.hcdt.util.StringUtils.isNullOrEmpty(vbden_lc_xlbosung) ){
			return isChuyenXuLyBoSung;
		}
		
		try{
			ChuyenBoSungModel chuyenBoSungModel = (ChuyenBoSungModel)CollectionUtil.convertJsonToObj(vbden_lc_xlbosung.toString(), new TypeToken<ChuyenBoSungModel>(){}.getType());				
			if( chuyenBoSungModel != null ){
				isChuyenXuLyBoSung = !CollectionUtil.isNullOrEmpty(chuyenBoSungModel.getHistory());
			}
		} catch(Exception ex){
			log.error("Err ", ex);
		}
		
		return isChuyenXuLyBoSung;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Kiểm tra màu xử lý của bước cuối
	 * @date    Aug 20, 2014 :: 11:15:58 AM
	 */
	private void kiemTraMauBuocCuoi(){
		if(props.getListLuanChuyen()!=null){
			int last = props.getListLuanChuyen().size()-1;
			String vColor = props.getListLuanChuyen().get(last).get("color")==null?"":props.getListLuanChuyen().get(last).get("color").toString();
			props.setMauBuocCuoi(loader.getParams().get(vColor).toString());
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose 
	 * @date    Jun 13, 2014 :: 3:10:16 PM 
	 * @param path
	 * @return
	 */
	public HCDTResource actionDownloadFile(String path){
		HCDTResource re=null;
		File f=new File(path);
		if(f.exists()){
			re=new HCDTResource(f);
			fileName=f.getName();
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
		}
		return re;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Copy tập tin LUÂN vào thư mục chính thức
	 * @date    May 26, 2014 :: 6:08:22 PM
	 */
	private void luuTapTapTinLuanChuyenChinhThuc(){
		if(props.getListTapTinLuanChuyen()!=null){
			if(props.getListTapTinLuanChuyen().size()>0){
				for(TapTinDinhKemLuanChuyenModel model:props.getListTapTinLuanChuyen()){
					File f=new File(props.getBasePath()+vTmpPathLc+model.getLctt_tenluutru());
					if(f.exists()){
						File newFile=new File(props.getBasePath()+vPathLc+model.getLctt_tenluutru());
						f.renameTo(newFile);
					}
				}
			}
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Kiểm tra điều kiện
	 * @date    May 24, 2014 :: 4:14:19 PM 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean xuLyVanBanValidator(){
		log.info("xuLyVanBanValidator");
		boolean re=true;
		if(props.isChuyenXuLy()){			
			boolean coNguoiNhanChuyenXl=false;
			for(Map map:props.getListChuyenXuLy()){
				if(Boolean.parseBoolean(map.get("chon").toString())){
					coNguoiNhanChuyenXl=true;
					break;
				}
			}
			if(!coNguoiNhanChuyenXl){
				re=false;
				Validator.showErrorMessage(getPortletId(), "frm:txtNhanXuLy", "Vui lòng chọn người nhận xử lý");
			}
			else{
				boolean coChonXlChinh=false;
				for(Map map:props.getListChuyenXuLy()){
					if(Boolean.parseBoolean(map.get("xlchinh").toString())&&Boolean.parseBoolean(map.get("chon").toString())){
						coChonXlChinh=true;
						break;
					}
				}
				if(!coChonXlChinh){
					re=false;
					Validator.showErrorMessage(getPortletId(), "frm:txtNhanXuLy", "Vui lòng chọn người nhận xử lý chính");
				}
			}
			
			if(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_hanxl()!=null){
				if(CommonUtils
						.convertDate(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_hanxl())
						.before(CommonUtils.convertDate(Calendar.getInstance().getTime()))){
					Validator.showErrorMessage(getPortletId(), "frm:txtHanXlBuocTiepTheo", "Hạn xử lý bước tiếp theo phải lớn hơn hoặc bằng ngày hiện tại");
					re=false;
				}else{//05.12.2014
					if(props.getObjXemVanBan().get("vbden_hanxl_toanvanban")!=null){
						Date hanTvb = null;
						hanTvb = (Date)props.getObjXemVanBan().get("vbden_hanxl_toanvanban");
						if(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_hanxl().after(hanTvb)){
							//Nếu sau hạn xử lý toàn văn bản
							Validator.showErrorMessage(getPortletId(), "frm:txtHanXlBuocTiepTheo", "Hạn xử lý bước tiếp theo phải nhỏ hơn hoặc bằng hạn xử lý toàn văn bản");
							re=false;
						}
					}					
				}
			}
		}
		if(props.getLoaiPhoBien()==IContanst.PVPB_NGUOINHAN){
			boolean coNhanPhoBien=false;
			for(Map map:props.getListPhoBien()){
				if(Boolean.parseBoolean(map.get("chon").toString())){
					coNhanPhoBien=true;
					break;
				}
			}
			if(!coNhanPhoBien){
				re=false;
				Validator.showErrorMessage(getPortletId(), "frm:txtNhanPhoBien", "Vui lòng chọn người nhận phổ biến");
			}
		}
		return re;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Kiểm tra trước khi sửa văn bản
	 * @date    Jun 11, 2014 :: 8:35:28 PM 
	 * @return
	 */
	private boolean modifyValidation(){
		boolean test = true;
		String soHieu 	= props.getObjVBDenModel().getVbden_sohieugoc().trim();
		String nguoiKy 	= props.getObjVBDenModel().getVbden_nguoiky().trim();
		String soTo = props.getObjVBDenModel().getVbden_soto().trim();
		String trichYeu = props.getObjVBDenModel().getVbden_trichyeu().trim();
		String soDen = props.getObjVBDenModel().getVbden_soden().trim();		
		String ttLuuTru="";
		if(props.getObjVBDenModel().getVbden_thongtin_lt()!=null){
			ttLuuTru = props.getObjVBDenModel().getVbden_thongtin_lt().trim();
		}
		
		Date ngayCoHl = props.getObjVBDenModel().getVbden_ngaycohl();
		Date ngayHetHl = props.getObjVBDenModel().getVbden_ngayhethl();
		Date ngayden = props.getObjVBDenModel().getVbden_ngayden();
		Date ngayBanHanh = props.getObjVBDenModel().getVbden_ngaybanhanh();
		
		props.getObjVBDenModel().setVbden_sohieugoc(soHieu);
		props.getObjVBDenModel().setVbden_nguoiky(nguoiKy);
		props.getObjVBDenModel().setVbden_soto(soTo);
		props.getObjVBDenModel().setVbden_trichyeu(trichYeu);
		props.getObjVBDenModel().setVbden_soden(soDen);
		props.getObjVBDenModel().setVbden_thongtin_lt(ttLuuTru);
		props.getObjVBDenModel().setVbden_tiepnhan(true);
		props.getObjVBDenModel().setVbden_linhvuc(propsLinhVuc.getDsLinhVucId());
		props.getObjVBDenModel().setVbden_trangthai_xl(0);
		props.getObjVBDenModel().setVbden_hienthi(true);
		
		
		//Nếu trạng thái lưu trữ là false thì thông tin lưu trữ rỗng
		if(!props.getObjVBDenModel().getVbden_trangthai_lt()){
			props.getObjVBDenModel().setVbden_thongtin_lt("");
		}
		//Cơ quan ban hành
		props.getObjVBDenModel().setVbden_coquanbanhanh(propsDonVi.tenDonVi);
		props.getObjVBDenModel().setVbden_coquanbanhanhid(propsDonVi.dvId); 
				
		Pattern patt = Pattern.compile(CommonUtils.getBundleValue("regex_chuoimacdinh"));
		Matcher m = patt.matcher(props.getObjVBDenModel().getChuoiMacDinh());
		if(!CommonUtils.isNumberic(props.getObjVBDenModel().getSoKeTiep()) || props.getObjVBDenModel().getSoKeTiep().length()>5){
			Validator.showErrorMessage(getPortletId(), "frm:txtSoDen", "Số đến không hợp lệ");
			test=false;
		}else if(!m.matches()){
			Validator.showErrorMessage(getPortletId(), "frm:txtSoDen", "Số đến không hợp lệ");
			test=false;
		}else if(soDenOld.compareTo(props.getObjVBDenModel().getSoKeTiep())!=0 && objVBDenFacade.isExistsSoDen(props.getObjVBDenModel().getSoKeTiep(), props.getObjVBDenModel().getSvb_id())){
			Validator.showErrorMessage(getPortletId(), "frm:txtSoDen", "Số đến không được trùng");
			test=false;
		}
		
		if(ngayden==null){
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayDen", "Vui lòng nhập ngày đến");
			test=false;
		}
		if(propsLinhVuc.getDsLinhVucId().equals("{}")){
			Validator.showErrorMessage(getPortletId(), "frm:chkLinhVuc", "Vui lòng chọn lĩnh vực");
			test=false;
		}
		
		if(trichYeu.equals("")){
			Validator.showErrorMessage(getPortletId(), "frm:txtTrichYeu", "Vui lòng nhập trích yếu");
			test=false;
		}
		if(ngayBanHanh==null){
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayBanHanh", "Vui lòng nhập ngày ban hành");
			test=false;
		}
		if(ngayCoHl != null && ngayHetHl != null && ngayCoHl.after(ngayHetHl)){
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayHieuLuc", "Ngày hiệu lực không hợp lệ");
			test=false;
		}
		/*if(propsDonVi.dvId==0){//!testDonVi
			Validator.showErrorMessage(getPortletId(), "frm:txtCoQuanBanHanh", "Vui lòng chọn cơ quan ban hành");
			test=false;
		}*/	
		
		boolean themCoQuan = false;
		//Kiểm tra cơ quan ban hành
		log.info("KIEM TRA CO QUAN 1: {}",propsDonVi.getTmpLoaiDonVi());
		if(propsDonVi.dvId==0 && props.getTenDonViPhatHanh().equals("")){//!testDonVi
			log.info("Chưa chọn đơn vị phát hành: "+propsDonVi.dvId);
			Validator.showErrorMessage(getPortletId(), "frm:txtCoQuanBanHanh", "Vui lòng chọn cơ quan ban hành");
			test=false;
		}else if(propsDonVi.getTmpLoaiDonVi()==0){ //chọn đơn vị ngoài hệ thống
			if(propsDonVi.dvId!=0){
				//props.selectMode==0: tiếp nhận cùng hệ thống OR scan không kiểm tra
				if(!objDmDonViFacade.isExists((int)propsDonVi.dvId)){//Kiểm tra 2 trình duyệt
					Validator.showErrorMessage(getPortletId(), "frm:txtCoQuanBanHanh", "Cơ quan ban hành không tồn tại");
					test=false;
					for(int i=0;i<propsDonVi.dsDonVi.size();i++){
						if(propsDonVi.dsDonVi.get(i).getDv_id()==propsDonVi.dvId){
							propsDonVi.dsDonVi.remove(i);
							propsDonVi.dvId = 0;
							propsDonVi.tenDonVi = "";
							break;
						}
					}
				}
			}else{
				log.info(">>>Thêm đơn vị: "+props.getTenDonViPhatHanh());
				themCoQuan = true;//Sử dụng kiểm tra cuối hàm nếu không có lỗi thì thực hiện thêm đơn vị
				props.setTenDonViPhatHanh(props.getTenDonViPhatHanh().trim().replaceAll("\\s+"," "));
				propsDonVi.dvId = 0;
				propsDonVi.tenDonVi = props.getTenDonViPhatHanh();
			}
		}
		
		//Kiểm tra loại văn bản
		if(props.getObjVBDenModel().getLvb_id()==null){
			Validator.showErrorMessage(getPortletId(), "frm:selLoaiVanBan", "Vui lòng chọn loại văn bản");
			test=false;
		}else if(!objDmLoaiVanBanFacade.isExists(props.getObjVBDenModel().getLvb_id())){
			Validator.showErrorMessage(getPortletId(), "frm:selLoaiVanBan", "Loại văn bản vừa chọn không tồn tại");
			propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(getCompanyId(), getOrganizationId()));
			test=false;
		}
		//Kiểm tra sổ văn bản
		if(props.getObjVBDenModel().getSvb_id()==null){
			Validator.showErrorMessage(getPortletId(), "frm:selSoVanBan", "Vui lòng chọn sổ văn bản");
			propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(getCompanyId(), getOrganizationId()));
			test=false;
		}else if(!objDmSoVanBanFacade.isExists(props.getObjVBDenModel().getSvb_id())){
			Validator.showErrorMessage(getPortletId(), "frm:selSoVanBan", "Sổ văn bản vừa chọn không tồn tại");
			propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade.getDsSoVbTheoNvb(1, companyId, getOrganizationId(),getListDvUser()));
			test=false;
		}
		
		//Kiểm tra độ mật
		if(props.getObjVBDenModel().getDm_id() == null){
			Validator.showErrorMessage(getPortletId(), "frm:selDoMat","Vui lòng chọn độ mật");
			test=false;
		} else if(!objDmDoMatFacade.isExists(props.getObjVBDenModel().getDm_id())){
			Validator.showErrorMessage(getPortletId(), "frm:selDoMat", "Độ mật vừa chọn không tồn tại");
			props.setListDoMat(objDmDoMatFacade.getDsDoMat(getCompanyId(), getOrganizationId()));
			test=false;
		}
		
		//Kiểm tra độ khẩn
		if(props.getObjVBDenModel().getDk_id()==null){
			Validator.showErrorMessage(getPortletId(), "frm:selDoKhan","Vui lòng chọn độ khẩn");
			test=false;
		} else if(!objDmDoKhanFacade.isExists(props.getObjVBDenModel().getDk_id())){
			Validator.showErrorMessage(getPortletId(), "frm:selDoKhan", "Độ khẩn vừa chọn không tồn tại");
			props.setListDoKhan(objDmDoKhanFacade.getDsDoKhan(getCompanyId(), getOrganizationId()));
			test=false;
		}
		boolean loiLv = false;
		for(int i = 0;i<propsLinhVuc.getDsLinhVuc().size(); i++){
			if(propsLinhVuc.getDsLinhVuc().get(i).isChecked()){
				if(!objDmLinhVucFacade.isExists(propsLinhVuc.getDsLinhVuc().get(i).getLv_id())){
					loiLv = true;
					propsLinhVuc.getDsLinhVuc().remove(i);
					i--;
				}
			}
		}		
		if(loiLv){
			test = false;
			Validator.showErrorMessage(getPortletId(), "frm:chkLinhVuc", "Một số lĩnh vực vừa chọn đã bị xóa");
		}
		
		//Kiểm tra cách thức xử lý
		if(props.getObjVBDenModel().getCtxl_id()==null){
			Validator.showErrorMessage(getPortletId(), "frm:selCachThucXuLy","Vui lòng chọn cách thức xử lý");
			test=false;
		} else if(!objDmCachThucXuLyFacade.isExists(props.getObjVBDenModel().getCtxl_id())){
			test = false;
			props.setListCachThucXuLy(objDmCachThucXuLyFacade.getDsCachThucXuLy(getCompanyId(), getOrganizationId()));
			Validator.showErrorMessage(getPortletId(), "frm:selCachThucXuLy", "Cách thức xử lý vừa chọn đã bị xóa");
		}
		
		if(props.getListXoaSua()!=null){
			int size = props.getListXoaSua().size();
			boolean coLoi = false;
			for(int i=0; i<size; i++){
				TapTinDinhKemVanBanModel ttdk = props.getListXoaSua().get(i);
				if(ttdk.getTtdk_vbden_id_nhan()!=null 
						&& !ttdk.getTtdk_vbden_id_nhan().equals("") 
						&& !ttdk.getTtdk_vbden_id_nhan().equals("{}")
						&& objVBDen_XuLyFacade.daTiepNhanTuChuyenDonVi(ttdk.getTtdk_vbden_id_nhan())){
					ttdk.setDuocxoa(false);
					props.getListTapTinVanBanSua().add(ttdk);
					props.getListXoaSua().remove(i);
					i--;
					size--;
					coLoi = true;
				}
			}			
			if(coLoi){
				Validator.showErrorMessage(getPortletId(), "frm:tmpXoaErrFile", "Không thể xóa một số tập tin đã được đơn vị khác tiếp nhận.");
				test = false;
			}
		}
		if(test){
			if(!props.getObjVBDenModel().isViTri()){
				props.getObjVBDenModel().setVbden_soden(props.getObjVBDenModel().getChuoiMacDinh()+"$"+props.getObjVBDenModel().getSoKeTiep()+"$");
			}
			else{				
				props.getObjVBDenModel().setVbden_soden("$"+props.getObjVBDenModel().getSoKeTiep()+"$"+props.getObjVBDenModel().getChuoiMacDinh());
			}
		}
		
		//Thực hiện thêm cơ quan ban hành
		log.info(">>>test: " + test);
		log.info(">>>themCoQuan: " + themCoQuan);
		if(test && themCoQuan){
			//Nếu tên đơn vị tồn tại
			props.setTenDonViPhatHanh(props.getTenDonViPhatHanh().trim().replaceAll("\\s+"," "));
			String dv_ten = props.getTenDonViPhatHanh();
			DmDonViModel donVi = objDmDonViFacade.getDonViTheoTen(dv_ten, getCompanyId(), getOrganizationId());
			if(donVi!=null){
				propsDonVi.dvId = donVi.getDv_id();
				propsDonVi.tenDonVi = donVi.getDv_ten();
				log.info(">>>Đơn vị: "+propsDonVi.dvId);
				log.info(">>>Đơn vị: "+propsDonVi.tenDonVi);
			}else{
				donVi = new DmDonViModel();
				donVi.setDv_ten(dv_ten);
				donVi.setDv_ma("DVKhac1");
				donVi.setCompanyid(getCompanyId());
				donVi.setOrganizationid(getOrganizationId());
				donVi.setDv_cha(0);
				donVi.setDv_diachi("");
				donVi.setDv_sdt("");
				donVi.setDv_email("");
				
				
				log.info(">>>Thực hiện thêm danh mục cấp đơn vị khác");
				DmCapDonViModel cap = objDmCapDonViFacade.getCapDonViTheoTen("Khác", getCompanyId(), getOrganizationId());
				if(cap!=null){
					donVi.setC_id(cap.getC_id());
					log.info(">>>Cấp đã tồn tại");
				}else{
					cap = new DmCapDonViModel();
					cap.setC_ten("Khác");
					cap.setCompanyid(getCompanyId());
					cap.setOrganizationid(getOrganizationId());
					objDmCapDonViFacade.capNhatCapDonVi(cap,1);
					donVi.setC_id(cap.getC_id());
					log.info(">>>Thêm mới cấp: "+donVi.getC_id());
				}
				
				objDmDonViFacade.capNhatDonVi(donVi,1);
				donVi.setDv_ma("DVKhac"+donVi.getDv_id());
				objDmDonViFacade.capNhatDonVi(donVi,2);
				
				propsDonVi.dvId = donVi.getDv_id();
				propsDonVi.tenDonVi = dv_ten;
				propsDonVi.reloadDsDonVi();
				log.info(">>>Thêm: " + donVi.getDv_id() + " " + propsDonVi.tenDonVi);
			}
			
			if(propsDonVi.dvId==0){//!testDonVi
				log.info("Chưa chọn đơn vị phát hành: "+propsDonVi.dvId);
				//props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:txtCoQuanBanHanh", "Vui lòng chọn cơ quan ban hành");
				test=false;
			}
		}
		//Cơ quan ban hành
		props.getObjVBDenModel().setVbden_coquanbanhanh(propsDonVi.tenDonVi);
		props.getObjVBDenModel().setVbden_coquanbanhanhid(propsDonVi.dvId);
		
		return test;
	}
			
	private int kiemTraKieuPhoBien(){
		try {
			log.info(">>kiemTraKieuPhoBien");
			int re = IContanst.PVPB_TATCA;
			String pb_phongban = props.getObjXemVanBan().get("vbden_pb_phongban")==null ? "" : props.getObjXemVanBan().get("vbden_pb_phongban").toString();
			String pb_canhan = props.getObjXemVanBan().get("vbden_pb_canhan").toString();
			if(pb_phongban.equals("0")){
				re = IContanst.PVPB_KHONGPHOBIEN;
			}else if(!pb_phongban.equals("")){
				re = IContanst.PVPB_NOIBO;
			}else if(!pb_canhan.equals("{}")){
				re = IContanst.PVPB_NGUOINHAN;
				for(Map<String, Object> map : props.getListPhoBien()){
					String user_dv = map.get("userid").toString() + "_" + map.get("donviid").toString();
					// lnakhang: bổ sung kiểm tra đơn vị. khi phổ biến cá nhân cần xét thêm đơn vị
					// nếu không có dấu _ => Dữ liệu cũ
					if(!props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan().contains("_")){
						if(props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan().contains(map.get("userid").toString())){
							map.put("chon", true);
						}
						else{
							map.put("chon", false);
						}
					}else{
						if(props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan().contains(user_dv)){
							map.put("chon", true);
						}
						else{
							map.put("chon", false);
						}
					}
				}
			}
			return re;
		} catch (Exception ex) {
			log.error("err ", ex);
		}
		return IContanst.PVPB_KHONGPHOBIEN;
	}
	
	/**
	 * @author  ltbao
	 * @purpose 
	 * @date    May 24, 2014 :: 3:51:31 PM 
	 * @param list
	 * @return
	 */
	private List<Long> listUserToListId(List<Map<String,Object>> list){
		List<Long> dsUserId = new ArrayList<Long>();
		
		if(!CollectionUtil.isNullOrEmpty(list)){
			for(Map<String, Object> map : list){
				if(!Boolean.parseBoolean(map.get("xlchinh").toString())&&Boolean.parseBoolean(map.get("chon").toString())){
					dsUserId.add(Long.parseLong(map.get("userid").toString()));
				}
			}
		}
		return dsUserId;
	}
	
	public void actionXoaCoQuanBanHanh(){
		try {
			log.info(">>actionXoaCoQuanBanHanh");
			propsDonVi.dvId = 0;
			propsDonVi.tenDonVi = "";
		} catch (Exception ex) {
			log.error("err ", ex);
		}
	}
	
	private List<Long> listIdOrgXlChinh(List<Map<String,Object>> list){
		List<Long> dsOrgId = new ArrayList<Long>();
		
		if(!CollectionUtil.isNullOrEmpty(list)){
			for(Map<String, Object> map : list){
				if(Boolean.parseBoolean(map.get("xlchinh").toString())
						&& Boolean.parseBoolean(map.get("chon").toString())){
					dsOrgId.add(Long.parseLong(map.get("donviid").toString()));
				}
			}
		}
		return dsOrgId;
	}
	
	private List<Long> listIdOrgXlPhoiHop(List<Map<String,Object>> list){
		List<Long> dsOrgId = new ArrayList<Long>();
		
		if(!CollectionUtil.isNullOrEmpty(list)){
			for(Map<String, Object> map : list){
				if(!Boolean.parseBoolean(map.get("xlchinh").toString())
						&&Boolean.parseBoolean(map.get("chon").toString())){
					dsOrgId.add(Long.parseLong(map.get("donviid").toString()));
				}
			}
		}
		return dsOrgId;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Lấy danh sách người nhận phổ biến
	 * @date    Jun 9, 2014 :: 1:21:27 PM 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getListPhoBien(){
		String re="";
		if(props.getListNhanPhoBien()!=null){
			for(Map map:props.getListPhoBien()){
				if(Boolean.parseBoolean(map.get("chon").toString())){
					re+=","+map.get("userid")+"_"+map.get("donviid");
				}
			}
		}
		
		if(re.trim().compareTo("")!=0){
			re=re.substring(1);
		}
		return "{"+re+"}";
	}
	
	private String getListOrgPhoBien(){
		String re="";
		if(props.getListNhanPhoBien()!=null){
			for(Map map:props.getListPhoBien()){
				if(Boolean.parseBoolean(map.get("chon").toString())){
					re+=","+map.get("donviid");
				}
			}
		}
		
		if(re.trim().compareTo("")!=0){
			re=re.substring(1);
		}
		return re;
	}
	
	/**
	 * 
	 * @author  hltphat
	 * @purpose 
	 * @date    Dec 28, 2017 :: 1:23:27 PM 
	 * @return
	 */
	public boolean kiemTraQuyenSua(Long id){
		if(id == organizationId){
			return true;
		}
		return false;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xem chi tiết văn bản xử lý
	 * @date    May 23, 2014 :: 1:11:42 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionXemVanBan(AjaxBehaviorEvent evt){
		log.info("actionXemVanBan");
		try{
			tabIndex=0;
			Object obj=evt.getComponent().getAttributes().get("obj");
			if(obj!=null){
				Map<String,Object> map=(Map<String,Object>)obj;
				props.setListChuyenXuLy(new ArrayList<Map<String,Object>>());
				
				long vbden_id=Long.parseLong(map.get("vbden_id").toString());
				if(objVBDenFacade.isExists(vbden_id)){
					if(objVBDen_LuanChuyenFacade.getObjLuanChuyen(Long.parseLong(map.get("vbden_lc_id").toString())).getVbden_lc_thuhoi()==true
							&&Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())==false){
						JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Bước xử lý đã bị thu hồi',4);");
						taoDuLieuThongKe(null);			
						props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));
					}else{
						hienTabSet=true;	
						if(map.get("xlChinh")== null || map.get("xlChinh").toString().equals("false")){
							//TODO update trạng thái đã xem phối hợp xử lý
						}
						//nduong custom
						if(map.get("xlChinh").toString().equals("true")){
							 quyenxlchinh = true;
							 log.info("Quyền xử lý chính bây giờ là: "+quyenxlchinh);
						}
						loadThongTin(map);
						taoCayLuanChuyen();
						phanQuyen();	
						JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "setTreeTooltip()");	
					}
				} else{
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản đã bị xóa',4);");
					taoDuLieuThongKe(null);			
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
				}
			} 
		} catch(Exception ex){
			log.error("actionXemVanBan ", ex);
		}
	} 

	/**
	 * @author  ltbao
	 * @purpose Chọn người nhận xử lý ở bước tiếp theo
	 * @date    May 23, 2014 :: 6:16:30 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionChonNguoiXuLy(AjaxBehaviorEvent evt){
		boolean tmp=false;
		props.setListNhanChuyenXuLy(new ArrayList<Map<String,Object>>());
		for(@SuppressWarnings("rawtypes") Map map:props.getListChuyenXuLy()){
			if(Boolean.parseBoolean(map.get("chon").toString())){
				props.getListNhanChuyenXuLy().add(map);
				tmp=true;
			}
		}
		if(tmp){
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChonNguoiNhanXuLy.hide()");
		}
		else{
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Vui lòng chọn người xử lý',3);");
		}
	}
		
	/**
	 * @author  ltbao
	 * @purpose Chọn người nhận phổ biến
	 * @date    May 24, 2014 :: 2:05:27 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionChonNguoiNhanPhoBien(AjaxBehaviorEvent evt){
		boolean tmp=false;
		props.setListNhanPhoBien(new ArrayList<Map<String,Object>>());
		for(@SuppressWarnings("rawtypes") Map map:props.getListPhoBien()){
			if(Boolean.parseBoolean(map.get("chon").toString())){
				props.getListNhanPhoBien().add(map);
				tmp=true;
			}
		}
		if(tmp){
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChonNguoiPhoBien.hide()");
		}
		else{
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Vui lòng chọn người người được phổ biến',3);");
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Tạo chuỗi số ngẫu nhiên
	 * @date    May 26, 2014 :: 9:23:58 AM 
	 * @return  Chuỗi số ngẫu nhiên
	 */
	private String randomString(){
		String s ="";
		Random ran=new Random();
		for(int i=0; i<20;i++){
			s=s+(ran.nextInt(100)+"");
		}
		return s;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Upload tập tin lên server
	 * @date    May 24, 2014 :: 8:51:31 AM 
	 * @param event
	 */
	public void actionUploadFile(FileEntryEvent event) {
	    log.info(">>actionUploadFile");
	    try{							
			FileEntryResults fileResult =  ((FileEntry)event.getComponent()).getResults();				
			for(FileEntryResults.FileInfo fileInfo : fileResult.getFiles()){					
				File oldFile = new File(fileInfo.getFile().getAbsolutePath());
				if(fileInfo.getSize() == 0){
					fileInfo.updateStatus(new MaxFileSizeStatus("Không thể tải tập tin rỗng (0 Kb)"), false, true);
				}else if (CommonUtils.checkFileExt(fileInfo.getFileName(), 
		    		CommonUtils.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(), false, true);
					//checkValidate = false;
		    	} else if (fileInfo.getSize() > (Long.parseLong(loader.getParams().get("vKichThuocFile").toString().trim())*1024*1024) ) {
		    		fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
					//checkValidate = false;
				} else if (Boolean.parseBoolean(CommonUtils.getMimeTypeBundleValue("checkMimeType").toString()) == true &&
		    		CommonUtils.isValidMimeType(oldFile, CommonUtils.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false, true);
					//checkValidate = false;
				}				
				else if(fileInfo.isSaved()){						
					String tenmoi=randomString()+"."+FileNameUtil.getFileNameExtension(fileInfo.getFileName());
					TapTinDinhKemLuanChuyenModel fileObj = new TapTinDinhKemLuanChuyenModel();
					fileObj.setLctt_tenhienthi(fileInfo.getFileName());
					fileObj.setLctt_type(fileInfo.getContentType());
					fileObj.setLctt_tenluutru(tenmoi);
					fileObj.setLctt_ngaytai(DateUtils.getCurrentDate());
					fileObj.setLctt_nhomvb(1);//1. van ban den;
					fileObj.setLctt_size(fileInfo.getSize());
					fileObj.setUserid(getUserId());
					fileObj.setLctt_nguoitai(objUserUtil.getUserFullName());					
					File f=new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_lc//"+fileObj.getLctt_tenhienthi());
					if(f.exists()){
						//Đổi tên
						File newFile = new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_lc//"+fileObj.getLctt_tenluutru());
						f.renameTo(newFile);
						Resource fRe=new HCDTResource(new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_lc//"+fileObj.getLctt_tenluutru()));
						fileObj.setDownload(fRe);
						fileObj.setPath(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_lc//"+fileObj.getLctt_tenluutru());
						fileObj.setType(CommonUtils.getFileExtension(f));
					}
					this.props.getListThemTapTinLuanChuyen().add(fileObj);
					props.getListTapTinLuanChuyen().add(fileObj);
				}
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			 JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileUpload','tmpUpload')");
		}
	    JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileUpload','tmpUpload')");
	}
	
	/**
	 * @author  ltbao
	 * @purpose Upload tập tin lên server
	 * @date    May 24, 2014 :: 8:51:31 AM 
	 * @param event
	 */
	public void actionUploadFileGopY(FileEntryEvent event) {
	    log.info(">>actionUploadFile");
	    try{							
			FileEntryResults fileResult =  ((FileEntry)event.getComponent()).getResults();				
			for(FileEntryResults.FileInfo fileInfo : fileResult.getFiles()){					
				File oldFile = new File(fileInfo.getFile().getAbsolutePath());
				if(fileInfo.getSize() == 0){
					fileInfo.updateStatus(new MaxFileSizeStatus("Không thể tải tập tin rỗng (0 Kb)"), false, true);
				}else if (CommonUtils.checkFileExt(fileInfo.getFileName(), 
		    		CommonUtils.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(), false, true);
					//checkValidate = false;
		    	} else if (fileInfo.getSize() > (Long.parseLong(loader.getParams().get("vKichThuocFile").toString().trim())*1024*1024) ) {
		    		fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
					//checkValidate = false;
				} else if (Boolean.parseBoolean(CommonUtils.getMimeTypeBundleValue("checkMimeType").toString()) == true &&
		    		CommonUtils.isValidMimeType(oldFile, CommonUtils.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false, true);
					//checkValidate = false;
				}
				else if(fileInfo.isSaved()){						
					String tenmoi=randomString()+"."+FileNameUtil.getFileNameExtension(fileInfo.getFileName());
					TapTinGopYModel fileObj = new TapTinGopYModel();
					fileObj.setTtgy_tt_tenhienthi(fileInfo.getFileName());
					fileObj.setTtgy_tt_type(fileInfo.getContentType());
					fileObj.setTtgy_tt_tenluutru(tenmoi);
					fileObj.setTtgy_tt_size(fileInfo.getSize());
					fileObj.setUserid(getUserId());
					fileObj.setTtgy_tt_nguoitai(objUserUtil.getUserFullName());					
					File f=new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_gy//"+fileObj.getTtgy_tt_tenhienthi());
					if(f.exists()){
						File newFile= new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_gy//"+fileObj.getTtgy_tt_tenluutru());
						f.renameTo(newFile);
						HCDTResource fRe=new HCDTResource(new File(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_gy//"+fileObj.getTtgy_tt_tenluutru()));
						fileObj.setDownload(fRe);
						fileObj.setPath(props.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+"//vb_den_gy//"+fileObj.getTtgy_tt_tenluutru());
						fileObj.setType(CommonUtils.getFileExtension(newFile));
					}
					props.getListTapTinGopY().add(fileObj);
				}
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileUploadGopY','tmpUploadGopY')");
		}
	    JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileUploadGopY','tmpUploadGopY')");
	}
	
	/**
	 * @author  lqthai
	 * @purpose Tải lên tập tin đính kèm
	 * @date    May 20, 2014 :: 5:02:27 PM 
	 * @param event
	 */
	public void actionUploadFileSua(FileEntryEvent event) {
	    try{
			FileEntryResults fileResult =  ((FileEntry)event.getComponent()).getResults();				
			for(FileEntryResults.FileInfo fileInfo : fileResult.getFiles()){					
				File oldFile = new File(fileInfo.getFile().getAbsolutePath());
				if(fileInfo.getSize() == 0){
					fileInfo.updateStatus(new MaxFileSizeStatus("Không thể tải tập tin rỗng (0 Kb)"), false, true);
				}else if (CommonUtils.checkFileExt(fileInfo.getFileName(), 
		    		CommonUtils.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(), false, true);
					//checkValidate = false;
		    	} else if (fileInfo.getSize() > (Long.parseLong(loader.getParams().get("vKichThuocFile").toString().trim())*1024*1024) ) {
		    		fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
					//checkValidate = false;
				} else if (Boolean.parseBoolean(CommonUtils.getMimeTypeBundleValue("checkMimeType").toString()) == true &&
		    		CommonUtils.isValidMimeType(oldFile, CommonUtils.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false, true);
					//checkValidate = false;
				} else if(fileInfo.isSaved()){						
					String tenmoi=DigestUtils.md5Hex(new Random().nextInt(9999)+fileInfo.getFileName())
							     +"."+FileNameUtil.getFileNameExtension(fileInfo.getFileName());
					TapTinDinhKemVanBanModel fileObj = new TapTinDinhKemVanBanModel();
					fileObj.setTtdk_tenhienthi(fileInfo.getFileName());
					fileObj.setTtdk_type(fileInfo.getContentType());
					fileObj.setTtdk_tenluutru(tenmoi);
					fileObj.setTtdk_ngaytai(DateUtils.getCurrentDate());
					fileObj.setTtdk_nhomvb(1);//1. van ban den;
					fileObj.setTtdk_size(fileInfo.getSize());
					fileObj.setTtdk_userid(userId);
					fileObj.setTtdk_nguoitai(getUserFullName());
					File f=new File(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTtdk_tenhienthi());
					if(f.exists()){
						log.info("Tồn tại");
						File newFile =new File(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTtdk_tenluutru());
						f.renameTo(newFile);
						log.info("Đỗi tên");
						HCDTResource fRe=new HCDTResource(new File(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTtdk_tenluutru()));
						fileObj.setDownload(fRe);
						fileObj.setUploadnew(true);
						fileObj.setPath(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTtdk_tenluutru());
						fileObj.setType(CommonUtils.getFileExtension(newFile));
						//ds sử dụng lưu tập tin
						props.getListTapTinVanBanSua().add(fileObj);
						props.getListThemSua().add(fileObj);
					}
					
				}
			}
			
		}
		catch(Exception ex){
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileSuaUpload','tmpSuaUpload')");
			ex.printStackTrace();
		}
	    JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileSuaUpload','tmpSuaUpload')");
	    
	}
	
	/**
	 * @author  ltbao
	 * @purpose Lấy danh sách người nhận xử lý
	 * @date    Jun 24, 2014 :: 8:28:12 PM 
	 * @return  
	 */
	@SuppressWarnings("rawtypes")
	public String getListNguoiNhanXuLy(){
		String s= "";
		for(Map map:props.getListChuyenXuLy()){
			if(Boolean.parseBoolean(map.get("chon").toString())){
				s=s+map.get("email")+",";
			}
		}
		return s;
	}
	
	private Map<String,Object> getMapVanBanDen(Long vbden_id, long vbden_lc_id){
		Map<String,Object> map = objVBDen_XuLyFacade.getMapVanBanDen(userId, getListDvUser(), getOrganizationId(), String.valueOf(vbden_id), vbden_lc_id);
		if(map != null && map.get("vbden_butphe_userid") != null){
			map.put("vbden_butphe_fullname", UserUtil.getUserFullName(Long.parseLong(map.get("vbden_butphe_userid").toString())));
		}
		if(map != null && map.get("vbden_butphelanhdao_userid") != null){
			map.put("vbden_butphelanhdao_fullname", UserUtil.getUserFullName(Long.parseLong(map.get("vbden_butphelanhdao_userid").toString())));
		}
		log.info("Map: "+ new Gson().toJson(map));
		return map==null?new HashMap<String, Object>():map;
	}
	
	/**
	 * @author  ltbao
	 * @purpose Lưu thông tin xử lý nhưng không chuyển 
	 * @date    May 24, 2014 :: 3:57:07 PM 
	 * @param evt
	 * @throws SystemException 
	 * @throws PortalException 
	 */
	public void xuLyVanBanDen(AjaxBehaviorEvent evt) throws Exception {
		log.info("luuThongTinXuLy");
		if(xuLyVanBanValidator()){
			UserUtil uu = new UserUtil();
			String noiDungXuLy = "";
			//set lại dữ liệu lưu trữ. nếu không sẽ mất dữ liệu lưu trữ nếu chuyển hoặc hoàn tất xử lý
			props.getObjVBDen_LuanChuyenModel().setVbden_lc_thongtin_lt(props.getObjXemVanBan().get("vbden_thongtin_lt").toString());
			props.getObjVBDen_LuanChuyenModel().setVbden_lc_trangthai_lt(Boolean.parseBoolean(props.getObjXemVanBan().get("vbden_trangthai_lt").toString()));
			
			if(objVBDenFacade.isExists(props.getObjVBDen_LuanChuyenModel().getVbden_id())){
				if(!objVBDen_LuanChuyenFacade.getObjLuanChuyen(props.getObjVBDen_LuanChuyenModel().getVbden_lc_id()).getVbden_lc_thuhoi()){
					boolean thayDoiButPhe = !String.valueOf(props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe()).equals(butPheHientai);
					//Nội dugn thông báo notify
					String noidungThongBao = ("["+props.getObjXemVanBan().get("vbden_sohieugoc").toString().replace("$", "")+"]").trim().replace("[]", "")
							+" "+(props.getObjXemVanBan().get("vbden_trichyeu").toString().length()>100?(props.getObjXemVanBan().get("vbden_trichyeu").toString().substring(0, 100)+"..."):props.getObjXemVanBan().get("vbden_trichyeu").toString());
					/*BEGIN 30.07.2014 Cập nhật chức năng thu hồi*/
					props.getObjVBDen_LuanChuyenModel().setVbden_lc_thaotac(true);
					
					noidungThongBao = HtmlUtil.escape(noidungThongBao);
					if(thayDoiButPhe){
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_butphe_userid(userId);
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_butphe_ngay(DateUtils.getCurrentDate());
						props.getObjXemVanBan().put("vbden_butphe_fullname", UserUtil.getUserFullName(userId));
						props.getObjXemVanBan().put("vbden_butphe_ngay", DateUtils.getCurrentDate());
						props.getObjXemVanBan().put("vbden_butphe_userid", userId);
					}					
					/*END   30.07.2014*/
					if(props.getLoaiPhoBien()==IContanst.PVPB_TATCA){
						noiDungXuLy = "Phổ biến cho tất cả"; 
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_canhan("{}");
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_phongban("");
						if(props.isHoanThanhXuLyToanVanBan()){
							String dsIdNguoiDung = objUserUtil.getChuoiIdNguoiDungTheoDonVi(getOrganizationId());
							String arr[] = dsIdNguoiDung.split(",");
							String dsOrg = "";
							for(String str : arr){
								dsOrg += "," + getOrganizationId();
							}
							dsOrg = dsOrg.equals("") ? null : dsOrg.substring(1);
							log.info("org Notification "+ getOrgNotification());
							objNotificationClient
							.sendNotification(objUserUtil.getUserId()+"", 
									objUserUtil.getChuoiIdNguoiDungTheoDonVi(getOrganizationId()), 
									dsOrg,
									"Phổ biến văn bản đến", 
									NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
									.getLinkModelByMa(Hcdt.TRA_CUU).getLink_id(), 
									"?id="+props.getObjXemVanBan().get("vbden_id").toString()+"&nhom=1&tab=0&phathanh=2"
									,noidungThongBao, 
									CommonUtils.getBundleValue("vIconXuLyChinh").toString(), NotificationsVar.NHAN_PHO_BIEN,
									getOrgNotification());
						}
					} else if(props.getLoaiPhoBien()==IContanst.PVPB_NOIBO){
						noiDungXuLy = "Phổ biến cho nội bộ phòng: " + uu.getOrganization(getOrganizationId()).getName(); 
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_canhan("{}");
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_phongban(getOrganizationId()+"");
						if(props.isHoanThanhXuLyToanVanBan()){
							String dsIdNguoiDung = objUserUtil.getChuoiIdNguoiDungTheoDonVi(getOrganizationId());
							String arr[] = dsIdNguoiDung.split(",");
							String dsOrg = "";
							for(String str : arr){
								dsOrg += "," + getOrganizationId();
							}
							dsOrg = dsOrg.equals("") ? null : dsOrg.substring(1);
							log.info("org Notification "+ getOrgNotification());
							objNotificationClient
							.sendNotification(objUserUtil.getUserId()+"", 
									objUserUtil.getChuoiIdNguoiDungTheoDonVi(getOrganizationId()), 
									dsOrg,
									"Phổ biến văn bản đến", 
									NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
									.getLinkModelByMa(Hcdt.TRA_CUU).getLink_id(), 
									"?id="+props.getObjXemVanBan().get("vbden_id").toString()+"&nhom=1&tab=0&phathanh=2"
									,noidungThongBao, 
									CommonUtils.getBundleValue("vIconXuLyChinh").toString(), NotificationsVar.NHAN_PHO_BIEN,
									getOrgNotification());
						}
						
					} else if(props.getLoaiPhoBien()==IContanst.PVPB_NGUOINHAN){
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_canhan(getListPhoBien());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_phongban("");
						noiDungXuLy = "Phổ biến đến: ";
						int i = 0;
						for(Map map:props.getListPhoBien()){
							if(Boolean.parseBoolean(map.get("chon").toString()) && i == 0){
								noiDungXuLy += uu.getUserFullName(Long.parseLong(map.get("userid").toString()));
							}else if(Boolean.parseBoolean(map.get("chon").toString())){
								noiDungXuLy += ", " + uu.getUserFullName(Long.parseLong(map.get("userid").toString()));
							}
							i++;
						}
						//Gửi thông báo notifications
						if(props.isHoanThanhXuLyToanVanBan()){
							log.info("org Notification "+ getOrgNotification());
							objNotificationClient
							.sendNotification(objUserUtil.getUserId()+"", 
									props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan().replace("{", "").replace("}",""), 
									getListOrgPhoBien().replace("{","").replace("}",""),
									"Phổ biến văn bản đến", 
									NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
									.getLinkModelByMa(Hcdt.TRA_CUU).getLink_id(), 
									"?id="+props.getObjXemVanBan().get("vbden_id").toString()+"&nhom=1&tab=0&phathanh=2"
									,noidungThongBao, 
									CommonUtils.getBundleValue("vIconXuLyChinh").toString(), NotificationsVar.NHAN_PHO_BIEN,
									getOrgNotification());
						}
						
					} else if(props.getLoaiPhoBien()==IContanst.PVPB_KHONGPHOBIEN){
						noiDungXuLy = "Không phổ biến";
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_canhan("{}");
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_pb_phongban("0");
					}
					
					if(props.isHoanThanhXuLyToanVanBan()){
						log.info("POINT 1 {}",objBanLienDonViFacade.getObjLienDonViModelLuuXuLy(props.getObjVBDen_LuanChuyenModel().getVbden_id()));
						log.info("POINT 2 {}", props.getObjVBDen_LuanChuyenModel().getVbden_id());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat(Calendar.getInstance().getTime());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat_userid(objUserUtil.getUserId());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_hoanthanh(true);
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_phongban(phongban_org);
						objVBDen_XuLyFacade.luuXuLy(props.getObjVBDen_LuanChuyenModel(), 
								props.getListThemTapTinLuanChuyen(), props.getListXoaTapTinLuanChuyen(), true, thayDoiButPhe);
						luuKetQuaXuLyCungHeThong(props.getObjVBDen_LuanChuyenModel().getVbden_id(),noiDungXuLy);
						props.getObjVanBanDen().put("vColorToanVb", loader.getParams().get("vColor_daxuly").toString());
						props.getObjVanBanDen().put("vColor", loader.getParams().get("vColor_daxuly").toString());
						props.getObjVanBanDen().put("vbdden_lc_hoanthanh", true);
						props.getObjVanBanDen().put("vbden_hoanthanh", true);
						props.getObjXemVanBan().put("vbden_hoanthanh", true);
						//Cập nhật lại loại bxl
						if( butPhe ){
							props.getObjXemVanBan().put("vbden_butphe", props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe());
						}
						
						props.getObjVanBanDen().put("vbden_lc_lbxl",0);						
						props.getObjXemVanBan().put("vbden_pb_phongban", props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_phongban());
						props.getObjXemVanBan().put("vbden_pb_canhan", props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan());
						
						luuTapTapTinLuanChuyenChinhThuc();		
						//Tạo lại dữ liệu thống kê		
						tabSelect = 1;
					} else if(props.isChuyenXuLy()){
						List<Long> dsUserIdPhoiHop 	= listUserToListId(props.getListChuyenXuLy());
						List<Long> dsIdOrgXlChinh 	= listIdOrgXlChinh(props.getListChuyenXuLy());
						List<Long> dsIdOrgXlPhoiHop = listIdOrgXlPhoiHop(props.getListChuyenXuLy());
						
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_hoanthanh(true);	
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_xlphoihop_dsuserid( CollectionUtil.convertToJsonString(dsUserIdPhoiHop) );						
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_id(Long.parseLong(props.getObjXemVanBan().get("vbden_id").toString()));
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_hoanthanh(false);
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_mail(false);
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_max(true);
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_ngaychuyen(Calendar.getInstance().getTime());
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_cha(props.getObjVBDen_LuanChuyenModel().getVbden_lc_id());
						//props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_butphe(props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe());// Kế thừa but phê củ
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_trangthai_lt(props.getObjVBDen_LuanChuyenModel().getVbden_lc_trangthai_lt()); //Kế thừa trạng thái lưu trữ
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_pb_phongban(props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_phongban());//Kế thừa nội dung phổ biến
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_pb_canhan(props.getObjVBDen_LuanChuyenModel().getVbden_lc_pb_canhan());//Kế thừa nội dung phổ biến
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_thongtin_lt(props.getObjVBDen_LuanChuyenModel().getVbden_lc_thongtin_lt());//Kế thừa thông tin lưu trữ
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_trangthai_lt(props.getObjVBDen_LuanChuyenModel().getVbden_lc_trangthai_lt());//Kế thừa trạng thái lưu trữ
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_nguoichuyen(getUserFullName() + (xuLyThay?(" (Xử lý thay "+UserUtil.getUserFullName(props.getObjVBDen_LuanChuyenModel().getVbden_lc_xlchinh_userid())+")"):""));
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_cn_gannhat(Calendar.getInstance().getTime());
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_cn_gannhat_userid(getUserId());
						
						//@ptgiang Xử lý bổ sung	
						String curDate = DateUtils.formatDate2String(DateUtils.getCurrentDate(), "dd/MM/yyyy HH:mm:ss");
						long userIdXlChinh = 0;
						long dvIdXlChinh = 0;
						
						ChuyenBoSungModel chuyenBoSungModel = new ChuyenBoSungModel();
						chuyenBoSungModel.setHistory(new HashMap<String, Map<String,List<String>>>());
						chuyenBoSungModel.setCurrent(new HashMap<String, String>());		
						
						List<String> dsUserIdDvId = new ArrayList<String>();
						if(!CollectionUtil.isNullOrEmpty(props.getListChuyenXuLy())){
							for(Map<String, Object> map : props.getListChuyenXuLy()){
								if( Boolean.parseBoolean(map.get("chon").toString()) ){
									//nduong
									long userid = Long.valueOf(map.get("userid").toString());
									long dvid = Long.valueOf(map.get("donviid").toString());
									String userIdDvId = String.format(IContanst.PATTERN_USERID_DVID, userid, dvid);
									dsUserIdDvId.add(userIdDvId);
									
									if(map.get("xlchinh") != null && Boolean.parseBoolean(map.get("xlchinh").toString())){
										userIdXlChinh = userid;
										dvIdXlChinh = dvid;
									} else{
										chuyenBoSungModel.getCurrent().put(userIdDvId, curDate);
									}
								}
							}
						}						
						
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_xlchinh_userid(userIdXlChinh);
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_xlchinh_dvid(dvIdXlChinh);
						
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_xlbosung(CollectionUtil.convertObjToJson(chuyenBoSungModel).toString()); 
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_user_donvi(CollectionUtil.convertToJsonString(dsUserIdDvId));
						
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_max(false);
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat(Calendar.getInstance().getTime());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat_userid(objUserUtil.getUserId());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_phongban(phongban_org);
						/*Thông tin cập nhật gần nhất cho luan chuyen truoc*/
						props.getObjVBDen_LuanChuyenModelTiepTheo().setVbden_lc_lbxl(0);
						if(props.getObjVBDen_LuanChuyenModel().getVbden_lc_mail()){
							props.getObjVBDen_LuanChuyenModel().setVbden_lc_mail_dsnhan(props.getTxtDsEmailNhanXuLy()); 
						}
						objVBDen_XuLyFacade.chuyenXuLy(props.getObjVBDen_LuanChuyenModel(), props.getObjVBDen_LuanChuyenModelTiepTheo(), props.getListThemTapTinLuanChuyen(), props.getListXoaTapTinLuanChuyen(),thayDoiButPhe);			
						
						//Cập nhật lại loại bxl
						props.getObjVanBanDen().put("vbden_lc_lbxl",0);
						//Buoc ke tiep co phoi hop xl
						if(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlphoihop_dsuserid().contains(userId+"")){
							props.getObjVanBanDen().put("xlchinh",false);
							props.getObjVanBanDen().put("xlphoihop", true);
							log.info("Là phối hợp");
						}
						//Buoc ke tiep khog phoi hop cung khong xu ly chinh
						else if(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlchinh_userid()!=userId){
							props.getObjVanBanDen().put("vbden_lc_hoanthanh",true);
							props.getObjVanBanDen().put("vColor", loader.getParams().get("vColor_daxuly"));
							log.info("Hoàn thành");
						}
						luuTapTapTinLuanChuyenChinhThuc();			
						//Kiểm tra gửi mail
						if(props.getObjVBDen_LuanChuyenModel().getVbden_lc_mail()){
							emailXuLyChinh = objUserUtil.getUser(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlchinh_userid()).getEmailAddress();
							
							String []arrMailNhanXuLy = CommonUtils.getValidEmails(props.getTxtDsEmailNhanXuLy().split(","));
							
							sendMailXuLy(props.getObjVBDen_LuanChuyenModel().getVbden_id(), arrMailNhanXuLy, emailXuLyChinh, props.getListChuyenXuLy());
						}					
						log.info(">>>dsIdOrgXlChinh: 	" + dsIdOrgXlChinh);
						log.info(">>>dsIdOrgXlPhoiHop: 	" + dsIdOrgXlPhoiHop);
						String idOrgXlChinh = dsIdOrgXlChinh.toString().replace("[", "").replace("]", "").replace(" ", "");
						String idOrgXlPhoiHop = dsIdOrgXlPhoiHop.toString().replace("[", "").replace("]", "").replace(" ", "");
						//Gửi notification đến người nhận xử lý chính
						log.info("org Notification "+ getOrgNotification());
						objNotificationClient
							.sendNotification(objUserUtil.getUserId()+"",
									props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlchinh_userid()+"", 
									idOrgXlChinh,
									"Xử lý văn bản đến", 
									NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
									.getLinkModelByMa(Hcdt.XU_LY_VAN_BAN_DEN).getLink_id(), 
									"?instant="+props.getObjXemVanBan().get("vbden_id").toString()
									,noidungThongBao, 
									CommonUtils.getBundleValue("vIconXuLyChinh").toString(), NotificationsVar.NHAN_CHUYEN_XU_LY_CHINH,
									getOrgNotification());
						//Gửi thông  báo đến người phối hợp
						if(props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlphoihop_dsuserid().compareTo("{}")!=0){
							log.info("org Notification "+ getOrgNotification());
							objNotificationClient
							.sendNotification(objUserUtil.getUserId()+"", 
									props.getObjVBDen_LuanChuyenModelTiepTheo().getVbden_lc_xlphoihop_dsuserid().replace("{","").replace("}",""), 
									idOrgXlPhoiHop,
									"Xử lý văn bản đến", 
									NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
									.getLinkModelByMa(Hcdt.XU_LY_VAN_BAN_DEN).getLink_id(),
									"?instant="+props.getObjXemVanBan().get("vbden_id").toString()
									,noidungThongBao, 
									CommonUtils.getBundleValue("vIconPhoiHop").toString(), NotificationsVar.NHAN_CHUYEN_XU_LY_PHOI_HOP,
									getOrgNotification());
						}			
						
						if( butPhe ){
							props.getObjXemVanBan().put("vbden_butphe", props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe());
						}					
						tabSelect = 1;
					} else{
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_hoanthanh(false);
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat(Calendar.getInstance().getTime());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_cn_gannhat_userid(objUserUtil.getUserId());
						props.getObjVBDen_LuanChuyenModel().setVbden_lc_phongban(phongban_org);
						
						objVBDen_XuLyFacade.luuXuLy(props.getObjVBDen_LuanChuyenModel(), props.getListThemTapTinLuanChuyen(), props.getListXoaTapTinLuanChuyen(), false, thayDoiButPhe);
						luuTapTapTinLuanChuyenChinhThuc();							 
					}	
					
					//Kiểm tra mail phổ biến
					if(mailPhoBien && quyenphobien){
						log.info(" --> Thêm mail phổ biến");
						MailPhoBienModel objMailPhoBienModel=new MailPhoBienModel();
						objMailPhoBienModel.setMail_ngaygui(Calendar.getInstance().getTime());
						objMailPhoBienModel.setMail_nguoigui(getUserFullName());
						objMailPhoBienModel.setMail_nguoiguiid(getUserId());
						objMailPhoBienModel.setNhomvb(1);
						objMailPhoBienModel.setVb_id(props.getObjVBDen_LuanChuyenModel().getVbden_id());
						objMailPhoBienModel.setMail_dsnguoinhan(props.getTxtDsEmailNhanPhoBien());
						objMailPhoBienFacade.themMailPhoBien(objMailPhoBienModel);
						mailPhoBien=false;
						/*Soan mail*/
						props.setListMailPhoBien(objMailPhoBienFacade.getListMailPhoBien(vbden_id, IContanst.NVB_DEN));
						try {
							sendEmailPhoBien(props.getTxtDsEmailNhanPhoBien().split(","));
						} catch (Exception e) {
							JavascriptContext.addJavascriptCall(
									FacesContext.getCurrentInstance(), "thongBao('Gửi email thất bại',4);");
							log.error("Err SendMail", e); 
						}
					}			
					
					taoCayLuanChuyen();
					phanQuyen();
					
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Cập nhật thành công',3);");
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "activeTabTienTrinh();");
					log.info(">> Hoàn thành");
				} else{
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Bước xử lý đã bị thu hồi',4);");
					//taoDuLieuThongKe(null);			
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
				}
			} else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản không tồn tại, không thể xử lý',4);");
				//taoDuLieuThongKe(null);			
				props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
				phanQuyen();
			}
			
			//iopio
			//Cập nhật lại màu
			Map<String,Object> mapVanBan = getMapVanBanDen(vbden_id, vbden_lc_id);
			if(mapVanBan!=null){
				props.getObjVanBanDen().put("vColor", loader.getParams().get(mapVanBan.get("color")));
				props.getObjVanBanDen().put("vColorToanVb", loader.getParams().get(mapVanBan.get("color_toanvanban")));		
				Set<String> dsKey = props.getObjVanBanDen().keySet();
				for(String str:dsKey){
					if(mapVanBan.get(str)!=null){
						props.getObjVanBanDen().put(str, mapVanBan.get(str));
					}
				}
				props.getObjVanBanDen().put("select", 1);
				
				mapVanBan = null;
			}		
		}
		if(props.isLocTheoHSVB()){
			taoDuLieuThongKe(getFilterModelTheoHSVB());
		} else{
			taoDuLieuThongKe(null);
		}
	}
	
	public void actionLoadDsEmail(){
		props.setTxtDsEmailNhanXuLy(pmGetDsEmailNhanXuLy(props.getListChuyenXuLy())); 
	}
	
	/**
	 * @author  ltbao
	 * @purpose Chuẩn bị thu hồi văn bản
	 * @date    Jul 30, 2014 :: 10:05:59 AM 
	 * @param   evt
	 */
	public void preActionThuHoi(AjaxBehaviorEvent evt){
		try {
			log.info(">>preActionThuHoi");
			Object obj = evt.getComponent().getAttributes().get("obj");
			if(obj!=null){
				props.setLyDoThuHoi("");
				props.setVanBanThuHoi(Long.parseLong(obj.toString()));
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogThuHoi.show();");
			} 
		} catch (Exception ex) {
			log.error("err ", ex);
		}
		
	}
	
	public String layGiaTriTrongChuoi(String str, int index){
		String[] array = str.split("@");
		if(index<array.length){
			return array[index];
		}
		return "";
	}
	
	/**
	 * @author  ltbao
	 * @purpose Sự kiện thu hồi văn bản
	 * @date    Jul 30, 2014 :: 9:57:56 AM 
	 * @param   evt
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void actionThuHoi(AjaxBehaviorEvent evt){
		VBDen_LuanChuyenModel objCanThuHoi = objVBDen_LuanChuyenFacade.getObjLuanChuyen(props.getVanBanThuHoi());
		boolean validate = false;
		if(props.getLyDoThuHoi().trim().compareTo("")!=0){
			validate = true;
		}
		if(objVBDen_LuanChuyenFacade.kiemTraThuHoi(props.getVanBanThuHoi())){
			validate = false;
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogThuHoi.hide();");
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Bước xử lý đã bị thu hồi',4);");
			taoCayLuanChuyen();
			phanQuyen();
		}
		if(validate){
			if(!objCanThuHoi.getVbden_lc_thaotac()){
				objCanThuHoi.setVbden_lc_thuhoi(true);
				objCanThuHoi.setVbden_lc_ldthuhoi(props.getLyDoThuHoi()+"@"+objUserUtil.getUserFullName());
				//objCanThuHoi.setVbden_lc_phongban(phongban_org);
				//Tạo bước copy luân chuyển
				VBDen_LuanChuyenModel objLuanChuyenCopy = objVBDen_LuanChuyenFacade.getObjLuanChuyen(objCanThuHoi.getVbden_lc_cha());
				
				if(objLuanChuyenCopy.getVbden_lc_cha()==-1){
					objCanThuHoi.setVbden_lc_phongban(objLuanChuyenCopy.getVbden_lc_phongban_0());
				}else{
					//Lấy bước trước đó của bước cha
					VBDen_LuanChuyenModel objAccestor = objVBDen_LuanChuyenFacade.getObjLuanChuyen(objLuanChuyenCopy.getVbden_lc_cha());				
					//Set lại giá trị phòng ban cảu obj thu hồi
					objCanThuHoi.setVbden_lc_phongban(objAccestor.getVbden_lc_phongban());
				}
				
				List<TapTinDinhKemLuanChuyenModel> listTapTin = objTapTinDinhKemLuanChuyenFacade.getDsTapTinTheoLuanChuyen(objLuanChuyenCopy.getVbden_lc_id(),1);
				objLuanChuyenCopy.setVbden_lc_id(null);
				objLuanChuyenCopy.setVbden_lc_hoanthanh(false);
				objLuanChuyenCopy.setVbden_lc_cha(objCanThuHoi.getVbden_lc_id());
				objLuanChuyenCopy.setVbden_lc_lbxl(0);
				long reverse_id = objVBDen_XuLyFacade.thuHoiXuLyVanBan(objCanThuHoi, objLuanChuyenCopy, listTapTin);//lc_id_thuHoi
				
				taoDuLieuThongKe(null);
				props.setListVanBanDenLazy(getLazyDataModel(pageLoad, cPageSize,false,null));
				props.getListVanBanDen().get(0).put("select", 0);
				for(Map map:props.getListVanBanDen()){
					if(Long.parseLong(map.get("vbden_lc_id").toString())==reverse_id){
						loadThongTin(map);
						taoCayLuanChuyen();				
						phanQuyen();
						map.put("select", 1);
						break;
					}
				}
				
				//khoiTaoMauXuLy();
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogThuHoi.hide();");
				//Gửi thông báo thu hồi
				String noidungThongBao = ("["+props.getObjXemVanBan()
						.get("vbden_sohieugoc").toString()
						.replace("$", "")+"]").trim().replace("[]", "")+" "+(props.getObjXemVanBan()
								.get("vbden_trichyeu").toString()
								.length()>100?(props.getObjXemVanBan()
										.get("vbden_trichyeu").toString()
										.substring(0, 100)+"..."):props.getObjXemVanBan()
										.get("vbden_trichyeu").toString())+". Lý do thu hồi: "+props.getLyDoThuHoi();
				
				noidungThongBao = HtmlUtil.escape(noidungThongBao);
				log.info("org Notification "+ getOrgNotification());
				objNotificationClient.sendNotification(objUserUtil.getUserId()+"", 
						objCanThuHoi.getVbden_lc_xlchinh_userid()+"",
						objCanThuHoi.getVbden_lc_xlchinh_dvid()+"",
						"Thu hồi xử lý văn bản đến", "", 
						objDmLinkFacade.getLinkModelByMa("NONE").getLink_id(), "", noidungThongBao, 
						CommonUtils.getBundleValue("vIconXuLyChinh"), NotificationsVar.NHAN_CHUYEN_XU_LY_CHINH,
						getOrgNotification());
				
				if(objCanThuHoi.getVbden_lc_xlphoihop_dsuserid().compareTo("{}")!=0){
					String dsId = objCanThuHoi.getVbden_lc_user_donvi().replace("{", "").replace("}", "").replace(" ", "");
					String arrUserOrg_Id[] = dsId.split(",");
					Map<String, String> mapUserOrg_Id = new HashMap<String, String>();
					for(String userOrg_Id : arrUserOrg_Id){
						String id[] = userOrg_Id.split("_");
						mapUserOrg_Id.put(id[0], id[1]);
					}
					//log.info(">>>mapUserOrg_Id: "+mapUserOrg_Id);
					String dsIdPhoiHop[] = objCanThuHoi.getVbden_lc_xlphoihop_dsuserid().replace("{", "").replace("}", "").replace(" ", "").split(",");
					String dsOrgPhoiHop = "";
					for(String id : dsIdPhoiHop){
						if(mapUserOrg_Id.get(id)!=null){
							dsOrgPhoiHop += "," + mapUserOrg_Id.get(id);
						}
					}
					if(!dsOrgPhoiHop.equals("")){
						dsOrgPhoiHop = dsOrgPhoiHop.substring(1);
					}
					dsOrgPhoiHop = dsOrgPhoiHop.equals("") ? null : dsOrgPhoiHop;
					//log.info(">>>dsOrgPhoiHop: "+dsOrgPhoiHop);
					log.info("org Notification "+ getOrgNotification());
					objNotificationClient.sendNotification(objUserUtil.getUserId()+"", 
							objCanThuHoi.getVbden_lc_xlphoihop_dsuserid().replace("{", "").replace("}", ""),
							dsOrgPhoiHop,
							"Thu hồi xử lý văn bản đến", "", 
							objDmLinkFacade.getLinkModelByMa("NONE").getLink_id(), "", 
							noidungThongBao, 
							CommonUtils.getBundleValue("vIconPhoiHop").toString(), NotificationsVar.NHAN_CHUYEN_XU_LY_PHOI_HOP,
							getOrgNotification());
				}
			}else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogThuHoi.hide();");
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Bước luân chuyển đã được thao tác, không thể thu hồi',4);");
				taoCayLuanChuyen();
				phanQuyen();
			}
		}else{
			Validator.showErrorMessage(getPortletId(), "frmThuHoi:txtLyDoThuHoi", "Vui lòng nhập lý do thu hồi");
		}
	}
	
	public void actionXemLyDoThuHoi(AjaxBehaviorEvent evt){
		Object  obj = evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			props.setLyDoThuHoi(obj.toString());
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),"dialogLyDoThuHoi.show()");
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose 
	 * @date    Jun 13, 2014 :: 9:03:39 AM 
	 * @param to
	 */
	private void sendEmailPhoBien(final String[] dsMailTo) throws Exception{	
		
		if(dsMailTo == null || dsMailTo.length == 0){
			return;
		}
		
		log.info("sendEmailPhoBien {}", Arrays.toString(dsMailTo)); 
		
		final String urlTraCuu = CommonUtils.getPathServer()
				+ "/"
				+ objDmLinkFacade.getLinkModelByMa(Hcdt.TRA_CUU).getLink_giatri()     
				+ "?id=" + props.getObjXemVanBan().get("vbden_id").toString()
				+ "&nhom=" + IContanst.NVB_DEN + "&tab=0&phathanh=2";
		
		final String tieuDe = loader.getParams().get("vMailPhoBienDen_TieuDe").toString().replace("[$doKhan$]", props.getObjXemVanBan().get("dk_ten").toString());
		final String noiDung = loader.getParams().get("vMailPhoBienDen_NoiDung").toString()
				.replace("[$donviTiepNhan$]", props.getObjXemVanBan().get("donvixuly").toString())
				.replace("[$nguoiGui$]", props.getObjXemVanBan().get("vbden_nguoinhap").toString())
				.replace("[$soHieuGoc$]", props.getObjXemVanBan().get("vbden_sohieugoc").toString())
				.replace("[$soDen$]", props.getObjXemVanBan().get("vbden_soden").toString())
				.replace("[$doKhan$]", props.getObjXemVanBan().get("dk_ten").toString())
				.replace("[$soVanBan$]", props.getObjXemVanBan().get("svb_ten").toString())
				.replace("[$doMat$]", props.getObjXemVanBan().get("dm_ten").toString())
				.replace("[$cachthucXuLy$]", props.getObjXemVanBan().get("ctxl_ten").toString())
				.replace("[$trichYeu$]", HtmlUtil.escape(props.getObjXemVanBan().get("vbden_trichyeu").toString()));
		
		if( props.getLoaiPhoBien() == IContanst.PVPB_TATCA ){
//			Email email = new Email();
//			email.setSubject(tieuDe);
//			email.setText(noiDung);			
//			email.setTo(dsMailTo);	
//			email.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);
//			EmailProvider.sendMail(email); 
			
			EmailDataProvider emailDataProvider = new EmailDataProvider();
			DsEmail objEmailModel = new DsEmail();
			objEmailModel.setEmail_tieude(tieuDe);
			objEmailModel.setEmail_noidung(noiDung);
			objEmailModel.setNgay_gui(DateUtils.getCurrentDate());
			String strMailTo = "";
			for (String string : dsMailTo) {
				strMailTo += string +";";
			}
			objEmailModel.setEmail_to(strMailTo);
			objEmailModel.setEmail_trangthai(0);
			objEmailModel.setCompanyid(getCompanyId());
			emailDataProvider.actionInsertOrEdit(objEmailModel, 3);
			
		} else{
			for( String mailTo : dsMailTo ){
				if( mailTo.isEmpty() ){
					continue;
				}
				
				mailTo = mailTo.trim();
				
				Long userIdNhan = objVanBanPhoiHopFacade.getUserIdByEmail(mailTo);
				VanBanPhoiHopModel vbphModel = new VanBanPhoiHopModel();
				vbphModel.setLink_ma(Hcdt.TRA_CUU.toString()); 
				vbphModel.setCompanyid(props.getCompanyId());
				vbphModel.setNvb_id(IContanst.NVB_DEN);
				vbphModel.setVb_id( Long.parseLong(props.getObjXemVanBan().get("vbden_id").toString()) );
				vbphModel.setPhxl_email_nhan(mailTo);
				vbphModel.setPhxl_ngaygui(DateUtils.getCurrentDate());
				vbphModel.setPhxl_nguoigui_userid(getUserId()); 	
				vbphModel.setPhxl_nguoinhan_userid(userIdNhan);
				String maTruyCap = CommonUtils.generateRandomString(IContanst.DODAI_MATRUYCAP);
				vbphModel.setPhxl_matruycap(maTruyCap); 			
				objVanBanPhoiHopFacade.capNhatVanBanPhoiHop(vbphModel);	

				String noiDungMail = noiDung;
				if( userIdNhan == null ){				
					noiDungMail = noiDungMail.replace("[$url$]", (urlTraCuu + "&email=" + mailTo + " <br/><br/>Mã truy cập: <b>" + maTruyCap + "</b>") );
				} else{
					noiDungMail = noiDungMail.replace("[$url$]", urlTraCuu + "&email=" + mailTo );	
				}

				log.info("send to {}", mailTo); 
//				Email email = new Email();
//				email.setSubject(tieuDe);
//				email.setText(noiDungMail);			
//				email.setTo(mailTo);	
//				email.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);
//				EmailProvider.sendMail(email);
				
				EmailDataProvider emailDataProvider = new EmailDataProvider();
				DsEmail objEmailModel = new DsEmail();
				objEmailModel.setEmail_tieude(tieuDe);
				objEmailModel.setEmail_noidung(noiDungMail);
				objEmailModel.setNgay_gui(DateUtils.getCurrentDate());
				objEmailModel.setEmail_to(mailTo);
				objEmailModel.setEmail_trangthai(0);
				objEmailModel.setCompanyid(getCompanyId());
				emailDataProvider.actionInsertOrEdit(objEmailModel, 3);
			}
		}
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Gửi mail thông báo xử lý văn bản
	 * @date    Dec 6, 2016 :: 10:02:50 AM 
	 * @param vbden_id
	 * @param dsMailTo
	 * @param noidung
	 * @param emailXuLyChinh
	 * @param listChuyenXuLy
	 */
	private void sendMailXuLy(final long vbden_id, final String[] dsMailTo, 
			final String emailXuLyChinh, final List<Map<String,Object>> listChuyenXuLy){		
		log.info("sendMailXuLy"); 
		try {
			final String tieude = loader.getParams().get("vMailChuyenXuLyDen_TieuDe").toString().replace("[$doKhan$]", props.getObjXemVanBan().get("dk_ten").toString());
			
			final String noidung = loader.getParams().get("vMailChuyenXuLyDen_NoiDung").toString()
					.replace("[$donviTiepNhan$]", props.getObjXemVanBan().get("donvixuly").toString())
					.replace("[$nguoiGui$]", props.getObjXemVanBan().get("vbden_nguoinhap").toString())
					.replace("[$soHieuGoc$]", props.getObjXemVanBan().get("vbden_sohieugoc").toString())
					.replace("[$soDen$]", props.getObjXemVanBan().get("vbden_soden").toString())
					.replace("[$doKhan$]", props.getObjXemVanBan().get("dk_ten").toString())
					.replace("[$soVanBan$]", props.getObjXemVanBan().get("svb_ten").toString())
					.replace("[$doMat$]", props.getObjXemVanBan().get("dm_ten").toString())
					.replace("[$cachthucXuLy$]", props.getObjXemVanBan() != null ? props.getObjXemVanBan().get("ctxl_ten").toString() : "")
					.replace("[$trichYeu$]", HtmlUtil.escape(props.getObjXemVanBan().get("vbden_trichyeu").toString()));		

			String pathServer = CommonUtils.getPathServer();			
			String urlVanBanPhoiHop = pathServer + "/" + 
					objDmLinkFacade.getLinkModelByMa(CommonUtils.Hcdt.TRA_CUU).getLink_giatri() + 
						"?id=" + vbden_id +
						"&nhom=" + IContanst.NVB_DEN + 
						"&email=";
			
			String urlXuLyVanBan = pathServer + "/" + 
					objDmLinkFacade.getLinkModelByMa(CommonUtils.Hcdt.XU_LY_VAN_BAN_DEN).getLink_giatri() + 
					"?instant=" + vbden_id;
			
			for( String mailTo : dsMailTo ){
				mailTo = mailTo.trim();
				
				boolean coQuyenXuLy = false;
				for( Map<String,Object> mapUser : listChuyenXuLy ){					
					if( mapUser.get("chon") == null || Boolean.parseBoolean(mapUser.get("chon").toString()) == false ||
							com.cusc.hcdt.util.StringUtils.isNullOrEmpty(mapUser.get("email")) ){ 
						continue;
					}
					if(mailTo.equalsIgnoreCase(mapUser.get("email").toString()) ){
						coQuyenXuLy = true;
						break;
					}
				}

				String noiDungMail = noidung;
				if(coQuyenXuLy == false){
					//Lưu văn bản phối hợp
					VanBanPhoiHopModel vbphModel = new VanBanPhoiHopModel();
					vbphModel.setLink_ma(Hcdt.VAN_BAN_PHOI_HOP.toString()); 
					vbphModel.setCompanyid(props.getCompanyId());
					vbphModel.setNvb_id(IContanst.NVB_DEN);
					vbphModel.setVb_id(vbden_id);
					vbphModel.setPhxl_email_nhan(mailTo);
					vbphModel.setPhxl_ngaygui(DateUtils.getCurrentDate());
					vbphModel.setPhxl_nguoigui_userid(getUserId()); 		
					vbphModel.setPhxl_nguoinhan_userid(objVanBanPhoiHopFacade.getUserIdByEmail(vbphModel.getPhxl_email_nhan())); 						
					String maTruyCap = CommonUtils.generateRandomString(IContanst.DODAI_MATRUYCAP);
					noiDungMail = noiDungMail.replace("[$url$]", (urlVanBanPhoiHop + mailTo + " <br/><br/>Mã truy cập: <b>" + maTruyCap + "</b>") );	
					vbphModel.setPhxl_matruycap(maTruyCap); 
					objVanBanPhoiHopFacade.capNhatVanBanPhoiHop(vbphModel);
				} else{					 
					noiDungMail = noiDungMail.replace("[$url$]", urlXuLyVanBan);						
				}

				if(mailTo.trim().equals(emailXuLyChinh.trim())){
					noiDungMail = noiDungMail.replace("[$phamViXuLy$]", "Xử lý chính");
				} else{
					noiDungMail = noiDungMail.replace("[$phamViXuLy$]", "Phối hợp xử lý");
				}
				
				//log.info("send to {} content {}", mailTo, noiDungMail); 
//				Email mail = new Email();
//				mail.setTo(mailTo);				
//				mail.setSubject(tieude);
//				mail.setText(noiDungMail);	
//				mail.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);	
//				EmailProvider.sendMail(mail);
				
				EmailDataProvider emailDataProvider = new EmailDataProvider();
				DsEmail objEmailModel = new DsEmail();
				objEmailModel.setEmail_tieude(tieude);
				objEmailModel.setEmail_noidung(noiDungMail);
				objEmailModel.setNgay_gui(DateUtils.getCurrentDate());
				objEmailModel.setEmail_to(mailTo);
				objEmailModel.setEmail_trangthai(0);
				objEmailModel.setCompanyid(getCompanyId());
				emailDataProvider.actionInsertOrEdit(objEmailModel, 3);
				
			}
		} catch (Exception e) { 
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Gửi email thất bại',3);");
			log.error("Err sent mail", e);
		}
	}
		
	private String pmGetDsEmail(List<Map<String, Object>> dsUser){
		StringBuilder sb = new StringBuilder();
		if(!CollectionUtil.isNullOrEmpty(dsUser)){ 
			boolean isFirst = true;
			for(Map<String,Object> map: dsUser){
				if( isFirst == false ){
					sb.append(",");
				}
				sb.append( map.get("email").toString() );
				isFirst = false;
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author  ltbao
	 * @purpose Sửa văn bản đến
	 * @date    Jun 11, 2014 :: 5:27:56 PM 
	 * @param evt
	 */
	@SuppressWarnings("rawtypes")
	public void preActionSuaVanBanDen(AjaxBehaviorEvent evt){
		try{
		if(objVBDenFacade.isExists(vbden_id)){
			props.setObjVBDenModel(objVBDenFacade.getObjVanBanDen(vbden_id));
			//Kiểm tra lại thông tin độ mật của văn bản
			boolean cauHinhDoMat = false;
			CauHinhDoMatModel objCauHinh = objCauHinhDoMatFacade.getObjCauHinhDoMat(getOrganizationId(), props.getObjVBDenModel().getDm_id());
			props.getObjXemVanBan().put("dm_id", props.getObjVBDenModel().getDm_id());
			if(objCauHinh!=null){
				cauHinhDoMat =  objCauHinh.getVanthu_capnhat()==null?true:objCauHinh.getVanthu_capnhat();
			}			
			
			quyenSuaVanBan = (userId==Long.parseLong(props.getObjXemVanBan().get("userid").toString()))
					||(quyenVanThu && cauHinhDoMat);
			
			//Kiểm tra độ mật để chỉnh sửa thông tin văn bản		
			if(quyenSuaVanBan){
				//if(objVBDenFacade.isExists(vbden_id)){			
					
					long vbOrgan = props.getObjVBDenModel().getOrganizationid();
					//Set lại các danh mục theo 
					propsLoaiVb.setOrganizationid(vbOrgan);
					propsSoVanBan.setOrganizationid(vbOrgan);
					propsLinhVuc.setOrganizationid(vbOrgan);
					propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(companyId, vbOrgan));
					//propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade.getDsSoVanBanTheoDonVi(companyId, vbOrgan));
					propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade.getDsSoVbTheoNvb(1, companyId, vbOrgan,getListDvUser()));
					props.setListDoMat(objDmDoMatFacade.getDsDoMat(companyId, vbOrgan));
					props.setListDoKhan(objDmDoKhanFacade.getDsDoKhan(companyId, vbOrgan));
					props.setListCachThucXuLy(objDmCachThucXuLyFacade.getDsCachThucXuLy(companyId, vbOrgan));
					
					getListDonVi().clear();
					if( isCallFromDanhMuc() == false ){
						getListDonVi().add(objUserUtil.getOrganization(vbOrgan)); 
					} else {
						setListDonVi(objUserUtil.getDsDonViTheoNguoiDung(getUserId()));
					}
					
					List<Long> dsOrgId = new ArrayList<Long>();
					if( getListDonVi() != null ){
						for( Organization o : getListDonVi() ){
							dsOrgId.add(o.getOrganizationId());
						}
					}
					propsLinhVuc.setDsLinhVuc(objDmLinhVucFacade.getDsLinhVuc("{}", companyId, dsOrgId));

					//props.setListLinhVuc(listLinhVuc);
					
					
					props.setSuaSoDen(false);					
					soDenOld = props.getObjVBDenModel().getSoKeTiep();
					props.setListTapTinVanBanSua(objTaptinDinhKemVanBanFacade.getDsTapTinTheoVanBan(props.getObjVBDenModel().getVbden_id(), 1));
					List<Map<String,Object>> dsLinhVucVb = objDmLinhVucFacade.getDsLinhVucTheoDsId(props.getObjVBDenModel().getVbden_linhvuc());
					propsLinhVuc.setDsLinhVucVb(dsLinhVucVb);
					propsDonVi.dvId = Integer.parseInt(props.getObjVBDenModel().getVbden_coquanbanhanhid().toString());
					propsDonVi.tenDonVi = props.getObjVBDenModel().getVbden_coquanbanhanh();
					for(DmLinhVucModel lv:propsLinhVuc.getDsLinhVuc()){
						for(Map map:dsLinhVucVb){
							if(lv.getLv_id()==Integer.parseInt(map.get("lv_id").toString())){
								lv.setChecked(true);
								break;
							}else{
								lv.setChecked(false);
							}
						}
					}
					propsLinhVuc.actionChonLinhVuc();
					if(props.getListTapTinVanBanSua()!=null){
						for(TapTinDinhKemVanBanModel ttdk:props.getListTapTinVanBanSua()){
							boolean daDuocTiepNhan = false;
							if(ttdk.getTtdk_vbden_id_nhan()==null 
									|| ttdk.getTtdk_vbden_id_nhan().equals("") 
									|| ttdk.getTtdk_vbden_id_nhan().equals("{}")){
								daDuocTiepNhan = false;
							}else{
								daDuocTiepNhan = objVBDen_XuLyFacade.daTiepNhanTuChuyenDonVi(ttdk.getTtdk_vbden_id_nhan());
							}
							
							File f=new File(props.getBasePath() + getPathUpload(ttdk.getTtdk_nhomvb()) + ttdk.getTtdk_tenluutru());
							if(f.exists()){
								HCDTResource resource=new HCDTResource(f);
								ttdk.setDownload(resource);
								ttdk.setPath(props.getBasePath() + getPathUpload(ttdk.getTtdk_nhomvb()) + ttdk.getTtdk_tenluutru());
								ttdk.setType(CommonUtils.getFileExtension(f));
								if(ttdk.getTtdk_nhomvb() != IContanst.NVB_DEN 
										|| ttdk.getTtdk_vb_id() != props.getObjVBDenModel().getVbden_id()
										|| daDuocTiepNhan){//Do dc tiếp nhận từ cùng hệ thống hoặc nhận từ chức năng phổ biến đến các đơn vị
									ttdk.setDuocxoa(false);
								}
							}
						}				
					}
					props.setListThemSua(new ArrayList<TapTinDinhKemVanBanModel>());
					props.setListXoaSua(new ArrayList<TapTinDinhKemVanBanModel>());
					soDenTmp=props.getObjVBDenModel().getVbden_soden();
					
					//
					propsDonVi.setTenDonVi(props.getObjVBDenModel().getVbden_coquanbanhanh());
					propsDonVi.setDvId(props.getObjVBDenModel().getVbden_coquanbanhanhid());
					propsDonVi.setTmpLoaiDonVi(1);
					log.info(" >> Ten don vị: "+propsDonVi.getTenDonVi());
					log.info(" >> Id don vị: "+propsDonVi.getDvId());
					
					int svbId = props.getObjVBDenModel().getSvb_id();
					props.getObjVBDenModel().setViTri(objDmSoVanBanFacade.getViTriStt(svbId));
					
					//@ptgiang Load link xem chi tiết
					props.getObjVBDenModel().setVbden_phucdap( pmGetIdPhVbDiBySoHieuGoc(props.getObjVBDenModel().getVbden_phucdap()) );
					
					String butphe = props.getObjVBDenModel().getVbden_butphe();
					butphe = butphe == null ? "" : butphe;
					props.setButPheVanBan(butphe);
					
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogSuaVanBanDen.show()");
					
			}else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Bạn không được phép chỉnh sửa văn bản này',4);");
			}
		}	
		
		else{			
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản không tồn tại',4);");
			taoDuLieuThongKe(null);			
			props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
		}
		}catch(Exception ex){
			log.error("Err preDen", ex);
		}
	}
	
	/**
	 * @author  lqthai
	 * @purpose Pre Chọn đơn vi phát hành
	 * @date    May 24, 2014 :: 11:40:29 AM
	 */
	public void preActionChonDonViPhatHanh(AjaxBehaviorEvent ae){
		propsDonVi.modeChonDv = Integer.parseInt(ae.getComponent().getAttributes().get("modeId").toString());
		if(propsDonVi.modeChonDv==1){ //Khi chọn dơn vi cho thêm mới vb
			for(DmDonViModel dvTmp : propsDonVi.dsDonVi){
				boolean test = false;
				if(dvTmp.getDv_id()== propsDonVi.dvId && !test){
					dvTmp.setChecked(true);
					test = true;
				}else{
					dvTmp.setChecked(false);
				}
			}
		}if(propsDonVi.modeChonDv==2){ //chon don vi lọc
			for(DmDonViModel dvTmp : propsDonVi.dsDonVi){				
				dvTmp.setChecked(false);
				String [] tmp = propsDonVi.getDsTenDonVi().toLowerCase().split(",");
				for(int i=0;i<tmp.length;i++){
					if(dvTmp.getDv_ten().toLowerCase().equals(tmp[i])){
						dvTmp.setChecked(true);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @author  ltbao
	 * @purpose Xóa tập tin đính kèm khi sửa văn bản 
	 * @date    Jun 11, 2014 :: 7:48:47 PM 
	 * @param   evt
	 */
	public void actionXoaFileDinhKemSua(AjaxBehaviorEvent evt){
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			props.getListTapTinVanBanSua().remove(obj);
			props.getListThemSua().remove(obj);
			TapTinDinhKemVanBanModel xoa=(TapTinDinhKemVanBanModel)obj;
			if(xoa.getTtdk_id()!=null){
				props.getListXoaSua().add((TapTinDinhKemVanBanModel)obj);
			}
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Sự kiện lăng nghe sự thay đỗi tab trên chức năng xử lý
	 * @date    May 23, 2014 :: 3:35:01 PM 
	 * @param evt
	 */
	public void actionTabChange(TabChangeEvent evt){
		props.setTabIndex(evt.getNewTabIndex());
	}	
		
	/**
	 * @author  ltbao
	 * @purpose Sự kiện khi chọn chuyển xử lý 
	 * @date    May 24, 2014 :: 3:29:25 PM 
	 * @param evt
	 */
	public void actionChonChuyenXuLy(AjaxBehaviorEvent evt){		
		log.info(">>actionChonChuyenXuLy");
		if(props.isHoanThanhXuLyToanVanBan()){
			//Cảnh bảo
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('"+CommonUtils.getBundleValue("vCanhBao")+"',2)");
		}
		if(props.getObjVBDen_LuanChuyenModel() != null
				&& props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe() != null
				&& props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe().trim().isEmpty()){
			props.getObjVBDen_LuanChuyenModel().setVbden_lc_butphe(null);
		}
			
		butPheHientai = props.getObjVBDen_LuanChuyenModel().getVbden_lc_butphe();
		props.setHoanThanhXuLyToanVanBan(false); 
		if(coQuyenLanhDao){//có quyền bút phê = có quyền lãnh đạo
			if(props.getListChuyenXuLy() == null){ //fix lỗi chọn nhiều văn bản danh sách xử lý thêm dồn người xử lý từ các văn bản khác
				props.setListChuyenXuLy(new ArrayList<Map<String,Object>>());
			}
			VBDen_LuanChuyenModel luanChuyenTruoc = objVBDen_LuanChuyenFacade.getObjLuanChuyen(props.getObjVBDen_LuanChuyenModel().getVbden_lc_cha());
			if(luanChuyenTruoc != null){			
				long uid = luanChuyenTruoc.getVbden_lc_xlchinh_userid();
				long dvId = luanChuyenTruoc.getVbden_lc_xlchinh_dvid();
				for(Map<String, Object> u : props.getListChuyenXuLy()){
					if(String.valueOf(u.get("userid")).equals(String.valueOf(uid)) && 
							//nduong
							String.valueOf(u.get("donviid")).equals(String.valueOf(dvId))){
						//u.put("checked", true);
						u.put("chon", true);
						u.put("xlchinh", true);
						break;
					}
				}
			}
		}	
	}
	
	/**
	 * @author  ltbao
	 * @purpose  Sự kiện khi chọn hoàn thành xử lý toàn văn bản
	 * @date    May 24, 2014 :: 3:31:49 PM 
	 * @param evt
	 */
	public void actionChonHoanThanhXuLyToanVanBan(AjaxBehaviorEvent evt){
		props.setChuyenXuLy(false);		
	}

	/**
	 * @author  ltbao
	 * @purpose Xóa người nhận xử lý ở bước luân chuyển tiếp theo
	 * @date    May 23, 2014 :: 6:14:40 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionXoaNguoiNhanXuLy(AjaxBehaviorEvent evt){
		Gson gson = new Gson();
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			for(@SuppressWarnings("rawtypes") Map map:props.getListChuyenXuLy()){
				if(map.get("userid").toString().equals(((Map<String,Object>)obj).get("userid").toString()) && 
				   map.get("donviid").toString().equals(((Map<String, Object>)obj).get("donviid").toString())){
					map.put("chon",false);
					break;
				}
			}
			props.setTxtDsEmailNhanXuLy(getListNguoiNhanXuLy());
		}
		//ttdat
		//kiểm tra xem người dùng đã chọn người xử lỹ chính hay chưa
		boolean chonNguoiXLChinh = false;
		for (@SuppressWarnings("rawtypes") Map map:props.getListChuyenXuLy()){
			if (map.get("xlchinh") != null && Boolean.parseBoolean(map.get("xlchinh").toString()) && Boolean.parseBoolean(map.get("chon").toString())) {
				chonNguoiXLChinh = Boolean.parseBoolean(map.get("xlchinh").toString());
				break;
			}
		}
		//nếu chưa chọn người xử lý chính thì mặc định người đầu tiên sẽ được chọn
		if (!chonNguoiXLChinh) {
			for (@SuppressWarnings("rawtypes") Map map:props.getListChuyenXuLy()){
				if (Boolean.parseBoolean(map.get("chon").toString())) {
					map.put("xlchinh", true);
					break;
				}
			}
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Sự kiện khi chọn xử lý chính
	 * @date    Jul 2, 2014 :: 8:47:35 AM 
	 * @param evt
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void actionChonXlChinh(AjaxBehaviorEvent evt){
		Object obj =evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			Map<String,Object> tmpMap =(Map<String,Object>)obj;
			for(Map map:props.getListChuyenXuLy()){
				map.put("xlchinh", false);
			}
			tmpMap.put("xlchinh",true);
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xóa tập tin xử lý văn bản
	 * @date    May 24, 2014 :: 11:39:02 AM 
	 * @param evt
	 */
	public void actionXoaTapTinLuanChuyen(AjaxBehaviorEvent evt){
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			TapTinDinhKemLuanChuyenModel model=(TapTinDinhKemLuanChuyenModel)obj;
			if(model.getLctt_id()!=null){
				props.getListXoaTapTinLuanChuyen().add(model);
			}
			props.getListTapTinLuanChuyen().remove(obj);
			props.getListThemTapTinLuanChuyen().remove(obj);
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xóa tập tin góp ý
	 * @date    Jun 9, 2014 :: 10:40:53 AM 
	 * @param evt
	 */
	public void actionXoaTapTinGopY(AjaxBehaviorEvent evt){
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			props.getListTapTinGopY().remove(obj);
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xóa người được phổ biến theo bước luân chuyển
	 * @date    May 24, 2014 :: 1:24:46 PM 
	 * @param   evt
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void actionXoaNguoiNhanPhoBien(AjaxBehaviorEvent evt){
		log.info("actionXoaNguoiNhanPhoBien");
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			for(Map map:props.getListPhoBien()){
				if(map.get("userid").toString().compareTo(((Map<String,Object>)obj).get("userid").toString())==0){
					map.put("chon",false);
					log.info("txtDsEmailNhanXuLy {}",props.getTxtDsEmailNhanXuLy());
					if(props.getTxtDsEmailNhanXuLy() != null 
							&& map.get("email") != null){
						props.setTxtDsEmailNhanXuLy(props.getTxtDsEmailNhanXuLy().replace(" ", ""));
						props.setTxtDsEmailNhanXuLy(props.getTxtDsEmailNhanXuLy().replace(String.valueOf(map.get("email")), ""));
						props.setTxtDsEmailNhanXuLy(props.getTxtDsEmailNhanXuLy().replace(",,", ","));
						if(props.getTxtDsEmailNhanXuLy().startsWith(",")){
							props.setTxtDsEmailNhanXuLy(props.getTxtDsEmailNhanXuLy().substring(1));
						}
						if(props.getTxtDsEmailNhanXuLy().endsWith(",")){
							props.setTxtDsEmailNhanXuLy(props.getTxtDsEmailNhanXuLy().substring(0, props.getTxtDsEmailNhanXuLy().length()-1));
						}
					}
					break;
				}
			}
			log.info(">> Xóa thành công");
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xem chi tiết thông tin bước luân chuyển
	 * @date    Jun 10, 2014 :: 9:26:18 AM 
	 * @param   evt
	 */
	@SuppressWarnings({ "unchecked" })
	public void actionXemChiTietLuanChuyen(AjaxBehaviorEvent evt){
		try{
			Object obj=evt.getComponent().getAttributes().get("obj");
			if(obj != null){
				props.setObjXemChiTietBuocLuanChuyen((Map<String,Object>)obj);
				int index = props.getListLuanChuyen().indexOf(obj);				
				long lcId = 0;
				
				if(index < (props.getListLuanChuyen().size()-1) ){
					Map<String,Object> objNextLc = props.getListLuanChuyen().get(index+1);
					
					lcId = Long.parseLong(objNextLc.get("vbden_lc_id").toString());
					
					props.getObjXemChiTietBuocLuanChuyen().put("xulytieptheo", 
							objNextLc.get("nguoixuly")==null ? "" : objNextLc.get("nguoixuly").toString());
				}
				 
				//Lấy danh sách user phối hợp xử lý
				List<Map<String,Object>> listPhoiHop = new ArrayList<Map<String,Object>>();
								
				VBDen_LuanChuyenModel lcModel = objVBDen_LuanChuyenFacade.getObjLuanChuyen(lcId);		
				if(lcModel != null){
					Map<String, String> mapCurrentUser = new HashMap<String, String>();	
					ChuyenBoSungModel chuyenBoSungModel = (ChuyenBoSungModel)CollectionUtil.convertJsonToObj(lcModel.getVbden_lc_xlbosung(), new TypeToken<ChuyenBoSungModel>(){}.getType());
					if( chuyenBoSungModel != null && !CollectionUtil.isNullOrEmpty(chuyenBoSungModel.getCurrent()) ){
						mapCurrentUser = chuyenBoSungModel.getCurrent();
					}
					
					String ngayChuyenBanDau = DateUtils.formatDate2String(
							(Date)props.getObjXemChiTietBuocLuanChuyen().get("vbden_lc_cn_gannhat"), DateUtils.PATTERN_DATE_TIME);
					
					String userDvXlChinh = String.format(IContanst.PATTERN_USERID_DVID, 
							lcModel.getVbden_lc_xlchinh_userid(), lcModel.getVbden_lc_xlchinh_dvid()) ;
					
					List<String> dsUserIdDvId_DaXem = (List<String>)CollectionUtil.convertJsonStringToListString(lcModel.getVbden_xl_daxem());
					if( dsUserIdDvId_DaXem == null ){
						dsUserIdDvId_DaXem = new ArrayList<String>();
					}
					
					List<String> dsUserIdDvId = (List<String>)CollectionUtil.convertJsonStringToListString(lcModel.getVbden_lc_user_donvi());
					if( !CollectionUtil.isNullOrEmpty(dsUserIdDvId) ){
						for( String userIdDvId : dsUserIdDvId ){
							if( userIdDvId.equals(userDvXlChinh) ){
								continue;
							}
										
							try {	
								long userId = Long.parseLong( userIdDvId.split("_")[0] );	
								long dvId = Long.parseLong( userIdDvId.split("_")[1] );
								User u = UserLocalServiceUtil.getUser(userId);

								Map<String, Object> mapUerPhoiHop = new HashMap<String, Object>();
								mapUerPhoiHop.put("userid", userId); 
								mapUerPhoiHop.put("emailaddress", u.getEmailAddress());
								mapUerPhoiHop.put("email", u.getEmailAddress());
								mapUerPhoiHop.put("jobtitle", u.getJobTitle());
								mapUerPhoiHop.put("ngaychuyen", mapCurrentUser.get(userIdDvId)); 
								mapUerPhoiHop.put("chucvu", u.getJobTitle());
								mapUerPhoiHop.put("screenname", u.getScreenName()); 
								mapUerPhoiHop.put("fullname", UserUtil.getUserFullName(u));
								mapUerPhoiHop.put("phongban", objUserUtil.getStringPhongBanNguoiDung(userId, dvId));
								mapUerPhoiHop.put("donvi", objUserUtil.getOrganization(dvId).getName());
								
								if( mapCurrentUser.get(userIdDvId) != null && mapCurrentUser.get(userIdDvId).compareTo(ngayChuyenBanDau) != 0){
									mapUerPhoiHop.put("isChuyenXuLyBoSung", true);
								} else{
									mapUerPhoiHop.put("isChuyenXuLyBoSung", false);
								}
								
								if(dsUserIdDvId_DaXem.contains(userIdDvId)){
									mapUerPhoiHop.put("vbden_xl_daxem", true);
								} else{
									mapUerPhoiHop.put("vbden_xl_daxem", false);
								}
								
								listPhoiHop.add(mapUerPhoiHop);
							} catch (PortalException e) {
								log.error("Err ", e);
							} catch (SystemException e) {
								log.error("Err ", e);
							}
						}
					}

					Collections.sort(listPhoiHop, new MapComparator("fullname"));
				}
				
				List<Map<String,Object>> listPhoBienCaNhan = objVBDen_LuanChuyenFacade.getListNguoiPhoBienTheoBuocLuanChuyen(Long.parseLong(((Map<String,Object>)obj).get("vbden_lc_id").toString()), getOrganizationId());
				
				props.getObjXemChiTietBuocLuanChuyen().put("listPhoiHop", listPhoiHop);
				props.getObjXemChiTietBuocLuanChuyen().put("listPhoBienCaNhan", listPhoBienCaNhan);
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChiTietBuocXuLy.show()");
			}
		} catch( Exception ex ){
			log.error("Err actionXemChiTietLuanChuyen", ex);
		}
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Xử lý sự kiện chọn chuyển/ hủy xử lý bổ sung
	 * @date    Oct 12, 2016 :: 2:34:24 PM 
	 * @param evt
	 */
	public void preActionChuyenXuLyBoSung(AjaxBehaviorEvent evt){
		log.info(">>preActionChuyenXuLyBoSung");
		try{
			Object obj=evt.getComponent().getAttributes().get("obj");
			props.setGuiMailChuyenXuLyBoSung(false);
			props.setTxtDsEmailNhanXuLyBoSung(""); 
			
			if(obj != null){
				
				List<Map<String, Object>> listChuyenXuLyBoSung = new ArrayList<Map<String,Object>>();;
				
				int index = props.getListLuanChuyen().indexOf(obj);					
				
				//Lấy danh sách phối hợp xử lý
				if(index < (props.getListLuanChuyen().size()-1) ){	
					Map<String,Object> objNextLc = props.getListLuanChuyen().get(index+1);					
					lcIdXuLyBoSungTmp = Long.parseLong(objNextLc.get("vbden_lc_id").toString());		
					long userIdNext = Long.parseLong(objNextLc.get("vbden_lc_xlchinh_userid").toString());
					List<Map<String,Object>> listPhoiHop = objVBDen_XuLyFacade.getDsPhoiHopXuLy(lcIdXuLyBoSungTmp);		
					
					//log.info("listPhoiHop {}", listPhoiHop);
					//log.info("getListChuyenXuLy {}", props.getListChuyenXuLy());
					
					pmGetDsChuyenXuLy();
					if(props.getListChuyenXuLy() != null && !props.getListChuyenXuLy().isEmpty()){						
						for( Map<String,Object> mapUser : props.getListChuyenXuLy() ){						
							if( Long.parseLong(mapUser.get("userid").toString()) == userId ||
									Long.parseLong(mapUser.get("userid").toString()) == userIdNext){ 
								continue;
							}
							
							Map<String,Object> mapUserChuyenBoSung = new HashMap<String, Object>();	
							mapUserChuyenBoSung.putAll(mapUser);
							mapUserChuyenBoSung.put("isDeleted", false);
							mapUserChuyenBoSung.put("isChoiceBefore", false);
							mapUserChuyenBoSung.put("chon", false);
							mapUserChuyenBoSung.put("daXuLy", false);
							
							long userid = Long.valueOf(mapUser.get("userid").toString());
							long dvid = Long.valueOf(mapUser.get("donviid").toString());
							if(listPhoiHop != null && !listPhoiHop.isEmpty()){
								for( Map<String,Object> mapUserPhoiHop : listPhoiHop ){
									if( userid == Long.parseLong(mapUserPhoiHop.get("userid").toString()) ){
										mapUserChuyenBoSung.put("isChoiceBefore", true);
										mapUserChuyenBoSung.put("chon", true);
										mapUserChuyenBoSung.put("daXuLy", objThongTinGopYFacade.coGopY(
												String.format(IContanst.PATTERN_USERID_DVID, userid, dvid), 
												lcIdXuLyBoSungTmp, IContanst.NVB_NOIBO));
										break;
									}
								}
							}
							listChuyenXuLyBoSung.add(mapUserChuyenBoSung);
						}
					}
				}
				
				props.setListChuyenXuLyBoSung(listChuyenXuLyBoSung); 
				
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChuyenXuLyBoSung.show();");
			}
		} catch( Exception ex ){
			log.error("Err preActionChuyenXuLyBoSung", ex);
		}
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Xử lý sự kiện chọn Đồng ý chuyển/ hủy xử lý bổ sung
	 * @date    Oct 12, 2016 :: 2:34:24 PM 
	 * @param evt
	 */
	public void actionChuyenXuLyBoSung(){
		log.info("actionChuyenXuLyBoSung {}", props.getListChuyenXuLyBoSung());		
		try{
			if( props.getListChuyenXuLyBoSung() == null || props.getListChuyenXuLyBoSung().isEmpty() ){
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChuyenXuLyBoSung.hide();");
				return;
			}
			
			boolean isPass = true;
			
			VBDenModel vbDenModel = objVBDenFacade.getObjVanBanDen(Long.parseLong(props.getObjXemVanBan().get("vbden_id").toString()));
			
			if(vbDenModel == null || vbDenModel.getVbden_id() == null || vbDenModel.getVbden_id().longValue() == 0){
				isPass = false;
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản không tồn tại, không thể chuyển xử lý',4);");
			} else if(vbDenModel.getVbden_hoanthanh() != null && vbDenModel.getVbden_hoanthanh().booleanValue() == true){
				isPass = false;
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản đã hoàn thành, không thể chuyển xử lý',4);");
			}
			
			if(isPass){			
				VBDen_LuanChuyenModel lcModel = objVBDen_LuanChuyenFacade.getObjLuanChuyen(lcIdXuLyBoSungTmp);
				List<String> dsUserIdDvId = (List<String>)CollectionUtil.convertJsonStringToListString(lcModel.getVbden_lc_user_donvi());
				
				List<Long> dsUserId_XlPhoiHop = new ArrayList<Long>();
				List<String> dsUserIdDvId_Deleted = new ArrayList<String>();
				List<String> dsUserIdDvId_Added = new ArrayList<String>();
				List<Long> dsUserId_Deleted = new ArrayList<Long>();
				List<Long> dsUserId_Added = new ArrayList<Long>();
				List<Long> dsOrgId_Added = new ArrayList<Long>();
				List<Long> dsOrgId_Deleted = new ArrayList<Long>();
				
				for( Map<String,Object> mapUser : props.getListChuyenXuLyBoSung() ){
					long userid = Long.valueOf(mapUser.get("userid").toString());
					//nduong
					long dvid = Long.valueOf(mapUser.get("donviid").toString());
					String userIdDvId = String.format(IContanst.PATTERN_USERID_DVID, userid, dvid);
					
					boolean isDeleted = Boolean.parseBoolean(mapUser.get("isDeleted").toString());	
					boolean isChon = Boolean.parseBoolean(mapUser.get("chon").toString());		
					boolean isChoiceBefore = Boolean.parseBoolean(mapUser.get("isChoiceBefore").toString());
					
					if( isChon || (isChoiceBefore && isDeleted == false) ){
						dsUserId_XlPhoiHop.add( Long.parseLong(mapUser.get("userid").toString()) );
					}			
					
					if( isDeleted ){
						dsUserId_Deleted.add(userid);
						dsUserIdDvId_Deleted.add( userIdDvId );
						dsUserIdDvId.remove(String.format(IContanst.PATTERN_USERID_DVID, userid, dvid));
						dsOrgId_Deleted.add(dvid);
					}
					
					if( isChon ){
						dsUserId_Added.add(userid);
						dsUserIdDvId_Added.add( userIdDvId );
						dsUserIdDvId.add(String.format(IContanst.PATTERN_USERID_DVID, userid, dvid));
						dsOrgId_Added.add(dvid);
					}
				}
				
				if( !dsUserIdDvId_Deleted.isEmpty() && objThongTinGopYFacade.coGopY(dsUserIdDvId_Deleted, lcIdXuLyBoSungTmp, IContanst.NVB_DEN) ){
					//log.info("Ds user da xu ly dsUserId_Deleted {}", dsUserId_Deleted);
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Một hoặc nhiều người xử lý được chọn đã xử lý văn bản, không thể xóa',4);");
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChuyenXuLyBoSung.hide();");
					props.setListChuyenXuLyBoSung(null); 	
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad, cPageSize, true, null));	
					phanQuyen();				
					return;
				}
				
				if (!isAllowChuyenBoSung(vbDenModel.getVbden_id(), lcIdXuLyBoSungTmp)) {
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Tiến trình xử lý đã được thay đổi, vui lòng thử lại',4);");
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChuyenXuLyBoSung.hide();");
					props.setListChuyenXuLyBoSung(null); 	
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad, cPageSize, true, null));	
					phanQuyen();				
					return;
				}
								
				/*log.info("dsUserId_XlPhoiHop {}", dsUserId_XlPhoiHop);
				log.info("dsUserId_Deleted {}", dsUserId_Deleted);
				log.info("dsUserId_Added {}", dsUserId_Added);*/
				
				if( !dsUserIdDvId_Deleted.isEmpty() || !dsUserIdDvId_Added.isEmpty() ){
					ChuyenBoSungModel chuyenBoSungModel = (ChuyenBoSungModel)CollectionUtil.convertJsonToObj(lcModel.getVbden_lc_xlbosung(), new TypeToken<ChuyenBoSungModel>(){}.getType());					
					if( chuyenBoSungModel ==null ){
						chuyenBoSungModel = new ChuyenBoSungModel();
					}
					//log.info("chuyenBoSungModel before {}", chuyenBoSungModel);

					String curDate = DateUtils.formatDate2String(DateUtils.getCurrentDate(), "dd/MM/yyyy HH:mm:ss");
					Map<String, List<String>> mapHistory = new HashMap<String, List<String>>();
					mapHistory.put("add", dsUserIdDvId_Added);
					mapHistory.put("del", dsUserIdDvId_Deleted); 					
					chuyenBoSungModel.getHistory().put(curDate, mapHistory);
					
					if( !dsUserIdDvId_Deleted.isEmpty() && !CollectionUtil.isNullOrEmpty(chuyenBoSungModel.getCurrent()) ){
						for( String userIdDvId :  dsUserIdDvId_Deleted){
							chuyenBoSungModel.getCurrent().remove(userIdDvId);
						}
					}
					
					if( !dsUserIdDvId_Added.isEmpty() ){
						for( String userIdDvId :  dsUserIdDvId_Added){
							chuyenBoSungModel.getCurrent().put(userIdDvId, curDate);
						}
					}
					
					//log.info("chuyenBoSungModel after {}", chuyenBoSungModel);	
					
					String dsXlPhoiHop = lcModel.getVbden_lc_xlphoihop_dsuserid();
					//log.info(">>>dsXlPhoiHop: "+dsXlPhoiHop);
					//log.info(">>>userID: "+getUserId());
					if(dsXlPhoiHop.contains(""+getUserId())  && !dsUserId_XlPhoiHop.contains(getUserId())){
						dsUserId_XlPhoiHop.add(getUserId());
						//log.info(">>>dsUserId_XlPhoiHop: "+dsUserId_XlPhoiHop);
					}
					
					lcModel.setVbden_lc_xlphoihop_dsuserid(CollectionUtil.convertToJsonString(new HashSet<Long>(dsUserId_XlPhoiHop)));   
					lcModel.setVbden_lc_xlbosung(CollectionUtil.convertObjToJson(chuyenBoSungModel).toString());
					lcModel.setVbden_lc_user_donvi(CollectionUtil.convertToJsonString(new HashSet<String>(dsUserIdDvId)));
					if( props.isGuiMailChuyenXuLyBoSung() && 
							props.getTxtDsEmailNhanXuLyBoSung() != null && !props.getTxtDsEmailNhanXuLyBoSung().isEmpty() ){
						lcModel.setVbden_lc_mail(true); 
						if( lcModel.getVbden_lc_mail_dsnhan() == null || lcModel.getVbden_lc_mail_dsnhan().isEmpty() ){
							lcModel.setVbden_lc_mail_dsnhan(props.getTxtDsEmailNhanXuLyBoSung().trim());
						} else{
							lcModel.setVbden_lc_mail_dsnhan(lcModel.getVbden_lc_mail_dsnhan() + "," + props.getTxtDsEmailNhanXuLyBoSung().trim());
						}
					}
					objVBDen_LuanChuyenFacade.capNhatBuocLuanChuyen(lcModel); 			
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Chuyển/ Hủy xử lý bổ sung thành công',3);");	
					
					//Gửi thông  báo đến người phối hợp
					String noidungThongBao =(props.getObjVanBanDen().get("vbden_trichyeu").toString().length()>100
							? (props.getObjVanBanDen().get("vbden_trichyeu").toString().substring(0, 100)+"...")
							: props.getObjVanBanDen().get("vbden_trichyeu").toString());
					noidungThongBao = HtmlUtil.escape(noidungThongBao);
					
					if( !CollectionUtil.isNullOrEmpty(dsUserId_Added) ){
						//Thêm
						String dsOrgId =  !CollectionUtil.isNullOrEmpty(dsOrgId_Added) ? CollectionUtil.convertToJsonString(dsOrgId_Added).replace(" ", "").replace("{", "").replace("}", "") : null;
						log.info("org Notification "+ getOrgNotification());
						objNotificationClient.sendNotification(objUserUtil.getUserId()+"", 
								CollectionUtil.convertToJsonString(dsUserId_Added).replace(" ", "").replace("{", "").replace("}", ""), 
								dsOrgId, 
								"Xử lý văn bản đến", 
								NotificationsVar.SUBTITLE_XULY, objDmLinkFacade.getLinkModelByMa(Hcdt.XU_LY_VAN_BAN_DEN).getLink_id(), 
								"?instant="+props.getObjVanBanDen().get("vbden_id").toString(),
								noidungThongBao, 
								CommonUtils.getBundleValue("vIconPhoiHop").toString(), NotificationsVar.NHAN_CHUYEN_XU_LY_PHOI_HOP,
								getOrgNotification());
					}
					
					if( !CollectionUtil.isNullOrEmpty(dsUserId_Deleted) ){						
						//Thu hồi
						String dsOrgId =  !CollectionUtil.isNullOrEmpty(dsOrgId_Deleted) ? CollectionUtil.convertToJsonString(dsOrgId_Deleted).replace(" ", "").replace("{", "").replace("}", "") : null;
						log.info("org Notification "+ getOrgNotification());
						objNotificationClient.sendNotification(objUserUtil.getUserId()+"", 
								CollectionUtil.convertToJsonString(dsUserId_Deleted).replace(" ", "").replace("{", "").replace("}", ""), 
								dsOrgId,
								"Thu hồi xử lý văn bản đến", "", 
								objDmLinkFacade.getLinkModelByMa("NONE").getLink_id(), "", 
								noidungThongBao, 
								CommonUtils.getBundleValue("vIconPhoiHop").toString(), NotificationsVar.NHAN_THU_HOI_CHUYEN_XU_LY,
								getOrgNotification());						
					}
					
					//Gửi mail chuyển xử lý bổ sung
					if( props.isGuiMailChuyenXuLyBoSung() && 
							props.getTxtDsEmailNhanXuLyBoSung() != null && !props.getTxtDsEmailNhanXuLyBoSung().isEmpty() ){
						sendMailXuLy(vbDenModel.getVbden_id(), props.getTxtDsEmailNhanXuLyBoSung().split(","), 
								"", props.getListChuyenXuLyBoSung());  
					}
					
					taoCayLuanChuyen();		
				}
			} else{
				props.setListVanBanDenLazy(getLazyDataModel(pageLoad, cPageSize, true, null));	
				phanQuyen();				
			}
		} catch( Exception ex ){
			log.error("Err actionChuyenXuLyBoSung", ex);
		}
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogChuyenXuLyBoSung.hide();");
		props.setListChuyenXuLyBoSung(null); 
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Kiểm tra lcIdChuyenBoSung của vbden_id có còn được quyền chuyển bổ sung hay không
	 * 			Chỉ được chuyển bổ sung tại bước cuối cùng 
	 * @date    Oct 20, 2016 :: 11:05:34 AM 
	 * @param vbden_id
	 * @param lcIdChuyenBoSung
	 * @return
	 */
	private boolean isAllowChuyenBoSung(long vbden_id, long lcIdChuyenBoSung){
		List<Map<String,Object>> dsLuanChuyen = objVBDen_LuanChuyenFacade.getListLuanChuyenTheoVanBanDen(vbden_id);
		
		//Nếu có bước luân chuyển
		if( dsLuanChuyen != null && !dsLuanChuyen.isEmpty() ){
			//Khởi tạo danh sách tập tin luân chuyển
			int lastIndexAllowChuyenBoSung = -1;
			int idx = 0;
			int soBuocLuanChuyen = dsLuanChuyen.size();
			for(Map<String, Object>  map : dsLuanChuyen){
				/*30.07.2014 Thêm thông tin chức năng thu hồi*/
				boolean thuhoi = false;
				
				//Chỉ thu hồi các bước không phải là bước tiếp nhận
				if(Integer.parseInt(map.get("vbden_lc_lbxl").toString())==0){
					//Cho phép thu hồi khi bước xử lý do mình chuyển và vẫn chưa thao tác và vẩn chưa từng bị thu hồi
					if((objVBDen_LuanChuyenFacade
							.getObjLuanChuyen(Long.parseLong(map.get("vbden_lc_cha").toString()))
							.getVbden_lc_xlchinh_userid()==objUserUtil.getUserId())
							&&!Boolean.parseBoolean(map.get("vbden_lc_thaotac").toString())
							&&!Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())){
						thuhoi = true;
					}
					if(xuLyThay){
						if(!Boolean.parseBoolean(map.get("vbden_lc_thaotac").toString())
								&&!Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())){
							thuhoi = true;
						}
					}
				}
				
				long useridtmp = Long.parseLong(map.get("vbden_lc_xlchinh_userid").toString());
				
				//Cho phép chuyển xử lý bổ sung				
				if( thuhoi == false && useridtmp == userId && idx < soBuocLuanChuyen - 1 ){
					lastIndexAllowChuyenBoSung = idx;
				}
				
				idx++;
			}		
			if( lastIndexAllowChuyenBoSung >= 0 && lastIndexAllowChuyenBoSung < soBuocLuanChuyen - 1){
				long lcId = Long.parseLong(dsLuanChuyen.get(lastIndexAllowChuyenBoSung + 1).get("vbden_lc_id").toString());
				if( lcId == lcIdChuyenBoSung ){
					return true;
				}
			}
		}	
		return false;
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Xử lý sự kiện chọn xóa người xử lý bổ sung
	 * @date    Oct 12, 2016 :: 2:47:03 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionXoaNguoiXuLyBoSung(AjaxBehaviorEvent evt){
		try{
			Object obj=evt.getComponent().getAttributes().get("obj");
			//log.info("actionXoaNguoiXuLyBoSung {}", obj);
			if(obj != null){
				Map<String, Object> objUser = (Map<String,Object>)obj;				
				boolean isDeleted = Boolean.parseBoolean(objUser.get("isDeleted").toString());				
				objUser.put("isDeleted", !isDeleted);	
				objUser.put("chon", isDeleted);
			}
		} catch( Exception ex ){
			log.error("Err actionXoaNguoiXuLyBoSung", ex);
		}
	}	
	
	/**
	 * @author  ptgiang
	 * @purpose Load danh sách email nhận xử lý bổ sung
	 * @date    Dec 27, 2016 :: 4:30:11 PM
	 */
	public void actionLoadDsEmailXuLyBoSung(){
		props.setTxtDsEmailNhanXuLyBoSung(pmGetDsEmailNhanXuLy(props.getListChuyenXuLyBoSung())); 
	}
	
	private String pmGetDsEmailNhanXuLy(List<Map<String, Object>> dsUser){
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for(Map<String, Object> map : dsUser){
			if(Boolean.parseBoolean(map.get("chon").toString())){
				if( isFirst == false ){
					sb.append(",");
				}
				sb.append(map.get("email"));
				isFirst = false;
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * @author  ltbao
	 * @purpose Thêm thông tin góp ý
	 * @date    Jun 10, 2014 :: 9:51:43 AM 
	 * @param evt
	 */
	public void actionThemGopY(AjaxBehaviorEvent evt){		
		if(props.getObjThongTinGopYModel().getTtgy_noidung().trim().compareTo("")!=0){
			props.getObjThongTinGopYModel().setTtgy_noidung(props.getObjThongTinGopYModel().getTtgy_noidung().trim());
			props.getObjThongTinGopYModel().setTtgy_lc_id(Long.parseLong((objVBDen_XuLyFacade
					.getMaxLuanChuyenId(Long.parseLong(props.getObjXemVanBan().get("vbden_id").toString())).toString())));
			props.getObjThongTinGopYModel().setTtgy_nguoigui(getUserFullName());
			props.getObjThongTinGopYModel().setTtgy_nhomvb(1);
			props.getObjThongTinGopYModel().setTtgy_userid(getUserId());
			props.getObjThongTinGopYModel().setPhongban_organizationid(phongban_org);
			props.getObjThongTinGopYModel().setDonvi_organizationid(getOrganizationId());
			props.getObjThongTinGopYModel().setTtgy_vb_id(vbden_id);
			props.getObjThongTinGopYModel().setTtgy_thoigian(Calendar.getInstance().getTime());
			objVBDen_XuLyFacade.themGopY(props.getListTapTinGopY(), props.getObjThongTinGopYModel());
			/*Lưu tập tin góp ý sang thư mục chính thức*/
			if(props.getListTapTinGopY().size()!=0){
				for(TapTinGopYModel model:props.getListTapTinGopY()){
					File f=new File(props.getBasePath()+vTmpPathGy+model.getTtgy_tt_tenluutru());
					if(f.exists()){
						File newFile=new File(props.getBasePath()+vPathGy+model.getTtgy_tt_tenluutru());
						f.renameTo(newFile);
					}
				}
			}
			//Gửi thông báo góp ý
			Long nguoiXlChinhHienTai = 0l;
			VBDen_LuanChuyenModel maxLuanChuyen = objVBDen_LuanChuyenFacade.getObjLuanChuyen(Long.parseLong(objVBDen_XuLyFacade.getMaxLuanChuyenId(vbden_id)));
			nguoiXlChinhHienTai = maxLuanChuyen.getVbden_lc_xlchinh_userid();
			String noidungThongBao = ("["+props.getObjXemVanBan().get("vbden_sohieugoc").toString().replace("$", "")+"]")+" "
					+(props.getObjXemVanBan().get("vbden_trichyeu").toString().length()>100?(props.getObjXemVanBan()
							.get("vbden_trichyeu").toString().substring(0, 80)+"..."):props.getObjXemVanBan().get("vbden_trichyeu").toString())
							+": "+props.getObjThongTinGopYModel().getTtgy_noidung();
			noidungThongBao = HtmlUtil.escape(noidungThongBao);
			
			log.info("org Notification "+ getOrgNotification());
			objNotificationClient
			.sendNotification(objUserUtil.getUserId()+"",nguoiXlChinhHienTai+"",
					maxLuanChuyen.getVbden_lc_xlchinh_dvid()+"",
					"Góp ý văn bản đến", 
					NotificationsVar.SUBTITLE_XULY, objDmLinkFacade
					.getLinkModelByMa(Hcdt.XU_LY_VAN_BAN_DEN).getLink_id(),
					"?instant="+props.getObjXemVanBan().get("vbden_id").toString()
					,noidungThongBao, 
					null, NotificationsVar.NHAN_THONG_TIN_GOP_Y,
					getOrgNotification());
			
			//counter.stop();
			//log.info("Thời gian thực hiện "+counter.getTime());
			props.setObjThongTinGopYModel(new ThongTinGopYModel());
			props.setListTapTinGopY(new ArrayList<TapTinGopYModel>());
			taoCayLuanChuyen();
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Góp ý thành công',3);");
		}
		else{
			props.getObjThongTinGopYModel().setTtgy_noidung(props.getObjThongTinGopYModel().getTtgy_noidung().trim());
			Validator.showErrorMessage(getPortletId(), "frm:txtNoiDungGopY", "Vui lòng nhập nội dung góp ý");			
		}
		
	}
	
	/**
	 * @author  ltbao
	 * @purpose chuẩn bị sửa hạn xử lý toàn văn bản --> chỉ dùng cho người dùng dự thảo văn bản
	 * @date    Jun 16, 2014 :: 4:57:02 PM 
	 * @param   evt
	 */
	public void preActionSuaHanXuLyToanVanBan(AjaxBehaviorEvent evt){
		suaHanXlToanVb=true;
	}
	
	/**
	 * @author  ltbao
	 * @purpose  Sửa hạn xử lý toàn văn bản
	 * @date    Jun 16, 2014 :: 4:58:22 PM 
	 * @param evt <h:message for="txtHanXuLyToanVanBan"></h:message>
	 */
	public void actionSuaHanXuLyToanVanBan1(AjaxBehaviorEvent evt){		
		boolean validate = true;
		Date re;
		if((Date)props.getObjXemVanBan().get("vbden_hanxl_toanvanban")==null){
			re=null;
		}
		else{
			re =(Date)props.getObjXemVanBan().get("vbden_hanxl_toanvanban");
		}
		if(re!=null&&CommonUtils.convertDate(re).before(CommonUtils.convertDate(Calendar.getInstance().getTime()))){
			validate = false;
			Validator.showErrorMessage(getPortletId(), "frm:txtHanXuLyToanVanBan", "Hạn xử lý toàn văn bản phải lớn hơn hoặc bằng ngày hiện tại");
		}
		
		if(validate){
			props.getObjXemVanBan().put("vbden_hanxl_toanvanban", re);
			objVBDen_XuLyFacade.suaHanXuLyToanVanBan(re, vbden_id);
			suaHanXlToanVb=false;
		}
	}
	
	public void actionSuaHanXuLyToanVanBan(DateSelectEvent evt){
		suaHanXlToanVb=false;
		Date re=evt.getDate();
		props.getObjXemVanBan().put("vbden_hanxl_toanvanban", re);
		objVBDen_XuLyFacade.suaHanXuLyToanVanBan(re, vbden_id);
	}
		
	/**
	 * @author  ltbao
	 * @purpose Sửa văn bản đến
	 * @date    Jun 11, 2014 :: 8:20:17 PM 
	 * @param   evt
	 */
	public void actionSuaVanBan(AjaxBehaviorEvent evt){
		String lv="";	
		for(DmLinhVucModel model:propsLinhVuc.getDsLinhVuc()){
			if(model.isChecked()){
				lv+=","+model.getLv_id();
			}
		}
		if(lv.compareTo("")!=0){
			lv=lv.substring(1);
		}
		lv="{"+lv+"}";
		propsLinhVuc.setDsLinhVucId(lv);
		if(modifyValidation()){		
			//Ghi log but phe
			if(props.getObjVBDenModel().getVbden_butphe()==null){
				props.getObjVBDenModel().setVbden_butphe("");
			}
			if(!props.getButPheVanBan().equals(props.getObjVBDenModel().getVbden_butphe())){
				//log.info(">>>Sửa but phe: "+props.getButPheVanBan());
				if(props.getObjVBDenModel().getVbden_butphe() == null || props.getObjVBDenModel().getVbden_butphe().trim().equals("")){//nếu là xóa bút phê
					props.getObjVBDenModel().setVbden_butphe_userid(null);
					props.getObjVBDenModel().setVbden_butphe_ngay(null);
					//log.info(">>>Sửa but phe -> rong: "+props.getButPheVanBan());
				}else{
					//log.info(">>>Sửa but phe -> khac rong: "+props.getButPheVanBan());
					props.getObjVBDenModel().setVbden_butphe_userid(userId);
					props.getObjVBDenModel().setVbden_butphe_ngay(DateUtils.getCurrentDate());
				}					
			}
			
			props.getObjXemVanBan().put("vbden_butphe_ngay", props.getObjVBDenModel().getVbden_butphe_ngay());
			props.getObjXemVanBan().put("vbden_butphe_fullname", getUserFullName());
			props.getObjXemVanBan().put("vbden_butphe_userid", userId);
			
			//@ptgiang Loại bỏ link xem chi tiết
			props.getObjXemVanBan().put("vbden_phucdap", props.getObjVBDenModel().getVbden_phucdap());//Backup trước khi escape để view
			String escapeLink = CommonUtils.escapeLinkVblq(props.getObjVBDenModel().getVbden_phucdap());					
			props.getObjVBDenModel().setVbden_phucdap(escapeLink);//@ptgiang Load link xem chi tiết
			
			props.getObjVBDenModel().setVbden_linhvuc(lv);
			objVBDenFacade.suaVanBanDen(props.getObjVBDenModel(), props.getListThemSua(),props.getListXoaSua(), props.getListTapTinVanBanSua());
			
			//Lưu và thư mục chính thức
			if(props.getListThemSua()!=null){
				for(TapTinDinhKemVanBanModel model:props.getListThemSua()){
					File f=new File(props.getBasePath()+vTmpPath+model.getTtdk_tenluutru());
					if(f.exists()){
						File newFile=new File(props.getBasePath()+vPath+model.getTtdk_tenluutru());
						f.renameTo(newFile);
					}
				}
			}
			if(props.getListXoaSua()!=null){
				for(TapTinDinhKemVanBanModel xoa:props.getListXoaSua()){
					File f=new File(props.getBasePath()+vPath+xoa.getTtdk_tenluutru());
					if(f.exists()){
						log.info("Xóa file "+f.getName()+"-->"+(f.delete()?"Thành công":"Không thành công"));						
					}
				}
			}		
			
			//Cập nhật thông tin văn bản
			props.setListTapTinVanBan(props.getListTapTinVanBanSua());
			
			props.setListLinhVuc(objDmLinhVucFacade.getDsLinhVucTheoDsId(props.getObjVBDenModel().getVbden_linhvuc()));
			
			props.getObjVanBanDen().put("vbden_sohieugoc",	props.getObjVBDenModel().getVbden_sohieugoc());
			props.getObjVanBanDen().put("vbden_trichyeu",	props.getObjVBDenModel().getVbden_trichyeu());
			
			props.getObjXemVanBan().put("lvb_ten", 			objDmLoaiVanBanFacade.getObjLoaiVanBan(props.getObjVBDenModel().getLvb_id()).getLvb_ten());
			props.getObjXemVanBan().put("svb_id",			props.getObjVBDenModel().getSvb_id());			
			props.getObjXemVanBan().put("svb_ten",			objDmSoVanBanFacade.getObjSoVanBan(props.getObjVBDenModel().getSvb_id()).getSvb_ten());			
			props.getObjXemVanBan().put("dm_ten",			objDmDoMatFacade.getObjDoMat(props.getObjVBDenModel().getDm_id()).getDm_ten());
			props.getObjXemVanBan().put("dk_ten",			objDmDoKhanFacade.getObjDoKhan(props.getObjVBDenModel().getDk_id()).getDk_ten());
			
			props.getObjXemVanBan().put("vbden_soden", 		props.getObjVBDenModel().getVbden_soden().replace("$",""));
			props.getObjXemVanBan().put("vbden_ngayden", 	props.getObjVBDenModel().getVbden_ngayden());			
			props.getObjXemVanBan().put("vbden_sohieugoc",	props.getObjVBDenModel().getVbden_sohieugoc());
			props.getObjXemVanBan().put("vbden_trichyeu",	props.getObjVBDenModel().getVbden_trichyeu());
			props.getObjXemVanBan().put("vbden_ngaybanhanh",props.getObjVBDenModel().getVbden_ngaybanhanh());
			props.getObjXemVanBan().put("vbden_ngaycohl",	props.getObjVBDenModel().getVbden_ngaycohl());
			props.getObjXemVanBan().put("vbden_ngayhethl",	props.getObjVBDenModel().getVbden_ngayhethl());
			props.getObjXemVanBan().put("vbden_coquanbanhanh",props.getObjVBDenModel().getVbden_coquanbanhanh());
			props.getObjXemVanBan().put("vbden_nguoiky",	props.getObjVBDenModel().getVbden_nguoiky());
			props.getObjXemVanBan().put("vbden_soto",		props.getObjVBDenModel().getVbden_soto());
			//props.getObjXemVanBan().put("vbden_phucdap",	props.getObjVBDenModel().getVbden_phucdap());
			props.getObjXemVanBan().put("vbden_trangthai_lt",props.getObjVBDenModel().getVbden_trangthai_lt());
			props.getObjXemVanBan().put("vbden_thongtin_lt",props.getObjVBDenModel().getVbden_thongtin_lt());
			props.getObjXemVanBan().put("vbden_butphe", props.getObjVBDenModel().getVbden_butphe());
			
			//Sửa lổi hai trình duyệt khi sửa độ mật của văn bản đến
			props.getObjXemVanBan().put("dm_id",props.getObjVBDenModel().getDm_id());
			props.getObjVanBanDen().put("dm_id",props.getObjVBDenModel().getDm_id());
			
			props.getObjXemVanBan().put("vbden_bophanthuchien",props.getObjVBDenModel().getVbden_bophanthuchien());
			props.getObjXemVanBan().put("vbden_ghichu",props.getObjVBDenModel().getVbden_ghichu());
			
			//Kiểm tra lại quyền sửa văn bản
			CauHinhDoMatModel objCauHinh = objCauHinhDoMatFacade.getObjCauHinhDoMat(getOrganizationId(), props.getObjVBDenModel().getDm_id());
			boolean quyenSua = true;
			if(objCauHinh!=null){				
				if(objCauHinh.getVanthu_capnhat()!=null){
					quyenSua = objCauHinh.getVanthu_capnhat();
				}
			}
			quyenSuaVanBan = (userId==Long.parseLong(props.getObjXemVanBan().get("userid").toString())) ||(quyenVanThu && quyenSua);
			
			String tmpTrichYeu = props.getObjXemVanBan().get("vbden_trichyeu").toString();
			if(tmpTrichYeu.length()>130){
				tmpTrichYeu = tmpTrichYeu.substring(0, 130);
				tmpTrichYeu = tmpTrichYeu+" ...";
			}
			props.getObjVanBanDen().put("tmpTrichYeu", tmpTrichYeu);
			//Tăng số lần sửa lên 1
			vanBanLogBean.setVbdenLogSoLuong(vanBanLogBean.getVbdenLogSoLuong()+1);
			//Gửi thông báo
			String noidungThongBao =(props.getObjXemVanBan()
					.get("vbden_trichyeu").toString().length()>100?(props.getObjXemVanBan()
							.get("vbden_trichyeu").toString().substring(0, 100)+"..."):props
							.getObjXemVanBan().get("vbden_trichyeu").toString());
			
			noidungThongBao = HtmlUtil.escape(noidungThongBao);
			log.info("org Notification "+ getOrgNotification());
			objNotificationClient.sendNotification(
				objUserUtil.getUserId()+"", 
				objVBDen_LuanChuyenFacade.getChuoiNguoiXuLyChinhTheoVanBan(props.getObjVBDenModel().getVbden_id()), 
				null,
				"Cập nhật thông tin văn bản đến", 
				"", 
				objDmLinkFacade.getLinkModelByMa(Hcdt.XU_LY_VAN_BAN_DEN).getLink_id(), 
				"?instant="+props.getObjVBDenModel().getVbden_id()+"", 
				noidungThongBao, 
				null, 
				NotificationsVar.CAP_NHAT_THONG_TIN_VAN_BAN_XU_LY,
				getOrgNotification());
			
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogSuaVanBanDen.hide();");
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Cập nhật thành công',3);");
			props.setListThemSua(null);
			props.setListXoaSua(null);
			props.setListTapTinVanBanSua(null);  
			props.setObjVBDenModel(null);
		}					
	}
		
	@SuppressWarnings("unchecked")
	public void actionXemTapTinLuanChuyen(AjaxBehaviorEvent evt){
		Object obj=evt.getComponent().getAttributes().get("list");
		if(obj!=null){
			props.setListTapTinLuanChuyenTmp((List<TapTinDinhKemLuanChuyenModel>)obj);
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogTapTinDinhKemLuanChuyen.show()");
		}
		
	}
	
	/**
	 * @author  ltbao
	 * @purpose Lọc văn bản đến theo các điều kiện
	 * @date    Jun 11, 2014 :: 9:25:17 AM 
	 * @param   evt
	 */
	public void actionLocVanBan(AjaxBehaviorEvent evt){
		props.setLocTheoHSVB(false);
		request = "";
		props.getObjVBDen_XuLyFilterModel().setCoquanbanhanh(propsDonVi.getDsTenDonVi());
		taoDuLieuThongKe(null);
		props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));
		if(props.getListVanBanDen()==null||props.getListVanBanDen().size()!=0){
			hienTabSet = true;
		}else{
			hienTabSet = false;
		}
		log.info("Trích yếu: "+ props.getObjVBDen_XuLyFilterModel().getTrichyeu());
		if(props.getObjVBDen_XuLyFilterModel().getTrichyeu() != null && !props.getObjVBDen_XuLyFilterModel().getTrichyeu().isEmpty()){
			JavascriptUtils.callJavascript("showHighlight('"+props.getObjVBDen_XuLyFilterModel().getTrichyeu() +"');");
		}
	}
	
	public VBDen_XuLyFilterModel getFilterModelTheoHSVB(){
		VBDen_XuLyFilterModel objLocTheoHsvb  = new VBDen_XuLyFilterModel();
		objLocTheoHsvb.setDsIdHsvb("");
		
		for(Map<String,Object> map:props.getListFilterHoSoVanBan()){
			if(Boolean.parseBoolean(map.get("chon").toString())){
				objLocTheoHsvb.setDsIdHsvb(objLocTheoHsvb.getDsIdHsvb()+","+map.get("hsvb_id").toString());
			}				
		}
		if(objLocTheoHsvb.getDsIdHsvb().trim().equals("")==false){
			objLocTheoHsvb.setDsIdHsvb(objLocTheoHsvb.getDsIdHsvb().substring(1));
		}
		
		return objLocTheoHsvb;		
	}
	
	public void locTheoHsvb(){
		request = "";	
		props.setLocTheoHSVB(true);
		VBDen_XuLyFilterModel objLocTheoHsvb = getFilterModelTheoHSVB();		
		props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,objLocTheoHsvb));
		props.getListVanBanDenLazy().setRowCount(props.getListVanBanDen().size());
		taoDuLieuThongKe(objLocTheoHsvb);
	}
	
	/**
	 * @author  ltbao
	 * @purpose Tiền xử lý thêm mới sổ văn bản trực tiếp
	 * @date    Jun 11, 2014 :: 7:00:10 PM 
	 * @param   evt
	 */
	public void preActionThemSoVanBan(AjaxBehaviorEvent evt){		
		propsSoVanBan.setObjDmSoVanBanModel(new DmSoVanBanModel());
		
		//propsSoVanBan.setOrganizationid(getOrganizationId())
		propsSoVanBan.getObjDmSoVanBanModel().setCompanyid(getCompanyId());
		propsSoVanBan.getObjDmSoVanBanModel().setNvb_id(1);
		propsSoVanBan.getObjDmSoVanBanModel().setOrganizationid(propsSoVanBan.getOrganizationid());
		propsSoVanBan.getObjDmSoVanBanModel().setOrganization_ten("");
		propsSoVanBan.getObjDmSoVanBanModel().setSvb_ten("");
		propsSoVanBan.getObjDmSoVanBanModel().setSvb_ma("");		
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "sovanbanDialog.show()");
	}
	
	/**
	 * @author  ltbao
	 * @purpose 
	 * @date    Jun 12, 2014 :: 2:06:02 PM 
	 * @param evt
	 */
	@SuppressWarnings("unchecked")
	public void actionXoaVanBan(AjaxBehaviorEvent evt){
		Object obj=evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			Map<String,Object> mapBiXoa = (Map<String,Object>)obj;
			String vanBanDuocChon="";
			for(Map<String,Object> map:props.getListVanBanDen()){
				if(Integer.parseInt(map.get("select").toString()) == 1){
					vanBanDuocChon = map.get("vbden_id").toString();
					break;
				}
			}
			boolean isSelect  = Integer.parseInt(mapBiXoa.get("select").toString()) == 1;
			//Nếu văn bản tồn tại
			if(objVBDenFacade.isExists(Long.parseLong(mapBiXoa.get("vbden_id").toString()))){
				if(objVBDen_XuLyFacade.daTiepNhanTuChuyenDonVi(Long.parseLong(mapBiXoa.get("vbden_id").toString()))){//Nếu khi chuyển đơn vị khác đã tiếp nhận
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản đã được đơn vị khác tiếp nhận, không thể xóa',4)");
				}else if(!objVBDen_XuLyFacade.kiemTraXoa(Long.parseLong(mapBiXoa.get("vbden_id").toString()))){
					Long vbdenid = Long.parseLong(mapBiXoa.get("vbden_id").toString());
					
					//Cập nhật lại kết quả xử lý là đã xóa
					VBDenModel objVBDenModel = objVBDenFacade.getObjVanBanDen(vbdenid);
					if(objVBDenModel.getOrganizationid()!=objVBDenModel.getCunght_organizationid()){//Nếu là đến cùng hệ thống
						long vbIdGoc;
						int nvbId;
						if(objVBDenModel.getVbden_id_ldv() != null){//Tiếp nhận từ chuyển xử lý văn bản đến
							nvbId = IContanst.NVB_DEN;
							vbIdGoc = objVBDenModel.getVbden_id_ldv();
						} else{//Tiếp nhận từ phát hành văn bản đi
							nvbId = IContanst.NVB_DI_PH;
							vbIdGoc = objVBDenModel.getPhdi_id()==null?-1:objVBDenModel.getPhdi_id();
						}
						
						KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade.getObjKetQuaXuLy(vbIdGoc, nvbId, objVBDenModel.getOrganizationid());
						if(kqxlModel != null){
							kqxlModel.setKqxl_daxem(true);
							kqxlModel.setKqxl_trangthai(IContanst.DA_XOA);//Đã xóa
							kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil.getUserFullName(userId));
							kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils.getCurrentDate());
							objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);
						}
					}
					
					objVBDen_XuLyFacade.xoaVanBanDen(vbdenid);	
					
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad, cPageSize, false,null));				
					log.info(">>> "+props.getListVanBanDenLazy().getRowCount());
					if(props.getListVanBanDenLazy().getRowCount()>1){
						if(isSelect){
							if(props.getListVanBanDen()!=null&&props.getListVanBanDen().size()!=0){
								props.getListVanBanDen().get(0).put("select", 1);
								hienTabSet = false;
								log.info("Ẩn thông tin khi xóa văn bản đang chọn");
							}else{
								hienTabSet = false;
							}
						}else{
							if(vanBanDuocChon.equals("")==false){
								props.getListVanBanDen().get(0).put("select", 0);
								for(Map<String,Object> map:props.getListVanBanDen()){
									if(map.get("vbden_id").toString().equals(vanBanDuocChon)){
										map.put("select", 1);
										break;
									}
								}
							}
						}
					}else{
						props.getListVanBanDenLazy().setRowCount(0);
						hienTabSet = false;
					}					
				}else{
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Trạng thái của văn bản đã bị thay đổi, không thể xóa',4)");
					taoDuLieuThongKe(null);			
					props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
				}				
			}else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản không tồn tại',4);");
				taoDuLieuThongKe(null);			
				props.setListVanBanDenLazy(getLazyDataModel(pageLoad,cPageSize, true,null));	
			}
			/*int tong = Integer.parseInt(props.getObjThongKe().get("tong").toString());
			props.getObjThongKe().put("tong",tong - 1);
			int chuaxuly = Integer.parseInt(props.getObjThongKe().get("chuaxuly").toString());
			props.getObjThongKe().put("chuaxuly",chuaxuly - 1);*/
			loadThongTin(props.getListVanBanDen().get(0));
			taoDuLieuThongKe(null);
		}
	}
	
	/**
	 * @author  ltbao
	 * @purpose Xuất thông tin xử lý
	 * @date    Oct 23, 2014 :: 10:05:14 AM 
	 * @param 	evt
	 */
	public void actionXuatThongTinXuLy(AjaxBehaviorEvent evt){
		Object obj = evt.getComponent().getAttributes().get("tenbmluutru");
		Object obj2 = evt.getComponent().getAttributes().get("tenbmhienthi");
		Map<String, Object> mapThongTinBMXuat = new HashMap<String, Object>();
		mapThongTinBMXuat.put("tendonvicap1", loader.getParams().get("vTenDvCap1"));
		mapThongTinBMXuat.put("tendonvicap2", loader.getParams().get("vTenDvCap2"));
		mapThongTinBMXuat.put("tieude", "THÔNG TIN XỬ LÝ VĂN BẢN ĐẾN");
		if(obj!=null){			
			String filePath = props.getBasePath()+vPathBieuMau+obj.toString();			
			if(new File(filePath).exists()){
				resource = xuatThongTinXuLy(mapThongTinBMXuat, props.getObjXemVanBan(), props.getListLuanChuyen(),props.getListNoiNhanLienDonVi(), filePath);
				if(obj2!=null){
					fileName=obj2.toString();
				}else{
					fileName="BM_XuLyVanBanDen.docx";
				}
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx'); dialogChonBieuMau.hide();");
			}else{
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Tập tin biễu mẫu không tồn tại. Vui lòng làm mới lại trình duyệt')");
			}
			log.info("Xuất thành công");
		}else{
			if(props.getListBieuMau().size()!=0){
				String filePath = props.getBasePath()+vPathBieuMau+props.getListBieuMau().get(0).getBm_tenluutru();
				if(new File(filePath).exists()){
					resource = xuatThongTinXuLy(mapThongTinBMXuat, props.getObjXemVanBan(), props.getListLuanChuyen(),props.getListNoiNhanLienDonVi(), filePath);
					fileName=props.getListBieuMau().get(0).getBm_tenhienthi();
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
					log.info("Xuất thành công");
				}else{
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Tập tin biễu mẫu không tồn tại. Vui lòng làm mới lại trình duyệt')");
				}
			}else{
				File staticFile = new File(props.getBasePath()+vPathBieuMau+"staticBieuMauXuLyVanBanDen.docx");
				if(staticFile.exists()){
					resource = xuatThongTinXuLy(mapThongTinBMXuat, props.getObjXemVanBan(), props.getListLuanChuyen(),props.getListNoiNhanLienDonVi(), props.getBasePath()+vPathBieuMau+"staticBieuMauXuLyVanBanDen.docx");
					fileName="BM_XuLyVanBanDen.docx";
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
				}else{
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),"thongBao('Vui lòng cấu hình biểu mẫu xuất để thực hiện chức năng này',4)");
				}
			}
		}
	}
	
	//nduong111111111111111111111111111111111111111111
	/**
	 * @author  ltbao
	 * @purpose Quản lý xuất danh sách văn bản xử lý theo hai định WORD và EXCELL
	 * @date    Jun 23, 2014 :: 7:40:55 PM 
	 * @param   type
	 */
	public void xuatDanhSach(String type){
		if(type!=null){
			Map<String, Object> mapThongTinBMXuat = new HashMap<String, Object>();
			mapThongTinBMXuat.put("tendonvicap1", loader.getParams().get("vTenDvCap1"));
			mapThongTinBMXuat.put("tendonvicap2", loader.getParams().get("vTenDvCap2"));
			mapThongTinBMXuat.put("tieude", "THÔNG TIN XỬ LÝ VĂN BẢN ĐẾN");
			List<Map<String,Object>> listFull = null;;
			if(props.isLocTheoHSVB()){
				listFull = objVBDen_XuLyFacade
						.locDanhSachXuLyVanBan(getFilterModelTheoHSVB()
								, userId, companyId, getOrganizationId(), phongban_org, modeVaiTro, soNgayToiHan, props.getVaiTro(),0, 0,request,getListDvUser(),getOrgIdTemp());
			}else{
				listFull = objVBDen_XuLyFacade
						.locDanhSachXuLyVanBan(props.getObjVBDen_XuLyFilterModel()
								, userId, companyId, getOrganizationId(), phongban_org, modeVaiTro, soNgayToiHan, props.getVaiTro(),0, 0,request,getListDvUser(),getOrgIdTemp());
			}
			if(type.compareTo("DOC")==0){	
				resource = pCreateFileWord(mapThongTinBMXuat, listFull, "DS_XuLyVanBanDen");
				fileName="DS_XuLyVanBanDen.docx";
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
				
			}
			else if(type.compareTo("XLS")==0){
				resource = pCreateFileExcel(mapThongTinBMXuat, listFull, "DS_XuLyVanBanDen");
				fileName="DS_XuLyVanBanDen.xls";
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "onDownload('downloadx')");
			}		
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private Resource xuatThongTinXuLy(Map<String, Object> mapThongTinBMXuat, Map<String, Object> mapThongTinVanBan,List<Map<String,Object>> listLuanChuyen, List<DmVanBanLienDonViModel> listNoiNhan, String filePath) {
		log.info("---------createFileBMXuat-----------");
		
		DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");		
		try {
			/* Đọc file */
			InputStream inputDocx = new FileInputStream(filePath);		
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputDocx, TemplateEngineKind.Velocity);
			inputDocx.close();
			IContext context = report.createContext();
			//Thong tin chung
			context.put("tt_chung.donvicap1", mapThongTinBMXuat.get("tendonvicap1"));
			context.put("tt_chung.donvicap2", mapThongTinBMXuat.get("tendonvicap2"));
			context.put("tt_chung.tieude", mapThongTinBMXuat.get("tieude"));
			context.put("tt_chung.thoigian",dfm.format(Calendar.getInstance().getTime()));
			
			context.put("tt_chung.ngay", (DateUtils.getCurrentDayOfMonth() > 9 ? DateUtils.getCurrentDayOfMonth() : ("0" + DateUtils.getCurrentDayOfMonth())));
			context.put("tt_chung.thang", (DateUtils.getCurrentMonth() > 9 ? DateUtils.getCurrentMonth(): ("0" + DateUtils.getCurrentMonth())));
			context.put("tt_chung.nam", DateUtils.getCurrentYear()); 
			//organizationten
			context.put("xlvbden_tt_chung.donvi", 			mapThongTinVanBan.get("donvixuly")		==null?"":mapThongTinVanBan.get("donvixuly").toString()				);
			//Thông tin văn bản
			context.put("xlvbden_tt_chung.cachthucxuly", 	mapThongTinVanBan.get("ctxl_ten")				==null?"":mapThongTinVanBan.get("ctxl_ten").toString()						);
			context.put("xlvbden_tt_chung.sohieugoc", 		mapThongTinVanBan.get("vbden_sohieugoc")		==null?"":mapThongTinVanBan.get("vbden_sohieugoc").toString()				);	
			context.put("xlvbden_tt_chung.trichyeu",		mapThongTinVanBan.get("vbden_trichyeu")			==null?"":mapThongTinVanBan.get("vbden_trichyeu").toString()				);
			context.put("xlvbden_tt_chung.butphe",			mapThongTinVanBan.get("vbden_butphe")			==null?"":mapThongTinVanBan.get("vbden_butphe").toString());
			context.put("xlvbden_tt_chung.han_xltvb",		mapThongTinVanBan.get("vbden_hanxl_toanvanban")	==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_hanxl_toanvanban"))	);
			context.put("xlvbden_tt_chung.soden",			mapThongTinVanBan.get("vbden_soden")			==null?"":mapThongTinVanBan.get("vbden_soden").toString()					);
			context.put("xlvbden_tt_chung.ngayden",			mapThongTinVanBan.get("vbden_ngayden")			==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_ngayden"))			);
			context.put("xlvbden_tt_chung.loaivanban",		mapThongTinVanBan.get("lvb_ten")				==null?"":mapThongTinVanBan.get("lvb_ten").toString()						);
			context.put("xlvbden_tt_chung.sovanban",		mapThongTinVanBan.get("svb_ten")				==null?"":mapThongTinVanBan.get("svb_ten").toString()						);
			context.put("xlvbden_tt_chung.domat",			mapThongTinVanBan.get("dm_ten")					==null?"":mapThongTinVanBan.get("dm_ten").toString()						);
			context.put("xlvbden_tt_chung.dokhan",			mapThongTinVanBan.get("dk_ten")					==null?"":mapThongTinVanBan.get("dk_ten").toString()						);
			context.put("xlvbden_tt_chung.nguoigui",		mapThongTinVanBan.get("vbden_lc_nguoichuyen")	==null?"":mapThongTinVanBan.get("vbden_lc_nguoichuyen").toString()			);
			context.put("xlvbden_tt_chung.ngaygui",			mapThongTinVanBan.get("vbden_lc_ngaychuyen")	==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_lc_ngaychuyen"))	);
			context.put("xlvbden_tt_chung.hanxuly",			mapThongTinVanBan.get("vbden_lc_hanxl")	==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_lc_hanxl"))					);
			String dsLinhVuc = "";
			for(Map<String,Object> mapLv:props.getListLinhVuc()){
				dsLinhVuc+=", "+mapLv.get("lv_ten").toString();
			}
			if(dsLinhVuc.equals("")==false){
				dsLinhVuc = dsLinhVuc.substring(1);
			}
			context.put("xlvbden_tt_chung.linhvuc",			dsLinhVuc																											);
			String noiDungLuuTru = "";
			boolean trangThaiLt = Boolean.parseBoolean(mapThongTinVanBan.get("vbden_trangthai_lt").toString());
			noiDungLuuTru = (trangThaiLt?"(Đã":"(Chưa")+" lưu trữ)";
			
			if(trangThaiLt){
				noiDungLuuTru = noiDungLuuTru + ((mapThongTinVanBan.get("vbden_thongtin_lt")==null||mapThongTinVanBan.get("vbden_thongtin_lt").toString().trim().equals(""))?"":(" - "+mapThongTinVanBan.get("vbden_thongtin_lt").toString()));
			}
			log.info(">>>>>> "+noiDungLuuTru);
			context.put("xlvbden_tt_chung.luutru",			noiDungLuuTru																										);
			context.put("xlvbden_tt_chung.ngaybanhanh", 	mapThongTinVanBan.get("vbden_ngaybanhanh")		==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_ngaybanhanh")));
			context.put("xlvbden_tt_chung.ngaycohieuluc",	mapThongTinVanBan.get("vbden_ngaycohl")			==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_ngaycohl")));
			context.put("xlvbden_tt_chung.ngayhethieuluc",	mapThongTinVanBan.get("vbden_ngayhethl")		==null?"":dfm.format((Date)mapThongTinVanBan.get("vbden_ngayhethl")));
			context.put("xlvbden_tt_chung.coquanbanhanh",	mapThongTinVanBan.get("vbden_coquanbanhanh")	==null?"":mapThongTinVanBan.get("vbden_coquanbanhanh").toString()	);
			context.put("xlvbden_tt_chung.nguoiky",			mapThongTinVanBan.get("vbden_nguoiky")			==null?"":mapThongTinVanBan.get("vbden_nguoiky").toString()			);
			context.put("xlvbden_tt_chung.soto",			mapThongTinVanBan.get("vbden_soto")				==null?"":mapThongTinVanBan.get("vbden_soto").toString()			);
			context.put("xlvbden_tt_chung.phucdap",			mapThongTinVanBan.get("vbden_phucdap")			==null?"":StringUtils.removeEnd(mapThongTinVanBan.get("vbden_phucdap").toString()	, ","));
			context.put("xlvbden_tt_chung.xlchinh",			UserUtil.getUserFullName(Long.parseLong(mapThongTinVanBan.get("vbden_lc_xlchinh_userid").toString())));
			
			/* Khai bao list */
			FieldsMetadata metadata = new FieldsMetadata();
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.stt");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.nguoigui");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.ngaygui");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.nguoi_xlchinh");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.ngayxl");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.nguoi_xltieptheo");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.noidung_xl");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.thuhoi");
			metadata.addFieldAsList("xlvbden_ds_luanchuyen.phobien");
			
			metadata.addFieldAsList("xlvbden_ds_donvinhan.stt");
			metadata.addFieldAsList("xlvbden_ds_donvinhan.tendonvi");
			metadata.addFieldAsList("xlvbden_ds_donvinhan.noidunggui");
			metadata.addFieldAsList("xlvbden_ds_donvinhan.nguoigui");
			metadata.addFieldAsList("xlvbden_ds_donvinhan.ngaygui");	
			
			//Danh sách phối hợp
			metadata.addFieldAsList("xlvbden_ds_phoihop.stt");
			metadata.addFieldAsList("xlvbden_ds_phoihop.hoten");
			metadata.addFieldAsList("xlvbden_ds_phoihop.email");
			metadata.addFieldAsList("xlvbden_ds_phoihop.phongban");			
			//Danh sách góp ý			
			metadata.addFieldAsList("xlvbden_ds_gopy.stt");
			metadata.addFieldAsList("xlvbden_ds_gopy.nguoigui");
			metadata.addFieldAsList("xlvbden_ds_gopy.ngaygui");
			metadata.addFieldAsList("xlvbden_ds_gopy.noidung");
			
			//Danh sách tất cả góp ý			
			metadata.addFieldAsList("xlvbden_ds_tatca_gopy.stt");
			metadata.addFieldAsList("xlvbden_ds_tatca_gopy.nguoigui");
			metadata.addFieldAsList("xlvbden_ds_tatca_gopy.ngaygui");
			metadata.addFieldAsList("xlvbden_ds_tatca_gopy.noidung");
			report.setFieldsMetadata(metadata);	
			
			//Danh sách phối hợp xử lý
			List<Map<String,Object>> dsPhoiHop = new ArrayList<Map<String,Object>>();
			int sttPb = 1;
			List<Map<String,Object>> listPhoiHop=objVBDen_XuLyFacade.getDsPhoiHopXuLy(vbden_lc_id);
			if(listPhoiHop.size()!=0){
				for(Map map:listPhoiHop){
					map.put("phongban", objUserUtil
							.getStringPhongBanNguoiDung(Long.parseLong(map.get("userid").toString()), getOrganizationId()));
				}
			}			
			for(Map<String,Object> map:listPhoiHop){
				Map<String,Object> mapPhoiHop = new HashMap<String, Object>();
				mapPhoiHop.put("stt", sttPb);
				mapPhoiHop.put("hoten", UserUtil.getUserFullName(Long.parseLong(map.get("userid").toString())));
				mapPhoiHop.put("email", map.get("emailaddress").toString());
				mapPhoiHop.put("phongban", map.get("phongban").toString());				
				sttPb++;
				dsPhoiHop.add(mapPhoiHop);
			}
			context.put("xlvbden_ds_phoihop", dsPhoiHop);
			
			//Danh sách góp ý
			int sttGy = 1;
			List<ThongTinGopYModel> listGopYModel = objThongTinGopYFacade.getListThongTinGopY(Long.parseLong(objVBDen_XuLyFacade.getMaxLuanChuyenId(vbden_id)), 1);
			//List<ThongTinGopYModel> listGopYModel = objThongTinGopYFacade.getListThongTinGopY(vbden_lc_id, 1);
			List<Map<String,Object>> dsGopY = new ArrayList<Map<String,Object>>();
			for(ThongTinGopYModel modelGopY:listGopYModel){
				Map<String,Object> mapGopy = new HashMap<String, Object>();
				mapGopy.put("stt", sttGy);
				mapGopy.put("nguoigui", modelGopY.getTtgy_nguoigui());
				mapGopy.put("ngaygui", dfm.format(modelGopY.getTtgy_thoigian()));
				mapGopy.put("noidung",modelGopY.getTtgy_noidung());
				dsGopY.add(mapGopy);
				sttGy++;
			}
			context.put("xlvbden_ds_gopy", dsGopY);
			
			//Tất cả góp ý
			int sttTatCaGy = 1;
			//List<ThongTinGopYModel> listGopYModel = objThongTinGopYFacade.getListThongTinGopY(props.getVbnb_lc_id(), 2);
			List<Map<String,Object>> dsTatCaGopY = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> map:props.getListLuanChuyen()){
				List<ThongTinGopYModel> listAllGopy = (List<ThongTinGopYModel>)map.get("listgopy");
				for(ThongTinGopYModel modelGopY:listAllGopy){
					Map<String,Object> mapGopy = new HashMap<String, Object>();
					mapGopy.put("stt", sttTatCaGy);
					mapGopy.put("nguoigui", modelGopY.getTtgy_nguoigui());
					mapGopy.put("ngaygui", dfm.format(modelGopY.getTtgy_thoigian()));
					mapGopy.put("noidung",modelGopY.getTtgy_noidung());
					dsTatCaGopY.add(mapGopy);
					sttTatCaGy++;
				}
			}
			context.put("xlvbden_ds_tatca_gopy", dsTatCaGopY);
			
			/* Xuất thông tin luân chuyển */			
			List<Map<String, String>> tt_danhsach = new ArrayList<Map<String, String>>();
			int stt = 1;
			for (Map map: listLuanChuyen) {
				Map tt = new HashMap();
				tt.put("stt", stt);
				tt.put("nguoigui",		map.get("vbden_lc_nguoichuyen").toString());
				tt.put("ngaygui", 		dfm.format((Date)map.get("vbden_lc_ngaychuyen")));
				tt.put("nguoi_xlchinh", map.get("nguoixuly"));				
				tt.put("ngayxl",		(Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())||Boolean.parseBoolean(map.get("vbden_lc_thaotac").toString()))?dfm.format((Date)map.get("vbden_lc_cn_gannhat")):"");
				String nguoixl_tieptheo = "";
				if((stt-1)<listLuanChuyen.size()-1){
					nguoixl_tieptheo = listLuanChuyen.get(stt).get("nguoixuly").toString();
				}
				tt.put("nguoi_xltieptheo",nguoixl_tieptheo);
				tt.put("noidung_xl",map.get("vbden_lc_noidung")==null?"":map.get("vbden_lc_noidung").toString());
				tt.put("thuhoi",Boolean.parseBoolean(map.get("vbden_lc_thuhoi").toString())?"X":"");
				
				String phoBien = "";
				if(map.get("vbden_lc_pb_canhan").toString().equals("{}")==false){				
					List<Map<String,Object>> listPhoBien = objVBDen_LuanChuyenFacade
							.getListNguoiPhoBienTheoBuocLuanChuyen(Long.parseLong(map.get("vbden_lc_id").toString()), getOrganizationId());				
					String dsPhoBien = "";
					for(Map<String,Object> mapPb:listPhoBien){
						dsPhoBien+=", "+mapPb.get("name").toString();
					}
					if(dsPhoBien.equals("")==false){
						dsPhoBien = dsPhoBien.substring(1);
					}
					phoBien = dsPhoBien;
				}else if(map.get("vbden_lc_pb_phongban").toString().equals("")){
					phoBien = "Tất cả";
				}else if(map.get("vbden_lc_pb_phongban").toString().equals("")==false&&map.get("vbden_lc_pb_phongban").toString().equals("0")==false){
					phoBien = "Trong nội bộ";
				}else{
					phoBien = "Không phổ biến";
				}
				tt.put("phobien", phoBien);				
				tt_danhsach.add(tt);
				stt++;
			}	

			context.put("xlvbden_ds_luanchuyen", tt_danhsach);			
			/*Danh sách nơi nhận liên đơn vị*/
			int sttLdv = 1;
			List<Map<String, Object>> danhSachNoiNhan = new ArrayList<Map<String, Object>>();			
			for(DmVanBanLienDonViModel model:listNoiNhan){
				try{
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("stt", sttLdv);
					map.put("tendonvi", OrganizationLocalServiceUtil.getOrganization(model.getLdv_noinhan_organizationid()).getName());
					map.put("nguoigui", UserUtil.getUserFullName(model.getLdv_nguoigui_userid()));
					map.put("noidunggui", model.getLdv_noidung());
					map.put("ngaygui", dfm.format(model.getLdv_ngaygui()));
					danhSachNoiNhan.add(map);
					sttLdv++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			context.put("xlvbden_ds_donvinhan", danhSachNoiNhan);
			/* Xuất word */
			ByteArrayOutputStream outputDocx = new ByteArrayOutputStream();
			report.process(context, outputDocx);			
			ByteArrayResource res = new ByteArrayResource(outputDocx.toByteArray());			
			outputDocx.close();
			return res;
		} catch(Exception ex) {
			log.info("Loi createFileBMXuat:");
			ex.printStackTrace();
			return null;
		}		
	}	
	
	/**
	 * @author  ptgiang
	 * @purpose Xuất danh sách giao việc ra file word
	 * @param mapThongTinBMXuat :: thông tin chung 
	 * @param lstHcdtCongViecModel :: Danh sách giao việc
	 * @param fileName :: Tên file đánh dấu
	 * @return resource .docx
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private Resource pCreateFileWord(Map<String, Object> mapThongTinBMXuat, List<Map<String,Object>> listThongTinVanBanDen, String fileName) {
		log.info("---------createFileBMXuat-----------");
		
		DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			/* Đọc file */
			InputStream inputDocx = new FileInputStream(CommonUtils.getBieuMauCoDinhPath() + "/" + fileName + ".docx");		
			IXDocReport report 	  = XDocReportRegistry.getRegistry().loadReport(inputDocx, TemplateEngineKind.Velocity);
			inputDocx.close();
			IContext context 	  = report.createContext();
			//Thong tin chung
			String orgName = mapThongTinBMXuat.get("tendonvicap1").toString();
			context.put("tt_chung.tendonvicap1", orgName);
			context.put("tt_chung.tendonvicap2", mapThongTinBMXuat.get("tendonvicap2"));
			context.put("tt_chung.ngayhientai", (DateUtils.getCurrentDayOfMonth() > 9 ? DateUtils.getCurrentDayOfMonth() : ("0" + DateUtils.getCurrentDayOfMonth())));
			context.put("tt_chung.thanghientai", (DateUtils.getCurrentMonth() > 9 ? DateUtils.getCurrentMonth(): ("0" + DateUtils.getCurrentMonth())));
			context.put("tt_chung.namhientai", DateUtils.getCurrentYear());
			context.put("tt_chung.nguoidung", getUserFullName().toUpperCase());
			context.put("tt_chung.tongsovanban", listThongTinVanBanDen.size());
			
			/* Khai bao list */
			FieldsMetadata metadata = new FieldsMetadata();
			metadata.addFieldAsList("tt_danhsach.stt");
			metadata.addFieldAsList("tt_danhsach.sohieu");
			metadata.addFieldAsList("tt_danhsach.soden");
			metadata.addFieldAsList("tt_danhsach.trichyeu");
			metadata.addFieldAsList("tt_danhsach.donvi");
			metadata.addFieldAsList("tt_danhsach.ngayden");
			metadata.addFieldAsList("tt_danhsach.ngaychuyenden");
			metadata.addFieldAsList("tt_danhsach.trangthai");//.setSyntaxKind(SyntaxKind.Html.toString());			
			report.setFieldsMetadata(metadata);	
			
			/* Xuất thông tin Thành phần hồ sơ tiếp nhận */			
			List<Map<String, String>> tt_danhsach = new ArrayList<Map<String, String>>();
			int stt = 1;
			for (Map map: listThongTinVanBanDen) {
				Map tt = new HashMap();
				tt.put("stt", stt);
				tt.put("sohieu",map.get("vbden_sohieugoc").toString());
				tt.put("soden", map.get("vbden_soden").toString());
				tt.put("trichyeu", map.get("vbden_trichyeu"));
				tt.put("donvi", map.get("donvixuly"));
				if(map.get("vbden_ngayden")!=null){
					tt.put("ngayden", dfm.format((Date)map.get("vbden_ngayden")));
				}
				else {
					tt.put("ngayden","");
				}
				if(map.get("vbden_lc_ngaychuyen")!=null){
					tt.put("ngaychuyenden", dfm.format((Date)map.get("vbden_lc_ngaychuyen")));
				}
				else{
					tt.put("ngaychuyenden", "");
				}
				String trangthai = "";			
				if(map.get("color").toString().compareTo("vColor_chuaxuly_conhan")==0){
					trangthai="Chưa xử lý còn hạn";
				}
				else if(map.get("color").toString().compareTo("vColor_chuaxuly_trehan")==0){
					trangthai ="Chưa xử lý trễ hạn";
				}
				else if(map.get("color").toString().compareTo("vColor_chuaxuly_khongthoihan")==0){
					trangthai ="Chưa xử lý không thời hạn";
				}
				else if(map.get("color").toString().compareTo("vColor_daxuly")==0){
					trangthai ="Đã xử lý";
				}
				tt.put("trangthai",trangthai);				
				tt_danhsach.add(tt);
				stt++;
			}	

			context.put("tt_danhsach", tt_danhsach);				
			ByteArrayOutputStream outputDocx = new ByteArrayOutputStream();
			/* Xuất word */
			report.process(context, outputDocx);			
			ByteArrayResource res = new ByteArrayResource(outputDocx.toByteArray());
			outputDocx.close();
			return res;
		} catch(Exception ex) {
			log.info("LOI createFileBMXuat:");
			ex.printStackTrace();
			return null;
		}		
	}
		
	/**
	 * @author  ptgiang
	 * @purpose Xuất danh sách giao việc ra file excel
	 * @param mapThongTinBMXuat :: thông tin chung 
	 * @param lstHcdtCongViecModel :: Danh sách giao việc
	 * @param fileName :: Tên file đánh dấu
	 * @return resource .xls
	 */
	@SuppressWarnings("rawtypes")
	private Resource pCreateFileExcel(Map<String, Object> mapThongTinBMXuat, List<Map<String,Object>> listThongTinVanBanDen, String fileName) {
		log.info("createFileThongKe");
		try{					
			DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");			
			String strPath = CommonUtils.getBieuMauCoDinhPath() + "/" + fileName + ".xls";	
			log.info("strPath: " + strPath);
			
			//Đọc file
			File file = new File(strPath);
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook wb = new HSSFWorkbook(fis);		
			fis.close();
			file = null;
			HSSFSheet sheet = wb.getSheetAt(0);
			fis.close();
			
			//Tạo kiểu định dạng			
			ExcelFormatUtils excelFormatUtil = new ExcelFormatUtils(wb);
			
			//Cell tên đơn vị cấp 1
			Row rowTenDonViCap1 = sheet.getRow(0);
			Cell cellTenDonViCap1 = rowTenDonViCap1.getCell(0);
			cellTenDonViCap1.setCellType(Cell.CELL_TYPE_STRING);
			String objTenDonViCap1 = cellTenDonViCap1.getStringCellValue();
			if(objTenDonViCap1!=null &&objTenDonViCap1.length()>0){
				cellTenDonViCap1.setCellValue(objTenDonViCap1.replace("[$tendonvicap1$]", mapThongTinBMXuat.get("tendonvicap1").toString()));
			}
			
			//Cell tên đơn vị cấp 2
			Row rowTenDonViCap2 = sheet.getRow(1);
			Cell cellTenDonViCap2 = rowTenDonViCap2.getCell(0);
			cellTenDonViCap2.setCellType(Cell.CELL_TYPE_STRING);
			String objTenDonViCap2 = cellTenDonViCap2.getStringCellValue();
			if(objTenDonViCap2!=null && objTenDonViCap2.length()>0){
				cellTenDonViCap2.setCellValue(objTenDonViCap2.replace("[$tendonvicap2$]", mapThongTinBMXuat.get("tendonvicap2").toString()));
			}
			
			//Cell ten nguoi dung
			Row rowTenNguoi = sheet.getRow(3);
			Cell cellTenNguoi = rowTenNguoi.getCell(0);
			cellTenNguoi.setCellType(Cell.CELL_TYPE_STRING);
			String objTenNguoi = cellTenNguoi.getStringCellValue();
			log.info(objTenNguoi.toLowerCase());
			if(objTenNguoi!=null){
				cellTenNguoi.setCellValue(objTenNguoi.replace("[$tennguoidung$]",getUserFullName().toUpperCase()));
			}
			//Cell ngày lập
			String ngayLap = DateUtils.getCurrentDayOfMonth() > 9 ? ""+DateUtils.getCurrentDayOfMonth() : ("0" + DateUtils.getCurrentDayOfMonth());
			ngayLap ="ngày "+ ngayLap + " tháng " + (DateUtils.getCurrentMonth() > 9 ? DateUtils.getCurrentMonth(): ("0" + DateUtils.getCurrentMonth()));
			ngayLap = ngayLap + " năm " +  DateUtils.getCurrentYear();
			
			Row rowNgayLap = sheet.getRow(5);
			Cell cellNgayLap = rowNgayLap.getCell(0);
			cellNgayLap.setCellType(Cell.CELL_TYPE_STRING);
			String objNgayLap = cellNgayLap.getStringCellValue();
			if(objNgayLap!=null && objNgayLap.length()>0){
				cellNgayLap.setCellValue(objNgayLap.toString().replace("[$thoigian$]", ngayLap));
			}
			
			//Tổng số văn bản
			//Tổng số văn bản
			Row rowTongSoVanBan = sheet.getRow(6);
			Cell cellTongSoVanBan = rowTongSoVanBan.getCell(0);
			cellTongSoVanBan.setCellType(Cell.CELL_TYPE_STRING);
			String objTongSoVanBan = cellTongSoVanBan.getStringCellValue();
			if(objTongSoVanBan!=null &&objTongSoVanBan.length()>0){
				cellTongSoVanBan.setCellValue(objTongSoVanBan.replace("[$tongsovanban$]", "Tổng số văn bản: "+listThongTinVanBanDen.size()));
			}
			
			//Xuất danh sách công việc
			int rowIndex = 8; //Row bắt đầu điền thông tin
			int totalCol = 7;
			if(listThongTinVanBanDen!=null && listThongTinVanBanDen.size()>0){ 
				int totalRow = listThongTinVanBanDen.size();
				//Di chuyển footer xuống để chèn số liệu
				int lastRow = sheet.getLastRowNum();
				sheet.shiftRows(rowIndex + 1, lastRow, totalRow);  
				
				int currentRow = 0;			
				for (Map map: listThongTinVanBanDen) {
					int currentCol = 0;
					Row rowCV = sheet.createRow(rowIndex + currentRow);					
					//STT
					Cell cellStt = rowCV.createCell(currentCol);
					cellStt.setCellValue(currentRow+1);
					cellStt.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, false));
					currentCol++;
					
					//Số hiệu gốc
					Cell cellSohieu = rowCV.createCell(currentCol);
					cellSohieu.setCellValue(map.get("vbden_sohieugoc").toString());
					cellSohieu.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, false));
					currentCol++;
					
					//Số đến
					Cell cellSoDen = rowCV.createCell(currentCol);
					cellSoDen.setCellValue(map.get("vbden_soden").toString());
					cellSoDen.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, false));
					currentCol++;
					
					//Trichyeu
					Cell cellTrichYeu = rowCV.createCell(currentCol);
					cellTrichYeu.setCellValue(map.get("vbden_trichyeu").toString());
					cellTrichYeu.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, true));
					currentCol++;
					
					//Đơn vị
					Cell cellDonVi = rowCV.createCell(currentCol);
					cellDonVi.setCellValue(map.get("donvixuly").toString());
					cellDonVi.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, true));
					currentCol++;
					
					//Ngày đến
					Cell cellNgayDen = rowCV.createCell(currentCol);
					if(map.get("vbden_ngayden")!=null){
						cellNgayDen.setCellValue(dfm.format((Date)map.get("vbden_ngayden")));
					}
					else{
						cellNgayDen.setCellValue("");
					}				
					cellNgayDen.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, false));
					currentCol++;
					
					//Ngày chuyển đến
					Cell cellNgayChuyenDen = rowCV.createCell(currentCol);
					if(map.get("vbden_lc_ngaychuyen")!=null){
						cellNgayChuyenDen.setCellValue(dfm.format((Date)map.get("vbden_lc_ngaychuyen")));
					}
					else {
						cellNgayChuyenDen.setCellValue("");
					}
					cellNgayChuyenDen.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, false));
					currentCol++;
					
					String trangthai = "";			
					if(map.get("color").toString().compareTo("vColor_chuaxuly_conhan")==0){
						trangthai="Chưa xử lý còn hạn";
					}
					else if(map.get("color").toString().compareTo("vColor_chuaxuly_trehan")==0){
						trangthai ="Chưa xử lý trễ hạn";
					}
					else if(map.get("color").toString().compareTo("vColor_chuaxuly_khongthoihan")==0){
						trangthai ="Chưa xử lý không thời hạn";
					}
					else if(map.get("color").toString().compareTo("vColor_daxuly")==0){
						trangthai ="Đã xử lý";
					}
					//Trạng thái
					Cell cellTrangThai = rowCV.createCell(currentCol);
					cellTrangThai.setCellValue(trangthai);
					cellTrangThai.setCellStyle(excelFormatUtil.getCellStyle(totalRow, currentRow, totalCol, currentCol, wb, true));
					currentCol++;					
					currentRow++;
				}
			}
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			//wb.setSheetName(0, "DsGiaoViec");
			wb.write(output);		    
			log.info("createFileExcel thành công");
			ByteArrayResource res = new ByteArrayResource(output.toByteArray());
			output.close();
			return res;
		} catch(Exception ex){
			log.info("Lỗi createFileExcel");
			ex.printStackTrace();
			return null;
		}				
	}
	public void actionThayDoiSoVanBan(AjaxBehaviorEvent ae){
		if( props.getObjVBDenModel().getSvb_id() != null ){
			int svbId = props.getObjVBDenModel().getSvb_id();
			int soDen =  objVBDenFacade.getMaxSoDenTheoSoVanBan(svbId);
			if(soDen>99999){
				props.getObjVBDenModel().setSoKeTiep("");
				Validator.showErrorMessage(getPortletId(), "frm:txtSoDen", "Số đến không thể vượt quá 99999.\nHiệu chỉnh số nhỏ hơn hoặc chọn sổ văn bản khác");
				props.setSuaSoDen(true);
			}else{
				props.getObjVBDenModel().setSoKeTiep(objVBDenFacade.getMaxSoDenTheoSoVanBan(svbId)+"");
				props.setSuaSoDen(false);
			}
		
			props.getObjVBDenModel().setChuoiMacDinh(objDmSoVanBanFacade.getChuoiMacDinh(svbId));
			props.getObjVBDenModel().setViTri(objDmSoVanBanFacade.getViTriStt(svbId));
		}
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Lấy id phát hành văn bản đi theo số phát hành
	 * @date    Oct 17, 2016 :: 9:11:54 AM 
	 * @param soPhatHanh
	 * @return
	 */
	public void getIdPhVbDiBySoHieuGoc(){
		try{		
			props.getObjVBDenModel().setVbden_phucdap(
					pmGetIdPhVbDiBySoHieuGoc(props.getObjVBDenModel().getVbden_phucdap()));	
		} catch( Exception ex ){
			log.error("Err ", ex); 
		}
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Lấy id phát hành văn bản đi theo số phát hành
	 * @date    Oct 17, 2016 :: 9:11:54 AM 
	 * @param soPhatHanh
	 * @return
	 */
	private String pmGetIdPhVbDiBySoHieuGoc(Object vbden_phucdap){
		try{
			if( com.cusc.hcdt.util.StringUtils.isNullOrEmpty(vbden_phucdap) ){
				return "";
			}
			
			String escapeLink = CommonUtils.escapeLinkVblq(vbden_phucdap.toString());			
			return objVBDenFacade.getLinkTagsPhVbDi(escapeLink, getOrganizationId(), getUserId());			
		} catch( Exception ex ){
			log.error("Err ", ex); 
		}
		return "";
	}
	
	public void actionChonLoaiPhoBien(){
		try{	
			if(props.getLoaiPhoBien()==IContanst.PVPB_NGUOINHAN){
				pmGetDsPhoBien();
				props.setTxtDsEmailNhanPhoBien("");
			} else{
				actionLoadDsEmailPhoBien();
			}
		} catch(Exception ex){
			log.error("Err actionChonLoaiPhoBien ", ex); 
		}
	}
	
	public void actionLoadDsEmailPhoBien(){
		log.info(">>actionLoadDsEmailPhoBien {}", props.getLoaiPhoBien());
		try{		
			if(props.getLoaiPhoBien()==IContanst.PVPB_TATCA){				
				props.setTxtDsEmailNhanPhoBien( UserUtil.getChuoiEmailNguoiDungTheoCompany(getCompanyId()) );			
			} else if(props.getLoaiPhoBien()==IContanst.PVPB_NOIBO){		
				////////////////////////nduonggggggggggggggggggggggggg
				
				List<Map<String,Object>> list = objUserUtil.getListMapNguoiDungTheoDonVi(getOrganizationId());
				props.setListPhoBien(list);
				props.setTxtDsEmailNhanPhoBien( pmGetDsEmail(list) );	 			
			} else if(props.getLoaiPhoBien()==IContanst.PVPB_NGUOINHAN){
				String txtdsEmail = "";
				for(Map<String,Object> map: props.getListPhoBien()){
					if((Boolean) map.get("chon")){
						txtdsEmail += ","+map.get("email").toString();
					}
				}
				txtdsEmail = ((!txtdsEmail.equals(""))?txtdsEmail.substring(1):"");
				props.setTxtDsEmailNhanPhoBien(txtdsEmail);
			} else{
				//Không phổ biến
				props.setTxtDsEmailNhanPhoBien("");
			}
		} catch(Exception ex){
			log.error("Err loadDsEmailPhoBien ", ex); 
		}	
	}
	
	public void clearMessage(){
		JSFformUtil.resetInputForm(getPortletId()+":frm");
	}
	
	public void actionSuaSoDen(AjaxBehaviorEvent e){
		props.setSuaSoDen(true);
	}
	
	public void actionChonNguoiXuLy(){
		log.info(">>actionChonNguoiXuLy");
		props.setTxtDsEmailNhanXuLy(getListNguoiNhanXuLy());
	}
	
	//@LTBAO
	//=============== Văn bản liên đơn vị =======================
	boolean dakhoitao = false;
	private void khoiTaoDanhSachNoiNhanLienDonVi(){
		if (dakhoitao) {
			return;
		}
		dakhoitao = true;
		try{
			props.setListDonViNhanLienDonVi(objUserUtil.getListMapDonVi(getCompanyId()));
			String stringDonVi = "";
			if(props.getListNoiNhanLienDonVi()!=null){
				for(DmVanBanLienDonViModel model : props.getListNoiNhanLienDonVi()){
					if( model.getLdv_noinhan_organizationid() == null ||
							(model.getKqxlModel() != null && model.getKqxlModel().getKqxl_loaidonvi() == IContanst.LDV_NGOAIHT) ){
						continue;
					}
					stringDonVi +=",@"+model.getLdv_noinhan_organizationid()+"@";
					model.setTenDonVi(OrganizationLocalServiceUtil.getOrganization(model.getLdv_noinhan_organizationid()).getName());
					model.setTenNguoiGui(UserUtil.getUserFullName(model.getLdv_nguoigui_userid()));
					if(!objVBDenFacade.kiemTraTiepNhanLienDonVi(vbden_id, model.getLdv_noinhan_organizationid())){
						model.setCoTheXoa(false);
					}
				}
			}		
			log.info("Org vb" + getOrganizationIdVb());
			if(props.getListDonViNhanLienDonVi() != null){			
				int lsSize = props.getListDonViNhanLienDonVi().size();
				for(int i = 0 ; i < lsSize; i++){
					if(Long.parseLong(props.getListDonViNhanLienDonVi().get(i).get("organizationid").toString())==getOrganizationIdVb() 
							|| stringDonVi.contains("@"+props.getListDonViNhanLienDonVi().get(i).get("organizationid").toString()+"@")){
						props.getListDonViNhanLienDonVi().remove(i);
						i--;
						lsSize--;
					}
				}				
			}
			pmSortNoiNhanLienDonVi(props.getListNoiNhanLienDonVi());
		} catch(Exception e){
			e.printStackTrace();
			log.info(e.toString());
		}
	}
	
	/**
	 * @author  ltbao
	 * @throws SystemException 
	 * @throws PortalException 
	 * @purpose Load thông tin đơn vị nhận văn bản
	 * @date    Aug 9, 2014 :: 5:15:40 PM
	 */
	private void loadTabVanBanLienDonVi() throws PortalException, SystemException{
		props.setListThemNoiNhanLienDonVi(new ArrayList<DmVanBanLienDonViModel>());
		props.setListXoaNoiNhanLienDonVi(new ArrayList<DmVanBanLienDonViModel>());
		props.setListNoiNhanLienDonVi(objBanLienDonViFacade.getDanhSachNoiNhanTheoVanBan(vbden_id, 1));
		khoiTaoDanhSachNoiNhanLienDonVi();		
	}
	
	public void preActionChonNoiNhanLienDonVi(AjaxBehaviorEvent evt){
		props.setNoiDungChuyenLienDonVi("Chuyển đến các đơn vị thực hiện theo bút phê của Lãnh đạo");
		khoiTaoDanhSachNoiNhanLienDonVi();
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogNoiNhanLienDonVi.show()");
	}
	
	public void actionChonNoiNhanLienDonVi(AjaxBehaviorEvent evt){
		try{
			Boolean validate  = true;
			if(props.getNoiDungChuyenLienDonVi().trim().compareTo("")==0){
				validate = false;
				Validator.showErrorMessage(getPortletId(), "frmChonNoiNhanLienDonVi:txtNoiDungChuyen", "Vui lòng nhập nội dung");
			}
			boolean coNoiNhan = false;
			for(Map<String,Object> map:props.getListDonViNhanLienDonVi()){				
				if(Boolean.parseBoolean(map.get("chon").toString())){
					coNoiNhan = true;
					break;
				}
			}			
			if(props.getListDonViNhanLienDonVi()==null||props.getListDonViNhanLienDonVi().size()==0){
				Validator.showErrorMessage(getPortletId(), "frmChonNoiNhanLienDonVi:txtDanhSachNoiNhan", "Không có đơn vị để chọn");
				validate = false;
			}
			else if(!coNoiNhan){
				Validator.showErrorMessage(getPortletId(), "frmChonNoiNhanLienDonVi:txtDanhSachNoiNhan", "Vui lòng chọn nơi nhận");
				validate = false;
			}
			if(coNoiNhan&&validate){
				int lsSize = props.getListDonViNhanLienDonVi().size();
				for(int i = 0 ; i < lsSize;i++){
					if(Boolean.parseBoolean(props.getListDonViNhanLienDonVi().get(i).get("chon").toString())){
						DmVanBanLienDonViModel model = new DmVanBanLienDonViModel();
						model.setLdv_ngaygui(Calendar.getInstance().getTime());
						model.setLdv_nguoigui_userid(getUserId());
						model.setLdv_nhomvb(1);
						model.setLdv_noidung(props.getNoiDungChuyenLienDonVi());
						model.setLdv_noinhan_organizationid(Long.parseLong(props.getListDonViNhanLienDonVi().get(i).get("organizationid").toString()));
						model.setLdv_vb_id(vbden_id);
						model.setTenDonVi(props.getListDonViNhanLienDonVi().get(i).get("name").toString());
						model.setTenNguoiGui(objUserUtil.getUserFullName());
						model.setThemMoi(true);
						props.getListNoiNhanLienDonVi().add(model);
						props.getListThemNoiNhanLienDonVi().add(model);
						props.getListDonViNhanLienDonVi().remove(i);
						i--;
						lsSize--;
					}
				}
				
				//Gửi thông báo đến người nhận
				pmSortNoiNhanLienDonVi(props.getListNoiNhanLienDonVi()); 
			}
			if(validate){
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogNoiNhanLienDonVi.hide()");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void actionHuyBo( AjaxBehaviorEvent evt){
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogNoiNhanLienDonVi.hide()");
	}
	
	public void xoaNoiNhanLienDonVi(AjaxBehaviorEvent evt){
		Object obj = evt.getComponent().getAttributes().get("obj");
		if(obj!=null){
			DmVanBanLienDonViModel model = (DmVanBanLienDonViModel)obj;
			if(model.getThemMoi()){
				if(props.getListNoiNhanLienDonVi()!=null){
					for(DmVanBanLienDonViModel ldv : props.getListNoiNhanLienDonVi()){
						if(model.getLdv_noinhan_organizationid().equals(ldv.getLdv_noinhan_organizationid())){
							props.getListNoiNhanLienDonVi().remove(ldv);
							break;
						}
					}
				}
				if(props.getListThemNoiNhanLienDonVi()!=null){
					for(DmVanBanLienDonViModel ldv : props.getListThemNoiNhanLienDonVi()){
						if(model.getLdv_noinhan_organizationid().equals(ldv.getLdv_noinhan_organizationid())){
							props.getListThemNoiNhanLienDonVi().remove(ldv);
							break;
						}
					}
				}
			}else{
				boolean isPass = true;			
				if( objBanLienDonViFacade.getObjLienDonViModel(model.getLdv_id()) == null ){//Đơn vị không tồn tại
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Đơn vị không tồn tại, vui lòng thử lại',4)");
					isPass = false;
				} else if( !objVBDenFacade.kiemTraTiepNhanLienDonVi(model.getLdv_vb_id(), model.getLdv_noinhan_organizationid()) 
						&& model.getKqxlModel().getKqxl_trangthai()!=IContanst.DA_XOA && model.getKqxlModel().getKqxl_trangthai()!=IContanst.KHONG_TIEP_NHAN ){//xóa và không tiếp nhận thì bỏ qua (Cho phép xóa)
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Văn bản đã được đơn vị này tiếp nhận, không thể xóa',4)");
					isPass = false;
				}
				
				if( !isPass ){				
					try {
						loadTabVanBanLienDonVi();
					} catch ( Exception e) {
						e.printStackTrace();
					}
					model.setCoTheXoa(false);
					return;
				}
				
				
				if(objVBDenFacade.kiemTraTiepNhanLienDonVi(model.getLdv_vb_id(), model.getLdv_noinhan_organizationid())//Chưa tiếp nhận 
					|| model.getKqxlModel().getKqxl_trangthai()==IContanst.DA_XOA//Xóa 
					|| model.getKqxlModel().getKqxl_trangthai()==IContanst.KHONG_TIEP_NHAN//Không tiếp nhận
					|| model.getLdv_id()==null){
					props.getListNoiNhanLienDonVi().remove(model);
					if(props.getListThemNoiNhanLienDonVi()!=null){
						props.getListThemNoiNhanLienDonVi().remove(model);
					}
					if(model.getLdv_id() != null){
						props.getListXoaNoiNhanLienDonVi().add(model);
					}
					for(Map<String,Object> map:props.getListDonViNhanLienDonVi()){
						if(Long.parseLong(map.get("organizationid").toString())==model.getLdv_noinhan_organizationid()){
							map.put("chon",false);
							map.put("disable", false);
							break;
						}
					}
				}
			}
		}
		
	}
	
	public void actionCapNhatNoiNhanLienDonVi(AjaxBehaviorEvent evt){
		try {
			log.info(">>actionCapNhatNoiNhanLienDonVi");
			if(props.getListThemNoiNhanLienDonVi().size()==0 && props.getListXoaNoiNhanLienDonVi().size()==0){
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('"+CommonUtils.getBundleValue("vTbCapNhatThanhCong")+"',3);");
			} else{
				objBanLienDonViFacade.capNhatNoiNhanVanBan(props.getListThemNoiNhanLienDonVi(), 
						props.getListXoaNoiNhanLienDonVi(), objVBDenFacade.getObjVanBanDen(vbden_id), getOrgNotification(), orgTen);
				
				loadTabVanBanLienDonVi();
				
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('"+CommonUtils.getBundleValue("vTbCapNhatThanhCong")+"',3);");
			}
		} catch (Exception ex) {
			log.error("err ", ex);
		}
	}
	
	public void actionGuiLaiNoiNhanLienDonVi(AjaxBehaviorEvent evt){
		try {
			log.info(">>actionGuiLaiNoiNhanLienDonVi");
			Object obj = evt.getComponent().getAttributes().get("obj");
			if(obj!=null){
				DmVanBanLienDonViModel ldv = (DmVanBanLienDonViModel)obj;
				
				List<DmVanBanLienDonViModel> dsLienDonVi = new ArrayList<DmVanBanLienDonViModel>();
				dsLienDonVi.add(ldv);
				
				objBanLienDonViFacade.capNhatNoiNhanVanBan(dsLienDonVi, null, objVBDenFacade.getObjVanBanDen(vbden_id), getOrganizationId(), orgTen);
				
				loadTabVanBanLienDonVi();
				
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('"+CommonUtils.getBundleValue("vTbCapNhatThanhCong")+"',3);");
			}
		} catch (Exception ex) {
			log.error("err ", ex);
		}
	}
		
	// @ptgiang
	//=============== Quản lý kết quả trả vế =======================

	public void preActionThemKqxl(int mode){
		try{
			props.setModeKqxl(IActionMode.ADD); 			
			props.setKetQuaXuLyModel(initKqxlModel(IContanst.LDV_NGOAIHT));			
			JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogKetQuaXuLy.show()");
		} catch( Exception ex ){
			log.error("Err preActionThemKqxl ", ex); 
		}
	}
	
	/**
	 * 
	 * @author  hltphat
	 * @purpose lưu kết quả xử lý
	 * @date    Dec 29, 2017 :: 8:19:56 AM 
	 * @param objLienDonVi
	 */
	public void luuKetQuaXuLyCungHeThong(long vb_id, String noidung){
		log.info(">>> luuKetQuaXuLy");
		try{
			KetQuaXuLyModel kqxlModel = new KetQuaXuLyModel();
			//Xác định mode cập nhật Kết quả xử lý
			kqxlModel.setKqxl_donvinhan_noidung(noidung);
			kqxlModel.setKqxl_ngaycapnhat(DateUtils.getCurrentDate());
			kqxlModel.setUserid_capnhat(props.getUserId());
			kqxlModel.setKqxl_nguoicapnhat(UserUtil.getUserFullName(kqxlModel.getUserid_capnhat()));
			objKetQuaXuLyFacade.luuKetQuaXuLyTheoIdVB(vb_id, kqxlModel);
		}catch(Exception ex){
			log.info("Err luuKetQuaXuLy {}", ex); 
		}
	}
	
	public void preActionSuaKqxl(ActionEvent ae){
		try{
			Object objLienDonVi = ae.getComponent().getAttributes().get("item");
			if( objLienDonVi != null ){
				DmVanBanLienDonViModel ldvModel = (DmVanBanLienDonViModel)objLienDonVi;
				idxEditKqxl = props.getListNoiNhanLienDonVi().indexOf(objLienDonVi);
				KetQuaXuLyModel kqxlModel = null;
				if( ldvModel.getKqxlModel() == null ){
					kqxlModel = initKqxlModel(IContanst.LDV_CUNGHT);
				} else{
					kqxlModel = pmGetKetQuaXuLy(ldvModel.getKqxlModel().getKqxl_id());
					if( kqxlModel == null ){
						JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Đơn vị không tồn tại',4);");
						loadTabVanBanLienDonVi();
						return;
					}
				}
				
				if( CollectionUtil.isNullOrEmpty( kqxlModel.getDsTapTinKqxlModel() ) ){
					kqxlModel.setDsTapTinKqxlModel(new ArrayList<KetQuaXuLyTapTinModel>());
				}
				
				//Xác định mode cập nhật Kết quả xử lý
				if( kqxlModel.getKqxl_loaidonvi() == IContanst.LDV_CUNGHT ){
					props.setModeKqxl(IActionMode.EDIT_CUNGHT); 
				} else{
					props.setModeKqxl(IActionMode.EDIT_NGOAIHT); 
				}
				
				props.setKetQuaXuLyModel(kqxlModel);			
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogKetQuaXuLy.show();");
			}
		} catch( Exception ex ){
			log.error("Err preActionSuaKqxl ", ex); 
		}
	}	
	
	public void actionCapNhatKqxl(){
		try{
			log.info("getModeKqxl {} idxEditKqxl {}", props.getModeKqxl(), idxEditKqxl); 
			log.info("getKetQuaXuLyModel {}", props.getKetQuaXuLyModel()); 
			if( isPassValidationKqxl() ){				
				DmVanBanLienDonViModel ldvModel= null;
				if(props.getModeKqxl() == IActionMode.ADD_CUNGHT || props.getModeKqxl() == IActionMode.ADD_NGOAIHT){
					ldvModel = new DmVanBanLienDonViModel();
				} else{
					ldvModel = props.getListNoiNhanLienDonVi().get(idxEditKqxl);
				}
				if( (props.getModeKqxl() == IActionMode.ADD_CUNGHT || props.getModeKqxl() == IActionMode.EDIT_CUNGHT) &&
						 !isExistsDvCungHt(ldvModel.getLdv_id()) ){
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogKetQuaXuLy.hide();");
					JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Đơn vị không tồn tại, vui lòng thử lại',4);");
					loadTabVanBanLienDonVi();
					return;
				}
				
				props.getKetQuaXuLyModel().setKqxl_ngaycapnhat(DateUtils.getCurrentDate());
				props.getKetQuaXuLyModel().setUserid_capnhat(props.getUserId());
				//props.getKetQuaXuLyModel().setKqxl_trangthai(IContanst.CHUA_XEM);
				props.getKetQuaXuLyModel().setKqxl_nguoicapnhat(UserUtil.getUserFullName(props.getKetQuaXuLyModel().getUserid_capnhat()));
				
				if( props.getModeKqxl() == IActionMode.ADD_CUNGHT || props.getModeKqxl() == IActionMode.EDIT_CUNGHT ){
					props.getKetQuaXuLyModel().setKqxl_donvinhan_organizationid(ldvModel.getLdv_noinhan_organizationid());
					props.getKetQuaXuLyModel().setKqxl_donvinhan_ten(objUserUtil.getOrganization(ldvModel.getLdv_noinhan_organizationid()).getName());					
				}
				
				objKetQuaXuLyFacade.capNhatKetQuaXuLy(props.getKetQuaXuLyModel());	
				
				//Lưu tập tin vật lý
				for(KetQuaXuLyTapTinModel tapTinModel : props.getKetQuaXuLyModel().getDsTapTinKqxlModel()){
					File fTmp = new File(props.getBasePath() + vTmpPath + tapTinModel.getTt_tenluutru());
					if( tapTinModel.isUploadnew() && fTmp.exists() ){
						File newFile = new File(props.getBasePath() + vPath + tapTinModel.getTt_tenluutru());
						fTmp.renameTo(newFile);
					}
					
					File fStorage = new File(props.getBasePath() + vPath + tapTinModel.getTt_tenluutru());
					if( tapTinModel.getTt_id() != null && tapTinModel.isDel() && fStorage.exists()){
						fStorage.delete();
					}
				}
				
				props.getKetQuaXuLyModel().setDsTapTinKqxlModel(objKetQuaXuLyTapTinFacade.getDsKetQuaXuLyTapTin(props.getKetQuaXuLyModel().getKqxl_id()));
				ldvModel.setKqxlModel(props.getKetQuaXuLyModel()); 
				
				if(props.getModeKqxl() == IActionMode.ADD_CUNGHT || props.getModeKqxl() == IActionMode.ADD_NGOAIHT){
					props.getListNoiNhanLienDonVi().add(ldvModel);
				} else{
					props.getListNoiNhanLienDonVi().set(idxEditKqxl, ldvModel);
				}
				pmSortNoiNhanLienDonVi(props.getListNoiNhanLienDonVi()); 
				
				props.setKetQuaXuLyModel(null);
				idxEditKqxl = -1;
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "dialogKetQuaXuLy.hide();");
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Cập nhật thành công',3);");
			}			
		} catch( Exception ex ){
			log.error("Err preAactionCapNhatKqxlctionThemKqxl ", ex); 
		}
	}
	
	public void actionXoaKqxl(ActionEvent ae){
		try{
			Object objLienDonVi = ae.getComponent().getAttributes().get("item");
			if( objLienDonVi != null ){
				DmVanBanLienDonViModel ldvModel = (DmVanBanLienDonViModel)objLienDonVi;
				objKetQuaXuLyFacade.xoaKetQuaXuLy(ldvModel.getKqxlModel());
				props.getListNoiNhanLienDonVi().remove(ldvModel);
				JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "thongBao('Xóa thành công',3);");
			}
		} catch( Exception ex ){
			log.error("Err actionXoaKqxl ", ex); 
		}
	}
	
	public void actionUploadFileKqxl(FileEntryEvent event) {
	    try{
			FileEntryResults fileResult =  ((FileEntry)event.getComponent()).getResults();				
			for(FileEntryResults.FileInfo fileInfo : fileResult.getFiles()){					
				File oldFile = new File(fileInfo.getFile().getAbsolutePath());
				if(fileInfo.getSize() == 0){
					fileInfo.updateStatus(new MaxFileSizeStatus("Không thể tải tập tin rỗng (0 Kb)"), false, true);
				}else if (CommonUtils.checkFileExt(fileInfo.getFileName(), 
		    		CommonUtils.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(), false, true);
					//checkValidate = false;
		    	} else if (fileInfo.getSize() > (Long.parseLong(loader.getParams().get("vKichThuocFile").toString().trim())*1024*1024) ) {
		    		fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
					//checkValidate = false;
				} else if (Boolean.parseBoolean(CommonUtils.getMimeTypeBundleValue("checkMimeType").toString()) == true &&
		    		CommonUtils.isValidMimeType(oldFile, CommonUtils.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false, true);
					//checkValidate = false;
				} else if(fileInfo.isSaved()){						
					String tenmoi = DigestUtils.md5Hex(new Random().nextInt(9999) + fileInfo.getFileName()) + "." + FileNameUtil.getFileNameExtension(fileInfo.getFileName());
					KetQuaXuLyTapTinModel fileObj = new KetQuaXuLyTapTinModel();
					fileObj.setTt_tenhienthi(fileInfo.getFileName());
					fileObj.setTt_tenluutru(tenmoi);
					fileObj.setTt_ngaytai(DateUtils.getCurrentDate());
					fileObj.setTt_type(fileInfo.getContentType());
					fileObj.setTt_size(fileInfo.getSize());
					fileObj.setUserid(getUserId()); 
					fileObj.setTt_nguoitai(getUserFullName());
					File f = new File(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTt_tenhienthi());
					if(f.exists()){
						File newFile =new File(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTt_tenluutru());
						f.renameTo(newFile);
						fileObj.setUploadnew(true);
						fileObj.setPath(CommonUtils.getBasePath()+loader.getParams().get("vTmpPath_vbden").toString()+fileObj.getTt_tenluutru());
						fileObj.setType(CommonUtils.getFileExtension(newFile));
						//ds sử dụng lưu tập tin
						props.getKetQuaXuLyModel().getDsTapTinKqxlModel().add(fileObj);
					}					
				}
			}
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	    JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "uploadBtn_show('fileKqxlUpload','tmpKqxlUpload');");	    
	}
	
	public void actionXoaFileKqxl(ActionEvent ae){
		Object obj = ae.getComponent().getAttributes().get("obj");
		if(obj != null){
			KetQuaXuLyTapTinModel tapTinKqxlModel = (KetQuaXuLyTapTinModel)obj;
			if( tapTinKqxlModel.isUploadnew() ){
				props.getKetQuaXuLyModel().getDsTapTinKqxlModel().remove(obj);
			} else{
				tapTinKqxlModel.setDel(true); 
			}
		}
	}
	
	private KetQuaXuLyModel initKqxlModel(int ldvId){		
		KetQuaXuLyModel kqxlModel = new KetQuaXuLyModel();
		kqxlModel.setKqxl_loaidonvi(ldvId);
		kqxlModel.setNvb_id(IContanst.NVB_DEN);
		kqxlModel.setVb_id(vbden_id);	
		
		kqxlModel.setDsTapTinKqxlModel(new ArrayList<KetQuaXuLyTapTinModel>()); 
		return kqxlModel;
	}
	
	private KetQuaXuLyModel pmGetKetQuaXuLy(long kqxlId){
		KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade.getObjKetQuaXuLy(kqxlId);
		if( kqxlModel != null ){
			kqxlModel.setDsTapTinKqxlModel(objKetQuaXuLyTapTinFacade.getDsKetQuaXuLyTapTin(kqxlId)); 
		}
		return kqxlModel;
	}
	
	private boolean isPassValidationKqxl(){
		boolean isPass = true;
		
		if( props.getModeKqxl() == IActionMode.ADD_NGOAIHT &&
				com.cusc.hcdt.util.StringUtils.isNullOrEmpty(props.getKetQuaXuLyModel().getKqxl_donvinhan_ten()) ){
			Validator.showErrorMessage(getPortletId(), "frmKetQuaXuLy:txtTenDonViNhan", "Vui lòng nhập đơn vị nhận");			
			isPass = false;
		}
		
		if( com.cusc.hcdt.util.StringUtils.isNullOrEmpty(props.getKetQuaXuLyModel().getKqxl_donvinhan_noidung()) ){
			Validator.showErrorMessage(getPortletId(), "frmKetQuaXuLy:txtNoiDungNhan", "Vui lòng nhập nội dung");			
			isPass = false;
		}
		
		return isPass;
	}
	
	private boolean isExistsDvCungHt(long lienDonViId){
		log.info("lienDonViId {}", lienDonViId);
		return objBanLienDonViFacade.getObjLienDonViModel(lienDonViId) != null;
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Sắp xếp danh sách theo ngày gửi DESC, tên đơn vị A-Z
	 * @date    Nov 4, 2016 :: 4:43:52 PM 
	 * @param dsNoiNhanLienDonVi
	 */
	private void pmSortNoiNhanLienDonVi(List<DmVanBanLienDonViModel> dsNoiNhanLienDonVi){
		
		if( CollectionUtil.isNullOrEmpty(dsNoiNhanLienDonVi) ){
			return;
		}
		
		Collections.sort(dsNoiNhanLienDonVi, new Comparator<DmVanBanLienDonViModel>() {  
		    @Override  
		    public int compare(DmVanBanLienDonViModel p1, DmVanBanLienDonViModel p2) {  
		    	int compareDate = DateUtils.compareDate(p1.getLdv_ngaygui(), p2.getLdv_ngaygui(), false);    	
		        if( compareDate == 0 ){
		        	String str1 = p1.getTenDonVi();
		        	String str2 = p2.getTenDonVi();
		        	
		        	if( p1.getLdv_id() == null && p1.getKqxlModel() != null ){
		        		str1 = p1.getKqxlModel().getKqxl_donvinhan_ten();
		        	}
		        	
		        	if( p2.getLdv_id() == null && p2.getKqxlModel() != null ){
		        		str2 = p2.getKqxlModel().getKqxl_donvinhan_ten();
		        	}
		        	
		        	return com.cusc.hcdt.util.StringUtils.compareStringUnicode(str1, str2); 
		        } else{
		        	return compareDate;
		        }  
		    }  
		});  
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Lấy danh sách user chuyển xử lý
	 * @date    Dec 13, 2016 :: 8:39:58 AM
	 */
	private void pmGetDsChuyenXuLy(){
		props.setListChuyenXuLy( pmGetAllUserNhanXuLy() );
		
		if( props.getListChuyenXuLy() == null ){
			props.setListChuyenXuLy( new ArrayList<Map<String,Object>>() );
		}
		
		//Khởi tạo lại danh sách bộ lọc đơn vị
		pmCreateFilterDonViNhan();
		vFilterDonVi = null;
		actionLocDonVi();
	}
	
	/**
	 * @author  ptgiang
	 * @purpose Xử lý sự kiện chọn lọc đơn vị -> Khởi tạo lại danh sách bộ lọc phòng ban
	 * @date    Dec 13, 2016 :: 8:58:47 AM
	 */
	public void actionLocDonVi(){	
		log.info(">>>actionLocDonVi");
		listFilterPhongBan = new ArrayList<SelectItem>();
		
		SelectItem noSelect = new SelectItem("", "--- Tất cả ---");
		noSelect.setNoSelectionOption(true);
		listFilterPhongBan.add(noSelect);
		
		List<Organization> listPhongBan = null;
		if(vFilterDonVi != null && vFilterDonVi.compareTo("") != 0){
			listPhongBan = objUserUtil.getDsPhongBanTheoDonVi(Long.parseLong(vFilterDonVi));
		} else{
			listPhongBan = objUserUtil.getListPhongBan(objUserUtil.getCompanyId());
		}
		
		if( listPhongBan != null ){
			for(Organization org : listPhongBan){
				listFilterPhongBan.add(new SelectItem(org.getOrganizationId(), org.getName()));
			}	
		}
	}	
	
	private void pmGetDsPhoBien() {
		props.setListPhoBien( pmGetAllUserNhanPhoBien() );
		if( props.getListPhoBien() == null ){
			props.setListPhoBien( new ArrayList<Map<String,Object>>() );
		}
		
		//Khởi tạo lại danh sách bộ lọc đơn vị
		pmCreateFilterDonViNhan();
		vFilterDonVi = null;
		actionLocDonVi();
	}
	
	private List<Map<String,Object>> pmGetAllUserNhanXuLy(){
		if(listAllUserNhanXuLy == null){
			//listAllUserNhanXuLy = objUserUtil.getListMapChuyenXuLy(IContanst.SELECT_ALL, props.getCompanyId());
			listAllUserNhanXuLy = objUserUtil.getFullListMapChuyenXuLy(props.getCompanyId());
		}
		pmResetDefaultValue(listAllUserNhanXuLy);
		return listAllUserNhanXuLy; 
	}
	
	private List<Map<String,Object>> pmGetAllUserNhanPhoBien(){
		if(listAllUserNhanPhoBien == null){
			if(listAllUserNhanXuLy == null){
			listAllUserNhanPhoBien = objUserUtil.getFullListMapChuyenXuLy(props.getCompanyId());
			}else{
				listAllUserNhanPhoBien = listAllUserNhanXuLy;
			}
		}
		pmResetDefaultValue(listAllUserNhanPhoBien);
		return listAllUserNhanPhoBien;
	}
	
	private void pmResetDefaultValue(List<Map<String,Object>> list){
		if(list == null || list.isEmpty()){
			return;
		}
		
		for(Map<String, Object> map : list){
			map.put("chon", false);
			map.put("checked", false);
			map.put("xlchinh", false);
		}
	}
	
	private void pmCreateFilterDonViNhan(){
		if( CollectionUtil.isNullOrEmpty(listFilterDonVi)  ){			
			//Khởi tạo lại danh sách bộ lọc đơn vị
			listFilterDonVi = new ArrayList<SelectItem>();
			
			SelectItem noSelect = new SelectItem("","--- Tất cả ---");
			noSelect.setNoSelectionOption(true);
			listFilterDonVi.add(noSelect);
			
			List<Organization> listOrg = objUserUtil.getListDonVi(props.getCompanyId());			
			if( listOrg != null ){
				for(Organization org : listOrg){
					listFilterDonVi.add(new SelectItem(org.getOrganizationId(), org.getName()));
				}				
			}
		}
	}
	
	public String test() {
		Gson son = new Gson();
		String json = son.toJson(objUserUtil.getFullListMapChuyenXuLy(props.getCompanyId()));
		log.info("JSON: " + json);
		return json;
	}
	
	//======================= GETTER AND SETTER =======================
	
	public VBDen_XuLyProps getProps() {
		return props;
	}
	
	public void setProps(VBDen_XuLyProps props) {
		this.props = props;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<SelectItem> getListFilterPhongBan() {
		return listFilterPhongBan;
	}

	public void setListFilterPhongBan(List<SelectItem> listFilterPhongBan) {
		this.listFilterPhongBan = listFilterPhongBan;
	}

	public HcdtThongSoLoader getLoader() {
		return loader;
	}

	public void setLoader(HcdtThongSoLoader loader) {
		this.loader = loader;
	}

	public boolean isQuyenGopY() {
		return quyenGopY;
	}

	public void setQuyenGopY(boolean quyenGopY) {
		this.quyenGopY = quyenGopY;
	}

	public boolean isQuyenNhapButPhe() {
		return quyenNhapButPhe;
	}

	public void setQuyenNhapButPhe(boolean quyenNhapButPhe) {
		this.quyenNhapButPhe = quyenNhapButPhe;
	}

	public boolean isQuyenLuuTru() {
		return quyenLuuTru;
	}

	public void setQuyenLuuTru(boolean quyenLuuTru) {
		this.quyenLuuTru = quyenLuuTru;
	}

	public boolean isQuyenXoa() {
		return quyenXoa;
	}

	public void setQuyenXoa(boolean quyenXoa) {
		this.quyenXoa = quyenXoa;
	}

	public boolean isCoQuyen() {
		return coQuyen;
	}

	public void setCoQuyen(boolean coQuyen) {
		this.coQuyen = coQuyen;
	}

	public boolean isQuyenVanThu() {
		return quyenVanThu;
	}

	public void setQuyenVanThu(boolean quyenVanThu) {
		this.quyenVanThu = quyenVanThu;
	}

	public boolean isQuyenxlchinh() {
		return quyenxlchinh;
	}

	public void setQuyenxlchinh(boolean quyenxlchinh) {
		this.quyenxlchinh = quyenxlchinh;
	}

	public boolean isQuyenphobien() {
		return quyenphobien;
	}

	public void setQuyenphobien(boolean quyenphobien) {
		this.quyenphobien = quyenphobien;
	}

	public DmDonViBean getPropsDonVi() {
		return propsDonVi;
	}

	public void setPropsDonVi(DmDonViBean propsDonVi) {
		this.propsDonVi = propsDonVi;
	}

	public LienKetVanBanBean getPropsLkvb() {
		return propsLkvb;
	}

	public void setPropsLkvb(LienKetVanBanBean propsLkvb) {
		this.propsLkvb = propsLkvb;
	}

	public HoSoVanBanPopupBean getPropsHsvb() {
		return propsHsvb;
	}

	public void setPropsHsvb(HoSoVanBanPopupBean propsHsvb) {
		this.propsHsvb = propsHsvb;
	}

	public DmLoaiVanBanBean getPropsLoaiVb() {
		return propsLoaiVb;
	}

	public void setPropsLoaiVb(DmLoaiVanBanBean propsLoaiVb) {
		this.propsLoaiVb = propsLoaiVb;
	}

	public DmLinhVucBean getPropsLinhVuc() {
		return propsLinhVuc;
	}

	public void setPropsLinhVuc(DmLinhVucBean propsLinhVuc) {
		this.propsLinhVuc = propsLinhVuc;
	}

	public DmSoVanBanPopupBean getPropsSoVanBan() {
		return propsSoVanBan;
	}

	public void setPropsSoVanBan(DmSoVanBanPopupBean propsSoVanBan) {
		this.propsSoVanBan = propsSoVanBan;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public List<SelectItem> getListFilterPhongBanNguoiDung() {
		return listFilterPhongBanNguoiDung;
	}

	public void setListFilterPhongBanNguoiDung(
			List<SelectItem> listFilterPhongBanNguoiDung) {
		this.listFilterPhongBanNguoiDung = listFilterPhongBanNguoiDung;
	}

	public long getPhongban_org() {
		return phongban_org;
	}

	public void setPhongban_org(long phongban_org) {
		this.phongban_org = phongban_org;
	}

	public boolean isMailPhoBien() {
		return mailPhoBien;
	}

	public void setMailPhoBien(boolean mailPhoBien) {
		this.mailPhoBien = mailPhoBien;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public boolean isHientabxuly() {
		return hientabxuly;
	}

	public void setHientabxuly(boolean hientabxuly) {
		this.hientabxuly = hientabxuly;
	}

	public long getVbden_id() {
		return vbden_id;
	}

	public void setVbden_id(long vbden_id) {
		this.vbden_id = vbden_id;
	}

	public long getVbden_lc_id() {
		return vbden_lc_id;
	}

	public void setVbden_lc_id(long vbden_lc_id) {
		this.vbden_lc_id = vbden_lc_id;
	}

	public String getvTmpPathLc() {
		return vTmpPathLc;
	}

	public void setvTmpPathLc(String vTmpPathLc) {
		this.vTmpPathLc = vTmpPathLc;
	}

	public String getvPathLc() {
		return vPathLc;
	}

	public void setvPathLc(String vPathLc) {
		this.vPathLc = vPathLc;
	}

	public String getvTmpPathGy() {
		return vTmpPathGy;
	}

	public void setvTmpPathGy(String vTmpPathGy) {
		this.vTmpPathGy = vTmpPathGy;
	}

	public String getvPathGy() {
		return vPathGy;
	}

	public void setvPathGy(String vPathGy) {
		this.vPathGy = vPathGy;
	}

	public String getvTmpPath() {
		return vTmpPath;
	}

	public void setvTmpPath(String vTmpPath) {
		this.vTmpPath = vTmpPath;
	}

	public String getvPath() {
		return vPath;
	}

	public void setvPath(String vPath) {
		this.vPath = vPath;
	}

	public boolean isSuaHanXlToanVb() {
		return suaHanXlToanVb;
	}

	public void setSuaHanXlToanVb(boolean suaHanXlToanVb) {
		this.suaHanXlToanVb = suaHanXlToanVb;
	}

	public boolean isQuyenSuaVanBan() {
		return quyenSuaVanBan;
	}

	public void setQuyenSuaVanBan(boolean quyenSuaVanBan) {
		this.quyenSuaVanBan = quyenSuaVanBan;
	}

	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	public boolean isHienTabSet() {
		return hienTabSet;
	}

	public void setHienTabSet(boolean hienTabSet) {
		this.hienTabSet = hienTabSet;
	}

	public String getOrgTen() {
		return orgTen;
	}

	public void setOrgTen(String orgTen) {
		this.orgTen = orgTen;
	}

	public int getPhamViVanThu() {
		return phamViVanThu;
	}

	public void setPhamViVanThu(int phamViVanThu) {
		this.phamViVanThu = phamViVanThu;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isHienHoSoVanBan() {
		return hienHoSoVanBan;
	}

	public void setHienHoSoVanBan(boolean hienHoSoVanBan) {
		this.hienHoSoVanBan = hienHoSoVanBan;
	}

	public boolean isXuLyThay() {
		return xuLyThay;
	}

	public void setXuLyThay(boolean xuLyThay) {
		this.xuLyThay = xuLyThay;
	}

	public int getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(int tabSelect) {
		this.tabSelect = tabSelect;
	}

	public boolean isCoCauHinhCaNhan() {
		return coCauHinhCaNhan;
	}

	public void setCoCauHinhCaNhan(boolean coCauHinhCaNhan) {
		this.coCauHinhCaNhan = coCauHinhCaNhan;
	}

	public boolean isHienControlLienDonVi() {
		return hienControlLienDonVi;
	}

	public void setHienControlLienDonVi(boolean hienControlLienDonVi) {
		this.hienControlLienDonVi = hienControlLienDonVi;
	}

	public boolean isHienTabLienDonVi() {
		return hienTabLienDonVi;
	}

	public void setHienTabLienDonVi(boolean hienTabLienDonVi) {
		this.hienTabLienDonVi = hienTabLienDonVi;
	}

	public VanBanLogBean getVanBanLogBean() {
		return vanBanLogBean;
	}

	public void setVanBanLogBean(VanBanLogBean vanBanLogBean) {
		this.vanBanLogBean = vanBanLogBean;
	}

	public Map<String, Map<String, String>> getMapBasicUserInfo() {
		return mapBasicUserInfo;
	}

	public void setMapBasicUserInfo(Map<String, Map<String, String>> mapBasicUserInfo) {
		this.mapBasicUserInfo = mapBasicUserInfo;
	}

	public List<SelectItem> getListFilterDonVi() {
		return listFilterDonVi;
	}

	public void setListFilterDonVi(List<SelectItem> listFilterDonVi) {
		this.listFilterDonVi = listFilterDonVi;
	}

	public String getvFilterDonVi() {
		return vFilterDonVi;
	}

	public void setvFilterDonVi(String vFilterDonVi) {
		this.vFilterDonVi = vFilterDonVi;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	public List<Long> getListDvUser() {
		return listDvUser;
	}

	public void setListDvUser(List<Long> listDvUser) {
		this.listDvUser = listDvUser;
	}
	
	public Long getOrgIdTemp() {
		return orgIdTemp;
	}

	public void setOrgIdTemp(Long orgIdTemp) {
		this.orgIdTemp = orgIdTemp;
	}
	public List<Organization> getListDonVi() {
		return listDonVi;
	}

	public void setListDonVi(List<Organization> listDonVi) {
		this.listDonVi = listDonVi;
	}
	
	public List<SelectItem> getListFilterDonViTheoNguoiDung() {
		return listFilterDonViTheoNguoiDung;
	}

	public void setListFilterDonViTheoNguoiDung(
			List<SelectItem> listFilterDonViTheoNguoiDung) {
		this.listFilterDonViTheoNguoiDung = listFilterDonViTheoNguoiDung;
	}
	
	private Long orgNotification;
	public Long getOrgNotification() {
		return orgNotification;
	}

	public void setOrgNotification(Long orgNotification) {
		this.orgNotification = orgNotification;
	}
}
