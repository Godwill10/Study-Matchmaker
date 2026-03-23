import { useEffect, useState } from "react";
import API from "../api/api";

interface Option {
    label: string;
}

interface StateOption {
    code: string;
    label: string;
}

export default function RegistrationForm() {

    const [form, setForm] = useState<any>({
        fullName: "",
        email: "",
        password: "",
        state: "",
        university: "",
        major: "",
        academicLevel: "",
        studyStyle: "",
        preferredMode: "Hybrid",
        city: "",
        bio: "",
        courses: [],
        locationVisible: true,
    });

    const [states, setStates] = useState<StateOption[]>([]);
    const [universities, setUniversities] = useState<string[]>([]);
    const [majors, setMajors] = useState<Option[]>([]);
    const [levels, setLevels] = useState<Option[]>([]);
    const [studyStyles, setStudyStyles] = useState<Option[]>([]);
    const [courses, setCourses] = useState<Option[]>([]);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchStates();
        fetchMajors();
        fetchLevels();
        fetchStudyStyles();
    }, []);

    useEffect(() => {
        if (form.state) fetchUniversities(form.state);
    }, [form.state]);

    useEffect(() => {
        if (form.academicLevel) fetchCourses(form.academicLevel);
    }, [form.academicLevel]);

    const fetchStates = async () => {
        const res = await API.get("/reference/states");
        setStates(res.data);
    };

    const fetchUniversities = async (state: string) => {
        const res = await API.get(`/reference/universities?state=${state}`);
        setUniversities(res.data);
    };

    const fetchMajors = async () => {
        const res = await API.get("/reference/majors");
        setMajors(res.data);
    };

    const fetchLevels = async () => {
        const res = await API.get("/reference/academic-levels");
        setLevels(res.data);
    };

    const fetchStudyStyles = async () => {
        const res = await API.get("/reference/study-styles");
        setStudyStyles(res.data);
    };

    const fetchCourses = async (level: string) => {
        const res = await API.get(`/reference/courses?academicLevel=${level}`);
        console.log("COURSES DATA:", res.data); // 👈 ADD THIS
        setCourses(res.data);
    };

    const handleChange = (e: any) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleCourseToggle = (course: string) => {
        if (form.courses.includes(course)) {
            setForm({
                ...form,
                courses: form.courses.filter((c: string) => c !== course),
            });
        } else {
            setForm({
                ...form,
                courses: [...form.courses, course],
            });
        }
    };

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        setError("");

        if (!form.fullName || !form.email || !form.password) {
            setError("Please fill all required fields");
            return;
        }

        if (!form.state || !form.university || !form.major) {
            setError("Please complete all dropdown selections");
            return;
        }

        if (!form.academicLevel || !form.studyStyle) {
            setError("Please select academic level and study style");
            return;
        }

        if (form.courses.length === 0) {
            setError("Please select at least one course");
            return;
        }

        const payload = {
            ...form,
            school: form.university,
        };

        try {
            await API.post("/auth/register", payload);
            alert("Account created! You can now login.");
        } catch (err: any) {
            setError(err.response?.data?.message || "Registration failed");
        }
    };

    return (
        <form onSubmit={handleSubmit} className="card">

            {error && <div className="error">{error}</div>}

            <input name="fullName" placeholder="Full name" value={form.fullName} onChange={handleChange}/>
            <input name="email" placeholder="Email" value={form.email} onChange={handleChange}/>
            <input type="password" name="password" placeholder="Password" value={form.password}
                   onChange={handleChange}/>

            {/* STATE */}
            <select name="state" value={form.state} onChange={handleChange}>
                <option value="">Select state</option>
                {states.map(s => (
                    <option key={s.code} value={s.code}>{s.label}</option>
                ))}
            </select>

            {/* UNIVERSITY */}
            <select name="university" value={form.university} onChange={handleChange}>
                <option value="">Select university</option>
                {universities.map(u => (
                    <option key={u} value={u}>{u}</option>
                ))}
            </select>

            {/* MAJOR */}
            <select name="major" value={form.major} onChange={handleChange}>
                <option value="">Select major</option>
                {majors.map(m => (
                    <option key={m.label} value={m.label}>{m.label}</option>
                ))}
            </select>

            {/* LEVEL */}
            <select name="academicLevel" value={form.academicLevel} onChange={handleChange}>
                <option value="">Select academic level</option>
                {levels.map(l => (
                    <option key={l.label} value={l.label}>{l.label}</option>
                ))}
            </select>

            {/* STUDY STYLE */}
            <select name="studyStyle" value={form.studyStyle} onChange={handleChange}>
                <option value="">Select study style</option>
                {studyStyles.map(s => (
                    <option key={s.label} value={s.label}>{s.label}</option>
                ))}
            </select>

            {/* COURSES */}
            <div>
                <p>Select courses</p>

                {courses.map((c: any, index) => {
                    const label = c.label || c.name || c; // ✅ fallback protection

                    return (
                        <label
                            key={label || index}
                            style={{display: "block", marginBottom: "6px"}}
                        >
                            <input
                                type="checkbox"
                                checked={form.courses.includes(label)}
                                onChange={() => handleCourseToggle(label)}
                            />
                            {" "}{label}
                        </label>
                    );
                })}
            </div>



            <button type="submit">Create account</button>
        </form>
    );
}