#!/bin/bash

# Colores
GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
NC=$(tput sgr0)

# Función para mostrar resultado bonito
function check_status {
  if [[ $1 -ge 200 && $1 -lt 300 ]]; then
    echo -e "${GREEN}✓ El endpoint funciona (${1})${NC}"
  else
    echo -e "${RED}✗ El endpoint falló (${1})${NC}"
  fi
}

read -p "Ingresa el ID de la película que quieres usar para GET/PUT/DELETE: " MOVIE_ID
echo -e "\n"

echo "===== CREATE - Crear una película ====="
CREATE_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8001/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"Fargo","releaseYear":1996,"director":"Joel & Ethan Coen","genre":"Crime","durationMinutes":98,"rating":8.1}')
check_status "$CREATE_RESPONSE"
echo -e "\n"

echo "===== READ - Obtener todas las películas ====="
curl -s -X GET http://localhost:8002/movies
echo -e "\n"

echo "===== READ - Obtener película por ID ====="
READ_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X GET http://localhost:8002/movies/$MOVIE_ID)
curl -s -X GET http://localhost:8002/movies/$MOVIE_ID
check_status "$READ_RESPONSE"
echo -e "\n"

echo "===== UPDATE - Actualizar película ====="
UPDATE_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X PUT http://localhost:8003/movies \
  -H "Content-Type: application/json" \
  -d "{\"id\":$MOVIE_ID,\"title\":\"Updated Movie Title\",\"releaseYear\":2024,\"director\":\"Updated Director\",\"genre\":\"Thriller\",\"durationMinutes\":140,\"rating\":9.0}")
check_status "$UPDATE_RESPONSE"
echo -e "\n"

echo "===== READ - Ver todas las películas actualizadas ====="
curl -s -X GET http://localhost:8002/movies
echo -e "\n"

echo "===== DELETE - Eliminar película ====="
DELETE_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8004/movies/$MOVIE_ID)
check_status "$DELETE_RESPONSE"
echo -e "\n"

echo "===== READ - Ver todas las películas después del delete ====="
curl -s -X GET http://localhost:8002/movies
echo -e "\n"

echo -e "${GREEN}===== DONE =====${NC}"
