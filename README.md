# k8s-capstone-deploy

![Repo Size](https://img.shields.io/github/repo-size/VargasCardona/k8s-capstone-deploy?style=for-the-badge)
![License](https://img.shields.io/github/license/VargasCardona/k8s-capstone-deploy?style=for-the-badge)
![Last Commit](https://img.shields.io/github/last-commit/VargasCardona/k8s-capstone-deploy?style=for-the-badge)

This repository contains the final deployment setup for a Kubernetes-based application. It is designed to demonstrate a complete, production-ready environment with scalable microservices, efficient resource management, and secure configurations.

---

## Project Overview

This deployment leverages Kubernetes to manage containerized applications efficiently. Key features include:

- **Microservices Architecture**: Modular services for create, read, update, and delete operations in isolated containers.
- **Ingress Management**: Centralized routing through an ingress controller.
- **Auto-scaling**: Handle traffic load using Kubernetes' Horizontal Pod Autoscaler (HPA).
- **Persistent Storage**: Maintain state using persistent volumes (PV) and claims (PVC).
- **Backup Strategy**: CronJob-based routine for data backup.

---

## Repository Structure

```
.
├── CONTRIBUTING.md
├── LICENSE
├── k8s/
│   ├── backups/
│   ├── ingress.yaml
│   ├── mariadb-*.yaml
│   ├── *-microservice-*.yaml
├── microservices/
│   ├── create/
│   ├── read/
│   ├── update/
│   └── delete/
```

### `k8s/` Directory

Contains Kubernetes manifest files for:

- **Database (MariaDB)**:
  - Deployment, Service, PVC, PV, Secrets, Namespace
- **Microservices**:
  - Deployment, Service, ReplicaSet for each CRUD operation
- **Ingress**:
  - Routing rules to expose services
- **Backups**:
  - CronJob and storage setup

### `microservices/` Directory

Each subdirectory (`create`, `read`, `update`, `delete`) contains:

- Java Spring Boot codebase
- `Dockerfile` and `docker-compose.yml` (for local development)
- Gradle configuration and tests


## Infrastructure Diagram

![Diagrama de infraestructura(1)](https://github.com/user-attachments/assets/5b2fc6d2-0174-4594-bbaa-0cdf7851ef39)


## Deployment Steps

### Pre-requisites

- Kubernetes cluster (e.g., Minikube, GKE, EKS)
- `kubectl` CLI configured
- Docker (for image builds)
- Helm (optional for Ingress Controller installation)

### Build Docker Images

Each microservice should be built and pushed to a container registry:

```bash
cd microservices/create
docker build -t <registry>/create-service:latest .
docker push <registry>/create-service:latest
# Repeat for read, update, delete
```

### Apply Kubernetes Manifests

```bash
kubectl apply -f k8s/mariadb-namespace.yaml
kubectl apply -f k8s/mariadb-secret.yaml
kubectl apply -f k8s/mariadb-pv.yaml
kubectl apply -f k8s/mariadb-pvc.yaml
kubectl apply -f k8s/mariadb-deployment.yaml
kubectl apply -f k8s/mariadb-service.yaml

kubectl apply -f k8s/create-microservice-deployment.yaml
kubectl apply -f k8s/create-microservice-service.yaml

# Repeat for read, update, delete

kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/backups/
```

---

## Secrets & Configs

Sensitive values like database credentials are stored in Kubernetes secrets (`mariadb-secret.yaml`). Environment variables are passed through `EnvLoader.java` in each microservice.

---

## Testing

Each microservice includes integration and unit tests located in `src/test`. Use the following command to run tests:

```bash
./gradlew test
```

---

## Contributors

- **Nicolás Vargas Cardona** - [GitHub](https://github.com/VargasCardona)
- **Mateo Loaiza García** - [GitHub](https://github.com/Matthub05)
- **Carmen Juliana Marin** - [GitHub](https://github.com/julianaMarin12)

---

## License

This project is licensed under the [GNU General Public License](https://www.gnu.org/licenses/) - see the LICENSE file for details.
