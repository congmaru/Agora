-- 비밀번호 : qwer1234!!
insert into user(`department`, `position`, `name`, `password`, `user_id`,`role`) values("SSAFY", "교육생", "홍길동", "$2a$10$85QgRw0Nxqv4QnICNc3aNO/l8ftp/aoYslLduHqh1.LwmIIea8avq", "test-1","ROLE_ADMIN");
insert into conference_category(`name`) values('업무'),('교육'),('기타');