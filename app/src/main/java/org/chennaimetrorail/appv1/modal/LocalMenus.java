package org.chennaimetrorail.appv1.modal;

public class LocalMenus {

    //private variables
    int id;
    String menu_name;
    String menu_className;
    String menu_priority;
    String menu_icon;

    // Empty constructor
    public LocalMenus(){

    }
    // constructor
    public LocalMenus(int id,String menu_name, String menu_className,String menu_priority,String menu_icon){
        this.id = id;
        this.menu_name = menu_name;
        this.menu_className = menu_className;
        this.menu_priority = menu_priority;
        this.menu_icon = menu_icon;
    }
    // constructor
    public LocalMenus(String menu_name, String menu_className,String menu_priority,String menu_icon){
        this.menu_name = menu_name;
        this.menu_className = menu_className;
        this.menu_priority = menu_priority;
        this.menu_icon = menu_icon;
    }
    // constructor
    public LocalMenus(int id,String menu_priority){
        this.id = id;
        this.menu_priority = menu_priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMenu_className() {
        return menu_className;
    }

    public void setMenu_className(String menu_className) {
        this.menu_className = menu_className;
    }

    public String getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(String menu_icon) {
        this.menu_icon = menu_icon;
    }
}
