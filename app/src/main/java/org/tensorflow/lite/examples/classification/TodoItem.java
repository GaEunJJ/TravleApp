package org.tensorflow.lite.examples.classification;

public class TodoItem {
    private int id;             // 게시글의 고유 id
    private String writeDate;   // 작성 날짜
    private String sort;        // 분류 종류
    private String user;        // 사용 내역
    private String ex_money;    // 사용한 환전 후 금액
    private Double total;

    public TodoItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEx_money() {
        return ex_money;
    }

    public void setEx_money(String ex_money) {
        this.ex_money = ex_money;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
