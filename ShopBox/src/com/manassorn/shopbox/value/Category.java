package com.manassorn.shopbox.value;

import com.manassorn.shopbox.db.DatabaseField;

public class Category {
	@DatabaseField(id=true)
	private int id;
	@DatabaseField
	private int parentId;
	@DatabaseField
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
