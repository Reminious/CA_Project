# What’s here:

**Important!**

* Leave_applcation table needs two more columns: work dissemination and contact details, reason can’t be null; status is: ‘Applied’, ‘Approved’ and ‘Rejected’. (no ‘Pending’)`  Leave_applcation表需要多两行，work dissemination 和 contact details，而且reason要改为不能是null。Status只能是‘Applied’, ‘Approved’ and ‘Rejected’. (无 ‘Pending’)`
* *Form* and *To* dates must be working days.
  `假期开始和结尾必须是工作日`
* Reports included:
  
  1. Employee on annual/compensation/medical/all leave during a selected period.
  2. Compensation claims for all/particular employee.
     `报告要能打印：员工在选定期间的年度/补偿/医疗/所有休假；所有/个别雇员的补偿假申请`
* Set environment variable SPRING_MAIL_PASSWORD as aizveeybltnbhbta to use email services (Debug as->Configuration->Environment)
* UI & UX will be scored, use [bootstrap](https://getbootstrap.com/docs/5.3/getting-started/introduction/) and [bootstrap icons](https://icons.getbootstrap.com/) for help design
* copy and paste navbar if used, layout.html is shit, dont use it
* ...

---

* [ ] Login/Logout: almost completed, only one interceptor needed to be added.
* [ ] Leave Application Submission: to be added.
* [ ] View Leave History: UI needed(pagination, navbars etc.), update function needed, delete completed.
* [ ] View Leave Application: to be added.
* [ ] View Leave Application for Approval and Subordinate Leave History: to be added.
* [ ] REST Controller and REST Repository: used (might be ok)
* [ ] Compensation Leave Management: to be added.
* [ ] Reporting: to be added.
* [ ] Movement Register: completed.
* [ ] Pagination: need to apply to all tables.
* [ ] Email interactions: to be added. (Forgot password used this, but more is needed) 

> ReactJS and Spring Security: currently aborted.👀️ 
---
**This file should be a guide to use our system in the end**
