# 1. Crear namespace
echo "📁 Creando namespace..."
kubectl apply -f k8s/mariadb-namespace.yaml

# 2. Aplicar recursos de MariaDB
echo "🗄️ Desplegando MariaDB..."
kubectl apply -f k8s/mariadb-pv.yaml
kubectl apply -f k8s/mariadb-pvc.yaml
kubectl apply -f k8s/mariadb-secret.yaml
kubectl apply -f k8s/mariadb-deployment.yaml
kubectl apply -f k8s/mariadb-service.yaml

# 3. Esperar a que MariaDB esté listo
echo "⏳ Esperando a que MariaDB esté listo..."
kubectl wait --for=condition=available --timeout=300s deployment/mariadb-deployment -n movies-app

# 4. Aplicar recursos de backup
echo "💾 Configurando sistema de backup..."
kubectl apply -f k8s/backups/backup-pv.yaml
kubectl apply -f k8s/backups/backup-pvc.yaml
kubectl apply -f k8s/backups/backup-cronjob.yaml

# 5. Desplegar microservicios
echo "🔧 Desplegando microservicios..."
kubectl apply -f k8s/create-microservice-configmap.yaml
kubectl apply -f k8s/create-microservice-deployment.yaml
kubectl apply -f k8s/create-microservice-service.yaml
kubectl apply -f k8s/read-microservice-deployment.yaml
kubectl apply -f k8s/read-microservice-service.yaml
kubectl apply -f k8s/update-microservice-deployment.yaml
kubectl apply -f k8s/update-microservice-service.yaml
kubectl apply -f k8s/delete-microservice-deployment.yaml
kubectl apply -f k8s/delete-microservice-service.yaml

# 6. Aplicar Ingress
echo "🌐 Configurando Ingress..."
kubectl apply -f k8s/ingress.yaml

# 7. Verificar el estado del despliegue
echo "✅ Verificando estado del despliegue..."
kubectl get all -n movies-app
