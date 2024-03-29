package service;

import model.Admin;

public interface AttendanceService {
	public Admin getAdminByUP(String username, String password); // 通过账号密码查询是否存在该管理员

	public boolean loginByUP(String username, String password); // 通过账号密码查询登录是否成功

	public boolean isExit(String username); // 通过账号查询该账户是否存在

	public boolean changePassword(String username,String password);// 更改密码
}
