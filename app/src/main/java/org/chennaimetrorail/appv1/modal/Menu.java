package org.chennaimetrorail.appv1.modal;

public class Menu {
	
	//private variables
	String menu_id;
	String menu_name;
	String menu_priority;
	
	// Empty constructor
	public Menu(){
		
	}
	// constructor
	public Menu(String menu_id, String menu_name, String menu_priority){
		this.menu_id = menu_id;
		this.menu_name = menu_name;
		this.menu_priority = menu_priority;
	}
	// constructor
	public Menu(String menu_id,String menu_priority){
		this.menu_id = menu_id;
		this.menu_name = menu_name;
		this.menu_priority = menu_priority;
	}

	// constructor
	public Menu(String menu_priority){
		this.menu_priority = menu_priority;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_priority() {
		return menu_priority;
	}

	public void setMenu_priority(String menu_priority) {
		this.menu_priority = menu_priority;
	}
}
