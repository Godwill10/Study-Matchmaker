import { Link } from 'react-router-dom';
import { Sparkles, Users, BookOpen, Target, Zap, Shield } from 'lucide-react';

const FEATURES = [
  {
    icon: Target,
    title: 'Smart Matching',
    desc: 'Our algorithm pairs you with students who share your courses, study style, and academic goals.',
  },
  {
    icon: Users,
    title: 'Campus Network',
    desc: 'Connect with peers at your university or across the country. Build your study circle.',
  },
  {
    icon: BookOpen,
    title: 'Course-Based Discovery',
    desc: 'Find partners taking the same classes. Filter by major, level, and preferred study mode.',
  },
  {
    icon: Zap,
    title: 'Instant Compatibility',
    desc: 'See match scores and reasons instantly so you know exactly why someone is a good fit.',
  },
  {
    icon: Shield,
    title: 'Verified Students',
    desc: 'Every account is tied to a real university. Study with students you can trust.',
  },
  {
    icon: Sparkles,
    title: 'Free Forever',
    desc: 'StudyMatch is completely free for all students. No premium tiers, no paywalls.',
  },
];

export default function Landing() {
  return (
    <>
      <div className="landing-hero">
        <nav className="landing-nav">
          <div className="landing-nav-brand">
            <Sparkles size={22} />
            StudyMatch
          </div>
          <div className="landing-nav-actions">
            <Link to="/login" className="btn-outline-white">
              Log in
            </Link>
            <Link to="/signup" className="btn-white">
              Get Started
            </Link>
          </div>
        </nav>

        <div className="landing-content">
          <div className="landing-content-inner">
            <h1>Find Your Perfect Study Partner</h1>
            <p>
              StudyMatch connects college students with compatible study partners
              based on courses, learning style, and schedule. Stop studying alone.
            </p>
            <div className="landing-cta">
              <Link to="/signup" className="btn-white" style={{ padding: '14px 32px', fontSize: 16 }}>
                Create Free Account
              </Link>
              <Link to="/login" className="btn-outline-white" style={{ padding: '14px 32px', fontSize: 16 }}>
                I Have an Account
              </Link>
            </div>
          </div>
        </div>
      </div>

      <section className="landing-features">
        <div className="landing-features-inner">
          <h2>Why StudyMatch?</h2>
          <p>Everything you need to find the right study partner, all in one place.</p>
          <div className="features-grid">
            {FEATURES.map((f) => (
              <div key={f.title} className="feature-card">
                <div className="feature-icon">
                  <f.icon size={22} />
                </div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}
