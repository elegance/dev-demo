package org.orh.pattern.ch06;

public class PrototypePattern {

    public static void main(String[] args) throws CloneNotSupportedException {
        Resume a = new Resume("大鸟");
        a.setPersonalInfo("男", "29");
        a.setWorkExperience("1998-2000", "XX公司");

        Resume b = (Resume) a.clone();
        b.setWorkExperience("1998-2006", "YY公司");

        Resume c = (Resume) a.clone();
        c.setWorkExperience("1998-2003", "ZZ公司");

        a.display();
        b.display();
        c.display();
    }

    static class Resume implements Cloneable {
        private String name;
        private String sex;
        private String age;

        private WorkExperience work;

        public Resume(String name) {
            this.name = name;
            this.work = new WorkExperience();
        }

        public void setPersonalInfo(String sex, String age) {
            this.sex = sex;
            this.age = age;
        }

        public void setWorkExperience(String workDate, String company) {
            work.setWorkDate(workDate);
            work.setCompany(company);
        }

        public void display() {
            System.out.printf("%s %s %s\n", name, sex, age);
            System.out.printf("工作经历：%s %s\n", work.workDate, work.company);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Resume newResume = (Resume) super.clone();
            newResume.work = (WorkExperience) work.clone();
            return newResume;
        }
    }

    static class WorkExperience implements Cloneable {
        private String workDate;
        private String company;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }
}
