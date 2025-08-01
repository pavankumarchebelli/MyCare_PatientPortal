
# 🏥 Labcorp Appointment Microservices – Deliverables

---

## ✅ 1. ERD Diagram

```mermaid
erDiagram
    PATIENT ||--o{ PATIENT_AVAILABILITY : has
    PATIENT ||--o{ PATIENT_ADDRESS : has
    LABCORP_LOCATION ||--o{ LABCORP_AVAILABILITY : provides
    PATIENT ||--o{ BOOKING : makes
    LABCORP_LOCATION ||--o{ BOOKING : hosts

    PATIENT {
        UUID patientId PK
        string name
        string gender
        date dob
        string phone
        string email
        UUID addressId
    }

    PATIENT_ADDRESS {
        UUID addressId PK
        UUID patientId FK
        string street
        string zipcode
        string state
        string county
        double latitude
        double longitude
    }

    PATIENT_AVAILABILITY {
        UUID availabilityId PK
        UUID patientId FK
        date date
        string preferredSlot
    }

    LABCORP_LOCATION {
        UUID locationId PK
        string locationName
        string address
        double latitude
        double longitude
    }

    LABCORP_AVAILABILITY {
        UUID availabilityId PK
        UUID locationId FK
        date date
        boolean morningSlot
        boolean afternoonSlot
        boolean eveningSlot
    }

    BOOKING {
        UUID bookingId PK
        UUID patientId FK
        UUID locationId FK
        string slot
        date bookingDate
    }
```

---

## ✅ 2. PostgreSQL Schema

Each service has its own DB: `patientdb`, `labcorpdb`, `bookingdb`.

Refer to previous messages for detailed `CREATE TABLE` SQL for each table with UUID PKs and check constraints.

---

## ✅ 3. C2 Architecture (Container View)

```mermaid
graph TD
  Client[User Interface / Postman] --> API_Gateway["API Gateway (Optional)"]

  subgraph Services
    PatientApp["Patient Application"]
    LabcorpApp["Labcorp Application"]
    BookingApp["Booking Application (BookingController handles logic)"]
    GeoService["Geolocation Service (Optional)"]
  end

  API_Gateway --> PatientApp
  API_Gateway --> LabcorpApp
  API_Gateway --> BookingApp

  BookingApp --> PatientApp
  BookingApp --> LabcorpApp
  BookingApp --> GeoService

  subgraph Databases
    PatientDB["PostgreSQL: patientdb"]
    LabcorpDB["PostgreSQL: labcorpdb"]
    BookingDB["PostgreSQL: bookingdb"]
  end

  PatientApp --> PatientDB
  LabcorpApp --> LabcorpDB
  BookingApp --> BookingDB


```

---

## ✅ 4. C4 Architecture (Component View for BookingService)

```mermaid
graph TD
  Controller["BookingController"]
  Repo["BookingRepository"]
  RestClient1["RestTemplate: PatientService"]
  RestClient2["RestTemplate: LabcorpService"]
  GeoUtil["GeoUtils (Distance Logic)"]
  DB["PostgreSQL: bookingdb"]

  Controller --> Repo
  Controller --> RestClient1
  Controller --> RestClient2
  Controller --> GeoUtil
  Repo --> DB

```

---
