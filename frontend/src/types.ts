export interface UserProfile {
  id?: number;
  fullName: string;
  email?: string;
  school: string;
  major?: string;
  academicLevel?: string;
  studyStyle?: string;
  preferredMode?: string;
  courses: string[];
  state?: string;
  city?: string;
  bio?: string;
  locationVisible?: boolean;
  profileImageUrl?: string;
}

export interface MatchResult {
  user: UserProfile;
  score: number;
  reasons: string[];
}

export interface AuthResponse {
  token: string;
  user: UserProfile;
}

export interface ReferenceOption {
  label: string;
}

export interface StateOption {
  code: string;
  label: string;
}

export interface ConnectionRequestDto {
  id: number;
  sender: UserProfile;
  receiver: UserProfile;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  createdAt: string;
  respondedAt?: string;
}

export interface FriendDto {
  user: UserProfile;
  connectedAt: string;
  sharedCourses: string[];
}

export interface StudySessionDto {
  id: number;
  host: UserProfile;
  participants: UserProfile[];
  title: string;
  course?: string;
  topic?: string;
  description?: string;
  startTime: string;
  endTime?: string;
  location?: string;
  mode: string;
  maxParticipants: number;
  currentParticipantCount: number;
  status: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  createdAt: string;
  isHost: boolean;
  isParticipant: boolean;
  isFull: boolean;
}
