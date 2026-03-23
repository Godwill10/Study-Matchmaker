import { Link } from 'react-router-dom';
import type { MatchResult } from '../types';

export default function MatchResults({ matches }: { matches: MatchResult[] }) {
  if (!matches?.length) return <div className="matches-empty"><h3>No matches found</h3><p>Update your courses or profile to get better recommendations.</p></div>;
  return <div className="matches-container">{matches.map((match, index) => <div key={match.user.id ?? index} className="match-card"><div className="match-header"><h2>{match.user.fullName}</h2><span className="score">🔥 {Math.round(match.score)}%</span></div><div className="match-details"><p><strong>University:</strong> {match.user.school}</p><p><strong>Major:</strong> {match.user.major || 'N/A'}</p><p><strong>Academic level:</strong> {match.user.academicLevel || 'N/A'}</p><p><strong>Study style:</strong> {match.user.studyStyle || 'N/A'}</p><p><strong>Courses:</strong> {(match.user.courses || []).join(', ')}</p>{match.user.locationVisible && <p><strong>Location:</strong> {[match.user.city, match.user.state].filter(Boolean).join(', ') || 'Visible'}</p>}</div><div className="match-reasons"><h4>Why this is a good match:</h4><ul>{match.reasons.map((reason, i) => <li key={i}>✅ {reason}</li>)}</ul></div><div className="match-actions"><Link className="btn primary" to={`/students/${match.user.id}`}>View Profile</Link></div></div>)}</div>;
}
