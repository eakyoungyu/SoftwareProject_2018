package org.project.softwareproject_2018;

public class ListViewItem {
    private String timeStr ;
    private String customerStr ;

    public void setTime(String time) {
        timeStr = time ;
    }
    public void setCustomer(String customer) {
        customerStr = customer;
    }

    public String getTime() {
        return this.timeStr ;
    }
    public String getCustomer() {
        return this.customerStr ;
    }
}
