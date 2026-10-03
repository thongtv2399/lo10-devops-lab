# LO10 DevOps Lab

Project thực hành LO10, tập trung vào Docker, CI/CD, Structured Logging, Correlation ID và Git Workflow cho ứng dụng Java Spring Boot.

## Mục tiêu

Project được xây dựng độc lập để thực hành các nội dung:

- Docker Fundamentals
- Multi-stage Docker Build
- Docker Compose
- Container Health Check
- Graceful Shutdown
- Structured JSON Logging
- Correlation ID
- Maven Test và Package
- GitHub Actions CI
- Feature Branch Workflow
- Pull Request
- Branch Protection
- Conventional Commits

Project không sử dụng Database, JPA, Security hoặc nghiệp vụ phức tạp để giữ đúng trọng tâm LO10.

## Công nghệ sử dụng

- Java 21
- Spring Boot 4.1.1
- Maven Wrapper
- Spring Web
- Spring Boot Actuator
- Jakarta Validation
- JUnit 5
- Mockito
- MockMvc
- Docker
- Docker Compose
- GitHub Actions
- SLF4J
- Logback
- MDC

## Kiến trúc tổng quan

```text
Client
  |
  v
CorrelationIdFilter
  |
  v
Spring Boot REST API
  |
  v
MessageService
  |
  v
ConcurrentHashMap
```

Luồng CI:

```text
Git Push hoặc Pull Request
            |
            v
GitHub Actions
            |
            v
Setup Java 21
            |
            v
Maven Test
            |
            v
Maven Package
            |
            v
Upload Artifacts
            |
            v
Docker Build
```

## Cấu trúc project

```text
lo10-devops-lab
|
|-- .github
|   `-- workflows
|       `-- ci.yml
|
|-- .mvn
|   `-- wrapper
|       `-- maven-wrapper.properties
|
|-- src
|   |-- main
|   |   |-- java
|   |   |   `-- com.thongtv5.lo10devopslab
|   |   |       |-- Lo10DevopsLabApplication.java
|   |   |       |-- controller
|   |   |       |   |-- HealthController.java
|   |   |       |   `-- MessageController.java
|   |   |       |-- dto
|   |   |       |   |-- CreateMessageRequest.java
|   |   |       |   `-- MessageResponse.java
|   |   |       |-- logging
|   |   |       |   `-- CorrelationIdFilter.java
|   |   |       `-- service
|   |   |           `-- MessageService.java
|   |   |
|   |   `-- resources
|   |       `-- application.properties
|   |
|   `-- test
|       `-- java
|           `-- com.thongtv5.lo10devopslab
|               |-- Lo10DevopsLabApplicationTests.java
|               |-- controller
|               |   |-- HealthControllerTest.java
|               |   `-- MessageControllerTest.java
|               |-- logging
|               |   `-- CorrelationIdFilterTest.java
|               `-- service
|                   `-- MessageServiceTest.java
|
|-- .dockerignore
|-- .gitattributes
|-- .gitignore
|-- Dockerfile
|-- compose.yml
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
`-- README.md
```

## API

### Kiểm tra trạng thái ứng dụng

```http
GET /api/health
```

Response:

```json
{
  "status": "UP",
  "service": "lo10-devops-lab",
  "timestamp": "2026-10-03T12:43:02Z"
}
```

### Tạo Message

```http
POST /api/messages
Content-Type: application/json
```

Request:

```json
{
  "content": "Docker and CI/CD are ready"
}
```

Response:

```http
HTTP/1.1 201 Created
Location: /api/messages/1
```

```json
{
  "id": 1,
  "content": "Docker and CI/CD are ready",
  "createdAt": "2026-10-03T12:30:29Z"
}
```

### Lấy Message theo ID

```http
GET /api/messages/1
```

Nếu Message tồn tại:

```http
HTTP/1.1 200 OK
```

Nếu Message không tồn tại:

```http
HTTP/1.1 404 Not Found
```

## Validation

Trường `content` có các điều kiện:

