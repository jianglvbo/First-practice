package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import model.PageCount;
import model.PunchCard;
import service.PunchCardServiceImpl;
import util.ExcelExportUtil;

public class PunchCardAction extends ActionSupport {
	PunchCardServiceImpl service = new PunchCardServiceImpl();
	private int id; // id
	private String punchCardMan; // 打卡人
	private String punchCardDate; // 打卡时间
	private String remark; // 备注
	// ===========================================
	private int maxPage;//最大页码
	private int minPage;//最小页码
	private int currentPage=-1;//当前页码
	private PageCount pageCount=PageCount.getInstance();//页码对象
	private PunchCard punchCard = new PunchCard();
	private int[] ids;//id数组，用于批量删除
	private List<PunchCard> punchCardList = new ArrayList<PunchCard>();//打卡单链表,用于储存查询数据
	ExcelExportUtil excel=new ExcelExportUtil();//excel工具类
	private String searchName;//查找的名字
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getMinPage() {
		return minPage;
	}

	public void setMinPage(int minPage) {
		this.minPage = minPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public PageCount getPageCount() {
		return pageCount;
	}

	public void setPageCount(PageCount pageCount) {
		this.pageCount = pageCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPunchCardMan() {
		return punchCardMan;
	}

	public void setPunchCardMan(String punchCardMan) {
		this.punchCardMan = punchCardMan;
	}

	public String getPunchCardDate() {
		return punchCardDate;
	}

	public void setPunchCardDate(String punchCardDate) {
		this.punchCardDate = punchCardDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PunchCard getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(PunchCard punchCard) {
		this.punchCard = punchCard;
	}

	public List<PunchCard> getPunchCardList() {
		return punchCardList;
	}

	public void setPunchCardList(List<PunchCard> punchCardList) {
		this.punchCardList = punchCardList;
	}

	// ===============================================================

	public String add() {//添加打卡记录
		punchCard.setId(id);
		punchCard.setPunchCardMan(punchCardMan);
		punchCard.setPunchCardDate(punchCardDate);
		punchCard.setRemark(remark);
		service.addPunchCard(punchCard);
		pageCount.setMaxPage(service.getMaxPage());		//获取当前记录的页码
		pageCount.setCurrentPage(pageCount.getMaxPage());//添加记录后自动跳转到最后一页
		return "add";
	}

	public String edit() {//编辑打卡信息
		currentPage = pageCount.getCurrentPage();			//编辑完保持之前的页码
		punchCard = service.getPunchCardById(id);
		return "edit";
	}

	public String limit() {//根据页码查找打卡信息
		pageCount.setMaxPage(service.getMaxPage()); // 获取记录的页数，每次检测，防治删除数据后页码减少或者添加数据后页码增加
		if(currentPage!=-1) {
			if (currentPage > pageCount.getMaxPage()) {
				currentPage = pageCount.getMaxPage(); // 如果当前页码大于等于最大页码的话，让当前页码等于最大页码减一
			}
			pageCount.setCurrentPage(currentPage);
		}	
		currentPage = pageCount.getCurrentPage();
		punchCardList = service.getAllByLimit(pageCount.getCurrentPage());
		return "limit";
	}

	public String search() {//查找打卡信息
		pageCount.setCurrentPage(0);
		pageCount.setMaxPage(0);
		punchCardList = service.getPunchCardByPunchCardMan(searchName);
		return "search";
	}
	public String initialize() throws Exception {//初始化打卡单，页码归零
		pageCount.setCurrentPage(0);
		return "initialize";
	}
	
	public void ExportToExcel() throws Exception {//导出到excel
		punchCardList=service.getAll();
		excel.PunchCardExcel(punchCardList);
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpSession session=request.getSession();
		try {
			response.getWriter().append("{\"isSuccess\":"+true+"}");	//返回给ajax的值，json格式
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String delete() {
		currentPage = pageCount.getCurrentPage();			//保持之前的页码
		service.deletePunchCardById(id);
		return "delete";
	}
	public String remove() {
		currentPage = pageCount.getCurrentPage();			//保持之前的页码
		service.removePunchCardByIds(ids);
		return "remove";
	}
	public String update() {
		currentPage = pageCount.getCurrentPage();			//保持之前的页码
		punchCard.setId(id);
		punchCard.setPunchCardMan(punchCardMan);
		punchCard.setPunchCardDate(punchCardDate);
		punchCard.setRemark(remark);
		service.updatePunchCardById(id, punchCard);
		return "update";
	}
	
	
}
