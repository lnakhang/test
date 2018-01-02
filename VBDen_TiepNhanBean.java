/**
 *--------------------CREATE----------------------
 * @creator  : lqthai
 * @date     : Jan 20, 2014 9:42:58 PM
 * @filename : HcdtVanBanDenBean.java on project hcdt-portlet
 * @purpose  : 
 * @version  : v1.0
 * @copyright: 
 *---------------------EDIT-----------------------
 * @editor      : lqthai
 * @date        : 9:42:58 PM
 * @purpose     : 
 * @changedSign : (ex: //@nlphuong) 
 */
package com.cusc.hcdt.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.mail.EmailException;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
import com.cusc.hcdt.facade.KetQuaXuLyFacade;
import com.cusc.hcdt.facade.TapTinDinhKemVanBanFacade;
import com.cusc.hcdt.facade.VBDenFacade;
import com.cusc.hcdt.facade.VBDi_PhatHanhFacade;
import com.cusc.hcdt.facade.VanBanPhoiHopFacade;
import com.cusc.hcdt.model.ChuyenBoSungModel;
import com.cusc.hcdt.model.DmCapDonViModel;
import com.cusc.hcdt.model.DmDoKhanModel;
import com.cusc.hcdt.model.DmDoMatModel;
import com.cusc.hcdt.model.DmDonViModel;
import com.cusc.hcdt.model.DmLinhVucModel;
import com.cusc.hcdt.model.DmLoaiVanBanModel;
import com.cusc.hcdt.model.DmSoVanBanModel;
import com.cusc.hcdt.model.DmVanBanLienDonViModel;
import com.cusc.hcdt.model.KetQuaXuLyModel;
import com.cusc.hcdt.model.LienKetVanBanModel;
import com.cusc.hcdt.model.MailPhoBienModel;
import com.cusc.hcdt.model.TapTinDinhKemLuanChuyenModel;
import com.cusc.hcdt.model.TapTinDinhKemVanBanModel;
import com.cusc.hcdt.model.VBDenModel;
import com.cusc.hcdt.model.VBDen_LuanChuyenModel;
import com.cusc.hcdt.model.VBDi_PhatHanhModel;
import com.cusc.hcdt.model.VanBanPhoiHopModel;
import com.cusc.hcdt.props.VBDen_TiepNhanProps;
import com.cusc.hcdt.service.mail.Email;
import com.cusc.hcdt.service.mail.EmailProvider;
import com.cusc.hcdt.service.mail.EmailService;
import com.cusc.hcdt.util.CollectionUtil;
import com.cusc.hcdt.util.HCDTResource;
import com.cusc.hcdt.util.NumberUtils;
import com.cusc.hcdt.util.UserUtil;
import com.cusc.hcdt.util.Validator;
import com.cusc.hcdt.util.common.CommonUtils;
import com.cusc.hcdt.util.common.CommonUtils.Hcdt;
import com.cusc.hcdt.util.jsf.JavascriptUtils;
import com.cusc.hcdt.util.common.DateUtils;
import com.cusc.hcdt.util.common.IContanst;
import com.cusc.hcdt.util.permission.HcdtRoles;
import com.cusc.hcdt.util.permission.HcdtRoles.HcdtRole;
import com.cusc.hcdt.util.uploadFile.CheckFileExtensionStatus;
import com.cusc.hcdt.util.uploadFile.CheckMimeTypeStatus;
import com.cusc.hcdt.util.uploadFile.MaxFileSizeStatus;
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

import org.apache.commons.lang3.StringUtils;

/**
 * @author lqthai
 * @date Jan 20, 2014
 * @purpose
 */
@ManagedBean(name = "VBDen_TiepNhanBean")
@ViewScoped
public class VBDen_TiepNhanBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 3951212823883797363L;
	private static final Logger _log = LoggerFactory
			.getLogger(VBDen_TiepNhanBean.class);
	private static final String BEANNAME = "VBDen_TiepNhanBean";
	@ManagedProperty(value = "#{HcdtThongSoLoader}")
	private HcdtThongSoLoader loader = FacesContext
			.getCurrentInstance()
			.getApplication()
			.evaluateExpressionGet(FacesContext.getCurrentInstance(),
					"#{HcdtThongSoLoader}", HcdtThongSoLoader.class);;
	@ManagedProperty(value = "#{VBDen_TiepNhanProps}")
	private VBDen_TiepNhanProps props;
	@ManagedProperty(value = "#{DmDonViBean}")
	private DmDonViBean propsDonVi;
	@ManagedProperty(value = "#{LienKetVanBanBean}")
	private LienKetVanBanBean propsLkvb;
	@ManagedProperty(value = "#{HoSoVanBanPopupBean}")
	private HoSoVanBanPopupBean propsHsvb;
	@ManagedProperty(value = "#{DmLoaiVanBanBean}")
	private DmLoaiVanBanBean propsLoaiVb;
	@ManagedProperty(value = "#{DmLinhVucBean}")
	private DmLinhVucBean propsLinhVuc;
	@ManagedProperty(value = "#{DmCachThucXuLyBean}")
	private DmCachThucXuLyBean propsCachThucXuLy;
	@ManagedProperty(value = "#{DmSoVanBanPopupBean}")
	private DmSoVanBanPopupBean propsSoVanBan;
	@ManagedProperty(value = "#{RecognizeBean}")
	private RecognizeBean ocrBean;
	// FACADE
	private DmDoKhanFacade objDmDoKhanFacade;
	private DmDoMatFacade objDmDoMatFacade;
	private DmLinhVucFacade objDmLinhVucFacade;
	private DmLoaiVanBanFacade objDmLoaiVanBanFacade;
	private DmSoVanBanFacade objDmSoVanBanFacade;
	private DmDonViFacade objDmDonViFacade;
	private DmCapDonViFacade objDmCapDonViFacade;;
	private DmLinkFacade objDmLinkFacade;
	private VBDi_PhatHanhFacade objVBDi_PhatHanhFacade;
	// private DmCachThucXuLyFacade objDmCachThucXuLyFacade = new
	// DmCachThucXuLyFacade();
	// private DmVanBanLienDonViFacade objDmVanBanLienDonViFacade = new
	// DmVanBanLienDonViFacade();
	private KetQuaXuLyFacade objKetQuaXuLyFacade;
	private VanBanPhoiHopFacade objVanBanPhoiHopFacade;
	private DmCachThucXuLyFacade objDmCachThucXuLyFacade;
	private TapTinDinhKemVanBanFacade objTapTinDinhKemVanBanFacade;
	private DmVanBanLienDonViFacade objBanLienDonViFacade;

	private VBDenFacade objVBDenFacade;

	private UserUtil userUtil;
	private String fullName = "";
	private boolean coQuyen = false;
	private boolean coCauHinh = true;
	private long userId;
	private long companyid;
	private long orgId;
	private boolean checkHienThiSoDen = true;

