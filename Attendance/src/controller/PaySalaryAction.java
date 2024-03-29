package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import model.CheckReport;
import model.Classes;
import model.Employees;
import model.Leave;
import model.PageCount;
import model.PaySalary;
import model.PunchCard;
import service.CheckReportServiceImpl;
import service.ClassesServiceImpl;
import service.EmployeesServiceImpl;
import service.LeaveServiceImpl;
import service.PaySalaryServiceImpl;
import util.ExcelExportUtil;

public class PaySalaryAction extends ActionSupport {
	PaySalaryServiceImpl service = new PaySalaryServiceImpl();
	CheckReportServiceImpl service_C = new CheckReportServiceImpl();
	ClassesServiceImpl service_CL = new ClassesServiceImpl();
	LeaveServiceImpl service_L = new LeaveServiceImpl();
	EmployeesServiceImpl service_E=new EmployeesServiceImpl();
	List<PunchCard> checkReportList = new ArrayList<PunchCard>();
	List<Classes> ClassesList = new ArrayList<Classes>();
	List<Leave> leaveList = new ArrayList<Leave>();
	private int id; // 付款单id
	private String salariedMan;// 领薪水的人
	private String salary;// 薪水
	private String calculateStartDate;// 薪水开始计算时间
	private String calculateEndDate;// 薪水结束计算时间
	private int maxPage;// 最大页码
	private int minPage;// 最小页码
	private int currentPage = -1;// 当前页码
	private PageCount pageCount = PageCount.getInstance();// 页码对象
	private int[] ids;// id数组,用与批量删除
	private List<PaySalary> paySalaryList = new ArrayList<PaySalary>();// 薪水链表，用于储存查询数据
	private PaySalary paySalary = new PaySalary();// 薪水对象
	private String searchName;// 查找名字
	private String staffCode;// 员工编码
	private String staffSalary;// 员工薪水
	private String paidTime;// 付薪时间
	private List<Employees> employeesList=new ArrayList<Employees>();// 用来储存员工记录
	private String name;//员工名字
	ExcelExportUtil excel = new ExcelExportUtil();// excel工具类

	
	public List<Employees> getEmployeesList() {
		return employeesList;
	}

	public void setEmployeesList(List<Employees> employeesList) {
		this.employeesList = employeesList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}

	public String getStaffSalary() {
		return staffSalary;
	}