- Không được null.
- Không được rỗng.
- Không được chỉ chứa khoảng trắng.
- Không được vượt quá 500 ký tự.

Request không hợp lệ:

```json
{
  "content": "   "
}
```

Kết quả:

```http
HTTP/1.1 400 Bad Request
```

## Actuator

Các endpoint được public:

```text
/actuator/health
/actuator/info
/actuator/health/liveness
/actuator/health/readiness
```

Kiểm tra health:

```bash
curl http://localhost:8080/actuator/health
```

Kiểm tra liveness:

```bash
curl http://localhost:8080/actuator/health/liveness
```

Kiểm tra readiness:

```bash
curl http://localhost:8080/actuator/health/readiness
```

## Chạy ứng dụng bằng Maven

Chạy test:

```bash
./mvnw clean test
```

Chạy ứng dụng:

```bash
./mvnw spring-boot:run
```

Kiểm tra API:

```bash
curl http://localhost:8080/api/health
```

## Kết quả test

```text
Lo10DevopsLabApplicationTests : 1 test
HealthControllerTest          : 2 tests
MessageServiceTest            : 7 tests
MessageControllerTest         : 6 tests
CorrelationIdFilterTest       : 4 tests

Tests run                     : 20
Failures                      : 0
Errors                        : 0
Skipped                       : 0
```

## Docker

Project sử dụng Multi-stage Docker Build.

### Build stage

Build stage sử dụng Java 21 JDK để:

- Tải Maven dependencies.
- Compile source code.
- Chạy automated tests.
- Package Spring Boot JAR.

### Runtime stage

Runtime stage sử dụng Java 21 JRE và chỉ chứa:

- Java Runtime.
- Spring Boot JAR.
- Công cụ phục vụ Health Check.

Source code, Maven và test source không được đưa vào final runtime image.

Ứng dụng chạy bằng non-root user:

```text
spring
```

## Build Docker image

```bash
docker build \
  --tag lo10-devops-lab:1.0.1 \
  .
```

Kiểm tra image:

```bash
docker image ls lo10-devops-lab
```

## Chạy Docker container

```bash
docker run \
  --detach \
  --name lo10-devops-lab \
  --publish 8081:8080 \
  lo10-devops-lab:1.0.1
```

Kiểm tra container:

```bash
docker ps
```

Kiểm tra log:

```bash
docker logs lo10-devops-lab
```

Kiểm tra API:

```bash
curl http://localhost:8081/api/health
```

Dừng và xóa container:

```bash
docker stop lo10-devops-lab
docker rm lo10-devops-lab
```

## Docker Health Check

Dockerfile sử dụng endpoint:

```text
/actuator/health
```

Khi ứng dụng sẵn sàng, Docker hiển thị:

```text
Up (healthy)
```

Health Check giúp phân biệt:

```text
Container process đang chạy
```

với:

```text
Application thực sự sẵn sàng phục vụ request
```

## Docker Compose

Build và chạy service:

```bash
docker compose up \
  --detach \
  --build
```

Kiểm tra service:

```bash
docker compose ps
```

Xem log:

```bash
docker compose logs app
```

Theo dõi log:

```bash
docker compose logs \
  --follow \
  app
```

Dừng Compose stack:

```bash
docker compose down
```

## Structured JSON Logging

Ứng dụng xuất Console Log dưới dạng JSON theo Logstash format.

Ví dụ:

```json
{
  "@timestamp": "2026-10-03T19:40:48.21008+07:00",
  "@version": "1",
  "message": "Started Lo10DevopsLabApplication",
  "logger_name": "com.thongtv5.lo10devopslab.Lo10DevopsLabApplication",
  "thread_name": "main",
  "level": "INFO"
}
```

Lợi ích:

- Log có cấu trúc rõ ràng.
- Dễ parse bởi logging platform.
- Dễ tìm kiếm theo level, logger và operation.
- Phù hợp với môi trường container.

## Business Logging

MessageService ghi các field nghiệp vụ riêng:

```text
messageId
operation
removedMessageCount
```

Ví dụ:

