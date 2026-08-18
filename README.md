# 07_DisponibilidadAsientos_Grupo07

Proyecto Backend Spring Boot y Suite de Automatización para el módulo de **Disponibilidad de Asientos** de la plataforma **EventPass**.

---   HUARIPAUCAR CARDENAS, Jesus Angelo

## 👥 Integrantes y Roles

| Nombre Completo | Rol | Responsabilidad Principal |
| :--- | :--- | :--- |
| **ESPINOZA NOVOA, Roberto Carlos** | **QA LEAD** | Coordinación, gestión de documentación y entregables, arquitectura de pruebas. |
| **REJAS CARRASCO, Keller Steven** | **QA TESTER** | Diseño y ejecución de casos de prueba manuales (CP-01 y CP-02), registro de evidencias. |
| **HUARIPAUCAR CARDENAS, Jesus Angelo** | **QA AUTOMATION** | Desarrollo de la suite de automatización Spring Boot / MockMvc (CP-01 a CP-04), assertions y backend. |
| **VILCAPUMA TRUJILLO, Marilyn Angy** | **QA TESTER** | Diseño y ejecución de casos de prueba manuales (CP-03 y CP-04), reporte de defectos. |

---

## 🎯 Funcionalidad Asignada
**Reto 07: Disponibilidad de asientos**
- **Descripción:** Módulo encargada de consultar la disponibilidad de ubicaciones por zona (VIP, Preferencial, General), filtrar asientos según su estado (*AVAILABLE*, *OCCUPIED*, *BLOCKED*) y controlar la reserva evitando duplicidades o selección de asientos no disponibles.

---

## 📝 Casos de Prueba Realizados (Manuales)

1. **CP-01 – Consulta de disponibilidad general y leyenda de estados**
   - **Precondición:** Evento con asientos registrados.
   - **Entrada:** `GET /api/seats`
   - **Resultado Esperado:** Retorna la lista completa de asientos indicando su estado y precio por zona.
   - **Estado:** `PASS`

2. **CP-02 – Intento de reserva de un asiento ocupado (Escenario Negativo)**
   - **Precondición:** Asiento `VIP-A3` con estado `OCCUPIED`.
   - **Entrada:** `POST /api/seats/reserve` con payload `{"seatCode": "VIP-A3", "userId": "USER-123"}`
   - **Resultado Esperado:** Código HTTP `409 Conflict` con mensaje: *"El asiento 'VIP-A3' ya se encuentra reservado u ocupado y no está disponible."*
   - **Estado:** `PASS`

3. **CP-03 – Reserva exitosa de asiento libre**
   - **Precondición:** Asiento `VIP-A1` con estado `AVAILABLE`.
   - **Entrada:** `POST /api/seats/reserve` con payload `{"seatCode": "VIP-A1", "userId": "USER-456"}`
   - **Resultado Esperado:** Código HTTP `200 OK` y actualización del estado a `OCCUPIED`.
   - **Estado:** `PASS`

4. **CP-04 – Resumen y contadores de disponibilidad**
   - **Precondición:** Módulo de asientos inicializado.
   - **Entrada:** `GET /api/seats/summary`
   - **Resultado Esperado:** Objeto JSON con conteos exactos (`totalSeats`, `availableSeats`, `occupiedSeats`).
   - **Estado:** `PASS`

---

## 🤖 Casos Automatizados

Se automatizaron los casos **CP-01** (Consulta general) y **CP-02** (Bloqueo de reserva sobre asiento ocupado):

- **Herramienta Utilizada:** Java 21 + Spring Boot Starter Test + MockMvc + JUnit 5 + Hamcrest Assertions.
- **Ubicación del Código de Pruebas:** [`src/test/java/com/eventpass/seats/SeatControllerTest.java`](file:///C:/Users/XhaPP/.gemini/antigravity/scratch/eventpass-backend/src/test/java/com/eventpass/seats/SeatControllerTest.java)

### Justificación de Automatización:
- **CP-01:** Es una prueba de regresión crítica ejecutada constantemente para asegurar que la grilla de asientos cargue correctamente.
- **CP-02:** Valida la lógica de negocio crítica de sobreventa mediante aserciones estrictas sobre respuestas de conflicto (HTTP 409).

---

## 🚀 Instrucciones para Ejecutar el Backend y las Pruebas

### Requisitos Previos:
- **Java JDK 17 o 21** instalado.
- **Maven** o IDE (IntelliJ IDEA / VS Code / Eclipse).

### 1. Ejecutar las Pruebas Automatizadas (QA Automation):
```bash
mvn test
```
*Todas las aserciones de `SeatControllerTest` se ejecutarán y generarán el reporte de PASS/FAIL en consola.*

### 2. Iniciar el Servidor Backend:
```bash
mvn spring-boot:run
```
El servidor iniciará en: `http://localhost:8080`

### 3. Probar Endpoints REST:
- **Obtener todos los asientos:** `GET http://localhost:8080/api/seats`
- **Filtrar por zona VIP:** `GET http://localhost:8080/api/seats?zone=VIP`
- **Resumen de disponibilidades:** `GET http://localhost:8080/api/seats/summary`
- **Intentar reservar ocupado (Error 409):** 
  ```bash
  curl -X POST http://localhost:8080/api/seats/reserve -H "Content-Type: application/json" -d "{\"seatCode\":\"VIP-A3\",\"userId\":\"U1\"}"
  ```
- **Consola de Base de Datos H2:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:eventpassdb`, Usuario: `sa`, Contraseña: vacía)
