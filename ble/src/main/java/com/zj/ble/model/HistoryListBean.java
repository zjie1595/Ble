package com.zj.ble.model;

import java.util.List;

/**
 * Created by DELL on 2020/7/18
 */

public class HistoryListBean {
    List<HistoryBean> historyBeanList;

    public List<HistoryBean> getHistoryBeanList() {
        return historyBeanList;
    }

    public void setHistoryBeanList(List<HistoryBean> historyBeanList) {
        this.historyBeanList = historyBeanList;
    }

    public static class HistoryBean{
        private String projectNum;
        private String weldingPortNum;
        private String welderNum;
        private int weldingState;

        public String getProjectNum() {
            return projectNum;
        }

        public void setProjectNum(String projectNum) {
            this.projectNum = projectNum;
        }

        public String getWeldingPortNum() {
            return weldingPortNum;
        }

        public void setWeldingPortNum(String weldingPortNum) {
            this.weldingPortNum = weldingPortNum;
        }

        public String getWelderNum() {
            return welderNum;
        }

        public void setWelderNum(String welderNum) {
            this.welderNum = welderNum;
        }

        public int getWeldingState() {
            return weldingState;
        }

        public void setWeldingState(int weldingState) {
            this.weldingState = weldingState;
        }
    }
}