```json
{
  "message": "Message created successfully",
  "level": "INFO",
  "messageId": 1,
  "operation": "createMessage"
}
```

Không ghép dữ liệu nghiệp vụ vào chuỗi log giúp logging platform có thể filter theo từng field.

## Correlation ID

Ứng dụng sử dụng header:

```text
X-Correlation-ID
```

Nếu Client gửi Correlation ID:

```bash
curl \
  --include \
  --header "X-Correlation-ID: lo10-test-001" \
  http://localhost:8080/api/health
```

Ứng dụng giữ nguyên giá trị và trả lại trong response:

```text
X-Correlation-ID: lo10-test-001
```

Nếu Client không gửi header, ứng dụng tự tạo UUID:

```text
X-Correlation-ID: 4444fe3f-c312-4fbf-b339-262724349b8c
```

Correlation ID được đưa vào MDC để xuất hiện trong Structured JSON Log.

Sau khi request hoàn tất, Correlation ID được xóa khỏi MDC để tránh rò rỉ context giữa các request sử dụng cùng thread.

## GitHub Actions CI

Workflow:

```text
.github/workflows/ci.yml
```

Pipeline được kích hoạt khi:

- Push vào `main`.
- Pull Request vào `main`.
- Chạy thủ công bằng `workflow_dispatch`.

### Job Test and Package

Thực hiện:

- Checkout source code.
- Setup Java 21.
- Cache Maven dependencies.
- Chạy 20 automated tests.
- Package Spring Boot JAR.
- Upload application JAR.
- Upload Maven Surefire reports.

### Job Build Docker Image

Chỉ chạy khi job Test and Package thành công.

Thực hiện:

- Checkout source code.
- Build Docker image.
- Inspect Docker image.

## CI Artifacts

Workflow tạo hai artifacts:

```text
lo10-devops-lab-jar
lo10-test-reports
```

Artifacts được lưu trong 7 ngày theo cấu hình workflow.

## Git Workflow

Quy ước branch:

```text
feature/<tên-tính-năng>
fix/<tên-lỗi>
test/<nội-dung-test>
docs/<nội-dung-tài-liệu>
ci/<nội-dung-pipeline>
chore/<công-việc-bảo-trì>
```

Ví dụ:

```text
feature/add-message-api
fix/health-check-port
test/add-controller-tests
docs/add-project-readme
ci/update-github-actions
```

Quy trình:

```text
main
  |
  v
Feature Branch
  |
  v
Local Test
  |
  v
Commit
  |
  v
Push Branch
  |
  v
Pull Request
  |
  v
CI Validation
  |
  v
Squash and Merge
  |
  v
main
```

## Conventional Commits

Các commit message đã sử dụng:

```text
feat: initialize LO10 DevOps lab
ci: update GitHub Actions runtime versions
```

Các loại commit đề xuất:

```text
feat
fix
test
docs
ci
chore
refactor
```

## Branch Protection

Nhánh `main` được bảo vệ bằng ruleset:

```text
Require Pull Request before merging
Require status checks to pass
Require branch to be up to date
Block force pushes
Restrict deletions
```

Các status check bắt buộc:

```text
Test and Package
Build Docker Image
```

Required approvals được đặt bằng `0` vì đây là repository cá nhân.

## Trạng thái hiện tại

Đã hoàn thành:

- Spring Boot REST API tối giản.
- Validation.
- 20 Automated Tests.
- Docker Multi-stage Build.
- Non-root Container.
- Docker Health Check.
- Docker Compose.
- Graceful Shutdown.
- Structured JSON Logging.
- Business Log Fields.
- Correlation ID.
- GitHub Actions CI.
- CI Artifacts.
- Feature Branch Workflow.
- Pull Request Validation.
- Branch Protection.

## Phạm vi chưa triển khai

Project chưa thực hiện Continuous Deployment tới:

- Cloud Server.
- Kubernetes.
- AWS ECS.
- Azure Container Apps.
- Docker Registry.

Pipeline hiện tại tập trung vào Continuous Integration:

```text
Compile
Test
Package
Upload Artifacts
Build Docker Image
```