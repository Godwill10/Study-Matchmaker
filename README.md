# Study Matchmaker

Study Matchmaker is a full-stack web application that helps students find compatible study partners, connect with classmates, schedule study sessions, invite friends, and message connected friends.

The app is designed around a simple idea: students should be able to find people studying similar subjects, build a trusted network, and organize study time more easily.

---

## Table of Contents

- [Overview](#overview)
- [Core Features](#core-features)
- [How the App Works](#how-the-app-works)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Backend Overview](#backend-overview)
- [Frontend Overview](#frontend-overview)
- [Authentication](#authentication)
- [Study Matching](#study-matching)
- [Connections and Friends](#connections-and-friends)
- [Study Sessions](#study-sessions)
- [Messaging](#messaging)
- [Setup Instructions](#setup-instructions)
- [Available Scripts](#available-scripts)
- [Environment and Configuration](#environment-and-configuration)
- [API Overview](#api-overview)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## Overview

Study Matchmaker allows students to:

- Register and create an account
- Build a student profile
- Browse other students
- Find potential study partners
- Send and respond to connection requests
- Build a friend network
- Create study sessions
- Invite connected friends to sessions
- Join sessions created by connected friends
- Edit or cancel hosted sessions
- Message connected friends only
- See unread message counts in the menu

The application includes a React/TypeScript frontend and a Spring Boot backend with JWT authentication and database persistence.

---

## Core Features

### User Authentication

- User signup
- User login
- JWT-based authentication
- Protected frontend routes
- Password hashing with BCrypt
- Persistent login using browser local storage

### Student Profiles

- Full name
- Email
- School
- State
- City
- Major
- Academic level
- Study style
- Preferred study mode
- Courses
- Bio
- Location visibility
- Profile image URL support

### People Discovery

- Browse other students
- View student profiles
- See shared academic interests
- Navigate to individual student profile pages

### Matching

- Match students based on shared profile and study data
- Match results can include compatibility scores and reasons
- Course-based and profile-based matching support

### Connections / Friends

- Send connection requests
- Accept or decline requests
- View pending requests
- View connected friends
- Display shared courses with friends
- Restrict certain app actions to connected friends only

### Study Sessions

- Create study sessions
- Browse upcoming sessions
- View sessions you joined
- View sessions you host
- Invite connected friends while creating a session
- Join sessions hosted by connected friends
- Leave joined sessions
- Edit hosted sessions
- Cancel hosted sessions
- Prevent invalid actions such as:
    - Joining your own session
    - Joining sessions from non-friends
    - Joining full sessions
    - Editing sessions you do not host
    - Editing non-upcoming sessions
    - Setting a past start time
    - Setting max participants below the current participant count

### Messaging

- Messages page in the main menu
- Text connected friends only
- View conversations with friends
- Send messages
- Store message history
- Track unread messages
- Show unread message count beside the Messages menu item
- Mark messages as read when opening a conversation

---

## How the App Works

### 1. Register

A student creates an account by entering basic information such as:

- Name
- Email
- Password
- State
- University
- Major
- Academic level
- Study style
- Courses

After registration, the user receives a JWT token and is logged in.

### 2. Browse People

Students can browse other users and view profiles. This helps them discover classmates or people with overlapping academic interests.

### 3. Connect with Students

A student can send a connection request to another student. Once accepted, both users become connected friends.

Connections are important because several app features are restricted to friends only.

### 4. Create Study Sessions

A user can create a study session with details such as:

- Title
- Course
- Topic
- Description
- Start time
- End time
- Location
- Mode
- Max participants

The host can also invite connected friends while creating the session.

### 5. Browse and Join Sessions

Students can browse upcoming sessions from themselves and connected friends. They can join sessions if:

- The session is upcoming
- The session is not full
- The session host is their connection
- They are not already the host
- They have not already joined

### 6. Edit or Cancel Hosted Sessions

A host can edit session details after creation. Editing updates the existing session instead of creating a duplicate.

Hosts can update:

- Title
- Course
- Topic
- Description
- Start time
- End time
- Location
- Mode
- Max participants

Hosts can also cancel their own sessions.

### 7. Message Friends

Users can open the Messages page and select a connected friend to chat with.

Messaging is friends-only. The backend checks friendship status before allowing messages to be sent or conversations to be viewed.

Unread message counts appear beside the Messages menu item.

---

## Tech Stack

## Frontend

The frontend is built with:

- React `18.3.1`
- TypeScript `5.8.3`
- Vite `5.4.18`
- React Router DOM `6.30.1`
- Axios `1.9.0`
- Lucide React `1.7.0`
- CSS
- npm

### Frontend Tools and Libraries

| Tool / Library | Purpose |
|---|---|
| React | UI component framework |
| TypeScript | Static typing |
| Vite | Frontend build tool and dev server |
| React Router DOM | Client-side routing |
| Axios | HTTP API requests |
| Lucide React | Icons |
| CSS | Styling |
| npm | Package management |

---

## Backend

The backend is built with:

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate ORM
- Jakarta EE imports
- JWT authentication
- Lombok
- Maven
- PostgreSQL
- H2 support for development/testing

### Backend Tools and Libraries

| Tool / Library | Purpose |
|---|---|
| Spring Boot | Backend application framework |
| Spring MVC | REST API controllers |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| Hibernate | JPA implementation |
| Jakarta Validation | Request validation |
| JWT | Token-based authentication |
| BCrypt | Password hashing |
| Lombok | Reduces boilerplate Java code |
| Maven | Backend build and dependency management |
| PostgreSQL | Production/development database |
| H2 | Optional in-memory database for development/testing |

---

## Database

The application supports relational database persistence.

Main database entities include:

- `User`
- `ConnectionRequest`
- `Friendship`
- `StudySession`
- `Message`

### Database Features

- Users are persisted with unique email addresses
- Passwords are stored as encoded hashes
- User courses are stored as an element collection
- Friendships represent accepted connections
- Study sessions store host and participant relationships
- Messages store sender, receiver, content, timestamps, and read status

---

## Project Structure

```text
Study-Matchmaker/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/studymatchmaker/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       ├── service/
│   │   │   │       └── StudyMatchmakerApplication.java
│   │   │   └── resources/
│   │   └── test/
│   ├── mvnw
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   ├── styles.css
│   │   └── types.ts
│   ├── index.html
│   ├── package.json
│   ├── tsconfig.json
│   └── vite.config.ts
│
├── package.json
├── package-lock.json
└── README.md
```

---

## Backend Overview

The backend is organized into standard Spring layers.

### Controllers

Controllers expose REST API endpoints.

Main controllers include:

- `AuthController`
- `UserController`
- `ConnectionController`
- `StudySessionController`
- `MessageController`
- `ReferenceDataController`

### Services

Services contain business logic.

Main services include:

- `AuthService`
- `UserService`
- `ConnectionService`
- `StudySessionService`
- `MessageService`
- `MatchingService`
- `MatchService`
- `MapperService`
- `ReferenceDataService`

### Repositories

Repositories handle database access using Spring Data JPA.

Main repositories include:

- `UserRepository`
- `ConnectionRequestRepository`
- `FriendshipRepository`
- `StudySessionRepository`
- `MessageRepository`

### Models

Models represent database entities.

Main models include:

- `User`
- `ConnectionRequest`
- `Friendship`
- `StudySession`
- `Message`

### DTOs

DTOs define request and response payloads.

Main DTOs include:

- `RegisterRequest`
- `LoginRequest`
- `AuthResponse`
- `UserProfileDto`
- `FriendDto`
- `ConnectionRequestDto`
- `CreateSessionRequest`
- `StudySessionDto`
- `SendMessageRequest`
- `MessageDto`
- `MatchRequest`
- `MatchResponse`
- `MatchResult`
- `UpdateProfileRequest`

---

## Frontend Overview

The frontend is organized into pages, components, API helpers, and shared types.

### Main Pages

- `Landing.tsx`
- `Login.tsx`
- `Signup.tsx`
- `Dashboard.tsx`
- `People.tsx`
- `StudentProfile.tsx`
- `Profile.tsx`
- `Network.tsx`
- `Schedule.tsx`
- `Messages.tsx`

### Main Components

- `AppShell.tsx`
- `ProtectedRoute.tsx`

### Routing

Routes are handled in `App.tsx`.

Protected routes require a JWT token.

Main protected routes:

- `/dashboard`
- `/people`
- `/people/:id`
- `/schedule`
- `/messages`
- `/network`
- `/profile`

---

## Authentication

Authentication uses JWT.

### Signup Flow

1. User submits registration form.
2. Backend validates request.
3. Backend normalizes email.
4. Backend hashes password.
5. User is saved.
6. JWT token is generated.
7. Frontend stores token and user data in local storage.
8. User is redirected to dashboard.

### Login Flow

1. User submits email and password.
2. Backend authenticates credentials.
3. JWT token is generated.
4. Frontend stores token and user data.
5. User is redirected to dashboard.

### Protected Routes

Frontend routes are protected by checking for a stored token. If no token exists, users are redirected to login.

Backend routes are protected by Spring Security and JWT filtering.

---

## Study Matching

The matching feature helps students discover compatible study partners based on profile information.

Matching can consider:

- Courses
- Major
- Academic level
- Study style
- Preferred study mode
- School
- Location-related data

Match results may include:

- Matched user
- Compatibility score
- Reasons for the match

---

## Connections and Friends

The app uses a connection request system.

### Connection Flow

1. User sends a connection request.
2. Receiver can accept or decline.
3. If accepted, a friendship is created.
4. Friends can:
    - View each other in the network
    - Join each other’s sessions
    - Be invited to study sessions
    - Message each other

### Friends-Only Restrictions

The backend enforces friends-only rules for:

- Joining study sessions
- Inviting users to sessions
- Sending messages
- Viewing message conversations

---

## Study Sessions

Study sessions are one of the core features of the app.

### Session Fields

A study session includes:

- Host
- Participants
- Title
- Course
- Topic
- Description
- Start time
- End time
- Location
- Mode
- Max participants
- Status
- Created timestamp

### Session Statuses

Sessions can have these statuses:

- `UPCOMING`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

### Creating a Session

When creating a session, a host can enter the study details and optionally invite connected friends.

Invited friends are added as participants.

### Editing a Session

Hosts can edit their own upcoming sessions.

Editable details include:

- Title
- Course
- Topic
- Description
- Start time
- End time
- Location
- Mode
- Max participants

The frontend uses:

```http
PUT /api/sessions/{id}
```

to update an existing session.

### Cancelling a Session

Only the host can cancel a session.

### Joining a Session

Users can join a session only if:

- They are connected with the host
- The session is upcoming
- The session is not full
- They are not the host
- They are not already a participant

---

## Messaging

The app includes a friends-only messaging system.

### Messaging Rules

Users can only message people they are connected with.

The backend checks friendship status before:

- Loading a conversation
- Sending a message

### Message Fields

A message includes:

- Sender
- Receiver
- Content
- Created timestamp
- Read timestamp

### Unread Messages

Unread messages are messages where:

- The current user is the receiver
- `readAt` is `null`

The Messages menu item shows an unread count badge.

When a user opens a conversation, messages from that friend are marked as read.

---

## API Overview

Base API path:

```text
/api
```

---

## Auth Endpoints

```http
POST /api/auth/register
POST /api/auth/login
```

### Register

Creates a new account and returns an auth token.

### Login

Authenticates a user and returns an auth token.

---

## User Endpoints

Common user/profile endpoints include:

```http
GET /api/users/me
PUT /api/users/me
GET /api/users
GET /api/users/{id}
```

Exact endpoint names may vary depending on controller implementation.

---

## Connection Endpoints

```http
GET /api/connections/friends
```

Returns connected friends.

Other connection endpoints support sending, accepting, and declining requests.

---

## Study Session Endpoints

```http
POST /api/sessions
PUT /api/sessions/{id}
GET /api/sessions/upcoming
GET /api/sessions/mine
GET /api/sessions/hosted
GET /api/sessions/{id}
POST /api/sessions/{id}/join
POST /api/sessions/{id}/leave
POST /api/sessions/{id}/cancel
```

### `POST /api/sessions`

Creates a new study session.

### `PUT /api/sessions/{id}`

Updates an existing hosted session.

### `GET /api/sessions/upcoming`

Gets upcoming sessions from the user and connected friends.

### `GET /api/sessions/mine`

Gets sessions the current user is participating in.

### `GET /api/sessions/hosted`

Gets sessions hosted by the current user.

### `POST /api/sessions/{id}/join`

Joins a session.

### `POST /api/sessions/{id}/leave`

Leaves a joined session.

### `POST /api/sessions/{id}/cancel`

Cancels a hosted session.

---

## Message Endpoints

```http
GET /api/messages/unread-count
GET /api/messages/{friendId}
POST /api/messages
```

### `GET /api/messages/unread-count`

Returns the number of unread messages for the logged-in user.

### `GET /api/messages/{friendId}`

Returns the conversation with a connected friend and marks received messages from that friend as read.

### `POST /api/messages`

Sends a message to a connected friend.

Example request:

```json
{
  "receiverId": 2,
  "content": "Hey, do you want to study tomorrow?"
}
```

---

## Reference Data Endpoints

Reference data endpoints provide values for signup/profile forms, such as:

- States
- Universities
- Majors
- Academic levels
- Study styles
- Courses

Example paths may include:

```http
GET /api/reference/states
GET /api/reference/universities
GET /api/reference/majors
GET /api/reference/academic-levels
GET /api/reference/study-styles
GET /api/reference/courses
```

---

## Setup Instructions

## Prerequisites

Install:

- Node.js
- npm
- Java 17+
- Maven or Maven Wrapper
- PostgreSQL, if using PostgreSQL

---

## Install Dependencies

From the project root:

```bash
npm install
```

If needed, install frontend dependencies directly:

```bash
cd frontend
npm install
```

---

## Run the App

From the project root:

```bash
npm run dev
```

This starts both:

- Backend Spring Boot server
- Frontend Vite dev server

Frontend usually runs at:

```text
http://localhost:5173
```

Backend usually runs at:

```text
http://localhost:8080
```

---

## Run Backend Only

```bash
cd backend
./mvnw spring-boot:run
```

Or from the project root:

```bash
./backend/mvnw -f backend/pom.xml spring-boot:run
```

---

## Run Frontend Only

```bash
cd frontend
npm run dev
```

---

## Available Scripts

From the root project, common scripts may include:

```bash
npm run dev
npm run dev:backend
npm run dev:frontend
```

From the frontend folder:

```bash
npm run dev
npm run build
npm run preview
```

From the backend folder:

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw clean package
```

---

## Environment and Configuration

Backend configuration is stored in:

```text
backend/src/main/resources/application.yml
```

Common configuration includes:

- Server port
- Database URL
- Database username
- Database password
- JPA/Hibernate settings
- JWT secret
- CORS settings

If port `8080` is already in use, stop the old process:

```bash
lsof -ti :8080 | xargs kill -9
```

Then restart:

```bash
npm run dev
```

---

## Database Notes

If Hibernate auto-update is enabled, schema changes may be applied automatically.

If not, some schema changes may need to be applied manually.

For unread messages, the messages table needs a `read_at` column:

```sql
ALTER TABLE messages ADD COLUMN read_at TIMESTAMP;
```

---

## Security Notes

The app uses:

- JWT tokens
- Protected backend endpoints
- Protected frontend routes
- BCrypt password hashing
- Friends-only backend validation for sensitive interactions

The frontend should not be trusted by itself. The backend enforces key rules such as:

- Only friends can message each other
- Only friends can join each other’s sessions
- Only hosts can edit/cancel their sessions

---

## Error Handling

The backend uses exception handling to return API errors for cases such as:

- Invalid credentials
- User not found
- Session not found
- Invalid session actions
- Unauthorized friend-only actions
- Invalid request data

The frontend displays relevant error messages in the UI.

---

## Future Improvements

Potential improvements include:

- Real-time messaging with WebSockets
- Message typing indicators
- Message read receipts per conversation
- Push notifications
- Email notifications
- File sharing for study materials
- Group chat for study sessions
- Study session reminders
- Calendar integration
- Profile picture uploads
- Location-based matching
- Admin dashboard
- Better mobile chat layout
- Pagination for messages
- Session completion and review flow
- Advanced matching algorithm
- Deployment to cloud platforms

---

## Author

Godwill Afolabi