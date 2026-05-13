# Monitoring Setup — Prometheus + Grafana (Windows)

## 1. Download Prometheus
- Go to: https://prometheus.io/download/
- Download the Windows zip (prometheus-x.x.x.windows-amd64.zip)
- Extract to: C:\monitoring\prometheus\
- Copy prometheus.yml from this folder into C:\monitoring\prometheus\

## 2. Start Prometheus
```powershell
cd C:\monitoring\prometheus
.\prometheus.exe --config.file=prometheus.yml
```
- Dashboard: http://localhost:9090

## 3. Download Grafana
- Go to: https://grafana.com/grafana/download?platform=windows
- Download the Windows installer
- Install and start the Grafana service

## 4. Open Grafana
- URL: http://localhost:3000
- Default login: admin / admin

## 5. Add Prometheus as data source in Grafana
- Go to: Configuration → Data Sources → Add data source
- Select: Prometheus
- URL: http://localhost:9090
- Click: Save & Test

## 6. Import Spring Boot dashboard
- Go to: Dashboards → Import
- Enter dashboard ID: 11378 (Spring Boot Micrometer)
- Select your Prometheus data source
- Click: Import

## Metrics endpoints (once services are running)
- Gateway:  http://localhost:8080/actuator/prometheus
- Users:    http://localhost:8081/api/v1/actuator/prometheus
- Orders:   http://localhost:8082/api/v1/actuator/prometheus
- Products: http://localhost:8083/api/v1/actuator/prometheus