//	private int modeChuyenTrang; //Kiểm tra trang có chuyễn sau khi thêm hay không mode = 1 là không chuyển trang
	private String orgName;
	private long phongbanId;
	private boolean butPhe = false;// Kiểm tra người dùng hiện tại có quyền thêm
									// bút phê k?
	// idVb + mode tạm sử dụng để giữ trạng thái khi chuyển tab.
	// Cùng hệ thống
	private long idTmp = 0;
	private int modeTmp = 0;
	// Văn bản scan
	private long idTmpScan = 0;
	private int modeTmpScan = 0;

	private long userIdNhanXl; // Người nhận xử lý mặc định theo cấu hình
	private long userIdXlChinh; // Biến tmp lưu id xlChinh
	private String dsMailNhanPhoBien;
	private String dsMailNhanXuLyPhoiHop = "";

	private String emailXuLyChinh = "";
	private boolean canhBaoSoDen = false;
	//hltphat check So Den tu tang 1.Luu va tiep tuc 2. Luu
	private int checkSoDen;
	private Integer soden;
	// nduong
	private boolean runRefesh = true;

	private List<SelectItem> listFilterDonVi = new ArrayList<SelectItem>();
	private List<Organization> listDonVi = new ArrayList<Organization>();
	private List<Organization> donViCungHt = new ArrayList<Organization>();
	// biến dùng tạm khi không load theo cấu hình sẽ gáng cho orgId khi thay đổi
	// value
	private long orgIdTemp = 0;
	private List<Long> listDvUser = new ArrayList<Long>();

	private String butPheHienTai = null;
	private List<Map<String, Object>> listAllUserNhanXuLy = null;
	private List<Map<String, Object>> listAllUserNhanPhoBien = null;

	public VBDen_TiepNhanBean() throws PortalException, SystemException {
		_log.info("-----------BEANNAME--------------{}", BEANNAME);

		objDmDoKhanFacade = new DmDoKhanFacade();
		objDmDoMatFacade = new DmDoMatFacade();
		objDmLinhVucFacade = new DmLinhVucFacade();
		objDmLoaiVanBanFacade = new DmLoaiVanBanFacade();
		objDmSoVanBanFacade = new DmSoVanBanFacade();
		objDmDonViFacade = new DmDonViFacade();
		objDmCapDonViFacade = new DmCapDonViFacade();
		objDmLinkFacade = new DmLinkFacade();
		objVBDi_PhatHanhFacade = new VBDi_PhatHanhFacade();
		objDmCachThucXuLyFacade = new DmCachThucXuLyFacade();
		objKetQuaXuLyFacade = new KetQuaXuLyFacade();
		objVanBanPhoiHopFacade = new VanBanPhoiHopFacade();

		objTapTinDinhKemVanBanFacade = new TapTinDinhKemVanBanFacade();
		objBanLienDonViFacade = new DmVanBanLienDonViFacade();

		objVBDenFacade = new VBDenFacade();
		userUtil = new UserUtil();

		props = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{VBDen_TiepNhanProps}", VBDen_TiepNhanProps.class);
		propsDonVi = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{DmDonViBean}", DmDonViBean.class);
		propsLkvb = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{LienKetVanBanBean}", LienKetVanBanBean.class);
		propsHsvb = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{HoSoVanBanPopupBean}", HoSoVanBanPopupBean.class);
		propsLoaiVb = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{DmLoaiVanBanBean}", DmLoaiVanBanBean.class);
		propsLinhVuc = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{DmLinhVucBean}", DmLinhVucBean.class);
		propsCachThucXuLy = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{DmCachThucXuLyBean}", DmCachThucXuLyBean.class);
		propsSoVanBan = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{DmSoVanBanPopupBean}", DmSoVanBanPopupBean.class);
		ocrBean = FacesContext
				.getCurrentInstance()
				.getApplication()
				.evaluateExpressionGet(FacesContext.getCurrentInstance(),
						"#{RecognizeBean}", RecognizeBean.class);

		coQuyen = HcdtRoles.userHasRole(HcdtRole.CBVanThu);
		if (coQuyen) {
			if ((loader.getUserParams().get("vOrgId").toString()
					.equals("-1000"))
					|| (loader.getUserParams().get("vPhongBanId").toString()
							.equals("-1000"))) {
				coCauHinh = false;
			} else {

				// Thêm Item phòng ban
				// lnakhang: bổ sung kiểm tra nếu không có cha thì không get id

				List<Organization> lstOrg = getUser().getOrganizations();
				for (int i = 0; i < lstOrg.size(); i++) {
					// kiểm tra có phải đơn vị hay không
					if (userUtil.isDonVi(lstOrg.get(i))) {
						if (lstOrg.get(i).getParentOrganization() != null) {
							long idOrgan = lstOrg.get(i)
									.getParentOrganization()
									.getParentOrganizationId();
							if (idOrgan != 0) {
								listFilterDonVi
										.add(new SelectItem(
												lstOrg.get(i)
														.getOrganizationId(),
												checkOrganNull(userUtil
														.getOrganization(
																idOrgan)
														.getName())
														+ checkOrganNull(lstOrg
																.get(i)
																.getParentOrganization()
																.getName())
														+ lstOrg.get(i)
																.getName()));
								listDvUser.add(lstOrg.get(i)
										.getOrganizationId());
							} else {
								listFilterDonVi.add(new SelectItem(lstOrg
										.get(i).getOrganizationId(),
										checkOrganNull(lstOrg.get(i)
												.getParentOrganization()
												.getName())
												+ lstOrg.get(i).getName()));
								listDvUser.add(lstOrg.get(i)
										.getOrganizationId());
							}
						} else {
							listFilterDonVi.add(new SelectItem(lstOrg.get(i)
									.getOrganizationId(), lstOrg.get(i)
									.getName()));
							listDvUser.add(lstOrg.get(i).getOrganizationId());
						}
					}
				}
				butPheHienTai = null;
				fullName = getUserFullName();
				userId = getUserId();
				companyid = getCompanyId();
				setOrgId(Long.parseLong(loader.getUserParams().get("vOrgId")
						.toString()));
				propsHsvb.setOrgId(getOrgId());
				propsDonVi.setOrgId(getOrgId());
				phongbanId = Long.parseLong(loader.getUserParams()
						.get("vPhongBanId").toString());
				setOrgName(userUtil.getOrganization(getOrgId()).getName());
				butPhe = HcdtRoles.userHasRole(HcdtRole.LanhDaoDonVi,
						HcdtRole.LanhDaoPhong);

				// BashPath upload file
				props.setBashPath(CommonUtils.getBasePath());
				props.setTMP_PATH(loader.getParams().get("vTmpPath_vbden")
						.toString());
				props.setUPLOAD_PATH(loader.getParams().get("vPath_vbden")
						.toString());

				// Lấy ds các danh mục
				props.setDsLinhVucVb(new ArrayList<Map<String, Object>>());
				props.setDsDoKhan(new ArrayList<DmDoKhanModel>());
				props.setDsDoMat(new ArrayList<DmDoMatModel>());

				props.setDsDoKhan(objDmDoKhanFacade.getDsDoKhan(companyid,
						getOrgId()));
				props.setDsDoMat(objDmDoMatFacade.getDsDoMat(companyid,
						getOrgId()));

				propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade
						.getDsSoVbTheoNvb(1, companyid, getOrgId(),
								getListDvUser()));
				propsLoaiVb
						.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(
								companyid, getOrgId(), getListDvUser()));
				// ds các phòng ban của người dùng đang đăng nhập (thuộc đơn vị
				// đã cấu hình)
				props.setDsPhongBan(userUtil
						.getDsPhongBanCuaNguoiDungTheoDonVi(getOrgId(), userId));

				// Lây ds người dùng thuộc đơn vị (đơn vị lấy từ cấu hình cá
				// nhân của người dùng)
				pmGetDsChuyenXuLy();
				pmGetDsNhanPhoBien();
				// Khơi tạo thông tin thêm mới
				khoiTaoTTThemMoi();

				// DS VĂN BẢN ĐẾN CÙNG HỆ THỐNG
				// props.setDsVBDenCungHeThong(objVBDenFacade.getDsTatCaVBDenCungHeThong(getListDvUser(),companyid));
				// props.setDsVBDenCungHeThong(objVBDenFacade.getDsVBDenCungHeThong(props.getsSoHieuGoc().trim(),
				// props.getsTrichYeu().trim(),
				// props.getsNgayBanHanhTu(), props.getsNgayBanHanhDen(),
				// props.getsLoaiVanBan(), props.getsCoQuanBanHanh(),
				// props.getsNgayNhanTu(), props.getsNgayNhanDen(),
				// props.getsHinhThucNhan(), 0, companyid, getListDvUser()));

				// DS VĂN BẢN ĐẾN SCAN
				// props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(companyid));

				props.setDsVBDenCungHeThong(objVBDenFacade
						.getDsVBDenCungHeThong(props.getsSoHieuGoc().trim(),
								props.getsTrichYeu().trim(),
								props.getsNgayBanHanhTu(),
								props.getsNgayBanHanhDen(),
								props.getsLoaiVanBan(),
								props.getsCoQuanBanHanh(),
								props.getsNgayNhanTu(),
								props.getsNgayNhanDen(),
								props.getsHinhThucNhan(), getOrgIdTemp(),
								companyid, getListDvUser()));

				// mode Form
				// Gắn Liên kết văn bản vào chức năng modeForm: 1. Chức năng
				// chính 2.Liên kết văn bản
				// Có sử dụng LKVB: modeFrm dùng để render giữa lkvb và tiếp
				// nhận
				propsLkvb.getProps().setModeFrm(1);
				String request = CommonUtils.getUrlParameter("tabindex");
				if (request != null && request.trim().equals("") == false
						&& CommonUtils.isNumberic(request)) {
					props.setSelectMode(Integer.parseInt(request));
				}

				String vbdenId = CommonUtils.getUrlParameter("id");
				if (vbdenId != null && !vbdenId.trim().equals("")
						&& CommonUtils.isNumberic(vbdenId)) {
					actionTiepNhanVanBan(Long.parseLong(vbdenId));
				}

				// pmGetRequestValue();//demo get url

				// Cấu hình bật tắt chức năng
				if (loader.getCompanyParams().get("TrichLocDuLieu") != null) {
					props.setTrichLocDuLieu(Boolean.parseBoolean(loader
							.getCompanyParams().get("TrichLocDuLieu")
							.toString()));
				}
				if (loader.getCompanyParams().get("HoSoVanBan") != null) {
					props.setHoSoVanBan(Boolean.parseBoolean(loader
							.getCompanyParams().get("HoSoVanBan").toString()));
				}
				if (loader.getCompanyParams().get("LienKetVanBan") != null) {
					props.setLienKetVanBan(Boolean
							.parseBoolean(loader.getCompanyParams()
									.get("LienKetVanBan").toString()));
				}
				if (loader.getCompanyParams().get("TabXuLyDen") != null) {
					props.setTabXuLy(Boolean.parseBoolean(loader
							.getCompanyParams().get("TabXuLyDen").toString()));
				}
			}
		}
	}

	public String checkOrganNull(Object name) {
		if (name.toString() == null) {
			return "";
		} else {
			return name.toString() + "/";
		}
	}

	public void actionDonViVBCHTChange(ValueChangeEvent ev) {
		setOrgId(Long.parseLong(ev.getNewValue().toString()));
		propsDonVi.setOrgId(getOrgId());
		propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(
				companyid, getOrgId(), getListDvUser()));
		propsDonVi.setDsDonVi(objDmDonViFacade.getDsDonVi(companyid,
				getOrgId(), getListDvUser()));
	}

	public void actionDonViChange() {
		try {
			_log.info("actionDonViChange>>");
			setOrgName(userUtil.getOrganization(getOrgId()).getName());
			_log.info("Id: " + getOrgId() + " Name: " + getOrgName());
			propsLoaiVb.setOrganizationid(getOrgId());
			propsSoVanBan.setOrganizationid(getOrgId());
			propsLinhVuc.setOrganizationid(getOrgId());
			propsCachThucXuLy.setOrganizationid(getOrgId());
			propsDonVi.setOrgId(getOrgId());
			propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade.getDsLoaiVanBan(
					companyid, getOrgId(), getListDvUser()));
			propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade.getDsSoVbTheoNvb(1,
					companyid, getOrgId(), getListDvUser()));
			props.setDsDoKhan(objDmDoKhanFacade.getDsDoKhan(companyid,
					getOrgId()));
			props.setDsDoMat(objDmDoMatFacade.getDsDoMat(companyid, getOrgId()));
			props.setDsPhongBan(userUtil.getDsPhongBanCuaNguoiDungTheoDonVi(
					getOrgId(), userId));

			// Lây ds người dùng thuộc đơn vị (đơn vị lấy từ cấu hình cá nhân
			// của người dùng)
			// pmGetDsChuyenXuLy();
			// pmGetDsNhanPhoBien();
			// Khơi tạo thông tin thêm mới
			if (isRunRefesh()) {
				// DS VĂN BẢN ĐẾN CÙNG HỆ THỐNG
				// props.setDsVBDenCungHeThong(objVBDenFacade.getDsVBDenCungHeThong(getOrgId(),companyid));
			}
			// DS VĂN BẢN ĐẾN SCAN
			// props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(companyid));
			// Linh vực
			// Linh vực
			getListDonVi().clear();
			if (isCallFromDanhMuc() == false) {
				getListDonVi().add(userUtil.getOrganization(getOrgId()));
			} else {

				setListDonVi(userUtil.getDsDonViTheoNguoiDung(getUserId()));
			}

			List<Long> dsOrgId = new ArrayList<Long>();
			if (getListDonVi() != null) {
				for (Organization o : getListDonVi()) {
					dsOrgId.add(o.getOrganizationId());
				}
			}
			propsLinhVuc.setDsLinhVuc(objDmLinhVucFacade.getDsLinhVuc("{}",
					companyid, dsOrgId));
			propsCachThucXuLy.setDsCachThucXuLy(objDmCachThucXuLyFacade
					.getDsCachThucXuLy(companyid, dsOrgId));

			// Phongban
			propsDonVi.getDsDonViCungHt().clear();
			setDonViCungHt(userUtil.getListDonVi(userUtil.getCompanyId()));
			if (getDonViCungHt() != null) {
				for (Organization org : getDonViCungHt()) {
					if (org.getOrganizationId() != getOrgId()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("orgname", org.getName());
						map.put("orgid", org.getOrganizationId());
						map.put("checked", false);
						propsDonVi.getDsDonViCungHt().add(map);
					}
				}
			}
			khoiTaoTTThemMoi();
			setRunRefesh(false);
		} catch (Exception ex) {
			_log.error("Err", ex);
		}
	}

	/**
	 * @author lqthai
	 * @purpose Gửi email
	 * @date Jun 13, 2014 :: 9:45:19 AM
	 * @param dsMail
	 *            : Ds mail nhận a,c,v,b
	 * @throws EmailException
	 */
	private void sendEmailPhoBien(final String[] dsMailTo) throws Exception {
		final String urlTraCuu = CommonUtils.getPathServer()
				+ "/"
				+ objDmLinkFacade.getLinkModelByMa(Hcdt.TRA_CUU)
						.getLink_giatri() + "?id="
				+ props.getObjVBDenModel().getVbden_id() + "&nhom="
				+ IContanst.NVB_DEN + "&tab=0";

		final String tieuDe = loader.getParams().get("vMailPhoBienDen_TieuDe")
				.toString();
		final String noiDung = loader
				.getParams()
				.get("vMailPhoBienDen_NoiDung")
				.toString()
				.replace("[$soHieuGoc$]",
						props.getObjVBDenModel().getVbden_sohieugoc())
				.replace("[$soDen$]", props.getObjVBDenModel().getVbden_soden())
				.replace(
						"[$trichYeu$]",
						HtmlUtil.escape(props.getObjVBDenModel()
								.getVbden_trichyeu()));

		if (props.getPhamViPhoBien() == IContanst.PVPB_TATCA) {
			Email email = new Email();
			email.setSubject(tieuDe);
			email.setText(noiDung);
			email.setTo(dsMailTo);
			email.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);
			EmailProvider.sendMail(email);
		} else {
			for (String mailTo : dsMailTo) {
				mailTo = mailTo.trim();

				Long userIdNhan = objVanBanPhoiHopFacade
						.getUserIdByEmail(mailTo);
				VanBanPhoiHopModel vbphModel = new VanBanPhoiHopModel();
				vbphModel.setLink_ma(Hcdt.TRA_CUU.toString());
				vbphModel.setCompanyid(companyid);
				vbphModel.setNvb_id(IContanst.NVB_DEN);
				vbphModel.setVb_id(props.getObjVBDenModel().getVbden_id());
				vbphModel.setPhxl_email_nhan(mailTo);
				vbphModel.setPhxl_ngaygui(DateUtils.getCurrentDate());
				vbphModel.setPhxl_nguoigui_userid(getUserId());
				vbphModel.setPhxl_nguoinhan_userid(userIdNhan);
				String maTruyCap = CommonUtils
						.generateRandomString(IContanst.DODAI_MATRUYCAP);
				vbphModel.setPhxl_matruycap(maTruyCap);
				objVanBanPhoiHopFacade.capNhatVanBanPhoiHop(vbphModel);

				String noiDungMail = noiDung;
				if (userIdNhan == null) {
					noiDungMail = noiDungMail
							.replace("[$url$]",
									(urlTraCuu + "&email=" + mailTo
											+ " <br/><br/>Mã truy cập: <b>"
											+ maTruyCap + "</b>"));
				} else {
					noiDungMail = noiDungMail.replace("[$url$]", urlTraCuu
							+ "&email=" + mailTo);
				}

				Email email = new Email();
				email.setSubject(tieuDe);
				email.setText(noiDungMail);
				email.setTo(mailTo);
				email.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);
				EmailProvider.sendMail(email);
			}
		}
	}

	/**
	 * @author ptgiang
	 * @purpose Gửi mail thông báo xử lý văn bản
	 * @date Dec 6, 2016 :: 10:02:50 AM
	 * @param vbden_id
	 * @param dsMailTo
	 * @param noidung
	 * @param emailXuLyChinh
	 * @param listChuyenXuLy
	 */
	private void sendEmailXuLy(final long vbden_id, final String[] dsMailTo,
			final String noidung, final String emailXuLyChinh,
			final List<Map<String, Object>> listChuyenXuLy) {
		_log.info("sendMailXuLy");
		try {

			String tieude = loader.getParams().get("vMailChuyenXuLyDen_TieuDe")
					.toString();

			String pathServer = CommonUtils.getPathServer();
			String urlVanBanPhoiHop = pathServer
					+ "/"
					+ objDmLinkFacade.getLinkModelByMa(
							CommonUtils.Hcdt.VAN_BAN_PHOI_HOP).getLink_giatri()
					+ "?instant=" + vbden_id + "&nhom=" + IContanst.NVB_DEN
					+ "&email=";

			String urlXuLyVanBan = pathServer
					+ "/"
					+ objDmLinkFacade.getLinkModelByMa(
							CommonUtils.Hcdt.XU_LY_VAN_BAN_DEN)
							.getLink_giatri() + "?instant=" + vbden_id;

			for (String mailTo : dsMailTo) {
				mailTo = mailTo.trim();

				boolean coQuyenXuLy = false;
				for (Map<String, Object> mapUser : listChuyenXuLy) {
					if (mapUser.get("checked") == null
							|| Boolean.parseBoolean(mapUser.get("checked")
									.toString()) == false
							|| com.cusc.hcdt.util.StringUtils
									.isNullOrEmpty(mapUser.get("email"))) {
						continue;
					}
					if (mailTo
							.equalsIgnoreCase(mapUser.get("email").toString())) {
						coQuyenXuLy = true;
						break;
					}
				}

				String noiDungMail = noidung;
				if (coQuyenXuLy == false) {
					// Lưu văn bản phối hợp
					VanBanPhoiHopModel vbphModel = new VanBanPhoiHopModel();
					vbphModel.setLink_ma(Hcdt.VAN_BAN_PHOI_HOP.toString());
					vbphModel.setCompanyid(companyid);
					vbphModel.setNvb_id(IContanst.NVB_DEN);
					vbphModel.setVb_id(vbden_id);
					vbphModel.setPhxl_email_nhan(mailTo);
					vbphModel.setPhxl_ngaygui(DateUtils.getCurrentDate());
					vbphModel.setPhxl_nguoigui_userid(getUserId());
					vbphModel.setPhxl_nguoinhan_userid(objVanBanPhoiHopFacade
							.getUserIdByEmail(vbphModel.getPhxl_email_nhan()));
					String maTruyCap = CommonUtils
							.generateRandomString(IContanst.DODAI_MATRUYCAP);
					noiDungMail = noiDungMail
							.replace("[$url$]",
									(urlVanBanPhoiHop + mailTo
											+ " <br/><br/>Mã truy cập: <b>"
											+ maTruyCap + "</b>"));
					vbphModel.setPhxl_matruycap(maTruyCap);
					objVanBanPhoiHopFacade.capNhatVanBanPhoiHop(vbphModel);
				} else {
					noiDungMail = noiDungMail.replace("[$url$]", urlXuLyVanBan);
				}

				if (mailTo.trim().equals(emailXuLyChinh.trim())) {
					noiDungMail = noiDungMail.replace("[$phamViXuLy$]",
							"Xử lý chính");
				} else {
					noiDungMail = noiDungMail.replace("[$phamViXuLy$]",
							"Phối hợp xử lý");
				}

				// log.info("send to {} content {}", mailTo, noiDungMail);
				Email mail = new Email();
				mail.setTo(mailTo);
				mail.setSubject(tieude);
				mail.setText(noiDungMail);
				mail.setMimeType(EmailService.EMAIL_MINETYPE_TEXT);
				EmailProvider.sendMail(mail);
			}
		} catch (Exception e) {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Gửi email thất bại',3);");
			_log.error("Err sent mail", e);
		}
	}

	/**
	 * @author lqthai
	 * @purpose Lọc Văn bản đến cùng hệ thống
	 * @date May 23, 2014 :: 11:47:37 PM
	 */
	public void actionTimKiemVBDenCungHt() {
		props.setsCoQuanBanHanh(propsDonVi.getDsTenDonVi());
		_log.info("Action tìm kiếm văn bản cùng hệ thống");

		_log.info("Action tìm kiếm văn bản cùng hệ thống Toàn bộ");
		props.setDsVBDenCungHeThong(objVBDenFacade.getDsVBDenCungHeThong(props
				.getsSoHieuGoc().trim(), props.getsTrichYeu().trim(), props
				.getsNgayBanHanhTu(), props.getsNgayBanHanhDen(), props
				.getsLoaiVanBan(), props.getsCoQuanBanHanh(), props
				.getsNgayNhanTu(), props.getsNgayNhanDen(), props
				.getsHinhThucNhan(), getOrgIdTemp(), companyid, getListDvUser()));

		if (props.getsTrichYeu() != null && !props.getsTrichYeu().isEmpty()) {
			JavascriptUtils.callJavascript("showHighlight('"
					+ props.getsTrichYeu() + "');");
		}
	}

	/**
	 * @author lqthai
	 * @purpose Lọc Văn bản đến scan
	 * @date May 23, 2014 :: 11:47:37 PM
	 */
	public void actionTimKiemVBDenScan() {
		props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(props
				.getsSoHieuGocScan().trim(), props.getsTrichYeuScan().trim(),
				props.getsNgayBanHanhTuScan(), props.getsNgayBanHanhDenScan(),
				props.getsNgayDenTuScan(), props.getsNgayDenDenScan(), props
						.getsNgayScanTuScan(), props.getsNgayScanDenScan(),
				companyid));
	}

	/**
	 * @author lqthai
	 * @purpose ajax Chọn người xử lý chính (Fix lỗi khi chọn lại người xl chính
	 *          bị bỏ chọn) Sử dụng một biến tạm userIdXlChinh để lưu idXlchinh
	 *          đã chọn
	 * @date Jun 25, 2014 :: 11:50:34 PM
	 * @param ae
	 */
	public void actionChonXlChinh(AjaxBehaviorEvent ae) {
		_log.info(">>>actionChonXlChinh");
		userIdXlChinh = Long.parseLong(ae.getComponent().getAttributes()
				.get("idXlChinh").toString());
		_log.info(">>>userIdXlChinh: " + userIdXlChinh);
		for (Map<String, Object> map : props.getDsNhanXuLy()) {
			_log.info(">>>map: " + map.get("userid").toString());
			if ((Boolean) map.get("checked")) {
				if (Long.parseLong(map.get("userid").toString()) == userIdXlChinh) {
					map.put("xlchinh", true);
					_log.info(">>>Xl chinh: " + map.get("userid").toString());
				} else {
					map.put("xlchinh", false);
					_log.info(">>>Xl Phoihop: " + map.get("userid").toString());
				}
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Chọn nhận phổ biến
	 * @date May 23, 2014 :: 11:29:10 AM
	 */
	public void actionChonNhanPhoBien() {
		props.setModeNguoiNhan(2);

		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"dialogChonNguoiNhan.show();");
	}

	/**
	 * @author lqthai
	 * @purpose Chọn nhận xử lý
	 * @date May 23, 2014 :: 11:30:17 AM
	 */
	public void actionChonNhanXuLy() {
		props.setModeNguoiNhan(1);

		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"dialogChonNguoiNhan.show();");
	}

	/**
	 * @author lqthai
	 * @purpose Xóa nguoi nhận phạm vi phổ biến đã chọn
	 * @date May 22, 2014 :: 1:25:17 PM
	 * @param ae
	 */
	@SuppressWarnings("unchecked")
	public void actionXoaNhanPhoBien(AjaxBehaviorEvent ae) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj = (Map<String, Object>) ae.getComponent().getAttributes()
				.get("nguoiNhan");
		if (obj != null) {
			try {
				_log.info("Xóa người nhận phổ biến đã chọn");
				// props.dsNhanPhoBien.remove(obj);
				obj.put("checked", false);
			} catch (Exception e) {
				_log.info(e.toString());
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Xóa nguoi nhận xử lý đã chọn
	 * @date May 22, 2014 :: 1:25:17 PM
	 * @param ae
	 */
	@SuppressWarnings("unchecked")
	public void actionXoaNhanXuLy(AjaxBehaviorEvent ae) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj = (Map<String, Object>) ae.getComponent().getAttributes()
				.get("nguoiNhan");
		if (obj != null) {
			try {
				_log.info("Xóa nguoi nhận xử lý đã chọn");
				// props.dsNhanXuLy.remove(obj);
				obj.put("checked", false);
			} catch (Exception e) {
				_log.info(e.toString());
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Xóa đơn vị đã chọn
	 * @date Dec 2, 2016 :: 4:13:05 PM
	 */
	public void actionXoaCoQuanBanHanh() {
		try {
			_log.info(">>actionXoaCoQuanBanHanh");
			propsDonVi.dvId = 0;
			propsDonVi.tenDonVi = "";
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	public void actionChonThemMoiLVB(ActionEvent ae) {
		try {
			_log.info(">>actionChonThemMoiLVB");
			props.getObjVBDenModel().setLvb_id(propsLoaiVb.getLvbId_ThemMoi());
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	public void actionChonThemMoiSVB(ActionEvent ae) {
		try {
			_log.info(">>actionChonThemMoiSVB");
			props.getObjVBDenModel()
					.setSvb_id(propsSoVanBan.getSvbId_ThemMoi());
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	public void actionChonThemMoiCTXL(ActionEvent ae) {
		try {
			_log.info(">>actionChonThemMoiCTXL");
			props.getObjVBDenModel().setCtxl_id(
					propsCachThucXuLy.getCtxlId_ThemMoi());
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	/**
	 * @author lqthai
	 * @purpose Kiểm tra lỗi nhập liệu
	 * @date May 20, 2014 :: 6:22:15 PM
	 * @return
	 * @throws EmailException
	 */
	private boolean validation() throws EmailException {
		_log.info(">>>validation");
		StopWatch watch = new StopWatch();
		watch.start();
		boolean test = true;
		String soHieu = props.getObjVBDenModel().getVbden_sohieugoc().trim();
		String nguoiKy = props.getObjVBDenModel().getVbden_nguoiky().trim();
		String soTo = props.getObjVBDenModel().getVbden_soto().trim();
		String trichYeu = props.getObjVBDenModel().getVbden_trichyeu().trim();
		// Số đến sẽ theo quy tắc cấu hình của sổ văn bản
		String soDen = "";
		if (props.isSoDenVitri()) {
			soDen = "$" + props.getSoDen() + "$" + props.getSoDenMa();
		} else {
			soDen = props.getSoDenMa() + "$" + props.getSoDen() + "$";
		}
		// String soDen = props.objVBDenModel.getVbden_soden().trim();
		String butPhe = "";
		if (props.getObjVBDenModel().getVbden_butphe() != null) {
			butPhe = props.getObjVBDenModel().getVbden_butphe().trim();
		}
		String ttLuuTru = "";
		if (props.getObjVBDenModel().getVbden_thongtin_lt() != null) {
			ttLuuTru = props.getObjVBDenModel().getVbden_thongtin_lt().trim();
		}

		Date ngayCoHl = props.getObjVBDenModel().getVbden_ngaycohl();
		Date ngayHetHl = props.getObjVBDenModel().getVbden_ngayhethl();
		Date ngayden = props.getObjVBDenModel().getVbden_ngayden();
		Date ngayBanHanh = props.getObjVBDenModel().getVbden_ngaybanhanh();

		Date hanXlToanVb = props.getObjVBDenModel().getVbden_hanxl_toanvanban();
		Date hanXlBuocTt = props.getObjVBDenLuanChuyenModel()
				.getVbden_lc_hanxl();

		props.getObjVBDenModel().setVbden_sohieugoc(soHieu);
		props.getObjVBDenModel().setVbden_nguoiky(nguoiKy);
		props.getObjVBDenModel().setVbden_soto(soTo);
		props.getObjVBDenModel().setVbden_trichyeu(trichYeu);
		props.getObjVBDenModel().setVbden_soden(soDen);
		props.getObjVBDenModel().setVbden_butphe(butPhe);
		props.getObjVBDenModel().setVbden_thongtin_lt(ttLuuTru);
		props.getObjVBDenModel().setVbden_tiepnhan(true);
		props.getObjVBDenModel()
				.setVbden_linhvuc(propsLinhVuc.getDsLinhVucId());
		props.getObjVBDenModel().setVbden_trangthai_xl(0);
		// _log.info(props.getObjVBDenModel().getVbden_linhvuc());
		props.getObjVBDenModel().setVbden_hienthi(true);// mặc định hiển thị là
														// true

		if (props.getSelectMode() == 1) {
			props.getObjVBDenModel().setPhongban_organizationid(phongbanId);
		}

		if (props.getSelectMode() != 2) {// Không phải tiếp nhận từ văn bản
											// scan, scan là false
			props.getObjVBDenModel().setVbden_scan(false);
		}
		// Chuyển xử lý trạng thái là 1
		if (props.isTmpChuyenXl()) {
			props.getObjVBDenModel().setVbden_trangthai_xl(1);
		}
		// Hoàn thành trạng thái là 2
		if (props.getObjVBDenModel().getVbden_hoanthanh()) {
			props.getObjVBDenModel().setVbden_trangthai_xl(2);
		}

		// Chưa hoàn thành la null
		if (!props.isTmpChuyenXl()) {
			props.getObjVBDenModel().setVbden_hanxl_toanvanban(null);
		}
		// Nếu trạng thái lưu trữ là false thì thông tin lưu trữ rỗng
		if (!props.getObjVBDenModel().getVbden_trangthai_lt()) {
			props.getObjVBDenModel().setVbden_thongtin_lt("");
		}

		// Mặc định CungHt = org
		if (props.mode != 2) { // Chỉ có trường hợp thêm mới văn bản (2. tiếp
								// nhận cùng hệ thống, scan)
			props.getObjVBDenModel().setCunght_organizationid(getOrgId());
			props.getObjVBDenModel().setCunght_organizationten(getOrgName());
		}

		if (props.getObjVBDenModel().getLvb_id() == null) {
			Validator.showErrorMessage(getPortletId(), "frm:selLoaiVanBan",
					"Vui lòng chọn loại văn bản");
			props.setSelectMode2(0);
			test = false;
		}

		if (props.getObjVBDenModel().getSvb_id() == null) {
			Validator.showErrorMessage(getPortletId(), "frm:selSoVanBan",
					"Vui lòng chọn sổ văn bản");
			props.setSelectMode2(0);
			test = false;
		}

		if (props.getObjVBDenModel().getDm_id() == null) {
			Validator.showErrorMessage(getPortletId(), "frm:selDoMat",
					"Vui lòng chọn độ mật");
			props.setSelectMode2(0);
			test = false;
		}

		if (props.getObjVBDenModel().getDk_id() == null) {
			Validator.showErrorMessage(getPortletId(), "frm:selDoKhan",
					"Vui lòng chọn độ khẩn");
			props.setSelectMode2(0);
			test = false;
		}

		if (props.getObjVBDenModel().getCtxl_id() == null) {
			Validator.showErrorMessage(getPortletId(), "frm:selCachThucXuLy",
					"Vui lòng chọn cách thức xử lý");
			props.setSelectMode2(0);
			test = false;
		}
		// Kiểm tra số đến
		if (!CommonUtils.isNumberic(props.getSoDen())
				|| props.getSoDen().length() > 5) {
			Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
					"Số đến không hợp lệ");
			props.setSelectMode2(0);
			test = false;
		} else if (!CommonUtils.checkRegex(
				CommonUtils.getBundleValue("regex_chuoimacdinh"),
				props.getSoDenMa())) {
			Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
					"Số đến không hợp lệ");
			props.setSelectMode2(0);
			test = false;
		}

		/*
		 * else if(objVBDenFacade.isExistsSoDen(props.getSoDen(),
		 * props.getObjVBDenModel
		 * ().getSvb_id()==null?-1:props.getObjVBDenModel().getSvb_id())){
		 * Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
		 * "Số đến không được trùng"); test=false;
		 * if(!props.getSoDen().equals("99999"
		 * )&&props.getObjVBDenModel().getSvb_id()!=null){
		 * props.setSoDen(objVBDenFacade
		 * .getMaxSoDenTheoSoVanBan(props.getObjVBDenModel().getSvb_id())+""); }
		 * props.setSelectMode2(0); }
		 */
		// Kiểm tra ngày đến
		if (ngayden == null) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayDen",
					"Vui lòng nhập ngày đến");
			test = false;
		}

		/*
		 * for(DmLinhVucModel modelLv : propsLinhVuc.getDsLinhVuc()){ if(
		 * propsLinhVuc.getDsLinhVucId().contains(","+modelLv.getLv_id()+",") ||
		 * propsLinhVuc.getDsLinhVucId().contains("{"+modelLv.getLv_id()+",") ||
		 * propsLinhVuc.getDsLinhVucId().contains(","+modelLv.getLv_id()+"}") ){
		 * modelLv.setChecked(true); }else{ modelLv.setChecked(false); } }
		 */
		// Kiểm tra lĩnh vực
		if (propsLinhVuc.getDsLinhVucId().equals("{}")) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:chkLinhVuc",
					"Vui lòng chọn lĩnh vực");
			test = false;
		} else {
			boolean tmp = true;
			for (int i = 0; i < propsLinhVuc.getDsLinhVuc().size(); i++) {
				if (propsLinhVuc.getDsLinhVuc().get(i).isChecked()) {
					if (!objDmLinhVucFacade.isExists(propsLinhVuc
							.getDsLinhVuc().get(i).getLv_id())) {
						propsLinhVuc.getDsLinhVuc().remove(i);
						i--;
						tmp = false;
					}
				}
			}
			if (!tmp) {
				propsLinhVuc.actionChonLinhVuc();
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:chkLinhVuc",
						"Một số lĩnh vực vừa chọn không tồn tại");
				test = false;
			}
		}
		// Kiểm tra số hiệu gốc
		/*
		 * if(soHieu.equals("")){ props.setSelectMode2(0);
		 * Validator.showErrorMessage(getPortletId(), "frm:txtSoHieuGoc",
		 * "Vui lòng nhập số hiệu gốc"); test=false; }
		 */
		// Kiểm tra trích yếu
		if (trichYeu.equals("")) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtTrichYeu",
					"Vui lòng nhập trích yếu");
			test = false;
		}
		// Kiểm tra ngày ban hành
		if (ngayBanHanh == null) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayBanHanh",
					"Vui lòng nhập ngày ban hành");
			test = false;
		}
		// Kiểm tra ngày có hiệu lực
		if ((ngayBanHanh != null && ngayCoHl != null && ngayBanHanh
				.after(ngayCoHl))) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayHieuLuc",
					"Ngày có hiệu lực phải sau ngày ban hành");
			test = false;
		} else if ((ngayBanHanh != null && ngayHetHl != null && ngayBanHanh
				.after(ngayHetHl))) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayHieuLuc",
					"Ngày hết hiệu lực phải sau ngày ban hành");
			test = false;
		} else if (ngayCoHl != null && ngayHetHl != null
				&& ngayCoHl.after(ngayHetHl)) {
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtNgayHieuLuc",
					"Ngày hết hiệu lực phải sau ngày có hiệu lực");
			test = false;
		}

		// @ptgiang Kiểm tra nơi nhận liên đơn vị
		if (props.isChuyenDenDonVi()) {
			if (props.getNoiDungChuyenDenDonVi() == null
					|| props.getNoiDungChuyenDenDonVi().isEmpty()) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(),
						"frm:txtNoiDungChuyen", "Vui lòng nhập nội dung gửi");
				test = false;
			}
			boolean coChonDonViNhan = false;
			for (Map<String, Object> mapDonVi : props.getDsChuyenDenDonVi()) {
				if (Boolean.parseBoolean(mapDonVi.get("chon").toString())) {
					coChonDonViNhan = true;
					break;
				}
			}
			if (coChonDonViNhan == false) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(),
						"frm:txtNhanLienDonVi", "Vui lòng chọn đơn vị nhận");
				test = false;
			}
		}

		boolean themCoQuan = false;
		// Kiểm tra cơ quan ban hành
		if (propsDonVi.dvId == 0 && props.getTenDonViPhatHanh().equals("")) {// !testDonVi
			_log.info("Chưa chọn đơn vị phát hành: " + propsDonVi.dvId);
			props.setSelectMode2(0);
			Validator.showErrorMessage(getPortletId(), "frm:txtCoQuanBanHanh",
					"Vui lòng chọn cơ quan ban hành");
			test = false;
		} else if (propsDonVi.getTmpLoaiDonVi() == 0
				&& props.getSelectMode() == 0) { // chọn đơn vị ngoài hệ thống
			if (propsDonVi.dvId != 0) {
				// props.selectMode==0: tiếp nhận cùng hệ thống OR scan không
				// kiểm tra
				if (!objDmDonViFacade.isExists((int) propsDonVi.dvId)) {// Kiểm
																		// tra 2
																		// trình
																		// duyệt
					props.setSelectMode2(0);
					Validator.showErrorMessage(getPortletId(),
							"frm:txtCoQuanBanHanh",
							"Cơ quan ban hành không tồn tại");
					test = false;
					for (int i = 0; i < propsDonVi.dsDonVi.size(); i++) {
						if (propsDonVi.dsDonVi.get(i).getDv_id() == propsDonVi.dvId) {
							propsDonVi.dsDonVi.remove(i);
							propsDonVi.dvId = 0;
							propsDonVi.tenDonVi = "";
							break;
						}
					}
				}
			} else {
				_log.info(">>>Thêm đơn vị: " + props.getTenDonViPhatHanh());
				themCoQuan = true;// Sử dụng kiểm tra cuối hàm nếu không có lỗi
									// thì thực hiện thêm đơn vị
				props.setTenDonViPhatHanh(props.getTenDonViPhatHanh().trim()
						.replaceAll("\\s+", " "));
				propsDonVi.dvId = 0;
				propsDonVi.tenDonVi = props.getTenDonViPhatHanh();
			}
		}

		// Kiểm tra chọn người nhận phổ biến
		if (props.getPhamViPhoBien() == IContanst.PVPB_NGUOINHAN) {
			boolean testNhanPb = false;
			for (Map<String, Object> map : props.getDsNhanPhoBien()) {
				if ((Boolean) map.get("checked")) {
					testNhanPb = true;
					break;
				}
			}
			if (!testNhanPb) {
				if (test && props.getSelectMode2() == 0) {
					props.setSelectMode2(1);
				}
				Validator.showErrorMessage(getPortletId(), "frm:tmpErrPbcn",
						"Vui lòng chọn người nhận");
				test = false;
			}
		}
		// Kiểm tra 2 trình duyệt các danh mục
		if (test) {
			if (!objDmLoaiVanBanFacade.isExists(props.getObjVBDenModel()
					.getLvb_id())) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:selLoaiVanBan",
						"Loại văn bản vừa chọn không tồn tại");
				test = false;
				propsLoaiVb.setDsLoaiVanBan(objDmLoaiVanBanFacade
						.getDsLoaiVanBan(companyid, 0, getListDvUser()));
			}
			if (!objDmSoVanBanFacade.isExists(props.getObjVBDenModel()
					.getSvb_id())) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:selSoVanBan",
						"Sổ văn bản vừa chọn không tồn tại");
				test = false;
				propsSoVanBan.setDsSoVanBan(objDmSoVanBanFacade
						.getDsSoVbTheoNvb(1, companyid, getOrgId(),
								getListDvUser()));
				// Lấy lại thông tin
				props.getObjVBDenModel().setSvb_id(
						propsSoVanBan.getDsSoVanBan().get(0).getSvb_id());// sổ
																			// văn
																			// bản
																			// được
																			// chọn
																			// mặc
																			// định
																			// là
																			// sổ
																			// đầu
																			// tiên
				props.setSoDen(objVBDenFacade
						.getMaxSoDenTheoSoVanBan(propsSoVanBan.getDsSoVanBan()
								.get(0).getSvb_id() + 1)
						+ "");
				props.setSoDenMa(objDmSoVanBanFacade
						.getChuoiMacDinh(propsSoVanBan.getDsSoVanBan().get(0)
								.getSvb_id()));
				props.setSoDenVitri(objDmSoVanBanFacade
						.getViTriStt(propsSoVanBan.getDsSoVanBan().get(0)
								.getSvb_id()));
				props.setSuaSoDen(false);
			}
			if (!objDmDoKhanFacade
					.isExists(props.getObjVBDenModel().getDk_id())) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:selDoKhan",
						"Độ khẩn vừa chọn không tồn tại");
				test = false;
				props.setDsDoKhan(objDmDoKhanFacade.getDsDoKhan(companyid,
						getOrgId()));
			}
			if (!objDmDoMatFacade.isExists(props.getObjVBDenModel().getDm_id())) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:selDoMat",
						"Độ mật vừa chọn không tồn tại");
				test = false;
				props.setDsDoMat(objDmDoMatFacade.getDsDoMat(companyid,
						getOrgId()));
			}
		}
		// Bước tiếp nhận là max luân chuyển
		// Nếu chuyển xử lý thì bước chuyển xử lý là max luân chuyển
		props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_max(true);
		if (props.isTmpChuyenXl()) {
			// Nếu chọn chuyển xử lý thì cập nhật thông tin cho objLuanChuyen
			props.getObjVBDenLuanChuyenModel().setVbden_lc_butphe("");
			props.getObjVBDenLuanChuyenModel().setVbden_lc_noidung("");
			props.getObjVBDenLuanChuyenModel().setVbden_lc_trangthai_lt(
					props.getObjVBDenModel().getVbden_trangthai_lt());
			props.getObjVBDenLuanChuyenModel().setVbden_lc_thongtin_lt(
					props.getObjVBDenModel().getVbden_thongtin_lt());
			props.getObjVBDenLuanChuyenModel().setVbden_lc_cn_gannhat(
					DateUtils.getCurrentDate());
			props.getObjVBDenLuanChuyenModel().setVbden_lc_cn_gannhat_userid(
					userId);
			props.getObjVBDenLuanChuyenModel().setVbden_lc_hoanthanh(false);
			props.getObjVBDenLuanChuyenModel().setVbden_lc_lbxl(0);
			props.getObjVBDenLuanChuyenModel().setVbden_lc_mail(false);
			props.getObjVBDenLuanChuyenModel().setVbden_lc_ngaychuyen(
					DateUtils.getCurrentDate());// Ngày chuyển là ngày hiện tại
			props.getObjVBDenLuanChuyenModel()
					.setVbden_lc_nguoichuyen(fullName);// người chuyển là người
														// hiện tại
			props.getObjVBDenLuanChuyenModel().setVbden_lc_phongban(phongbanId);
			props.getObjVBDenLuanChuyenModel().setVbden_lc_max(true);
			// Nếu chuyển cho mình thì set bước dầu tiên max luân chuyển là
			// false
			if (props.getObjVBDenLuanChuyenModel().getVbden_lc_xlchinh_userid() == props
					.getObjVBDenLuanChuyenModelTiepNhan()
					.getVbden_lc_xlchinh_userid()) {
				props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_max(
						false);
			}
			// Kiểm tra xử lý chính
			boolean testXlChinh = false;
			if (props.isTmpChuyenXl()) {
				String curDate = DateUtils.formatDate2String(
						DateUtils.getCurrentDate(), "dd/MM/yyyy HH:mm:ss");
				List<String> dsUserIdDvId = new ArrayList<String>();
				ChuyenBoSungModel chuyenBoSungModel = new ChuyenBoSungModel();
				chuyenBoSungModel
						.setHistory(new HashMap<String, Map<String, List<String>>>());
				chuyenBoSungModel.setCurrent(new HashMap<String, String>());

				String idsNhanXl = "";
				dsMailNhanXuLyPhoiHop = "";
				emailXuLyChinh = "";
				for (Map<String, Object> map : props.getDsNhanXuLy()) {
					if ((Boolean) map.get("checked")) {
						long userid = Long
								.valueOf(map.get("userid").toString());
						long dvid = Long.valueOf(map.get("donviid").toString());
						String userIdDvId = String.format(
								IContanst.PATTERN_USERID_DVID, userid, dvid);
						dsUserIdDvId.add(userIdDvId);

						if ((Boolean) map.get("xlchinh")) {
							testXlChinh = true;
							props.getObjVBDenLuanChuyenModel()
									.setVbden_lc_xlchinh_userid(userid);
							props.getObjVBDenLuanChuyenModel()
									.setVbden_lc_xlchinh_dvid(dvid);
							emailXuLyChinh = map.get("email").toString();
						} else {
							chuyenBoSungModel.getCurrent().put(userIdDvId,
									curDate);
							idsNhanXl += "," + map.get("userid").toString();
							dsMailNhanXuLyPhoiHop += ","
									+ map.get("email").toString();
						}
					}

				}
				idsNhanXl = ((!idsNhanXl.equals("")) ? idsNhanXl.substring(1)
						: "");
				idsNhanXl = "{" + idsNhanXl + "}";
				dsMailNhanXuLyPhoiHop = ((!dsMailNhanXuLyPhoiHop.equals("")) ? dsMailNhanXuLyPhoiHop
						.substring(1) : "");
				props.getObjVBDenLuanChuyenModel()
						.setVbden_lc_xlphoihop_dsuserid(idsNhanXl);

				props.getObjVBDenLuanChuyenModel().setVbden_lc_xlbosung(
						CollectionUtil.convertObjToJson(chuyenBoSungModel)
								.toString());
				props.getObjVBDenLuanChuyenModel().setVbden_lc_user_donvi(
						CollectionUtil.convertToJsonString(dsUserIdDvId));
			}
			if (!testXlChinh) {
				if (test && props.getSelectMode2() == 0) {
					props.setSelectMode2(1);
				}
				Validator.showErrorMessage(getPortletId(), "frm:tmpErrXlChinh",
						"Vui lòng chọn người xử lý chính");
				test = false;
			}

			if (hanXlToanVb != null && hanXlBuocTt != null
					&& hanXlBuocTt.after(hanXlToanVb)) {
				if (test && props.getSelectMode2() == 0) {
					props.setSelectMode2(1);
				}
				Validator
						.showErrorMessage(getPortletId(), "frm:errHanXlLc",
								"Hạn xử lý bước tiếp theo phải nhỏ hơn hoặc bằng hạn xử lý toàn văn bản");
				test = false;
			}

			if (hanXlToanVb != null
					&& CommonUtils.convertDate(DateUtils.getCurrentDate())
							.after(CommonUtils.convertDate(hanXlToanVb))) {
				if (test && props.getSelectMode2() == 0) {
					props.setSelectMode2(1);
				}
				Validator
						.showErrorMessage(getPortletId(), "frm:errHanXlToanVb",
								"Hạn xử lý toàn văn bản phải lớn hơn hoặc bằng ngày hiện tại");
				test = false;
			}
			if (hanXlBuocTt != null
					&& CommonUtils.convertDate(DateUtils.getCurrentDate())
							.after(CommonUtils.convertDate(hanXlBuocTt))) {
				if (test && props.getSelectMode2() == 0) {
					props.setSelectMode2(1);
				}
				Validator
						.showErrorMessage(getPortletId(), "frm:errHanXlLc",
								"Hạn xử lý bước tiếp theo phải lớn hơn hoặc bằng ngày hiện tại");
				test = false;
			}

		}
		if (test) {
			// luân chuyển bước đầu tiên (Tiếp nhận)
			String noiDung = "";
			if (props.getObjVBDenLuanChuyenModelTiepNhan()
					.getVbden_lc_noidung() != null) {
				noiDung = props.getObjVBDenLuanChuyenModelTiepNhan()
						.getVbden_lc_noidung().trim();
			}
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_cha(-1l);
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_noidung(
					noiDung);
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_butphe(
					butPhe);
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_hoanthanh(
					props.isTmpChuyenXl());
			props.getObjVBDenLuanChuyenModelTiepNhan()
					.setVbden_lc_xlchinh_userid(userId);
			props.getObjVBDenLuanChuyenModelTiepNhan()
					.setVbden_lc_xlphoihop_dsuserid("{}");
			props.getObjVBDenLuanChuyenModelTiepNhan()
					.setVbden_lc_trangthai_lt(
							props.getObjVBDenModel().getVbden_trangthai_lt());
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_thongtin_lt(
					props.getObjVBDenModel().getVbden_thongtin_lt());
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_lbxl(1);// 1.bước
																			// tiếp
																			// nhận
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_cn_gannhat(
					DateUtils.getCurrentDate());
			props.getObjVBDenLuanChuyenModelTiepNhan()
					.setVbden_lc_cn_gannhat_userid(userId);
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_phongban(
					props.getObjVBDenModel().getPhongban_organizationid());
			props.getObjVBDenLuanChuyenModelTiepNhan()
					.setVbden_lc_xlchinh_dvid(
							props.getObjVBDenModel().getOrganizationid());
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_ngaychuyen(
					DateUtils.getCurrentDate());// Ngày chuyển là ngày hiện tại
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_nguoichuyen(
					fullName);// người chuyển là người hiện tại

			if (props.isTmpChuyenXl()
					&& props.getObjVBDenLuanChuyenModelTiepNhan()
							.getVbden_lc_mail()) {
				props.getObjVBDenLuanChuyenModelTiepNhan()
						.setVbden_lc_mail_dsnhan(props.getTxtEmailChuyenXuLy());
			}

			if (props.isTmpChuyenXl()
					|| props.getObjVBDenModel().getVbden_hoanthanh()) {
				props.getObjVBDenLuanChuyenModelTiepNhan()
						.setVbden_lc_hoanthanh(true);// Nếu chuyển xl or xác
														// nhận hoàn thành toàn
														// văn bản
			}

			// Người tiếp nhận là người xử lý chính bước đầu tiên
			String curDate = DateUtils.formatDate2String(
					DateUtils.getCurrentDate(), "dd/MM/yyyy HH:mm:ss");
			String userIdDvId = String.format(IContanst.PATTERN_USERID_DVID,
					props.getObjVBDenLuanChuyenModelTiepNhan()
							.getVbden_lc_xlchinh_userid(), props
							.getObjVBDenLuanChuyenModelTiepNhan()
							.getVbden_lc_xlchinh_dvid());

			List<String> dsUserIdDvId = new ArrayList<String>();
			dsUserIdDvId.add(userIdDvId);

			ChuyenBoSungModel chuyenBoSungModel = new ChuyenBoSungModel();
			chuyenBoSungModel
					.setHistory(new HashMap<String, Map<String, List<String>>>());
			chuyenBoSungModel.setCurrent(new HashMap<String, String>());
			chuyenBoSungModel.getCurrent().put(userIdDvId, curDate);

			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_xlbosung(
					CollectionUtil.convertObjToJson(chuyenBoSungModel)
							.toString());
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_user_donvi(
					CollectionUtil.convertToJsonString(dsUserIdDvId));

			// Set ds phổ biến
			// Phổ biến chuỗi id cá nhân mặc định là {}
			props.getObjVBDenModel().setVbden_pb_canhan("{}");
			props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_canhan("{}");
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_pb_canhan(
					"{}");

			props.getObjVBDenModel().setVbden_pb_phongban("");
			props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_phongban("");
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_pb_phongban(
					"");
			// Phổ biến phòng ban mặc định là rỗng
			props.getObjVBDenModel().setVbden_pb_phongban("");
			props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_phongban("");
			props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_pb_phongban(
					"");
			if (props.getPhamViPhoBien() == IContanst.PVPB_TATCA) {
				dsMailNhanPhoBien = UserUtil
						.getChuoiEmailNguoiDungTheoDonVi(getOrgId());
				_log.info(">>>Phổ biến: Tất cả " + dsMailNhanPhoBien);

			} else if (props.getPhamViPhoBien() == IContanst.PVPB_NOIBO) {
				_log.info(">>>Phổ biến: Phòng ban");
				try {
					String pbPb = String.valueOf(getIdPrimOrganization());
					props.getObjVBDenModel().setVbden_pb_phongban(pbPb);
					props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_phongban(
							pbPb);
					props.getObjVBDenLuanChuyenModelTiepNhan()
							.setVbden_lc_pb_phongban(pbPb);
					_log.info(">>>>"
							+ props.getObjVBDenModel().getVbden_pb_phongban());

					List<User> dsNguoiDungCuaPhongBan = new ArrayList<User>();
					dsNguoiDungCuaPhongBan = UserLocalServiceUtil
							.getOrganizationUsers(phongbanId);
					dsMailNhanPhoBien = "";
					for (User user : dsNguoiDungCuaPhongBan) {
						dsMailNhanPhoBien += "," + user.getEmailAddress();
					}
					dsMailNhanPhoBien = ((!dsMailNhanPhoBien.equals("")) ? dsMailNhanPhoBien
							.substring(1) : "");
					_log.info(">>>dsMailNhanPhoBien Pb: " + dsMailNhanPhoBien);
				} catch (Exception ex) {
					_log.info("" + ex);
				}
			} else if (props.getPhamViPhoBien() == IContanst.PVPB_NGUOINHAN) {
				_log.info(">>>Phổ biến: Cá nhân");
				String userids = "";
				dsMailNhanPhoBien = "";
				for (Map<String, Object> map : props.getDsNhanPhoBien()) {
					if ((Boolean) map.get("checked")) {
						_log.info("" + map.toString());
						userids += "," + map.get("userid").toString();
						dsMailNhanPhoBien += "," + map.get("email").toString();
					}
				}
				userids = "{"
						+ ((!userids.equals("")) ? userids.substring(1) : "")
						+ "}";
				dsMailNhanPhoBien = ((!dsMailNhanPhoBien.equals("")) ? dsMailNhanPhoBien
						.substring(1) : "");
				props.getObjVBDenModel().setVbden_pb_canhan(userids);
				props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_canhan(
						userids);
				props.getObjVBDenLuanChuyenModelTiepNhan()
						.setVbden_lc_pb_canhan(userids);
			} else {
				props.getObjVBDenModel().setVbden_pb_phongban("0");
				props.getObjVBDenLuanChuyenModel().setVbden_lc_pb_phongban("0");
				props.getObjVBDenLuanChuyenModelTiepNhan()
						.setVbden_lc_pb_phongban("0");
			}
			// Mail nhận phổ biến
			if (props.isMailPhoBien()
					&& props.getPhamViPhoBien() != IContanst.PVPB_KHONGPHOBIEN) {
				props.setObjMailPhoBienModel(new MailPhoBienModel());
			} else {
				props.setObjMailPhoBienModel(null);
			}

		}

		// ds tập tin trường hợp tiếp nhận cùng ht + tiếp nhận scan
		if (props.getDsTapTinVbXoa().size() != 0 && test) {
			for (TapTinDinhKemVanBanModel ttdk : props.getDsTapTinVbXoa()) {
				props.getDsTapTinVb().add(ttdk);
			}
		}

		// Thực hiện thêm cơ quan ban hành
		if (test && themCoQuan) {
			// Nếu tên đơn vị tồn tại
			props.setTenDonViPhatHanh(props.getTenDonViPhatHanh().trim()
					.replaceAll("\\s+", " "));
			String dv_ten = props.getTenDonViPhatHanh();
			DmDonViModel donVi = objDmDonViFacade.getDonViTheoTen(dv_ten,
					companyid, getOrgId());
			if (donVi != null) {
				propsDonVi.dvId = donVi.getDv_id();
				propsDonVi.tenDonVi = donVi.getDv_ten();
				_log.info(">>>Đơn vị: " + propsDonVi.dvId);
				_log.info(">>>Đơn vị: " + propsDonVi.tenDonVi);
			} else {
				donVi = new DmDonViModel();
				donVi.setDv_ten(dv_ten);
				donVi.setDv_ma("DVKhac1");
				donVi.setCompanyid(companyid);
				donVi.setOrganizationid(getOrgId());
				donVi.setDv_cha(0);
				donVi.setDv_diachi("");
				donVi.setDv_sdt("");
				donVi.setDv_email("");

				_log.info(">>>Thực hiện thêm danh mục cấp đơn vị khác");
				DmCapDonViModel cap = objDmCapDonViFacade.getCapDonViTheoTen(
						"Khác", companyid, getOrgId());
				if (cap != null) {
					donVi.setC_id(cap.getC_id());
					_log.info(">>>Cấp đã tồn tại");
				} else {
					cap = new DmCapDonViModel();
					cap.setC_ten("Khác");
					cap.setCompanyid(companyid);
					cap.setOrganizationid(getOrgId());
					objDmCapDonViFacade.capNhatCapDonVi(cap, 1);
					donVi.setC_id(cap.getC_id());
					_log.info(">>>Thêm mới cấp: " + donVi.getC_id());
				}

				objDmDonViFacade.capNhatDonVi(donVi, 1);
				donVi.setDv_ma("DVKhac" + donVi.getDv_id());
				objDmDonViFacade.capNhatDonVi(donVi, 2);

				propsDonVi.dvId = donVi.getDv_id();
				propsDonVi.tenDonVi = dv_ten;
				propsDonVi.reloadDsDonVi();
				_log.info(">>>Thêm: " + donVi.getDv_id());
			}

			if (propsDonVi.dvId == 0) {// !testDonVi
				_log.info("Chưa chọn đơn vị phát hành: " + propsDonVi.dvId);
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(),
						"frm:txtCoQuanBanHanh",
						"Vui lòng chọn cơ quan ban hành");
				test = false;
			}
		}

		// Cơ quan ban hành
		props.getObjVBDenModel().setVbden_coquanbanhanh(propsDonVi.tenDonVi);
		props.getObjVBDenModel().setVbden_coquanbanhanhid(propsDonVi.dvId);

		watch.stop();
		_log.info(">>>Thời gian kiểm tra: " + watch.getTime());
		_log.info(">>>Id Hsvb: " + propsHsvb.getDsIdHsvb());
		return test;
	}

	public void actionChonLoaiPhoBien() {
		try {
			if (props.getPhamViPhoBien() == IContanst.PVPB_NGUOINHAN) {
				pmGetDsNhanPhoBien();
				props.setTxtEmailPhoBien("");
			} else {
				loadDsEmailPhoBien();
			}
		} catch (Exception ex) {
			_log.error("Err actionChonLoaiPhoBien ", ex);
		}
	}

	/**
	 * @author Thai Bao
	 * @purpose Lấy danh sách email phổ biến
	 * @date Oct 26, 2015 :: 8:47:25 AM
	 */
	public void loadDsEmailPhoBien() {
		try {
			if (props.getPhamViPhoBien() == IContanst.PVPB_TATCA) {
				props.setTxtEmailPhoBien(UserUtil
						.getChuoiEmailNguoiDungTheoCompany(companyid));
			} else if (props.getPhamViPhoBien() == IContanst.PVPB_NOIBO) {
				List<Map<String, Object>> list = userUtil
						.getListMapNguoiDungTheoDonVi(getOrgId());
				props.setDsNhanPhoBien(list);
				props.setTxtEmailPhoBien(pmGetDsEmail(list));
			} else if (props.getPhamViPhoBien() == IContanst.PVPB_NGUOINHAN) {
				String txtdsEmail = "";
				for (Map<String, Object> map : props.getDsNhanPhoBien()) {
					if ((Boolean) map.get("checked")) {
						txtdsEmail += "," + map.get("email").toString();
					}
				}
				txtdsEmail = ((!txtdsEmail.equals("")) ? txtdsEmail
						.substring(1) : "");
				props.setTxtEmailPhoBien(txtdsEmail);
			} else {
				// Không phổ biến
				props.setTxtEmailPhoBien("");
			}
		} catch (Exception ex) {
			_log.error("Err loadDsEmailPhoBien ", ex);
		}
	}

	/**
	 * @author Thai Bao
	 * @purpose Load danh sách email xử lý
	 * @date Oct 26, 2015 :: 8:48:09 AM
	 */
	public void loadDsEmailXuLy() {
		StringBuilder sb = new StringBuilder();
		if (!CollectionUtil.isNullOrEmpty(props.getDsNhanXuLy())) {
			boolean isFirst = true;
			for (Map<String, Object> map : props.getDsNhanXuLy()) {
				if ((Boolean) map.get("checked")) {
					if (isFirst == false) {
						sb.append(",");
					}
					sb.append(map.get("email").toString());
					isFirst = false;
				}
			}
		}
		props.setTxtEmailChuyenXuLy(sb.toString());
	}

	private String pmGetDsEmail(List<Map<String, Object>> dsUser) {
		StringBuilder sb = new StringBuilder();
		if (!CollectionUtil.isNullOrEmpty(dsUser)) {
			boolean isFirst = true;
			for (Map<String, Object> map : dsUser) {
				if (isFirst == false) {
					sb.append(",");
				}
				sb.append(map.get("email").toString());
				isFirst = false;
			}
		}
		return sb.toString();
	}

	/**
	 * @author lqthai
	 * @throws EmailException
	 * @throws CloneNotSupportedException
	 * @purpose Lưu lại
	 * @date May 20, 2014 :: 9:41:18 PM
	 */
	public void actionCapNhat(ActionEvent ae) throws EmailException {
		_log.info(">>>>actionCapNhat");
		// mode 1. không chuyển trang, 2. Chuyển trang
		int mode = Integer.parseInt(ae.getComponent().getAttributes()
				.get("mode").toString());
		if(mode == 1){
			soden = props.getObjVBDenModel().getSvb_id();
		}
		this.checkSoDen = Integer.parseInt(ae.getComponent().getAttributes()
				.get("mode").toString());
		Long vbId = props.getObjVBDenModel().getVbden_id();
		if (vbId != null && !objVBDenFacade.isExists(vbId)) {
			props.mode = 0;
			if (props.getSelectMode() == 1) {
				props.setDsVBDenCungHeThong(objVBDenFacade
						.getDsVBDenCungHeThong(getOrgId(), companyid));
				modeTmp = 0;
				idTmp = 0;
			} else if (props.getSelectMode() == 2) {
				props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(companyid));
				modeTmpScan = 0;
				idTmpScan = 0;
			}
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Văn bản đã bị thu hồi',4);");
		} else if (vbId != null && objVBDenFacade.isTiepNhan(vbId)) {
			props.mode = 0;
			if (props.getSelectMode() == 1) {
				props.setDsVBDenCungHeThong(objVBDenFacade
						.getDsVBDenCungHeThong(getOrgId(), companyid));
				modeTmp = 0;
				idTmp = 0;
			} else if (props.getSelectMode() == 2) {
				props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(companyid));
				modeTmpScan = 0;
				idTmpScan = 0;
			}
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Văn bản đã được tiếp nhận',4);");
		} else {
			if (validation()) {
				// Liên kết văn bản
				List<LienKetVanBanModel> dsLkvbThem = new ArrayList<LienKetVanBanModel>();
				// List<LienKetVanBanModel> dsLkvbXoa = new
				// ArrayList<LienKetVanBanModel>();
				// propsLkvb.taoDsLienKet();
				dsLkvbThem = propsLkvb.getDsLienKetThem();

				// @ptgiang Loại bỏ link xem chi tiết
				String escapeLink = CommonUtils.escapeLinkVblq(props
						.getObjVBDenModel().getVbden_phucdap());
				props.getObjVBDenModel().setVbden_phucdap(escapeLink);
				if (props.getObjVBDenModel().getVbden_butphe() != null
						&& !props.getObjVBDenModel().getVbden_butphe().trim()
								.isEmpty()) {
					props.getObjVBDenModel().setVbden_butphe(
							props.getObjVBDenModel().getVbden_butphe().trim());
				} else {
					props.getObjVBDenModel().setVbden_butphe(null);
				}
				// dsLkvbXoa = propsLkvb.getDsLienKetXoa();

				if (String.valueOf(butPheHienTai).equals(
						props.getObjVBDenModel().getVbden_butphe()) == false) {
					if (props.getObjVBDenModel().getVbden_butphe() == null) {// nếu
																				// là
																				// xóa
																				// bút
																				// phê
						props.getObjVBDenModel().setVbden_butphe_userid(null);
						props.getObjVBDenModel().setVbden_butphe_ngay(null);
					} else {
						props.getObjVBDenModel().setVbden_butphe_userid(userId);
						props.getObjVBDenModel().setVbden_butphe_ngay(
								DateUtils.getCurrentDate());
					}
				}
				boolean cungHt = props.getObjVBDenModel().getVbden_id() != null ? true
						: false;
				boolean success = objVBDenFacade.themVanBanDen(
						props.getObjVBDenModel(), // văn bản
						props.getDsTapTinVb(), // tập tin của văn bản
						props.getObjVBDenLuanChuyenModel(), // luân chuyển
						props.getDsTapTinLc(), // tập tin của bước luân chuyển
						props.getObjVBDenLuanChuyenModelTiepNhan(), // bước luân
																	// chuyển
																	// đầu tiên
						props.isTmpChuyenXl(), // trạng thái chuyển xử lý
						props.getObjMailPhoBienModel(),// Log gửi mail
						dsLkvbThem,// liên kết văn bản đã chọn
						propsHsvb.getDsIdHsvb(),// chuỗi id hsvb đã chọn {1,2,3}
						props.getBashPath() + props.getUPLOAD_PATH(),
						props.getBashPath() + props.getTMP_PATH());
				if (cungHt && success) {
					_log.info(">>>Tiếp nhận cùng hệ thống");
					long vbIdGoc;
					int nvbId;
					if (props.getObjVBDenModel().getVbden_id_ldv() != null) {// Tiếp
																				// nhận
																				// từ
																				// chuyển
																				// xử
																				// lý
																				// văn
																				// bản
																				// đến
						nvbId = IContanst.NVB_DEN;
						vbIdGoc = props.getObjVBDenModel().getVbden_id_ldv();
					} else {// Tiếp nhận từ phát hành văn bản đi
						nvbId = IContanst.NVB_DI_PH;
						vbIdGoc = props.getObjVBDenModel().getPhdi_id();
					}
					_log.info(">>>nvbId: " + nvbId + "  vbIdGoc: " + vbIdGoc);
					KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade
							.getObjKetQuaXuLy(vbIdGoc, nvbId, props
									.getObjVBDenModel().getOrganizationid());

					if (kqxlModel != null) {
						kqxlModel.setKqxl_daxem(true);
						kqxlModel.setKqxl_trangthai(IContanst.DA_TIEP_NHAN);// Đã
																			// tiếp
																			// nhận
						kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil
								.getUserFullName(userId));
						kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils
								.getCurrentDate());
						objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);
						_log.info(">>>Thay đổi trạng thái");
					}
				}
				if (success) {
					// @ptgiang Cập nhật nơi nhận liên đơn vị
					if (props.getDsChuyenDenDonVi() != null
							&& !props.getDsChuyenDenDonVi().isEmpty()) {
						List<DmVanBanLienDonViModel> listThemNoiNhanLienDonVi = new ArrayList<DmVanBanLienDonViModel>();
						String userFullNameGui = userUtil.getUserFullName();
						for (Map<String, Object> mapDonVi : props
								.getDsChuyenDenDonVi()) {
							if (Boolean.parseBoolean(mapDonVi.get("chon")
									.toString())) {
								DmVanBanLienDonViModel model = new DmVanBanLienDonViModel();
								model.setLdv_ngaygui(Calendar.getInstance()
										.getTime());
								model.setLdv_nguoigui_userid(getUserId());
								model.setLdv_nhomvb(1);
								model.setLdv_noidung(props
										.getNoiDungChuyenDenDonVi());
								model.setLdv_noinhan_organizationid(Long
										.parseLong(mapDonVi.get(
												"organizationid").toString()));
								model.setLdv_vb_id(props.getObjVBDenModel()
										.getVbden_id());
								model.setTenDonVi(mapDonVi.get("name")
										.toString());
								model.setTenNguoiGui(userFullNameGui);
								model.setThemMoi(true);
								listThemNoiNhanLienDonVi.add(model);
							}
						}
						objBanLienDonViFacade.capNhatNoiNhanVanBan(
								listThemNoiNhanLienDonVi, null, objVBDenFacade
										.getObjVanBanDen(props
												.getObjVBDenModel()
												.getVbden_id()), getOrgId(),
								getOrgName());
					}

					// gửi mail chuyển xử lý văn bản
					if (props.isTmpChuyenXl()
							&& props.getObjVBDenLuanChuyenModelTiepNhan()
									.getVbden_lc_mail()) {
						String vMailChuyenXuLyDen_NoiDung = loader.getParams()
								.get("vMailChuyenXuLyDen_NoiDung").toString();
						vMailChuyenXuLyDen_NoiDung = vMailChuyenXuLyDen_NoiDung
								.replace(
										"[$soHieuGoc$]",
										props.getObjVBDenModel()
												.getVbden_sohieugoc())
								.replace(
										"[$soDen$]",
										props.getObjVBDenModel()
												.getVbden_soden()
												.replace("$", ""))
								.replace(
										"[$trichYeu$]",
										HtmlUtil.escape(props
												.getObjVBDenModel()
												.getVbden_trichyeu()));

						// _log.info(">>>Full url: "+ furl);
						String[] arrMailNhanXuLy = CommonUtils
								.getValidEmails(props.getTxtEmailChuyenXuLy()
										.split(","));
						sendEmailXuLy(props.getObjVBDenModel().getVbden_id(),
								arrMailNhanXuLy, vMailChuyenXuLyDen_NoiDung,
								emailXuLyChinh, props.getDsNhanXuLy());
					}
					// mail phổ biến
					if (props.isMailPhoBien()
							&& props.getPhamViPhoBien() != IContanst.PVPB_KHONGPHOBIEN
							&& (props.getObjVBDenModel().getVbden_hoanthanh() != null && props
									.getObjVBDenModel().getVbden_hoanthanh()
									.booleanValue())) {

						props.getObjMailPhoBienModel().setMail_dsnguoinhan(
								dsMailNhanPhoBien);
						props.getObjMailPhoBienModel().setMail_ngaygui(
								DateUtils.getCurrentDate());
						props.getObjMailPhoBienModel().setMail_nguoigui(
								fullName);
						props.getObjMailPhoBienModel().setMail_nguoiguiid(
								userId);
						props.getObjMailPhoBienModel().setNhomvb(1);
						// _log.info(">>>Ds mail nhận phổ biến: "+dsMailNhanPhoBien);

						try {
							sendEmailPhoBien(props.getTxtEmailPhoBien().split(
									","));
						} catch (Exception e) {
							JavascriptContext.addJavascriptCall(
									FacesContext.getCurrentInstance(),
									"thongBao('Gửi email thất bại',4);");
							_log.error("Err SendMail", e);
						}
					}

					khoiTaoTTThemMoi();
					if (props.mode == 2) {
						props.mode = 0;
						if (props.getSelectMode() == 1) {
							props.setDsVBDenCungHeThong(objVBDenFacade
									.getDsVBDenCungHeThong(getOrgId(),
											companyid));
							modeTmp = 0;
							idTmp = 0;
						} else if (props.getSelectMode() == 2) {
							props.setDsVBDenScan(objVBDenFacade
									.getDsVBDenScan(companyid));
							modeTmpScan = 0;
							idTmpScan = 0;
						}
					}
					String url = "";
					if (mode == 2) {
						url += "window.location ='"
								+ CommonUtils.getPathServer()
								+ "/"
								+ objDmLinkFacade.getLinkModelByMa(
										Hcdt.XU_LY_VAN_BAN_DEN)
										.getLink_giatri() + "';";
					}
					JavascriptContext
							.addJavascriptCall(
									FacesContext.getCurrentInstance(),
									"clearTag('vbpdTag','vbpdValue');"
											+ "thongBao('"+ CommonUtils.getBundleValue("vTbCapNhatThanhCong")+ "',3); " + url);
				} else {
					JavascriptContext.addJavascriptCall(
							FacesContext.getCurrentInstance(),
							"thongBao('Cập nhật dữ liệu không thành công',4);");
				}
				props.setSelectMode2(0);
				canhBaoSoDen = false;
			}
		}
		propsLkvb.setDsLienKetThem(null);
		propsLkvb.clearLienKet();
		propsLkvb.reloadDsVanBan();
		// Load lại danh sách
		props.setDsVBDenCungHeThong(objVBDenFacade.getDsVBDenCungHeThong(props
				.getsSoHieuGoc().trim(), props.getsTrichYeu().trim(), props
				.getsNgayBanHanhTu(), props.getsNgayBanHanhDen(), props
				.getsLoaiVanBan(), props.getsCoQuanBanHanh(), props
				.getsNgayNhanTu(), props.getsNgayNhanDen(), props
				.getsHinhThucNhan(), getOrgIdTemp(), companyid, getListDvUser()));
	}

	private Integer smartChonSoVanBan() {
		Integer svbid = null;
		if (propsSoVanBan.getDsSoVanBan() != null
				&& propsSoVanBan.getDsSoVanBan().size() != 0) {
			int length = propsSoVanBan.getDsSoVanBan().size();
			for (int i = 0; i < length; i++) {
				svbid = propsSoVanBan.getDsSoVanBan().get(i).getSvb_id();
				int stt = (objVBDenFacade.getMaxSoDenTheoSoVanBan(svbid) + 1);
				if (stt <= 99999) {
					break;
				}
			}
		}
		_log.info("<<smartChonSoVanBan {}", svbid);
		return svbid;
	}

	/**
	 * @author lqthai
	 * @purpose Khởi tạo lại thông tin thêm mới
	 * @date May 25, 2014 :: 10:06:51 PM
	 */
	private void khoiTaoTTThemMoi() {
		_log.info(">>>>>khoiTaoLaiTTThemMoi");
//		if(modeChuyenTrang != 1){
			props.setObjVBDenModel(new VBDenModel());
//		}else{
//			Integer svbId = props.getObjVBDenModel().getSvb_id();
//			String 	svbTen = props.getObjVBDenModel().getSvb_ten();
//			props.setObjVBDenModel(new VBDenModel());
//			props.getObjVBDenModel().setSvb_id(svbId);
//			props.getObjVBDenModel().setSvb_ten(svbTen);
//		}
		if (CollectionUtil.isNullOrEmpty(propsLoaiVb.getDsLoaiVanBan())) {
			props.getObjVBDenModel().setLvb_id(null);
		} else {
			props.getObjVBDenModel().setLvb_id(
					propsLoaiVb.getDsLoaiVanBan().get(0).getLvb_id());
		}
		if (CollectionUtil.isNullOrEmpty(propsSoVanBan.getDsSoVanBan())) {
			props.getObjVBDenModel().setSvb_id(null);
		} else {
			props.getObjVBDenModel().setSvb_id(propsSoVanBan.getDsSoVanBan().get(0).getSvb_id());
		}
		if (CollectionUtil.isNullOrEmpty(props.getDsDoMat())) {
			props.getObjVBDenModel().setDm_id(null);
		} else {
			props.getObjVBDenModel().setDm_id(
					props.getDsDoMat().get(0).getDm_id());
		}
		if (CollectionUtil.isNullOrEmpty(props.getDsDoKhan())) {
			props.getObjVBDenModel().setDk_id(null);
		} else {
			props.getObjVBDenModel().setDk_id(
					props.getDsDoKhan().get(0).getDk_id());
		}

		if (CollectionUtil.isNullOrEmpty(propsCachThucXuLy.getDsCachThucXuLy())) {
			props.getObjVBDenModel().setCtxl_id(null);
		} else {
			props.getObjVBDenModel().setCtxl_id(
					propsCachThucXuLy.getDsCachThucXuLy().get(0).getCtxl_id());
		}

		props.getObjVBDenModel().setVbden_linhvuc("{}");
		props.getObjVBDenModel().setVbden_coquanbanhanhid(null);

		props.setDsTapTinVb(new ArrayList<TapTinDinhKemVanBanModel>());
		props.setDsTapTinVbXoa(new ArrayList<TapTinDinhKemVanBanModel>());

		props.setObjVBDenLuanChuyenModel(new VBDen_LuanChuyenModel());
		props.setObjVBDenLuanChuyenModelTiepNhan(new VBDen_LuanChuyenModel());
		props.setDsTapTinLc(new ArrayList<TapTinDinhKemLuanChuyenModel>());

		props.getObjVBDenModel().setVbden_trangthai_lt(false);
		props.getObjVBDenModel().setVbden_thongtin_lt("");
		props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_noidung("");
		props.getObjVBDenModel().setVbden_butphe("");
		props.getObjVBDenModel().setVbden_hoanthanh(false);
		props.setTmpChuyenXl(false);
		props.setPhamViPhoBien(IContanst.PVPB_DEFAULT);
		props.getObjVBDenLuanChuyenModelTiepNhan().setVbden_lc_mail(false);
		props.setObjMailPhoBienModel(new MailPhoBienModel());
		props.setMailPhoBien(false);
		dsMailNhanPhoBien = "";
		actionKhoiTaoSoDen(null);

		props.setChuyenDenDonVi(false);
		props.setNoiDungChuyenDenDonVi("");
		props.setDsChuyenDenDonVi(userUtil.getListMapDonVi(getCompanyId(),
				getOrgId()));

		// dsNhanXuLy
		for (Map<String, Object> mapNxl : props.getDsNhanXuLy()) {
			mapNxl.put("checked", false);
		}
		// dsNhanPhoBien
		for (Map<String, Object> mapNpb : props.getDsNhanPhoBien()) {
			mapNpb.put("checked", false);
		}
		// Người nhận mặc định trong dsNhanXuLy
		if (!loader.getParams().get("vLanhDaoButPhe").equals("-1000")
				&& !loader.getParams().get("vLanhDaoButPhe").equals("")) {
			userIdNhanXl = Long.parseLong(loader.getParams()
					.get("vLanhDaoButPhe").toString());
			Map<String, Object> userTmp = userUtil.getMapNguoiDungTheoId(
					userIdNhanXl, getOrgId());
			userIdXlChinh = userIdNhanXl;
			userTmp.put("checked", true);
			userTmp.put("xlchinh", true);
			boolean test = true;
			for (Map<String, Object> map : props.getDsNhanXuLy()) {
				if (map.get("userid").toString()
						.equals(String.valueOf(userIdNhanXl))) {
					test = false;
					map.put("checked", true);
					map.put("xlchinh", true);
					break;
				}
			}
			if (test) {
				props.getDsNhanXuLy().add(userTmp);
			}
		}

		// Thông tin người dùng
		props.getObjVBDenModel().setUserid(userId);// userid: Ngươi nhập
		props.getObjVBDenModel().setVbden_nguoinhap(fullName); // fullname:
																// người nhập
		props.getObjVBDenModel().setCompanyid(companyid);// Set companyid
		props.getObjVBDenModel().setVbden_ngayden(DateUtils.getCurrentDate());// Set
																				// ngày
																				// đến
																				// là
																				// ngày
																				// hiện
																				// tại
		props.getObjVBDenModel().setOrganizationid(getOrgId());
		props.getObjVBDenModel().setOrganizationten(getOrgName());
		props.getObjVBDenModel().setPhongban_organizationid(phongbanId);
		props.getObjVBDenModel().setVbden_butphe(null);
		butPheHienTai = null;
		// ds lĩnh vực về rỗng
		propsLinhVuc.setDsLinhVucId("{}");
		props.getObjVBDenModel().setVbden_linhvuc("{}");
		for (DmLinhVucModel map : propsLinhVuc.getDsLinhVuc()) {
			map.setChecked(false);
		}

		// xóa đơn vị phát hành
		propsDonVi.tenDonVi = "";
		propsDonVi.dvId = 0;
		for (DmDonViModel dvTmp : propsDonVi.dsDonVi) {
			dvTmp.setChecked(false);
		}
		// Xóa ds liên kết văn bản
		propsLkvb.getProps().getDsVanBanDenLk().clear();
		propsLkvb.getProps().getDsVanBanDiLk().clear();
		propsLkvb.getProps().getDsVanBanNoiBoLk().clear();

		// propsLkvb.getProps().getDsLienKetVBDen().clear();
		// propsLkvb.getProps().getDsLienKetVBDi().clear();
		// propsLkvb.getProps().getDsLienKetVBNoiBo().clear();

		propsLkvb.getProps().setSoLuongLk(0);

		/*
		 * propsLkvb.setVbId(125); propsLkvb.setNhomVb(1);
		 * propsLkvb.getDsVanBanDaLk(125, 1); propsLkvb.getDsVanBan(125, 1);
		 */
		// Xóa hsvb đã chọn
		propsHsvb.setDsIdHsvb("{}");
		propsHsvb.setSoLuongHsvb(0);
		for (Map<String, Object> map : propsHsvb.getDsHoSoVanBan()) {
			map.put("checked", false);
			_log.info(">>>map" + map.toString());
		}
		propsHsvb.setTmpHsvb("");
		// pmGetDsChuyenXuLy();
		// pmGetDsNhanPhoBien();
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"showHideChuyenDonVi( $('.chkChyenDenDonVi').prop('checked') );");
	}

	public void actionKhoiTaoSoDen(ActionEvent ae) {
		try {
			_log.info(">>actionKhoiTaoSoDen");
			if (propsSoVanBan.getDsSoVanBan() != null
					&& propsSoVanBan.getDsSoVanBan().size() > 0) {
				// max so đến
				// _log.info(">>> Svb id: "+propsSoVanBan.getDsSoVanBan().get(0).getSvb_id());
				// _log.info(">>> max so den: "+objVBDenFacade.getMaxSoDenTheoSoVanBan(propsSoVanBan.getDsSoVanBan().get(0).getSvb_id()));

				// Chọn mặc định sổ văn bả là sổ đầu tiên
				if(this.checkSoDen==1){
					props.getObjVBDenModel().setSvb_id(soden);
				}else{
					props.getObjVBDenModel().setSvb_id(smartChonSoVanBan());
				}
				//

				int soDen = objVBDenFacade.getMaxSoDenTheoSoVanBan(props
						.getObjVBDenModel().getSvb_id()) + 1;
				_log.info("SD: "+ soDen);
				if (soDen > 99999) {
					props.setSoDen("");
					Validator.showErrorMessage(getPortletId(),
									"frm:txtSoDen",
									"Số đến không thể vượt quá 99999.\nHiệu chỉnh số nhỏ hơn hoặc chọn sổ văn bản khác");
					props.setSuaSoDen(true);
				} else {
					if (checkSoDen != 1) {
						props.setSoDen((objVBDenFacade.getMaxSoDenTheoSoVanBan(props.getObjVBDenModel().getSvb_id()) + 1)+ "");
					} else {
						props.setSoDen(""+ (Integer.parseInt(props.getSoDen()) + 1));
					}
					props.setSuaSoDen(false);
				}

				props.setSoDenMa(objDmSoVanBanFacade.getChuoiMacDinh(props
						.getObjVBDenModel().getSvb_id()));
				props.setSoDenVitri(objDmSoVanBanFacade.getViTriStt(props
						.getObjVBDenModel().getSvb_id()));
			} else {
				props.setSoDenMa("");
				props.setSoDenVitri(true);
			}

		} catch (Exception ex) {
			_log.error("err ", ex);
		}

	}

	/**
	 * 
	 * @author hltphat
	 * @purpose Tải tập tin bằng link
	 * @date Dec 7, 2017 :: 9:34:39 AM
	 * @param event
	 * @throws IOException 
	 */
	public void actionUploadFileFromLink(ActionEvent event)  {
		_log.info(">>>>>>>>>>actionUploadFileFromLink<<<<<<<<<<<<");
		try{
			String linkFile = props.getLinkFile();
			_log.info(">>>>>>>>>>KIEM TRA LINH: "+linkFile);
			Pattern r = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
			Matcher mc = r.matcher(linkFile);
			if(!mc.matches()){
				Validator.showErrorMessage(getPortletId(), "frm:tmpErrFileLink",
						"Link không hợp lệ");
			}else{
				//tải file về
				URL url = new URL(linkFile);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				int responseCode = httpConn.getResponseCode();
				// always check HTTP response code first
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		        	String fileName = "";
		            String disposition = httpConn.getHeaderField("Content-Disposition");
		            String contentType = httpConn.getContentType();
		            String tenmoi = DigestUtils.md5Hex(new Random().nextInt(9999) + fileName)
		    				+ "."+ FileNameUtil.getFileNameExtension(fileName);
		            int contentLength = httpConn.getContentLength();
		            if (disposition != null) {
		                // extracts file name from header field
		                int index = disposition.indexOf("filename=");
		                if (index > 0) {
		                    fileName = disposition.substring(index + 10,
		                            disposition.length() - 1);
		                }
		            } else {
		                // extracts file name from URL
		                fileName = linkFile.substring(linkFile.lastIndexOf("/") + 1,
		                		linkFile.length());
		            }
		            InputStream inputStream = httpConn.getInputStream();
		            String saveFilePath = props.getBashPath() + props.getTMP_PATH()
							 + File.separator + fileName;
		            // opens an output stream to save into file
		            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
		            
		            int bytesRead = -1;
		            byte[] buffer = new byte[4096];
		            while ((bytesRead = inputStream.read(buffer)) != -1) {
		                outputStream.write(buffer, 0, bytesRead);
		            }
		            outputStream.close();
		            inputStream.close();
		            //end tải tập tin
		            _log.info(">>>>>>>FILE NAME: {}",fileName);
		            _log.info(">>>>>>>FILE TYPE: {}",contentType);
		            _log.info(">>>>>>>FILE SIZE: {}",contentLength);
		            
		            //load file lên danh sách
		            TapTinDinhKemVanBanModel fileObj = new TapTinDinhKemVanBanModel();
					fileObj.setTtdk_tenhienthi(fileName);
					fileObj.setTtdk_type(contentType);
					fileObj.setTtdk_tenluutru(tenmoi);
					fileObj.setTtdk_ngaytai(DateUtils.getCurrentDate());
					fileObj.setTtdk_nhomvb(1);// 1. van ban den;
					fileObj.setTtdk_size(Long.parseLong(""+contentLength));
					fileObj.setTtdk_userid(userId);
					fileObj.setTtdk_nguoitai(getFullName());
					File f = new File(props.getBashPath() + props.getTMP_PATH()
							+ fileObj.getTtdk_tenhienthi());
					File newFile = new File(props.getBashPath()
							+ props.getTMP_PATH() + fileObj.getTtdk_tenluutru());
					if (f.exists()) {
						f.renameTo(newFile);
					}
					Resource fRe = new HCDTResource(newFile);
					fileObj.setDownload(fRe);
					fileObj.setUploadnew(true);
					fileObj.setPath(props.getBashPath() + props.getTMP_PATH()
							+ fileObj.getTtdk_tenluutru());
					fileObj.setType(CommonUtils.getFileExtension(newFile));
					// ds sử dụng lưu tập tin
					props.setLinkFile("");
					props.getDsTapTinVb().add(fileObj);
		        } else {
		        	_log.info("No file to download. Server replied HTTP code: " + responseCode);
		        }
		        httpConn.disconnect();
			}
		}catch(IOException ex){
			_log.info("Err Link File: " + ex.getMessage());
		}
	}

	/**
	 * @author lqthai
	 * @purpose Tải lên tập tin đính kèm
	 * @date May 20, 2014 : * @param event
	 */
	public void actionUploadFile(FileEntryEvent event) {
		_log.info(">>actionUploadFile");
		try {
			FileEntryResults fileResult = ((FileEntry) event.getComponent())
					.getResults();
			for (FileEntryResults.FileInfo fileInfo : fileResult.getFiles()) {
				long filesize = 0;
				long sizelimit = 0;

				if (fileInfo != null) {
					filesize = fileInfo.getSize();
					sizelimit = Integer.parseInt(loader.getParams()
							.get("vKichThuocFile").toString()) * 1024 * 1024;
					// _log.info("sizelimit: "+sizelimit);
				}

				if (filesize == 0) {
					fileInfo.updateStatus(new MaxFileSizeStatus(
							"Không thể tải tập tin rỗng (0 Kb)"), false, true);
				} else if (CommonUtils
						.checkFileExt(fileInfo.getFileName(), CommonUtils
								.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(),
							false, true);
				} else if (fileInfo.getSize() > sizelimit) {
					fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
				} else if (Boolean.parseBoolean(CommonUtils
						.getMimeTypeBundleValue("checkMimeType").toString()) == true
						&& CommonUtils
								.isValidMimeType(
										fileInfo.getFile(),
										CommonUtils
												.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false,
							true);
				} else if (fileInfo.isSaved()) {
					String tenmoi = DigestUtils.md5Hex(new Random()
							.nextInt(9999) + fileInfo.getFileName())
							+ "."
							+ FileNameUtil.getFileNameExtension(fileInfo
									.getFileName());
					TapTinDinhKemVanBanModel fileObj = new TapTinDinhKemVanBanModel();
					fileObj.setTtdk_tenhienthi(fileInfo.getFileName());
					fileObj.setTtdk_type(fileInfo.getContentType());
					fileObj.setTtdk_tenluutru(tenmoi);
					fileObj.setTtdk_ngaytai(DateUtils.getCurrentDate());
					fileObj.setTtdk_nhomvb(1);// 1. van ban den;
					fileObj.setTtdk_size(fileInfo.getSize());
					fileObj.setTtdk_userid(userId);
					fileObj.setTtdk_nguoitai(getFullName());
					File f = new File(props.getBashPath() + props.getTMP_PATH()
							+ fileObj.getTtdk_tenhienthi());
					File newFile = new File(props.getBashPath()
							+ props.getTMP_PATH() + fileObj.getTtdk_tenluutru());
					if (f.exists()) {
						f.renameTo(newFile);
					}
					Resource fRe = new HCDTResource(newFile);
					_log.info("Duong dan>>>>>: {}", props.getBashPath() + props.getTMP_PATH());
					fileObj.setDownload(fRe);
					fileObj.setUploadnew(true);
					fileObj.setPath(props.getBashPath() + props.getTMP_PATH()
							+ fileObj.getTtdk_tenluutru());
					fileObj.setType(CommonUtils.getFileExtension(newFile));
					// ds sử dụng lưu tập tin
					props.getDsTapTinVb().add(fileObj);
					_log.info("---------fileObj.file_id--------{}");
				}
			}

		} catch (Exception ex) {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"uploadBtn_show('fileUpload','tmpUpload')");
			ex.printStackTrace();
		}
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"uploadBtn_show('fileUpload','tmpUpload')");

	}

	/**
	 * @author lqthai
	 * @purpose Xóa tập tin đính kèm
	 * @date May 20, 2014 :: 6:24:21 PM
	 * @param ae
	 */
	public void actionXoaFileDinhKem(ActionEvent ae) {
		_log.info(">>actionXoaFileDinhKem");
		TapTinDinhKemVanBanModel obj = new TapTinDinhKemVanBanModel();
		obj = (TapTinDinhKemVanBanModel) ae.getComponent().getAttributes()
				.get("file");
		if (obj != null) {
			try {
				_log.info("Xóa file");
				if (obj.getTtdk_id() != null) {
					_log.info(">>>>THêm vào ds xóa file");
					obj.setDel(true);
					props.getDsTapTinVbXoa().add(obj);// thêm vào ds xóa
				}
				props.getDsTapTinVb().remove(obj);
			} catch (Exception e) {
				_log.info(e.toString());
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Tải lên tập tin đính kèm bước luân chuyển
	 * @date May 20, 2014 :: 5:02:27 PM
	 * @param event
	 */
	public void actionUploadFileLuanChuyen(FileEntryEvent event) {
		_log.info(">>actionUploadFileLuanChuyen");
		try {
			FileEntryResults fileResult = ((FileEntry) event.getComponent())
					.getResults();
			for (FileEntryResults.FileInfo fileInfo : fileResult.getFiles()) {
				long filesize = 0;
				long sizelimit = 0;

				if (fileInfo != null) {
					filesize = fileInfo.getSize();
					sizelimit = Integer.parseInt(loader.getParams()
							.get("vKichThuocFile").toString()) * 1024 * 1024;
					// _log.info("sizelimit: "+sizelimit);
				}

				if (filesize == 0) {
					fileInfo.updateStatus(new MaxFileSizeStatus(
							"Không thể tải tập tin rỗng (0 Kb)"), false, true);
				} else if (CommonUtils
						.checkFileExt(fileInfo.getFileName(), CommonUtils
								.getMimeTypeBundleValue("filedinhkemExtension")) == false) {
					fileInfo.updateStatus(new CheckFileExtensionStatus(),
							false, true);
				} else if (fileInfo.getSize() > sizelimit) {
					fileInfo.updateStatus(new MaxFileSizeStatus(), false, true);
				} else if (Boolean.parseBoolean(CommonUtils
						.getMimeTypeBundleValue("checkMimeType").toString()) == true
						&& CommonUtils
								.isValidMimeType(
										fileInfo.getFile(),
										CommonUtils
												.getMimeTypeBundleValue("filedinhkemValidMimeType")) == false) {
					fileInfo.updateStatus(new CheckMimeTypeStatus(), false,
							true);
				} else if (fileInfo.isSaved()) {
					String tenmoi = DigestUtils.md5Hex(new Random()
							.nextInt(9999) + fileInfo.getFileName())
							+ "."
							+ FileNameUtil.getFileNameExtension(fileInfo
									.getFileName());
					TapTinDinhKemLuanChuyenModel fileObj = new TapTinDinhKemLuanChuyenModel();
					fileObj.setLctt_tenhienthi(fileInfo.getFileName());
					fileObj.setLctt_type(fileInfo.getContentType());
					fileObj.setLctt_tenluutru(tenmoi);
					fileObj.setLctt_ngaytai(DateUtils.getCurrentDate());
					fileObj.setLctt_nhomvb(1);// 1. van ban den;
					fileObj.setLctt_size(fileInfo.getSize());
					fileObj.setUserid(userId);
					fileObj.setLctt_nguoitai(getFullName());
					File f = new File(props.getBashPath() + props.getTMP_PATH()
							+ "vb_den_lc/" + fileObj.getLctt_tenhienthi());
					File newFile = new File(props.getBashPath()
							+ props.getTMP_PATH() + "vb_den_lc/"
							+ fileObj.getLctt_tenluutru());
					if (f.exists()) {
						f.renameTo(newFile);
					}
					Resource fRe = new HCDTResource(newFile);
					fileObj.setDownload(fRe);
					fileObj.setPath(props.getBashPath() + props.getTMP_PATH()
							+ "vb_den_lc/" + fileObj.getLctt_tenluutru());
					fileObj.setType(CommonUtils.getFileExtension(newFile));
					props.getDsTapTinLc().add(fileObj);
					_log.info("---------fileObj.file_id--------{}");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"uploadBtn_show('fileUploadLc','tmpUploadLc')");
		}
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"uploadBtn_show('fileUploadLc','tmpUploadLc')");

	}

	/**
	 * @author lqthai
	 * @purpose Xóa tập tin đính kèm bước luân chuyển
	 * @date May 20, 2014 :: 6:24:21 PM
	 * @param ae
	 */
	public void actionXoaFileDinhKemLuanChuyen(ActionEvent ae) {
		_log.info(">>actionXoaFileDinhKemLuanChuyen");
		TapTinDinhKemLuanChuyenModel obj = new TapTinDinhKemLuanChuyenModel();
		obj = (TapTinDinhKemLuanChuyenModel) ae.getComponent().getAttributes()
				.get("file");
		if (obj != null) {
			try {
				_log.info("Xóa file");
				props.getDsTapTinLc().remove(obj);
			} catch (Exception e) {
				_log.info(e.toString());
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose tiếp nhận văn bản đến cùng hệ thống + scan
	 * @date May 25, 2014 :: 9:44:38 PM
	 * @param ae
	 */
	public void actionTiepNhanVanBan(Long orgId, Long vbId) {
		try {
			// long vbId =
			// Long.parseLong(ae.getComponent().getAttributes().get("vbId").toString());
			// long OrgId =
			// Long.parseLong(ae.getComponent().getAttributes().get("vbOgrId").toString());
			_log.info("Hiện tại Orgid là: " + getOrgId() + ", " + orgId);
			setOrgId(orgId);
			setRunRefesh(false);
			actionDonViChange();
			actionTiepNhanVanBan(vbId);
		} catch (Exception e) {
			_log.error("Err ", e);
		}
	}

	public void actionTiepNhanVanBan(long vbId) {
		if (!objVBDenFacade.isExists(vbId)) {
			if (props.getSelectMode() == 1) {// tiếp nhận vb cung he thong
				JavascriptContext.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"thongBao('Văn bản đã bị thu hồi',4);");
				props.setDsVBDenCungHeThong(objVBDenFacade
						.getDsVBDenCungHeThong(getOrgId(), companyid));
			} else if (props.getSelectMode() == 2) {// tiếp nhận vb scan
				JavascriptContext.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"thongBao('Văn bản không tồn tại',4);");
				props.setDsVBDenScan(objVBDenFacade.getDsVBDenScan(companyid));
			}
		} else if (objVBDenFacade.isTiepNhan(vbId)) {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Văn bản đã được tiếp nhận',4);");
		} else {
			props.mode = 2;
			if (props.getSelectMode() == 1) {
				modeTmp = props.mode;
				idTmp = vbId;
				tiepNhanVanBanTheoId(vbId, 1);// tiếp nhận vb cung he thong
				props.setSelectMode2(0);// trở về tab đầu
			} else if (props.getSelectMode() == 2) {
				modeTmpScan = props.mode;
				idTmpScan = vbId;
				tiepNhanVanBanTheoId(vbId, 2);// tiếp nhận vb scan
				props.setSelectMode2(0);// trở về tab đầu
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Xóa văn bản (Xóa ở ds cùng hệ thống và scan)
	 * @date Jun 25, 2014 :: 2:55:47 PM
	 * @param ae
	 */
	public void actionXoaVanBan(long vbId, long loai) {
		// long vbId =
		// Long.parseLong(ae.getComponent().getAttributes().get("vbId").toString());
		// long loai =
		// Long.parseLong(ae.getComponent().getAttributes().get("loai").toString());
		if (!objVBDenFacade.isExists(vbId)) {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Văn bản không tồn tại',4);");
		} else if (objVBDenFacade.isTiepNhan(vbId)) {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Văn bản đã được tiếp nhận',4);");
		} else {

			// Cập nhật lại kết quả xử lý là đã xóa
			VBDenModel objVBDenModel = objVBDenFacade.getObjVanBanDen(vbId);
			long vbIdGoc;
			int nvbId;
			if (objVBDenModel.getVbden_id_ldv() != null) {// Tiếp nhận từ chuyển
															// xử lý văn bản đến
				nvbId = IContanst.NVB_DEN;
				vbIdGoc = objVBDenModel.getVbden_id_ldv();
			} else {// Tiếp nhận từ phát hành văn bản đi
				nvbId = IContanst.NVB_DI_PH;
				vbIdGoc = objVBDenModel.getPhdi_id();
			}

			KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade.getObjKetQuaXuLy(
					vbIdGoc, nvbId, objVBDenModel.getOrganizationid());

			if (kqxlModel == null) {
				kqxlModel = new KetQuaXuLyModel();
				kqxlModel.setVb_id(vbIdGoc);
				kqxlModel.setNvb_id(IContanst.NVB_DEN);
				try {
					kqxlModel.setKqxl_donvinhan_ten(userUtil.getOrganization(
							objVBDenModel.getOrganizationid()).getName());
				} catch (Exception e) {
					_log.error("Err get getOrgName() {}",
							objVBDenModel.getOrganizationid());
				}
				kqxlModel.setKqxl_donvinhan_organizationid(objVBDenModel
						.getOrganizationid());
				kqxlModel.setKqxl_loaidonvi(IContanst.LDV_CUNGHT);
			}

			kqxlModel.setKqxl_daxem(true);
			kqxlModel.setKqxl_trangthai(IContanst.DA_XOA);// Đã xóa
			kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil
					.getUserFullName(userId));
			kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils.getCurrentDate());
			objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);

			objVBDenFacade.xoaVanBan(vbId);
		}

		if (loai == 1) {
			// props.dsVBDenCungHeThong =
			// objVBDenFacade.getDsVBDenCungHeThong(companyid);
			actionTimKiemVBDenCungHt();
		} else {
			actionTimKiemVBDenScan();
			// props.dsVBDenScan = objVBDenFacade.getDsVBDenScan(companyid);
		}
	}

	/**
	 * @author lqthai
	 * @purpose Hủy bỏ tiếp nhận
	 * @date May 25, 2014 :: 11:01:40 PM
	 */
	public void actionHuyBo() {
		props.mode = 0;
		if (props.getSelectMode() == 1) {

			modeTmp = 0;
			idTmp = 0;
		} else if (props.getSelectMode() == 2) {
			modeTmpScan = 0;
			idTmpScan = 0;
		}
		// url="tab:"+props.selectMode+",mode:0,id:0";
		// setParamsURL(url);
	}

	/**
	 * @author lqthai
	 * @purpose Xem văn bản cùng hệ thống, văn bản scan.
	 * @date May 25, 2014 :: 11:00:24 PM
	 * @param vbId
	 * @param loai
	 *            1. cùng hệ thống, 2. Scan
	 */
	public void tiepNhanVanBanTheoId(long vbId, int loai) {
		VBDenModel objVBDenModel = objVBDenFacade.getObjVanBanDen(vbId);
		_log.info("objVBDenModel {}", objVBDenModel);
		if (objVBDenModel != null && objVBDenModel.getVbden_id() > 0) {

			// props.setObjVBDenModel(new VBDenModel());
			props.setObjVBDenModel(objVBDenModel);

			// @ptgiang
			// Reset id các danh mục do các danh mục lấy theo id riêng đơn vị
			props.getObjVBDenModel().setLvb_id(null);
			props.getObjVBDenModel().setDm_id(null);
			props.getObjVBDenModel().setDk_id(null);
			props.getObjVBDenModel().setCtxl_id(null);
			props.getObjVBDenModel().setSvb_id(null);
			props.getObjVBDenModel().setVbden_linhvuc("{}");

			if (props.getObjVBDenModel().getVbden_butphe() != null
					&& !props.getObjVBDenModel().getVbden_butphe().trim()
							.isEmpty()) {
				props.getObjVBDenModel().setVbden_butphe(
						props.getObjVBDenModel().getVbden_butphe().trim());
			} else {
				props.getObjVBDenModel().setVbden_butphe(null);
			}
			butPheHienTai = props.getObjVBDenModel().getVbden_butphe();
			/*
			 * try { props.getObjVBDenModel().setCunght_organizationten(
			 * OrganizationLocalServiceUtil
			 * .getOrganization(props.getObjVBDenModel
			 * ().getCunght_organizationid()).getName()); } catch
			 * (PortalException e) { e.printStackTrace(); } catch
			 * (SystemException e) { e.printStackTrace(); }
			 */
			if (loai == 1) {// Tiếp nhận văn bản đến cùng hệ thống
				// Cập nhật thông tin đã xem
				// _log.info("getVbden_id_ldv {}",
				// objVBDenModel.getVbden_id_ldv());
				// _log.info("objVBDenModel {}", objVBDenModel);
				//
				long vbIdGoc;
				int nvbId;
				if (objVBDenModel.getVbden_id_ldv() != null) {// Tiếp nhận từ
																// chuyển xử lý
																// văn bản đến
					nvbId = IContanst.NVB_DEN;
					vbIdGoc = objVBDenModel.getVbden_id_ldv();
				} else {// Tiếp nhận từ phát hành văn bản đi
					nvbId = IContanst.NVB_DI_PH;
					vbIdGoc = objVBDenModel.getPhdi_id();
				}

				KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade
						.getObjKetQuaXuLy(vbIdGoc, nvbId,
								objVBDenModel.getOrganizationid());
				boolean daXem = false;
				_log.info("kqxlModel {}", kqxlModel);
				if (kqxlModel != null
						&& kqxlModel.getKqxl_donvinhan_nguoixem_userid() != null) {
					daXem = true;
				}
				_log.info("daXem {}", daXem);
				if (!daXem) {
					if (kqxlModel == null) {
						kqxlModel = new KetQuaXuLyModel();
						kqxlModel.setVb_id(vbIdGoc);
						kqxlModel.setNvb_id(IContanst.NVB_DEN);
						try {
							kqxlModel.setKqxl_donvinhan_ten(userUtil
									.getOrganization(
											objVBDenModel.getOrganizationid())
									.getName());
						} catch (Exception e) {
							_log.error("Err get getOrgName() {}",
									objVBDenModel.getOrganizationid());
						}
						kqxlModel
								.setKqxl_donvinhan_organizationid(objVBDenModel
										.getOrganizationid());
						kqxlModel.setKqxl_loaidonvi(IContanst.LDV_CUNGHT);
					}

					kqxlModel.setKqxl_daxem(true);
					kqxlModel.setKqxl_trangthai(IContanst.DA_XEM);// Đã xem
					kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil
							.getUserFullName(userId));
					kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils
							.getCurrentDate());
					objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);
				}

				// props.getObjVBDenModel().setSvb_id(smartChonSoVanBan());

				// int soDen =
				// objVBDenFacade.getMaxSoDenTheoSoVanBan(props.getObjVBDenModel().getSvb_id());
				// if(soDen>99999){
				// props.setSoDen("");
				// Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
				// "Số đến không thể vượt quá 99999.\nHiệu chỉnh số nhỏ hơn hoặc chọn sổ văn bản khác");
				// props.setSuaSoDen(true);
				// }else{
				// props.setSoDen(objVBDenFacade.getMaxSoDenTheoSoVanBan(props.getObjVBDenModel().getSvb_id())+"");
				// props.setSuaSoDen(false);
				// }
				// props.setSoDenMa(objDmSoVanBanFacade.getChuoiMacDinh(props.getObjVBDenModel().getSvb_id()));
				//
				// props.setSoDenVitri(objDmSoVanBanFacade.getViTriStt(props.getObjVBDenModel().getSvb_id()));

				// props.getObjVBDenModel().setSvb_id(propsSoVanBan.getDsSoVanBan().get(0).getSvb_id());
				// Set lại các thông tin người dùng
				props.getObjVBDenModel().setUserid(userId);// userid: Ngươi nhập
				props.setNguoiChuyenCungHt(props.getObjVBDenModel()
						.getVbden_nguoinhap());
				if(props.getObjVBDenModel().getVbden_butphe() != "" && props.getObjVBDenModel().getVbden_butphe() != null){
					props.getObjVBDenModel().setVbden_butphelanhdao_ngay(props.getObjVBDenModel().getVbden_butphe_ngay());
					props.getObjVBDenModel().setVbden_butphelanhdao(props.getObjVBDenModel().getVbden_butphe());
					props.getObjVBDenModel().setVbden_butphelanhdao_userid(props.getObjVBDenModel().getVbden_butphe_userid());
				}
				props.setNguoiButPhe(UserUtil.getUserFullName(props.getObjVBDenModel().getVbden_butphe_userid()));
				props.getObjVBDenModel().setCunght_nguoichuyen(
						props.getObjVBDenModel().getVbden_nguoinhap());
				props.getObjVBDenModel().setVbden_nguoinhap(fullName); // fullname:
																		// người
																		// nhập
				props.getObjVBDenModel().setCompanyid(companyid);// Set
																	// companyid
				// Số đến là rỗng nếu là đến cùng hệ thống
				props.getObjVBDenModel().setVbden_soden("");
				// FIXME cơ quan bàn hành là đơn vị ngoài hệ thống sẽ lỗi id org
				// ko tồn tại @ptgiang Tắt code do danh mục lấy theo đơn vị
				try {
					Organization orgCungHt = userUtil.getOrganization(props
							.getObjVBDenModel().getVbden_coquanbanhanhid());

					if (orgCungHt != null) {
						propsDonVi.dvId = Integer.parseInt(props
								.getObjVBDenModel().getVbden_coquanbanhanhid()
								.toString());
						propsDonVi.tenDonVi = props.getObjVBDenModel()
								.getVbden_coquanbanhanh();
					} else {
						props.getObjVBDenModel().setVbden_coquanbanhanhid(null);
					}
				} catch (Exception e) {
					_log.error("Err check org cùng hệ thống ", e.getMessage());
				}

				objVBDenModel.setVbden_ngayden(DateUtils.getCurrentDate());

				// @ptgiang Trường hợp tiếp nhận từ phát hành văn bản đi cùng hệ
				// thống:
				// thông tin "Phúc đáp cho văn bản",
				// "Nơi nhận/Bộ phận thực hiện", "Ghi chú" sẽ được clear khi
				// được tiếp nhận
				if (props.getObjVBDenModel().getVbden_id_ldv() == null) {
					// Thời hạn xử lý yêu cầu từ phát hành
					/*
					 * VBDi_PhatHanhModel vbPhModel =
					 * objVBDi_PhatHanhFacade.getObjVBDi_PhatHanhModel
					 * (props.getObjVBDenModel().getPhdi_id()); if( vbPhModel !=
					 * null ){
					 * props.getObjVBDenModel().setThoiHanXuLyTuPh(vbPhModel
					 * .getPhdi_thoihanthuchien());
					 * props.getObjVBDenModel().setTenDonViPh
					 * (vbPhModel.getOrganizationten()); }
					 */
					props.getObjVBDenModel().setVbden_phucdap("");
					props.getObjVBDenModel().setVbden_bophanthuchien(null);
					props.getObjVBDenModel().setVbden_ghichu(null);
					// preload 1 số dm bị mất do dm chuyển san quản lý theo đơn
					// vị
					// load obj gốc
					VBDi_PhatHanhModel vbPhModel = objVBDi_PhatHanhFacade
							.getObjVBDi_PhatHanhModel(props.getObjVBDenModel()
									.getPhdi_id());
					_log.info("vbPhModel {}", vbPhModel);
					// set lại độ mật, độ khẩn, loại văn bản, lĩnh vực
					// XXX trường hợp nhận từ phát hành không load lại: cách
					// thức xử lý(không có), cơ quan ban hành là đơn vị gửi phát
					// hành đi(mặc định)
					if (props.getDsDoKhan() != null) {
						for (DmDoKhanModel m : props.getDsDoKhan()) {
							if (m.getDk_ten().equalsIgnoreCase(
									vbPhModel.getDk_ten())) {
								props.getObjVBDenModel().setDk_id(m.getDk_id());
								props.getObjVBDenModel().setDk_ten(
										m.getDk_ten());
								break;
							}
						}
					}
					if (props.getDsDoMat() != null) {
						for (DmDoMatModel m : props.getDsDoMat()) {
							if (m.getDm_ten().equalsIgnoreCase(
									vbPhModel.getDm_ten())) {
								props.getObjVBDenModel().setDm_id(m.getDm_id());
								props.getObjVBDenModel().setDm_ten(
										m.getDm_ten());
								break;
							}
						}
					}
					if (propsLoaiVb.getDsLoaiVanBan() != null) {
						for (DmLoaiVanBanModel m : propsLoaiVb
								.getDsLoaiVanBan()) {
							if (m.getLvb_ten().equalsIgnoreCase(
									vbPhModel.getLvb_ten())) {
								props.getObjVBDenModel().setLvb_id(
										m.getLvb_id());
								props.getObjVBDenModel().setLvb_ten(
										m.getLvb_ten());
								break;
							}
						}
					}

					props.getObjVBDenModel()
							.setVbden_linhvuc(
									objVBDenFacade.getDsLinhVucId_ChuyenPhongBan(
											vbPhModel.getPhdi_linhvuc(),
											props.getObjVBDenModel()
													.getPhongban_organizationid() == null ? getOrgId()
													: props.getObjVBDenModel()
															.getPhongban_organizationid()));
					if (props.getObjVBDenModel().getVbden_linhvuc() != null
							&& props.getObjVBDenModel().getVbden_linhvuc()
									.isEmpty() == false) {
						String[] dsLv = props.getObjVBDenModel()
								.getVbden_linhvuc().replace("{", "")
								.replace("}", "").split(",");
						for (String id : dsLv) {
							if (id.trim().isEmpty() == false) {
								for (DmLinhVucModel lv : propsLinhVuc
										.getDsLinhVuc()) {
									if (Integer.parseInt(id) == lv.getLv_id()) {
										lv.setChecked(true);
									}
								}
							}
						}
						propsLinhVuc.setDsLinhVucId(props.getObjVBDenModel()
								.getVbden_linhvuc());
					} else {
						propsLinhVuc.setDsLinhVucId("{}");
					}
				} else {// trường hợp nhận từ chức năng chuyển đến đơn vị
						// preload 1 số dm bị mất do dm chuyển san quản lý theo
						// đơn vị
					VBDenModel mGoc = objVBDenFacade
							.getObjVanBanDen(objVBDenModel.getVbden_id_ldv());

					_log.info(
							"props.getObjVBDenModel().getPhongban_organizationid() {}",
							props.getObjVBDenModel()
									.getPhongban_organizationid());

					Map<String, Object> mThongTin = objVBDenFacade
							.getDanhMucId_ChuyenPhongBan(
									mGoc.getLvb_id(),
									mGoc.getDm_id(),
									mGoc.getDk_id(),
									mGoc.getCtxl_id(),
									mGoc.getVbden_linhvuc(),
									mGoc.getVbden_coquanbanhanhid(),
									props.getObjVBDenModel()
											.getPhongban_organizationid() == null ? getOrgId()
											: props.getObjVBDenModel()
													.getPhongban_organizationid(),
									mGoc.getSvb_id());
					_log.info("mThongTin {}", mThongTin);

					props.getObjVBDenModel().setDk_id(
							NumberUtils.getInteger(mThongTin.get("dk_id"), -1));
					props.getObjVBDenModel().setDm_id(
							NumberUtils.getInteger(mThongTin.get("dm_id"), -1));
					props.getObjVBDenModel()
							.setCtxl_id(
									NumberUtils.getInteger(
											mThongTin.get("ctxl_id"), -1));
					props.getObjVBDenModel()
							.setLvb_id(
									NumberUtils.getInteger(
											mThongTin.get("lvb_id"), -1));
					props.getObjVBDenModel()
							.setSvb_id(
									NumberUtils.getInteger(
											mThongTin.get("svb_id"), -1));
					props.getObjVBDenModel().setVbden_linhvuc(
							mThongTin.get("lv_id") == null ? "{}" : mThongTin
									.get("lv_id").toString());
					if (props.getObjVBDenModel().getVbden_linhvuc() != null
							&& props.getObjVBDenModel().getVbden_linhvuc()
									.isEmpty() == false) {
						String[] dsLv = props.getObjVBDenModel()
								.getVbden_linhvuc().replace("{", "")
								.replace("}", "").split(",");
						for (String id : dsLv) {
							if (id.trim().isEmpty() == false) {
								for (DmLinhVucModel lv : propsLinhVuc
										.getDsLinhVuc()) {
									if (Integer.parseInt(id) == lv.getLv_id()) {
										lv.setChecked(true);
									}
								}
							}
						}
					} else {
						propsLinhVuc.setDsLinhVucId("{}");
					}
					propsLinhVuc.setDsLinhVucId(props.getObjVBDenModel()
							.getVbden_linhvuc());
					props.getObjVBDenModel().setVbden_coquanbanhanhid(
							NumberUtils.getLong(mThongTin.get("cq_id"), -1L));

					// FIXME cơ quan ban hành load được id những chưa lên giao
					// diện
					if (props.getObjVBDenModel().getVbden_coquanbanhanhid() != null) {
						try {
							propsDonVi.tenDonVi = objDmDonViFacade
									.getObjDonViModel(
											props.getObjVBDenModel()
													.getVbden_coquanbanhanhid()
													.intValue()).getDv_ten();
							propsDonVi.dvId = props.getObjVBDenModel()
									.getVbden_coquanbanhanhid().intValue();
							for (DmDonViModel dvTmp : propsDonVi.dsDonVi) {
								if (dvTmp.getDv_id() == props
										.getObjVBDenModel()
										.getVbden_coquanbanhanhid().intValue()) {
									dvTmp.setChecked(true);
									break;
								}
							}
						} catch (Exception e) {
							_log.error("Không tìm được đơn vị với id {} ",
									props.getObjVBDenModel()
											.getVbden_coquanbanhanhid());
						}
						try {
							propsDonVi.tenDonVi = OrganizationLocalServiceUtil
									.getOrganization(
											props.getObjVBDenModel()
													.getVbden_coquanbanhanhid())
									.getName();
							propsDonVi.dvId = props.getObjVBDenModel()
									.getVbden_coquanbanhanhid().intValue();
							for (DmDonViModel dvTmp : propsDonVi.dsDonVi) {
								if (dvTmp.getDv_id() == props
										.getObjVBDenModel()
										.getVbden_coquanbanhanhid().intValue()) {
									dvTmp.setChecked(true);
									break;
								}
							}
						} catch (Exception e) {
							_log.error("Không tìm được đơn vị với id {} ",
									props.getObjVBDenModel()
											.getVbden_coquanbanhanhid());
						}
					}
				}

				// clear bút phê, phần bút phê là của đơn vị nhận, bút phê của
				// đơn vị gửi ko gửi kèm văn bản (chỉ có ý kiến khi phát hành là
				// có kèm theo)
				props.getObjVBDenModel().setVbden_butphe(null);
				props.getObjVBDenModel().setVbden_butphe_ngay(null);
				props.getObjVBDenModel().setVbden_butphe_userid(null);
				//chọn sổ văn bản đầu tiên
				props.getObjVBDenModel().setSvb_id(smartChonSoVanBan());
				setCheckHienThiSoDen(props.getObjVBDenModel().getSvb_id() == null ? false
						: true);
				// <--
			}
			// ds tập tin đính kèm
			props.setDsTapTinVb(objTapTinDinhKemVanBanFacade
					.getDsTapTinTheoVanBan(vbId, 1));
			for (TapTinDinhKemVanBanModel ttdk : props.getDsTapTinVb()) {
				File f = new File(props.getBashPath()
						+ CommonUtils.getPathUpload(ttdk.getTtdk_nhomvb())
						+ ttdk.getTtdk_tenluutru());
				Resource fRe = new HCDTResource(f);
				ttdk.setDownload(fRe);
				ttdk.setPath(props.getBashPath()
						+ CommonUtils.getPathUpload(ttdk.getTtdk_nhomvb())
						+ ttdk.getTtdk_tenluutru());
				ttdk.setType(CommonUtils.getFileExtension(f));
				if (ttdk.getTtdk_nhomvb() != IContanst.NVB_DEN
						|| ttdk.getTtdk_vb_id() != props.getObjVBDenModel()
								.getVbden_id()) {// Do dc tiếp nhận từ cùng hệ
													// thống hoặc nhận từ chức
													// năng phổ biến đến các đơn
													// vị
					ttdk.setDuocxoa(false);
				}
			}
			// url="tab:"+props.selectMode+",mode:"+props.mode+",id:"+vbId;
			// setParamsURL(url);

		}
	}

	/**
	 * @author lqthai
	 * @purpose Tiếp nhận văn bản đến cùng hệ thống
	 * @date May 25, 2014 :: 6:26:44 PM
	 */
	public void actionTiepNhan() {
		props.mode = 2;
		if (props.getSelectMode() == 1) {
			modeTmp = props.mode;
		} else if (props.getSelectMode() == 2) {
			modeTmpScan = props.mode;
		}
	}

	/**
	 * @author lqthai
	 * @purpose
	 * @date May 25, 2014 :: 11:02:40 PM
	 * @param v
	 */
	public void actionTabChange(ValueChangeEvent v) {
		CommonUtils.resetInputForm(null);
		_log.info(">>>>actionTabChange");
		if (props.getSelectMode() == 0) {
			props.mode = 0;
			khoiTaoTTThemMoi();
			// url="tab:0,mode:0,id:0";
			// setParamsURL(url);
			_log.info(">>>>mode 0");
		} else if (props.getSelectMode() == 1) {
			props.mode = modeTmp;
			if (props.mode != 0 && idTmp != 0) {
				khoiTaoTTThemMoi();
				tiepNhanVanBanTheoId(idTmp, 1);
				props.setSelectMode2(0);
			} else {
				// url="tab:1,mode:"+props.mode+",id:"+idTmp;
				// setParamsURL(url);
			}
		} else if (props.getSelectMode() == 2) {
			props.mode = modeTmpScan;
			if (props.mode != 0 && idTmpScan != 0) {
				khoiTaoTTThemMoi();
				tiepNhanVanBanTheoId(idTmpScan, 2);
				props.setSelectMode2(0);
			} else {
				// url="tab:2,mode:"+props.mode+",id:"+idTmpScan;
				// setParamsURL(url);
			}
		}
	}

	/**
	 * @author lqthai
	 * @purpose Sửa số đến
	 * @date Jul 15, 2014 :: 6:05:27 PM
	 * @param mode
	 *            1. Sửa, 2. Lưu
	 */
	public void actionSuaSoDen(int mode) {
		canhBaoSoDen = false;
		if (mode == 1) {
			props.setSuaSoDen(true);
		} else if (mode == 2) {
			boolean test = true;

			if (!CommonUtils.isNumberic(props.getSoDen())
					|| props.getSoDen().length() > 5) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
						"Số đến không hợp lệ");
				test = false;
			} else if (!CommonUtils.checkRegex(
					CommonUtils.getBundleValue("regex_chuoimacdinh"),
					props.getSoDenMa())) {
				props.setSelectMode2(0);
				Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
						"Số đến không hợp lệ");
				test = false;
			} else if (objVBDenFacade.isExistsSoDen(props.getSoDen(), props
					.getObjVBDenModel().getSvb_id())) {
				String sd = props.getSoDen();
				// props.setSoDen(objVBDenFacade.getMaxSoDenTheoSoVanBan(props.getObjVBDenModel().getSvb_id())+"");
				if (Integer.parseInt(props.getSoDen()) > 99999) {
					props.setSoDen(sd);
					Validator.showErrorMessage(getPortletId(), "frm:txtSoDen",
							"Số đến không thể vượt quá 99999");
					test = false;
				} else {
					// Validator.showErrorMessage(getPortletId(),
					// "frm:txtSoDen", "Số đến không được trùng");
					canhBaoSoDen = true;
				}

				props.setSelectMode2(0);
			} else {
				canhBaoSoDen = false;
			}

			props.setSuaSoDen(!test);
		}
		_log.info("actionSuaSoDen {}", mode);
	}

	/**
	 * @author lqthai
	 * @purpose Load lại số đến tương ứng với sổ văn bản
	 * @date Jul 20, 2014 :: 10:57:18 PM
	 * @param ae
	 */
	public void actionThayDoiSoVanBan(AjaxBehaviorEvent ae) {
		if (props.getObjVBDenModel().getSvb_id() != null) {
			int svbId = props.getObjVBDenModel().getSvb_id();

			int soDen = objVBDenFacade.getMaxSoDenTheoSoVanBan(svbId) + 1;
			if (soDen >= 99999) {
				props.setSoDen("");
				Validator
						.showErrorMessage(
								getPortletId(),
								"frm:txtSoDen",
								"Số đến không thể vượt quá 99999.\nHiệu chỉnh số nhỏ hơn hoặc chọn sổ văn bản khác");
				props.setSuaSoDen(true);
			} else {
				props.setSoDen(objVBDenFacade.getMaxSoDenTheoSoVanBan(svbId) + 1 + "");
				props.setSuaSoDen(false);
				canhBaoSoDen = false;
			}
			setCheckHienThiSoDen(true);
			props.setSoDenMa(objDmSoVanBanFacade.getChuoiMacDinh(svbId));
			props.setSoDenVitri(objDmSoVanBanFacade.getViTriStt(svbId));
		}
	}
	
	/**
	 * 
	 * @author  hltphat
	 * @purpose Lấy tên người bút phê dựa theo id
	 * @date    Dec 28, 2017 :: 9:56:39 AM 
	 * @param id
	 * @return
	 */
	public String layTenNguoiButPhe(Long id){
		return UserUtil.getUserFullName(id);
	}
	
	/**
	 * @author lqthai
	 * @purpose pre Thêm sổ văn bản
	 * @date Jun 11, 2014 :: 12:03:08 AM
	 */
	public void preActionThemSoVanBan() {
		propsSoVanBan.setObjDmSoVanBanModel(new DmSoVanBanModel());
		propsSoVanBan.setOrganizationid(getOrgId());
		propsSoVanBan.getObjDmSoVanBanModel().setCompanyid(getCompanyId());
		propsSoVanBan.getObjDmSoVanBanModel().setNvb_id(1);
		propsSoVanBan.getObjDmSoVanBanModel().setOrganizationid(getOrgId());
		propsSoVanBan.getObjDmSoVanBanModel().setOrganization_ten(getOrgName());
		propsSoVanBan.getObjDmSoVanBanModel().setSvb_ten("");
		propsSoVanBan.getObjDmSoVanBanModel().setSvb_ma("");
	}

	/**
	 * @author ltbao
	 * @purpose Chuyển text từ chức năng ocr qua form tiếp nhận văn bản đến
	 * @date Oct 1, 2014 :: 11:11:44 AM
	 * @param evt
	 */
	public void getTextFromOcr(AjaxBehaviorEvent evt) {
		if (ocrBean.getMapData().get("data0") != null
				&& ocrBean.getMapData().get("data0").toString().equals("") == false) {
			props.getObjVBDenModel().setVbden_sohieugoc(
					ocrBean.getMapData().get("data0").toString());
		}
		/*
		 * if(ocrBean.getMapData().get("data1")!=null&&ocrBean.getMapData().get(
		 * "data1").toString().equals("")==false){
		 * props.getObjVBDenModel().setVbden_soden
		 * (ocrBean.getMapData().get("data1").toString()); }
		 */
		if (ocrBean.getMapData().get("data1") != null
				&& ocrBean.getMapData().get("data1").toString().equals("") == false) {
			props.getObjVBDenModel().setVbden_nguoiky(
					ocrBean.getMapData().get("data1").toString());
		}
		if (ocrBean.getMapData().get("data2") != null
				&& ocrBean.getMapData().get("data2").toString().equals("") == false) {
			props.getObjVBDenModel().setVbden_trichyeu(
					ocrBean.getMapData().get("data2").toString());
		}
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"dialogReconize.hide();");
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"thongBao('Cập nhật thành công',3);");
	}

	/**
	 * @author Lý Thai Bảo
	 * @purpose Hiện popup cho phép trích lọc thông tin từ bản scan
	 * @date Sep 30, 2014 :: 2:06:37 PM
	 * @param evt
	 */
	public void actionShowReconize(AjaxBehaviorEvent evt) {
		if (props.getDsTapTinVb() != null && props.getDsTapTinVb().size() != 0) {
			ocrBean.initBean(props.getDsTapTinVb(), 1);
		} else {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"thongBao('Vui lòng tải lên ít nhất một tập tin',2)");
		}
	}

	/**
	 * @author ptgiang
	 * @purpose Lấy id phát hành văn bản đi theo số phát hành
	 * @date Oct 17, 2016 :: 9:11:54 AM
	 * @param soPhatHanh
	 * @return
	 */
	public void getIdPhVbDiBySoHieuGoc() {
		try {
			props.getObjVBDenModel().setVbden_phucdap(
					pmGetIdPhVbDiBySoHieuGoc(props.getObjVBDenModel()
							.getVbden_phucdap()));
		} catch (Exception ex) {
			_log.error("Err ", ex);
		}
	}

	/**
	 * @author ptgiang
	 * @purpose Lấy id phát hành văn bản đi theo số phát hành
	 * @date Oct 17, 2016 :: 9:11:54 AM
	 * @param soPhatHanh
	 * @return
	 */
	private String pmGetIdPhVbDiBySoHieuGoc(Object vbden_phucdap) {
		try {
			if (vbden_phucdap == null
					|| StringUtils.isEmpty(vbden_phucdap.toString())) {
				return "";
			}

			String escapeLink = CommonUtils.escapeLinkVblq(vbden_phucdap
					.toString());
			return objVBDenFacade.getLinkTagsPhVbDi(escapeLink, getOrgId(),
					getUserId());
		} catch (Exception ex) {
			_log.error("Err ", ex);
		}
		return "";
	}

	/**
	 * @author lqthai
	 * @purpose
	 * @date Dec 7, 2016 :: 10:06:12 AM
	 */
	public void preActionKhongTiepNhan(Long vbden_id) {
		try {
			_log.info(">>preActionTraVanBan");
			_log.info(">>>Văn bản: " + vbden_id);

			props.setVbIdKhongTiepNhan(vbden_id);

			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"dialogKhongTiepNhan.show();");
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	public void actionKhongTiepNhan() {
		try {
			_log.info(">>actionTraVanBan");
			_log.info(">>>Lý do: " + props.getLyDoKhongTiepNhan());
			if (!objVBDenFacade.isExists(props.getVbIdKhongTiepNhan())) {
				JavascriptContext.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"thongBao('Văn bản không tồn tại',4);");
			} else if (objVBDenFacade.isTiepNhan(props.getVbIdKhongTiepNhan())) {
				JavascriptContext.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"thongBao('Văn bản đã được tiếp nhận',4);");
			} else {
				// Cập nhật lại kết quả xử lý là đã xóa
				VBDenModel objVBDenModel = objVBDenFacade.getObjVanBanDen(props
						.getVbIdKhongTiepNhan());

				actionXoaVanBan(props.getVbIdKhongTiepNhan(), 1);

				long vbIdGoc;
				int nvbId;
				if (objVBDenModel.getVbden_id_ldv() != null) {// Tiếp nhận từ
																// chuyển xử lý
																// văn bản đến
					nvbId = IContanst.NVB_DEN;
					vbIdGoc = objVBDenModel.getVbden_id_ldv();
				} else {// Tiếp nhận từ phát hành văn bản đi
					nvbId = IContanst.NVB_DI_PH;
					vbIdGoc = objVBDenModel.getPhdi_id();
				}

				KetQuaXuLyModel kqxlModel = objKetQuaXuLyFacade
						.getObjKetQuaXuLy(vbIdGoc, nvbId,
								objVBDenModel.getOrganizationid());

				if (kqxlModel != null) {
					kqxlModel.setKqxl_daxem(true);
					kqxlModel.setKqxl_trangthai(IContanst.KHONG_TIEP_NHAN);// Không
																			// đồng
																			// ý
																			// tiếp
																			// nhận
					kqxlModel.setKqxl_lydo_khongtiepnhan(props
							.getLyDoKhongTiepNhan());
					kqxlModel.setKqxl_donvinhan_nguoixem(UserUtil
							.getUserFullName(userId));
					kqxlModel.setKqxl_donvinhan_ngayxem(DateUtils
							.getCurrentDate());
					objKetQuaXuLyFacade.capNhatKetQuaXuLy(kqxlModel);
				}
			}
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(),
					"dialogKhongTiepNhan.hide();");

		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	/**
	 * @author ptgiang
	 * @purpose Lấy danh sách user chuyển xử lý
	 * @date Dec 13, 2016 :: 8:39:58 AM
	 */
	private void pmGetDsChuyenXuLy() {
		props.setDsNhanXuLy(pmGetAllUserNhanXuLy());

		if (props.getDsNhanXuLy() == null) {
			props.setDsNhanXuLy(new ArrayList<Map<String, Object>>());
		}

		// Khởi tạo lại danh sách bộ lọc đơn vị
		// pmCreateFilterDonViNhan();
		props.setvFilterDonVi(null);
		actionLocDonVi();
	}

	/**
	 * @author ptgiang
	 * @purpose Xử lý sự kiện chọn lọc đơn vị -> Khởi tạo lại danh sách bộ lọc
	 *          phòng ban
	 * @date Dec 13, 2016 :: 8:58:47 AM
	 */
	public void actionLocDonVi() {
		List<SelectItem> listFilterPhongBan = new ArrayList<SelectItem>();

		SelectItem noSelect = new SelectItem("", "--- Tất cả ---");
		noSelect.setNoSelectionOption(true);
		listFilterPhongBan.add(noSelect);

		List<Organization> listPhongBan = null;
		if (props.getvFilterDonVi() != null
				&& props.getvFilterDonVi().compareTo("") != 0) {
			listPhongBan = userUtil.getDsPhongBanTheoDonVi(Long.parseLong(props
					.getvFilterDonVi()));
		} else {
			listPhongBan = userUtil.getListPhongBan(userUtil.getCompanyId());
		}

		if (listPhongBan != null) {
			for (Organization org : listPhongBan) {
				listFilterPhongBan.add(new SelectItem(org.getOrganizationId(),
						org.getName()));
			}
		}
		props.setFilterPhongBan(listFilterPhongBan);
	}

	private void pmGetDsNhanPhoBien() {
		props.setDsNhanPhoBien(pmGetAllUserNhanPhoBien());

		if (props.getDsNhanPhoBien() == null) {
			props.setDsNhanPhoBien(new ArrayList<Map<String, Object>>());
		}

		pmCreateFilterDonViNhan();
		props.setvFilterDonVi(null);
		actionLocDonVi();
	}

	private List<Map<String, Object>> pmGetAllUserNhanXuLy() {
		if (listAllUserNhanXuLy == null) {
			// listAllUserNhanXuLy =
			// userUtil.getListMapChuyenXuLy(IContanst.SELECT_ALL, companyid);
			listAllUserNhanXuLy = userUtil
					.getFullListMapChuyenXuLy(getCompanyId());
		}
		pmResetDefaultValue(listAllUserNhanXuLy);
		return listAllUserNhanXuLy;
	}

	private List<Map<String, Object>> pmGetAllUserNhanPhoBien() {
		if (listAllUserNhanPhoBien == null) {
			if (listAllUserNhanXuLy == null) {
				listAllUserNhanPhoBien = userUtil
						.getFullListMapChuyenXuLy(companyid);
			} else {
				listAllUserNhanPhoBien = listAllUserNhanXuLy;
			}
		}
		pmResetDefaultValue(listAllUserNhanPhoBien);
		return listAllUserNhanPhoBien;
	}

	private void pmResetDefaultValue(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> map : list) {
			map.put("chon", false);
			map.put("checked", false);
			map.put("xlchinh", false);
		}
	}

	private void pmCreateFilterDonViNhan() {
		try {
			if (CollectionUtil.isNullOrEmpty(props.getFilterDonVi())) {
				// Khởi tạo lại danh sách bộ lọc đơn vị
				List<SelectItem> listFilterDonVi = new ArrayList<SelectItem>();

				SelectItem noSelect = new SelectItem("", "--- Tất cả ---");
				noSelect.setNoSelectionOption(true);
				listFilterDonVi.add(noSelect);

				List<OrganizationBase> listOrg = userUtil
						.getListDonViQuery(companyid);
				if (listOrg != null) {
					for (OrganizationBase org : listOrg) {
						listFilterDonVi.add(new SelectItem(org
								.getOrganizationid(), org.getName()));
					}
				}

				props.setFilterDonVi(listFilterDonVi);

			}
		} catch (Exception ex) {
			_log.error("err ", ex);
		}
	}

	// =====================================
	// @ptgiang Nơi nhận liên đơn vị

	public void actionCheckChonChuyenDonVi() {
		props.setNoiDungChuyenDenDonVi("Chuyển đến các đơn vị thực hiện theo bút phê của Lãnh đạo");
		props.setDsChuyenDenDonVi(userUtil.getListMapDonVi(getCompanyId(),
				getOrgId()));

	}

	public void preActionChonNoiNhanLienDonVi() {
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"dialogNoiNhanLienDonVi.show();");
	}

	@SuppressWarnings("unchecked")
	public void actionXoaNoiNhanLienDonVi(AjaxBehaviorEvent evt) {
		Object obj = evt.getComponent().getAttributes().get("obj");
		if (obj != null) {
			Map<String, Object> mapXoa = (Map<String, Object>) obj;
			long dvIdXoa = Long.parseLong(mapXoa.get("organizationid")
					.toString());
			for (Map<String, Object> mapDonVi : props.getDsChuyenDenDonVi()) {
				if (Long.parseLong(mapDonVi.get("organizationid").toString()) == dvIdXoa) {
					mapDonVi.put("chon", false);
					break;
				}
			}
		}
	}

	// private void khoiTaoDanhSachNoiNhanLienDonVi(){
	// if( CollectionUtil.isNullOrEmpty(props.getDsChuyenDenDonVi()) ){
	// //props.setDsChuyenDenDonVi(new ArrayList<Map<String,Object>>());
	// props.setDsChuyenDenDonVi(null);

	// }
	// if( props.getDsChuyenDenDonVi() == null ){
	// props.setDsChuyenDenDonVi(new ArrayList<Map<String,Object>>());
	// }
	// }

	// -------------------------PROPERTIES--------------------//

	public VBDen_TiepNhanProps getProps() {
		return props;
	}

	public void setProps(VBDen_TiepNhanProps props) {
		this.props = props;
	}

	public DmDoKhanFacade getObjDmDoKhanFacade() {
		return objDmDoKhanFacade;
	}

	public void setObjDmDoKhanFacade(DmDoKhanFacade objDmDoKhanFacade) {
		this.objDmDoKhanFacade = objDmDoKhanFacade;
	}

	public DmDoMatFacade getObjDmDoMatFacade() {
		return objDmDoMatFacade;
	}

	public void setObjDmDoMatFacade(DmDoMatFacade objDmDoMatFacade) {
		this.objDmDoMatFacade = objDmDoMatFacade;
	}

	public DmLinhVucFacade getObjDmLinhVucFacade() {
		return objDmLinhVucFacade;
	}

	public void setObjDmLinhVucFacade(DmLinhVucFacade objDmLinhVucFacade) {
		this.objDmLinhVucFacade = objDmLinhVucFacade;
	}

	public DmLoaiVanBanFacade getObjDmLoaiVanBanFacade() {
		return objDmLoaiVanBanFacade;
	}

	public void setObjDmLoaiVanBanFacade(
			DmLoaiVanBanFacade objDmLoaiVanBanFacade) {
		this.objDmLoaiVanBanFacade = objDmLoaiVanBanFacade;
	}

	public DmSoVanBanFacade getObjDmSoVanBanFacade() {
		return objDmSoVanBanFacade;
	}

	public void setObjDmSoVanBanFacade(DmSoVanBanFacade objDmSoVanBanFacade) {
		this.objDmSoVanBanFacade = objDmSoVanBanFacade;
	}

	public VBDenFacade getObjVBDenFacade() {
		return objVBDenFacade;
	}

	public void setObjVBDenFacade(VBDenFacade objVBDenFacade) {
		this.objVBDenFacade = objVBDenFacade;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public boolean isCoCauHinh() {
		return coCauHinh;
	}

	public void setCoCauHinh(boolean coCauHinh) {
		this.coCauHinh = coCauHinh;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isButPhe() {
		return butPhe;
	}

	public void setButPhe(boolean butPhe) {
		this.butPhe = butPhe;
	}

	public long getUserIdNhanXl() {
		return userIdNhanXl;
	}

	public void setUserIdNhanXl(long userIdNhanXl) {
		this.userIdNhanXl = userIdNhanXl;
	}

	public DmSoVanBanPopupBean getPropsSoVanBan() {
		return propsSoVanBan;
	}

	public void setPropsSoVanBan(DmSoVanBanPopupBean propsSoVanBan) {
		this.propsSoVanBan = propsSoVanBan;
	}

	public boolean isCoQuyen() {
		return coQuyen;
	}

	public void setCoQuyen(boolean coQuyen) {
		this.coQuyen = coQuyen;
	}

	public DmCachThucXuLyBean getPropsCachThucXuLy() {
		return propsCachThucXuLy;
	}

	public void setPropsCachThucXuLy(DmCachThucXuLyBean propsCachThucXuLy) {
		this.propsCachThucXuLy = propsCachThucXuLy;
	}

	public RecognizeBean getOcrBean() {
		return ocrBean;
	}

	public void setOcrBean(RecognizeBean ocrBean) {
		this.ocrBean = ocrBean;
	}

	public boolean isCanhBaoSoDen() {
		return canhBaoSoDen;
	}

	public void setCanhBaoSoDen(boolean canhBaoSoDen) {
		this.canhBaoSoDen = canhBaoSoDen;
	}

	public HcdtThongSoLoader getLoader() {
		return loader;
	}

	public void setLoader(HcdtThongSoLoader loader) {
		this.loader = loader;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public List<SelectItem> getListFilterDonVi() {
		return listFilterDonVi;
	}

	public void setListFilterDonVi(List<SelectItem> listFilterDonVi) {
		this.listFilterDonVi = listFilterDonVi;
	}

	public List<Organization> getListDonVi() {
		return listDonVi;
	}

	public void setListDonVi(List<Organization> listDonVi) {
		this.listDonVi = listDonVi;
	}

	public List<Organization> getDonViCungHt() {
		return donViCungHt;
	}

	public void setDonViCungHt(List<Organization> donViCungHt) {
		this.donViCungHt = donViCungHt;
	}

	public long getOrgIdTemp() {
		return orgIdTemp;
	}

	public void setOrgIdTemp(long orgIdTemp) {
		this.orgIdTemp = orgIdTemp;
	}

	public List<Long> getListDvUser() {
		return listDvUser;
	}

	public void setListDvUser(List<Long> listDvUser) {
		this.listDvUser = listDvUser;
	}

	public boolean isRunRefesh() {
		return runRefesh;
	}

	public void setRunRefesh(boolean runRefesh) {
		this.runRefesh = runRefesh;
	}

	public boolean isCheckHienThiSoDen() {
		return checkHienThiSoDen;
	}

	public void setCheckHienThiSoDen(boolean checkHienThiSoDen) {
		this.checkHienThiSoDen = checkHienThiSoDen;
	}
}
