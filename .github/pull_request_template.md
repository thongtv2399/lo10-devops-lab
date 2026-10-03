## Summary

Mô tả ngắn gọn mục đích và nội dung của Pull Request.

- 
- 
- 

## Type of Change

Chọn các loại thay đổi phù hợp:

- [ ] Feature
- [ ] Bug fix
- [ ] Refactoring
- [ ] Test
- [ ] Documentation
- [ ] CI/CD
- [ ] Docker hoặc container configuration
- [ ] Logging hoặc observability
- [ ] Dependency hoặc maintenance

## Related Issue

Issue hoặc task liên quan:

```text
Không có
```

## Changes

Liệt kê các thay đổi chính:

- 
- 
- 

## Validation

Các bước đã thực hiện để kiểm tra thay đổi:

- [ ] Đã chạy Maven test tại local
- [ ] Tất cả Unit Test đều thành công
- [ ] Đã kiểm tra API liên quan
- [ ] Đã kiểm tra validation và HTTP status
- [ ] Đã kiểm tra Docker build
- [ ] Đã kiểm tra Docker Compose
- [ ] Không phát sinh lỗi hoặc warning mới chưa được giải thích

Lệnh kiểm tra:

```bash
./mvnw clean test
```

Kết quả:

```text
Tests run:
Failures:
Errors:
Skipped:
```

## Docker Checklist

Nếu Pull Request thay đổi Docker hoặc container configuration:

- [ ] Docker image build thành công
- [ ] Container chạy bằng non-root user
- [ ] Container Health Check hoạt động
- [ ] Actuator health trả về trạng thái UP
- [ ] Port mapping đã được kiểm tra
- [ ] Không đưa secret vào Docker image
- [ ] Build Context không chứa file không cần thiết
- [ ] Docker Compose chạy thành công
- [ ] Container và network test đã được dọn sau khi kiểm tra

Lệnh build:

```bash
docker build --tag lo10-devops-lab:local .
```

Lệnh kiểm tra Docker Compose:

```bash
docker compose up --detach --build
docker compose ps
docker compose down
```

## Logging Checklist

Nếu Pull Request thay đổi logging:

- [ ] Log được xuất dưới dạng Structured JSON
- [ ] Business data sử dụng structured key-value
- [ ] Log không chứa password, token hoặc secret
- [ ] Correlation ID xuất hiện trong request log
- [ ] Correlation ID được trả về response header
- [ ] MDC được dọn sau khi request hoàn tất
- [ ] Không sử dụng System.out.println trong business code

## Security Checklist

- [ ] Không commit password
- [ ] Không commit API key
- [ ] Không commit token
- [ ] Không commit file .env
- [ ] Không commit private key hoặc certificate nhạy cảm
- [ ] Không mở thêm Actuator endpoint không cần thiết
- [ ] Dependency mới đã được kiểm tra mục đích sử dụng
- [ ] Thay đổi tuân thủ Principle of Least Privilege

## CI Checklist

- [ ] GitHub Actions workflow hợp lệ
- [ ] Test and Package thành công
- [ ] Build Docker Image thành công
- [ ] Artifacts được tạo đúng
- [ ] Action version không sử dụng phiên bản deprecated
- [ ] Không đưa secret trực tiếp vào workflow

## Git Checklist

- [ ] Branch name tuân thủ convention
- [ ] Commit message tuân thủ Conventional Commits
- [ ] Pull Request chỉ chứa thay đổi liên quan
- [ ] Branch đã cập nhật với main
- [ ] Không có merge conflict
- [ ] Không commit build output trong target

## Reviewer Checklist

- [ ] Code dễ đọc và đúng trách nhiệm
- [ ] Không có thay đổi ngoài phạm vi Pull Request
- [ ] Validation và error handling phù hợp
- [ ] Automated test bao phủ thay đổi quan trọng
- [ ] Docker configuration hợp lý
- [ ] Logging có đủ context để điều tra lỗi
- [ ] Không có thông tin nhạy cảm
- [ ] Documentation đã được cập nhật nếu cần

## Screenshots or Logs

Thêm ảnh hoặc log cần thiết để chứng minh kết quả.

```text
Không có
```

## Additional Notes

Thông tin bổ sung dành cho reviewer:

```text
Không có
```