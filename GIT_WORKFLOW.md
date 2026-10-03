# Git Workflow

Tài liệu mô tả Git Workflow được áp dụng cho project
`lo10-devops-lab`.

## Mục tiêu

Git Workflow giúp bảo đảm:

- Mọi thay đổi được thực hiện trên branch riêng.
- Nhánh `main` luôn ở trạng thái ổn định.
- Thay đổi được kiểm tra bằng Pull Request.
- Automated Test phải thành công trước khi merge.
- Docker Image phải build thành công trước khi merge.
- Commit history dễ đọc và dễ truy vết.
- Không đưa thông tin nhạy cảm vào repository.

## Nhánh chính

Nhánh chính của project:

```text
main
```

Nhánh `main` đại diện cho phiên bản đã được kiểm tra và
sẵn sàng sử dụng.

Không thực hiện thay đổi trực tiếp trên `main`.

Mọi thay đổi phải đi qua:

```text
Feature Branch
        ↓
Local Validation
        ↓
Commit
        ↓
Push Branch
        ↓
Pull Request
        ↓
CI Validation
        ↓
Squash and Merge
        ↓
main
```

## Quy ước đặt tên branch

### Feature

Dùng khi thêm chức năng mới:

```text
feature/<tên-chức-năng>
```

Ví dụ:

```text
feature/add-message-api
feature/add-correlation-id
feature/add-health-endpoint
```

### Fix

Dùng khi sửa lỗi:

```text
fix/<tên-lỗi>
```

Ví dụ:

```text
fix/health-check-port
fix/missing-correlation-header
fix/docker-startup-error
```

### Test

Dùng khi thêm hoặc sửa automated test:

```text
test/<nội-dung-test>
```

Ví dụ:

```text
test/add-controller-tests
test/add-correlation-filter-tests
```

### Documentation

Dùng khi cập nhật tài liệu:

```text
docs/<nội-dung-tài-liệu>
```

Ví dụ:

```text
docs/add-project-readme
docs/add-pr-template
docs/add-git-workflow
```

### CI

Dùng khi thay đổi GitHub Actions hoặc CI Pipeline:

```text
ci/<nội-dung-thay-đổi>
```

Ví dụ:

```text
ci/add-docker-build
ci/update-github-actions
ci/upload-test-reports
```

### Chore

Dùng cho công việc bảo trì không thay đổi nghiệp vụ:

```text
chore/<nội-dung-bảo-trì>
```

Ví dụ:

```text
chore/update-dependencies
chore/clean-unused-files
```

### Refactor

Dùng khi cải thiện cấu trúc code nhưng không thay đổi hành vi:

```text
refactor/<nội-dung-thay-đổi>
```

Ví dụ:

```text
refactor/message-service
refactor/logging-configuration
```

## Quy trình bắt đầu công việc

Luôn bắt đầu từ `main` mới nhất:

```bash
git switch main

git pull --ff-only origin main
```

Tạo branch mới:

```bash
git switch --create feature/example-change
```

Kiểm tra branch hiện tại:

```bash
git branch --show-current
```

Kiểm tra trạng thái:

```bash
git status
```

Kết quả cần có:

```text
On branch feature/example-change
nothing to commit, working tree clean
```

## Conventional Commits

Project áp dụng cấu trúc commit:

```text
<type>: <mô-tả-ngắn>
```

Ví dụ:

```text
feat: add message API
fix: correct container health check
test: add message controller tests
docs: add project README
ci: update GitHub Actions runtime versions
chore: update Maven dependencies
refactor: simplify message lookup
```

## Các loại commit

### feat

Thêm chức năng mới:

```text
feat: add correlation ID filter
```

### fix

Sửa lỗi:

```text
fix: correct Docker port mapping
```

### test

Thêm hoặc sửa test:

```text
test: add health controller tests
```

### docs

Cập nhật tài liệu:

```text
docs: add Git workflow guide
```

### ci

Thay đổi CI/CD Pipeline:

```text
ci: add Docker image build job
```

### chore

Công việc bảo trì:

```text
chore: update project configuration
```

### refactor

Cải thiện cấu trúc code nhưng không thay đổi hành vi:

```text
refactor: simplify message service
```

## Quy tắc commit message

Commit message nên:

- Ngắn gọn.
- Mô tả đúng thay đổi.
- Sử dụng động từ hiện tại.
- Không kết thúc bằng dấu chấm.
- Chỉ chứa một nhóm thay đổi có liên quan.

Commit tốt:

```text
feat: add message API
```

```text
ci: update GitHub Actions runtime versions
```

```text
docs: add pull request template
```

Commit không tốt:

```text
update
```

```text
fix code
```

```text
change many things
```

```text
final version
```

## Local Validation

Trước khi commit, chạy automated test:

```bash
./mvnw clean test
```

Kết quả yêu cầu:

```text
Tests run: 20
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Nếu thay đổi Dockerfile, `.dockerignore` hoặc `compose.yml`,
cần kiểm tra thêm Docker Build:

```bash
docker build \
  --tag lo10-devops-lab:local \
  .
```

Nếu thay đổi Docker Compose:

```bash
docker compose up \
  --detach \
  --build

docker compose ps

docker compose down
```

## Kiểm tra thay đổi trước commit

Kiểm tra trạng thái:

```bash
git status
```

Kiểm tra nội dung thay đổi:

```bash
git diff
```

Kiểm tra các file chuẩn bị commit:

```bash
git diff --staged
```

Không commit:

```text
target/
.env
.env.*
secrets/
*.log
Private Key
Access Token
Password
API Key
```

## Tạo commit

Stage file cụ thể:

```bash
git add*