	public void setStaffSalary(String staffSalary) {
		this.staffSalary = staffSalary;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSalariedMan() {
		return salariedMan;
	}

	public void setSalariedMan(String salariedMan) {
		this.salariedMan = salariedMan;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCalculateStartDate() {
		return calculateStartDate;
	}

	public void setCalculateStartDate(String calculateStartDate) {
		this.calculateStartDate = calculateStartDate;
	}

	public String getCalculateEndDate() {
		return calculateEndDate;
	}

	public void setCalculateEndDate(String calculateEndDate) {
		this.calculateEndDate = calculateEndDate;
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

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<PaySalary> getPaySalaryList() {
		return paySalaryList;
	}

	public void setPaySalaryList(List<PaySalary> paySalaryList) {
		this.paySalaryList = paySalaryList;
	}

	public PaySalary getPaySalary() {
		return paySalary;
	}

	public void setPaySalary(PaySalary paySalary) {
		this.paySalary = paySalary;
	}

	// ==============================================================
	public String add() {// 添加薪水记录
		paySalary.setId(id);
		paySalary.setSalariedMan(salariedMan);
		paySalary.setSalary(salary);
		paySalary.setCalculateStartDate(calculateStartDate);
		paySalary.setCalculateEndDate(calculateEndDate);
		paySalary.setPaidTime(paidTime);
		service.addPaySalary(paySalary);
		pageCount.setMaxPage(service.getMaxPage()); // 获取当前记录的页码
		pageCount.setCurrentPage(pageCount.getMaxPage());// 添加记录后自动跳转到最后一页
		return "add";
	}

	public String delete() {// 删除薪水记录
		currentPage = pageCount.getCurrentPage(); // 删除完回到之前的页码
		service.deletePaySalaryById(id);
		return "delete";
	}

	public String remove() {// 批量删除薪水信息
		currentPage = pageCount.getCurrentPage(); // 删除完回到之前的页码
		service.removePaySalaryByIds(ids);
		return "remove";
	}

	public String update() {// 修改薪水信息
		currentPage = pageCount.getCurrentPage(); // 编辑完保持之前的页码
		paySalary.setId(id);
		paySalary.setSalariedMan(salariedMan);
		paySalary.setSalary(salary);
		paySalary.setCalculateStartDate(calculateStartDate);
		paySalary.setCalculateEndDate(calculateEndDate);
		paySalary.setPaidTime(paidTime);
		service.updatePaySalaryById(id, paySalary);
		return "update";
	}

	public String edit() throws Exception {// 编辑薪水信息
		currentPage = pageCount.getCurrentPage(); // 编辑完保持之前的页码
		for (PaySalary p : paySalaryList) {
			if (p.getSalariedMan().equals(paySalary.getSalariedMan())) {
				paySalary.setSalary(p.getSalary());
			}
		}
		PaySalaryAction Paction = new PaySalaryAction();
		paySalary = service.getPaySalaryById(id);
		employeesList=service_E.getAllEmployees();
		return "edit";
	}

	public String limit() throws Exception {// 根据页码获取薪水信息
		pageCount.setMaxPage(service.getMaxPage()); // 获取记录的页数，每次检测，防治删除数据后页码减少或者添加数据后页码增加
		if (currentPage != -1) {
			if (currentPage > pageCount.getMaxPage()) {
				currentPage = pageCount.getMaxPage(); // 如果当前页码大于等于最大页码的话，让当前页码等于最大页码减一
			}
			pageCount.setCurrentPage(currentPage);
		}
		currentPage = pageCount.getCurrentPage();
		paySalaryList = service.getAllByLimit(pageCount.getCurrentPage());
		checkReportList = service_C.getAllFromCheckReport(); // 获取考勤信息
		ClassesList = service_CL.getAll(); // 获取班次信息
		leaveList = service_L.getAll(); // 获取请假信息
		PaySalaryAction Paction = new PaySalaryAction();
		Paction.Calculate(paySalaryList, ClassesList, leaveList, checkReportList);
		return "limit";
	}

	public String search() throws Exception {// 查找薪水信息
		pageCount.setCurrentPage(0);
		pageCount.setMaxPage(0);
		paySalaryList = service.getAll();
		ClassesList = service_CL.getAll(); // 获取班次信息
		leaveList = service_L.getAll(); // 获取请假信息
		checkReportList = service_C.getAllFromCheckReport(); // 获取考勤信息
		PaySalaryAction Paction = new PaySalaryAction();
		Paction.Calculate(paySalaryList, ClassesList, leaveList, checkReportList);
		if (!searchName.equals("")) {
			for (int i = 0; i < paySalaryList.size(); i++) {
				if (!paySalaryList.get(i).getSalariedMan().equals(searchName)) {
					paySalaryList.remove(i);
					i--;
				}
			}
		}
		return "search";
	}

	public String initialize() throws Exception {// 初始化薪水页码
		pageCount.setCurrentPage(0);
		return "initialize";
	}

	/* 计算薪水 */
	public void Calculate(List<PaySalary> paySalaryList, List<Classes> ClassesList, List<Leave> leaveList,
			List<PunchCard> checkReportList) throws Exception {

		for (int i = 0; i < checkReportList.size(); i++) { // 删除掉旷工记录
			if (checkReportList.get(i).getPunchCardDate() == null
					|| checkReportList.get(i).getPunchCardDate_end() == null) {
				checkReportList.remove(i);
				i--; // 删除完链表指针往前返回一步，防止数据遗漏
			}
		}
		for (PaySalary p : paySalaryList) {
			Date classesMorningDate=null;
			Date classesAfternoonDate = null;
			long workTime=0;
			for (Classes c : ClassesList) {
				if(p.getClasses().equals(c.getName())) {
					classesMorningDate = new SimpleDateFormat("HH:mm").parse(c.getMorningBusinessHours()); // 上班时间
					classesAfternoonDate = new SimpleDateFormat("HH:mm").parse(c.getAfternoonBusinessHours());// 下班时间
					 workTime = (classesAfternoonDate.getTime() - classesMorningDate.getTime()) / 3600000; // 每天需要工作的时间 单位小时
				}
			}
			Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(p.getCalculateStartDate()); // 开始计算薪水日期
			Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(p.getCalculateEndDate()); // 结束计算薪水日期
			long totalWorkTime = 0;
			long totalLeaveTime = 0;
			long workTime_EveryDay = (classesAfternoonDate.getTime() - classesMorningDate.getTime()) / 3600000; // 每天应该工作的时间
			long needWorkTime = (date4.getTime() - date3.getTime()) / 3600000
					- (date4.getTime() - date3.getTime()) / (3600000 * 24) * workTime_EveryDay; // 需要工作的时间
			Calendar searchFirstTimCalendar = Calendar.getInstance();
			searchFirstTimCalendar.setTime(date3);
			int maxDay = searchFirstTimCalendar.getActualMaximum(searchFirstTimCalendar.DATE);
			for (PunchCard pun : checkReportList) {
				Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pun.getPunchCardDate()); // 上班打卡时间
				Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pun.getPunchCardDate_end()); // 下班打卡时间
				if (pun.getPunchCardMan().equals(p.getSalariedMan())) { // 判断是不是要付薪水的人
					if (date1.getDate() >= date3.getDate() && date1.getDate() <= date4.getDate()) { // 判断是否在薪水计算日期内
						long x = (classesMorningDate.getHours() - date1.getHours());
						if (x > 0) { // 如果提前打卡上班的话，就让上班时间为班次规定上班时间
							x = 0;
						}
						long y = (date2.getHours() - classesAfternoonDate.getHours());
						if (y > 0) { // 如果晚打卡下班的话，就让下班时未班次规定下班时间
							y = 0;
						}
						long sum = x + y + workTime;
						if (sum > workTime) { // 如果上班时间超过worktime 就让它等于worktime
							sum = workTime;
						}
						totalWorkTime += sum;
					}
				}
				/* 扣除请假时长 */
				for (Leave l : leaveList) {
					Date leaveStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pun.getPunchCardDate());
					Date leaveEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pun.getPunchCardDate());
					if (l.getPleaseDummyMan().equals(p.getSalariedMan())) { // 判断请假人和付薪水得人是不是同一个人
						if (leaveStartDate.getTime() >= date3.getTime() && leaveEndDate.getTime() <= date4.getTime()) { // 判断是否在薪水计算日期内
							long leaveDays = leaveEndDate.getDate() - leaveStartDate.getDate();
							if (leaveStartDate.getHours() < date1.getHours()) {
								leaveStartDate.setHours(date1.getHours());
							}
							if (leaveEndDate.getHours() > date2.getHours()) {
								leaveEndDate.setHours(date2.getHours());
							}
							long leaveTime = leaveDays * workTime_EveryDay + leaveEndDate.getHours()
									- leaveStartDate.getHours();
							totalLeaveTime += leaveTime;
						}
					}
				}
			}
			if(needWorkTime==0||needWorkTime==0) {
				p.setSalary("0");
			}else {
				double f = (double) (totalWorkTime - totalLeaveTime) / (double) needWorkTime
						* ((double) needWorkTime / (double) (maxDay * 24));
				BigDecimal bg = new BigDecimal(f); // 保留两位小数
				double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double sa = Integer.valueOf(p.getStaffSalary()) * f1;
				BigDecimal bg1 = new BigDecimal(sa); // 保留两位小数
				double saa = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				p.setSalary(String.valueOf(saa));
			}
		}
		}

	public void ExportToExcel() throws Exception {//导出到excel
		currentPage = pageCount.getCurrentPage();
		paySalaryList = service.getAll();
		ClassesList = service_CL.getAll(); // 获取班次信息
		leaveList = service_L.getAll(); // 获取请假信息
		checkReportList = service_C.getAllFromCheckReport(); // 获取考勤信息
		PaySalaryAction Paction = new PaySalaryAction();
		Paction.Calculate(paySalaryList, ClassesList, leaveList, checkReportList);
		excel.PaySalaryExcel(paySalaryList);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		try {
			response.getWriter().append("{\"isSuccess\":" + true + "}"); // 返回给ajax的值，json格式
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
