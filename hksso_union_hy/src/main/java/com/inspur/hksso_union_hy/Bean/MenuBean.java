package com.inspur.hksso_union_hy.Bean;

import java.util.Date;
import java.util.List;

/**
 * Menu Bean对象
 * @author lijinfeng
 * @date 2014-08-30
 * @version 1.0
 * @description 菜单资源对象
 */

public class MenuBean implements java.io.Serializable{
	private String id;
	private String name;
	private String appcode;
	private String icon;
	private String path;
	private String data;
	private String isLeaf;
	private String parent_code;
	private String resPath;
	private String sort_order;
	private String creator;
	private Date createTime;
	private String lastEditor;
	private Date lastTime;
	private String remark;
	private String status;
	private String opentype;
	private String childrenCount;

	//子菜单
	private List<MenuBean> subMenu;
	
	public MenuBean(){
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getAppCode() {
//		return appCode;
////	}

//	public void setAppCode(String appcode) {
//		this.appCode = appcode;
//	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isleaf) {
		this.isLeaf = isleaf;
	}

//	public String getParentCode() {
//		return parentCode;
//	}
//
//	public void setParentCode(String parentCode) {
//		this.parentCode = parentCode;
//	}

	public String getResPath() {
		return resPath;
	}

	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}




	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}

	public void setResPath(String respath) {
		this.resPath = respath;
	}
//	public String getSortOrder() {
//		return sortOrder;
//	}
//
//	public void setSortOrder(String sortorder) {
//		this.sortOrder = sortorder;
//	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createtime) {
		this.createTime = createtime;
	}

	public String getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(String lasteditor) {
		this.lastEditor = lasteditor;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lasttime) {
		this.lastTime = lasttime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setOpentype(String opentype) {
		this.opentype = opentype;
	}
	public String getOpentype() {
		return opentype;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(String childrenCount) {
		this.childrenCount = childrenCount;
	}

//	public List<MenuBean> getChildren() {
//		return children;
//	}
//
//	public void setChildren(List<MenuBean> children) {
//		this.children = children;
//	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public List<MenuBean> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<MenuBean> subMenu) {
		this.subMenu = subMenu;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
