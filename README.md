# itsec-backend-springboot

Sample Backend Rest Client Services 

**Technology Stacks:**
1. Language: Java
2. Framework Spring Boot
3. RDBMS PostgreSQL
4. Redis


**API Features**
- Application develop with Layered Architecture
- Authentication with JWT
- MFA with email
- Security Block With Rate Limit Access
- RBAC with 4 Roles (SUPER_ADMIN, EDITOR, CONTRIBUTOR, VIEWER)
- Logging Activity
- Unit Testing with Mockito
- Docker Base


**Api Collection Documentation**

https://documenter.getpostman.com/view/193001/2sAYQfDpMu

**How to Run**

copy `.env.example` to `.env`   
run `docker-compose up -d`   
run `docker-compose logs -f` to view logs  

run `docker-compose down -v` to stop application   

use `http://localhost:9090` to set api base url 



