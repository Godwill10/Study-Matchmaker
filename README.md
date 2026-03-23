# Study Matchmaker V2 Scaffold

This folder is a V2 upgrade scaffold built on top of your latest Study Matchmaker codebase.

## Included in this package
- guided registration backed by reference-data endpoints
- state -> university dropdown flow
- major, academic level, study style dropdowns
- courses by academic level multi-select
- editable profile page
- seeded dummy students for matching demos
- upgraded explainable matching logic
- friend requests and friends list
- session requests with approve/deny flow
- meetings calendar page with clickable meeting links
- match history page with review prompts
- student directory and student profile pages

## Important backend paths
- `backend/src/main/java/com/studymatchmaker/controller/ReferenceDataController.java`
- `backend/src/main/java/com/studymatchmaker/controller/FriendController.java`
- `backend/src/main/java/com/studymatchmaker/controller/SessionRequestController.java`
- `backend/src/main/java/com/studymatchmaker/controller/StudyMeetingController.java`
- `backend/src/main/java/com/studymatchmaker/controller/MatchHistoryController.java`
- `backend/src/main/java/com/studymatchmaker/service/ReferenceDataService.java`
- `backend/src/main/java/com/studymatchmaker/service/DummyStudentSeeder.java`

## Important frontend paths
- `frontend/src/components/RegistrationForm.tsx`
- `frontend/src/components/ProfileEditor.tsx`
- `frontend/src/pages/Profile.tsx`
- `frontend/src/pages/Students.tsx`
- `frontend/src/pages/StudentProfile.tsx`
- `frontend/src/pages/CalendarPage.tsx`
- `frontend/src/pages/RequestsPage.tsx`
- `frontend/src/pages/FriendsPage.tsx`
- `frontend/src/pages/HistoryPage.tsx`

## Notes
- This package is designed as a strong integrated starting point. It adds the requested features and structure, but it still deserves a local compile-and-polish pass in your environment.
- Backend currently defaults to H2 so you can run quickly. You can swap back to PostgreSQL once you are happy with the flow.
- Chat remains based on your existing group foundation and can be extended next.
